"""Gate for the evidence pack (evidence/FORMAT.md): every claim is well formed, worded as a finding,
cites only sources from the two reports' verified id lists, and says only what the report it cites says.

    python tools/evidence_check.py                  # check evidence/claims.json
    python tools/evidence_check.py --research DIR   # DIR/research_notes/*/ids_check.json, DIR/reports/*.md
    python tools/evidence_check.py --ids FILE [--ids FILE ...] --reports DIR
    python tools/evidence_check.py --skip-content   # no report text at hand: check everything else
    python tools/evidence_check.py --selftest       # plant each defect class, require each to fail

The id lists and reports are local (they are not in the public repository). Their folder comes from
--research, else $SISYPHUS_RESEARCH_DIR, else research/ at the repository root. Feature ids are checked
against SPEC.md. Exit 0 = pass, 1 = findings, 2 = an id list, a report, SPEC.md or the pack cannot be
read. Standard library only.
"""
import argparse
import copy
import json
import os
import pathlib
import re
import sys
import unicodedata

ROOT = pathlib.Path(__file__).resolve().parent.parent
PACK = ROOT / "evidence" / "claims.json"
SPEC = ROOT / "SPEC.md"
REPORT_FILES = {1: "Exercise evidence for daily training.md", 2: "Exercise evidence for health and longevity.md"}

LANGS = ("en",)  # first draft is English only; Italian and its checks come in a separate pass
MODULES = {"shared", "strength", "stamina", "daily_movement", "mobility_balance", "body_measures"}
LEVELS = {"high", "moderate", "limited", "consensus"}
INDEPENDENCE = {"IND", "IND-decl", "UNK", "COMM"}
DESIGN_CODE = re.compile(r"^(Guide|Cons|UR|CR|NMA|MA|IPD-MA|SR|RCT|XO|Obs|Mech|Narr)( of (Obs|XO|RCT|Mech))?$")
ID_RE = re.compile(r"^[a-z][a-z0-9_]*(\.[a-z0-9_]+){2}$")
RULE_RE = re.compile(r"^(\d+)(?:-(\d+))?$")
RULE_RANGE = {1: (1, 31), 2: (1, 63)}  # Report 2 holds 32-63 and updates to 1-31

TOP_KEYS = {"format": int, "pack_version": int, "labels": dict, "claims": list}
CLAIM_KEYS = {  # name: (type, required)
    "id": (str, True), "version": (int, True), "held": (str, False), "module": (str, True), "features": (list, True),
    "report_rules": (list, True), "claim": (dict, True), "body": (dict, False), "population": (dict, True),
    "design": (dict, True), "certainty": (dict, True), "grade": (str, True), "status": (str, True),
    "observational": (bool, True), "sources": (list, True),
}
SOURCE_KEYS = {"author": (str, True), "year": (int, True), "pmid": (str, False), "doi": (str, False),
               "coi": (dict, False), "independence": (str, False)}
TEXT_FIELDS = ("claim", "body", "population", "design", "certainty")  # text fields shown to the person

# Instructions, promises, second person (never about the person, §7 rule 7) and weight-judging words.
BANNED = {
    "en": ["should", "must", "need to", "needs to", "have to", "has to", "make sure", "try to", "we recommend",
           "avoid", "guarantee", "guaranteed", "guarantees", "definitely", "will", "recommended", "ought to",
           "you", "your", "yours", "you're", "overweight", "obese", "obesity", "bmi", "fat"],
}
ASSOC = {"en": re.compile(r"\bassociat", re.I)}
BANNED_RE = {lang: [(w, re.compile(r"(?<!\w)" + re.escape(w) + r"(?!\w)", re.I)) for w in words]
             for lang, words in BANNED.items()}


def numbers(text):
    """The numbers in an English text, thousands separators dropped: '15,827 and 4.7' -> {'15827', '4.7'}."""
    return set(re.findall(r"\d+(?:\.\d+)?", re.sub(r"(?<=\d),(?=\d{3}\b)", "", text)))


