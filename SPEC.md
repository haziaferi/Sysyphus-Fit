# SisyphusFit spec

Scope: an offline Android app, package `com.hazia.sisyphusfit`, for organising and tracking near-daily movement and training in modules that can be used alone or stacked.

History: `SPEC-history.md`, one line per change. It is never a source of current rules.

Kinds: O owner rule · D decided · I inherited · A assumed · Q open.

## Rule index

| ID | Name | Section |
|---|---|---|
| D-R01 | Jobs and how often | 1. Purpose and jobs |
| D-R02 | Records and comments, never prescribes | 1. Purpose and jobs |
| D-R03 | Platform | 2. Platform and build |
| D-R04 | Package name | 2. Platform and build |
| O-R05 | Repository and local research | 2. Platform and build |
| D-R06 | Gradle modules | 2. Platform and build |
| D-R07 | No account, no network | 2. Platform and build |
| D-R08 | Frontend before building | 2. Platform and build |
| D-R09 | Compose Foundation only | 2. Platform and build |
| D-R10 | Bundled font files | 2. Platform and build |
| D-R11 | Android's own backup | 2. Platform and build |
| Q-R12 | App icon | 2. Platform and build |
| D-R13 | Signing key | 2. Platform and build |
| D-R14 | Evidence sources | 3. Evidence and commentary |
| D-R15 | What the evidence says about the app's shape | 3. Evidence and commentary |
| D-R16 | Commentary rules | 3. Evidence and commentary |
| D-R17 | Evidence pack | 3. Evidence and commentary |
| D-R120 | Notes that span modules | 3. Evidence and commentary |
| A-R18 | Wellness, not medical software | 3. Evidence and commentary |
| D-R19 | The five modules | 4. Training modules |
| D-R20 | Body measures without verdicts | 4. Training modules |
| D-R21 | Modules chosen by the person | 4. Training modules |
| D-R22 | Additions inside modules | 4. Training modules |
| D-R23 | Modules are tags | 4. Training modules |
| D-R24 | This week counts a mixed session by its parts | 4. Training modules |
| D-R25 | Each module's own units in This week | 4. Training modules |
| D-R26 | Units | 5. Data model |
| D-R27 | Ids | 5. Data model |
| D-R28 | Tables, schema version 2 | 5. Data model |
| D-R29 | An amount is a session without times | 5. Data model |
| D-R30 | The counting day | 5. Data model |
| D-R31 | Only done sets are stored | 5. Data model |
| D-R32 | Partial reps | 5. Data model |
| D-R33 | Migrations add, never rewrite | 5. Data model |
| O-R34 | Nothing logged is lost | 6. Safety and gates |
| D-R35 | The database never deletes itself | 6. Safety and gates |
| D-R36 | Backup format | 6. Safety and gates |
| D-R37 | Gates | 6. Safety and gates |
| D-R38 | Thin slice, then deepening | 7. Stages |
| D-R39 | Stage list | 7. Stages |
| O-R40 | Today is designed last | 7. Stages |
| D-R41 | A harvest row is read against the code first | 7. Stages |
| Q-R42 | The shape of Today | 7. Stages |
| D-R43 | Where suggestions stop | 7. Stages |
| D-R44 | Screens grouped by purpose | 8. Screens by purpose |
| D-R45 | Purposes and screens | 8. Screens by purpose |
| D-R46 | Doing and showing screens | 8. Screens by purpose |
| D-R47 | Calm before complete | 9. Screen rules |
| D-R48 | One home per thing | 9. Screen rules |
| D-R49 | One form per element | 9. Screen rules |
| O-R50 | Touch areas and hierarchy | 9. Screen rules |
| D-R51 | Judgement-free | 9. Screen rules |
| D-R52 | Gates G1-G5 | 9. Screen rules |
| D-R53 | Greyscale | 9. Screen rules |
| D-R54 | Screen width | 9. Screen rules |
| D-R55 | Fit on the phone's app area | 9. Screen rules |
| D-R56 | Text follows the phone's font size | 9. Screen rules |
| D-R57 | Density | 9. Screen rules |
| D-R58 | English first | 9. Screen rules |
| D-R59 | Typeface | 10. Type and tokens |
| D-R60 | Type roles | 10. Type and tokens |
| D-R61 | Text line boxes | 10. Type and tokens |
| D-R62 | Design tokens | 10. Type and tokens |
| D-R63 | Evidence note sheet spacing | 10. Type and tokens |
| D-R64 | Icons | 11. Visual language |
| D-R65 | Stone language | 11. Visual language |
| D-R66 | The tab bar's valley | 11. Visual language |
| D-R67 | The live comparison | 12. Exercise screen and rest |
| D-R68 | Set input | 12. Exercise screen and rest |
| D-R69 | The main bar and running controls | 12. Exercise screen and rest |
| D-R70 | The running control on the exercise screen | 12. Exercise screen and rest |
| D-R71 | The rest timer | 12. Exercise screen and rest |
| D-R72 | Default rest length | 12. Exercise screen and rest |
| D-R73 | Session clock | 12. Exercise screen and rest |
| D-R74 | The rest round is the boulder | 12. Exercise screen and rest |
| D-R75 | The figure | 12. Exercise screen and rest |
| D-R76 | The boulder's form | 12. Exercise screen and rest |
| D-R77 | What the figure and the stone share | 12. Exercise screen and rest |
| D-R78 | One centre line | 12. Exercise screen and rest |
| D-R79 | The comparison card at larger font sizes | 12. Exercise screen and rest |
| D-R80 | The rest round grows with its time | 12. Exercise screen and rest |
| D-R81 | The compact rest screen | 12. Exercise screen and rest |
| D-R82 | Navigation | 13. App shell |
| D-R83 | The app opens on Start | 13. App shell |
| D-R84 | Wiring by hand | 13. App shell |
| D-R85 | Edge to edge | 13. App shell |
| D-R86 | The shared data core | 13. App shell |
| D-R87 | Starting while a session is open | 13. App shell |
| D-R88 | Tab screens at larger font sizes | 13. App shell |
| D-R89 | Tab roots without screens | 13. App shell |
| D-R90 | The module choice screen | 14. First launch |
| D-R91 | Module cards on the choice | 14. First launch |
| D-R92 | When the data cannot be opened | 14. First launch |
| D-R93 | Build order for Start | 15. Start or log |
| D-R94 | What Start shows | 15. Start or log |
| D-R95 | Session names | 15. Start or log |
| D-R96 | The resume card | 15. Start or log |
| D-R97 | Start's date line | 15. Start or log |
| D-R98 | Exercises before routines | 16. Exercises and routines |
| D-R99 | Where exercises are made and managed | 16. Exercises and routines |
| D-R100 | The add sheet | 16. Exercises and routines |
| D-R101 | Exercise type and its lock | 16. Exercises and routines |
| D-R102 | Editing an exercise | 16. Exercises and routines |
| D-R103 | Exercise editor fields | 16. Exercises and routines |
| Q-R104 | How the type is chosen in the editor | 16. Exercises and routines |
| Q-R105 | How modules are picked in the editor | 16. Exercises and routines |
| Q-R106 | How categories are picked in the editor | 16. Exercises and routines |
| Q-R107 | How optional fields show | 16. Exercises and routines |
| Q-R108 | How the default rest is picked | 16. Exercises and routines |
| Q-R109 | How notes are written | 16. Exercises and routines |
| Q-R111 | How the add sheet adds | 16. Exercises and routines |
| Q-R112 | Pill and chip shapes | 16. Exercises and routines |
| D-R113 | How the add sheet's search reaches categories | 16. Exercises and routines |
| D-R114 | Categories and labels | 16. Exercises and routines |
| D-R115 | No streaks | Out of scope |
| D-R116 | No readiness or load alarms | Out of scope |
| D-R117 | No prescriptions or automatic deloads | Out of scope |
| D-R118 | No profiles or connected sensors | Out of scope |
| D-R119 | No hidden duplicates | Out of scope |

## Glossary

| Term | Meaning |
|---|---|
| Module | One of the five training areas of R19 (Daily movement, Strength, Stamina, Mobility and balance, Body measures); the person chooses which are on. |
| Tag | A module attached to an exercise; an exercise may carry several (R23). |
| Label | A name from the person's own reusable list, attached to a session (R23). |
| Session | One sitting of activity, counted on one calendar date; it holds logged sets or is an amount. |
| Amount | A session with no start or end time, only a day and a quantity (R29). |
| Counting day | The local date a session counts on: the date of its start in the zone it was recorded in (R30). |
| Set | One logged effort inside a session: reps, weight, time or distance. |
| Partial rep | A rep not carried through its full range, counted apart from full reps (R32). |
| Hold | An isometric exercise, logged by the time it is held. |
| Doing screen | A screen for doing something (logging, timing); it has one main action (R46). |
| Showing screen | A screen that shows things (This week, History, Settings, More); it has no main action (R46). |
| Main bar | The full-width main action at the bottom of a doing screen (R69). |
| Running control | The control shown while something runs, such as a rest or a recording (R69, R70). |
| Rest round | The round control that shows a rest's remaining time on the exercise screen (R70, R74). |
| Boulder | The rest round's stone, drawn from the owner's outline (R76). |
| Figure | The small Sisyphus who pushes the boulder during a rest (R75). |
| Comparison card | The card on the exercise screen that sets last time's numbers beside today's (R67, R78). |
| Evidence note | A short finding from the evidence pack, shown behind a circled "i" (R16). |
| Evidence pack | The versioned claims the app ships, the only source of evidence-note text (R17). |
| The floor | What every harvested mechanic assumes: user-defined exercises; a session holding sets of reps, weight, time or distance; a saved routine that can be started again; a history of sessions; one local database; no account; works offline. |
| Harvest row | A feature id from the 2026-10-19 feature harvest (`FA15`, `GL2`), or a feature id this spec gives (`X-WEEK`, `N-POWER`). |
| Density | Balanced (the default) or compact, the person's choice (R57). |
| Font size | The phone's own text-size setting, 1.0 to 2.0 (R56). |
| Carve | Sisifo's carving axis, 0-100, set by the type role (R59). |
| Stone language | The app's visual idea, taken from Sisifo's carving: surfaces cut on one side and worn on the other (R65). |
| Pebble | A worn, slightly uneven rounded shape for something you press (R65). |
| Carved line | A line cut into the surface, dark over light (R65). |
| Valley | The dip in the tab bar's top line under the current tab (R66). |
| The 6T | The OnePlus 6T the screens are walked and judged on (R55). |
| Gates G1-G5 | The hard checks every mockup and screen passes before it is shown (R52). |

## 1. Purpose and jobs

SisyphusFit organises and tracks near-daily movement and training, built from training modules that can be used alone or stacked into a personal set-up. It records what the person does and comments neutrally with graded scientific evidence. It works with no account and no network.

### D-R01 · Jobs and how often

**Rule:** The app serves one person, on their own phone, for the jobs below, as often as each row says. Defaults, what opens first and the order of lists follow this order.

**Values:**

| Job | How often |
|---|---|
| Log daily movement: a quick amount, steps, a sitting break | daily, several times |
| Log a session live: sets, holds, rests | 3-5 times a week |
| Check This week, per module | a few times a week |
| Log afterwards: a walk or session done without the phone | weekly |
| Look back: History, a past session | weekly |
| Read an evidence note | occasionally |
| Make or edit exercises and routines | at the start, then rarely |
| Back up or restore by hand | rarely; the scheduled backup runs on its own |
| Choose modules | once, changed rarely |

**Acceptance:** none: this ranks jobs; the rules that apply it carry their own checks.

**Since:** 2026-11-21 · owner · agreed rewording

### D-R02 · Records and comments, never prescribes

**Rule:** The app records and comments neutrally with scientific evidence; it makes no promises and recommends nothing with certainty. Features reflect the findings but leave every setting to the person.

**Why:** Broad studies cannot prescribe for one person.

**Acceptance:** `python tools/evidence_check.py` passes: its wording check refuses instructions, promises and second person in every note.

**Since:** 2026-10-20 · owner · migrated

## 2. Platform and build

### D-R03 · Platform

**Rule:** The app is for Android only. A Windows build stays possible: `:core` holds no Android types and `:data` uses Room KMP with a desktop JVM target.

**Acceptance:** `./gradlew --offline check` compiles and tests `:core` and `:data` on the desktop JVM.

**Since:** 2026-10-19 · owner · migrated

### D-R04 · Package name

**Rule:** The package and application id are `com.hazia.sisyphusfit`.

**Acceptance:** `applicationId` in `app/build.gradle.kts` is `com.hazia.sisyphusfit`.

**Since:** 2026-10-19 · owner · migrated

### O-R05 · Repository and local research

**Rule:** The code lives in the public repository github.com/haziaferi/Sysyphus-Fit. `research/` stays local and is never committed.

**Why:** `research/` holds phone screenshots and per-phone state.

**Acceptance:** `research/` is in `.gitignore`, and `python ~/.claude/tools/publish_check.py .` passes before every push.

**Since:** 2026-11-21 · owner · agreed rewording

### D-R06 · Gradle modules

**Rule:** The build has the three modules below, each depending only on what its row allows. A Windows build would add a `:windows` module beside `:app`.

**Values:**

| Module | Kind | Holds | May depend on |
|---|---|---|---|
| `:core` | Kotlin Multiplatform (android, desktop JVM) | pure rules: ids, the counting day, a day's total, progression, the week per module | kotlinx-datetime only |
| `:data` | Kotlin Multiplatform with Room KMP | the database, DAOs, the backup format, the safe open, the stores | `:core`, Room, androidx.sqlite |
| `:app` | Android application | `MainActivity`, the shell, the screens and their ViewModels | `:data` |

**Evidence:** Most tests run on the desktop JVM (fast, no emulator).

**Acceptance:** `settings.gradle.kts` includes exactly these modules, and each module's dependencies stay within its row.

**Since:** 2026-10-28 · builder · migrated

### D-R07 · No account, no network

**Rule:** The app has no account and uses no network at runtime (harvest row `GM9`).

**Acceptance:** The merged manifest declares no `INTERNET` permission.

**Since:** 2026-10-19 · owner · migrated

### D-R08 · Frontend before building

**Rule:** Screens, layout, wording, navigation and palette are discussed with the owner before they are built; the code never decides them.

**Acceptance:** none: a working practice; each stage's screens are signed off from mockups before they are built (R38).

