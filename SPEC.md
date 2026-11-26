# SisyphusFit spec

Scope: an offline Android app, package `com.hazia.sisyphusfit`, for organising and tracking near-daily movement and training in modules that can be used alone or stacked.

History: `SPEC-history.md`, one line per change. It is never a source of current rules.

Kinds: O owner rule · D decided · I inherited · A assumed · Q open.

## Rule index

| ID | Name | Section |
|---|---|---|
| D-R01 | Jobs and how often | 1. Purpose and jobs |
| D-R125 | Purpose profile | 1. Purpose and jobs |
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
| D-R28 | Tables | 5. Data model |
| D-R121 | Schema 3 additions | 5. Data model |
| D-R29 | An amount is a session without times | 5. Data model |
| D-R30 | The counting day | 5. Data model |
| D-R31 | Only done sets are stored | 5. Data model |
| D-R32 | Partial reps | 5. Data model |
| D-R33 | Migrations add, never rewrite | 5. Data model |
| O-R34 | Nothing logged is lost | 6. Safety and gates |
| D-R35 | The database never deletes itself | 6. Safety and gates |
| D-R36 | Backup format | 6. Safety and gates |
| D-R123 | Scheduled backups never replace a good one | 6. Safety and gates |
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
| D-R127 | Pressed and focused pieces | 9. Screen rules |
| D-R128 | One-message screens | 9. Screen rules |
| D-R129 | Ending, archiving and deleting | 9. Screen rules |
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
| D-R126 | Colour and spacing targets | 10. Type and tokens |
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
| Q-R124 | The rest timer in the background | 12. Exercise screen and rest |
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
| D-R122 | When the data was set aside | 14. First launch |
| D-R93 | Build order for Start | 15. Start or log |
| D-R94 | What Start shows | 15. Start or log |
| D-R95 | Session names | 15. Start or log |
| D-R96 | The resume card | 15. Start or log |
| D-R97 | Start's date line | 15. Start or log |
| D-R98 | Exercises before routines | 16. Exercises and routines |
| D-R99 | Where exercises are made and managed | 16. Exercises and routines |
| D-R100 | The add sheet | 16. Exercises and routines |
| D-R101 | Changing an exercise's type | 16. Exercises and routines |
| D-R102 | Editing an exercise | 16. Exercises and routines |
| D-R103 | Exercise editor fields | 16. Exercises and routines |
| D-R104 | How the type is chosen in the editor | 16. Exercises and routines |
| D-R105 | How modules are picked in the editor | 16. Exercises and routines |
| D-R106 | How categories are picked in the editor | 16. Exercises and routines |
| D-R107 | How optional fields show | 16. Exercises and routines |
| D-R108 | How the default rest is picked | 16. Exercises and routines |
| D-R109 | How notes are written | 16. Exercises and routines |
| Q-R111 | How the add sheet adds | 16. Exercises and routines |
| Q-R112 | Pill and chip ends | 16. Exercises and routines |
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
| Tag | A module or one of the person's categories attached to an exercise; an exercise may carry several (R23, R114). |
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

### D-R125 · Purpose profile

**Rule:** Every screen is designed and judged against the profile below, which the design skills read through `design/direction.md`. Design questions are settled by the evaluations of the dedicated skills (`app-register-layout`, `typography-spacing-core`, `visual-craft` with `color-optimization-core`, `design-critique-plus` with `design-system-plus`), each recorded in the rule it settles; the owner picks only where they leave a tie or a matter of taste.

**Values:**

| Field | Profile |
|---|---|
| purpose | record what was done and comment on it neutrally, against the person's own past (R02, R51) |
| posture and hands | portrait, mostly one thumb; the phone held or set down between sets |
| context | home, gym or outdoors, in short glances between sets; walks outdoors in daylight |
| frequency | R01's table |
| stakes | a logged value lost or wrong; a mis-tap that ends, archives or deletes |
| data | body measures and weight are sensitive |
| tone | 0.25 on `app-register-layout`'s scale: no deadlines, no targets to fall short of, a gap is not a failure |
| comparison | the person's own history only; never other people |
| requirements that lead | legibility and reach mid-set, mis-tap safety, fit at font size 2.0, the route cost of logging, rows that never move |
| requirements that do not apply | urgency colouring, social comparison, splitting screens by module (modules are tags, R23) |

**Evidence:** Placed from R01, R02, R23 and R51 by two `app-register-layout` reviews, which put the tone at 0.25 and 0.3 with the person's own history as the comparison, near the skill's sleep-tracking anchor (2026-11-26, `research/skill-review-2026-11-26.md`).

**Acceptance:** none: a profile; each design rule's Evidence cites the skill evaluation that settled it.

**Since:** 2026-11-26 · owner · agreed rewording

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

**Rule:** Screens, layout, wording, navigation and palette are settled by the design skills' evaluations (R125), and the owner signs off the result before it is built; the code never decides them.

**Acceptance:** none: a working practice; each stage's screens are signed off from mockups before they are built (R38).

**Since:** 2026-11-26 · owner · agreed rewording

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

**Rule:** Android's own backup sends nothing to a cloud backup, and a device-to-device transfer to a new phone carries everything, on every Android version the app runs on. The app's own backup (R36) is the way out.

**Why:** Training data never leaves the phone without the person (R07).

**Values:**

| Android | Cloud backup | Device-to-device transfer | File |
|---|---|---|---|
| 12 and later | nothing | everything | `data_extraction_rules.xml` |
| 11 (the minimum, `minSdk` 30) | nothing | everything | `backup_rules.xml` |

**Evidence:** On Android 11 one rule set (`fullBackupContent`) covers both cloud backup and device transfer, so every `include` in `backup_rules.xml` carries `requireFlags="deviceToDeviceTransfer"` and applies to a transfer only. The app holds no Keystore-bound keys and no device identifiers, so transferred data stays readable (searched the code, 2026-11-21). Not checked: a transfer from a real Android 11 phone, or through a phone maker's own transfer tool.

**Acceptance:** `data_extraction_rules.xml` excludes every domain from `cloud-backup` and leaves `device-transfer` whole; `backup_rules.xml` includes every domain only with `requireFlags="deviceToDeviceTransfer"`.

**Not:** Android's encrypted cloud backup: the data would leave the device. No device transfer, on any version: a new phone would start empty until a restore.

**Since:** 2026-11-21 · owner · agreed rewording

### Q-R12 · App icon

**Rule:** What is the app's launcher icon?

**Default:** The rocc1 boulder alone in `rest.stone` on a `surface.page` background layer, inside the adaptive icon's safe zone, checked at 48 dp on dark and light wallpapers, with the same ground for the splash screen (`visual-craft`, skill review 2026-11-26). Changes: the launcher icon and the splash; lint's `MissingApplicationIcon` stays suppressed in the manifest until it is drawn in its own design round.

**Why:** The boulder alone reads at launcher size (about 48 dp), while the figure pushing it tells more of the story but shrinks to a few pixels, under `visual-craft`'s 4% stroke minimum.

**Since:** 2026-11-26 · owner · agreed rewording

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

**Rule:** Three evidence notes span modules: strength plus aerobic work together, lifting before cardio for strength, and two days without movement. The first two show only where the person is doing one of the modules they span, the third only where Daily movement is being done (R16 rule 4). The third is never triggered or worded by the person's own days without movement (R51, R115).

**Evidence:** Not checked: which of the three the pack already holds.

**Acceptance:** `python tools/evidence_check.py` passes with all three in the pack, and each note's chip appears only on screens of the modules it spans; two days with nothing logged change no note.

**Since:** 2026-11-26 · owner · agreed rewording

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

**Rule:** The person chooses which modules are on at first launch, with nothing assumed on, and changes the choice later in Settings. Switching a module off hides its Start card and its This week section, and stops what it runs on its own (Daily movement's reminders, sitting-break prompts and step counting, R22); History keeps every session, its exercises stay in the add sheet and under More, and switching it back on shows and starts everything again.

**Acceptance:** A fresh install shows the module choice before any tab, with every module unticked (`ModuleGateTest`, `ModuleChoiceScreenTest`); switching Strength off and on again leaves every Strength session and exercise as it was; with Daily movement off, no reminder or prompt fires and no sensor steps are recorded.

**Not:** Hiding a switched-off module's exercises from the add sheet too: they stay reachable.

**Since:** 2026-11-26 · owner · agreed rewording

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

**Evidence:** Schema 2's `exercise_module`, `label` and `session_label` tables and schema 3's `session.module` (R28, R121). Not checked: nothing writes a set-less session's module yet.

**Acceptance:** A session with sets has the modules of its exercises' tags; a set-less session has exactly the module it was logged under; no exercise carries a label.

**Since:** 2026-11-26 · owner · agreed rewording

### D-R24 · This week counts a mixed session by its parts

**Rule:** "This week" counts each set, whole, under every tag of its exercise, and a session once under each module it touched. Totals stay per module and are never combined; an untagged exercise counts nowhere.

**Evidence:** `:core` `weekByModule`; mutants on counting only the first tag and on counting a session once per set each turned a test red (2026-10-21).

**Acceptance:** `CoreTest`'s week tests pass: `aMixedSessionCountsByItsParts`, `anExerciseWithTwoTagsCountsWhollyUnderEach`, `aSessionCountsOnceInAModuleHoweverManyOfItsSetsAreThere`, `anUntaggedExerciseCountsNowhereAndAnEmptyWeekIsEmpty`.

**Since:** 2026-10-21 · owner · migrated

### D-R25 · Each module's own units in This week

**Rule:** "This week" shows each module's totals in its own units, as below, never converted or combined. A session with sets counts as R24 says; a session without sets (a walk, a quick amount) counts its time under the module it is logged under, chosen when it is logged.

**Why:** Steps and distance are units of measurement, but not in the way sets are (owner, 2026-10-21).

**Values:**

| Module | This week's totals |
|---|---|
| Daily movement | time; steps, each day the larger of its entered and its sensor count (R121) |
| Strength | sets, time |
| Stamina | time, distance |
| Mobility and balance | sets, holds, time |
| Body measures | none: a measure is not a weekly total; each shows in History on its day, as a plain value, and in the body-measure chart once it exists (stage 8) |

**Evidence:** Not checked: `weekByModule` counts logged sets only; it reads neither schema 3's `session.seconds` and `session.module` nor the `steps` table (R121).

**Acceptance:** A 30-minute walk logged under Daily movement adds 30 min to Daily movement's time and nothing to any other module; steps never appear as sets or minutes; This week has no Body measures section.

**Not:** Time only, with steps and distance left out of This week; sets only, as built before. The latest measure of the week under Body measures: a measure is not a total.

**Since:** 2026-11-26 · owner · agreed rewording

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

**Acceptance:** Every column for these quantities is INTEGER in Room's exported `3.json`.

**Since:** 2026-11-26 · owner · migrated

### D-R27 · Ids

**Rule:** Every table has `id TEXT PRIMARY KEY`, holding a ULID from `:core` `Ids`. A restored or imported row then never collides with a local one, and a later Windows copy can merge.

**Acceptance:** `CoreTest`'s ULID tests pass, and every table in `3.json` has a text primary key `id`.

**Since:** 2026-11-26 · builder · migrated

### D-R28 · Tables

**Rule:** The database is schema version 3, with the tables below. Schema 1 was designed for the whole scope, so that no package needs a migration of logged data; schemas 2 and 3 only add to it.

**Values:**

| Table | Columns beyond id | Serves |
|---|---|---|
| `exercise` | name, kind (DYNAMIC, ISOMETRIC, TIMED, DISTANCE), sided (0/1, counted per side), archived, show_partial_reps (0/1, default 0; schema 2), rest_seconds? and note? (schema 3) | the floor; `CM2`; R32, R103 |
| `routine` | name, weekdays (bit 0 = Monday … bit 6 = Sunday; 0 = unscheduled), position | the floor; `CM3`, `KA6` |
| `routine_item` | routine_id, exercise_id, position, target_sets, target_reps, target_weight_g, target_seconds, target_distance_m, rest_seconds | the floor; `LO4`-style rest, `GM6` |
| `session` | routine_id?, activity (free type, "Walk"), started_at?, ended_at?, zone, day, state (OPEN, FINISHED, STOPPED_EARLY), intensity? (1-5), note?, seconds? and module? (schema 3) | the floor; `FT2`, `GR4`, `FA13`, `KA2`, `GL1` (OPEN = in progress); R29, R121 |
| `logged_set` | session_id, exercise_id, position, side? (L, R), reps?, weight_g?, seconds?, distance_m?, rpe? (×10), rir?, done_at, power (0/1, default 0; schema 2), partial_reps (default 0; schema 2), kind (schema 3) | the floor; `LL2`, `GM2`, `BC1`; R22, R32, R121 |
| `progression_rule` | routine_item_id, add_weight_g, add_reps, when (ALL_SETS_HIT) | `LL5`, `GM6` |
| `interval_preset` | name, prepare_s, work_s, rest_s, rounds, sets, set_rest_s, last_used_at? | `TX1`, `FA1`, `SH1` |
| `track_point` | session_id, t, lat_e7, lon_e7, alt_dm?, accuracy_m?, segment | `OT1`, `OT5` |
| `goal` | exercise_id? (null = all training), period (DAY, WEEK, MONTH), target, unit (SESSIONS, MINUTES, METRES, SETS) | `FA9` |
| `setting` | key, value | `OS8` (backup folder, schedule), the modules chosen, the evidence-note level, later preferences |
| `exercise_module` (schema 2) | exercise_id, module (DAILY_MOVEMENT, STRENGTH, STAMINA, MOBILITY_BALANCE, BODY_MEASURES); unique per pair | R23 |
| `label` (schema 2) | name, position, archived | R23 |
| `session_label` (schema 2) | session_id, label_id; unique per pair | R23 |
| `body_measure` (schema 2) | kind (WEIGHT, WAIST and others), value, unit (G, MM), taken_at, zone, day, note? | `OS1`, R20 |
| `steps` (schema 3) | day, steps, source (ENTERED, SENSOR); unique per day and source | R25, R121 |
| `category` (schema 3) | name | R103 |
| `exercise_category` (schema 3) | exercise_id, category_id; unique per pair | R103 |

**Evidence:** Schema 2 built 2026-10-21 with `2.json` exported; ten planted mutants each turned the intended test red. Schema 3 built 2026-11-26 with `3.json` exported.

**Acceptance:** Room's exported `3.json` holds exactly these tables and columns.

**Since:** 2026-11-26 · owner · migrated

### D-R121 · Schema 3 additions

**Rule:** Schema 3 adds, by R33, the columns and tables below, so a session without sets keeps its time and its module, a set keeps the type it was logged under, steps are kept per day, and an exercise holds R103's rest, note and categories. A set's `kind` is its exercise's type when it was logged: the migration and every restore fill a missing one from the set's exercise (all the sets of a version-1 or version-2 backup), and a later change of type leaves it. Steps, entered or counted by the sensor, are never stored as sessions, and the backup format moves to `version` 3 (R36).

**Values:**

| Where | Added | Holds |
|---|---|---|
| `session` | `seconds`, nullable | an amount's time (R29) |
| `session` | `module`, nullable | the module a session without sets is logged under (R23, R25) |
| `logged_set` | `kind`, default empty only for the migration's ALTER TABLE | what the comparison matches on (R67) |
| `exercise` | `rest_seconds`, nullable | the exercise's rest: null for Default (Settings' length, R72), 0 for None, else seconds (R71, R103) |
| `exercise` | `note`, nullable | the exercise's notes (R103) |
| `steps`, a new table | id, day, steps, source (ENTERED, SENSOR) | one row per day and source (R22); each entry adds to the day's ENTERED row |
| `category`, a new table | id, name | the person's own categories (R103) |
| `exercise_category`, a new table | id, exercise_id, category_id | a category on an exercise, several per exercise |

