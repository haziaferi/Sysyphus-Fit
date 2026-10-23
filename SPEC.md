# SisyphusFit — specification

Version 0.2 (draft for the owner's workshop), 2026-10-20. Offline-first Android app for organising
and tracking near-daily movement and training, built from training modules that can be used alone or
stacked: daily movement, strength, stamina, mobility and balance, and optionally body measures. It
records, and comments neutrally with graded scientific evidence; it never promises or prescribes.
Package `com.hazia.sisyphusfit`.

Sections 7-11 are new in 0.2 and are a DRAFT: marked **[decided]** where the owner has decided,
**[proposed]** where it is a proposal for the workshop, **[open]** where the owner has deferred it.

## 0. Owner decisions so far

| # | Decision | Date |
|---|---|---|
| 0.1 | Android only for now; keep a Windows build possible (no Android types in `:core`, Room KMP in `:data` with a desktop JVM target). | 2026-10-19 |
| 0.2 | Package name `com.hazia.sisyphusfit`. | 2026-10-19 |
| 0.3 | Data layer and skeleton first; screens later. | 2026-10-19 |
| 0.4 | Repository: github.com/haziaferi/Sysyphus-Fit (public). `research/` stays local: it holds phone screenshots and per-phone state. | 2026-10-19 |
| 0.5 | Scope: the 50-point scope of the feature harvest, re-cut by module in §3 (0.22-0.27) with the owner's additions. | 2026-10-19, re-cut 2026-10-20 |
| 0.6 | No account, no network at runtime (harvest row GM9: no INTERNET permission). | 2026-10-19 (scope) |
| 0.7 | The two evidence reports are the app's foundation (§7). | 2026-10-20 |
| 0.8 | The app makes no promises and cannot recommend anything with certainty; it can neutrally comment with scientific evidence. Broad studies cannot prescribe for one person, so features reflect the findings but allow customisation. | 2026-10-20 |
| 0.9 | Modular structure: daily movement, strength, stamina, mobility and balance, usable alone or stacked into a personal set-up (§8). | 2026-10-20 |
| 0.10 | Body measures is an optional fifth module, off by default, with no BMI/BMR verdicts and no forecasts. | 2026-10-20 |
| 0.11 | Features are gathered into screens by shared purpose, with few overlaps and only where necessary (§9); Factotum's Today and purpose map are a skeleton to adapt, not a blueprint. | 2026-10-20 |
| 0.12 | Today is designed last: it depends on every other screen. | 2026-10-20 |
| 0.13 | While logging sets, the past is shown as the live comparison only (today vs last time, harvest row SG2). It also does LL2's job: last time's numbers show from the start of each set, and today's fill in as they are logged (workshop). | 2026-10-20 |
| 0.14 | Visual size follows function hierarchy (not every control is visually 48 dp). Touch-area minimum: see 0.21 (42 dp), which replaces the 48 dp first written here. | 2026-10-20 |
| 0.15 | Prefer evidence from authors without commercial interests; the short-bout rules rest on independent physiology and trials, the VILPA cohort is a supporting association only. | 2026-10-20 |
| 0.16 | The commentary rules of §7 are adopted as written. Evidence notes sit behind a circled "i"; tapping it opens the note in a bottom sheet. | 2026-10-20 (workshop) |
| 0.17 | Evidence notes default to the brief level for a new user. | 2026-10-20 (workshop) |
| 0.18 | Training modules are chosen at first launch; nothing is assumed on. | 2026-10-20 (workshop) |
| 0.19 | Added inside existing modules: isometric holds as a first-class exercise type and "move fast" (power) as a set option, in Strength; self-checks (chair rise, balance hold, walking speed) as the person's own trend only, never a verdict, in Mobility and balance. | 2026-10-20 (workshop) |
| 0.20 | The purposes and screens of §9, with the rows left out, are the working list for mockups; screens can still move when mockups show a problem. | 2026-10-20 (workshop) |
| 0.21 | The screen rules of §10 are adopted, with: touch areas at least 42 dp (avoids overlap or too much empty space when two lines or buttons are close), touch targets at least 5 dp apart; charts and tracking are part of the app, never presented as success versus failure. Note: 42 dp is above WCAG 2.2 AA (24 px), below Android's 48 dp guideline and WCAG AAA (44 px); Android's Accessibility Scanner will flag it. | 2026-10-20 (workshop) |
| 0.22 | Stages: a thin slice first (every module minimally usable on a shared base), then deepening stages ordered by scoring on the owner's axes (evidence strength, everyday usefulness, effort, foundation value; equal weights). A stage is done when tests and gates are green, it has been walked on the 6T, the owner has signed off its screens from mockups, and it is pushed after the publish check. | 2026-10-20 (workshop) |
| 0.23 | Modules are tags on exercises; an exercise may carry several. A session carries the tags of the exercises in it, is logged per calendar date, and can be given labels from a reusable list the person keeps (several per session). | 2026-10-20 (workshop) |
| 0.24 | "This week" counts a mixed session by its parts: each exercise's sets and time count under its own module tag(s); per-module totals stay separate and are never combined. | 2026-10-20 (workshop) |
| 0.25 | Until Today exists, the app opens on "Start or log". | 2026-10-20 (workshop) |
| 0.26 | Daily movement gains steps (entered, and counted by the phone's step sensor), movement reminders the person schedules, and sitting-break prompts in hours the person chooses. | 2026-10-20 (workshop) |
| 0.27 | Backup (one file and scheduled to a folder) and resuming a session after the app is killed stay in the first stage: nothing logged may be lost. | 2026-10-20 (workshop) |
| 0.28 | Set input is the hybrid: − and + change a value by 1 at a time; tapping the number opens a keypad (replaced by the phone's own keyboard, 0.38); weight accepts up to two decimals (e.g. 61,25 kg; stored in grams, no schema change). | 2026-10-20 (stage 1a mockups) |
| 0.29 | Navigation: a bottom tab bar, Inizia · Questa settimana · Storico · Altro. | 2026-10-20 (stage 1a mockups) |
| 0.30 | Evidence notes open brief (claim and certainty); who was studied, study type and sources sit under "Dettagli". | 2026-10-20 (stage 1a mockups) |
| 0.31 | Screens that show things (This week, Settings, Altro) have no main action button; screens for doing things keep one, and its form is chosen by scoring a few variations. | 2026-10-20 (stage 1a mockups) |
| 0.32 | The next mockup round adds visual polish (typography, spacing and rhythm, icons, depth and detail), still greyscale, drawing on the creative-design-suite and design-optimization resources. | 2026-10-20 (stage 1a mockups) |
| 0.33 | Partial reps are a separate count from full reps, never converted (7½ = 7 + 1 partial; five half-reps = 0 + 5 partials). The partial count appears only on exercises where the person switches it on. Schema 2 adds `partial_reps` to a logged set and a per-exercise switch. | 2026-10-20 (stage 1a mockups) |
| 0.34 | Density: balanced by default; compact is a user option, as in the polish round's compact preview. | 2026-10-21 (polish round) |
| 0.35 | Main action on doing screens: a full-width bar at the bottom (scored best on the owner's five button axes: same place and easy reach, mis-tap risk, content covered, same place everywhere, label visible; 0.973 vs floating 0.942 vs bar with a secondary 0.821). While something is running (e.g. a timer), a floating button appears for it (e.g. stop); it never sits beside the main bar and stopping keeps the held press (FA4). | 2026-10-21 (polish round) |
| 0.36 | Typeface: IBM Plex Sans (variable, `IBMPlexSans[wdth,wght].ttf`, SIL OFL 1.1) for all text and numbers, bundled in the app (offline). Set numbers use its tabular figures (checked: equal widths). Seven faces scored within 0.002 of each other, so the owner chose by eye. "Plex" is a Reserved Font Name: a subset or modified copy must be renamed. Replaced by Sisifo (0.40). | 2026-10-21 (polish round) |
| 0.37 | Icons: our own set of 28, drawn in VectorCraft on a 24 grid with a 2 dp stroke, round caps and no fill. Module icons: one shoe print (Daily movement), a pocket tape case with its tail and tab (Body measures), a figure on one leg with the other reaching back (Mobility & balance, clear of Android's Accessibility figure), a heart with a pulse line (Stamina), a dumbbell (Strength). Backup is a floppy disk; restore is the floppy inside an arrow turning back. The session clock is a wristwatch and the Start tab a play sign in a rounded square, so neither can be taken for the circled "i" at 24 px. Holds and partial reps are shown as text, not markers. Chosen by the owner after scoring variants on legibility at 1x and distinctness (research/screens-2026-10-20/polish/icons/review-2026-10-21). | 2026-10-21 (icon review) |
| 0.38 | Number entry: tapping a number opens the phone's own numeric keyboard; the app draws no keypad of its own. The −/+ steps and the two-decimal weight of 0.28 stay. | 2026-10-22 (final stage 1a mockups) |
| 0.39 | Language: the app's text is written in English first, and the mockups are in English. Italian comes later as a translation pass [deferred]; numbers then follow the phone's locale (57.5 / 57,5). The evidence pack's Italian pass is deferred with it. | 2026-10-22 (final stage 1a mockups) |
| 0.40 | Typeface: Sisifo (github.com/haziaferi/sisifo, SIL OFL 1.1), carved from Ortica Linear for this app, replaces IBM Plex Sans (0.36). Two variable files are bundled (offline): `Sisifo[CARV,wght].ttf` and its 12° oblique `Sisifo-Italic[CARV,wght].ttf`, with weight 300-700 and Carve 0-100. Carve follows text size, stepped: 0 up to 16 dp, 100 above (type tokens, §10). Numbers that change in place use tabular figures (`tnum`); titles set in capitals may use the inscriptional capitals (`ss01`); the italic is for emphasis in text, notes and hints, and display accents. Blind panel against the font search's finalists on ten axes: Sisifo 0.880, next Libertinus Sans 0.739, IBM Plex Sans 0.531. The stepped and ramped carve mappings tied (0.885 / 0.873) and the owner chose stepped. The app takes Sisifo's released files and does not build them. | 2026-10-23 (Sisifo stage 6 sign-off) |

**Not decided, and not to be decided by the code:** screens, layout, wording, navigation, palette
(owner rule: discuss frontend before building). The skeleton therefore has no screen.

**Defaults the skeleton had to take, open for the owner:**
- Android's own backup (`app/src/main/res/xml/data_extraction_rules.xml`): nothing to a cloud
  backup; a device-to-device transfer to a new phone carries everything. Lint requires a choice.
- The app icon: none yet (an icon is a visual decision); lint's MissingApplicationIcon is
  suppressed in the manifest until there is one.
- Signing: the default debug key. Factotum signs with a shared committed debug key so any checkout
  can update the installed app; not copied here without the owner's say.

## 1. Modules

| Module | Kind | Holds | May depend on |
|---|---|---|---|
| `:core` | Kotlin Multiplatform (android, desktop JVM) | pure rules: ids, which day a session counts on, a day's total, progression, the week per module | kotlinx-datetime only |
| `:data` | Kotlin Multiplatform with Room KMP | the database, DAOs, the backup format, the safe open | `:core`, Room, androidx.sqlite |
| `:app` | Android application | the Android entry points (empty until screens are decided) | `:data` |

A Windows build would add a `:windows` module beside `:app`; `:core` and `:data` already compile
and test on the desktop JVM, which is also where most tests run (fast, no emulator).

## 2. The floor (what every harvested mechanic assumes)

User-defined exercises; a session holding sets of reps / weight / time / distance; a saved routine
that can be started again; a history of sessions; one local database; no account; works offline.

## 3. Scope, staged by module (re-cut 2026-10-20; worksheet `research/stages-2026-10-20/`, kept locally)

**[decided 0.22-0.27; order proposed by scoring]** A thin slice first, then deepening stages. Each
stage ships on its own; packages ship whole. Done = tests and gates green, walked on the 6T, screens
signed off by the owner from mockups before they are built, pushed after the publish check.

The deepening order comes from `stages.py` (four axes, equal weights, effort checked against the
existing code). It is a **plateau**, not a verdict: neighbours differ by less than 0.05 and swap
under ±50% weight changes; only the first group (stage 2) and the last group (body measures,
self-checks, GPS) hold in every variant. Neighbouring packages that tie were merged into one stage
(owner: fewest stages compatible with stability).

**Stage 1a, the shared base, proven by Strength** (split from 1b for stability: a mistake in the
shared base is caught before four modules depend on it):
- schema 2, additive from schema 1 with a migration test: module tags on exercises (several per
  exercise, 0.23), reusable session labels, a body-measure table, the power flag and the partial-reps count on a set (0.19, 0.33);
- app shell; module choice at first launch (X-MODULES, 0.18); opens on "Start or log" (0.25);
- exercises, routines, one session model across modules, logged per calendar date (N-EXERCISES,
  N-ROUTINES, N-SESSION, FT2, FA13); resume after the app is killed (GL1, 0.27);
- history; "This week" per module, mixed sessions counted by their parts (N-HISTORY, X-WEEK, 0.24);
  no streak or missed-day count (FA15);
- evidence notes behind a circled "i", brief by default, from the evidence pack (X-EVIDENCE, §7);
- settings (X-SETTINGS); backup package: one-file backup and restore, scheduled backup to a chosen
  folder (KA9, OS8, 0.27); no INTERNET permission (GM9);
- Strength minimal: sets with the live comparison showing last time from the start of each set
  (SG2, 0.13).

**Stage 1b, the other modules, minimal:**
- Daily movement: a quick amount and the day total (FA10, FA11).
- Stamina: a steady session, live or entered afterwards, with pace (FT3).
- Mobility and balance: holds counted per side (CM2).
- Body measures (offered unticked): entering a measure (OS1).

**Deepening stages (scored order):**
2. Strength detail: effort per set, RPE or reps in reserve (BC1); "move fast" (N-POWER); hold timer
   for isometric holds (KA3).
3. Looking back and planning: last sets shown when picking an exercise (GM2); one own-trend chart,
   no success/failure framing (N-CHART); weekday to-do list (CM3); steps entered (N-STEPS) and
   **counted by the phone's step sensor (new row, not yet priced; needs the Physical-activity
   permission, 0.26)**.
4. Intervals package (TX1, TR5, FA5, FA1); movement reminders the person schedules (IT1);
   sitting-break prompts in chosen hours (AK1).
5. Goals in plain words (FA9, N-GOAL); closing a session with duration, intensity, comment (GR4);
   launcher shortcut per routine or timer (TR2).
6. Rest-timer package (GL2, LL4, KA5).
7. Progression package (LL5, GM6). **Blocked until the owner decides where suggestions stop (§11).**
8. Early stop reviewed, unfinished sets skipped (KA2); quick intervals from a cycles stepper (SH1);
   body-measure chart (OS2, OS4).
9. Self-checks as own trend (PL1, 0.19); body-measure changes (OS5).
10. GPS package (OT1, FA4, OT5, FT5).
11. Today (KA6), designed last (0.12).

Standing rule from the harvest: before building a row, read it against the code; its effort and
frequency were estimated before any code existed.

## 4. Data model, schema version 2

Schema 1 was designed for the whole scope, so that no package needs a migration of logged data;
schema 2 (stage 1a, 0.23, 0.33) only adds to it. Units are integers: time in epoch milliseconds
(UTC) plus the zone it was recorded in, durations in seconds, weight in grams, girths in
millimetres, distance in metres, coordinates in 1e-7 degrees.

Ids are text ULIDs (`:core` `Ids`), so a restored or imported row never collides with a local one
and a later Windows copy can merge. Every table has `id TEXT PRIMARY KEY`.

| Table | Columns (beyond id) | Serves |
|---|---|---|
| `exercise` | name, kind (DYNAMIC, ISOMETRIC, TIMED, DISTANCE), sided (0/1, counted per side), archived, show_partial_reps (0/1, default 0; schema 2) | floor; CM2; 0.33 |
| `routine` | name, weekdays (bit 0 = Monday … bit 6 = Sunday; 0 = unscheduled), position | floor; CM3, KA6 |
| `routine_item` | routine_id, exercise_id, position, target_sets, target_reps, target_weight_g, target_seconds, target_distance_m, rest_seconds | floor; LO4-style rest, GM6 |
| `session` | routine_id?, activity (free type, e.g. "Walk"), started_at?, ended_at?, zone, day (local date it counts on), state (OPEN, FINISHED, STOPPED_EARLY), intensity? (1-5), note? | floor; FT2, GR4, FA13, KA2, GL1-style resume (OPEN = in progress) |
| `logged_set` | session_id, exercise_id, position, side? (L, R), reps?, weight_g?, seconds?, distance_m?, rpe? (×10), rir?, done_at, power (0/1, "move fast", default 0; schema 2), partial_reps (default 0; schema 2) | floor; LL2, GM2, BC1; 0.19, 0.33 |
| `progression_rule` | routine_item_id, add_weight_g, add_reps, when (ALL_SETS_HIT) | LL5, GM6 |
| `interval_preset` | name, prepare_s, work_s, rest_s, rounds, sets, set_rest_s, last_used_at? | TX1, FA1, SH1 |
| `track_point` | session_id, t, lat_e7, lon_e7, alt_dm?, accuracy_m?, segment | OT1, OT5 |
| `goal` | exercise_id? (null = all training), period (DAY, WEEK, MONTH), target, unit (SESSIONS, MINUTES, METRES, SETS) | FA9 |
| `setting` | key, value | OS8 (backup folder, schedule) and later preferences; module on/off, evidence-note level |
| `exercise_module` *(schema 2)* | exercise_id, module (DAILY_MOVEMENT, STRENGTH, STAMINA, MOBILITY_BALANCE, BODY_MEASURES); unique per pair | 0.23 |
| `label` *(schema 2)* | name, position, archived | 0.23 |
| `session_label` *(schema 2)* | session_id, label_id; unique per pair | 0.23 |
| `body_measure` *(schema 2)* | kind (e.g. WEIGHT, WAIST), value, unit (G, MM), taken_at, zone, day, note? | OS1, 0.10 |

Rules the schema carries:
- **A session without times is an amount** (FA10's shape): `started_at` and `ended_at` null, `day`
  set. A day's total time is the union of its timed sessions plus the sum of its amounts
  (`:core` `DayTotals`).
- **`day` is the local date of `started_at` in `zone`** (FA13), computed once by `:core` `CountingDay`
  and stored, so a later change of the phone's zone does not move history.
- **No streak is stored or derived** (FA15). Nothing in the schema counts missed days.
- `logged_set` rows exist only for sets that were done; an early stop keeps no skipped rows (KA2).
- **Modules are tags on exercises** (0.23): a session's modules are its exercises' tags, never
  stored on the session. **"This week" counts a mixed session by its parts** (0.24, `:core`
  `weekByModule`): each set counts, whole, under every tag of its exercise; a session counts once
  under each module it touched; totals stay per module and are never combined; an untagged
  exercise counts nowhere. **[open, to refine]** It counts logged sets and their time only: a
  session without sets (a walk, a quick amount) counts nowhere yet, and steps and distance (km)
  are units of measurement but not in the way sets are (owner, 2026-10-21), so how they enter
  "This week" is still to be decided.
- **A partial rep is its own count** (0.33): `partial_reps` beside `reps`, never converted (7½ is
  reps 7, partial_reps 1). The exercise's switch only decides whether the count is offered.
- **A body measure is the person's own number** (0.10): an integer in its stated unit; `day` follows
  the session rule. No BMI, BMR, verdict or forecast is stored or derived.
- **Migrations add, never rewrite**: a new column goes last with a SQL default (a fresh table then
  matches a migrated one column for column); no destructive fallback, so a failed migration stops
  the open with the file untouched. Proven by a test that builds a schema-1 file from Room's
  exported `1.json`, fills every table, migrates through the app's own open, and compares every
  cell (`quote()`) before and after, plus the migrated shape against a fresh one; on desktop and,
  under Robolectric, on the phone's path (`KeepFileOnCorruptionDriver`). **A failed migration
  leaves the file as it was** (§5): a migration statement that throws, a migration Room's check
  refuses, and a file a newer app wrote each stop the open with every cell, table shape and the
  version unchanged, nothing set aside and nothing wiped.

## 5. Safety and gates

- **The database never deletes itself.** Android opens through `KeepFileOnCorruptionDriver` (the
  stock driver's default handler deletes a corrupt file); `openOrRecover` sets an unopenable file
  aside as `<name>.unopenable-<stamp>` and opens a fresh one. Proven by a Robolectric test that
  writes garbage and finds the bytes set aside, with the stock driver as the failing control.
- **Backup is the first feature** (KA9): one JSON file, every table, `format`
  `sisyphusfit-backup`, `version` 2 since schema 2. Restore replaces everything in one transaction. The gate is a
  round trip: export, restore into an empty database, export again, byte-identical. A version-1
  backup still restores: its rows unchanged, the new columns at their defaults, the new tables
  empty (pinned by a version-1 file the schema-1 app exported).
- **Gates:** `./gradlew --offline check` runs every test and Android lint with warnings as errors
  (the four version-currency ids informational).
- **Before a push:** `python ~/.claude/tools/publish_check.py .` (also installed as the pre-push hook).

## 6. State, 2026-10-19

Skeleton built: `:core` (ids, the counting day, a day's total, the progression rule), `:data` (schema
1 with all ten tables, the floor DAO, the corruption-safe open, the one-file backup), `:app` (no
screen). `./gradlew --offline check` passes: 39 tests (core 13 on desktop and 13 on Android host,
data 10 on desktop and 3 under Robolectric) and lint at zero warnings. Two planted mutants (restore
outside a transaction; a table missing from the backup list) each turned the intended test red.
Next: stage 1 (§3), after the owner's screen decisions for it.

2026-10-20: screens paused by the owner ("the premises for these screens are flawed": the first
mockups assumed a set-by-set gym session as the app's centre). Evidence gathered first (§7), the
structure re-cut into training modules (§8) and screens by purpose (§9). No code changed.

2026-10-21: schema 2 built (branch `schema-2`, not merged): four tables and three columns added
(§4), migration 1→2 with `2.json` exported, backup version 2, and the `:core` rule "This week
counts a mixed session by its parts" (`weekByModule`). `./gradlew --offline check` passes: 56
tests (core 18 on desktop and 18 on Android host, data 15 on desktop and 5 under Robolectric) and
lint at zero warnings. Ten planted mutants each turned the intended test red: the week counting
only an exercise's first tag; a session counted once per set; a migration that rewrites a schema-1
cell; a migration missing an index; restore refusing a version-1 backup; a schema-2 table missing
from the backup list; the open adding no migrations; a destructive fallback beside or instead of
the migrations (caught by the Robolectric test only: on desktop the open still failed and kept the
file); every failed open treated as corruption and set aside. No screen yet.

## 7. Foundation: evidence and commentary

**Sources [decided 0.7].** Two local reports, every citation machine-checked against PubMed and
doi.org (`research/verify_ids.py`):
- `research/reports/Exercise evidence for daily training.md`: strength, muscle, stamina, muscular
  endurance, joints, daily vs N days; rules 1-31.
- `research/reports/Exercise evidence for health and longevity.md`: health and longevity, insulin
  sensitivity, sedentary vs active, real-life adoption; rules 32-63, updates to 1-31; short-bout
  rules re-anchored on independent evidence (0.15).

**What the evidence says about the app's shape [decided, from the reports].**
- Weekly totals per module decide long-term outcomes; frequency spreads the dose. The app counts
  weekly, and never forms a combined score across modules.
- Near-daily movement is justified by metabolism (insulin sensitivity raised ~24-48 h after
  activity; light movement is enough for the acute glucose effect) and by habit, not by more
  strength or longer life. Consensus: no more than two days in a row without activity (Strong
  for diabetes or prediabetes, an inference otherwise).
- No streaks, no reset-style pressure, no readiness or recovery scores, no load-ratio injury
  alarms; a missed day changes nothing.
- Self-monitoring with self-chosen goals is the best-supported technique; app effects fade, so
  the app must stay useful without novelty.

**Commentary rules [decided 0.16, 0.17].** Shown behind a circled "i" that opens a bottom sheet;
brief by default. An evidence note:
1. states a finding, never an instruction: "Studi su adulti allenati hanno trovato…", never
   "dovresti";
2. carries who was studied, certainty in plain words, and "associazione, non causa" when the
   evidence is observational;
3. names its sources (author, year, PMID/DOI) one level down;
4. appears only where it concerns what the person is doing, never as a notification;
5. can be hidden by type; a setting chooses the level: off / brief / full;
6. **never carries invented or approximated evidence**: every note comes from the evidence pack,
   which comes only from the two reports (a placeholder note was caught in the round-2 mockups);
7. never interprets the person's own data as a health condition (e.g. no "your glucose…", no
   BMI verdict). General information about studies, not individual advice; this is also the line
   between a wellness app and medical software (to confirm, not legal advice).

**Evidence pack [proposed].** The reports' claims as versioned data shipped with the app (offline):
id, claim, population, design, certainty, association/cause, sources (PMID/DOI), which features it
explains, Italian and English text written with the hedging intact. Its gate: every source id is
in a report's verified id list; no note text without a claim id.

## 8. Training modules

**[decided 0.9, 0.10, 0.18, 0.19]** Each module can be used alone or stacked; the person chooses
which are on at first launch (nothing assumed) and can change it later; the settings inside a
module reflect the evidence and every one is editable.

| Module | Covers | Offered at first launch |
|---|---|---|
| Daily movement | sitting breaks, walks after meals, steps entered or counted, a quick amount | yes |
| Strength | sessions, sets, routines, programmes, progression; isometric holds as an exercise type; "move fast" (power) as a set option | yes |
| Stamina | steady sessions, intervals and timers, offline GPS recordings | yes |
| Mobility and balance | mobility and balance routines, fall-prevention work (most relevant 65+); self-checks (chair rise, balance hold, walking speed) as the person's own trend, never a verdict | yes |
| Body measures | weight and measurements, changes over time; no verdicts, no forecasts | yes, unticked |

**Cross-module rules [proposed]:** one session model under all modules (the schema's `session`
already carries any activity); weekly totals per module, side by side, never combined; notes that
span modules (strength plus aerobic together, lifting before cardio for strength, two days without
movement) appear only where the person is doing one of them.

**Modules as tags [decided 0.23, 0.24]:** a module is a tag on an exercise (several allowed); a
session carries its exercises' tags, belongs to a calendar date and can carry labels from the
person's reusable list; weekly totals count each exercise under its own tag(s), per module, never
combined.

**Schema impact [decided for stage 1a]:** schema 2, additive from schema 1 with a migration test:
exercise-module tags (many-to-many), labels and session-label links, a body-measure table, a power
flag and a partial-reps count on a logged set, a per-exercise partial-reps switch (0.33). Module on/off and evidence-note levels live in `setting`.

## 9. Screens by purpose

**[decided 0.11; draft content]** Method ported from Factotum (`research/purpose-2026-10-20/`:
`purpose-map-method.md`, checker `purpose_map.py --map <json>` with a selftest, `score_maps.py`).
Every one of the 162 harvest rows is placed on exactly one screen, or declared shared with a
reason, or left out with a reason.

Three groupings were drafted and scored on the owner's axes (few overlaps, calm load per screen,
taps to common tasks, purpose coherence; equal weights): by module 0.61, by purpose 0.836, hybrid
0.841. Grouping screens by module loses (it duplicates session, history and exercise screens per
module); modules are switches and content, not a screen tree. By purpose and the hybrid are a
plateau and differ only in Today (deferred, 0.12).

**Purposes and screens [proposed, from the two leading maps; Today excluded]:**

| Purpose | Its job | Screens |
|---|---|---|
| What's next | see what today holds and start or log it | Today **[open]**, Start or log (sheet) |
| Doing it | move, and have the app keep count | Session, Exercise in a session, Set details (sheet), Number entry (sheet), Interval timer, Recording, Stopping and finishing, Log afterwards |
| Looking back | see what was done | This week (a section per module), History, A recorded activity, Progress |
| Making it yours | choose modules and goals; shape routines, programmes, timers, exercises | Modules and goals, Your plans, Programme library, Timers and routes, Exercises |
| Knowing why | the evidence behind a feature, how a movement is done | Evidence notes |
| Looking after the app | settings, backup, import/export, privacy | Settings, Back up and restore, Import, export, delete |

**Left out of the register [proposed]:** per-set prescriptions and automatic deloads (0.8),
readiness claims ("muscle recovered"), a hidden swipe duplicating a visible control, multi-person
profiles, Health Connect and Bluetooth sensors (owner: not chosen), BMI/BMR verdicts and weight
forecasts (0.10). Full reasons in `research/purpose-2026-10-20/compare.md`.

## 10. Screen rules

**[decided 0.21]** Adapted from Factotum's Today skeleton (`research/purpose-2026-10-20/factotum-today.md`)
and the round-1/2 scoring (`research/screens-2026-10-20/`):
1. Calm before complete: zones separated by tonal depth, not hue; a screen for doing things has
   one obvious primary action in the bottom zone, a screen that shows things has none (0.31);
   anything that can grow collapses to one row.
2. One home per thing: a summary links to the thing's screen; nothing is edited in two places.
3. The same element has one form, place and wording on every screen (module card, row, set row,
   evidence-note chip).
4. Visual size follows function hierarchy; touch areas at least 42 dp (0.14, 0.21).
5. Judgement-free wording: no streaks, "Niente qui." when empty. Charts and tracking are part of
   the app, never presented as success versus failure.
6. Hard gates on every mockup before it is shown: G1 no overlaps, G2 nothing clipped or wider than
   the screen, G3 touch targets at least 5 dp apart, G4 touch areas at least 42 dp. Then the owner's
   axes: harmony, density, visual and functional hierarchy, reach and legibility, taps per set,
   at-a-glance clarity, consistency (equal weights; a plateau is shown as a plateau).
7. Greyscale until the palette is discussed.
8. **[decided 0.40]** Type follows the tokens below: every text element takes one role. Sizes are in dp,
   balanced / compact (0.34), from the polish round's type scale. Carve is stepped by size: 0 up to 16 dp, 100 above.

| Role | Used for (stage 1a mockups) | Size dp | Line height dp | Weight | Carve | Features |
|---|---|---|---|---|---|---|
| label | secondary text, values beside a row | 13 / 12 | 20 / 16 | 400 | 0 | |
| eyebrow | section heads, dates, column heads, a note's fact names: tracked capitals | 13 / 12 | 16 / 16 | 500 | 0 | |
| body | running text and rows | 16 / 14 | 24 / 20 | 400 | 0 | |
| row title | names in rows, weekly amounts, the running timer | 19 / 17 | 24 / 24 | 500 | 100 | `tnum` on numbers |
| heading | screen titles, large secondary values | 23 / 20 | 28 / 24 | 500 | 100 | |
| exercise | the exercise's name on its own screen | 28 / 24 | 32 / 28 | 500 | 100 | |
| set number | the set's weight, reps and partials | 40 / 34 | 44 / 40 | 500 | 100 | `tnum` |

The polish round's largest size (48 / 40 dp) was the app's own keypad readout, removed by 0.38; it is not a token.
Italic (the oblique file) keeps its role's size, weight and carve. `ss01` applies only to titles set in capitals.

## 11. Decided, deferred, and the workshop agenda

**Decided 2026-10-20:** 0.7-0.27 above (0.16-0.27 in the workshop: agenda items 1-5 done).

**Deferred by the owner [open]:** the shape of Today (by purpose with a Start sheet, or a card per
module); where suggestions stop (record and comment only / one editable "next time" proposal after
a session / a per-set proposal). The evidence note's form is decided (0.30). How sessions without
sets, steps and distance count in "This week" (§4; owner 2026-10-21: units of measurement, but not
in the way sets are; to refine). The Italian translation of the app and of the evidence pack (0.39).

**Workshop agenda [proposed order]:**
1. Commentary rules (§7) and the evidence pack.
2. Training modules (§8): the list, defaults, what each covers.
3. Purposes and screens (§9), and the rows left out.
4. Screen rules (§10).
5. ~~Re-cut of the scope stages by module~~ done: §3, decisions 0.22-0.27.
6. Inner screens to mock up first, then the deferred items, then Today last.