**Since:** 2026-10-19 · owner · migrated

### D-R09 · Compose Foundation only

**Rule:** The app's pieces are built on Compose Foundation only, with no Material, in `app/.../ui/stone`, and the theme in `ui/theme`. A debug-only gallery shows every piece with density and font-size switches, and it is installed on the 6T for the owner's walk.

**Acceptance:** No `androidx.compose.material` artifact is on the app's dependency list; the gallery builds only in the debug variant (`GalleryActivity` under `app/src/debug`).

**Since:** 2026-10-28 · owner · migrated

### D-R10 · Bundled font files

**Rule:** Sisifo is bundled from release v1.0 of github.com/haziaferi/sisifo and checked by hash; its OFL ships in the app's assets. The app takes Sisifo's released files and does not build them.

**Values:**

| File | sha256 |
|---|---|
| upright `Sisifo[CARV,wght].ttf` | `6abb50cdd398d256a6d34eef6f3e697411c7212e51b9f8f51a523d7be3b53ad2` |
| oblique `Sisifo-Italic[CARV,wght].ttf` | `89f60b26ea58847d4a67846405974c0b8be3277580b79b934132cf91a694882d` |

**Evidence:** v1.0 is the signed-off 0.7 at version 1.000; only the version fields changed, checked table by table (2026-10-28).

**Acceptance:** `FontFilesTest.fontFilesAreTheRelease` matches both hashes.

**Since:** 2026-10-28 · owner · migrated

### D-R11 · Android's own backup

**Rule:** Android's own backup sends nothing to a cloud backup, and a device-to-device transfer to a new phone carries everything. The app's own backup (R36) is the way out.

**Why:** Training data never leaves the phone without the person (R07).

**Acceptance:** `app/src/main/res/xml/data_extraction_rules.xml` excludes every domain from `cloud-backup` and leaves `device-transfer` whole.

**Not:** Android's encrypted cloud backup: the data would leave the device. No device transfer either: a new phone would start empty until a restore.

**Since:** 2026-11-21 · owner · agreed rewording

### Q-R12 · App icon

**Rule:** What is the app's launcher icon?

**Default:** none proposed: an icon is a visual decision, which the owner left for a design round of its own on 2026-11-21; lint's `MissingApplicationIcon` is suppressed in the manifest until there is one.

**Why:** The boulder alone reads at launcher size (about 48 dp), while the figure pushing it tells more of the story but shrinks to a few pixels.

**Since:** 2026-11-21 · owner · agreed rewording

### D-R13 · Signing key

**Rule:** Debug builds are signed with Android's default debug key of the machine that builds them.

**Acceptance:** `app/build.gradle.kts` declares no `signingConfig`, and no keystore is in the repository.

**Not:** A shared committed debug key, as Factotum uses: the repository is public and keystores are gitignored. A shared key kept outside the repository: one machine installs the app, so nothing needs it.

**Since:** 2026-11-21 · owner · agreed rewording

## 3. Evidence and commentary

### D-R14 · Evidence sources

**Rule:** The app's evidence comes from the two local reports below, and every citation in them is machine-checked against PubMed and doi.org (`research/verify_ids.py`). Evidence from authors without commercial interests comes first.

**Why:** The short-bout rules rest on independent physiology and trials; the VILPA cohort is a supporting association only.

**Values:**

| Report | Covers | Rules |
|---|---|---|
| `research/reports/Exercise evidence for daily training.md` | strength, muscle, stamina, muscular endurance, joints, daily vs N days | 1-31 |
| `research/reports/Exercise evidence for health and longevity.md` | health and longevity, insulin sensitivity, sedentary vs active, real-life adoption; updates to 1-31 | 32-63 |

**Acceptance:** `python tools/evidence_check.py` with the reports present finds every source id in a report's verified id list.

**Since:** 2026-10-20 · owner · migrated

### D-R15 · What the evidence says about the app's shape

**Rule:** The app's shape follows the findings below.

**Values:**

| Finding | What the app does |
|---|---|
| Weekly totals per module decide long-term outcomes; frequency spreads the dose | counts weekly, per module, and never forms a combined score (R24) |
| Near-daily movement is justified by metabolism (insulin sensitivity raised about 24-48 h after activity; light movement is enough for the acute glucose effect) and by habit, not by more strength or a longer life | none stated |
| Consensus: no more than two days in a row without activity (Strong for diabetes or prediabetes, an inference otherwise) | none stated |
| Streaks, reset-style pressure, readiness or recovery scores and load-ratio alarms are not supported | has none (R115, R116) |
| Self-monitoring with self-chosen goals is the best-supported technique; app effects fade | stays useful without novelty |

**Acceptance:** none: each row is checked through the rule it names.

**Since:** 2026-10-20 · owner · migrated

### D-R16 · Commentary rules

**Rule:** Every evidence note follows the rules below. It sits behind a circled "i" that opens it in a bottom sheet, and it opens brief: the claim and its certainty, with who was studied, the study type and the sources under "Details".

**Values:**

| Rule | A note |
|---|---|
| 1 | states a finding, never an instruction: "Studies of trained adults found…", never "you should" |
| 2 | carries who was studied, certainty in plain words, and "association, not cause" when the evidence is observational |
| 3 | names its sources (author, year, PMID or DOI) one level down |
| 4 | appears only where it concerns what the person is doing, never as a notification |
| 5 | can be hidden by type; a setting chooses the level: off, brief or full; brief is the default for a new user |
| 6 | never carries invented or approximated evidence: every note comes from the evidence pack (R17) |
| 7 | never interprets the person's own data as a health condition: no "your glucose…", no BMI verdict |

**Evidence:** A placeholder note was caught in the round-2 mockups, which is why rule 6 exists.

**Acceptance:** `python tools/evidence_check.py` passes on the pack; on a note, the brief level shows claim and certainty only, and "Details" opens the rest.

**Since:** 2026-11-21 · owner · agreed rewording

### D-R17 · Evidence pack

**Rule:** The reports' claims ship with the app as versioned data, `evidence/claims.json`, in the format of `evidence/FORMAT.md`: id, claim, population, design, certainty, association or cause, sources, the features each explains, and the text with its hedging intact. The text is English; the Italian text comes with the translation pass (R58).

**Evidence:** The pack and its gate exist since commit `9b0c633`; the earlier spec still marked the pack as a proposal.

**Acceptance:** `python tools/evidence_check.py` passes in CI: every source id is in a report's verified id list, every feature id is one this spec names, and no note text exists without a claim id.

**Since:** 2026-11-21 · owner · agreed rewording

### D-R120 · Notes that span modules

**Rule:** Three evidence notes span modules: strength plus aerobic work together, lifting before cardio for strength, and two days without movement. Each shows only where the person is doing one of the modules it spans (R16 rule 4).

**Evidence:** Not checked: which of the three the pack already holds.

**Acceptance:** `python tools/evidence_check.py` passes with all three in the pack, and each note's chip appears only on screens of the modules it spans.

**Since:** 2026-11-21 · owner · agreed rewording

### A-R18 · Wellness, not medical software

**Rule:** General information about studies, never individual advice, keeps the app a wellness app and not medical software.

**Evidence:** Not checked: no legal review has been made; this is not legal advice.

**Since:** 2026-10-20 · owner · migrated

## 4. Training modules

### D-R19 · The five modules

**Rule:** The app is built from the five modules below, in this order; each can be used alone or stacked with others into a personal set-up. The settings inside a module reflect the evidence, and every one is editable.

**Values:**

| Module | Covers |
|---|---|
| Daily movement | movement spread through the day: sitting breaks, walks after meals, steps entered or counted, a quick amount |
| Strength | sessions, sets, routines, programmes, progression; isometric holds as an exercise type; "move fast" (power) as a set option |
| Stamina | sessions set out to do: steady sessions, intervals and timers, offline GPS recordings |
| Mobility and balance | mobility and balance routines, fall-prevention work (most relevant at 65 and over); self-checks as the person's own trend |
| Body measures | weight and measurements, and their changes over time |

**Acceptance:** The module choice offers these five in this order (`ModuleChoiceScreenTest`).

**Since:** 2026-10-29 · owner · migrated

### D-R20 · Body measures without verdicts

**Rule:** Body measures records the person's own numbers only, each an integer in its stated unit, its `day` set as a session's is (R30). No BMI, BMR, verdict or forecast is shown, stored or derived.

**Acceptance:** No table, column or code path computes a BMI, a BMR or a forecast; no screen shows one.

**Not:** BMI and BMR (harvest row `OS3`): they risk reading as a health verdict. A weight forecast (`OS6`): it projects a forecast. Both left out in `research/purpose-2026-10-20/compare.md`.

**Since:** 2026-10-20 · owner · migrated

### D-R21 · Modules chosen by the person

**Rule:** The person chooses which modules are on at first launch, with nothing assumed on, and changes the choice later in Settings.

**Acceptance:** A fresh install shows the module choice before any tab, with every module unticked (`ModuleGateTest`, `ModuleChoiceScreenTest`).

**Since:** 2026-10-20 · owner · migrated

### D-R22 · Additions inside modules

**Rule:** The modules carry the additions below.

**Values:**

| Module | Addition | Harvest row |
|---|---|---|
| Strength | isometric holds as a first-class exercise type | `KA3` |
| Strength | "move fast" (power) as a set option | `N-POWER` |
| Mobility and balance | self-checks (chair rise, balance hold, walking speed) as the person's own trend, never a verdict | `PL1` |
| Daily movement | steps, entered and counted by the phone's step sensor (needs the Physical-activity permission) | `N-STEPS` |
| Daily movement | movement reminders the person schedules | `IT1` |
| Daily movement | sitting-break prompts in hours the person chooses | `AK1` |

**Acceptance:** none: each addition is checked by the stage that builds it (R39).

**Since:** 2026-10-20 · owner · migrated

### D-R23 · Modules are tags

**Rule:** A module is a tag on an exercise, and an exercise may carry several. A session with sets carries the tags of its exercises, never stored on the session; a session without sets carries the one module it is logged under (R25). A session can be given several labels from a reusable list the person keeps, and labels tag sessions only.

**Evidence:** Schema 2's `exercise_module`, `label` and `session_label` tables (R28). Not checked: schema 2 has no column for a set-less session's module; it needs a schema change by R33.

**Acceptance:** A session with sets has the modules of its exercises' tags; a set-less session has exactly the module it was logged under; no exercise carries a label.

**Since:** 2026-11-21 · owner · agreed rewording

### D-R24 · This week counts a mixed session by its parts

**Rule:** "This week" counts each set, whole, under every tag of its exercise, and a session once under each module it touched. Totals stay per module and are never combined; an untagged exercise counts nowhere.

**Evidence:** `:core` `weekByModule`; mutants on counting only the first tag and on counting a session once per set each turned a test red (2026-10-21).

**Acceptance:** `CoreTest`'s week tests pass: `aMixedSessionCountsByItsParts`, `anExerciseWithTwoTagsCountsWhollyUnderEach`, `aSessionCountsOnceInAModuleHoweverManyOfItsSetsAreThere`, `anUntaggedExerciseCountsNowhereAndAnEmptyWeekIsEmpty`.

**Since:** 2026-10-21 · owner · migrated

### D-R25 · Each module's own units in This week

**Rule:** "This week" shows each module's totals in its own units, as below, never converted or combined. A session without sets (a walk, a quick amount) counts its time under the module it is logged under, chosen when it is logged.

**Why:** Steps and distance are units of measurement, but not in the way sets are (owner, 2026-10-21).

**Values:**

| Module | This week's totals |
|---|---|
| Daily movement | time, steps |
| Strength | sets, time |
| Stamina | time, distance |
| Mobility and balance | sets, holds, time |

**Evidence:** Not checked: `weekByModule` counts logged sets only, and a set-less session has no module column yet (R23).

**Acceptance:** A 30-minute walk logged under Daily movement adds 30 min to Daily movement's time and nothing to any other module; steps never appear as sets or minutes.

**Not:** Time only, with steps and distance left out of This week; sets only, as built before.

**Since:** 2026-11-21 · owner · agreed rewording

## 5. Data model

### D-R26 · Units

**Rule:** Every quantity is stored as an integer in the unit below.

**Values:**

| Quantity | Stored as |
|---|---|
| time | epoch milliseconds (UTC) plus the zone it was recorded in |
| duration | seconds |
| weight | grams (a weight entered with up to two decimals, 61.25 kg, fits) |
| girth | millimetres |
| distance | metres |
| coordinates | 1e-7 degrees |

**Acceptance:** Every column for these quantities is INTEGER in Room's exported `2.json`.

**Since:** 2026-10-20 · owner · migrated

### D-R27 · Ids

**Rule:** Every table has `id TEXT PRIMARY KEY`, holding a ULID from `:core` `Ids`. A restored or imported row then never collides with a local one, and a later Windows copy can merge.

**Acceptance:** `CoreTest`'s ULID tests pass, and every table in `2.json` has a text primary key `id`.

**Since:** 2026-10-19 · builder · migrated

### D-R28 · Tables, schema version 2

**Rule:** The database is schema version 2, with the tables below. Schema 1 was designed for the whole scope, so that no package needs a migration of logged data; schema 2 only adds to it.

**Values:**

