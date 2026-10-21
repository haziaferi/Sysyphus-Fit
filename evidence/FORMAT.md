# Evidence pack: format

The evidence pack is the only source of text for SisyphusFit's evidence notes (SPEC §7, decisions 0.7,
0.8, 0.15-0.17, 0.30). It is versioned data shipped with the app, read offline. Every claim comes from
one of the two local evidence reports, whose citations were machine-checked against PubMed and doi.org:

- Report 1, *Exercise evidence for daily training*: rules 1-31.
- Report 2, *Exercise evidence for health and longevity*: rules 32-63, plus updates to rules 1-31.

The reports stay local (the repository is public); the pack carries what the app shows plus enough
of the reports' own grading to trace each claim back. `tools/evidence_check.py` is the gate.

## File: `evidence/claims.json`

```json
{
  "format": 1,
  "pack_version": 1,
  "labels": { "observational": {"it": "...", "en": "..."}, "certainty_level": { "<level>": {"it", "en"} } },
  "claims": [ { ...claim... } ]
}
```

- `format`: the schema version of this file (this document). A change to the fields bumps it.
- `pack_version`: bumped whenever any claim is added, removed or changed.
- `labels`: shared UI words. `observational` is the flag text shown on every observational claim
  ("Associazione, non causa"); `certainty_level` names the four levels below.

## A claim

| Field | Type | Required | Meaning |
|---|---|---|---|
| `id` | string | yes | Stable id, `module.topic.name`, lowercase (`^[a-z][a-z0-9_]*(\.[a-z0-9_]+){2}$`). Never reused for a different finding. The app finds note text only through this id. |
| `version` | integer ≥ 1 | yes | Bumped when the claim's text or sources change. |
| `module` | enum | yes | `shared`, `strength`, `stamina`, `daily_movement`, `mobility_balance`, `body_measures` (SPEC §8). Notes about the week or the app as a whole are `shared`. |
| `features` | list of strings | yes, ≥ 1 | What the claim explains: harvest row ids (e.g. `FA15`, `AK1`), SPEC features (`X-WEEK`, `N-HISTORY`) or decision numbers (`0.19`). |
| `report_rules` | list of `{report, rule}` | yes, ≥ 1 | The report rules the claim comes from. `report` is 1 or 2; `rule` is a string, one number or a range (`"6"`, `"1-4"`). Report 1 holds 1-31; Report 2 holds 32-63 and updates to 1-31. |
| `claim` | `{it, en}` | yes | The brief note: one finding, in the phone's language. |
| `body` | `{it, en}` | no | The full note (level "full"): the next most important findings and limits. |
| `population` | `{it, en}` | yes | Who was studied ("A chi si riferisce"). Under "Dettagli". |
| `design` | `{codes, it, en}` | yes | Study type. `codes` are the report's design codes (`Guide`, `Cons`, `UR`, `CR`, `NMA`, `MA`, `IPD-MA`, `SR`, `RCT`, `XO`, `Obs`, `Mech`, `Narr`, and `"MA of Obs"`-style combinations). `it`/`en` are plain words ("Tipo di fonti"). Under "Dettagli". |
| `certainty` | `{level, it, en}` | yes | Certainty in plain words, shown in the brief note beside the claim (0.30). `level` is one of the four below. |
| `grade` | string | yes | The report's own tag `{D · P · C · M}`, copied verbatim, for tracing. Not shown. |
| `status` | string | yes | The report's own status for the rule ("Strong", "Inference", with its qualifiers), copied verbatim. Not shown. |
| `observational` | boolean | yes | True when the outcome evidence is observational. The app then shows `labels.observational`. |
| `sources` | list | yes, ≥ 1 | One level down, under "Dettagli". Each: `author` (first author's surname or the issuing body), `year`, `pmid` and/or `doi`, optional `coi` `{it, en}` (a conflict of interest the report discloses), optional `independence` (`IND`, `IND-decl`, `UNK`, `COMM`: the report's funding-independence label, decision 0.15). |

No other fields are allowed, so no note text can sit outside a claim with an id.

## Certainty levels

The level is read from the report's own grading, never estimated afresh. GRADE (or CINeMA), where a
source reported it, decides; otherwise the report's rule of thumb on the `{D · P · C · M}` tag does.

| Level | it / en label | When |
|---|---|---|
| `high` | Alta / High | GRADE high or moderate; or P3 · C3 with M2-M3. |
| `moderate` | Media / Moderate | P2-P3 with C2-C3, not meeting `high`. |
| `limited` | Limitata / Limited | Any P1 or C1 in the claim's core evidence; or GRADE low or very low. |
| `consensus` | Parere di esperti / Expert opinion | Rests on a guideline or consensus statement without direct trial outcomes. |

The plain-words text (`certainty.it`, `certainty.en`) says the level and why in a few words, e.g.
"Media: risultati concordi, ma soprattutto su giovani uomini".

## Wording rules (SPEC §7, rules 1-7)

1. **A finding, never an instruction.** "Studi su adulti allenati hanno trovato…", never "dovresti".
   No imperatives, no "you should", no "we recommend". Guidelines are reported as what they say
   ("Una linea guida… descrive…"), not passed on as orders.
2. **Hedging preserved in both languages.** Past tense for what studies found; "associated with" /
   "si è associato a" for observational results; numbers, ranges and qualifiers ("about", "on
   average", "possibly small") identical in Italian and English. Italian uses a decimal comma and a
   thousands dot (4,7; 15.827); English the reverse.
3. **Who was studied and how sure** travel with every claim (`population`, `certainty`); the
   observational flag travels with every observational claim.
4. **Sources one level down**: author, year, PMID and/or DOI, plus any conflict of interest the report
   flags. Prefer authors without commercial interests (0.15); a commercially tied source is kept only
   with its `coi` shown, and never as the only support.
5. **Neutral about body weight.** No weight-judging words (overweight, obese, BMI, sovrappeso…); the
   reports' own phrase is "higher body weight".
6. **Never invented or approximated.** Every number, population and qualifier is in the report the
   claim cites. Where the report gives no number, the claim gives none.
7. **Never about the person.** No "your glucose", no "tuo/tua", no reading of the person's own data as
   a health condition. General information about studies, not individual advice.

Where a note appears, its level (off / brief / full) and hiding by type are app settings (§7 rule 5);
they are not in the pack.

## The gate: `tools/evidence_check.py`

Standard library only.

```
python tools/evidence_check.py                     # check evidence/claims.json
python tools/evidence_check.py --research DIR      # where the local research folder is
python tools/evidence_check.py --ids FILE --ids FILE
python tools/evidence_check.py --selftest          # plant each defect, require each to fail
```

The verified id lists are read from `DIR/research_notes/*/ids_check.json`. `DIR` comes from
`--research`, else the environment variable `SISYPHUS_RESEARCH_DIR`, else `research/` at the repository
root (where it sits, git-ignored, in the owner's main checkout). No id list found is a failure, never a
pass.

It fails on:
- schema: a missing or unknown field, a wrong type, an invalid enum, a malformed or duplicate id;
- sources: a PMID or DOI not in a verified id list (or listed there as bad); a year or first author
  that does not match PubMed's record; a DOI that differs from PubMed's DOI for the same PMID;
- rules: a rule number outside its report's range;
- text: an empty `it` or `en`; a banned instruction, second-person or weight word (word-boundary
  matched, case-insensitive, per language); numbers that differ between `it` and `en`;
- observational: design codes containing `Obs` without `observational: true`; an observational claim
  whose claim or body lacks association wording ("associ…") in either language.
