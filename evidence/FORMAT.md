# Evidence pack: format

The evidence pack is the only source of text for SisyphusFit's evidence notes (SPEC §7, decisions 0.7,
0.8, 0.15-0.17, 0.30). It is versioned data shipped with the app, read offline. Every claim comes from
one of the two local evidence reports, whose citations were machine-checked against PubMed and doi.org:

- Report 1, *Exercise evidence for daily training*: rules 1-31.
- Report 2, *Exercise evidence for health and longevity*: rules 32-63, plus updates to rules 1-31.

The reports stay local (the repository is public); the pack carries what the app shows plus enough
of the reports' own grading to trace each claim back. `tools/evidence_check.py` is the gate.

**Language: this first draft is English only.** Every text object holds `en` and nothing else; the gate
rejects any other language key. Italian (the app shows notes in the phone's language) is a separate,
later pass: it adds the Italian text and its checks (wording rules for Italian, numbers matching the
English) to this format and to the gate together.

## File: `evidence/claims.json`

```json
{
  "format": 2,
  "pack_version": 2,
  "labels": { "observational": {"en": "..."}, "certainty_level": { "<level>": {"en": "..."} } },
  "claims": [ { ...claim... } ]
}
```

- `format`: the schema version of this file (this document). A change to the fields bumps it
  (2 added `held`).
- `pack_version`: bumped whenever any claim is added, removed or changed.
- `labels`: shared UI words. `observational` is the flag text shown on every observational claim
  ("Association, not cause"); `certainty_level` names the four levels below.

A *text object* is `{"en": "..."}`; no other keys.

## A claim

| Field | Type | Required | Meaning |
|---|---|---|---|
| `id` | string | yes | Stable id, `module.topic.name`, lowercase (`^[a-z][a-z0-9_]*(\.[a-z0-9_]+){2}$`). Never reused for a different finding. The app finds note text only through this id. |
| `version` | integer ≥ 1 | yes | Bumped when the claim's text or sources change. |
| `held` | string | no | Present = the claim is in the pack but **no screen may show it**; the text says what it waits for (e.g. "SPEC §11: …"). Removing it is the owner's decision. |
| `module` | enum | yes | `shared`, `strength`, `stamina`, `daily_movement`, `mobility_balance`, `body_measures` (SPEC §8). Notes about the week or the app as a whole are `shared`. |
| `features` | list of strings | yes, ≥ 1 | What the claim explains: harvest row ids (e.g. `FA15`, `AK1`), SPEC features (`X-WEEK`, `N-HISTORY`) or decision numbers (`0.19`). Each must be named in SPEC.md. |
| `report_rules` | list of `{report, rule}` | yes, ≥ 1 | The report rules the claim comes from. `report` is 1 or 2; `rule` is a string, one number or a range (`"6"`, `"1-4"`). Report 1 holds 1-31; Report 2 holds 32-63 and updates to 1-31. |
| `claim` | text object | yes | The brief note: one finding. |
| `body` | text object | no | The full note (level "full"): the next most important findings and limits. |
| `population` | text object | yes | Who was studied. Under "Details". |
| `design` | `{codes, en}` | yes | Study type. `codes` are the report's design codes (`Guide`, `Cons`, `UR`, `CR`, `NMA`, `MA`, `IPD-MA`, `SR`, `RCT`, `XO`, `Obs`, `Mech`, `Narr`, and `"MA of Obs"`-style combinations); `en` is plain words. Under "Details". |
| `certainty` | `{level, en}` | yes | Certainty in plain words, shown in the brief note beside the claim (0.30). `level` is one of the four below. |
| `grade` | string | yes | The report's own grade, copied verbatim, for tracing: a `{D · P · C · M}` tag from the text or a rules table, or a summary-table grade cell (which has no braces). Not shown. |
| `status` | string | yes | The report's own status for the rule ("Strong", "Inference", with its qualifiers), or the summary table's "What holds" cell, copied verbatim. Not shown. |
| `observational` | boolean | yes | True when the outcome evidence is observational. The app then shows `labels.observational`. |
| `sources` | list | yes, ≥ 1 | One level down, under "Details". Each: `author` (first author's surname or the issuing body), `year`, `pmid` and/or `doi`, optional `coi` (text object: a conflict of interest the report discloses), optional `independence` (`IND`, `IND-decl`, `UNK`, `COMM`: the report's funding-independence label, decision 0.15). |

No other fields are allowed, so no note text can sit outside a claim with an id.

## Certainty levels

The level is read from the report's own grading, never estimated afresh. GRADE (or CINeMA), where a
source reported it, decides; otherwise the report's rule of thumb on the `{D · P · C · M}` tag does.

| Level | Label | When |
|---|---|---|
| `high` | High | GRADE high or moderate; or P3 · C3 with M2-M3. |
| `moderate` | Moderate | P2-P3 with C2-C3, not meeting `high`. |
| `limited` | Limited | Any P1 or C1 in the claim's core evidence; or GRADE low or very low. |
| `consensus` | Expert opinion | Rests on a guideline or consensus statement without direct trial outcomes. |

The plain-words text (`certainty.en`) says the level and why in a few words, e.g. "Moderate:
consistent results, but mostly in young men".

## Wording rules (SPEC §7, rules 1-7)

1. **A finding, never an instruction.** "Studies in trained adults found…", never "you should". No
   imperatives, no "we recommend". Guidelines are reported as what they say ("A guideline…
   describes…"), not passed on as orders.
2. **Hedging preserved.** Past tense for what studies found; "associated with" for observational
   results; numbers, ranges and qualifiers ("about", "on average", "possibly small") exactly as the
   report gives them.
3. **Who was studied and how sure** travel with every claim (`population`, `certainty`); the
   observational flag travels with every observational claim.
4. **Sources one level down**: author, year, PMID and/or DOI, plus any conflict of interest the report
   flags. Prefer authors without commercial interests (0.15); a commercially tied source is kept only
   with its `coi` shown, and never as the only support.
5. **Neutral about body weight.** No weight-judging words (overweight, obese, BMI…); the reports' own
   phrase is "higher body weight".
6. **Never invented or approximated.** Every number, population and qualifier is in the report the
   claim cites. Where the report gives no number, the claim gives none.
7. **Never about the person.** No "your glucose", no second person, no reading of the person's own
   data as a health condition. General information about studies, not individual advice.

Where a note appears, its level (off / brief / full) and hiding by type are app settings (§7 rule 5);
they are not in the pack.

## The gate: `tools/evidence_check.py`

Standard library only.

```
python tools/evidence_check.py                     # check evidence/claims.json
python tools/evidence_check.py --research DIR      # where the local research folder is
python tools/evidence_check.py --ids FILE --ids FILE --reports DIR
python tools/evidence_check.py --skip-content      # no report text at hand: check everything else
python tools/evidence_check.py --selftest          # plant each defect, require each to fail
```

The verified id lists are read from `DIR/research_notes/*/ids_check.json` and the two reports from
`DIR/reports/` (or `--reports`). `DIR` comes from `--research`, else the environment variable
`SISYPHUS_RESEARCH_DIR`, else `research/` at the repository root (where it sits, git-ignored, in the
owner's main checkout). A missing id list or report is a failure, never a pass; `--skip-content` runs
without the reports and says so in its result. Feature ids are read from SPEC.md in the repository.

It fails on:
- schema: a missing or unknown field (including any language key other than `en`), a wrong type, an invalid
  enum, a malformed or duplicate id;
- sources: a PMID or DOI not in a verified id list (or listed there as bad); a year or first author
  that does not match PubMed's record; a DOI that differs from PubMed's DOI for the same PMID;
- rules: a rule number outside its report's range;
- features: an id SPEC.md does not name;
- held: an empty `held` (a held claim must say what it waits for);
- text: an empty or missing `en`; a banned instruction, promise ("will"), second-person or weight word
  (word-boundary matched, case-insensitive);
- report content (§7 rule 6), against the report(s) the claim's `report_rules` cite: a `grade` or
  `status` not found verbatim; a source PMID or DOI the report does not cite; a number in any shown
  text (claim, body, population, design, certainty, COI note) that the report does not contain. The
  number check is necessary, not sufficient: it proves a number exists in the report, not that it sits
  beside the same finding, so small numbers (3, 7) can pass by coincidence. Wording still needs a
  reader;
- observational: design codes containing `Obs` without `observational: true`; an observational claim
  whose claim or body lacks association wording ("associat…").