| Table | Columns beyond id | Serves |
|---|---|---|
| `exercise` | name, kind (DYNAMIC, ISOMETRIC, TIMED, DISTANCE), sided (0/1, counted per side), archived, show_partial_reps (0/1, default 0; schema 2) | the floor; `CM2`; R32 |
| `routine` | name, weekdays (bit 0 = Monday … bit 6 = Sunday; 0 = unscheduled), position | the floor; `CM3`, `KA6` |
| `routine_item` | routine_id, exercise_id, position, target_sets, target_reps, target_weight_g, target_seconds, target_distance_m, rest_seconds | the floor; `LO4`-style rest, `GM6` |
| `session` | routine_id?, activity (free type, "Walk"), started_at?, ended_at?, zone, day, state (OPEN, FINISHED, STOPPED_EARLY), intensity? (1-5), note? | the floor; `FT2`, `GR4`, `FA13`, `KA2`, `GL1` (OPEN = in progress) |
| `logged_set` | session_id, exercise_id, position, side? (L, R), reps?, weight_g?, seconds?, distance_m?, rpe? (×10), rir?, done_at, power (0/1, default 0; schema 2), partial_reps (default 0; schema 2) | the floor; `LL2`, `GM2`, `BC1`; R22, R32 |
| `progression_rule` | routine_item_id, add_weight_g, add_reps, when (ALL_SETS_HIT) | `LL5`, `GM6` |
| `interval_preset` | name, prepare_s, work_s, rest_s, rounds, sets, set_rest_s, last_used_at? | `TX1`, `FA1`, `SH1` |
| `track_point` | session_id, t, lat_e7, lon_e7, alt_dm?, accuracy_m?, segment | `OT1`, `OT5` |
| `goal` | exercise_id? (null = all training), period (DAY, WEEK, MONTH), target, unit (SESSIONS, MINUTES, METRES, SETS) | `FA9` |
| `setting` | key, value | `OS8` (backup folder, schedule), the modules chosen, the evidence-note level, later preferences |
| `exercise_module` (schema 2) | exercise_id, module (DAILY_MOVEMENT, STRENGTH, STAMINA, MOBILITY_BALANCE, BODY_MEASURES); unique per pair | R23 |
| `label` (schema 2) | name, position, archived | R23 |
| `session_label` (schema 2) | session_id, label_id; unique per pair | R23 |
| `body_measure` (schema 2) | kind (WEIGHT, WAIST and others), value, unit (G, MM), taken_at, zone, day, note? | `OS1`, R20 |

**Evidence:** Schema 2 built 2026-10-21 with `2.json` exported; ten planted mutants each turned the intended test red.

**Acceptance:** Room's exported `2.json` holds exactly these tables and columns.

**Since:** 2026-10-21 · owner · migrated

### D-R29 · An amount is a session without times

