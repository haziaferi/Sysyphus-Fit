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
| 0.35 | Main action on doing screens: a full-width bar at the bottom (scored best on the owner's five button axes: same place and easy reach, mis-tap risk, content covered, same place everywhere, label visible; 0.973 vs floating 0.942 vs bar with a secondary 0.821). While something is running (e.g. a timer), a floating button appears for it (e.g. stop); it never sits beside the main bar and stopping keeps the held press (FA4). The running control is refined by 0.41. | 2026-10-21 (polish round) |
| 0.36 | Typeface: IBM Plex Sans (variable, `IBMPlexSans[wdth,wght].ttf`, SIL OFL 1.1) for all text and numbers, bundled in the app (offline). Set numbers use its tabular figures (checked: equal widths). Seven faces scored within 0.002 of each other, so the owner chose by eye. "Plex" is a Reserved Font Name: a subset or modified copy must be renamed. Replaced by Sisifo (0.40). | 2026-10-21 (polish round) |
| 0.37 | Icons: our own set of 28, drawn in VectorCraft on a 24 grid with a 2 dp stroke, round caps and no fill. Module icons: one shoe print (Daily movement), a pocket tape case with its tail and tab (Body measures), a figure on one leg with the other reaching back (Mobility & balance, clear of Android's Accessibility figure), a heart with a pulse line (Stamina), a dumbbell (Strength). Backup is a floppy disk; restore is the floppy inside an arrow turning back. The session clock is a wristwatch and the Start tab a play sign in a rounded square, so neither can be taken for the circled "i" at 24 px. The object icons are faceted from 0.53; the path icons and −/+ keep round caps. Holds and partial reps are shown as text, not markers. Chosen by the owner after scoring variants on legibility at 1x and distinctness (research/screens-2026-10-20/polish/icons/review-2026-10-21). | 2026-10-21 (icon review) |
| 0.38 | Number entry: tapping a number opens the phone's own numeric keyboard; the app draws no keypad of its own. The −/+ steps and the two-decimal weight of 0.28 stay. | 2026-10-22 (final stage 1a mockups) |
| 0.39 | Language: the app's text is written in English first, and the mockups are in English. Italian comes later as a translation pass [deferred]; numbers then follow the phone's locale (57.5 / 57,5). The evidence pack's Italian pass is deferred with it. | 2026-10-22 (final stage 1a mockups) |
| 0.40 | Typeface: Sisifo (github.com/haziaferi/sisifo, SIL OFL 1.1), carved from Ortica Linear for this app, replaces IBM Plex Sans (0.36). Two variable files are bundled (offline): `Sisifo[CARV,wght].ttf` and its 12° oblique `Sisifo-Italic[CARV,wght].ttf`, with weight 300-700 and Carve 0-100. Carve follows text size, stepped: 0 up to 16 dp, 100 above (type tokens, §10). Numbers that change in place use tabular figures (`tnum`); titles set in capitals may use the inscriptional capitals (`ss01`); the italic is for emphasis in text, notes and hints, and display accents. Blind panel against the font search's finalists on ten axes: Sisifo 0.880, next Libertinus Sans 0.739, IBM Plex Sans 0.531. The stepped and ramped carve mappings tied (0.885 / 0.873) and the owner chose stepped. The app takes Sisifo's released files and does not build them. | 2026-10-23 (Sisifo stage 6 sign-off) |
| 0.41 | Running control on the exercise screen (sets and holds): a hold starts from the main bar ("Start · set N"), and Done (the main bar) ends it and records its time, as it records a strength set, so a hold has no control of its own; Done also starts the rest (0.42). On this screen the only running control is the rest: round, the remaining time inside its ring, at the same height on every exercise screen of a density, in the band between the comparison card and the steppers, so nothing on the screen moves when it appears (checked within 0.5 dp). A tap opens −30 s / +30 s; a held press skips. After Done on a hold, "Hold saved · Undo" shows for 10 s, so a stray Done can resume the hold (FA4's undo). Chosen by the owner over a pill, an inline panel and a sheet from the bar (the sheet lay over live steppers and failed G1). Recordings and the interval timer (stages 1b, 4 and 10) keep 0.35's floating stop with the held press. The round's form is refined by 0.44-0.46. | 2026-10-23 (stage 1a mockups in Sisifo) |
| 0.42 | Rest timer (stage 6, as §3 stages it; the hold timer stays in stage 2): it starts on Done (LL4) and counts down the rest length from the plan (`routine_item.rest_seconds`, LO4), else the exercise's own, set ahead in Edit exercise, else one default in Settings (its value is [open]). It moves by −30 s / +30 s (not KA5's 10) or is skipped, and runs on with a live notification (GL2). How it ends is the person's choice in Settings: vibration, sound, notification, alone or together. The exercise's own length needs one column when stage 6 is built (`exercise.rest_seconds`, nullable); the default and the end signal are `setting` keys. | 2026-10-23 (stage 1a mockups in Sisifo) |
| 0.43 | Session clock: kept on every session and exercise screen (option C of three), in the header row, left of Finish. Scored with consistency first: it sits at the same place and size on all six session and exercise screens. Under the title it was 2 dp off on the session screens; as first drawn it moved by 76 dp. Under Finish tied with the header row (same consistency, mean within 0.001) but took Finish's hit box down to 42 dp; the owner chose the header row. | 2026-10-23 (stage 1a mockups in Sisifo) |
| 0.44 | The rest round is the boulder, and Sisyphus pushes it. During a rest a small figure pushes the round from its left, up an implied 20° slope (no ground is drawn), at one step per second; the round stays where 0.41 puts it, centred, and does not move round the page. Its surface turns as he pushes, its rim keeping pace with his feet (one turn about every 23 s), and his hands follow its outline. He never reaches a top and the stone is never shown rolling back. The round and the figure appear only during a rest, a small thing to look forward to: at 0:00, or on a skip, the rest ends and both leave the screen; there is no figure during a set. −30 s / +30 s (0.41) redraws the ring as its share of the new length, and he keeps his pace. A screen reader skips the figure and the turning and reads the round's own label (0.41). Inside the round a white ring shows the rest so far, from the top, clockwise; the time stays upright in the middle; the ring and the time are the only white. With reduced motion nothing turns, he holds still, and the ring and the time move once a second. The figure is a side-view silhouette with adult proportions (about 7½ heads; about 37 dp standing, 17 dp bent into the push); his push follows heavy sled-push data (knee bent about 52-69° through the stance, steps of 46 cm; Martínez-Serrano 2021, Osterwald 2021), the low trunk following Muybridge's plate 393 and the owner's own sketch; his near leg is a darker grey, #4a4a4a, so the legs stay separable where they cross. Blind panel: the pushed round 0.798, a figure on a track round it 0.694-0.701, the plain round 0.673 (last). Set aside: the track versions (a summit to reach, a stone rolling back), a stone at the ring's head (not seen at true size), a centred pair (the round stays centred). | 2026-10-24 to 27 (rest-round mockups) |
| 0.45 | The boulder's form, for now: the owner's own outline (rocc1, first cleaning, its bumps kept), 80 dp across, overhanging the 72 dp control by 4 dp; the touch target stays 72 dp. Nothing on its face. Chosen in motion, with the figure walking: its many dents and peaks make the turn read faster than a smoother rim. Stills had put a reworked smooth rim ahead (0.844 against 0.650), but the owner judges turning only in motion; marks on the face read busy and somewhat cartoonish in the page. | 2026-10-26 (boulder panels) |
| 0.46 | What the figure and the stone share: the white ring's track, before it fills, is the near leg's #4a4a4a. Set aside: a shaded side on the stone (a still light on a turning stone counters its motion), a carved edge light, an outline, a grain. | 2026-10-26 |
| 0.47 | The exercise screen keeps one centre line. Screens fill the phone's width, 360-412 dp; wider (large phones, foldables, landscape), the screen keeps a 412 dp column, centred, so the centre line stays the screen's centre. The rest round, the set values and the comparison card's middle gap sit on the screen's centre: each value (weight, reps, partials) is centred, its unit hanging to its right. The comparison card is a table mirrored round its middle gap: "Set N" at the left edge, last time at its column's left, today at its column's right, and the partial note ("+ 1 ½") at the right edge, each 16 dp in from the card's edge. The outer columns share one reserved width, so nothing moves between screens; with no partial (a plank, or none that day) the slot stays empty. In the card "Set N" and the column heads are 14 dp and the values 16 dp; the evidence-note chip ("Training frequency") is 11 dp in the secondary grey between two carved lines (0.53), below them in the hierarchy (§10 rule 4), and keeps its 48 dp touch target. | 2026-10-26 to 27 (rest-screen mockups) |
| 0.48 | Fit is judged on the phone's own app area, not in a browser (whose bars bias it). On the OnePlus 6T an app gets 384 × 755.6 dp (status bar 80 px, three-button navigation 135 px, at 450 dpi); the rest screen, the exercise screen and the hold rest fit with no overlaps, the main bar ending at the navigation bar. | 2026-10-27 |
| 0.49 | Font size: the type tokens (§10) are in sp, their line heights too, so text follows the phone's font size; from Android 14 that reaches 200% on Android's nonlinear curve (AOSP FontScaleConverterFactory: at 2.0, 11 → 22, 14 → 26, 16 → 28, 19 → 32, 23 → 35.5, 28 → 37.3, 40 → 46.9 dp). Padding, gaps, icons and touch targets stay in dp, and no text has a fixed height (the card's rows, the chip and the main bar take their sizes as minimums). Carve follows the role's token, not the rendered size: a role keeps its carve at every font size. What gives way when text does not fit: the set value shrinks to fit between its steppers, never below the compact token (set number 34, partials 20), and then its unit moves under it, centred; the header's title may end in "…", its full text on a tap, while the session clock and Finish never shorten; the main bar may wrap to two lines; when the screen no longer fits, the body scrolls under the fixed main bar, and the rest round's band moves down with the card at that font size (0.41's fixed height holds within one font size). Gated at 1.0, 1.3, 1.5 and 2.0 with long real values ("102.5 × 12", 142.25 kg, a 10:00 rest, "Single-leg Romanian deadlift", "Wednesday afternoon", 1:12:40) by G1-G5 (§10 rule 6). Sources: Android 14's notes on nonlinear font scaling (text and line heights in sp, padding and heights in dp, test at 200%); WCAG 2.2 SC 1.4.4 (resize to 200% without loss of content or function; truncation acceptable when the full content is available on activation) and WCAG2ICT ("up to the text sizing capabilities of the platform"). | 2026-10-27 (font-scale mockups) |
| 0.50 | The comparison card at larger font sizes reflows, keeping its text at full size, in steps taken only when the step before no longer fits: (1) Mirror B as 0.47, its outer columns as wide as the longer of "Set N" and the note at that size; (2) the column heads on a line each, last time's date over its column, "Today" over today's; (3) per set, "Set N" and the partial note on one line (left and right edges) above the pair of values, last time at the left, today at the right; (4) each value on its own line, last time left-aligned, today right-aligned. Chosen over capping the card's text at 1.3 (0.766 against 0.760, within the plateau, but its values stopped at 20 dp where 28 were asked, and its heads touched), over shrinking it to keep the mirror (0.698) and over last time behind a tap (0.585). Owner's axes, equal weights: text grows as asked and hierarchy kept (measured), layout holds and reads at true size (blind panel of three, on the 6T's own pixels). | 2026-10-27 (font-scale mockups) |
| 0.51 | The rest round grows with its time: the boulder, the white ring and the figure scale together by the time's font scale (at 2.0, 19 → 32 dp, 1.68×: the boulder about 135 dp across, the touch target with it), so the time keeps its asked size inside the ring; a time still wider than the ring at that size ("10:00") steps down until it fits. The round stays on the centre line and the figure keeps his place at its left; at large font sizes the round may sit below the first screenful and scrolls with the body. Chosen over the time shrinking inside an unchanged 80 dp boulder (0.760 against 0.762; the time stopped at 24 dp where 32 were asked), over a time fixed in dp (0.749) and over the time beside the round (0.708). | 2026-10-27 (font-scale mockups) |
| 0.52 | Compact density (0.34) on the rest screen: the rest round, the figure and the comparison card keep their balanced sizes, so they are the same objects in both densities; only the screen around them is denser. At the normal font size the boulder stays 80 dp over the 72 dp control (0.45) and the figure about 5 dp per head (about 37 dp standing, 0.44), turning at the same pace; at larger font sizes both grow as 0.51. The round sits on the centre of the band between the card and the steppers, as at balanced (0.41), measured on the rest screen and kept on every exercise screen of the density; compact's band is taller (on the 6T the round's top is 378 dp down the app area on both rest screens). The card keeps 0.47's balanced text: "Set N" and the column heads 14 sp, the values 16 sp on 24 sp rows, the evidence-note chip 11 sp, gaps 8 dp, outer columns 53 dp. The time inside the round follows the row-title token (19 / 17 sp). Blind panels on the 6T's app area at its own pixels (owner's axes: actual size .3, harmony .25, hierarchy .25, same screen as balanced .2, compact .2, figure .2, digits .15, seven measured axes .1): size and height, balanced sizes on the band centre 0.894 against 0.770 for the next (scaled stone with the figure kept; balanced sizes under the card), 0.750 for scaled 0.89 on the centre, 0.586 for 0.78, the plain-round control 0.533 (last); the band centre beat "under the card" at every size. Card: balanced kept 0.783 (first on actual size and same screen, last on compact), tokens + 1 0.758, plain tokens 0.738, control 0.531 (last). The measured axes were a plateau (0.752-0.755). With 0.50-0.51 and 0.53 the compact exercise screens pass G1-G5 at 360-412 dp and font sizes 1.0-2.0. | 2026-10-27 (compact rest screen) |
| 0.53 | Visual language: the app's elements take the stone's idea from Sisifo's carving: surfaces cut on one side and worn on the other, worn pebble shapes for things you press, and lines carved into the surface. Still greyscale (§10 rule 7); every dark fill of the interface is one grey, #2b2b2b (text keeps #1c1c1c, secondary text #575757; the figure's near leg and the ring's track keep #4a4a4a, 0.46; the boulder and the figure keep #1e1e1e, as drawn and judged). **Main bar:** square corners; its top edge sinks 8 dp across the middle third in one soft dip, lit faintly inside the dip. **Secondary buttons** (pills): worn pebbles (each corner a slightly different radius) standing on a 2 dp ground shadow. **Steppers (−/+):** borderless light pebbles (#fafafa), − and + with different outlines, on a 1 dp shadow; the number you tap to type is underlined 1.5 dp in #6b6b6b. **Module cards, week sections, the resume card and rows:** cut on the left (square), worn on the right (8 dp corners), #f0f0f0 lit softly toward the right. **Comparison card:** 4 dp corners and no vein; its column heads sit on a carved line (3 dp: dark over light); the current set is circled by a 1.5 dp pebble outline. **History's date dividers:** a carved 5 dp spindle that fades out over its first and last fifth. **Icons:** the object icons are faceted (each curve drawn as two straight cuts, square ends and mitred corners) with a 0.7 dp light below them; the path icons (navigation, chevrons, play, stop, timer, tabs) and −/+ keep 0.37's round caps. **Tags:** plain secondary text joined by a middle dot, no box: they sit lowest (§10 rule 4). **Evidence-note chip:** no outline; a carved 3 dp line above and below it, otherwise as 0.47. **Tab bar:** no top hairline; its top line (1.5 dp, #2b2b2b) dips under the current tab into a valley whose floor runs 3 dp above the screen's bottom edge, a 4 dp rounded underline on that floor; the current tab's label stays where it is. The valley is 172 dp wide at the top and 60 dp at the floor at the normal font size; at the first tab its outer wall runs off the screen. **At larger font sizes** the tab bar fits its labels: the floor is as wide as the current label, never under 60 dp, and runs 7 dp below it; the walls reach 32 dp across (56 dp gave the same at 1.0 but ran over the next label at 1.5 and 2.0). The bar grows with its labels, 10 dp above the icon and below the label, so it is 72 dp at 1.0, about 77 at 1.3, 80 at 1.5, and 87 to 120 at 2.0. A label wraps to two lines ("This / week") when on one line it would come within 4 dp of its tab's edges, or when any tab's valley would come within 2 dp of an icon or a label; the rule is the same for every tab, so the bar keeps one height when the tab changes. This happens at 2.0 on 360 and 384 dp screens, not at 412. Measured on the four tab screens at both densities, 360, 384 and 412 dp and font sizes 1.0 to 2.0 (96 pages): no label or icon within 5.9 dp of the line; checked on the 6T. When the tab changes, the valley and its underline slide along the line to the new tab in 250 ms, widening or narrowing to the new label on the way, easing to a stop (cubic-bezier(0.2, 0, 0, 1)); the screen itself changes at once; with reduced motion the valley jumps (owner's pick on the 6T from slide, fill and sink, slide with the underline behind, none). Scored in three stages from the owner's per-element picks (69 designs, gates G1-G4 pass on all 20 screens): 0.785 on the eleven axes of `results_vl/axes2.json`, next the same with relief rows 0.783 and with the hill tab bar 0.773; the valley drawn as a sunken surface 0.74; the current design and stock Material 0.592 (last, tied). Judged blind on 3× renders, then checked on the 6T at true size: clean. Set aside: the vein on the comparison card, from its corner (passes a few dp from "TODAY") or as a crack down its middle gap (within 6 dp of text on 36 of 144 pages, the exercise screen at normal size among them); the hill tab bar (the current tab on a crest), the sunken-surface valley (the current tab read too quietly), relief rows. At compact the forms are the same, in the same dp (G1-G5 pass on the exercise screens at 360-412 dp and font sizes 1.0-2.0; 0.53 adds no failure on the 20 compact screens). The tab screens at larger font sizes: 0.54. | 2026-10-27/28 (visual-language rounds) |
| 0.54 | The tab screens (Start, This week, History, More) at larger font sizes follow 0.49: no text has a fixed height (rows, buttons, pills and their hit boxes take their sizes as minimums), the main bar wraps, and the body scrolls between the header and the fixed bottom. The tab bar stays fixed while the body scrolls (0.53's fitted bar; owner's pick on the 6T over hiding it while scrolling down). When any module card's or history card's name would wrap beside its pill, every card on that screen puts its pill under its text, right-aligned, and the name takes the card's width (one form per screen, §10 rule 3). "Log afterwards" stays the last item of Start's list and scrolls with it. Gated on the four tab screens at both densities, 360, 384 and 412 dp, font sizes 1.0-2.0 and long names ("Start Wednesday afternoon", "Single-leg Romanian deadlift · Plank · Walk"): G1-G5 pass on all 144 pages, where the tab pages as they were failed G1, G2 and G4 from 1.3. Owner's axes, equal weights: text grows as asked and hierarchy kept (measured: 1.0 for every option), layout holds, reads at true size and harmony with 0.53 (blind panel of three on the 6T's own pixels): the pill under, "Log afterwards" in the list 0.864; the pill under with "Log afterwards" fixed above the main bar 0.823; the whole card as the target, with a chevron, 0.823 (best layout, lowest harmony); the pill beside, as now, 0.772; the tab pages as they were 0.419 (last). | 2026-10-28 (tab screens at larger font sizes) |
| 0.55 | Design tokens: `design/tokens.json` is the one source of the app's visual values, in two layers: raw values (a grey ramp named by CIE L*, black at four alphas) and the roles screens use (colour: surface, fill, ink, line, shadow, rest; space, size, radius, stroke, elevation, motion, type). A screen never uses a raw value. Greyscale until the palette is discussed (§10 rule 7): colour later changes only the roles' references. `tools/tokens.py` checks the file (every reference resolves, every raw value is used by a role, density pairs, SPEC §10's tables match it; CI runs it) and writes SPEC §10's tables and the mockups' CSS from it; how it reaches Compose is decided at the foundation step. Taken from the decided screens (0.47-0.54) as drawn: rebuilt from the file, all 40 (20 per density) matched them pixel for pixel. Then the owner merged #6a6a6a into #6b6b6b (the carved lines' cut and the edge grey, 0.4 L* apart), drew History's date spindle in the edge grey instead of a grey of its own, and dropped the note's facts hairline; only those change (the carved lines by one grey level). Compact's list gap stays 8 dp, as drawn and judged (the base compact scale had 4). Kept apart though close: text #1c1c1c and the stone #1e1e1e, secondary text #575757 and the main bar's dip light #5a5a5a, the page #ebebeb and worn cards #f0f0f0, cards #fafafa and white. Contrast (WCAG 2.2, every text and icon on every surface it sits on, both densities): all pass, the lowest secondary text on the page at 6.1:1 (text needs 4.5:1, icons 3:1). | 2026-10-28 (design tokens) |
| 0.56 | Compose foundation (stage 1a): the app's pieces are built on Compose Foundation only, no Material, in `app/.../ui/stone` with the theme in `ui/theme`. The tokens reach Compose as `Tokens.kt`, written by `tools/tokens.py kotlin --write` from `design/tokens.json` and committed; the check (and CI) fails when it differs. Sisifo is bundled from release v1.0 of github.com/haziaferi/sisifo (the signed-off 0.7 at version 1.000; only the version fields changed, checked table by table), checked by hash: upright sha256 `6abb50cdd398d256a6d34eef6f3e697411c7212e51b9f8f51a523d7be3b53ad2`, oblique `89f60b26ea58847d4a67846405974c0b8be3277580b79b934132cf91a694882d`; its OFL ships in the app's assets. Text is set in CSS line boxes (lines x the role's line height, glyphs overflowing), as the mockups were drawn and judged: Compose otherwise keeps a font's full height where it is taller than the line (the 40 sp set number measured 54 dp in its 44 line). Proofs: unit tests (theme, density, the variable axes, Android 14's font curve at 2.0, CSS corner radii, the valley's geometry and motion, touch targets of at least 42 dp, each piece's font-size behaviour of 0.49); eight Roborazzi goldens (balanced and compact at 1.0, 1.3 and 2.0, and the 6T's app area with and without the sheet) that tests verify; and the pieces' sizes against the decided mockups, all 15 within 1 dp (the check found the chip at the older 32 dp; it is 0.47's 28). A debug-only gallery shows every piece with density and font-size switches; it is installed on the 6T for the owner's walk. Owner: the band behind Android's three navigation buttons takes the app's own colour, as other apps do: the bar is transparent with dark buttons and Android's contrast scrim is off, so the tab bar's surface (or a doing screen's bottom zone) runs under it (`stoneEdgeToEdge()`). | 2026-10-28 (Compose foundation) |
| 0.57 | Evidence note sheet spacing (owner, on the 6T walk of the 0.56 gallery: the note read too dense). Of three spacings tried on the phone (as drawn: 8 dp under the eyebrow, the claim on 24 sp lines; more: 12 dp, 28 sp; most: 16 dp, 30 sp) the owner chose more: `space.note-head` 12 dp under the note's eyebrow, and the claim in `type.note-claim`, the row title's 19 sp on 28 sp lines. At compact the gap stays 12 dp and the claim keeps the compact row title's 24 sp line, already about that proportion at 17 sp (1.41 against 1.47). | 2026-10-28 (6T walk) |
| 0.58 | App shell and shared data core (stage 1a). **Navigation:** Navigation-compose 2.10.2, one `NavHost` with a nested graph per tab and the documented multiple-back-stacks pattern (each tab keeps its place; Back from another tab's root returns to Start, from Start leaves the app); the app opens on Start (0.25); the screen changes at once, only the valley slides (0.53). **Wiring:** by hand, one `AppContainer` on the `Application`, a ViewModel per screen with its own factory; the database opens once, on first use, never on the main thread. Both chosen by measured spikes (`research/spikes-2026-10-28/`, every option passing the same behaviour tests and lint) on the owner's axes, smooth path for the person and the build ½, familiarity ¼, least code ¼: navigation-compose 0.906, Navigation 3 0.713, an own navigator 0.625; by hand 0.875, Hilt 0.689, Koin 0.640; equal weights keep both orders. Navigation-compose is in maintenance mode (critical fixes only); Navigation 3 is where Google is going. **Shared data core** in `:data`, no schema change: settings, with the modules chosen recorded only when the person chooses (none recorded means not chosen yet, 0.18; an empty choice is a choice); exercises with their module tags, replaced whole; sessions and sets, one session open at a time, counted on the local day it started in its zone, resumable after the app is killed (0.27); names a newer build may write are skipped, not fatal (an unknown module drops from its exercise, an unknown kind leaves that exercise out of the list, its sets kept). A database that cannot be opened is a state the screen shows, not a crash; a first open left half-way (the person backs out) is kept, not repeated. Each tab's root shows its name until its screen is built. Proofs: 40 new tests (22 in `:data` on the desktop, 18 in `:app` under Robolectric: tabs, Back, rotation and process death through a parcelled bundle, the open off the main thread), each restore and main-thread test turned red by a planted mutant; a fresh review of the whole branch (3 findings fixed, each test first); `./gradlew --offline check` 150 tests, 0 failed. | 2026-10-28 (app shell) |
| 0.59 | Starting a session while one is open resumes the open one instead of failing (a double tap, or a start racing resume): `SessionStore.start` returns the open session's id and adds nothing, the same session resume (0.27) would show. Owner's call on the 0.58 review's deferred minors: a database rule allowing only one open session (a partial unique index, schema 3) goes in the backup plan, where a restore that brings two is settled; the process-death test's last Back and `ExerciseStore.update`'s read inside its transaction fold into the next screen plans; keeping module names a newer build wrote goes in the module-choice plan; accent-aware sorting (a `Collator`) comes with Italian. Proof: `startingWhileOneIsOpenResumesIt` red on the old refusal, then green. | 2026-10-29 (0.58 minors) |
| 0.60 | First-launch module choice (stage 1a, 0.18). Before the tabs, with no tab bar, the app asks once: **Start your journey** — "Choose which paths you want to follow. The other choices will still be available in Settings." — and the bar **Step forward**. Owner's calls: all five start unticked (0.18 over §8's "Body measures unticked"); stepping forward with none ticked is a choice; all five are offered, the four not built yet marked "Arrives in a later update"; a tap anywhere on a card ticks it. Module lines (owner's wording): Daily movement "Creating mindful interludes to get up and break stillness"; Strength "Working out with weights and planks"; Stamina "Tracking cardio at every pace"; Mobility and balance "Training range of motion and stability"; Body measures "Tracking personal progress through body measurements and/or weight". Each card has an "info" chip (carved lines, `type.chip`) that opens a small raised card in place, "Includes: …" in the owner's wording; Back or a tap elsewhere closes it. The circled "i" stays the evidence notes' (0.16). Daily movement is movement spread through the day, Stamina sessions set out to do. Wording rules (owner): "no judgement" is how the app is built, not something it says; mechanics a module has anyway (routines) are not named. Picked on the 6T from variants: the "info" chip over ✦ and ⟐ ("info is more explanatory"), the module icon before the name over after it. At large font sizes the cards follow 0.54's reflow (chip and box under the text, right-aligned, on every card once one name would wrap). Text line boxes follow the text at large font sizes (Compose keeps line / size; the box had scaled the line on Android's curve alone, overlapping wrapped lines at 1.3 and 2.0); the gallery's 1.3 and 2.0 goldens moved by line spacing only; the owner kept this proportional spacing (body at 2.0: 42 dp, against 36 on the curve). The choice and the tab bodies keep clear of three-button navigation and cutouts at the sides (landscape). With TalkBack, the open card is announced and the list behind it waits until it closes. While the choice is read, only the page is drawn; a read that fails opens the tabs, where Start's failed state (0.58) shows it. Done with it: names a newer build wrote are kept when the choice or an exercise's tags are rewritten (0.59's M4); the process-death test ends out of the app (M1). Proofs: `./gradlew --offline check` 183 tests, 0 failed; a fresh review of the whole branch (2 findings fixed, each test first); goldens `module-choice-{balanced,compact}-{100,200}`; mutants on Back closing the card (caught) and on where the navigation controller is created (not needed: saved state waits until first read). | 2026-10-29 (module choice) |

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
| `:app` | Android application | the Android entry points: `MainActivity`, the shell (0.58), the screens and their ViewModels | `:data` |

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
6. Rest-timer package (GL2, LL4, KA5), as specified in 0.41-0.42, its round drawn as 0.44-0.46, sized by 0.51-0.52.
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
   the screen, G3 touch targets at least 5 dp apart, G4 touch areas at least 42 dp, G5 no lost text: nothing
   cut, clipped, covered by the rest round, the figure or the main bar, or crossing other text, at font scales
   1.0, 1.3, 1.5 and 2.0 with long real values (0.49); a title's "…" with its full text on a tap is allowed, and
   text below the fold of a scrolling body counts as kept. Then the owner's
   axes: harmony, density, visual and functional hierarchy, reach and legibility, taps per set,
   at-a-glance clarity, consistency (equal weights; a plateau is shown as a plateau).
7. Greyscale until the palette is discussed.
8. **[decided 0.40]** Type follows the tokens below: every text element takes one role. Sizes are in sp (0.49),
   balanced / compact (0.34), from the polish round's type scale. Carve is stepped by the token's size, not the
   rendered size: 0 up to 16, 100 above (0.49).

| Role | Used for (stage 1a mockups) | Size sp | Line height sp | Weight | Carve | Features |
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
**[decided 0.47]** In the comparison card, label ("Set N") and eyebrow (the column heads) are 14 sp and the values body (16 sp); the
evidence-note chip is 11 sp, line height 16 sp, in the secondary grey, on every screen it appears (rule 3). At compact they keep these
sizes (0.52). At larger font sizes the card reflows (0.50).

9. **[decided 0.55]** Design tokens: every visual value comes from `design/tokens.json` through a role; a screen
   never uses a raw value. The tables below are written from the file (`python tools/tokens.py md --write`) and
   checked against it, as is the type table above.

<!-- tokens:begin (tools/tokens.py md --write) -->
Colour roles (screens use these; raw greys are named by CIE L*):

| Role | Raw | Value | Used for |
|---|---|---|---|
| surface.page | grey.93 | `#ebebeb` | the screen behind everything |
| surface.card | grey.98 | `#fafafa` | comparison card, facts, light pebbles (steppers, the rest round's control), the tab bar |
| surface.worn | grey.94 | `#f0f0f0` | module cards, week sections, the resume card and rows: cut left, worn right (0.53) |
| surface.worn-light | grey.100 | `#ffffff` | the soft light toward their worn right side |
| surface.sunk | grey.88 | `#dfdfdf` | the bottom zone behind the main bar; secondary pills |
| fill.dark | grey.17 | `#2b2b2b` | every dark fill of the interface: main bar, tab bar line and underline, selected states (0.53) |
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
| rest.stone | grey.11 | `#1e1e1e` | the boulder and the figure (0.44, 0.45) |
| rest.near-leg | grey.31 | `#4a4a4a` | the figure's near leg (0.44) |
| rest.ring-track | rest.near-leg | `#4a4a4a` | the white ring's track before it fills: the near leg's grey (0.46) |
| rest.ring | grey.100 | `#ffffff` | the ring and the time are the only white in the round (0.44) |

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
| size.row | 56 / 48 | minimum height of a row (0.49: text may make it taller) |
| size.card | 72 / 56 | minimum height of a module card |
| size.button | 56 / 48 | minimum height of the main bar and buttons |
| size.hit | 48 / 44 | minimum touch target (G4 asks 42) |
| size.stepper | 56 / 48 | the − / + pebbles beside a set value |
| size.stepper-small | 48 / 44 | the smaller − / + pebbles (partials) |
| size.float | 64 / 56 | height of the floating running control as a pill (0.35) |
| size.float-round | 72 / 64 | the floating running control as a round (0.35: recordings, the interval timer) |
| size.rest-control | 72 / 72 | the rest round's touch target (0.45); the boulder is 80 at font size 1.0 and grows with the time (0.51) |
| size.tab-bar | 72 / 72 | the tab bar at font size 1.0; it grows with its labels (0.53) |

Shape, line, elevation and motion (the same at both densities):

| Token | Value | Used for |
|---|---|---|
| radius.worn | 0 8 8 0 | cut on the left, worn on the right (0.53) |
| radius.card | 14 dp | facts, segmented choices, label chips |
| radius.comparison | 4 dp | the comparison card (0.53) |
| radius.box | 8 dp | a checkbox |
| radius.sheet | 22 dp | a sheet's top corners |
| radius.full | 999 dp | round ends: the grab handle, close and note chips |
| radius.pebble-pill | 52% 48% 50% 46% / 50% 54% 46% 50% | secondary buttons (0.53) |
| radius.pebble-a | 49% 51% 56% 44% / 52% 46% 54% 48% | light pebbles: the reps steppers, the rest round's control, the current set's outline |
| radius.pebble-b | 55% 45% 52% 48% / 46% 52% 48% 54% | light pebbles: the weight and partials steppers |
| stroke.hair | 1 dp | unselected choices' outlines |
| stroke.selected | 2 dp | a selected box, segment or chip |
| stroke.line | 1.5 dp | the tab bar's line, the tap-to-type underline, the current set's pebble outline |
| stroke.icon | 2 dp | icons on the 24 grid (0.37) |
| stroke.carved | 3 dp | a carved line, dark over light (0.53) |
| stroke.underline | 4 dp | the current tab's underline on the valley floor (0.53) |
| stroke.ring | 2.6 dp | the rest round's white ring and its track |
| elevation.ground | 0 2 0 0 #c4c4c4 | a pill standing on the ground |
| elevation.step | 0 1 0 0 #0000002e | a light pebble |
| elevation.edge-inset | inset 0 0 0 1 #6b6b6b | a flat row's outline |
| elevation.float | 0 6 16 0 #00000033, 0 1 4 0 #00000024 | a sheet |
| motion.valley | duration 250 ms, easing cubic-bezier(0.2, 0, 0, 1) | the tab valley sliding to the new tab (0.53) |
| motion.step | duration 1000 ms | one push step of the figure (0.44) |
| motion.stone-turn | duration 23000 ms | about one turn of the stone (0.44) |
| motion.reduced | nothing turns or slides; the figure holds still; the ring and the time move once a second; the valley jumps (0.44, 0.53) |  |
<!-- tokens:end -->

## 11. Decided, deferred, and the workshop agenda

**Decided 2026-10-20:** 0.7-0.27 above (0.16-0.27 in the workshop: agenda items 1-5 done).

**Deferred by the owner [open]:** the shape of Today (by purpose with a Start sheet, or a card per
module); where suggestions stop (record and comment only / one editable "next time" proposal after
a session / a per-set proposal). The evidence note's form is decided (0.30). How sessions without
sets, steps and distance count in "This week" (§4; owner 2026-10-21: units of measurement, but not
in the way sets are; to refine). The Italian translation of the app and of the evidence pack (0.39). The default rest length in Settings (0.42).
The boulder's form is chosen "for now" (0.45).

**Workshop agenda [proposed order]:**
1. Commentary rules (§7) and the evidence pack.
2. Training modules (§8): the list, defaults, what each covers.
3. Purposes and screens (§9), and the rows left out.
4. Screen rules (§10).
5. ~~Re-cut of the scope stages by module~~ done: §3, decisions 0.22-0.27.
6. Inner screens to mock up first, then the deferred items, then Today last.