def spec_features(text):
    """Feature ids SPEC.md names: harvest rows (FA15), SPEC features (X-WEEK, N-HISTORY), decisions (0.24)."""
    return (set(re.findall(r"\b[A-Z]{2}\d{1,2}\b", text)) | set(re.findall(r"\b[XN]-[A-Z]+\b", text))
            | set(re.findall(r"^\| (0\.\d+) \|", text, re.M)))


def norm_name(s):
    s = unicodedata.normalize("NFKD", s or "")
    return "".join(c for c in s if not unicodedata.combining(c)).lower().split()


class Ids:
    """The union of the reports' verified id lists (written by research/verify_ids.py)."""

    def __init__(self, docs):
        self.pmids, self.dois, self.bad = {}, set(), set()
        for d in docs:
            self.pmids.update({k: v for k, v in d.get("pmids", {}).items() if v})
            self.dois.update(k.lower() for k, ok in d.get("dois", {}).items() if ok)
            self.bad.update(d.get("bad_pmids", []))
            self.bad.update(x.lower() for x in d.get("bad_dois", []))

    @classmethod
    def load(cls, paths):
        return cls([json.loads(pathlib.Path(p).read_text(encoding="utf-8")) for p in paths])


def check_keys(obj, spec, where, out):
    for k in obj:
        if k not in spec:
            out.append(f"{where}: unknown field '{k}'")
    for k, (typ, req) in spec.items():
        if k not in obj:
            if req:
                out.append(f"{where}: missing field '{k}'")
        elif not isinstance(obj[k], typ) or (typ is int and isinstance(obj[k], bool)):
            out.append(f"{where}: '{k}' must be {typ.__name__}")


def check_text_object(obj, where, out, extra=()):
    if not isinstance(obj, dict):
        return
    for k in obj:
        if k not in LANGS and k not in extra:
            out.append(f"{where}: unknown field '{k}'")
    for lang in LANGS:
        v = obj.get(lang)
        if not isinstance(v, str) or not v.strip():
            out.append(f"{where}: missing {lang} text")


def check_text(obj, where, out):
    texts = {lang: obj[lang] for lang in LANGS if isinstance(obj.get(lang), str)}
    for lang, t in texts.items():
        for word, rx in BANNED_RE[lang]:
            if rx.search(t):
                out.append(f"{where}.{lang}: banned word '{word}'")


def check_source(s, where, ids, out):
    if not isinstance(s, dict):
        out.append(f"{where}: must be an object")
        return
    check_keys(s, SOURCE_KEYS, where, out)
    pmid, doi = s.get("pmid"), (s.get("doi") or "").lower() or None
    if not pmid and not doi:
        out.append(f"{where}: no pmid or doi")
    if "independence" in s and s["independence"] not in INDEPENDENCE:
        out.append(f"{where}: independence '{s['independence']}' not one of {sorted(INDEPENDENCE)}")
    if "coi" in s:
        check_text_object(s["coi"], where + ".coi", out)
        if isinstance(s["coi"], dict):
            check_text(s["coi"], where + ".coi", out)
    if pmid:
        rec = ids.pmids.get(pmid)
        if pmid in ids.bad or rec is None:
            out.append(f"{where}: PMID {pmid} is not in a verified id list")
        else:
            if str(s.get("year")) != str(rec.get("year")):
                out.append(f"{where}: PMID {pmid} year {s.get('year')} != PubMed {rec.get('year')}")
            a, b = norm_name(s.get("author")), norm_name(rec.get("first_author"))
            if not a or not b or a[0] != b[0]:
                out.append(f"{where}: PMID {pmid} author '{s.get('author')}' != PubMed '{rec.get('first_author')}'")
            if doi and rec.get("doi") and doi != rec["doi"].lower():
                out.append(f"{where}: DOI {doi} != PubMed's {rec['doi']} for PMID {pmid}")
    if doi and (doi in ids.bad or doi not in ids.dois) and not (pmid and pmid in ids.pmids):
        out.append(f"{where}: DOI {doi} is not in a verified id list")