**Rule:** An amount (`FA10`'s shape) is a session with `started_at` and `ended_at` null and `day` set. A day's total time is the union of its timed sessions plus the sum of its amounts (`:core` `dayTotalSeconds`).

**Acceptance:** `CoreTest`'s `overlappingSpansCountOnceAndAmountsAdd`, `aSpanInsideAnotherAddsNothing` and `emptyDayIsZero` pass.

**Since:** 2026-10-19 · builder · migrated

### D-R30 · The counting day

**Rule:** A session's `day` is the local date of `started_at` in `zone` (`FA13`), computed once by `:core` `countingDay` and stored. A later change of the phone's zone does not move history.

**Evidence:** Not checked: no test changes the zone after a session is stored.

**Acceptance:** `CoreTest.aSessionCountsOnTheDayItStartsInItsOwnZone` passes, and a stored session keeps its `day` after the phone's zone changes.

**Since:** 2026-10-19 · builder · migrated

### D-R31 · Only done sets are stored

**Rule:** `logged_set` rows exist only for sets that were done; an early stop keeps no rows for the sets it skipped (`KA2`).

**Acceptance:** Stopping a session early leaves only the sets marked done.

**Since:** 2026-10-19 · builder · migrated

### D-R32 · Partial reps

**Rule:** A partial rep is its own count, `partial_reps` beside `reps`, never converted: 7½ is reps 7 and partial_reps 1, five half-reps are reps 0 and partial_reps 5. The count is offered only on exercises where the person switches it on.

**Acceptance:** A set logged as 7 and 1 partial reads back as reps 7, partial_reps 1; an exercise with the switch off shows no partial count.

**Since:** 2026-10-20 · owner · migrated

### D-R33 · Migrations add, never rewrite

**Rule:** A new column goes last with a SQL default, so a fresh table matches a migrated one column for column. There is no destructive fallback: a failed migration stops the open with every cell, table shape and the version unchanged, nothing set aside and nothing wiped.

**Evidence:** A test builds a schema-1 file from Room's exported `1.json`, fills every table, migrates through the app's own open and compares every cell (`quote()`) before and after, plus the migrated shape against a fresh one, on the desktop and under Robolectric on the phone's path (`KeepFileOnCorruptionDriver`). A throwing statement, a migration Room's check refuses and a file a newer app wrote each leave the file as it was. A destructive fallback is caught by the Robolectric test only (2026-10-21).

**Acceptance:** `MigrationTest` passes on the desktop and `AndroidMigrationTest` under Robolectric.

**Since:** 2026-10-21 · owner · migrated

## 6. Safety and gates

### O-R34 · Nothing logged is lost

**Rule:** Backup (one file, and scheduled to a folder) and resuming a session after the app is killed are part of the first stage.

**Why:** Nothing logged may be lost.

**Acceptance:** Stage 1a is not done until the backup package (`KA9`, `OS8`) and resume after process death (`GL1`) pass their tests.

**Since:** 2026-11-21 · owner · agreed rewording

### D-R35 · The database never deletes itself

**Rule:** Android opens the database through `KeepFileOnCorruptionDriver`, because the stock driver's default handler deletes a corrupt file. `openOrRecover` sets an unopenable file aside as `<name>.unopenable-<stamp>` and opens a fresh one.

**Evidence:** A Robolectric test writes garbage and finds the bytes set aside, with the stock driver as the failing control.

**Acceptance:** That Robolectric test passes.

**Since:** 2026-10-19 · builder · migrated

### D-R36 · Backup format

**Rule:** A backup is one JSON file holding every table, `format` `sisyphusfit-backup`, `version` 2. Restore replaces everything in one transaction, and a version-1 backup still restores, its rows unchanged, the new columns at their defaults and the new tables empty.

**Evidence:** Pinned by a version-1 file the schema-1 app exported; mutants on restore outside a transaction and on a table missing from the backup list each turned a test red.

**Acceptance:** Export, restore into an empty database and export again give byte-identical files; the pinned version-1 file restores.

**Since:** 2026-10-21 · builder · migrated

### D-R37 · Gates

**Rule:** Every change passes the gates below at the times their rows give.

**Values:**

| Gate | Command | When |
|---|---|---|
| tests and lint | `./gradlew --offline check`: every test, Android lint with warnings as errors (the four version-currency ids informational) | every change; CI runs `./gradlew check` on every PR and push to main |
| evidence pack | `python tools/evidence_check.py` | in CI on every PR and push to main |
| design tokens | `python tools/tokens.py check` | in CI on every PR and push to main |
| publish check | `python ~/.claude/tools/publish_check.py .` (also the pre-push hook) | before every push |

**Acceptance:** `.github/workflows/ci.yml` runs the first three; the pre-push hook runs the fourth.

**Since:** 2026-10-28 · owner · migrated

## 7. Stages

### D-R38 · Thin slice, then deepening

**Rule:** A thin slice comes first, every module minimally usable on a shared base, then deepening stages ordered by scoring on the owner's axes. Each stage ships on its own, and a package ships whole. A stage is done when its tests and gates pass, it has been walked on the 6T, the owner has signed off its screens from mockups before they were built, and it is pushed after the publish check.

**Values:**

| Axis | Weight |
|---|---|
| evidence strength | equal |
| everyday usefulness | equal |
| effort | equal |
| foundation value | equal |

**Evidence:** `stages.py` (worksheet `research/stages-2026-10-20/`) scored the order with effort checked against the code. It is a plateau, not a verdict: neighbours differ by less than 0.05 and swap under ±50% weight changes; only stage 2 and the last group (body measures, self-checks, GPS) hold in every variant. Neighbours that tied were merged into one stage (owner: fewest stages compatible with stability).

**Acceptance:** none: a planning rule; each stage's own rules carry the checks.

**Since:** 2026-10-20 · owner · migrated

### D-R39 · Stage list

**Rule:** The stages are built in the order below. Stage 1a, the shared base, is proven by Strength and split from 1b, so that a mistake in the shared base is caught before four modules depend on it.

**Values:**

| Stage | Holds | Harvest rows |
|---|---|---|
| 1a | schema 2 (R28); the app shell; the module choice (R21); the app opens on Start (R83) | `X-MODULES` |
| 1a | exercises, routines, one session model across modules, logged per calendar date; resume after the app is killed | `N-EXERCISES`, `N-ROUTINES`, `N-SESSION`, `FT2`, `FA13`, `GL1` |
| 1a | history; "This week" per module (R24); no streak or missed-day count | `N-HISTORY`, `X-WEEK`, `FA15` |
| 1a | evidence notes, brief by default, from the pack (R16) | `X-EVIDENCE` |
| 1a | settings; the backup package: one-file backup and restore, scheduled backup to a chosen folder; no `INTERNET` permission | `X-SETTINGS`, `KA9`, `OS8`, `GM9` |
| 1a | Strength minimal: sets with the live comparison (R67) | `SG2` |
| 1b | Daily movement: a quick amount and the day total | `FA10`, `FA11` |
| 1b | Stamina: a steady session, live or entered afterwards, with pace | `FT3` |
| 1b | Mobility and balance: holds counted per side | `CM2` |
| 1b | Body measures: entering a measure | `OS1` |
| 2 | Strength detail: effort per set, RPE or reps in reserve; "move fast"; the hold timer | `BC1`, `N-POWER`, `KA3` |
| 3 | last sets shown when picking an exercise; one own-trend chart with no success or failure framing; weekday to-do list; steps entered and counted by the step sensor (the sensor row not yet priced) | `GM2`, `N-CHART`, `CM3`, `N-STEPS` |
| 4 | intervals package; movement reminders; sitting-break prompts | `TX1`, `TR5`, `FA5`, `FA1`, `IT1`, `AK1` |
| 5 | goals in plain words; closing a session with duration, intensity and comment; a launcher shortcut per routine or timer | `FA9`, `N-GOAL`, `GR4`, `TR2` |
| 6 | rest-timer package (R71, R74) | `GL2`, `LL4`, `KA5` |
| 7 | progression package: the "next time" proposal of R43 | `LL5`, `GM6` |
| 8 | early stop reviewed, unfinished sets skipped; quick intervals from a cycles stepper; body-measure chart | `KA2`, `SH1`, `OS2`, `OS4` |
| 9 | self-checks as own trend; body-measure changes | `PL1`, `OS5` |
| 10 | GPS package | `OT1`, `FA4`, `OT5`, `FT5` |
| 11 | Today, designed last (R40) | `KA6` |

**Acceptance:** none: an order of work; each stage is done by R38.

**Since:** 2026-11-21 · owner · migrated

### O-R40 · Today is designed last

**Rule:** Today is designed after every other screen.

**Why:** It depends on every other screen.

**Acceptance:** none: an order of work, kept by R39.

**Since:** 2026-11-21 · owner · agreed rewording

### D-R41 · A harvest row is read against the code first

**Rule:** Before a harvest row is built, it is read against the code.

**Why:** Its effort and frequency were estimated before any code existed.

**Acceptance:** none: a working practice; the stage's plan names the code each row touches.

**Since:** 2026-10-20 · unrecorded · migrated

### Q-R42 · The shape of Today

**Rule:** What shape does Today take?

**Default:** none proposed: the owner keeps it for Today's design round (R40), where the jobs of R01 and the built screens settle it with mockups; by purpose with a Start sheet (0.836) and a card per module in a hybrid (0.841) are a plateau in R45's scoring.

**Why:** The two leading purpose maps differ only in Today.

**Since:** 2026-11-21 · owner · agreed rewording

### D-R43 · Where suggestions stop

**Rule:** After a session, the app may offer one editable "next time" proposal, computed only from a progression rule the person sets (`progression_rule`); the app never picks the numbers. The proposal is an option in Settings, off by default.

**Why:** It applies the person's own rule, so it stays a record and not a prescription (R02, R117).

**Acceptance:** With the setting off, no proposal appears; with it on and every set hit, the proposal equals last time's numbers plus the rule's step, and editing it changes nothing logged.

**Not:** Recording and commenting only: progression would stay manual. A per-set proposal: R117 rules out per-set prescriptions.

**Since:** 2026-11-21 · owner · agreed rewording

## 8. Screens by purpose

### D-R44 · Screens grouped by purpose

**Rule:** Features are gathered into screens by shared purpose, with few overlaps and only where necessary. Modules are switches and content, not a screen tree. Every harvest row is placed on exactly one screen, declared shared with a reason, or left out with a reason.

**Values:**

| Grouping | Score |
|---|---|
| by purpose | 0.836 |
| hybrid | 0.841 |
| by module | 0.61 |

**Evidence:** Method ported from Factotum (`research/purpose-2026-10-20/`: `purpose-map-method.md`, the checker `purpose_map.py --map <json>` with a selftest, `score_maps.py`), on the owner's axes with equal weights: few overlaps, calm load per screen, taps to common tasks, purpose coherence. Factotum's Today and purpose map are a skeleton to adapt, not a blueprint.

**Acceptance:** `purpose_map.py --map` passes on the working map: all 162 harvest rows placed, shared or left out.

**Not:** Screens by module: it duplicates the session, history and exercise screens per module.

**Since:** 2026-10-20 · owner · migrated

### D-R45 · Purposes and screens

**Rule:** The purposes and screens below are the working list for mockups; a screen can still move when a mockup shows a problem.

**Values:**

| Purpose | Its job | Screens |
|---|---|---|
| What's next | see what today holds and start or log it | Today (R42), Start or log (the Start tab, R83) |
| Doing it | move, and have the app keep count | Session, Exercise in a session, Set details (sheet), Number entry (the phone's keyboard, R68), Interval timer, Recording, Stopping and finishing, Log afterwards |
| Looking back | see what was done | This week (a section per module), History, A recorded activity, Progress |
| Making it yours | choose modules and goals; shape routines, programmes, timers, exercises | Modules and goals, Your plans, Programme library, Timers and routes, Exercises |
| Knowing why | the evidence behind a feature, how a movement is done | Evidence notes |
| Looking after the app | settings, backup, import and export, privacy | Settings, Back up and restore, Import, export, delete |

**Evidence:** Full reasons for the rows left out are in `research/purpose-2026-10-20/compare.md`.

**Acceptance:** none: a working list; each screen's own rules carry the checks.

**Since:** 2026-10-20 · owner · migrated

### D-R46 · Doing and showing screens

**Rule:** A showing screen (This week, Settings, More) has no main action. A doing screen keeps one, in the form of R69.

**Acceptance:** The tab roots of This week, History and More draw no main bar.

**Since:** 2026-10-20 · owner · migrated

## 9. Screen rules

### D-R47 · Calm before complete

**Rule:** Zones are separated by tonal depth, not hue. A doing screen has one obvious primary action in its bottom zone, and anything that can grow collapses to one row.

**Acceptance:** Gate G1-G5 runs pass, and the owner signs the screen off on the 6T.

**Since:** 2026-10-20 · owner · migrated

### D-R48 · One home per thing

**Rule:** A summary links to the thing's own screen; nothing is edited in two places.

**Acceptance:** Every edit action in the app leads to one screen per thing.

**Since:** 2026-10-20 · owner · migrated

### D-R49 · One form per element

**Rule:** The same element has one form, place and wording on every screen: module card, row, set row, evidence-note chip.

**Acceptance:** The stone pieces in `ui/stone` are the only implementations of these elements, and their goldens match on every screen that uses them.

**Since:** 2026-10-20 · owner · migrated

### O-R50 · Touch areas and hierarchy

**Rule:** Visual size follows the function hierarchy, so not every control is visually 48 dp. Touch areas step up by importance on the ladder below, from its minimum to its maximum, with the main bar and the steppers on the upper steps; touch targets are at least 5 dp apart.

**Why:** 42 dp avoids overlap, or too much empty space, when two lines or buttons are close.

**Values:**

| Step | Balanced / compact | Used for |
|---|---|---|
| minimum | 42 dp | never below (gate G4) |
| 1 | 48 / 44 dp | chips, the smaller steppers (partials), icon buttons |
| 2 | 56 / 48 dp | rows, the main bar, the set steppers |
| 3 | 64-72 / 56-72 dp | module cards, the tab bar, the floating control |
| maximum | 72 dp | the rest round, which grows with font size (R80) |

**Evidence:** 42 dp is above WCAG 2.2 AA (24 px) and below Android's 48 dp guideline and WCAG AAA (44 px); Android's Accessibility Scanner flags it. The ladder meets `app-register-layout`'s hit-area requirement (a minimum, steps by importance, a maximum, no overlap), checked 2026-11-21; its sizes are the `size` tokens of `design/tokens.json`, not measured on rendered screens.

**Acceptance:** Gates G3 and G4 pass; the unit tests of the pieces check touch targets of at least 42 dp; every touch target's `size` token sits on one row of the ladder.

**Not:** 48 dp for every touch area: it leaves too much empty space between close lines. Three steps with the rest round as an exception: the owner kept the minimum and maximum as rows of their own.

**Since:** 2026-11-21 · owner · agreed rewording

### D-R51 · Judgement-free

**Rule:** Wording and charts never judge: no streaks, "Nothing here." when a list is empty, and charts and tracking are part of the app, never presented as success against failure. "No judgement" is how the app is built, never something it says, and mechanics a module has anyway (routines) are not named.

**Acceptance:** No screen text praises, blames or counts missed days; the empty state reads "Nothing here."

**Since:** 2026-11-21 · owner · agreed rewording

### D-R52 · Gates G1-G5

**Rule:** Every mockup and screen passes the gates below before it is shown, then is scored on the owner's axes with equal weights; a plateau is shown as a plateau.

**Values:**

| Gate | Checks |
|---|---|
| G1 | no overlaps |
| G2 | nothing clipped or wider than the screen |
| G3 | touch targets at least 5 dp apart |
| G4 | touch areas at least 42 dp |
| G5 | no lost text at font sizes 1.0, 1.3, 1.5 and 2.0 with long real values: nothing cut, clipped, covered by the rest round, the figure or the main bar, or crossing other text; a title's "…" with its full text on a tap is allowed, and text below the fold of a scrolling body counts as kept |
| axes | harmony, density, visual and functional hierarchy, reach and legibility, taps per set, at-a-glance clarity, consistency |

**Acceptance:** The gate script reports G1-G5 passing on every page of the screen at both densities, 360-412 dp and font sizes 1.0-2.0.

**Since:** 2026-10-27 · owner · migrated

### D-R53 · Greyscale

**Rule:** The app is greyscale until the palette is discussed with the owner.

**Acceptance:** Every colour role in `design/tokens.json` resolves to a grey or a black alpha.

**Since:** 2026-10-20 · owner · migrated

### D-R54 · Screen width

**Rule:** Screens fill the phone's width from 360 to 412 dp. Wider (large phones, foldables, landscape), the screen keeps a 412 dp column, centred, so its centre line stays the screen's centre.

**Evidence:** Not checked: the app has no 412 dp column yet.

**Acceptance:** At 600 dp wide the content column is 412 dp and centred on the screen.

**Since:** 2026-10-27 · owner · migrated

### D-R55 · Fit on the phone's app area

**Rule:** Fit is judged on the phone's own app area, not in a browser, whose bars bias it.

**Values:**

| Phone | App area | Status bar | Navigation | Density |
|---|---|---|---|---|
| OnePlus 6T | 384 × 755.6 dp | 80 px | three-button, 135 px | 450 dpi |

**Evidence:** The rest screen, the exercise screen and the hold rest fit with no overlaps, the main bar ending at the navigation bar (2026-10-27).

**Acceptance:** The goldens of the 6T's app area pass with and without the sheet.

**Since:** 2026-10-27 · owner · migrated

### D-R56 · Text follows the phone's font size

**Rule:** The type tokens and their line heights are in sp, so text follows the phone's font size up to 200% on Android's nonlinear curve; padding, gaps, icons and touch targets stay in dp, and no text has a fixed height. When text does not fit, it gives way as the rows below say.

**Values:**

| Element | When text does not fit |
|---|---|
| set value | shrinks to fit between its steppers, never below the compact token (set number 34, partials 20); then its unit moves under it, centred |
| header title | may end in "…", its full text on a tap |
| session clock and Finish | never shorten |
| main bar | may wrap to two lines |
| screen body | scrolls under the fixed main bar; the rest round's band moves down with the card at that font size, so R70's fixed height holds within one font size |
| card rows, chip, main bar | take their sizes as minimums |
| Android 14 curve at 2.0 | 11 → 22, 14 → 26, 16 → 28, 19 → 32, 23 → 35.5, 28 → 37.3, 40 → 46.9 dp (AOSP `FontScaleConverterFactory`) |

**Evidence:** Gated at 1.0, 1.3, 1.5 and 2.0 with long real values ("102.5 × 12", 142.25 kg, a 10:00 rest, "Single-leg Romanian deadlift", "Wednesday afternoon", 1:12:40) by G1-G5. Sources: Android 14's notes on nonlinear font scaling; WCAG 2.2 SC 1.4.4 (resize to 200% without loss; truncation acceptable when the full content is available on activation) and WCAG2ICT.

**Acceptance:** The unit tests check Android 14's curve at 2.0 and each piece's font-size behaviour; the goldens at 1.0, 1.3 and 2.0 pass.

**Since:** 2026-10-27 · owner · migrated

### D-R57 · Density

**Rule:** Density is balanced by default; compact is the person's option, as in the polish round's compact preview.

**Acceptance:** A fresh install draws balanced, and the compact setting switches every token to its compact value.

**Since:** 2026-10-21 · owner · migrated

### D-R58 · English first

**Rule:** The app's text and its mockups are written in English first. Italian comes later as a translation pass, with the evidence pack's Italian text and accent-aware sorting (a `Collator`); numbers then follow the phone's locale (57.5 or 57,5).

**Acceptance:** Every string the app shows is English.

**Since:** 2026-10-29 · owner · migrated

## 10. Type and tokens

### D-R59 · Typeface

**Rule:** All text and numbers are set in Sisifo (SIL OFL 1.1), carved from Ortica Linear for this app, from two bundled variable files with weight 300-700 and Carve 0-100 (R10). Carve is stepped by the type role's token, not the rendered size: 0 up to 16, 100 above, so a role keeps its carve at every font size. Its features are used as the rows below say.

**Values:**

| Feature | Used for |
|---|---|
| `tnum` | numbers that change in place |
| `ss01` | titles set in capitals: the inscriptional capitals |
| the 12° oblique file | emphasis in text, notes and hints, display accents; it keeps its role's size, weight and carve |

**Evidence:** Blind panel against the font search's finalists on ten axes: Sisifo 0.880, next Libertinus Sans 0.739, IBM Plex Sans 0.531. The stepped and ramped carve mappings tied (0.885 against 0.873) and the owner chose stepped.

**Acceptance:** The theme's unit tests check the variable axes and the carve by role.

**Not:** IBM Plex Sans: seven faces had scored within 0.002 of each other, and Sisifo won the blind panel; Plex is also a Reserved Font Name, so a subset would need renaming.

**Since:** 2026-10-27 · owner · migrated

### D-R60 · Type roles

**Rule:** Every text element takes one role below, balanced / compact. In the comparison card, "Set N" (label) and the column heads (eyebrow) are 14 sp and the values body at 16 sp, at both densities; the evidence-note chip is 11 sp on 16 sp lines in the secondary grey on every screen it appears.

**Values:**

| Role | Used for (stage 1a mockups) | Size sp | Line height sp | Weight | Carve | Features |
|---|---|---|---|---|---|---|
| label | secondary text, values beside a row | 13 / 12 | 20 / 16 | 400 | 0 | |
| eyebrow | section heads, dates, column heads, a note's fact names: tracked capitals | 13 / 12 | 16 / 16 | 500 | 0 | |
| body | running text and rows | 16 / 14 | 24 / 20 | 400 | 0 | |
| row title | names in rows, weekly amounts, the running timer | 19 / 17 | 24 / 24 | 500 | 100 | `tnum` on numbers |
| heading | screen titles, large secondary values | 23 / 20 | 28 / 24 | 500 | 100 | |
| exercise | the exercise's name on its own screen | 28 / 24 | 32 / 28 | 500 | 100 | |
| set number | the set's weight, reps and partials | 40 / 34 | 44 / 40 | 500 | 100 | `tnum` |

**Evidence:** From the polish round's type scale; `python tools/tokens.py check` compares this table with `design/tokens.json`.

**Acceptance:** `python tools/tokens.py check` passes.

**Not:** A 48 / 40 dp keypad readout: the app's own keypad is gone (R68), so it is not a token.

**Since:** 2026-10-27 · owner · migrated

### D-R61 · Text line boxes

**Rule:** Text is set in CSS line boxes, lines times the role's line height with glyphs overflowing, as the mockups were drawn and judged. At large font sizes the line box follows the text, keeping the role's line height over size.

**Evidence:** Compose otherwise keeps a font's full height where it is taller than the line: the 40 sp set number measured 54 dp in its 44 line. The owner kept the proportional spacing (body at 2.0: 42 dp, against 36 on the curve); the gallery's 1.3 and 2.0 goldens moved by line spacing only (2026-10-29).

**Acceptance:** The goldens at 1.3 and 2.0 pass, and wrapped lines never overlap.

**Not:** The line box scaled on Android's curve alone: wrapped lines overlapped at 1.3 and 2.0.

**Since:** 2026-10-29 · owner · migrated

### D-R62 · Design tokens

**Rule:** `design/tokens.json` is the one source of the app's visual values, in two layers: raw values (a grey ramp named by CIE L*, black at four alphas) and the roles screens use (colour: surface, fill, ink, line, shadow, rest; space, size, radius, stroke, elevation, motion, type); a screen never uses a raw value. When colour comes, only the roles' references change. The tables below are written from the file by `python tools/tokens.py md --write`, and the app's `Tokens.kt` by `python tools/tokens.py kotlin --write`.

**Values:**

<!-- tokens:begin (tools/tokens.py md --write) -->
Colour roles (screens use these; raw greys are named by CIE L*):

| Role | Raw | Value | Used for |
|---|---|---|---|
| surface.page | grey.93 | `#ebebeb` | the screen behind everything |
| surface.card | grey.98 | `#fafafa` | comparison card, facts, light pebbles (steppers, the rest round's control), the tab bar |
| surface.worn | grey.94 | `#f0f0f0` | module cards, week sections, the resume card and rows: cut left, worn right (R65) |
| surface.worn-light | grey.100 | `#ffffff` | the soft light toward their worn right side |
| surface.sunk | grey.88 | `#dfdfdf` | the bottom zone behind the main bar; secondary pills |
| fill.dark | grey.17 | `#2b2b2b` | every dark fill of the interface: main bar, tab bar line and underline, selected states (R65) |
| fill.dark-light | grey.38 | `#5a5a5a` | the faint light inside the main bar's dip |
| fill.on-dark | grey.100 | `#ffffff` | text and icons on fill.dark |
| ink.primary | grey.10 | `#1c1c1c` | text, icons, the current set's pebble outline |
| ink.secondary | grey.37 | `#575757` | secondary text, the evidence-note chip, tags |
| line.edge | grey.45 | `#6b6b6b` | outlines (flat rows), the 1.5 dp underline of a number you tap to type, the sheet's grab handle, switches |
| line.carved-dark | grey.45 | `#6b6b6b` | a carved line's cut: column heads, the chip's lines (3 dp, dark over light) |
| line.carved-light | grey.100 | `#ffffff` | a carved line's lit edge; the 0.7 dp light under object icons |
| line.spindle | grey.45 | `#6b6b6b` | History's carved date divider (owner 2026-10-28: the edge grey, not a grey of its own) |
| shadow.ground | grey.79 | `#c4c4c4` | the 2 dp ground under a pill |
| shadow.step | black.18 | `#0000002e` | the 1 dp shadow under light pebbles |
| shadow.float-near | black.14 | `#00000024` | a sheet's near shadow |
| shadow.float-far | black.20 | `#00000033` | a sheet's far shadow |
| shadow.scrim | black.42 | `#0000006b` | behind a sheet |
| rest.stone | grey.11 | `#1e1e1e` | the boulder and the figure (R75, R76) |
| rest.near-leg | grey.31 | `#4a4a4a` | the figure's near leg (R75) |
| rest.ring-track | rest.near-leg | `#4a4a4a` | the white ring's track before it fills: the near leg's grey (R77) |
| rest.ring | grey.100 | `#ffffff` | the ring and the time are the only white in the round (R74) |

Space and size, dp, balanced / compact:

| Token | dp | Used for |
|---|---|---|
| space.gutter | 16 / 12 | screen side margin |
| space.pad | 16 / 12 | inside a card or row |
| space.gap | 8 / 8 | between items in a list; compact keeps 8 as drawn and judged (the base compact scale had 4) |
| space.section | 24 / 16 | between sections |
| space.eyebrow | 8 / 4 | under a section head |
| space.foot | 16 / 12 | around the main bar in the bottom zone |
| space.note-head | 12 / 12 | an evidence note's sheet: under its eyebrow, before the claim (owner, 2026-10-28 walk) |
| size.row | 56 / 48 | minimum height of a row (R56: text may make it taller) |
| size.card | 72 / 56 | minimum height of a module card |
| size.button | 56 / 48 | minimum height of the main bar and buttons |
| size.hit | 48 / 44 | minimum touch target (G4 asks 42) |
| size.stepper | 56 / 48 | the − / + pebbles beside a set value |
| size.stepper-small | 48 / 44 | the smaller − / + pebbles (partials) |
| size.float | 64 / 56 | height of the floating running control as a pill (R69) |
| size.float-round | 72 / 64 | the floating running control as a round (R69: recordings, the interval timer) |
| size.rest-control | 72 / 72 | the rest round's touch target (R76); the boulder is 80 at font size 1.0 and grows with the time (R80) |
| size.tab-bar | 72 / 72 | the tab bar at font size 1.0; it grows with its labels (R66) |

Shape, line, elevation and motion (the same at both densities):

| Token | Value | Used for |
|---|---|---|
| radius.worn | 0 8 8 0 | cut on the left, worn on the right (R65) |
| radius.card | 14 dp | facts, segmented choices, label chips |
| radius.comparison | 4 dp | the comparison card (R65) |
| radius.box | 8 dp | a checkbox |
| radius.sheet | 22 dp | a sheet's top corners |
| radius.full | 999 dp | round ends: the grab handle, close and note chips |
| radius.pebble-pill | 52% 48% 50% 46% / 50% 54% 46% 50% | secondary buttons (R65) |
| radius.pebble-a | 49% 51% 56% 44% / 52% 46% 54% 48% | light pebbles: the reps steppers, the rest round's control, the current set's outline |
| radius.pebble-b | 55% 45% 52% 48% / 46% 52% 48% 54% | light pebbles: the weight and partials steppers |
| stroke.hair | 1 dp | unselected choices' outlines |
| stroke.selected | 2 dp | a selected box, segment or chip |
| stroke.line | 1.5 dp | the tab bar's line, the tap-to-type underline, the current set's pebble outline |
| stroke.icon | 2 dp | icons on the 24 grid (R64) |
| stroke.carved | 3 dp | a carved line, dark over light (R65) |
| stroke.underline | 4 dp | the current tab's underline on the valley floor (R66) |
| stroke.ring | 2.6 dp | the rest round's white ring and its track |
| elevation.ground | 0 2 0 0 #c4c4c4 | a pill standing on the ground |
| elevation.step | 0 1 0 0 #0000002e | a light pebble |
| elevation.edge-inset | inset 0 0 0 1 #6b6b6b | a flat row's outline |
| elevation.float | 0 6 16 0 #00000033, 0 1 4 0 #00000024 | a sheet |
| motion.valley | duration 250 ms, easing cubic-bezier(0.2, 0, 0, 1) | the tab valley sliding to the new tab (R66) |
| motion.step | duration 1000 ms | one push step of the figure (R74) |
| motion.stone-turn | duration 23000 ms | about one turn of the stone (R74) |
| motion.reduced | nothing turns or slides; the figure holds still; the ring and the time move once a second; the valley jumps (R74, R66) |  |
<!-- tokens:end -->

**Evidence:** `tools/tokens.py check` (CI runs it) checks every reference resolves, every raw value is used by a role, the density pairs, this spec's tables and `Tokens.kt`. Taken from the decided screens as drawn: rebuilt from the file, all 40 (20 per density) matched them pixel for pixel. The owner then merged #6a6a6a into #6b6b6b, drew History's date spindle in the edge grey and dropped the note's facts hairline. Kept apart though close: text #1c1c1c and the stone #1e1e1e, secondary text #575757 and the main bar's dip light #5a5a5a, the page #ebebeb and worn cards #f0f0f0, cards #fafafa and white. Contrast (WCAG 2.2, every text and icon on every surface it sits on, both densities): all pass, the lowest secondary text on the page at 6.1:1, where text needs 4.5:1 and icons 3:1 (2026-10-28).

**Acceptance:** `python tools/tokens.py check` passes.

**Since:** 2026-10-28 · owner · migrated

### D-R63 · Evidence note sheet spacing

**Rule:** The evidence note's sheet has `space.note-head` under its eyebrow and the claim in `type.note-claim`, as below.

**Values:**

| Density | Under the eyebrow | Claim |
|---|---|---|
| balanced | 12 dp | the row title's 19 sp on 28 sp lines |
| compact | 12 dp | 17 sp on the compact row title's 24 sp lines (1.41, against 1.47) |

**Evidence:** Chosen by the owner on the 6T walk of the foundation gallery, where the note read too dense (2026-10-28).

**Acceptance:** The note sheet's golden matches these values.

**Not:** As drawn, 8 dp and 24 sp lines: too dense; most, 16 dp and 30 sp lines.

**Since:** 2026-10-28 · owner · migrated

## 11. Visual language

### D-R64 · Icons

**Rule:** Icons are the app's own set of 28, drawn on a 24 grid with a 2 dp stroke and no fill. Object icons are faceted, while the path icons (navigation, chevrons, play, stop, timer, tabs) and − and + keep round caps. Holds and partial reps are shown as text, not markers.

**Values:**

| Icon | Drawing |
|---|---|
| Daily movement | one shoe print |
| Body measures | a pocket tape case with its tail and tab |
| Mobility and balance | a figure on one leg with the other reaching back, clear of Android's Accessibility figure |
| Stamina | a heart with a pulse line |
| Strength | a dumbbell |
| Backup | a floppy disk |
| Restore | the floppy inside an arrow turning back |
| Session clock | a wristwatch, so it cannot be taken for the circled "i" at 24 px |
| Start tab | a play sign in a rounded square, for the same reason |

**Evidence:** Chosen by the owner after scoring variants on legibility at 1x and distinctness (`research/screens-2026-10-20/polish/icons/review-2026-10-21`).

**Acceptance:** `IconsTest` and `StoneIconTest` pass.

**Since:** 2026-10-28 · owner · migrated

### D-R65 · Stone language

**Rule:** The app's elements take the stone's idea from Sisifo's carving: surfaces cut on one side and worn on the other, worn pebbles for things you press, and lines carved into the surface. Every dark fill of the interface is one grey, #2b2b2b, and each element takes the form below, the same in dp at both densities.

**Values:**

| Element | Form |
|---|---|
| main bar | square corners; its top edge sinks 8 dp across the middle third in one soft dip, lit faintly inside the dip |
| secondary buttons | pills as worn pebbles (each corner a slightly different radius) on a 2 dp ground shadow |
| steppers (− and +) | borderless light pebbles (#fafafa), − and + with different outlines, on a 1 dp shadow; the number you tap to type is underlined 1.5 dp in #6b6b6b |
| module cards, week sections, the resume card, rows | cut on the left (square), worn on the right (8 dp corners), #f0f0f0 lit softly toward the right |
| comparison card | 4 dp corners; its column heads sit on a carved line (3 dp); the current set is circled by a 1.5 dp pebble outline |
| History's date dividers | a carved 5 dp spindle that fades out over its first and last fifth |
| object icons | faceted (each curve drawn as two straight cuts, square ends and mitred corners) with a 0.7 dp light below them |
| tags | plain secondary text joined by a middle dot, no box: they sit lowest in the hierarchy |
| evidence-note chip | no outline; a carved 3 dp line above and below it |
| text and greys | text #1c1c1c, secondary text #575757; the boulder and the figure #1e1e1e; the figure's near leg and the ring's track #4a4a4a |

**Evidence:** Scored in three stages from the owner's per-element picks (69 designs, G1-G4 passing on all 20 screens): 0.785 on the eleven axes of `results_vl/axes2.json`, next the same with relief rows 0.783 and with the hill tab bar 0.773; the valley as a sunken surface 0.74; the design before and stock Material 0.592 (last, tied). Judged blind on 3× renders, then checked on the 6T at true size. At compact, G1-G5 pass on the exercise screens at 360-412 dp and font sizes 1.0-2.0 (2026-10-28).

**Acceptance:** The goldens of the gallery pass, and the pieces' sizes match the decided mockups within 1 dp.

**Not:** A vein on the comparison card, from its corner (passes a few dp from "TODAY") or down its middle gap (within 6 dp of text on 36 of 144 pages); relief rows.

**Since:** 2026-10-28 · owner · migrated

### D-R66 · The tab bar's valley

**Rule:** The tab bar has no top hairline: its top line (1.5 dp, #2b2b2b) dips under the current tab into a valley whose floor carries a 4 dp rounded underline, and the current tab's label stays where it is. When the tab changes, the valley slides along the line to the new tab in 250 ms, widening or narrowing to the new label, easing to a stop, while the screen itself changes at once. With reduced motion the valley jumps.

**Values:**

| Aspect | At font size 1.0 | At larger font sizes |
|---|---|---|
| valley | 172 dp wide at the top, 60 dp at the floor; at the first tab its outer wall runs off the screen | the floor is as wide as the current label, never under 60 dp, and runs 7 dp below it; the walls reach 32 dp across |
| floor | 3 dp above the screen's bottom edge | the same |
| bar height | 72 dp | grows with its labels, 10 dp above the icon and below the label: about 77 dp at 1.3, 80 at 1.5, 87 to 120 at 2.0 |
| label wrap | none | a label wraps to two lines ("This / week") when on one line it would come within 4 dp of its tab's edges, or when any valley would come within 2 dp of an icon or a label; the same rule for every tab, so the bar keeps one height; it happens at 2.0 on 360 and 384 dp screens, not at 412 |
| motion | 250 ms, cubic-bezier(0.2, 0, 0, 1) | the same |

**Evidence:** Measured on the four tab screens at both densities, 360, 384 and 412 dp and font sizes 1.0 to 2.0 (96 pages): no label or icon within 5.9 dp of the line; checked on the 6T. The motion is the owner's pick on the 6T from slide, fill and sink, slide with the underline behind, and none.

**Acceptance:** `TabBarTest` checks the valley's geometry and motion; the tab goldens pass.

**Not:** The hill tab bar (the current tab on a crest), 0.773; the valley as a sunken surface, 0.74 (the current tab read too quietly); walls 56 dp across, which ran over the next label at 1.5 and 2.0.

**Since:** 2026-10-28 · owner · migrated

## 12. Exercise screen and rest

### D-R67 · The live comparison

**Rule:** While sets are logged, the past is shown only as the live comparison, today against last time (`SG2`). Last time's numbers show from the start of each set, and today's fill in as they are logged, which also does `LL2`'s job.

**Acceptance:** On the exercise screen, last time's set N shows before set N is logged, and no other history appears.

**Since:** 2026-10-20 · owner · migrated

### D-R68 · Set input

**Rule:** − and + change a value by 1 at a time, and tapping the number opens the phone's own numeric keyboard; the app draws no keypad of its own. Weight accepts up to two decimals (61.25 kg), stored in grams.

**Acceptance:** Each stepper tap moves the value by 1; a tap on the number raises the system numeric keyboard; 61.25 kg is stored as 61250 g.

**Not:** The app's own keypad: replaced by the phone's keyboard.

**Since:** 2026-10-22 · owner · migrated

### D-R69 · The main bar and running controls

**Rule:** A doing screen's main action is a full-width bar at the bottom. While something runs, its running control takes the form its row below gives, never beside the main bar.

**Values:**

| Screen | Running control |
|---|---|
| exercise screen (sets and holds) | the rest round only (R70) |
| recordings (stage 10) | a floating stop; stopping takes a held press (`FA4`) |
| the interval timer (stage 4) | a floating stop; stopping takes a held press (`FA4`) |

**Evidence:** Scored best on the owner's five button axes (same place and easy reach, mis-tap risk, content covered, same place everywhere, label visible): the bar 0.973, a floating button 0.942, a bar with a secondary 0.821 (2026-10-21).

**Acceptance:** Every doing screen draws one main bar at the bottom; no running control sits beside it.

**Not:** A floating main action (0.942) and a bar with a secondary action (0.821).

**Since:** 2026-10-23 · owner · migrated

### D-R70 · The running control on the exercise screen

**Rule:** A hold starts from the main bar ("Start · set N"), and Done ends it and records its time, as it records a strength set; Done also starts the rest (R71). The only running control on this screen is the rest round, at the same height on every exercise screen of a density, in the band between the comparison card and the steppers, so nothing on the screen moves when it appears. A tap on the round opens −30 s and +30 s, a held press skips the rest, and after Done on a hold "Hold saved · Undo" shows for 10 s, so a stray Done can resume the hold (`FA4`'s undo).

**Evidence:** Nothing moves when the round appears, checked within 0.5 dp (2026-10-23).

**Acceptance:** The rest round's top is at the same y on every exercise screen of a density; an Undo within 10 s resumes the hold with its time.

**Not:** A pill, an inline panel, and a sheet from the bar (the sheet lay over live steppers and failed G1).

**Since:** 2026-10-23 · owner · migrated

### D-R71 · The rest timer

**Rule:** The rest starts on Done (`LL4`) and counts down the length that the first row below gives; it moves by −30 s or +30 s, or is skipped, and runs on with a live notification (`GL2`). How it ends is the person's choice in Settings: vibration, sound, notification, alone or together, kept as a `setting` key.

**Values:**

| Rest length, first found | Where it is kept |
|---|---|
| the plan's length (`LO4`) | `routine_item.rest_seconds` |
| the exercise's own length, set ahead in Edit exercise (R103) | `exercise.rest_seconds`, nullable, added with the exercise editor or stage 6, whichever is built first |
| the default in Settings (R72) | a `setting` key |

**Acceptance:** With all three set, the plan's length wins; with none but the default, the default runs; ±30 s moves the remaining time by 30 s.

**Not:** `KA5`'s 10 s steps.

**Since:** 2026-10-23 · owner · migrated

### D-R72 · Default rest length

**Rule:** The default rest length in Settings is 2 min, used when neither the plan nor the exercise sets one, and the person can change it.

**Why:** Rests over 2 min appear to maximise strength in trained lifters, while 60-120 s suffices for untrained people (Grgic 2018, PMID 28933024, in report 1), so 2 min suits both ends.

**Acceptance:** A fresh install's Settings shows a 2 min default rest, and a rest started with no plan or exercise length counts down from 2:00.

**Not:** 90 s: may be short for heavy lifts. 3 min: long for accessories.

**Since:** 2026-11-21 · owner · agreed rewording

### D-R73 · Session clock

**Rule:** The session clock is on every session and exercise screen, in the header row, left of Finish, at the same place and size on all six screens.

**Evidence:** Scored with consistency first: under the title it was 2 dp off on the session screens, and as first drawn it moved by 76 dp (2026-10-23).

**Acceptance:** The clock's bounds are the same on all six session and exercise screens.

**Not:** Under the title (2 dp off between screens); under Finish, which tied but took Finish's hit box down to 42 dp.

**Since:** 2026-10-23 · owner · migrated

### D-R74 · The rest round is the boulder

**Rule:** During a rest the round is the boulder, which the figure pushes up an implied 20° slope from its left; the round stays where R70 puts it, centred. They appear only during a rest, a small thing to look forward to, and both leave the screen at 0:00 or on a skip. Each part behaves as its row below says.

**Values:**

| Part | Behaviour |
|---|---|
| pace | one step per second; the stone turns as he pushes, its rim keeping pace with his feet (one turn about every 23 s), and his hands follow its outline |
| the top | he never reaches one, and the stone is never shown rolling back; no ground is drawn |
| during a set | no figure |
| −30 s or +30 s | the ring is redrawn as its share of the new length, and he keeps his pace |
| the ring | a white ring inside the round shows the rest elapsed, from the top, clockwise; the ring and the time are the only white |
| the time | upright in the middle |
| screen reader | skips the figure and the turning, and reads the round's own label |
| reduced motion | nothing turns, he holds still, and the ring and the time move once a second |

**Evidence:** Blind panel: the pushed round 0.798, a figure on a track round it 0.694-0.701, the plain round 0.673 (last) (2026-10-27).

**Acceptance:** During a rest the figure and the boulder are drawn, and after 0:00 or a skip neither is; with reduced motion the stone's angle never changes.

**Not:** The track versions (a summit to reach, a stone rolling back); a stone at the ring's head (not seen at true size); a centred pair (the round stays centred).

**Since:** 2026-10-27 · owner · migrated

### D-R75 · The figure

**Rule:** The figure is a side-view silhouette with adult proportions, his push following heavy sled-push data and his near leg a darker grey so the legs stay separable where they cross.

**Values:**

| Aspect | Value |
|---|---|
| proportions | about 7½ heads; about 37 dp standing, 17 dp bent into the push, about 5 dp per head |
| knee | bent about 52-69° through the stance |
| steps | 46 cm |
| trunk | low, following Muybridge's plate 393 and the owner's own sketch |
| near leg | #4a4a4a |
| body | #1e1e1e |

**Evidence:** Martínez-Serrano 2021, Osterwald 2021 (2026-10-27).

**Acceptance:** none: judged by eye on the 6T at true size; the figure is built with the rest-timer stage (stage 6).

**Since:** 2026-10-27 · owner · migrated

### D-R76 · The boulder's form

**Rule:** The boulder is the owner's own outline (rocc1, first cleaning, its bumps kept), 80 dp across, overhanging the 72 dp control by 4 dp; the touch target stays 72 dp. Nothing is on its face.

**Evidence:** Chosen in motion, with the figure walking: its many dents and peaks make the turn read faster than a smoother rim. Stills had put a reworked smooth rim ahead (0.844 against 0.650), but the owner judges turning only in motion. The owner called the choice provisional ("for now") on 2026-10-26.

**Acceptance:** The boulder is drawn 80 dp across at font size 1.0 on a 72 dp target.

**Not:** Marks on the face: busy and somewhat cartoonish in the page. A smoother rim: the turn reads slower.

**Since:** 2026-10-26 · owner · migrated

### D-R77 · What the figure and the stone share

**Rule:** The white ring's track, before it fills, is the near leg's #4a4a4a.

**Acceptance:** `rest.ring-track` resolves to `rest.near-leg` in `design/tokens.json`.

**Not:** A shaded side on the stone (a still light on a turning stone counters its motion), a carved edge light, an outline, a grain.

**Since:** 2026-10-26 · owner · migrated

### D-R78 · One centre line

**Rule:** The rest round, the set values and the comparison card's middle gap sit on the screen's centre; each value (weight, reps, partials) is centred, its unit hanging to its right. The comparison card is a table mirrored round its middle gap, laid out as below, and its outer columns share one reserved width, so nothing moves between screens.

**Values:**

| Element | Place |
|---|---|
| "Set N" | the card's left edge, 16 dp in |
| last time | its column's left |
| today | its column's right |
| the partial note ("+ 1 ½") | the card's right edge, 16 dp in; with no partial (a plank, or none that day) the slot stays empty |
| text sizes | R60 |
| evidence-note chip | 28 dp high, below the card's text in the hierarchy, between two carved lines; its touch target stays 48 dp |

**Evidence:** The pieces' sizes match the decided mockups within 1 dp, all 15; that check found the chip at an older 32 dp (2026-10-28).

**Acceptance:** The round's, the values' and the card gap's centres lie on the screen's centre on every exercise screen.

**Since:** 2026-10-27 · owner · migrated

### D-R79 · The comparison card at larger font sizes

**Rule:** At larger font sizes the comparison card keeps its text at full size and reflows in the steps below, each taken only when the step before does not fit.

**Values:**

| Step | Layout |
|---|---|
| 1 | the mirror of R78, its outer columns as wide as the longer of "Set N" and the note at that size |
| 2 | the column heads on a line each, last time's date over its column, "Today" over today's |
| 3 | per set, "Set N" and the partial note on one line (left and right edges) above the pair of values, last time left, today right |
| 4 | each value on its own line, last time left-aligned, today right-aligned |

**Evidence:** Owner's axes, equal weights: text grows as asked and hierarchy kept (measured), layout holds and reads at true size (blind panel of three, on the 6T's own pixels): reflow 0.766 (2026-10-27).

**Acceptance:** G1-G5 pass on the exercise screens at font sizes 1.0-2.0.

**Not:** Capping the card's text at 1.3 (0.760: its values stopped at 20 dp where 28 were asked, and its heads touched); shrinking it to keep the mirror (0.698); last time behind a tap (0.585).

**Since:** 2026-10-27 · owner · migrated

### D-R80 · The rest round grows with its time

**Rule:** The boulder, the white ring and the figure scale together by the time's font scale, so the time keeps its asked size inside the ring; a time still wider than the ring at that size ("10:00") steps down until it fits. The round stays on the centre line with the figure at its left, and at large font sizes it may sit below the first screenful and scroll with the body.

**Values:**

| Font size | Time | Scale | Boulder |
|---|---|---|---|
| 1.0 | 19 sp | 1.0× | 80 dp |
| 2.0 | 32 dp | 1.68× | about 135 dp, its touch target with it |

**Acceptance:** At font size 2.0 the boulder is about 135 dp and "10:00" fits inside the ring.

**Not:** The time shrinking inside an unchanged 80 dp boulder (0.760 against 0.762; the time stopped at 24 dp where 32 were asked); a time fixed in dp (0.749); the time beside the round (0.708).

**Since:** 2026-10-27 · owner · migrated

### D-R81 · The compact rest screen

**Rule:** At compact density the rest round, the figure and the comparison card keep their balanced sizes, so they are the same objects in both densities, and only the screen around them is denser. The round sits on the centre of the band between the card and the steppers, measured on the rest screen and kept on every exercise screen of the density.

**Values:**

| Element | At compact |
|---|---|
| boulder | 80 dp over the 72 dp control at font size 1.0; grows as R80 |
| figure | about 37 dp standing, turning at the same pace |
| round's top on the 6T | 378 dp down the app area on both rest screens (compact's band is taller) |
| card text | "Set N" and the column heads 14 sp, the values 16 sp on 24 sp rows, the chip 11 sp |
| card spacing | gaps 8 dp, outer columns 53 dp |
| time in the round | the row-title token (19 / 17 sp) |

**Evidence:** Blind panels on the 6T's app area at its own pixels (owner's axes: actual size .3, harmony .25, hierarchy .25, same screen as balanced .2, compact .2, figure .2, digits .15, seven measured axes .1). Size and height: balanced sizes on the band centre 0.894 against 0.770 for the next (scaled stone with the figure kept; balanced sizes under the card), 0.750 for scaled 0.89 on the centre, 0.586 for 0.78, the plain-round control 0.533 (last); the band centre beat "under the card" at every size. Card: balanced kept 0.783 (first on actual size and same screen, last on compact), tokens + 1 at 0.758, plain tokens 0.738, control 0.531 (last). The measured axes were a plateau (0.752-0.755) (2026-10-27).

**Acceptance:** G1-G5 pass on the compact exercise screens at 360-412 dp and font sizes 1.0-2.0.

**Not:** The round under the card (lost to the band centre at every size); scaled stones.

**Since:** 2026-10-27 · owner · migrated

## 13. App shell

### D-R82 · Navigation

**Rule:** A bottom tab bar holds Start · This week · History · More. One `NavHost` has a nested graph per tab, with the documented multiple-back-stacks pattern: each tab keeps its place, Back from another tab's root returns to Start, and Back from Start leaves the app. The screen changes at once; only the valley slides (R66).

**Evidence:** Navigation-compose 2.10.2, chosen by measured spikes (`research/spikes-2026-10-28/`, every option passing the same behaviour tests and lint) on the owner's axes, smooth path for the person and the build ½, familiarity ¼, least code ¼: navigation-compose 0.906, Navigation 3 0.713, an own navigator 0.625; equal weights keep the order. Navigation-compose is in maintenance mode (critical fixes only); Navigation 3 is where Google is going.

**Acceptance:** `AppShellTest` checks the tabs, Back, rotation and process death.

**Not:** Navigation 3 (0.713) and an own navigator (0.625).

**Since:** 2026-10-28 · owner · migrated

### D-R83 · The app opens on Start

**Rule:** Until Today exists, the app opens on Start ("Start or log").

**Acceptance:** After the module choice, a cold start shows the Start tab.

**Since:** 2026-10-20 · owner · migrated

### D-R84 · Wiring by hand

**Rule:** One `AppContainer` on the `Application` wires the app by hand, with a ViewModel per screen and its own factory. The database opens once, on first use, never on the main thread.

**Evidence:** By hand 0.875, Hilt 0.689, Koin 0.640 on the axes of R82 (2026-10-28).

**Acceptance:** `AppContainerTest` checks the open happens off the main thread and once.

**Not:** Hilt (0.689) and Koin (0.640).

**Since:** 2026-10-28 · owner · migrated

### D-R85 · Edge to edge

**Rule:** The band behind Android's three navigation buttons takes the app's own colour, as other apps do: the bar is transparent with dark buttons and Android's contrast scrim is off, so the tab bar's surface, or a doing screen's bottom zone, runs under it (`stoneEdgeToEdge()`). The module choice and the tab bodies keep clear of three-button navigation and cutouts at the sides (landscape).

**Acceptance:** The 6T golden shows the tab bar's surface under the navigation buttons; `SideInsetsTest` keeps the choice and a tab clear of side buttons.

**Since:** 2026-10-29 · owner · migrated

### D-R86 · The shared data core

**Rule:** The stores in `:data` behave as the rows below say, on schema 2 (R28). A database that cannot be opened is a state the screen shows, never a crash.

**Values:**

| Store | Behaviour |
|---|---|
| settings | the modules chosen are recorded only when the person chooses; none recorded means not chosen yet, and an empty choice is a choice |
| exercises | each with its module tags, replaced whole; `ExerciseStore.update` moves its read inside its transaction with the exercises plan |
| sessions and sets | one session open at a time, counted on its counting day, resumable after the app is killed |
| newer names | names a newer build may write are skipped, not fatal: an unknown module drops from its exercise in memory, an unknown kind leaves that exercise out of the list with its sets kept; a rewrite keeps them |
| first open | a first open left half-way (the person backs out) is kept, not repeated |

**Evidence:** 40 tests at the shell's merge (22 in `:data` on the desktop, 18 in `:app` under Robolectric), each restore and main-thread test turned red by a planted mutant; a fresh review of the whole branch fixed 3 findings, each test first; `./gradlew --offline check` ran 150 tests, 0 failed (2026-10-28).

**Acceptance:** The `:data` store tests (`SettingsTest`, `ExercisesTest`, `SessionsTest`) pass.

**Since:** 2026-10-29 · owner · migrated

### D-R87 · Starting while a session is open

**Rule:** Starting a session while one is open resumes the open one instead of failing: `SessionStore.start` returns the open session's id and adds nothing. A database rule allowing only one open session (a partial unique index, schema 3) waits for the backup plan, where a restore that brings two is settled.

**Acceptance:** `startingWhileOneIsOpenResumesIt` passes; it was red on the old refusal.

**Since:** 2026-10-29 · owner · migrated

### D-R88 · Tab screens at larger font sizes

**Rule:** The tab screens follow R56: rows, buttons, pills and their hit boxes take their sizes as minimums, Start's main bar wraps, and the body scrolls between the header and the fixed tab bar. When any module card's or history card's name would wrap beside its pill, every card on that screen puts its pill under its text, right-aligned, and the name takes the card's width. "Log afterwards" stays the last item of Start's list and scrolls with it.

**Evidence:** Gated on the four tab screens at both densities, 360, 384 and 412 dp, font sizes 1.0-2.0 and long names ("Start Wednesday afternoon", "Single-leg Romanian deadlift · Plank · Walk"): G1-G5 pass on all 144 pages, where the earlier tab pages failed G1, G2 and G4 from 1.3. Owner's axes, equal weights: the pill under with "Log afterwards" in the list 0.864 (2026-10-28).

**Acceptance:** G1-G5 pass on all 144 tab pages.

**Not:** Hiding the tab bar while scrolling down (the owner's pick on the 6T kept it fixed); "Log afterwards" fixed above the main bar (0.823); the whole card as the target with a chevron (0.823, lowest harmony); the pill beside (0.772).

**Since:** 2026-10-28 · owner · migrated

### D-R89 · Tab roots without screens

**Rule:** A tab whose screen is not built shows its name at its root.

**Acceptance:** Each unbuilt tab's root shows its name; `AppShellTest.eachTabShowsItsRoot` reaches every root.

**Since:** 2026-10-28 · owner · migrated

## 14. First launch

### D-R90 · The module choice screen

**Rule:** Before the tabs, with no tab bar, the app asks once for the modules, with the wording below. All five modules start unticked and all five are offered, the four not built in stage 1a marked "Arrives in a later update"; stepping forward with none ticked is a choice, and a tap anywhere on a card ticks it.

**Values:**

| Part | Wording |
|---|---|
| title | "Start your journey" |
| line | "Choose which paths you want to follow. The other choices will still be available in Settings." |
| bar | "Step forward" |
| Daily movement | "Creating mindful interludes to get up and break stillness" |
| Strength | "Working out with weights and planks" |
| Stamina | "Tracking cardio at every pace" |
| Mobility and balance | "Training range of motion and stability" |
| Body measures | "Tracking personal progress through body measurements and/or weight" |

**Evidence:** `ModuleTexts.kt` holds the owner's wording, the "Includes: …" texts among it; goldens `module-choice-{balanced,compact}-{100,200}`; `./gradlew --offline check` ran 190 tests, 0 failed, and a fresh review of the branch fixed 2 findings, each test first (2026-10-29).

**Acceptance:** `ModuleChoiceScreenTest` and the goldens pass.

**Not:** Body measures unticked while the others start ticked: every module starts unticked (R21).

**Since:** 2026-10-29 · owner · own words

### D-R91 · Module cards on the choice

**Rule:** Each card shows the module's icon before its name and an "info" chip (carved lines, `type.chip`) that opens a small raised card in place with "Includes: …"; Back or a tap elsewhere closes it, and with TalkBack the open card is announced while the list behind it waits. At large font sizes the cards follow R88's reflow, and the screen keeps clear of the sides as R85 says. A choice is recorded even when Back follows "Step forward" at once, and while it is read only the page is drawn.

**Evidence:** The owner's picks on the 6T: the "info" chip over two symbol marks ("info is more explanatory"), the icon before the name. A mutant on Back closing the card was caught (2026-10-29).

**Acceptance:** `ModuleChoiceScreenTest` checks the open card, Back and TalkBack; `ShellViewModelTest.aChoiceSurvivesLeavingAtOnce` checks the choice survives Back.

**Not:** The circled "i" as the mark: it stays the evidence notes' (R16).

**Since:** 2026-10-29 · owner · migrated

### D-R92 · When the data cannot be opened

**Rule:** When the data cannot be opened, on any launch, a screen says so in place of the choice or the tabs, with the wording below. "Try again" opens again; a failed open is not kept, and a damaged file is set aside, never removed.

**Values:**

| Part | Wording |
|---|---|
| title | "Your data couldn't be opened" |
| line | "Nothing has been deleted. Try again, and if it keeps happening, restart your phone." |
| bar | "Try again" |

**Evidence:** Goldens `cant-open-balanced-{100,200}` (2026-10-29).

**Acceptance:** `CantOpenTest` passes (the screen replaces the choice and the tabs; a retry while broken stays; a retry once it opens goes on), and `CantOpenScreenshotTest`'s goldens match.

**Not:** Restoring a backup from this screen: it waits for the backup plan.

**Since:** 2026-10-29 · owner · own words

## 15. Start or log

### D-R93 · Build order for Start

**Rule:** Exercises and routines are built first, then the Strength session, then Start. Another order may replace it when one serves better.

**Why:** Start's buttons lead to the session, which needs exercises, so every button works when it ships.

**Acceptance:** none: an order of work; Start's plan names the built screens each button opens.

**Since:** 2026-11-21 · owner · agreed rewording

### D-R94 · What Start shows

**Rule:** Start shows only the chosen modules, in R19's order, with the wording below. Start is built straight in the stone language, with no variants to score, and passes the gates of R52 before the owner judges it on the 6T walk.

**Values:**

| Case | Start shows |
|---|---|
| a chosen module not built yet | "Arrives in a later update" in place of its pill |
| no module chosen | "No paths chosen yet. You can choose them in Settings." and the main bar for a free session |

**Acceptance:** With Strength and Stamina chosen and only Strength built, Start shows two cards in that order, Stamina's reading "Arrives in a later update".

**Since:** 2026-10-29 · owner · migrated

### D-R95 · Session names

**Rule:** The main bar names a new session by its day and part of day in the phone's zone, as below, abbreviated ("Tue morn") only when the full name does not fit the screen.

**Values:**

| Part of day | Hours |
|---|---|
| morning | 05-12 |
| afternoon | 12-17 |
| evening | 17-21 |
| night | 21-05 |

**Acceptance:** A session started on a Tuesday at 08:00 is "Tuesday morning"; at 360 dp and font size 2.0 it shortens only if the full name does not fit.

**Since:** 2026-10-29 · owner · migrated

### D-R96 · The resume card

**Rule:** While a session is open, Start shows the resume card below and hides the module cards. "End/Save" records the session as stopped early (`KA2`).

**Values:**

| Part | Wording |
|---|---|
| title | "Ongoing session, paused · Tuesday morning" |
| line | "Paused at 08:31 · achieved 4 sets" |
| main bar | "Resume this session" |
| pill | "End/Save" |

**Acceptance:** With a session open, Start shows the resume card and no module card; "End/Save" leaves the session STOPPED_EARLY.

**Since:** 2026-10-29 · owner · own words

### D-R97 · Start's date line

**Rule:** Start's date line reads "Tuesday 20 October" in English; it follows the phone's locale once Italian comes in (R58).

**Acceptance:** On 2026-10-20 the line reads "Tuesday 20 October".

**Since:** 2026-10-29 · owner · migrated

## 16. Exercises and routines

### D-R98 · Exercises before routines

**Rule:** Exercises get their own plan first and routines the next. The routine editor, never mocked, gets a short mockup round before its plan.

**Acceptance:** none: an order of work.

**Since:** 2026-10-29 · owner · migrated

### D-R99 · Where exercises are made and managed

**Rule:** Exercises are made in the flow, from the session's "Add an exercise" sheet (Start, Strength, the session), and managed under More, grouped by module. Editing lives only under More.

**Acceptance:** The add sheet has no edit action; More lists the exercises grouped by module, each opening its editor.

**Not:** More, "Exercises and routines", ungrouped, as the first thing a new person does: clunky and low on the smooth path. Edit and Open pills on each row: "Open" was unclear, and the add sheet has no editing.

**Since:** 2026-10-30 · owner · migrated

### D-R100 · The add sheet

**Rule:** The session's "Add an exercise" sheet filters and adds as the rows below say. Each row adds as a whole, cued by an "Add" pill, and the sheet stays open after adding.

**Values:**

| Case | Behaviour |
|---|---|
| tags offered | the modules, plus the person's own categories (R103), never labels (R114); tags A-Z, exercises A-Z; tags no exercise carries are hidden |
| several tags | the exercises carrying all of them |
| no tag and no name | no list |
| a typed name with no tag | searches every exercise |
| "New exercise" | always shown; starts with the picked tags |
| an exercise already in the session | leaves the list |
| a row tapped | stays in place, its pill reading "Added" with a tick; a second tap takes it back out; rows never move while the sheet is open |
| "X added to this session." | screen-reader text only, never shown |

**Evidence:** Built into the trial as version 2 and adjusted in round 3 (2026-10-30): the visible line pushed the list down on the first add. With pill shapes whose ends change with width, tapping a row could wrap its details and move 11 of 14 rows below (2026-10-31), see R112.

**Acceptance:** Tapping a row twice leaves every row's position unchanged within 0.5 dp.

**Since:** 2026-11-21 · owner · migrated

### D-R101 · Exercise type and its lock

**Rule:** An exercise's type and its per-side setting stay editable. Once the exercise has logged sets, switching either asks first, with the wording below, and logged sets keep their numbers. The "Show partial reps" switch is offered for Dynamic exercises only.

**Why:** A wrong type, at creation or later, is fixed without deleting the exercise and its history.

**Values:**

| Part | Wording |
|---|---|
| question | "Squat has 20 logged sets. Change its type?" with the exercise's name and count |
| line | "Logged sets keep their numbers." |
| buttons | "Change" and "Keep" |

**Evidence:** Pattern 7.3 of the editor round, scored 2.93 against 2.91 for editable with no warning (`research/refs-2026-11-01/score/results.html`).

**Acceptance:** Switching the type of an exercise with logged sets shows the question, and "Keep" leaves the type unchanged; an Isometric exercise offers no partial-reps switch.

**Not:** Locked after the first logged set (settled 2026-10-30): a wrong type then needed a new exercise. Editable with no warning: one stray tap would change a logged exercise.

**Since:** 2026-11-21 · owner · agreed rewording

### D-R102 · Editing an exercise

**Rule:** Editing follows the rows below.

**Values:**

| Case | Behaviour |
|---|---|
| modules offered | the chosen modules, plus any the exercise already carries |
| a name already used, case ignored | refused: "You already have an exercise called Plank." |
| archive | a quiet pill at the foot of Edit; archived exercises leave the list, their sets stay; no archived list yet |
| Back with unsaved changes | a one-line sheet "Discard your changes?" with Discard and Keep editing |
| look | the stone language, judged on the 6T |

**Acceptance:** Saving "plank" beside "Plank" shows the refusal; an archived exercise is gone from the list with its sets kept in History.

**Since:** 2026-10-29 · owner · own words

### D-R103 · Exercise editor fields

**Rule:** The editor has, besides name, type, per side and modules, the optional fields below.

**Values:**

| Field | Behaviour |
|---|---|
| Default rest | optional; None · 30 s · 60 s · 90 s · 2 min · 3 min |
| Notes | free text (self-notes, cues, equipment), readable from inside the session; whether it is also edited there, against R48 and R99, is settled in the Strength session plan |
| Categories | the person's own words (dumbbells, core, push, legs), several per exercise, apart from modules and session labels; typed into a field that shows nothing until the first letter, then existing ones matching the start of any word, A-Z, and a "New: X" row, case ignored; none to start with |

**Evidence:** Not checked: schema 2 has no column for notes or categories, and `exercise.rest_seconds` is not yet added (R71); the exercises plan brings the schema change, by R33.

**Acceptance:** Plank can be made with 60 s rest, a note and the category "core", and the session shows its note.

**Not:** The editor as one sentence whose underlined words are its controls: the form beat it in the trial (2026-10-30).

**Since:** 2026-10-30 · owner · migrated

### Q-R104 · How the type is chosen in the editor

**Rule:** How does the editor ask for an exercise's type?

**Default:** Pattern 1.1, asked before the form (3.17): "New exercise" opens a dialog "What kind of exercise?" with the four types, and the form shows the type as a plain line with "Change". Changes: the editor's top and the add sheet's "New exercise" step. Next: 1.4, the inline segmented bar of editor A (3.06).

**Why:** Scored on the owner's axes from outside references (`research/refs-2026-11-01/score/results.html`); the owner picks next.

**Since:** 2026-11-01 · builder · own words

### Q-R105 · How modules are picked in the editor

**Rule:** How does the editor show and pick an exercise's modules?

**Default:** Pattern 2.2, filter chips that wrap (3.23), as in editor A. Changes: the modules control only.

**Why:** Scored on the owner's axes (`research/refs-2026-11-01/score/results.html`).

**Since:** 2026-11-01 · builder · own words

### Q-R106 · How categories are picked in the editor

**Rule:** How does the editor show and pick categories?

**Default:** Pattern 3.3, chips in the form plus a picker over it (3.28): a "Categories" row with the picked chips and "+ Add" opening a sheet with a search box, every category as a checkbox row A-Z, a "Create x" row, and Done. Changes: the type-ahead of R103 gives way to the picker sheet.

**Why:** Scored on the owner's axes (`research/refs-2026-11-01/score/results.html`).

**Since:** 2026-11-01 · builder · own words

### Q-R107 · How optional fields show

**Rule:** How does the editor show its optional fields (categories, notes, default rest)?

**Default:** Pattern 4.1, everything up front with the optional ones marked "(optional)" (3.26). Changes: the marks only. Next: 4.2, value rows that open sheets (3.17), which wins on the judged axes.

**Why:** Scored on the owner's axes (`research/refs-2026-11-01/score/results.html`).

**Since:** 2026-11-01 · builder · own words

### Q-R108 · How the default rest is picked

**Rule:** How does the editor pick the default rest?

**Default:** Pattern 5.1, a value row "Default rest · None ›" opening a list of the six choices, a tap picks and closes (3.37). Changes: the rest chips of editor A become one row. Next: 5.2, the chips inline (3.34), within the plateau.

**Why:** Scored on the owner's axes (`research/refs-2026-11-01/score/results.html`).

**Since:** 2026-11-01 · builder · own words

### Q-R109 · How notes are written

**Rule:** How does the editor hold notes?

**Default:** Pattern 6.3, a box in the form that grows (3.56), the only clear win of the round. Changes: none to editor A.

**Why:** Scored on the owner's axes (`research/refs-2026-11-01/score/results.html`).

**Since:** 2026-11-01 · builder · own words

### Q-R111 · How the add sheet adds

**Rule:** How does the add sheet take several exercises?

**Default:** Pattern A.1, the checklist that stays open with rows that stay put (3.14), as R100 has it. Changes: none. Next: A.4, tick several then confirm with "Add 2" (3.12).

**Why:** Scored on the owner's axes (`research/refs-2026-11-01/score/results.html`).

**Since:** 2026-11-01 · builder · own words

### Q-R112 · Pill and chip shapes

**Rule:** What shape do pills and chips take, and how does a picked chip show?

**Default:** Ends: pattern P.2, a small-radius rounded rectangle (8 dp) sized to its label (3.18), though the judges preferred P.1's round ends. Picked state: P.4, the colour changes and the width stays (2.88), which keeps every row of R100 still. Changes: `radius.pebble-pill` and the pills of R65.

**Why:** `radius.pebble-pill` is in % of the pill's size, so the ends change shape with width and look uneven across label lengths; the owner is rethinking the pebble pills.

**Since:** 2026-11-01 · builder · own words

### D-R113 · How the add sheet's search reaches categories

**Rule:** In the add sheet, a typed word matches exercise names and categories alike ("legs" finds Lunge), with no mode to choose.

**Evidence:** The trial measured it at 3 taps against 4 for a switch by the search box, with the same 227 dp of scrolling (2026-10-31).

**Acceptance:** Typing "legs" with no tag picked lists every exercise named or categorised with a word starting "legs".

**Not:** A switch by the search box between names and categories: one tap more.

**Since:** 2026-11-21 · owner · agreed rewording

### D-R114 · Categories and labels

**Rule:** Labels tag sessions only, and categories tag exercises only; they are two separate lists of the person's own words.

**Acceptance:** No exercise carries a label and no session a category; the add sheet's tags are modules and categories.

**Not:** One merged list of words for sessions and exercises: it needs a schema change and mixes two jobs.

**Since:** 2026-11-21 · owner · agreed rewording

## Acceptance list

- R02 · Records and comments, never prescribes: `python tools/evidence_check.py` passes: its wording check refuses instructions, promises and second person in every note.
- R03 · Platform: `./gradlew --offline check` compiles and tests `:core` and `:data` on the desktop JVM.
- R04 · Package name: `applicationId` in `app/build.gradle.kts` is `com.hazia.sisyphusfit`.
- R05 · Repository and local research: `research/` is in `.gitignore`, and `python ~/.claude/tools/publish_check.py .` passes before every push.
- R06 · Gradle modules: `settings.gradle.kts` includes exactly these modules, and each module's dependencies stay within its row.
- R07 · No account, no network: The merged manifest declares no `INTERNET` permission.
- R09 · Compose Foundation only: No `androidx.compose.material` artifact is on the app's dependency list; the gallery builds only in the debug variant (`GalleryActivity` under `app/src/debug`).
- R10 · Bundled font files: `FontFilesTest.fontFilesAreTheRelease` matches both hashes.
- R11 · Android's own backup: `app/src/main/res/xml/data_extraction_rules.xml` excludes every domain from `cloud-backup` and leaves `device-transfer` whole.
- R13 · Signing key: `app/build.gradle.kts` declares no `signingConfig`, and no keystore is in the repository.
- R14 · Evidence sources: `python tools/evidence_check.py` with the reports present finds every source id in a report's verified id list.
- R16 · Commentary rules: `python tools/evidence_check.py` passes on the pack; on a note, the brief level shows claim and certainty only, and "Details" opens the rest.
- R17 · Evidence pack: `python tools/evidence_check.py` passes in CI: every source id is in a report's verified id list, every feature id is one this spec names, and no note text exists without a claim id.
- R120 · Notes that span modules: `python tools/evidence_check.py` passes with all three in the pack, and each note's chip appears only on screens of the modules it spans.
- R19 · The five modules: The module choice offers these five in this order (`ModuleChoiceScreenTest`).
- R20 · Body measures without verdicts: No table, column or code path computes a BMI, a BMR or a forecast; no screen shows one.
- R21 · Modules chosen by the person: A fresh install shows the module choice before any tab, with every module unticked (`ModuleGateTest`, `ModuleChoiceScreenTest`).
- R23 · Modules are tags: A session with sets has the modules of its exercises' tags; a set-less session has exactly the module it was logged under; no exercise carries a label.
- R24 · This week counts a mixed session by its parts: `CoreTest`'s week tests pass: `aMixedSessionCountsByItsParts`, `anExerciseWithTwoTagsCountsWhollyUnderEach`, `aSessionCountsOnceInAModuleHoweverManyOfItsSetsAreThere`, `anUntaggedExerciseCountsNowhereAndAnEmptyWeekIsEmpty`.
- R25 · Each module's own units in This week: A 30-minute walk logged under Daily movement adds 30 min to Daily movement's time and nothing to any other module; steps never appear as sets or minutes.
- R26 · Units: Every column for these quantities is INTEGER in Room's exported `2.json`.
- R27 · Ids: `CoreTest`'s ULID tests pass, and every table in `2.json` has a text primary key `id`.
- R28 · Tables, schema version 2: Room's exported `2.json` holds exactly these tables and columns.
- R29 · An amount is a session without times: `CoreTest`'s `overlappingSpansCountOnceAndAmountsAdd`, `aSpanInsideAnotherAddsNothing` and `emptyDayIsZero` pass.
- R30 · The counting day: `CoreTest.aSessionCountsOnTheDayItStartsInItsOwnZone` passes, and a stored session keeps its `day` after the phone's zone changes.
- R31 · Only done sets are stored: Stopping a session early leaves only the sets marked done.
- R32 · Partial reps: A set logged as 7 and 1 partial reads back as reps 7, partial_reps 1; an exercise with the switch off shows no partial count.
- R33 · Migrations add, never rewrite: `MigrationTest` passes on the desktop and `AndroidMigrationTest` under Robolectric.
- R34 · Nothing logged is lost: Stage 1a is not done until the backup package (`KA9`, `OS8`) and resume after process death (`GL1`) pass their tests.
- R35 · The database never deletes itself: That Robolectric test passes.
- R36 · Backup format: Export, restore into an empty database and export again give byte-identical files; the pinned version-1 file restores.
- R37 · Gates: `.github/workflows/ci.yml` runs the first three; the pre-push hook runs the fourth.
- R43 · Where suggestions stop: With the setting off, no proposal appears; with it on and every set hit, the proposal equals last time's numbers plus the rule's step, and editing it changes nothing logged.
- R44 · Screens grouped by purpose: `purpose_map.py --map` passes on the working map: all 162 harvest rows placed, shared or left out.
- R46 · Doing and showing screens: The tab roots of This week, History and More draw no main bar.
- R47 · Calm before complete: Gate G1-G5 runs pass, and the owner signs the screen off on the 6T.
- R48 · One home per thing: Every edit action in the app leads to one screen per thing.
- R49 · One form per element: The stone pieces in `ui/stone` are the only implementations of these elements, and their goldens match on every screen that uses them.
- R50 · Touch areas and hierarchy: Gates G3 and G4 pass; the unit tests of the pieces check touch targets of at least 42 dp; every touch target's `size` token sits on one row of the ladder.
- R51 · Judgement-free: No screen text praises, blames or counts missed days; the empty state reads "Nothing here."
- R52 · Gates G1-G5: The gate script reports G1-G5 passing on every page of the screen at both densities, 360-412 dp and font sizes 1.0-2.0.
- R53 · Greyscale: Every colour role in `design/tokens.json` resolves to a grey or a black alpha.
- R54 · Screen width: At 600 dp wide the content column is 412 dp and centred on the screen.
- R55 · Fit on the phone's app area: The goldens of the 6T's app area pass with and without the sheet.
- R56 · Text follows the phone's font size: The unit tests check Android 14's curve at 2.0 and each piece's font-size behaviour; the goldens at 1.0, 1.3 and 2.0 pass.
- R57 · Density: A fresh install draws balanced, and the compact setting switches every token to its compact value.
- R58 · English first: Every string the app shows is English.
- R59 · Typeface: The theme's unit tests check the variable axes and the carve by role.
- R60 · Type roles: `python tools/tokens.py check` passes.
- R61 · Text line boxes: The goldens at 1.3 and 2.0 pass, and wrapped lines never overlap.
- R62 · Design tokens: `python tools/tokens.py check` passes.
- R63 · Evidence note sheet spacing: The note sheet's golden matches these values.
- R64 · Icons: `IconsTest` and `StoneIconTest` pass.
- R65 · Stone language: The goldens of the gallery pass, and the pieces' sizes match the decided mockups within 1 dp.
- R66 · The tab bar's valley: `TabBarTest` checks the valley's geometry and motion; the tab goldens pass.
- R67 · The live comparison: On the exercise screen, last time's set N shows before set N is logged, and no other history appears.
- R68 · Set input: Each stepper tap moves the value by 1; a tap on the number raises the system numeric keyboard; 61.25 kg is stored as 61250 g.
- R69 · The main bar and running controls: Every doing screen draws one main bar at the bottom; no running control sits beside it.
- R70 · The running control on the exercise screen: The rest round's top is at the same y on every exercise screen of a density; an Undo within 10 s resumes the hold with its time.
- R71 · The rest timer: With all three set, the plan's length wins; with none but the default, the default runs; ±30 s moves the remaining time by 30 s.
- R72 · Default rest length: A fresh install's Settings shows a 2 min default rest, and a rest started with no plan or exercise length counts down from 2:00.
- R73 · Session clock: The clock's bounds are the same on all six session and exercise screens.
- R74 · The rest round is the boulder: During a rest the figure and the boulder are drawn, and after 0:00 or a skip neither is; with reduced motion the stone's angle never changes.
- R76 · The boulder's form: The boulder is drawn 80 dp across at font size 1.0 on a 72 dp target.
- R77 · What the figure and the stone share: `rest.ring-track` resolves to `rest.near-leg` in `design/tokens.json`.
- R78 · One centre line: The round's, the values' and the card gap's centres lie on the screen's centre on every exercise screen.
- R79 · The comparison card at larger font sizes: G1-G5 pass on the exercise screens at font sizes 1.0-2.0.
- R80 · The rest round grows with its time: At font size 2.0 the boulder is about 135 dp and "10:00" fits inside the ring.
- R81 · The compact rest screen: G1-G5 pass on the compact exercise screens at 360-412 dp and font sizes 1.0-2.0.
- R82 · Navigation: `AppShellTest` checks the tabs, Back, rotation and process death.
- R83 · The app opens on Start: After the module choice, a cold start shows the Start tab.
- R84 · Wiring by hand: `AppContainerTest` checks the open happens off the main thread and once.
- R85 · Edge to edge: The 6T golden shows the tab bar's surface under the navigation buttons; `SideInsetsTest` keeps the choice and a tab clear of side buttons.
- R86 · The shared data core: The `:data` store tests (`SettingsTest`, `ExercisesTest`, `SessionsTest`) pass.
- R87 · Starting while a session is open: `startingWhileOneIsOpenResumesIt` passes; it was red on the old refusal.
- R88 · Tab screens at larger font sizes: G1-G5 pass on all 144 tab pages.
- R89 · Tab roots without screens: Each unbuilt tab's root shows its name; `AppShellTest.eachTabShowsItsRoot` reaches every root.
- R90 · The module choice screen: `ModuleChoiceScreenTest` and the goldens pass.
- R91 · Module cards on the choice: `ModuleChoiceScreenTest` checks the open card, Back and TalkBack; `ShellViewModelTest.aChoiceSurvivesLeavingAtOnce` checks the choice survives Back.
- R92 · When the data cannot be opened: `CantOpenTest` passes (the screen replaces the choice and the tabs; a retry while broken stays; a retry once it opens goes on), and `CantOpenScreenshotTest`'s goldens match.
- R94 · What Start shows: With Strength and Stamina chosen and only Strength built, Start shows two cards in that order, Stamina's reading "Arrives in a later update".
- R95 · Session names: A session started on a Tuesday at 08:00 is "Tuesday morning"; at 360 dp and font size 2.0 it shortens only if the full name does not fit.
- R96 · The resume card: With a session open, Start shows the resume card and no module card; "End/Save" leaves the session STOPPED_EARLY.
- R97 · Start's date line: On 2026-10-20 the line reads "Tuesday 20 October".
- R99 · Where exercises are made and managed: The add sheet has no edit action; More lists the exercises grouped by module, each opening its editor.
- R100 · The add sheet: Tapping a row twice leaves every row's position unchanged within 0.5 dp.
- R101 · Exercise type and its lock: Switching the type of an exercise with logged sets shows the question, and "Keep" leaves the type unchanged; an Isometric exercise offers no partial-reps switch.
- R102 · Editing an exercise: Saving "plank" beside "Plank" shows the refusal; an archived exercise is gone from the list with its sets kept in History.
- R103 · Exercise editor fields: Plank can be made with 60 s rest, a note and the category "core", and the session shows its note.
- R113 · How the add sheet's search reaches categories: Typing "legs" with no tag picked lists every exercise named or categorised with a word starting "legs".
- R114 · Categories and labels: No exercise carries a label and no session a category; the add sheet's tags are modules and categories.
- R115 · No streaks: No table or column counts days in a row or missed days, and no screen shows one.
- R116 · No readiness or load alarms: No screen or note shows a readiness, recovery or load-ratio value.
- R117 · No prescriptions or automatic deloads: No screen proposes a set's weight or reps by itself; the only proposal is R43's, from the person's own rule.
- R118 · No profiles or connected sensors: The merged manifest declares no Health Connect or Bluetooth permission.

## Out of scope

### D-R115 · No streaks

**Rule:** No streak or missed-day count is shown, stored or derived (`FA15`), and a missed day changes nothing.

**Why:** Reset-style pressure is not supported by the evidence (R15).

**Acceptance:** No table or column counts days in a row or missed days, and no screen shows one.

**Since:** 2026-10-20 · owner · migrated

### D-R116 · No readiness or load alarms

**Rule:** The app shows no readiness or recovery score ("muscle recovered") and no load-ratio injury alarm.

**Why:** The evidence does not support them (R15); showing muscles as "recovered" is a readiness claim (report rule 23).

**Evidence:** Harvest row `JF3`, left out in `research/purpose-2026-10-20/compare.md`.

**Acceptance:** No screen or note shows a readiness, recovery or load-ratio value.

**Since:** 2026-10-20 · owner · migrated

### D-R117 · No prescriptions or automatic deloads

**Rule:** The app gives no per-set prescription and no automatic deload.

**Why:** It records and comments, never prescribes (R02); automatic deloads contradict report rules 9-10.

**Evidence:** Harvest row `IV1`, left out in `research/purpose-2026-10-20/compare.md`.

**Acceptance:** No screen proposes a set's weight or reps by itself; the only proposal is R43's, from the person's own rule.

**Since:** 2026-11-21 · owner · migrated

### D-R118 · No profiles or connected sensors

**Rule:** The app has no multi-person profiles, no Health Connect and no Bluetooth sensors.

**Why:** The owner did not choose them. The app is for one person; sensing is not the active ingredient (report rule 60) and a sensor adds a permission and a pairing flow; Health Connect shares data with other apps.

**Evidence:** Harvest rows `SH5`, `RU4`, `OS7` and `CA1`, left out in `research/purpose-2026-10-20/compare.md`.

**Acceptance:** The merged manifest declares no Health Connect or Bluetooth permission.

**Since:** 2026-10-20 · owner · migrated

### D-R119 · No hidden duplicates

**Rule:** No hidden swipe duplicates a visible control.

**Why:** Swipe to repeat a set does with a hidden gesture a job visible controls already do (`LO1`, `LL1`).

**Evidence:** Harvest row `SG1`, left out in `research/purpose-2026-10-20/compare.md`.

**Acceptance:** none: checked in each screen's review.

**Since:** 2026-10-20 · owner · migrated