**Evidence:** `MigrationTest` and `AndroidMigrationTest` migrate a schema-1 and a schema-2 file keeping every cell, each set taking its exercise's type; on the desktop the migrated file also has the same shape as a fresh one, column order included, where the phone's path relies on Room's own check; `OldBackupTest` restores the pinned version-1 file and `backup-v2.json`, exported by the schema-2 app (acba13a); `SessionsTest.aSetKeepsTheTypeItWasLoggedUnder`. A mutant leaving the migration's fill out turned the migration test red (2026-11-26). Not checked: nothing writes steps, `session.module`, `session.seconds` or the exercise fields yet; their stores come with the screens that use them.

**Acceptance:** Room's exported `3.json` holds these columns and tables, `MigrationTest` and `AndroidMigrationTest` pass from schema 1 and schema 2, and `OldBackupTest` restores both pinned backups with every set under its exercise's type.

**Not:** Steps on the session too: a sensor's count is not a session. R103's fields in a schema 4 with the exercises plan: one migration instead of two. Restored older sets at the empty default: the comparison would match none of them.

**Since:** 2026-11-26 · owner · agreed rewording

### D-R29 · An amount is a session without times

**Rule:** An amount (`FA10`'s shape) is a session with `started_at` and `ended_at` null, `day` set and its time in `seconds` (R121). A day's total time is the union of its timed sessions plus the sum of its amounts (`:core` `dayTotalSeconds`).

**Acceptance:** `CoreTest`'s `overlappingSpansCountOnceAndAmountsAdd`, `aSpanInsideAnotherAddsNothing` and `emptyDayIsZero` pass.

**Since:** 2026-11-26 · owner · agreed rewording

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

**Evidence:** A test builds a schema-1 and a schema-2 file from Room's exported `1.json` and `2.json`, fills every table, migrates through the app's own open and compares every cell (`quote()`) before and after, on the desktop and under Robolectric on the phone's path (`KeepFileOnCorruptionDriver`); the desktop test also compares the migrated shape against a fresh one. From either file, a throwing statement, a migration Room's check refuses (told by its message) and a file a newer app wrote each leave the file as it was (2026-11-26). A destructive fallback is caught by the Robolectric test only (2026-10-21).

**Acceptance:** `MigrationTest` passes on the desktop and `AndroidMigrationTest` under Robolectric.

**Since:** 2026-11-26 · owner · migrated

## 6. Safety and gates

### O-R34 · Nothing logged is lost

**Rule:** Backup (one file, and scheduled to a folder) and resuming a session after the app is killed are part of the first stage.

**Why:** Nothing logged may be lost.

**Acceptance:** Stage 1a is not done until the backup package (`KA9`, `OS8`) and resume after process death (`GL1`) pass their tests.

**Since:** 2026-11-21 · owner · agreed rewording

### D-R35 · The database never deletes itself

**Rule:** Android opens the database through `KeepFileOnCorruptionDriver`, because the stock driver's default handler deletes a corrupt file. `openOrRecover` sets an unopenable file aside as `<name>.unopenable-<stamp>` and opens a fresh one, and the person is told before anything else shows (R122).

**Evidence:** A Robolectric test writes garbage and finds the bytes set aside, with the stock driver as the failing control. `newestSetAside` finds the file set aside, which R122's screen is keyed to (`CorruptionTest.theNewestSetAsideFileIsFound`, 2026-11-26).

**Acceptance:** That Robolectric test passes, and a set-aside file leads to R122's screen.

**Since:** 2026-11-26 · owner · agreed rewording

### D-R36 · Backup format

**Rule:** A backup is one JSON file holding every table, `format` `sisyphusfit-backup`, `version` 3. Restore replaces everything in one transaction, and a version-1 or version-2 backup still restores, its rows unchanged, the new columns at their defaults, each set under its exercise's type (R121) and the new tables empty.

**Evidence:** Pinned by a version-1 file the schema-1 app exported and a version-2 file the schema-2 app exported (2026-11-26); mutants on restore outside a transaction and on a table missing from the backup list each turned a test red.

**Acceptance:** Export, restore into an empty database and export again give byte-identical files; the pinned version-1 and version-2 files restore.

**Since:** 2026-11-26 · owner · migrated

### D-R123 · Scheduled backups never replace a good one

**Rule:** Each scheduled backup is a new file in the chosen folder, named by its date and time; none replaces an earlier one. Removing old files never removes the last backup made before the data shrank (a backup holding fewer rows in all than the one before it), and Settings shows when the data shrank.

**Evidence:** Not checked: the backup package is not built; how many dated files are kept is left to its plan.

**Acceptance:** Two scheduled runs leave two files; after a backup of 40 sessions and a fresh start, the next run still writes its file, the clean-up keeps the 40-session backup, and Settings shows that the data shrank.

**Not:** One file overwritten each run: a damaged or empty database would replace the last good backup. Skipping a backup when the data shrank: after a deliberate delete or a restore of an older file, backups would stop.

**Since:** 2026-11-26 · owner · agreed rewording

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
| 1a | schema 3 (R28); the app shell; the module choice (R21); the app opens on Start (R83) | `X-MODULES` |
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
| 6 | rest-timer package (R71, R74), settling R124 | `GL2`, `LL4`, `KA5` |
| 7 | progression package: the "next time" proposal of R43 | `LL5`, `GM6` |
| 8 | early stop reviewed, unfinished sets skipped; quick intervals from a cycles stepper; body-measure chart | `KA2`, `SH1`, `OS2`, `OS4` |
| 9 | self-checks as own trend; body-measure changes | `PL1`, `OS5` |
| 10 | GPS package | `OT1`, `FA4`, `OT5`, `FT5` |
| 11 | Today, designed last (R40) | `KA6` |

**Acceptance:** none: an order of work; each stage is done by R38.

**Since:** 2026-11-26 · owner · agreed rewording

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

**Rule:** After a session, the app may offer one editable "next time" proposal, computed only from a progression rule the person sets on a routine's item (`progression_rule`), so only an item of a routine gets one; the app never picks the numbers. The proposal is an option in Settings, off by default.

**Why:** It applies the person's own rule, so it stays a record and not a prescription (R02, R117).

**Acceptance:** With the setting off, no proposal appears; with it on and every set hit, the proposal equals last time's numbers plus the rule's step, and editing it changes nothing logged.

**Not:** Recording and commenting only: progression would stay manual. A per-set proposal: R117 rules out per-set prescriptions.

**Since:** 2026-11-26 · owner · agreed rewording

## 8. Screens by purpose

### D-R44 · Screens grouped by purpose

**Rule:** Features are gathered into screens by shared purpose, with few overlaps and only where necessary. Modules are switches and content, not a screen tree. Every harvest row is placed on exactly one screen, declared shared with a reason, or left out with a reason.

**Values:**

| Grouping | Score |
|---|---|
| by purpose | 0.836 |
| hybrid | 0.841 |
| by module | 0.61 |

**Evidence:** Method ported from Factotum (`research/purpose-2026-10-20/`: `purpose-map-method.md`, the checker `purpose_map.py --map <json>` with a selftest, `score_maps.py`), on the owner's axes with equal weights: few overlaps, calm load per screen, taps to common tasks, purpose coherence. Factotum's Today and purpose map are a skeleton to adapt, not a blueprint. The calm-load limit of 8 comes from Factotum's Today, and the taps axis weighs the nine tasks equally; weighted by R01's frequencies the mean taps move from 2.33 to 2.21 by purpose and from 2.11 to 1.98 for the hybrid [estimate], so the order holds (`app-register-layout` review, 2026-11-26).

**Acceptance:** `purpose_map.py --map` passes on the working map: all 162 harvest rows placed, shared or left out.

**Not:** Screens by module: it duplicates the session, history and exercise screens per module.

**Since:** 2026-11-26 · owner · migrated

### D-R45 · Purposes and screens

**Rule:** The purposes and screens below are the working list for mockups; a screen can still move when a mockup shows a problem.

**Values:**

| Purpose | Its job | Screens |
|---|---|---|
| What's next | see what today holds and start or log it | Today (R42), Start or log (the Start tab, R83) |
| Doing it | move, and have the app keep count | Session, Exercise in a session, Set details (sheet), Number entry (the phone's keyboard, R68), Interval timer, Recording, Stopping and finishing, Log afterwards |
| Looking back | see what was done | This week (a section per module but Body measures, R25), History, A recorded activity, Progress |
| Making it yours | choose modules and goals; shape routines, programmes, timers, exercises | Modules and goals, Your plans, Programme library, Timers and routes, Exercises |
| Knowing why | the evidence behind a feature, how a movement is done | Evidence notes |
| Looking after the app | settings, backup, import and export, privacy | Settings, Back up and restore, Import, export, delete |

**Evidence:** Full reasons for the rows left out are in `research/purpose-2026-10-20/compare.md`.

**Acceptance:** none: a working list; each screen's own rules carry the checks.

**Since:** 2026-11-26 · owner · agreed rewording

### D-R46 · Doing and showing screens

**Rule:** A showing screen (This week, History, Settings, More) has no main action. A doing screen keeps one, in the form of R69.

**Acceptance:** The tab roots of This week, History and More draw no main bar.

**Since:** 2026-11-26 · owner · agreed rewording

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

**Rule:** Visual size follows the function hierarchy, so not every control is visually 48 dp. Touch areas step up by importance on the ladder below, from its minimum to its maximum, each control on the step its row names; touch targets are at least 5 dp apart.

**Why:** 42 dp avoids overlap, or too much empty space, when two lines or buttons are close.

**Values:**

| Step | Balanced / compact | Used for |
|---|---|---|
| minimum | 42 dp | never below (gate G4) |
| 1 | 48 / 44 dp | chips (the module card's "info" chip included), segmented choices, checkboxes, switches, the header title's tap (R56), the smaller steppers (partials), icon buttons |
| 2 | 56 / 48 dp | rows, the main bar, the set steppers |
| 3 | 64-72 / 56-72 dp | module cards, the tab bar, the floating control |
| maximum | 72 dp at font size 1.0 | the rest round, its touch target growing with the round (about 121 dp at 2.0, R80) |

**Evidence:** 42 dp is above WCAG 2.2 AA (24 px) and below Android's 48 dp guideline and WCAG AAA (44 px); Android's Accessibility Scanner flags it. The ladder meets `app-register-layout`'s hit-area requirement (a minimum, steps by importance, a maximum, no overlap), checked 2026-11-21; its sizes are the `size` tokens of `design/tokens.json`, not measured on rendered screens. Segmented choices take `size.hit` in place of the mockups' 44 dp, and the "info" chip's target left its card's tick area (`PiecesTest.segmentsTakeTheTouchTarget`, `PiecesTest.noTouchTargetInsideAnother`, `ModuleChoiceScreenTest.theInfoChipLiesOutsideTheTickArea`, 2026-11-26).

**Acceptance:** Gates G3 and G4 pass; the unit tests of the pieces check touch targets of at least 42 dp; every touch target's `size` token sits on one row of the ladder.

**Not:** 48 dp for every touch area: it leaves too much empty space between close lines. Three steps with the rest round as an exception: the owner kept the minimum and maximum as rows of their own.

**Since:** 2026-11-26 · owner · agreed rewording

### D-R51 · Judgement-free

**Rule:** Wording and charts never judge: no streaks, "Nothing here." when a list is empty, followed by the one neutral action that fills it where there is one ("Log afterwards"), and charts and tracking are part of the app, never presented as success against failure. "No judgement" is how the app is built, never something it says, and mechanics a module has anyway (routines) are not named.

**Evidence:** The action after "Nothing here." follows `app-register-layout`'s requirement that an empty state names the next step (skill review, 2026-11-26).

**Acceptance:** No screen text praises, blames or counts missed days; the empty state reads "Nothing here.", with its action where the list has one.

**Since:** 2026-11-26 · owner · agreed rewording

### D-R52 · Gates G1-G5

**Rule:** Every mockup and screen passes the gates below before it is shown, then is scored on the owner's axes with equal weights; a plateau is shown as a plateau.

**Values:**

| Gate | Checks |
|---|---|
| G1 | no overlaps |
| G2 | nothing clipped or wider than the screen |
| G3 | touch targets at least 5 dp apart, none inside another |
| G4 | touch areas at least 42 dp |
| G5 | no lost text with long real values at font sizes 1.0, 1.3, 1.5 and 2.0 on the curve of R56, which every Android version the app runs on uses: nothing cut, clipped, covered by the rest round, the figure, the main bar or the keyboard (R68), or crossing other text; a title's "…" with its full text on a tap is allowed, and text below the fold of a scrolling body counts as kept |
| axes | harmony, density (counted on the first screen against two outside apps doing the same job), visual and functional hierarchy, reach and legibility, taps per set, at-a-glance clarity, consistency |

**Acceptance:** The gate script reports G1-G5 passing on every page of the screen at both densities, 360-412 dp and font sizes 1.0-2.0.

**Not:** Gating Android 11-13 at evenly scaled sizes: Compose scales text on R56's curve on those versions too (2026-11-26).

**Since:** 2026-11-26 · owner · agreed rewording

### D-R127 · Pressed and focused pieces

**Rule:** Every piece you press shows a pressed look drawn in its own shape, and a 2 dp focus ring in `ink.primary` that follows that shape 2 dp outside it, on the ground around it, when keyboard or switch access reaches it, so it shows round the main bar too; Foundation's rectangular default never shows. No control is drawn disabled: one that cannot act is not shown, or its screen says why, and the main bar always acts (R90).

**Evidence:** Required by `design-system-plus` (component states, focus first) and `visual-craft` (pressed and focus-visible on every button); a visible focus is a floor of `app-register-layout`. `StoneIndication` (`Press.kt`) draws every piece's press in its own shape (`state.pressed` over light pieces, `state.pressed-dark` under the main bar's label) and its ring; goldens `pieces-pressed` and `pieces-focused`, and every target takes the focus (`PiecesTest.noTouchTargetInsideAnother`, 2026-11-26). The two scrims behind a sheet or a module's card keep no indication: they have no shape of their own, and their press closes what is open. Not checked: Tab order on the phone with a keyboard.

**Acceptance:** Each piece's golden includes its pressed and its focused state; with a keyboard, Tab reaches every control on a screen and its ring shows.

**Not:** A disabled look: it reads as broken, and the app has no step that waits on a condition.

**Since:** 2026-11-26 · owner · agreed rewording

### D-R128 · One-message screens

**Rule:** A screen that tells one thing and offers one action (R92, R122) sets its title and line just above the bottom zone, next to that action. The space above stays empty, as the screen has nothing else to show.

**Evidence:** `ck analyse` finds 68% of the can't-open screen and 62% of the set-aside screen empty at font size 1.0, the message 570 dp above its bar; `app-register-layout` and `design-critique-plus` take an empty pane only with a stated reason (2026-11-26). `MessageScreen` sets both screens' words at the foot of their scrolling area (`SetAsideTest.theMessageSitsJustAboveTheBar`; goldens `set-aside-balanced-{100,200}`, `cant-open-balanced-{100,200}`, 2026-11-26).

**Acceptance:** On the goldens of R92 and R122 at 1.0 and 2.0, the line's last row ends within `space.section` of the bottom zone.

**Not:** The message at the top with the action at the bottom: the eye and the thumb travel the whole screen between them.

**Since:** 2026-11-26 · owner · agreed rewording

### D-R129 · Ending, archiving and deleting

**Rule:** A control that ends, archives or deletes sits last among its neighbours, apart from the main action, as the rows below say. Where the act can be undone, an Undo shows for 10 s, or longer when Android's accessibility setting "Time to take action" asks for it, as after a hold (R70); a delete that cannot be undone asks first, naming what goes. More and Settings order their rows by R01's frequency, a setting before what depends on it, and deleting last.

**Values:**

| Control | Where | After it |
|---|---|---|
| "End/Save" (R96) | the resume card's pill, never the main bar | "Session ended · Undo", which reopens it |
| archive (R102) | a quiet pill at the foot of Edit | "Archived · Undo"; later, "Bring it back" in More's "Archived" group (R99) |
| delete (R45) | last on the import, export and delete screen | a sheet naming what is deleted, with "Delete" and "Keep" |

**Evidence:** `app-register-layout`'s mis-tap safety and order requirements (a destructive row last, spaced or confirmed, so a tap aimed at a neighbour lands on it under 5% of the time), skill review 2026-11-26. Not checked: none of these controls is built.

**Acceptance:** Each control's screen test finds it last in its group and at least 16 dp from the main action; Undo within 10 s restores the session or the exercise as it was, and with a longer "Time to take action" the Undo stays that long; deleting asks first.

**Since:** 2026-11-26 · owner · agreed rewording

### D-R53 · Greyscale

**Rule:** The app is greyscale and light only, whatever the phone's dark theme, until the palette is discussed with the owner. It has no status colours yet; when one comes, it carries a second cue besides colour.

**Evidence:** The app draws no night variant (2026-11-26). `visual-craft` asks for the light, dark or both decision and for status roles with a second cue (skill review, 2026-11-26).

**Acceptance:** Every colour role in `design/tokens.json` resolves to a grey or a black alpha, and with the phone in dark theme the app draws as in light.

**Not:** A dark variant before the palette is discussed. Its cost, accepted (2026-11-26): on a dark-theme phone at night or in a dim gym, the light page glares between sets.

**Since:** 2026-11-26 · owner · agreed rewording

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

**Rule:** The type tokens and their line heights are in sp, so text follows the phone's font size up to 200% on Android 14's nonlinear curve, which Compose applies itself on every Android version the app runs on, the 6T's Android 11 included. Padding, gaps, icons and touch targets stay in dp, the rest round's growing with it (R80), and no text has a fixed height. When text does not fit, it gives way as the rows below say.

**Values:**

| Element | When text does not fit |
|---|---|
| set value | shrinks to fit between its steppers, never below its role's compact size (set number 35 for weight and reps, heading 20 for partials); then its unit moves under it, centred |
| header title | may end in "…", its full text on a tap |
| session clock and Finish | never shorten |
| main bar | may wrap to two lines |
| screen body | scrolls under the fixed main bar; the rest round's band moves down with the card at that font size, so R70's fixed height holds within one font size |
| card rows, chip, main bar | take their sizes as minimums |
| Android 14 curve at 2.0 | 11 → 22, 14 → 26, 16 → 28, 19 → 32, 23 → 35.5, 28 → 37.3, 40 → 46.9 dp (AOSP `FontScaleConverterFactory`) |
| the curve at 1.3 | 16 → 20.2 dp, 40 → 40 dp, the same on Android 11 and 16 |

**Evidence:** Gated at 1.0, 1.3, 1.5 and 2.0 with long real values ("102.5 × 12", 142.25 kg, a 10:00 rest, "Single-leg Romanian deadlift", "Wednesday afternoon", 1:12:40) by G1-G5. Under Robolectric, SDK 30 and SDK 36 give the same sizes at 1.15 and 1.3 (16 sp → 18.1 and 20.2 dp), where an even scale gives 18.4 and 20.8 (2026-11-26). Sources: Android 14's notes on nonlinear font scaling; WCAG 2.2 SC 1.4.4 (resize to 200% without loss; truncation acceptable when the full content is available on activation) and WCAG2ICT. Not checked: the 6T's own largest text size.

**Acceptance:** The unit tests check Android 14's curve at 2.0, the same curve on Android 11 at 1.3 (`android11UsesTheSameNonlinearCurve`, `lineBoxFollowsTheTextOnAndroid11`), and each piece's font-size behaviour; the goldens at 1.0, 1.3 and 2.0 pass.

**Since:** 2026-11-26 · owner · agreed rewording

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

**Rule:** Every text element takes one role below, balanced / compact; in the comparison card, "Set N" (label) and the column heads (eyebrow) are 14 sp and the values body at 16 sp at both densities, and the evidence-note chip is 11 sp on 16 sp lines in the secondary grey. At both densities the set number is at least 2.5 times body and body's lines at least 1.5 times its size. At font size 2.0 each heading outranks the content under it on the screen where they meet, by weight or carve where size alone does not.

**Values:**

| Role | Used for (stage 1a mockups) | Size sp | Line height sp | Weight | Carve | Features |
|---|---|---|---|---|---|---|
| label | secondary text, values beside a row | 13 / 12 | 20 / 16 | 400 | 0 | |
| eyebrow | section heads, dates, column heads, a note's fact names: capitals tracked 0.08 em | 13 / 12 | 16 / 16 | 500 | 0 | |
| body | running text and rows | 16 / 14 | 24 / 21 | 400 | 0 | |
| row title | names in rows, weekly amounts, the running timer | 19 / 17 | 24 / 24 | 500 | 100 | `tnum` on numbers |
| heading | screen titles, large secondary values, the set's partials | 23 / 20 | 28 / 24 | 500 | 100 | |
| exercise | the exercise's name on its own screen | 28 / 24 | 32 / 28 | 500 | 100 | |
| set number | the set's weight and reps | 40 / 35 | 44 / 40 | 500 | 100 | `tnum` |

**Evidence:** From the polish round's type scale; `python tools/tokens.py check` compares this table with `design/tokens.json`. `typography-spacing-core` finds the sizes a minor third (1.2) apart, its pick for dense interfaces, and the headings' lines within its band; the compact set number moved from 34 to 35 sp and compact body lines from 20 to 21 sp to meet both ratios, which `tokens.py check` computes (2026-11-26). Not checked: on R56's curve at 2.0, heading and exercise differ by 1.05 in size, untested on the exercise screen, which is not built.

**Acceptance:** `python tools/tokens.py check` passes and checks both ratios; at font size 2.0 the exercise screen's golden shows each heading above its content in weight or carve.

**Not:** A 48 / 40 dp keypad readout: the app's own keypad is gone (R68), so it is not a token. A 50-70 character measure: the 6T's 352 dp column holds about 44 body characters a line, 25 at font size 2.0.

**Since:** 2026-11-26 · owner · migrated

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
| surface.card | grey.100 | `#ffffff` | comparison card, facts, light pebbles (steppers, the rest round's control), the tab bar |
| surface.worn | grey.96 | `#f5f5f5` | module cards, week sections, the resume card and rows: cut left, worn right (R65) |
| surface.worn-light | grey.100 | `#ffffff` | the soft light toward their worn right side |
| surface.sunk | grey.88 | `#dfdfdf` | the bottom zone behind the main bar; secondary pills |
| fill.dark | grey.17 | `#2b2b2b` | every dark fill of the interface: main bar, tab bar line and underline, selected states (R65) |
| fill.dark-light | grey.38 | `#5a5a5a` | the faint light inside the main bar's dip |
| fill.on-dark | grey.100 | `#ffffff` | text and icons on fill.dark |
| ink.primary | grey.10 | `#1c1c1c` | text, icons, the current set's pebble outline |
| ink.secondary | grey.31 | `#4a4a4a` | secondary text, the evidence-note chip, tags |
| line.edge | grey.45 | `#6b6b6b` | outlines (flat rows), the 1.5 dp underline of a number you tap to type, the sheet's grab handle, switches |
| line.carved-dark | grey.45 | `#6b6b6b` | a carved line's cut: column heads, the chip's lines (3 dp, dark over light) |
| line.carved-light | grey.100 | `#ffffff` | a carved line's lit edge; the 0.7 dp light under object icons |
| line.spindle | grey.45 | `#6b6b6b` | History's carved date divider (owner 2026-10-28: the edge grey, not a grey of its own) |
| shadow.ground | grey.76 | `#bdbdbd` | the 2 dp ground under a pill |
| shadow.step | black.20 | `#00000033` | the 1 dp shadow under light pebbles and the worn cards you press: their edge on the page (R126) |
| shadow.float-near | black.14 | `#00000024` | a sheet's near shadow |
| shadow.float-far | black.20 | `#00000033` | a sheet's far shadow |
| shadow.scrim | black.42 | `#0000006b` | behind a sheet |
| state.pressed | black.06 | `#0000000f` | drawn over a light piece while it is pressed, in its own shape (R127) |
| state.pressed-dark | grey.24 | `#3b3b3b` | a dark piece's fill while it is pressed, under its label (R127) |
| state.focus | ink.primary | `#1c1c1c` | the focus ring, outside the piece's shape (R127) |
| rest.stone | grey.10 | `#1c1c1c` | the boulder and the figure (R75, R76) |
| rest.near-leg | grey.31 | `#4a4a4a` | the figure's near leg (R75) |
| rest.ring-track | rest.near-leg | `#4a4a4a` | the white ring's track before it fills: the near leg's grey (R77) |
| rest.ring | grey.100 | `#ffffff` | the ring and the time are the only white in the round (R74) |

Space and size, dp, balanced / compact:

| Token | dp | Used for |
|---|---|---|
| space.gutter | 16 / 12 | screen side margin |
| space.pad | 16 / 12 | inside a card or row, at its sides |
| space.pad-y | 12 / 8 | inside a worn card, above and below its content (R126) |
| space.gap | 8 / 8 | between items in a list; compact keeps 8 as drawn and judged (the base compact scale had 4) |
| space.section | 24 / 16 | between sections |
| space.eyebrow | 8 / 8 | under a section head: at least half its line (R126) |
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
| stroke.focus | 2 dp | the focus ring (R127) |
| stroke.focus-gap | 2 dp | between a piece's shape and its focus ring (R127) |
| elevation.ground | 0 2 0 0 #bdbdbd | a pill standing on the ground |
| elevation.step | 0 1 0 0 #00000033 | a light pebble |
| elevation.edge-inset | inset 0 0 0 1 #6b6b6b | a flat row's outline |
| elevation.float | 0 6 16 0 #00000033, 0 1 4 0 #00000024 | a sheet |
| motion.valley | duration 250 ms, easing cubic-bezier(0.2, 0, 0, 1) | the tab valley sliding to the new tab (R66) |
| motion.step | duration 1000 ms | one push step of the figure (R74) |
| motion.stone-turn | duration 23000 ms | about one turn of the stone (R74) |
| motion.reduced | nothing turns or slides; the figure holds still; the ring and the time move once a second; the valley jumps (R74, R66) |  |
<!-- tokens:end -->

**Evidence:** `tools/tokens.py check` (CI runs it) checks every reference resolves, every raw value is used by a role, the density pairs, this spec's tables and `Tokens.kt`. Taken from the decided screens as drawn: rebuilt from the file, all 40 (20 per density) matched them pixel for pixel. The owner then merged #6a6a6a into #6b6b6b, drew History's date spindle in the edge grey and dropped the note's facts hairline (2026-10-28). The palette round of 2026-11-26 moved the values to R126's targets, which the same check computes.

**Acceptance:** `python tools/tokens.py check` passes.

**Since:** 2026-11-26 · owner · migrated

### D-R126 · Colour and spacing targets

**Rule:** The values in `design/tokens.json` meet the targets below at both densities, and `tools/tokens.py check` fails on a miss. A value that misses one changes through a palette or spacing round with the skill named in its row, not by hand.

**Values:**

| Target | Requirement | Skill |
|---|---|---|
| text contrast | every text, secondary text included, at least 7:1 on every surface it sits on; icons and outlines that carry meaning 3:1 | `visual-craft` (outdoor glances, R125) |
| control separation | a fill you press stands at least 10 ΔE2000 from the ground it sits on, or has an edge that does | `visual-craft`, `design-system-plus` |
| close greys | two greys under 2 ΔE2000 apart serve one role and merge, or move apart | `design-system-plus` |
| picked state | a picked chip, segment or box differs from its unpicked form by at least 3:1, by outline and weight, never by the main bar's fill; the checkbox's tick keeps `fill.dark` | `design-critique-plus` |
| spacing unit | balanced spaces are multiples of 8 dp (4 allowed), compact ones of 4 dp; `space.note-head` is the one exception (R63) | `typography-spacing-core` |
| gap under a head | at least half the head's line height | `typography-spacing-core` |
| padding | a card's or row's vertical padding is a space token with a compact value, never a number in code | `design-system-plus` |

**Evidence:** From the skill review of 2026-11-26 (`research/skill-review-2026-11-26.md`). The palette round with `visual-craft` (2026-11-26) kept each grey's hue and moved only lightness, keeping the page `#ebebeb`, and searched the light greys for the least change that meets every row: secondary text `#575757` became `#4a4a4a` (7.43:1 on the page, 8.13 on worn cards), worn cards `#f0f0f0` became `#f5f5f5`, the light pebbles merged into white, and the stone merged into the text's grey. The fills stay light, so light pebbles and the worn cards you press stand on a 1 dp shadow of 20% black (11.1 ΔE2000 on the page) and pills on a `#bdbdbd` ground (10.8); a picked segment's 2 dp `ink.primary` outline stands 3.20:1 from the unpicked hairline. `design/tokens.json`'s `targets` block lists each pair `tokens.py check` computes. Not checked: how the worn side's light reads at 2.0 ΔE2000 over the card (3 before) on the 6T.

**Acceptance:** `python tools/tokens.py check` computes each row and passes.

**Not:** The 4.5:1 floor alone for text: the app is read in glances, outdoors too (R125).

**Since:** 2026-11-26 · owner · agreed rewording

### D-R63 · Evidence note sheet spacing

**Rule:** The evidence note's sheet has `space.note-head` under its eyebrow and the claim in `type.note-claim`, as below.

**Why:** The owner read the note as too dense on the 6T; its 12 dp head and its claim's lines (1.47 and 1.41) are deliberate exceptions to R126's spacing unit and R60's 1.5 line ratio, which is for body.

**Values:**

| Density | Under the eyebrow | Claim |
|---|---|---|
| balanced | 12 dp | the row title's 19 sp on 28 sp lines |
| compact | 12 dp | 17 sp on the compact row title's 24 sp lines (1.41, against 1.47) |

**Evidence:** Chosen by the owner on the 6T walk of the foundation gallery (2026-10-28).

**Acceptance:** The note sheet's golden matches these values.

**Not:** As drawn, 8 dp and 24 sp lines: too dense; most, 16 dp and 30 sp lines.

**Since:** 2026-11-26 · owner · migrated

## 11. Visual language

### D-R64 · Icons

**Rule:** Icons are the app's own set of 28, drawn on a 24 grid with a 2 dp stroke and no fill. Object icons are faceted, while the path icons (navigation, chevrons, play, stop, timer, tabs) and − and + keep round caps. Holds and partial reps are shown as text, not markers.

**Why:** Object icons are things, so they take the stone's facets (R65); path icons and − and + are signs, not things, so they keep the round caps that `visual-craft` asks of one icon set.

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

**Evidence:** Chosen by the owner after scoring variants on legibility at 1x and distinctness (`research/screens-2026-10-20/polish/icons/review-2026-10-21`). Not checked: legibility at 16 dp and 48 dp, which `visual-craft` asks for.

**Acceptance:** `IconsTest` and `StoneIconTest` pass, and every icon stays distinct at 16 dp and 48 dp.

**Since:** 2026-11-26 · owner · migrated

### D-R65 · Stone language

**Rule:** The app's elements take the stone's idea from Sisifo's carving: surfaces cut on one side and worn on the other, worn pebbles for things you press, and lines carved into the surface. Every dark fill of the interface is one grey, #2b2b2b, and each element takes the form below, the same in dp at both densities.

**Values:**

| Element | Form |
|---|---|
| main bar | square corners; its top edge sinks 8 dp across the middle third in one soft dip, lit faintly inside the dip |
| secondary buttons | pills as worn pebbles (each corner a slightly different radius) on a 2 dp ground shadow |
| steppers (− and +) | borderless light pebbles (#ffffff), − and + with different outlines, on a 1 dp shadow that is their edge on the page (R126); the number you tap to type is underlined 1.5 dp in #6b6b6b |
| module cards, week sections, the resume card, rows | cut on the left (square), worn on the right (8 dp corners), #f5f5f5 lit softly toward the right; those you press stand on the pebbles' 1 dp shadow |
| comparison card | 4 dp corners; its column heads sit on a carved line (3 dp); the current set is circled by a 1.5 dp pebble outline |
| History's date dividers | a carved 5 dp spindle that fades out over its first and last fifth |
| object icons | faceted (each curve drawn as two straight cuts, square ends and mitred corners) with a 0.7 dp light below them |
| tags | plain secondary text joined by a middle dot, no box: they sit lowest in the hierarchy |
| evidence-note chip | no outline; a carved 3 dp line above and below it |
| text and greys | text, the boulder and the figure #1c1c1c; secondary text, the figure's near leg and the ring's track #4a4a4a |

**Evidence:** Scored in three stages from the owner's per-element picks (69 designs, G1-G4 passing on all 20 screens): 0.785 on the eleven axes of `results_vl/axes2.json`, next the same with relief rows 0.783 and with the hill tab bar 0.773; the valley as a sunken surface 0.74; the design before and stock Material 0.592 (last, tied). Judged blind on 3× renders, then checked on the 6T at true size. At compact, G1-G5 pass on the exercise screens at 360-412 dp and font sizes 1.0-2.0 (2026-10-28).

**Acceptance:** The goldens of the gallery pass, and the pieces' sizes match the decided mockups within 1 dp.

**Not:** A vein on the comparison card, from its corner (passes a few dp from "TODAY") or down its middle gap (within 6 dp of text on 36 of 144 pages); relief rows.

**Since:** 2026-11-26 · owner · agreed rewording

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

**Rule:** While sets are logged, the past is shown only as the live comparison, today against last time (`SG2`). Last time's numbers show from the start of each set, and today's fill in as they are logged, which also does `LL2`'s job. Last time is the last session that logged the exercise under its current type, matched on each set's `kind` (R121, R101).

**Evidence:** `FloorDao.lastSets` takes only sets of the exercise's current type, from the last closed session that has any (`SessionsTest.lastSetsAreThoseOfTheExercisesCurrentType`, 2026-11-26). Not checked: the exercise screen is not built.

**Acceptance:** On the exercise screen, last time's set N shows before set N is logged, and no other history appears; after a change from Isometric to Timed, the card shows none of the Isometric sets as last time.

**Not:** Last time's numbers whatever their type: reps would face seconds.

**Since:** 2026-11-26 · owner · agreed rewording

### D-R68 · Set input

**Rule:** − and + change a value by its step below, and tapping the number opens the phone's own numeric keyboard for any other value; the app draws no keypad of its own. With the keyboard up, the body scrolls so the number being typed and its set row stay above the keyboard. Weight accepts up to two decimals (61.25 kg), stored in grams.

**Values:**

| Value | Step |
|---|---|
| weight | 2.5 kg, the smallest pair of plates in most gyms |
| reps, partials | 1 |
| seconds (holds, timed sets) | 5 s |

**Evidence:** Not checked: on the 2026-10-22 mockups the keyboard covered the set value by 44 dp and the − stepper by 46 dp, and the body did not scroll (`ck layout`, skill review 2026-11-26).

**Acceptance:** Each stepper tap moves the value by its step (60 → 62.5 kg, 5 → 6 reps, 30 → 35 s); a tap on the number raises the system numeric keyboard with the number and its row in view at font sizes 1.0 and 2.0; 61.25 kg is stored as 61250 g.

**Not:** The app's own keypad: replaced by the phone's keyboard. A step of 1 for every value: 60 → 62.5 kg needed the keyboard, and a hold from 30 to 45 s took 15 taps.

**Since:** 2026-11-26 · owner · migrated

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

**Rule:** A hold starts from the main bar ("Start · set N"), and Done ends it and records its time, as it records a strength set; Done also starts the rest as R71 says. The only running control on this screen is the rest round, at the same height on every exercise screen of a density, in the band between the comparison card and the steppers, so nothing on the screen moves when it appears. The acts below each work as their row says, each Undo shown as long as R129 says.

**Values:**

| Act | What follows |
|---|---|
| a tap on the round | −30 s and +30 s open |
| a held press on the round | the rest is skipped; "Rest skipped · Undo" brings it back with the time it had left |
| Done on a hold | "Hold saved · Undo", so a stray Done is taken back as if never tapped: the saved hold is removed, its clock runs on from the hold's start, and the rest it started stops (`FA4`'s undo) |
| a rest running | the screen stays on, so a glance from the bench finds the round |

**Evidence:** Nothing moves when the round appears, checked within 0.5 dp (2026-10-23).

**Acceptance:** The rest round's top is at the same y on every exercise screen of a density; an Undo within 10 s leaves no row for that hold, shows its clock counting from the hold's start, and the rest round leaves; an Undo after a skip shows the round with the time it had left; while a rest runs the window keeps the screen on, and after it does not.

**Not:** A pill, an inline panel, and a sheet from the bar (the sheet lay over live steppers and failed G1). The rest running on after an Undo. A skip with no way back: a thumb resting on the round while the phone is picked up or put down ends the rest.

**Since:** 2026-11-26 · owner · agreed rewording

### D-R71 · The rest timer

**Rule:** The rest starts on Done (`LL4`) and counts down the length the rows below give, first match winning; it moves by −30 s or +30 s, or is skipped, and runs on with a live notification (`GL2`, R124). How it ends is the person's choice in Settings: vibration, sound, notification, alone or together, kept as a `setting` key.

**Values:**

| Case | Rest | Kept in |
|---|---|---|
| the first side of a per-side exercise | none; the rest follows the second side | |
| the plan sets a length (`LO4`) | that length | `routine_item.rest_seconds` |
| the exercise sets one, ahead in Edit exercise (R103) | that length; "None" is no rest | `exercise.rest_seconds`, nullable, 0 for "None" (schema 3, R121) |
| otherwise | the default in Settings (R72) | a `setting` key |

**Acceptance:** With the plan, the exercise and the default all set, the plan's length wins, even over an exercise's "None"; with none but the default, the default runs; an exercise set to "None" starts no rest; a per-side hold rests only after its second side; ±30 s moves the remaining time by 30 s.

**Not:** `KA5`'s 10 s steps. A rest between the two sides: it doubles every per-side rest. A fixed 15 s between sides.

**Since:** 2026-11-26 · owner · agreed rewording

### D-R72 · Default rest length

**Rule:** The default rest length in Settings is 2 min, used when neither the plan nor the exercise sets one, and the person can change it.

**Why:** Rests over 2 min appear to maximise strength in trained lifters, while 60-120 s suffices for untrained people (Grgic 2018, PMID 28933024, in report 1), so 2 min suits both ends.

**Acceptance:** A fresh install's Settings shows a 2 min default rest, and a rest started with no plan or exercise length counts down from 2:00.

**Not:** 90 s: may be short for heavy lifts. 3 min: long for accessories.

**Since:** 2026-11-21 · owner · agreed rewording

### Q-R124 · The rest timer in the background

**Rule:** How does a rest keep time and end on time while the app is in the background or the phone sleeps?

**Default:** A notification that counts down by itself (Android's chronometer) and one alarm at the rest's end; if the person refuses notifications, the rest still runs on screen and its end vibrates while the app is open. Changes: R71's live notification and end choices, and the permissions stage 6 asks for. Settled in stage 6's plan.

**Why:** Android 13 and later ask the person before an app may post notifications; Android 14 leaves exact alarms off by default for new installs; OnePlus phones close background apps aggressively. A countdown kept by the app's own process can stop without warning.

**Since:** 2026-11-26 · owner · agreed rewording

### D-R73 · Session clock

**Rule:** The session clock is on every session and exercise screen, in the header row, left of Finish, at the same place and size on all six screens.

**Evidence:** Scored with consistency first: under the title it was 2 dp off on the session screens, and as first drawn it moved by 76 dp (2026-10-23).

**Acceptance:** The clock's bounds are the same on all six session and exercise screens.

**Not:** Under the title (2 dp off between screens); under Finish, which tied but took Finish's hit box down to 42 dp.

**Since:** 2026-10-23 · owner · migrated

### D-R74 · The rest round is the boulder

**Rule:** During a rest the round is the boulder, which the figure pushes up an implied 20° slope from its left; the round stays where R70 puts it, centred. They appear only during a rest, a small thing to look forward to, and both leave the screen at 0:00, on a skip or on an Undo (R70). Each part behaves as its row below says.

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

**Acceptance:** During a rest the figure and the boulder are drawn, and after 0:00, a skip or an Undo neither is; with reduced motion the stone's angle never changes.

**Not:** The track versions (a summit to reach, a stone rolling back); a stone at the ring's head (not seen at true size); a centred pair (the round stays centred).

**Since:** 2026-11-26 · owner · agreed rewording

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
| body | #1c1c1c, the text's grey (R126) |

**Evidence:** Martínez-Serrano 2021, Osterwald 2021 (2026-10-27). The body's #1e1e1e merged into the text's #1c1c1c, 0.63 ΔE2000 apart (palette round, 2026-11-26).

**Acceptance:** none: judged by eye on the 6T at true size; the figure is built with the rest-timer stage (stage 6).

**Since:** 2026-11-26 · owner · agreed rewording

### D-R76 · The boulder's form

**Rule:** The boulder is the owner's own outline (rocc1, first cleaning, its bumps kept), 80 dp across, overhanging the 72 dp control by 4 dp, at font size 1.0; the touch target stays the control's size, and both grow together (R80). Nothing is on its face.

**Evidence:** Chosen in motion, with the figure walking: its many dents and peaks make the turn read faster than a smoother rim. Stills had put a reworked smooth rim ahead (0.844 against 0.650), but the owner judges turning only in motion. The owner called the choice provisional ("for now") on 2026-10-26.

**Acceptance:** The boulder is drawn 80 dp across at font size 1.0 on a 72 dp target.

**Not:** Marks on the face: busy and somewhat cartoonish in the page. A smoother rim: the turn reads slower.

**Since:** 2026-11-26 · owner · agreed rewording

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
| evidence-note chip | 28 dp high, below the card's text in the hierarchy, between two carved lines; its touch target on R50's step 1 |

**Evidence:** The pieces' sizes match the decided mockups within 1 dp, all 15; that check found the chip at an older 32 dp (2026-10-28).

**Acceptance:** The round's, the values' and the card gap's centres lie on the screen's centre on every exercise screen.

**Since:** 2026-11-26 · owner · agreed rewording

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

**Acceptance:** G1-G5 pass on the exercise screens at every font size G5 names (R52).

**Not:** Capping the card's text at 1.3 (0.760: its values stopped at 20 dp where 28 were asked, and its heads touched); shrinking it to keep the mirror (0.698); last time behind a tap (0.585).

**Since:** 2026-11-26 · owner · agreed rewording

### D-R80 · The rest round grows with its time

**Rule:** The boulder, the white ring and the figure scale together by the time's font scale, so the time keeps its asked size inside the ring; a time still wider than the ring at that size ("10:00") steps down until it fits. The round stays on the centre line with the figure at its left, and at large font sizes it may sit below the first screenful and scroll with the body; while it is out of view, the remaining time also shows as plain text in a fixed place outside the body, set by the exercise screen's layout evaluation.

**Values:**

| Font size | Time | Scale | Boulder |
|---|---|---|---|
| 1.0 | 19 sp | 1.0× | 80 dp |
| 2.0 | 32 dp | 1.68× | about 135 dp; its 72 dp touch target scales with it to about 121 dp |

**Evidence:** The time outside the body follows `app-register-layout`'s co-visibility requirement: what a step needs shows together (skill review, 2026-11-26). Not checked: not built.

**Acceptance:** At font size 2.0 the boulder is about 135 dp, its touch target about 121 dp, and "10:00" fits inside the ring; scrolled so the round is out of view, the time still shows.

**Not:** The time shrinking inside an unchanged 80 dp boulder (0.760 against 0.762; the time stopped at 24 dp where 32 were asked); a time fixed in dp (0.749); the time beside the round (0.708).

**Since:** 2026-11-26 · owner · agreed rewording

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

**Acceptance:** G1-G5 pass on the compact exercise screens at 360-412 dp and every font size G5 names (R52).

**Not:** The round under the card (lost to the band centre at every size); scaled stones.

**Since:** 2026-11-26 · owner · agreed rewording

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

**Rule:** The stores in `:data` behave as the rows below say, on schema 3 (R28). A database that cannot be opened is a state the screen shows, never a crash.

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

**Since:** 2026-11-26 · owner · migrated

### D-R87 · Starting while a session is open

**Rule:** Starting a session while one is open resumes it instead of failing: `SessionStore.start` returns the open session's id and adds nothing. A session left idle is closed first, or dropped when nothing was logged in it, as R96 says, so only one still in use is resumed. A database rule allowing only one open session (a partial unique index) waits for the backup plan's schema, where a restore that brings two is settled.

**Evidence:** `SessionStore.closeIfIdle`, run by Start when it opens; `SessionsTest` and `StartViewModelTest` (2026-11-26).

**Acceptance:** `startingWhileOneIsOpenResumesIt` passes; it was red on the old refusal.

**Not:** Resuming an idle session: a morning's session would take the evening's sets, and one from an earlier day would count today's sets on that day (R30).

**Since:** 2026-11-26 · owner · agreed rewording

### D-R88 · Tab screens at larger font sizes

**Rule:** The tab screens follow R56: rows, buttons, pills and their hit boxes take their sizes as minimums, Start's main bar wraps, and the body scrolls between the header and the fixed tab bar. When any module card's or history card's name would wrap beside its pill, every card on that screen puts its pill under its text, right-aligned, and the name takes the card's width. "Log afterwards" stays the last item of Start's list and scrolls with it.

**Evidence:** Gated on the four tab screens at both densities, 360, 384 and 412 dp, font sizes 1.0-2.0 and long names ("Start Wednesday afternoon", "Single-leg Romanian deadlift · Plank · Walk"): G1-G5 pass on all 144 pages, where the earlier tab pages failed G1, G2 and G4 from 1.3. Owner's axes, equal weights: the pill under with "Log afterwards" in the list 0.864 (2026-10-28).

**Acceptance:** G1-G5 pass on all 144 tab pages.

**Not:** Hiding the tab bar while scrolling down (the owner's pick on the 6T kept it fixed); "Log afterwards" fixed above the main bar (0.823); the whole card as the target with a chevron (0.823, lowest harmony); the pill beside (0.772).

**Since:** 2026-11-26 · owner · agreed rewording

### D-R89 · Tab roots without screens

**Rule:** A tab whose screen is not built shows its name at its root.

**Acceptance:** Each unbuilt tab's root shows its name; `AppShellTest.eachTabShowsItsRoot` reaches every root.

**Since:** 2026-10-28 · owner · migrated

## 14. First launch

### D-R90 · The module choice screen

**Rule:** Before the tabs, with no tab bar, the app asks once for the modules, with the wording below. All five modules start unticked and all five are offered, each one not built yet marked "Arrives in a later update"; stepping forward with none ticked is a choice, and a tap anywhere on a card ticks it, except in the strip that holds its "info" chip (R91).

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

**Since:** 2026-11-26 · owner · agreed rewording

### D-R91 · Module cards on the choice

**Rule:** Each card shows the module's icon before its name and an "info" chip (carved lines, `type.chip`) in a strip of its own at the card's end, after the box (under the text and box at R88's reflow), whose touch target lies outside the card's tick area and at least 5 dp from it, that opens a small raised card in place with "Includes: …"; Back or a tap elsewhere closes it, and with TalkBack the open card is announced while the list behind it waits. At large font sizes the cards follow R88's reflow, and the screen keeps clear of the sides as R85 says. A choice is recorded even when Back follows "Step forward" at once, and while it is read only the page is drawn.

**Evidence:** The owner's picks on the 6T: the "info" chip over two symbol marks ("info is more explanatory"), the icon before the name. A mutant on Back closing the card was caught (2026-10-29). The chip left the card's toggle, where `design-critique-plus` counted it as overlapping targets, for its own strip 8 dp away; the box moved before it so the tick area stays one rectangle (`ModuleChoiceScreenTest.theInfoChipLiesOutsideTheTickArea`, 2026-11-26).

**Acceptance:** `ModuleChoiceScreenTest` checks the open card, Back and TalkBack; `ShellViewModelTest.aChoiceSurvivesLeavingAtOnce` checks the choice survives Back.

**Not:** The circled "i" as the mark: it stays the evidence notes' (R16).

**Since:** 2026-11-26 · owner · migrated

### D-R92 · When the data cannot be opened

**Rule:** When the data cannot be opened, such as after a failed migration (R33), a screen says so on any launch in place of the choice or the tabs, with the wording below; a file R35 set aside is told by R122 once an open succeeds. "Try again" opens again, and a failed open is not kept. Restoring from a backup joins this screen with the backup package.

**Values:**

| Part | Wording |
|---|---|
| title | "Your data couldn't be opened" |
| line | "Nothing has been deleted. Try again; if it keeps failing, the app may need an update." |
| bar | "Try again" |

**Evidence:** Goldens `cant-open-balanced-{100,200}`, re-recorded with this line (2026-11-26).

**Acceptance:** `CantOpenTest` passes (the screen replaces the choice and the tabs; a retry while broken stays; a retry once it opens goes on), and `CantOpenScreenshotTest`'s goldens match.

**Not:** "Restart your phone": it cannot fix a failed migration or a file a newer version wrote (R33).

**Since:** 2026-11-26 · owner · agreed rewording

### D-R122 · When the data was set aside

**Rule:** When R35 has set a damaged file aside and opened a fresh database, a screen says so before the module choice or the tabs, on every launch until the person dismisses it, with the wording below. Restoring from a backup joins this screen with the backup package.

**Values:**

| Part | Wording |
|---|---|
| title | "Your data was damaged" |
| line | "It couldn't be read, so it was set aside on your phone, not deleted. Your old data is kept and can be restored once backups arrive in a later update. Until then the app starts again from the beginning." |
| bar | "Start fresh" |

**Evidence:** The set-aside file on disk is the mark (`newestSetAside`), so a failed open or a killed process after the set-aside cannot lose the notice; "Start fresh" records its stamp as seen (`set_aside_seen`), and a later file is told again. Goldens `set-aside-balanced-{100,200}` (2026-11-26). Not checked: restoring a backup made before the damage brings back no seen stamp, so the notice shows once more.

**Acceptance:** A launch whose open set a file aside shows this screen first, and so does the next launch if the app was closed before it was dismissed (`ShellViewModelTest.aSetAsideFileIsToldUntilDismissed`, `SetAsideTest`); the set-aside file is still on disk.

**Not:** A silent fresh start: the module choice would come back as if the app were new.

**Since:** 2026-11-26 · owner · agreed rewording

## 15. Start or log

### D-R93 · Build order for Start

**Rule:** Exercises and routines are built first, then the Strength session, then Start. Another order may replace it when one serves better.

**Why:** Start's buttons lead to the session, which needs exercises, so every button works when it ships.

**Acceptance:** none: an order of work; Start's plan names the built screens each button opens.

**Since:** 2026-11-21 · owner · agreed rewording

### D-R94 · What Start shows

**Rule:** Start shows only the chosen modules, in R19's order, with the wording below. Start is built straight in the stone language, with no variants to score, and passes the gates of R52 before the owner judges it on the 6T walk.

**Why:** On Start "Arrives in a later update" takes the pill's place because the card cannot be started; on the module choice (R90) it is a line under the description because the card can still be ticked.

**Values:**

| Case | Start shows |
|---|---|
| a chosen module not built yet | "Arrives in a later update" in place of its pill |
| no module chosen | "Nothing here." with "Choose paths", which opens the choice in Settings (R51), and the main bar for a free session |

**Acceptance:** With Strength and Stamina chosen and only Strength built, Start shows two cards in that order, Stamina's reading "Arrives in a later update".

**Since:** 2026-11-26 · owner · migrated

### D-R95 · Session names

**Rule:** The main bar names a new session by its day and part of day in the phone's zone, as below, abbreviated ("Tue morn") only when the full name does not fit the screen. The day is the session's counting day (R30), so 00:30 on a Wednesday is "Wednesday night".

**Values:**

| Part of day | Hours |
|---|---|
| morning | 05-12 |
| afternoon | 12-17 |
| evening | 17-21 |
| night | 21-05 |

**Acceptance:** A session started on a Tuesday at 08:00 is "Tuesday morning", and one at 00:30 on a Wednesday "Wednesday night"; at 360 dp and font size 2.0 it shortens only if the full name does not fit.

**Not:** Naming a session before 05:00 by the evening before: its name and its counting day would disagree. Counting it on the day before: R30 would change.

**Since:** 2026-11-26 · owner · agreed rewording

### D-R96 · The resume card

**Rule:** While a session is open, Start shows the resume card below and hides the module cards; but when Start opens, every open session idle for over 3 h, counted from its last set or track point or, with none, from its start, is closed as stopped early at that moment (with only a time of its own, that long after its start), or dropped when nothing was logged in it, and the module cards show; one with only a note waits for the question below, so the note is kept. "End/Save" records the session as stopped early (`KA2`), ending at its last set's time. A session without sets asks when it ended, proposing the time it was paused, and records that.

**Values:**

| Part | Wording |
|---|---|
| title | "Ongoing session, paused · Tuesday morning" |
| line | "Paused at 08:31 · achieved 4 sets" |
| main bar | "Resume this session" |
| pill | "End/Save" |

**Evidence:** `SessionStore.endAndSave` ends at the last set, or at a time given for a session without sets that lies between its start and the tap, and `SessionStore.closeIfIdle` closes or drops every idle one when Start opens (`SessionsTest`, `StartViewModelTest`, 2026-11-26). Not checked: the resume card and its end-time question are not built, and the app records no pause time to propose yet.

**Acceptance:** With a session open, Start shows the resume card and no module card; "End/Save" at 09:40 on a session whose last set was at 08:31 leaves it STOPPED_EARLY, ended at 08:31; Start opened at 18:00 on a session whose last set was at 08:31 the same day closes it at 08:31 and shows the module cards; Start opened 3 h 1 min after a session with nothing logged began leaves no session; a session started at 23:30 with a set at 00:05 still shows the resume card at 00:15.

**Not:** Ending when "End/Save" is tapped: a session forgotten for hours would last hours. Closing every session from an earlier day: one still going past midnight would be cut. Closing only sessions from an earlier day: a morning's session left open would take the evening's sets. Keeping a session with nothing logged: a mistaken start would wait as a card. Ending a session without sets at its start: it would record no time.

**Since:** 2026-11-26 · owner · agreed rewording

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

**Rule:** Exercises are made in the flow, from the session's "Add an exercise" sheet (Start, Strength, the session), and managed under More, grouped by module in R19's order; an exercise with no module goes in "Other" after them, and archived exercises in "Archived" at the foot, each with "Bring it back". Within a group exercises run by last use, the latest first, as in the add sheet (R100). Editing lives only under More.

**Evidence:** The groups follow Start's order and the exercises the add sheet's, so a list repeated across screens keeps one order (`app-register-layout`, skill review 2026-11-26).

**Acceptance:** The add sheet has no edit action; More lists the exercises grouped by module in R19's order, then "Other", then "Archived", the latest used first in each, each opening its editor; an exercise with no module is listed under "Other".

**Not:** More, "Exercises and routines", ungrouped, as the first thing a new person does: clunky and low on the smooth path. Edit and Open pills on each row: "Open" was unclear, and the add sheet has no editing.

**Since:** 2026-11-26 · owner · migrated

### D-R100 · The add sheet

**Rule:** The session's "Add an exercise" sheet filters and adds as the rows below say. Each row adds as a whole, cued by an "Add" pill, and the sheet stays open after adding.

**Values:**

| Case | Behaviour |
|---|---|
| tags offered | the modules in R19's order, then the person's own categories A-Z (R103), never labels (R114); tags no exercise carries are hidden |
| opened from a module's session | that module's tag starts picked |
| exercises listed | by last use, the latest first, the order fixed when the sheet opens; those never used after them, A-Z |
| several tags | the exercises carrying all of them |
| no tag and no name | no list |
| a typed word with no tag | searches every exercise's name and categories (R113) |
| "New exercise" | always shown; starts with the picked tags |
| an exercise in the session when the sheet opens | not listed |
| a row tapped | stays in place, its pill reading "Added" with a tick; a second tap takes it back out; rows never move while the sheet is open |
| an exercise made, or brought back (R102), from "New exercise" | joins the list's end as an "Added" row; the rows above it stay |
| "X added to this session." | screen-reader text only, never shown |

**Evidence:** Built into the trial as version 2 and adjusted in round 3 (2026-10-30): the visible line pushed the list down on the first add. With pill shapes whose ends change with width, tapping a row could wrap its details and move 11 of 14 rows below (2026-10-31), see R112. The order by last use and the module picked on opening follow `app-register-layout`'s most-used-first and route-cost requirements: adding two exercises scrolled 674 dp at 384 dp and 3420 dp at 360 dp and font size 2.0 with A-Z (skill review, 2026-11-26).

**Acceptance:** Tapping a row twice leaves every row's position unchanged; an exercise added before the sheet opened is not listed; opened from a Strength session, the sheet lists Strength's exercises at once, the last used first.

**Not:** Listing the session's exercises as "Added" too: a tap could take out one with logged sets.

**Since:** 2026-11-26 · owner · agreed rewording

### D-R101 · Changing an exercise's type

**Rule:** An exercise's type and its per-side setting stay editable. Once the exercise has logged sets, switching either asks first, with the wording below, and logged sets keep their numbers, which the comparison then skips (R67). The "Show partial reps" switch is offered for Dynamic exercises only.

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

**Since:** 2026-11-26 · owner · agreed rewording

### D-R102 · Editing an exercise

**Rule:** Editing follows the rows below.

**Values:**

| Case | Behaviour |
|---|---|
| modules offered | the chosen modules, plus any the exercise already carries |
| a name already used, case ignored | refused: "You already have an exercise called Plank." |
| a name an archived exercise has, case ignored | refused: "You have an archived exercise called Plank." with "Bring it back", which unarchives it with its history and discards the form in progress; from Edit it opens the revived exercise in its editor, and from the add sheet's "New exercise" it joins the session as an "Added" row |
| archive | a quiet pill at the foot of Edit; archived exercises leave their group for "Archived" (R99), their sets stay |
| Back with unsaved changes | a one-line sheet "Discard your changes?" with Discard and Keep editing |
| look | the stone language, judged on the 6T |

**Acceptance:** Saving "plank" beside "Plank" shows the refusal; an archived exercise moves to "Archived" with its sets kept in History, and "Bring it back" there returns it to its group; saving "Plank" while "Plank" is archived offers "Bring it back", and taking it lists the exercise again with its sets.

**Not:** Archived names left free: two exercises called "Plank" could exist, one of them hidden.

**Since:** 2026-11-26 · owner · agreed rewording

### D-R103 · Exercise editor fields

**Rule:** The editor has, besides name, type, per side and modules, the optional fields below.

**Values:**

| Field | Behaviour |
|---|---|
| Default rest | optional; Default · None · 30 s · 60 s · 90 s · 2 min · 3 min; Default (where it starts) uses Settings' length (R72), None means no rest after its sets (R71) |
| Notes | free text (self-notes, cues, equipment), readable from inside the session; whether it is also edited there, against R48 and R99, is settled in the Strength session plan |
| Categories | the person's own words (dumbbells, core, push, legs), several per exercise, apart from modules and session labels; picked as R106 says, a search matching the start of any word, case ignored; none to start with |

**Evidence:** Schema 3 holds `exercise.rest_seconds` (null for Default, 0 for None), `exercise.note` and the `category` and `exercise_category` tables (R121, 2026-11-26). Not checked: the editor is not built.

**Acceptance:** Plank can be made with 60 s rest, a note and the category "core", and the session shows its note; a new exercise's rest reads "Default".

**Not:** The editor as one sentence whose underlined words are its controls: the form beat it in the trial (2026-10-30). "None" as the starting value: it would switch rests off for every new exercise.

**Since:** 2026-11-26 · owner · agreed rewording

### D-R104 · How the type is chosen in the editor

**Rule:** "New exercise" first asks "What kind of exercise?" in a sheet with the four types (pattern 1.1); the form then shows the type as a plain line with "Change", whose touch target sits on R50's step 1. Changing it later follows R101.

**Why:** Pattern 1.1 scored highest on the owner's axes from outside references (3.17, against 3.06 for 1.4, the inline segmented bar; `research/refs-2026-11-01/score/results.html`), and it fits a job done rarely, at the start (R01).

**Evidence:** `app-register-layout` supports 1.1 asked in a sheet: its low consistency score (2.3 against 3.7) came from a centred dialog stacked over the add sheet (skill review, 2026-11-26).

**Acceptance:** "New exercise" opens the type sheet before the form, and the form shows the picked type with "Change".

**Not:** A centred dialog: stacked over the add sheet, it is a second kind of overlay. Pattern 1.4, the inline segmented bar (3.06).

**Since:** 2026-11-26 · builder · own words

### D-R105 · How modules are picked in the editor

**Rule:** The editor's modules are filter chips that wrap (pattern 2.2), picked by outline and weight as R126 says, and each chip reserves the width of its picked label, so picking never moves a chip to another line.

**Why:** Pattern 2.2 scored 3.23 on the owner's axes (`research/refs-2026-11-01/score/results.html`).

**Evidence:** `app-register-layout` supports 2.2 only with the width kept: as drawn, a picked module chip moved to another line (`measure.json`), a row moving under the finger (skill review, 2026-11-26).

**Acceptance:** Picking and unpicking each module chip leaves every chip where it was, at 360 and 384 dp and font sizes 1.0 and 2.0.

**Not:** Chips that widen when picked: one moved to the next line.

**Since:** 2026-11-26 · owner · agreed rewording

### D-R106 · How categories are picked in the editor

**Rule:** A "Categories" row shows the picked categories as chips and "+ Add", which opens a sheet with a search box, every category as a checkbox row A-Z, a "Create x" row and Done (pattern 3.3).

**Why:** Pattern 3.3 scored 3.28 on the owner's axes (`research/refs-2026-11-01/score/results.html`).

**Evidence:** `app-register-layout` supports it: the least scroll (64 dp), the picks visible in the form, and the app's own sheet (skill review, 2026-11-26).

**Acceptance:** Plank gets "core" through "+ Add", typing "co", ticking "core" and Done, and the form shows "core"; "Create cardio" makes the category and picks it.

**Not:** A type-ahead field in the form: R103 had one until 2026-11-26.

**Since:** 2026-11-26 · builder · own words

### D-R107 · How optional fields show

**Rule:** The editor shows its optional fields as value rows that open a sheet or a list (pattern 4.2): categories as R106 says, the default rest as R108 says; notes are R109's box.

**Why:** Pattern 4.2 wins on the judged axes (3.17); 4.1, everything up front marked "(optional)", led overall (3.26) on familiarity and a code-size proxy (`research/refs-2026-11-01/score/results.html`).

**Evidence:** `app-register-layout` picks 4.2: making exercises is rare (R01), so its extra taps cost little, every field comes within 46 dp of the first screen against 174 dp, and at 360 dp and font size 2.0 the form scrolls 352 dp against 702; it also matches R106 and R108 (skill review, 2026-11-26).

**Acceptance:** At 384 dp and font size 1.0, every field of a new exercise's form starts within 46 dp of the first screen.

**Not:** Pattern 4.1: more scroll on a rare job, its lead from familiarity rather than from a requirement.

**Since:** 2026-11-26 · builder · own words

### D-R108 · How the default rest is picked

**Rule:** The default rest is a value row "Default rest · Default ›" that opens a list of R103's seven choices; a tap picks and closes (pattern 5.1).

**Why:** Pattern 5.1 scored 3.37 on the owner's axes, 5.2, the chips inline, 3.34 (`research/refs-2026-11-01/score/results.html`).

**Evidence:** `app-register-layout` supports it: mis-tap safety 3.7 against 2.8, and the inline chips widened 24.9 dp when picked (skill review, 2026-11-26).

**Acceptance:** Tapping the row, then "60 s", closes the list and the row reads "Default rest · 60 s".

**Not:** Pattern 5.2: the chips widen when picked.

**Since:** 2026-11-26 · owner · agreed rewording

### D-R109 · How notes are written

**Rule:** Notes are a box in the form that grows with its text (pattern 6.3).

**Why:** Pattern 6.3 scored 3.56, the only clear win of the round (`research/refs-2026-11-01/score/results.html`).

**Evidence:** `app-register-layout` supports it (skill review, 2026-11-26).

**Acceptance:** A five-line note shows whole in the form, with no scrolling inside the box.

**Since:** 2026-11-26 · builder · own words

### Q-R111 · How the add sheet adds

**Rule:** How does the add sheet take several exercises?

**Default:** Pattern A.1, the checklist that stays open with rows that stay put (3.14), as R100 has it. Changes: none. Next: A.4, tick several then confirm with "Add 2" (3.12). `app-register-layout` finds a plateau and leans to A.4 on scroll (2068 dp against 3420 at 360 dp and font size 2.0), measured with exercises A-Z; it is measured again with R100's order by last use before the exercises plan.

**Why:** Scored on the owner's axes (`research/refs-2026-11-01/score/results.html`).

**Since:** 2026-11-26 · builder · own words

### Q-R112 · Pill and chip ends

**Rule:** What ends do action pills and choice chips take?

**Default:** Action pills with round ends (P.1, which the judges preferred) and choice chips as 8 dp rounded rectangles sized to their label (P.2, 3.18), so the two roles do not share one shape. Changes: `radius.pebble-pill`, the pills of R65 and the chips.

**Why:** `radius.pebble-pill` is in % of the pill's size, so the ends change shape with width and look uneven across label lengths, and the owner is rethinking the pebble pills. The skills leave the ends to taste and ask only that roles which differ look different (`design-critique-plus`); the picked state is settled by R126 and R105.

**Since:** 2026-11-26 · builder · own words

### D-R113 · How the add sheet's search reaches categories

**Rule:** In the add sheet, a typed word matches exercise names and categories alike ("legs" finds Lunge), with no mode to choose.

**Evidence:** The trial measured it at 3 taps against 4 for a switch by the search box, with the same 227 dp of scrolling (2026-10-31).

**Acceptance:** Typing "legs" with no tag picked lists every exercise named or categorised with a word starting "legs".

**Not:** A switch by the search box between names and categories: one tap more.

**Since:** 2026-11-21 · owner · agreed rewording

### D-R114 · Categories and labels

**Rule:** Labels tag sessions only, and categories tag exercises only; they are two separate lists of the person's own words.

**Acceptance:** No exercise carries a label and no session a category; the add sheet's tags are modules and categories.

**Not:** One merged list of words for sessions and exercises: it mixes two jobs.

**Since:** 2026-11-26 · owner · agreed rewording

## Acceptance list

- R02 · Records and comments, never prescribes: `python tools/evidence_check.py` passes: its wording check refuses instructions, promises and second person in every note.
- R03 · Platform: `./gradlew --offline check` compiles and tests `:core` and `:data` on the desktop JVM.
- R04 · Package name: `applicationId` in `app/build.gradle.kts` is `com.hazia.sisyphusfit`.
- R05 · Repository and local research: `research/` is in `.gitignore`, and `python ~/.claude/tools/publish_check.py .` passes before every push.
- R06 · Gradle modules: `settings.gradle.kts` includes exactly these modules, and each module's dependencies stay within its row.
- R07 · No account, no network: The merged manifest declares no `INTERNET` permission.
- R09 · Compose Foundation only: No `androidx.compose.material` artifact is on the app's dependency list; the gallery builds only in the debug variant (`GalleryActivity` under `app/src/debug`).
- R10 · Bundled font files: `FontFilesTest.fontFilesAreTheRelease` matches both hashes.
- R11 · Android's own backup: `data_extraction_rules.xml` excludes every domain from `cloud-backup` and leaves `device-transfer` whole; `backup_rules.xml` includes every domain only with `requireFlags="deviceToDeviceTransfer"`.
- R13 · Signing key: `app/build.gradle.kts` declares no `signingConfig`, and no keystore is in the repository.
- R14 · Evidence sources: `python tools/evidence_check.py` with the reports present finds every source id in a report's verified id list.
- R16 · Commentary rules: `python tools/evidence_check.py` passes on the pack; on a note, the brief level shows claim and certainty only, and "Details" opens the rest.
- R17 · Evidence pack: `python tools/evidence_check.py` passes in CI: every source id is in a report's verified id list, every feature id is one this spec names, and no note text exists without a claim id.
- R120 · Notes that span modules: `python tools/evidence_check.py` passes with all three in the pack, and each note's chip appears only on screens of the modules it spans; two days with nothing logged change no note.
- R19 · The five modules: The module choice offers these five in this order (`ModuleChoiceScreenTest`).
- R20 · Body measures without verdicts: No table, column or code path computes a BMI, a BMR or a forecast; no screen shows one.
- R21 · Modules chosen by the person: A fresh install shows the module choice before any tab, with every module unticked (`ModuleGateTest`, `ModuleChoiceScreenTest`); switching Strength off and on again leaves every Strength session and exercise as it was; with Daily movement off, no reminder or prompt fires and no sensor steps are recorded.
- R23 · Modules are tags: A session with sets has the modules of its exercises' tags; a set-less session has exactly the module it was logged under; no exercise carries a label.
- R24 · This week counts a mixed session by its parts: `CoreTest`'s week tests pass: `aMixedSessionCountsByItsParts`, `anExerciseWithTwoTagsCountsWhollyUnderEach`, `aSessionCountsOnceInAModuleHoweverManyOfItsSetsAreThere`, `anUntaggedExerciseCountsNowhereAndAnEmptyWeekIsEmpty`.
- R25 · Each module's own units in This week: A 30-minute walk logged under Daily movement adds 30 min to Daily movement's time and nothing to any other module; steps never appear as sets or minutes; This week has no Body measures section.
- R26 · Units: Every column for these quantities is INTEGER in Room's exported `3.json`.
- R27 · Ids: `CoreTest`'s ULID tests pass, and every table in `3.json` has a text primary key `id`.
- R28 · Tables: Room's exported `3.json` holds exactly these tables and columns.
- R121 · Schema 3 additions: Room's exported `3.json` holds these columns and tables, `MigrationTest` and `AndroidMigrationTest` pass from schema 1 and schema 2, and `OldBackupTest` restores both pinned backups with every set under its exercise's type.
- R29 · An amount is a session without times: `CoreTest`'s `overlappingSpansCountOnceAndAmountsAdd`, `aSpanInsideAnotherAddsNothing` and `emptyDayIsZero` pass.
- R30 · The counting day: `CoreTest.aSessionCountsOnTheDayItStartsInItsOwnZone` passes, and a stored session keeps its `day` after the phone's zone changes.
- R31 · Only done sets are stored: Stopping a session early leaves only the sets marked done.
- R32 · Partial reps: A set logged as 7 and 1 partial reads back as reps 7, partial_reps 1; an exercise with the switch off shows no partial count.
- R33 · Migrations add, never rewrite: `MigrationTest` passes on the desktop and `AndroidMigrationTest` under Robolectric.
- R34 · Nothing logged is lost: Stage 1a is not done until the backup package (`KA9`, `OS8`) and resume after process death (`GL1`) pass their tests.
- R35 · The database never deletes itself: That Robolectric test passes, and a set-aside file leads to R122's screen.
- R36 · Backup format: Export, restore into an empty database and export again give byte-identical files; the pinned version-1 and version-2 files restore.
- R123 · Scheduled backups never replace a good one: Two scheduled runs leave two files; after a backup of 40 sessions and a fresh start, the next run still writes its file, the clean-up keeps the 40-session backup, and Settings shows that the data shrank.
- R37 · Gates: `.github/workflows/ci.yml` runs the first three; the pre-push hook runs the fourth.
- R43 · Where suggestions stop: With the setting off, no proposal appears; with it on and every set hit, the proposal equals last time's numbers plus the rule's step, and editing it changes nothing logged.
- R44 · Screens grouped by purpose: `purpose_map.py --map` passes on the working map: all 162 harvest rows placed, shared or left out.
- R46 · Doing and showing screens: The tab roots of This week, History and More draw no main bar.
- R47 · Calm before complete: Gate G1-G5 runs pass, and the owner signs the screen off on the 6T.
- R48 · One home per thing: Every edit action in the app leads to one screen per thing.
- R49 · One form per element: The stone pieces in `ui/stone` are the only implementations of these elements, and their goldens match on every screen that uses them.
- R50 · Touch areas and hierarchy: Gates G3 and G4 pass; the unit tests of the pieces check touch targets of at least 42 dp; every touch target's `size` token sits on one row of the ladder.
- R51 · Judgement-free: No screen text praises, blames or counts missed days; the empty state reads "Nothing here.", with its action where the list has one.
- R52 · Gates G1-G5: The gate script reports G1-G5 passing on every page of the screen at both densities, 360-412 dp and font sizes 1.0-2.0.
- R127 · Pressed and focused pieces: Each piece's golden includes its pressed and its focused state; with a keyboard, Tab reaches every control on a screen and its ring shows.
- R128 · One-message screens: On the goldens of R92 and R122 at 1.0 and 2.0, the line's last row ends within `space.section` of the bottom zone.
- R129 · Ending, archiving and deleting: Each control's screen test finds it last in its group and at least 16 dp from the main action; Undo within 10 s restores the session or the exercise as it was, and with a longer "Time to take action" the Undo stays that long; deleting asks first.
- R53 · Greyscale: Every colour role in `design/tokens.json` resolves to a grey or a black alpha, and with the phone in dark theme the app draws as in light.
- R54 · Screen width: At 600 dp wide the content column is 412 dp and centred on the screen.
- R55 · Fit on the phone's app area: The goldens of the 6T's app area pass with and without the sheet.
- R56 · Text follows the phone's font size: The unit tests check Android 14's curve at 2.0, the same curve on Android 11 at 1.3 (`android11UsesTheSameNonlinearCurve`, `lineBoxFollowsTheTextOnAndroid11`), and each piece's font-size behaviour; the goldens at 1.0, 1.3 and 2.0 pass.
- R57 · Density: A fresh install draws balanced, and the compact setting switches every token to its compact value.
- R58 · English first: Every string the app shows is English.
- R59 · Typeface: The theme's unit tests check the variable axes and the carve by role.
- R60 · Type roles: `python tools/tokens.py check` passes and checks both ratios; at font size 2.0 the exercise screen's golden shows each heading above its content in weight or carve.
- R61 · Text line boxes: The goldens at 1.3 and 2.0 pass, and wrapped lines never overlap.
- R62 · Design tokens: `python tools/tokens.py check` passes.
- R126 · Colour and spacing targets: `python tools/tokens.py check` computes each row and passes.
- R63 · Evidence note sheet spacing: The note sheet's golden matches these values.
- R64 · Icons: `IconsTest` and `StoneIconTest` pass, and every icon stays distinct at 16 dp and 48 dp.
- R65 · Stone language: The goldens of the gallery pass, and the pieces' sizes match the decided mockups within 1 dp.
- R66 · The tab bar's valley: `TabBarTest` checks the valley's geometry and motion; the tab goldens pass.
- R67 · The live comparison: On the exercise screen, last time's set N shows before set N is logged, and no other history appears; after a change from Isometric to Timed, the card shows none of the Isometric sets as last time.
- R68 · Set input: Each stepper tap moves the value by its step (60 → 62.5 kg, 5 → 6 reps, 30 → 35 s); a tap on the number raises the system numeric keyboard with the number and its row in view at font sizes 1.0 and 2.0; 61.25 kg is stored as 61250 g.
- R69 · The main bar and running controls: Every doing screen draws one main bar at the bottom; no running control sits beside it.
- R70 · The running control on the exercise screen: The rest round's top is at the same y on every exercise screen of a density; an Undo within 10 s leaves no row for that hold, shows its clock counting from the hold's start, and the rest round leaves; an Undo after a skip shows the round with the time it had left; while a rest runs the window keeps the screen on, and after it does not.
- R71 · The rest timer: With the plan, the exercise and the default all set, the plan's length wins, even over an exercise's "None"; with none but the default, the default runs; an exercise set to "None" starts no rest; a per-side hold rests only after its second side; ±30 s moves the remaining time by 30 s.
- R72 · Default rest length: A fresh install's Settings shows a 2 min default rest, and a rest started with no plan or exercise length counts down from 2:00.
- R73 · Session clock: The clock's bounds are the same on all six session and exercise screens.
- R74 · The rest round is the boulder: During a rest the figure and the boulder are drawn, and after 0:00, a skip or an Undo neither is; with reduced motion the stone's angle never changes.
- R76 · The boulder's form: The boulder is drawn 80 dp across at font size 1.0 on a 72 dp target.
- R77 · What the figure and the stone share: `rest.ring-track` resolves to `rest.near-leg` in `design/tokens.json`.
- R78 · One centre line: The round's, the values' and the card gap's centres lie on the screen's centre on every exercise screen.
- R79 · The comparison card at larger font sizes: G1-G5 pass on the exercise screens at every font size G5 names (R52).
- R80 · The rest round grows with its time: At font size 2.0 the boulder is about 135 dp, its touch target about 121 dp, and "10:00" fits inside the ring; scrolled so the round is out of view, the time still shows.
- R81 · The compact rest screen: G1-G5 pass on the compact exercise screens at 360-412 dp and every font size G5 names (R52).
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
- R122 · When the data was set aside: A launch whose open set a file aside shows this screen first, and so does the next launch if the app was closed before it was dismissed (`ShellViewModelTest.aSetAsideFileIsToldUntilDismissed`, `SetAsideTest`); the set-aside file is still on disk.
- R94 · What Start shows: With Strength and Stamina chosen and only Strength built, Start shows two cards in that order, Stamina's reading "Arrives in a later update".
- R95 · Session names: A session started on a Tuesday at 08:00 is "Tuesday morning", and one at 00:30 on a Wednesday "Wednesday night"; at 360 dp and font size 2.0 it shortens only if the full name does not fit.
- R96 · The resume card: With a session open, Start shows the resume card and no module card; "End/Save" at 09:40 on a session whose last set was at 08:31 leaves it STOPPED_EARLY, ended at 08:31; Start opened at 18:00 on a session whose last set was at 08:31 the same day closes it at 08:31 and shows the module cards; Start opened 3 h 1 min after a session with nothing logged began leaves no session; a session started at 23:30 with a set at 00:05 still shows the resume card at 00:15.
- R97 · Start's date line: On 2026-10-20 the line reads "Tuesday 20 October".
- R99 · Where exercises are made and managed: The add sheet has no edit action; More lists the exercises grouped by module in R19's order, then "Other", then "Archived", the latest used first in each, each opening its editor; an exercise with no module is listed under "Other".
- R100 · The add sheet: Tapping a row twice leaves every row's position unchanged; an exercise added before the sheet opened is not listed; opened from a Strength session, the sheet lists Strength's exercises at once, the last used first.
- R101 · Changing an exercise's type: Switching the type of an exercise with logged sets shows the question, and "Keep" leaves the type unchanged; an Isometric exercise offers no partial-reps switch.
- R102 · Editing an exercise: Saving "plank" beside "Plank" shows the refusal; an archived exercise moves to "Archived" with its sets kept in History, and "Bring it back" there returns it to its group; saving "Plank" while "Plank" is archived offers "Bring it back", and taking it lists the exercise again with its sets.
- R103 · Exercise editor fields: Plank can be made with 60 s rest, a note and the category "core", and the session shows its note; a new exercise's rest reads "Default".
- R104 · How the type is chosen in the editor: "New exercise" opens the type sheet before the form, and the form shows the picked type with "Change".
- R105 · How modules are picked in the editor: Picking and unpicking each module chip leaves every chip where it was, at 360 and 384 dp and font sizes 1.0 and 2.0.
- R106 · How categories are picked in the editor: Plank gets "core" through "+ Add", typing "co", ticking "core" and Done, and the form shows "core"; "Create cardio" makes the category and picks it.
- R107 · How optional fields show: At 384 dp and font size 1.0, every field of a new exercise's form starts within 46 dp of the first screen.
- R108 · How the default rest is picked: Tapping the row, then "60 s", closes the list and the row reads "Default rest · 60 s".
- R109 · How notes are written: A five-line note shows whole in the form, with no scrolling inside the box.
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