def check_content(c, where, reports, out):
    """§7 rule 6: the grade, status, sources and every number shown come from the report(s) the claim cites."""
    cited = sorted({r["report"] for r in c.get("report_rules", [])
                    if isinstance(r, dict) and r.get("report") in reports})
    if not cited:
        return
    text = "\n".join(reports[n] for n in cited)
    name = "Report " + " or ".join(str(n) for n in cited)
    for f in ("grade", "status"):
        if isinstance(c.get(f), str) and c[f] and c[f] not in text:
            out.append(f"{where}: {f} '{c[f]}' is not verbatim in {name}")
    have = numbers(text)
    shown = [(f, c[f]) for f in TEXT_FIELDS if isinstance(c.get(f), dict)]
    for j, s in enumerate(c.get("sources") or []):
        if not isinstance(s, dict):
            continue
        key = s.get("pmid") or s.get("doi")
        if isinstance(key, str) and key and key.lower() not in text.lower():
            out.append(f"{where}.sources[{j}]: {key} is not cited in {name}")
        if isinstance(s.get("coi"), dict):
            shown.append((f"sources[{j}].coi", s["coi"]))
    for f, obj in shown:
        for lang in LANGS:
            if isinstance(obj.get(lang), str):
                missing = sorted(numbers(obj[lang]) - have)
                if missing:
                    out.append(f"{where}.{f}.{lang}: number(s) {', '.join(missing)} not in {name}")


def check_pack(pack, ids, features, reports=None):
    """All findings for a parsed pack, as 'where: what' strings; empty = pass. features: the valid feature
    ids (from SPEC.md); reports: {1: text, 2: text}, or None to skip the content check."""
    out = []
    if not isinstance(pack, dict):
        return ["pack: must be an object"]
    check_keys(pack, {k: (t, True) for k, t in TOP_KEYS.items()}, "pack", out)
    labels = pack.get("labels") if isinstance(pack.get("labels"), dict) else {}
    check_text_object(labels.get("observational"), "labels.observational", out)
    if labels.get("observational") is None:
        out.append("labels: missing 'observational'")
    lv = labels.get("certainty_level") if isinstance(labels.get("certainty_level"), dict) else {}
    for level in sorted(LEVELS):
        if level not in lv:
            out.append(f"labels.certainty_level: missing '{level}'")
        else:
            check_text_object(lv[level], f"labels.certainty_level.{level}", out)
    seen = set()
    claims = pack.get("claims") if isinstance(pack.get("claims"), list) else []
    if not claims:
        out.append("pack: no claims")
    for i, c in enumerate(claims):
        if not isinstance(c, dict):
            out.append(f"claims[{i}]: must be an object")
            continue
        cid = c.get("id")
        where = cid if isinstance(cid, str) and cid else f"claims[{i}]"
        check_keys(c, CLAIM_KEYS, where, out)
        if not isinstance(cid, str) or not ID_RE.match(cid):
            out.append(f"{where}: missing or malformed id (module.topic.name)")
        elif cid in seen:
            out.append(f"{where}: duplicate id")
        seen.add(cid)
        if isinstance(c.get("version"), int) and c["version"] < 1:
            out.append(f"{where}: version must be >= 1")
        if c.get("module") not in MODULES:
            out.append(f"{where}: module '{c.get('module')}' not one of {sorted(MODULES)}")
        feats = c.get("features")
        if isinstance(feats, list) and (not feats or not all(isinstance(f, str) and f for f in feats)):
            out.append(f"{where}: features must be a non-empty list of strings")
        elif isinstance(feats, list):
            for f in feats:
                if f not in features:
                    out.append(f"{where}: feature '{f}' is not named in SPEC.md")
        rules = c.get("report_rules")
        if isinstance(rules, list):
            if not rules:
                out.append(f"{where}: no report_rules")
            for r in rules:
                ok = isinstance(r, dict) and set(r) == {"report", "rule"} and r["report"] in RULE_RANGE \
                    and isinstance(r["rule"], str) and RULE_RE.match(r["rule"])
                if not ok:
                    out.append(f"{where}: report_rules entry {r!r} must be {{report: 1|2, rule: 'n' or 'n-m'}}")
                    continue
                lo, hi = RULE_RANGE[r["report"]]
                m = RULE_RE.match(r["rule"])
                a, b = int(m.group(1)), int(m.group(2) or m.group(1))
                if not (lo <= a <= b <= hi):
                    out.append(f"{where}: rule {r['rule']} is outside Report {r['report']} ({lo}-{hi})")
        for f in TEXT_FIELDS:
            if f not in c:
                continue
            extra = {"design": ("codes",), "certainty": ("level",)}.get(f, ())
            check_text_object(c[f], f"{where}.{f}", out, extra)
            if isinstance(c[f], dict):
                check_text(c[f], f"{where}.{f}", out)
        design = c.get("design") if isinstance(c.get("design"), dict) else {}
        codes = design.get("codes")
        if "design" in c and (not isinstance(codes, list) or not codes
                              or not all(isinstance(x, str) and DESIGN_CODE.match(x) for x in codes)):
            out.append(f"{where}.design: codes must be a non-empty list of report design codes")
            codes = []
        cert = c.get("certainty") if isinstance(c.get("certainty"), dict) else {}
        if "certainty" in c and cert.get("level") not in LEVELS:
            out.append(f"{where}.certainty: level '{cert.get('level')}' not one of {sorted(LEVELS)}")
        for f in ("grade", "status"):
            if isinstance(c.get(f), str) and not c[f].strip():
                out.append(f"{where}: {f} must be the report's own text, not empty")
        if isinstance(c.get("held"), str) and not c["held"].strip():
            out.append(f"{where}: held must say what the claim waits for")
        obs_design = any("Obs" in x for x in codes or [] if isinstance(x, str))
        if obs_design and c.get("observational") is not True:
            out.append(f"{where}: observational design {codes} without observational: true")
        if c.get("observational") is True:
            parts = [c[f] for f in ("claim", "body") if isinstance(c.get(f), dict)]
            for lang in LANGS:
                said = " ".join(p.get(lang, "") for p in parts if isinstance(p.get(lang, ""), str))
                if not ASSOC[lang].search(said):
                    out.append(f"{where}: observational, but claim/body.{lang} has no association wording")
        srcs = c.get("sources")
        if isinstance(srcs, list):
            if not srcs:
                out.append(f"{where}: no sources")
            for j, s in enumerate(srcs):
                check_source(s, f"{where}.sources[{j}]", ids, out)
        if reports is not None:
            check_content(c, where, reports, out)
    return out


