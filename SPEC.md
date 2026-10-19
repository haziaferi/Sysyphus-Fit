# SisyphusFit — specification

Version 0.1, 2026-10-19. Offline-first Android app for organising and tracking near-daily training:
gym and strength, bodyweight, mobility and timers, offline cardio. Package `com.hazia.sisyphusfit`.

## 0. Owner decisions so far

| # | Decision | Date |
|---|---|---|
| 0.1 | Android only for now; keep a Windows build possible (no Android types in `:core`, Room KMP in `:data` with a desktop JVM target). | 2026-10-19 |
| 0.2 | Package name `com.hazia.sisyphusfit`. | 2026-10-19 |
| 0.3 | Data layer and skeleton first; screens later. | 2026-10-19 |
| 0.4 | Repository: github.com/haziaferi/Sysyphus-Fit (public). `research/` stays local: it holds phone screenshots and per-phone state. | 2026-10-19 |
| 0.5 | Scope: the 50-point scope of the feature harvest (§3), staged as recorded there. | 2026-10-19 |
| 0.6 | No account, no network at runtime (harvest row GM9: no INTERNET permission). | 2026-10-19 (scope) |

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
| `:core` | Kotlin Multiplatform (android, desktop JVM) | pure rules: ids, which day a session counts on, a day's total, progression | kotlinx-datetime only |
| `:data` | Kotlin Multiplatform with Room KMP | the database, DAOs, the backup format, the safe open | `:core`, Room, androidx.sqlite |
| `:app` | Android application | the Android entry points (empty until screens are decided) | `:data` |

A Windows build would add a `:windows` module beside `:app`; `:core` and `:data` already compile
and test on the desktop JVM, which is also where most tests run (fast, no emulator).

## 2. The floor (what every harvested mechanic assumes)

User-defined exercises; a session holding sets of reps / weight / time / distance; a saved routine
that can be started again; a history of sessions; one local database; no account; works offline.

## 3. Scope, staged (from `research/harvest-2026-10-19`, kept locally)

Each stage ships on its own. Packages ship whole.

1. LL2 last session's value beside each set · TR2 launcher shortcut per routine/timer · FT2 record /
   enter / import / repeat-last from one place · GM9 no INTERNET permission · CM3 weekday to-do list ·
   FA15 presence only, never a streak or missed-day count · GM2 last sets shown when picking an
   exercise · BC1 RPE / reps in reserve per set
2. KA6 Today: the week, what is due, the suggested next session, one start
3. FA9 goal progress in plain words, the amount done only
4. Backup package: KA9 one-file backup and restore · OS8 scheduled backup to a chosen folder
5. GR4 close a session with duration, intensity 1-5 and a comment · FA13 a session over midnight
   counts on its start day
6. Progression package: LL5 advance after a session where every set hit its target · GM6 show the
   next target while logging
7. Rest-timer package: GL2 foreground-service countdown with notification · LL4 start on set
   completion · KA5 -10 s / +10 s / skip
8. Intervals package: TX1 prepare / work / rest / rounds / sets from presets · TR5 +1 min and step
   back / forward · FA5 work resumes after a rest · FA1 repeat a whole run with one tap
9. SH1 start intervals from one cycles stepper · KA2 an early stop is reviewed; unfinished sets are
   skipped, not logged
10. GPS package: OT1 GPS track recorded offline in a foreground service, saved as GPX · FA4 stop on a
    held press, Undo for 10 s · OT5 every finished track exported to a chosen folder · FT5 "waiting for
    GPS - start anyway?"

Standing rule from the harvest: before building a row, read it against the code; its effort and
frequency were estimated before any code existed.

## 4. Data model, schema version 1

Designed for the whole scope now, so that no package needs a migration of logged data. Units are
integers: time in epoch milliseconds (UTC) plus the zone it was recorded in, durations in seconds,
weight in grams, distance in metres, coordinates in 1e-7 degrees.

Ids are text ULIDs (`:core` `Ids`), so a restored or imported row never collides with a local one
and a later Windows copy can merge. Every table has `id TEXT PRIMARY KEY`.

| Table | Columns (beyond id) | Serves |
|---|---|---|
| `exercise` | name, kind (DYNAMIC, ISOMETRIC, TIMED, DISTANCE), sided (0/1, counted per side), archived | floor; CM2 |
| `routine` | name, weekdays (bit 0 = Monday … bit 6 = Sunday; 0 = unscheduled), position | floor; CM3, KA6 |
| `routine_item` | routine_id, exercise_id, position, target_sets, target_reps, target_weight_g, target_seconds, target_distance_m, rest_seconds | floor; LO4-style rest, GM6 |
| `session` | routine_id?, activity (free type, e.g. "Walk"), started_at?, ended_at?, zone, day (local date it counts on), state (OPEN, FINISHED, STOPPED_EARLY), intensity? (1-5), note? | floor; FT2, GR4, FA13, KA2, GL1-style resume (OPEN = in progress) |
| `logged_set` | session_id, exercise_id, position, side? (L, R), reps?, weight_g?, seconds?, distance_m?, rpe? (×10), rir?, done_at | floor; LL2, GM2, BC1 |
| `progression_rule` | routine_item_id, add_weight_g, add_reps, when (ALL_SETS_HIT) | LL5, GM6 |
| `interval_preset` | name, prepare_s, work_s, rest_s, rounds, sets, set_rest_s, last_used_at? | TX1, FA1, SH1 |
| `track_point` | session_id, t, lat_e7, lon_e7, alt_dm?, accuracy_m?, segment | OT1, OT5 |
| `goal` | exercise_id? (null = all training), period (DAY, WEEK, MONTH), target, unit (SESSIONS, MINUTES, METRES, SETS) | FA9 |
| `setting` | key, value | OS8 (backup folder, schedule) and later preferences |

Rules the schema carries:
- **A session without times is an amount** (FA10's shape): `started_at` and `ended_at` null, `day`
  set. A day's total time is the union of its timed sessions plus the sum of its amounts
  (`:core` `DayTotals`).
- **`day` is the local date of `started_at` in `zone`** (FA13), computed once by `:core` `CountingDay`
  and stored, so a later change of the phone's zone does not move history.
- **No streak is stored or derived** (FA15). Nothing in the schema counts missed days.
- `logged_set` rows exist only for sets that were done; an early stop keeps no skipped rows (KA2).

## 5. Safety and gates

- **The database never deletes itself.** Android opens through `KeepFileOnCorruptionDriver` (the
  stock driver's default handler deletes a corrupt file); `openOrRecover` sets an unopenable file
  aside as `<name>.unopenable-<stamp>` and opens a fresh one. Proven by a Robolectric test that
  writes garbage and finds the bytes set aside, with the stock driver as the failing control.
- **Backup is the first feature** (KA9): one JSON file, every table, `format`
  `sisyphusfit-backup`, `version` 1. Restore replaces everything in one transaction. The gate is a
  round trip: export, restore into an empty database, export again, byte-identical.
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