def find_id_lists(research):
    return sorted(pathlib.Path(research).glob("research_notes/*/ids_check.json"))


# ------------------------------------------------------------------ self-test
FIXTURE_IDS = Ids([{
    "pmids": {"19204579": {"year": "2009", "first_author": "American College of Sports Medicine",
                           "doi": "10.1249/MSS.0b013e3181915670"},
              "35788615": {"year": "2022", "first_author": "Dos Santos M", "doi": "10.1001/jamainternmed.2022.2488"},
              "26243014": {"year": "2015", "first_author": "Milanović Z", "doi": "10.1007/s40279-015-0365-0"},
              "11111111": {"year": "2020", "first_author": "Bad X", "doi": None}},
    "dois": {"10.1002/ejsp.674": True, "10.9999/dead": False},
    "bad_pmids": ["11111111"], "bad_dois": [],
}])
FIXTURE_FEATURES = {"LL5", "X-WEEK"}
FIXTURE_REPORTS = {
    1: "ACSM 2009 raised load by 2-10% ([ACSM 2009, PMID 19204579]) {Guide (consensus) · P1 · C2} | Inference |",
    2: "In 15,827 adults, 4.7 years (PMID 35788615; PMID 26243014; doi:10.1002/ejsp.674) "
       "{MA of Obs · P3 · C3 · M3; Obs} | Strong |",
}

CLEAN = {
    "format": 1, "pack_version": 1,
    "labels": {"observational": {"en": "Association, not cause"},
               "certainty_level": {k: {"en": k} for k in sorted(LEVELS)}},
    "claims": [
        {"id": "strength.progression.double", "version": 1, "held": "SPEC §11", "module": "strength",
         "features": ["LL5"],
         "report_rules": [{"report": 1, "rule": "6"}],
         # 'shoulder', 'mustard', 'fatigue', 'willing': banned words inside longer words must not trip the gate
         "claim": {"en": "A guideline (2009) describes a 2–10% load increase, also for shoulder work and fatigue "
                         "in willing adults."},
         "population": {"en": "Healthy adults"},
         "design": {"codes": ["Guide"], "en": "Guideline"},
         "certainty": {"level": "consensus", "en": "Expert opinion"},
         "grade": "{Guide (consensus) · P1 · C2}", "status": "Inference", "observational": False,
         "sources": [{"author": "American College of Sports Medicine", "year": 2009, "pmid": "19204579",
                      "doi": "10.1249/mss.0b013e3181915670"}]},
        {"id": "shared.week.totals", "version": 1, "module": "shared", "features": ["X-WEEK"],
         "report_rules": [{"report": 2, "rule": "1-4"}],
         "claim": {"en": "In 15,827 adults, activity was associated with 4.7 more years."},
         "body": {"en": "No mustard."},
         "population": {"en": "Adults"},
         "design": {"codes": ["MA of Obs"], "en": "Meta-analysis of observational studies"},
         "certainty": {"level": "high", "en": "Good"},
         "grade": "{MA of Obs · P3 · C3 · M3; Obs}", "status": "Strong", "observational": True,
         "sources": [{"author": "dos Santos", "year": 2022, "pmid": "35788615"},
                     {"author": "Milanović", "year": 2015, "pmid": "26243014", "independence": "IND",
                      "coi": {"en": "None"}},
                     {"author": "Lally", "year": 2010, "doi": "10.1002/ejsp.674"}]},
    ],
}


def _set(path, value):
    """A mutation: set (or delete, with value=...) the item at path in a copy of CLEAN."""
    def f(p):
        obj = p
        for k in path[:-1]:
            obj = obj[k]
        if value is ...:
            del obj[path[-1]]
        else:
            obj[path[-1]] = value
    return f


C0, C1 = ["claims", 0], ["claims", 1]
DEFECTS = [  # (defect class, mutation, a substring the finding must contain)
    ("unknown PMID", _set(C0 + ["sources", 0, "pmid"], "99999999"), "not in a verified id list"),
    ("PMID marked bad", _set(C0 + ["sources", 0, "pmid"], "11111111"), "not in a verified id list"),
    ("unverified DOI", _set(C1 + ["sources", 2, "doi"], "10.9999/dead"), "not in a verified id list"),
    ("source without id", _set(C1 + ["sources", 2, "doi"], ...), "no pmid or doi"),
    ("wrong year", _set(C0 + ["sources", 0, "year"], 2010), "year"),
    ("wrong first author", _set(C1 + ["sources", 0, "author"], "Smith"), "author"),
    ("DOI differs from PubMed's", _set(C0 + ["sources", 0, "doi"], "10.1002/ejsp.674"), "!= PubMed's"),
    ("no sources", _set(C0 + ["sources"], []), "no sources"),
    ("missing en claim", _set(C0 + ["claim", "en"], ...), "missing en text"),
    ("empty en claim", _set(C0 + ["claim", "en"], "  "), "missing en text"),
    ("unknown language", _set(C0 + ["claim", "fr"], "Une ligne directrice."), "unknown field 'fr'"),
    ("Italian before its pass", _set(C0 + ["claim", "it"], "Una linea guida."), "unknown field 'it'"),
    ("missing en population", _set(C0 + ["population", "en"], ...), "missing en text"),
    ("missing population", _set(C0 + ["population"], ...), "missing field 'population'"),
    ("missing certainty", _set(C0 + ["certainty"], ...), "missing field 'certainty'"),
    ("certainty without en text", _set(C0 + ["certainty", "en"], ...), "missing en text"),
    ("invalid certainty level", _set(C0 + ["certainty", "level"], "sure"), "level 'sure'"),
    ("banned en instruction", _set(C0 + ["claim", "en"], "You should raise the load by 2–10% (2009)."),
     "banned word 'should'"),
    ("banned en phrase", _set(C0 + ["body"], {"en": "We recommend it."}), "'we recommend'"),
    ("about the person", _set(C1 + ["body", "en"], "You may see this."), "banned word 'you'"),
    ("about the person's data", _set(C1 + ["body", "en"], "Your glucose."), "banned word 'your'"),
    ("weight-judging word", _set(C1 + ["population", "en"], "Obese adults"), "banned word 'obese'"),
    ("banned word in a coi note", _set(C1 + ["sources", 1, "coi", "en"], "You must know"), "banned word"),
    ("claim without id", _set(C0 + ["id"], ...), "missing or malformed id"),
    ("malformed id", _set(C0 + ["id"], "Strength progression"), "malformed id"),
    ("duplicate id", _set(C1 + ["id"], "strength.progression.double"), "duplicate id"),
    ("text outside a claim field", _set(C0 + ["note"], {"en": "x"}), "unknown field 'note'"),
    ("text outside the claims", _set(["notes"], [{"en": "x"}]), "unknown field 'notes'"),
    ("observational design without flag", _set(C1 + ["observational"], False), "without observational: true"),
    ("observational without association wording",
     _set(C1 + ["claim"], {"en": "In 15,827 adults, 4.7 more years."}),
     "no association wording"),
    ("missing observational label", _set(["labels", "observational"], ...), "missing 'observational'"),
    ("wrong type", _set(C0 + ["version"], "1"), "'version' must be int"),
    ("boolean as int", _set(C0 + ["version"], True), "'version' must be int"),
    ("invalid module", _set(C0 + ["module"], "cardio"), "module 'cardio'"),
    ("invalid design code", _set(C0 + ["design", "codes"], ["Blog"]), "codes must be"),
    ("rule out of range", _set(C0 + ["report_rules"], [{"report": 1, "rule": "40"}]), "outside Report 1"),
    ("malformed rule", _set(C0 + ["report_rules"], [{"report": 3, "rule": "6"}]), "report_rules entry"),
    ("no features", _set(C0 + ["features"], []), "features must be"),
    ("empty grade", _set(C0 + ["grade"], "  "), "grade must be the report's own text"),
    ("held without a reason", _set(C0 + ["held"], " "), "held must say what the claim waits for"),
    ("held not text", _set(C0 + ["held"], True), "'held' must be str"),
    ("banned promise", _set(C0 + ["claim", "en"], "Lifting will raise the load by 2–10% (2009)."), "'will'"),
    ("banned 'recommended'", _set(C0 + ["body"], {"en": "Holds are recommended."}), "'recommended'"),
    ("banned 'ought to'", _set(C0 + ["body"], {"en": "Load ought to rise."}), "'ought to'"),
    ("feature not in SPEC.md", _set(C0 + ["features"], ["LL5", "ZZ9"]), "feature 'ZZ9' is not named in SPEC.md"),
    ("number not in the cited report", _set(C0 + ["claim", "en"], "A guideline (2009) describes a 2–12% increase."),
     "number(s) 12 not in Report 1"),
    ("number not in the cited report (population)", _set(C1 + ["population", "en"], "Adults aged 18–45"),
     "not in Report 2"),
    ("number in a coi note not in the report", _set(C1 + ["sources", 1, "coi", "en"], "Funded 7 times"),
     "coi.en: number(s) 7"),
    ("grade not verbatim", _set(C0 + ["grade"], "{Guide · P1 · C2}"), "grade '{Guide · P1 · C2}' is not verbatim"),
    ("status not verbatim", _set(C1 + ["status"], "Strong for everything"), "status 'Strong for everything'"),
    ("source cited only in the other report",
     _set(C0 + ["sources"], [{"author": "dos Santos", "year": 2022, "pmid": "35788615"}]),
     "35788615 is not cited in Report 1"),
    ("invalid independence", _set(C1 + ["sources", 1, "independence"], "maybe"), "independence 'maybe'"),
    ("not an object", lambda p: p["claims"].__setitem__(0, "text"), "must be an object"),
]


def selftest():
    failures = 0
    clean = check_pack(copy.deepcopy(CLEAN), FIXTURE_IDS, FIXTURE_FEATURES, FIXTURE_REPORTS)
    print(f"  {'PASS' if not clean else 'FAIL'}  clean case passes" + ("" if not clean else f": {clean}"))
    failures += bool(clean)
    for name, mutate, expect in DEFECTS:
        pack = copy.deepcopy(CLEAN)
        mutate(pack)
        found = check_pack(pack, FIXTURE_IDS, FIXTURE_FEATURES, FIXTURE_REPORTS)
        hit = [f for f in found if expect in f]
        print(f"  {'PASS' if hit else 'FAIL'}  {name}: " + (hit[0] if hit else f"not caught (got {found})"))
        failures += not hit
    if not {"FA15", "X-WEEK", "N-HISTORY", "0.24"} <= spec_features("| 0.24 | x |\nFA15, X-WEEK and N-HISTORY") \
            or spec_features("P3 · C3, BMI, 6T, 0.5 sets"):
        print("  FAIL  SPEC.md feature-id reading")
        failures += 1
    print(f"selftest: {len(DEFECTS)} defect classes + clean case, {failures} failure(s)")
    return 1 if failures else 0


def main(argv=None):
    ap = argparse.ArgumentParser(description=__doc__.split("\n\n")[0])
    ap.add_argument("--pack", default=str(PACK), help="the pack to check (default evidence/claims.json)")
    ap.add_argument("--research", help="the local research folder (default $SISYPHUS_RESEARCH_DIR, else research/)")
    ap.add_argument("--ids", action="append", help="an ids_check.json file; repeat for each (overrides --research)")
    ap.add_argument("--reports", help="the folder holding the two report .md files (default <research>/reports)")
    ap.add_argument("--skip-content", action="store_true",
                    help="do not check claims against the report text (reported, never a full pass)")
    ap.add_argument("--selftest", action="store_true")
    a = ap.parse_args(argv)
    if a.selftest:
        return selftest()
    research = pathlib.Path(a.research or os.environ.get("SISYPHUS_RESEARCH_DIR") or ROOT / "research")
    if a.ids:
        lists = [pathlib.Path(p) for p in a.ids]
    else:
        lists = find_id_lists(research)
        if not lists:
            print(f"evidence_check: no research_notes/*/ids_check.json under {research}; "
                  "pass --research DIR or set SISYPHUS_RESEARCH_DIR", file=sys.stderr)
            return 2
    missing = [str(p) for p in lists if not p.is_file()]
    if missing:
        print(f"evidence_check: id list not found: {', '.join(missing)}", file=sys.stderr)
        return 2
    try:
        pack = json.loads(pathlib.Path(a.pack).read_text(encoding="utf-8"))
    except (OSError, json.JSONDecodeError) as e:
        print(f"evidence_check: cannot read {a.pack}: {e}", file=sys.stderr)
        return 2
    try:
        features = spec_features(SPEC.read_text(encoding="utf-8"))
    except OSError as e:
        print(f"evidence_check: cannot read {SPEC}: {e}", file=sys.stderr)
        return 2
    reports = None
    if not a.skip_content:
        folder = pathlib.Path(a.reports) if a.reports else research / "reports"
        try:
            reports = {n: (folder / f).read_text(encoding="utf-8") for n, f in REPORT_FILES.items()}
        except OSError as e:
            print(f"evidence_check: cannot read a report under {folder}: {e}; pass --reports DIR, "
                  "or --skip-content to check everything else", file=sys.stderr)
            return 2
    ids = Ids.load(lists)
    findings = check_pack(pack, ids, features, reports)
    claims = pack.get("claims", []) if isinstance(pack, dict) else []
    n_src = sum(len(c.get("sources", [])) for c in claims if isinstance(c, dict))
    n_held = sum(1 for c in claims if isinstance(c, dict) and "held" in c)
    print(f"evidence_check: {len(claims)} claims ({n_held} held), {n_src} source citations, "
          f"{len(lists)} id lists ({len(ids.pmids)} PMIDs, {len(ids.dois)} DOIs), "
          f"{len(features)} SPEC feature ids, report text {'checked' if reports else 'NOT checked'}")
    for f in findings:
        print("  " + f)
    if not findings and reports is None:
        print("PASS without the content check (--skip-content)")
        return 0
    print("PASS" if not findings else f"FAIL: {len(findings)} finding(s)")
    return 1 if findings else 0


if __name__ == "__main__":
    if hasattr(sys.stdout, "reconfigure"):
        sys.stdout.reconfigure(encoding="utf-8")  # claim text and author names are not cp1252-safe
    sys.exit(main())
