"""Design tokens (design/tokens.json, SPEC §10 rule 9): check the file and generate from it.

    python tools/tokens.py              # check: every reference resolves, every raw value is used by a role,
                                        # colours are #rrggbb or #rrggbbaa, density pairs are [balanced, compact],
                                        # SPEC.md's token tables and the app's Tokens.kt match the file
    python tools/tokens.py css [compact] # CSS custom properties for one density, for the mockups
    python tools/tokens.py kotlin [--write] # the app's Tokens.kt (Compose types); --write puts it in place
    python tools/tokens.py md [--write]  # SPEC §10's token tables; --write puts them into SPEC.md between the markers
    python tools/tokens.py --selftest   # plant each defect class, require each to fail

Exit 0 = pass, 1 = findings. Standard library only.
"""
import copy
import json
import pathlib
import re
import sys

ROOT = pathlib.Path(__file__).resolve().parents[1]
FILE = ROOT / "design" / "tokens.json"
REF = re.compile(r"\{([\w.-]+)\}")
HEX = re.compile(r"^#[0-9a-f]{6}([0-9a-f]{2})?$")
GROUPS = ("color", "space", "size", "radius", "stroke", "elevation", "motion", "type")


def load(path=FILE):
    return json.loads(pathlib.Path(path).read_text(encoding="utf-8"))


def leaves(node, path=()):
    """(dotted name, token) for every token: a dict with $value."""
    if isinstance(node, dict):
        if "$value" in node:
            yield ".".join(path), node
            return
        for k, v in node.items():
            if not k.startswith("$"):
                yield from leaves(v, path + (k,))


def raw_values(t):
    return {f"{g}.{k}": v for g, vals in t["raw"].items() for k, v in vals.items() if not k.startswith("$")}


def table(t):
    out = dict(raw_values(t))
    for g in GROUPS:
        for name, tok in leaves(t.get(g, {}), (g,)):
            out[name] = tok["$value"]
    return out


def resolve(value, tab, seen=()):
    if isinstance(value, str):
        def one(m):
            key = m.group(1)
            if key in seen:
                raise ValueError("cycle at " + key)
            if key not in tab:
                raise KeyError(key)
            return str(resolve(tab[key], tab, seen + (key,)))
        return REF.sub(one, value)
    if isinstance(value, list):
        return [resolve(v, tab, seen) for v in value]
    if isinstance(value, dict):
        return {k: resolve(v, tab, seen) for k, v in value.items()}
    return value


def check(t):
    findings = []
    tab = table(t)
    for name, v in raw_values(t).items():
        if not isinstance(v, str) or not HEX.match(v):
            findings.append(f"{name}: not #rrggbb or #rrggbbaa: {v!r}")
    for g in GROUPS:
        for name, tok in leaves(t.get(g, {}), (g,)):
            try:
                resolve(tok["$value"], tab)
            except KeyError as e:
                findings.append(f"{name}: unknown reference {{{e.args[0]}}}")
            except ValueError as e:
                findings.append(f"{name}: {e}")
            if g in ("space", "size"):
                v = tok["$value"]
                if not (isinstance(v, list) and len(v) == 2 and all(isinstance(x, (int, float)) for x in v)):
                    findings.append(f"{name}: a density pair [balanced, compact] in dp is expected, got {v!r}")
    used = set()
    for g in GROUPS:
        for _, tok in leaves(t.get(g, {}), (g,)):
            used.update(REF.findall(json.dumps(tok["$value"])))
    for name in raw_values(t):
        if name not in used:
            findings.append(f"{name}: raw value no role uses")
    return findings


def css_name(name):
    return "--" + name.replace(".", "-")


def px(v):
    return " ".join(x if x.endswith("%") or x == "/" or x == "inset" or x.startswith("#") else f"{x}px"
                    for x in str(v).split()) if not isinstance(v, (int, float)) else f"{v}px"


def css(t, density=0):
    tab = table(t)
    lines = []
    for g in GROUPS:
        for name, tok in leaves(t.get(g, {}), (g,)):
            v = resolve(tok["$value"], tab)
            if g == "color":
                lines.append(f"{css_name(name)}:{v}")
            elif g in ("space", "size"):
                lines.append(f"{css_name(name)}:{v[density]}px")
            elif g in ("radius", "stroke"):
                lines.append(f"{css_name(name)}:{px(v)}")
            elif g == "elevation":
                lines.append(f"{css_name(name)}:" + ", ".join(px(part.strip()) for part in v.split(",")))
            elif g == "type" and isinstance(v, dict) and "size" in v and isinstance(v["size"], list):
                lines.append(f"{css_name(name)}-size:{v['size'][density]}px")
                if "line" in v:
                    lines.append(f"{css_name(name)}-line:{v['line'][density]}px")
    return ":root{" + ";".join(lines) + "}"


SPEC = ROOT / "SPEC.md"
BEGIN, END = "<!-- tokens:begin (tools/tokens.py md --write) -->", "<!-- tokens:end -->"


def desc(tok):
    return tok.get("$description", "").replace("|", "/")


def md(t):
    """SPEC §10's token tables: written from the file, never by hand."""
    tab = table(t)
    out = ["Colour roles (screens use these; raw greys are named by CIE L*):", "",
           "| Role | Raw | Value | Used for |", "|---|---|---|---|"]
    for name, tok in leaves(t["color"], ("color",)):
        ref = tok["$value"]
        raw = ref.strip("{}") if ref.startswith("{grey") or ref.startswith("{black") else ref.strip("{}").replace("color.", "")
        out.append(f"| {name.replace('color.', '')} | {raw} | `{resolve(ref, tab)}` | {desc(tok)} |")
    out += ["", "Space and size, dp, balanced / compact:", "", "| Token | dp | Used for |", "|---|---|---|"]
    for g in ("space", "size"):
        for name, tok in leaves(t[g], (g,)):
            b, c = tok["$value"]
            out.append(f"| {name} | {b:g} / {c:g} | {desc(tok)} |")
    out += ["", "Shape, line, elevation and motion (the same at both densities):", "", "| Token | Value | Used for |", "|---|---|---|"]
    for g in ("radius", "stroke", "elevation", "motion"):
        for name, tok in leaves(t[g], (g,)):
            v = resolve(tok["$value"], tab)
            if isinstance(v, dict):
                v = ", ".join(f"{k} {x}" + (" ms" if k == "duration" else "") for k, x in v.items())
            elif isinstance(v, (int, float)):
                v = f"{v:g} dp"
            out.append(f"| {name} | {v} | {desc(tok)} |")
    return "\n".join(out)


def spec_block():
    s = SPEC.read_text(encoding="utf-8")
    if BEGIN not in s or END not in s:
        return s, None
    return s, s[s.index(BEGIN) + len(BEGIN):s.index(END)].strip("\n")


KOTLIN = ROOT / "app" / "src" / "main" / "kotlin" / "com" / "hazia" / "sisyphusfit" / "ui" / "theme" / "Tokens.kt"


def camel(path):
    parts = re.split(r"[.-]", path)
    return parts[0] + "".join(p[:1].upper() + p[1:] for p in parts[1:])


def kt_num(v):
    return f"{v:g}" if float(v) != int(v) else str(int(v))


def kt_dp(v):
    return kt_num(v) + ".dp"


def kt_color(hex_):
    rgb, a = hex_[1:7], (hex_[7:9] or "ff")
    return f"Color(0x{a.upper()}{rgb.upper()})"


def kt_shadow(part):
    part = part.strip()
    inset = part.startswith("inset ")
    x, y, blur, spread, color = part.removeprefix("inset ").split()
    return (f"ShadowSpec({kt_dp(float(x))}, {kt_dp(float(y))}, {kt_dp(float(blur))}, {kt_dp(float(spread))}, "
            f"{kt_color(color)}, inset = {'true' if inset else 'false'})")


def kotlin(t):
    """Tokens.kt for the app: the same roles, resolved, in Compose types (SPEC §10 rule 9)."""
    tab = table(t)
    o = ["// Generated by tools/tokens.py from design/tokens.json (SPEC §10 rule 9). Do not edit:",
         "// change design/tokens.json and run `python tools/tokens.py kotlin --write`.",
         "package com.hazia.sisyphusfit.ui.theme", "",
         "import androidx.compose.animation.core.CubicBezierEasing",
         "import androidx.compose.ui.graphics.Color",
         "import androidx.compose.ui.unit.Dp",
         "import androidx.compose.ui.unit.TextUnit",
         "import androidx.compose.ui.unit.dp",
         "import androidx.compose.ui.unit.sp", "",
         "/** CSS box-shadow: x, y, blur, spread, colour; inset draws inside the shape. */",
         "data class ShadowSpec(val x: Dp, val y: Dp, val blur: Dp, val spread: Dp, val color: Color, val inset: Boolean)", "",
         "/** Corner radii in CSS order: top-left, top-right, bottom-right, bottom-left. */",
         "data class CornerRadii(val topLeft: Dp, val topRight: Dp, val bottomRight: Dp, val bottomLeft: Dp)", "",
         "/** CSS elliptical radii in percent of the box: horizontal then vertical, each in CSS corner order. */",
         "class PebbleRadii(val horizontal: List<Float>, val vertical: List<Float>)", "",
         "class MotionSpec(val durationMs: Int, val easing: CubicBezierEasing? = null)", "",
         "/** A text role; carve is Sisifo's CARV axis, tracking in em, features as CSS font-feature-settings. */",
         "data class TypeRole(",
         "    val size: TextUnit, val line: TextUnit, val weight: Int, val carve: Float,",
         "    val features: String = \"\", val upper: Boolean = false, val tracking: Float = 0f,",
         ")", "",
         "object Tokens {"]
    o.append("    object Colors {")
    for name, tok in leaves(t["color"], ("color",)):
        o.append(f"        /** {desc(tok)} */")
        o.append(f"        val {camel(name.removeprefix('color.'))} = {kt_color(resolve(tok['$value'], tab))}")
    o.append("    }")
    for g, cls in (("space", "Space"), ("size", "Size")):
        toks = list(leaves(t[g], (g,)))
        fields = ", ".join(f"val {camel(n.split('.', 1)[1])}: Dp" for n, _ in toks)
        o += ["", f"    class {cls}({fields}) {{", "        companion object {"]
        for i, density in enumerate(("Balanced", "Compact")):
            args = ", ".join(kt_dp(tok["$value"][i]) for _, tok in toks)
            o.append(f"            val {density} = {cls}({args})")
        o += ["        }", "    }"]
    o += ["", "    object Radius {"]
    for name, tok in leaves(t["radius"], ("radius",)):
        v, n = tok["$value"], camel(name.split(".", 1)[1])
        o.append(f"        /** {desc(tok)} */")
        if isinstance(v, str) and "/" in v:
            h, vv = (side.split() for side in v.split("/"))
            fl = lambda xs: ", ".join(kt_num(float(x.rstrip("%"))) + "f" for x in xs)
            o.append(f"        val {n} = PebbleRadii(listOf({fl(h)}), listOf({fl(vv)}))")
        elif isinstance(v, str):
            o.append(f"        val {n} = CornerRadii({', '.join(kt_dp(float(x)) for x in v.split())})")
        else:
            o.append(f"        val {n} = {kt_dp(v)}")
    o += ["    }", "", "    object Stroke {"]
    for name, tok in leaves(t["stroke"], ("stroke",)):
        o.append(f"        /** {desc(tok)} */")
        o.append(f"        val {camel(name.split('.', 1)[1])} = {kt_dp(tok['$value'])}")
    o += ["    }", "", "    object Elevation {"]
    for name, tok in leaves(t["elevation"], ("elevation",)):
        parts = resolve(tok["$value"], tab).split(",")
        o.append(f"        /** {desc(tok)} */")
        o.append(f"        val {camel(name.split('.', 1)[1])} = listOf({', '.join(kt_shadow(p) for p in parts)})")
    o += ["    }", "", "    object Motion {"]
    for name, tok in leaves(t["motion"], ("motion",)):
        v, n = tok["$value"], camel(name.split(".", 1)[1])
        if not isinstance(v, dict):
            o.append(f"        // {n}: {v}")
            continue
        o.append(f"        /** {desc(tok)} */")
        ease = ""
        if "easing" in v:
            nums = re.findall(r"[\d.]+", v["easing"])
            ease = ", CubicBezierEasing(" + ", ".join(kt_num(float(x)) + "f" for x in nums) + ")"
        o.append(f"        val {n} = MotionSpec({v['duration']}{ease})")
    o.append("    }")
    roles = {name.split(".", 1)[1]: tok["$value"] for name, tok in leaves(t["type"], ("type",))}
    base = {r: v for r, v in roles.items() if isinstance(v, dict) and isinstance(v.get("size"), list) and "weight" in v}
    def role(v, i):
        return (f"TypeRole({kt_num(v['size'][i])}.sp, {kt_num(v['line'][i])}.sp, {v['weight']}, {kt_num(v['carve'])}f"
                + (f", features = \"{','.join(v['features'])}\"" if v.get("features") else "")
                + (", upper = true" if v.get("case") == "upper" else "")
                + (f", tracking = {kt_num(v['tracking'])}f" if v.get("tracking") else "") + ")")
    derived = {}
    for r, over in roles["comparison"].items():
        derived["comparison-" + r] = {**base[r], "size": over["size"], "line": over.get("line", [base[r]["line"][0]] * 2)}
    chip = roles["chip"]
    derived["chip"] = {**base[chip["role"]], "size": chip["size"], "line": chip["line"]}
    allr = {**base, **derived}
    fields = ", ".join(f"val {camel(r)}: TypeRole" for r in allr)
    o += ["", "    /** Type roles in sp (SPEC 0.49); comparison and chip roles keep their sizes at both densities (0.47, 0.52). */",
          f"    class Type({fields}) {{", "        companion object {"]
    for i, density in enumerate(("Balanced", "Compact")):
        o.append(f"            val {density} = Type(")
        o += [f"                {role(v, i)}," for v in allr.values()]
        o.append("            )")
    o += ["        }", "    }", "}", ""]
    return "\n".join(o)


def kotlin_drift(t, text):
    return [] if text == kotlin(t) else [
        "app/.../ui/theme/Tokens.kt differs from design/tokens.json: run python tools/tokens.py kotlin --write"]


TYPE_ROW = re.compile(r"^\| ([a-z -]+) \| [^|]* \| (\d+) / (\d+) \| (\d+) / (\d+) \| (\d+) \| (\d+) \|", re.M)


def type_table(t):
    """SPEC §10 rule 8's type table (hand-written, with what each role is used for) against the file's type roles."""
    if not SPEC.exists():
        return []
    rows = {m.group(1).replace(" ", "-"): [int(x) for x in m.groups()[1:]] for m in TYPE_ROW.finditer(SPEC.read_text(encoding="utf-8"))}
    findings = []
    for name, tok in leaves(t["type"], ("type",)):
        v = tok["$value"]
        role = name.split(".", 1)[1]
        if not (isinstance(v, dict) and isinstance(v.get("size"), list) and "weight" in v):
            continue
        want = v["size"] + v["line"] + [v["weight"], v["carve"]]
        if rows.get(role) != want:
            findings.append(f"type.{role}: SPEC §10's table says {rows.get(role)}, design/tokens.json {want}")
    return findings


def selftest():
    good = load()
    assert not check(good), check(good)
    cases = {
        "unknown reference": lambda t: t["color"]["ink"]["primary"].__setitem__("$value", "{grey.12}"),
        "not #rrggbb": lambda t: t["raw"]["grey"].__setitem__("10", "#1c1c1"),
        "raw value no role uses": lambda t: t["raw"]["grey"].__setitem__("50", "#777777"),
        "density pair": lambda t: t["space"]["gap"].__setitem__("$value", 8),
        "cycle": lambda t: t["color"]["rest"]["near-leg"].__setitem__("$value", "{color.rest.ring-track}"),
    }
    for what, plant in cases.items():
        t = copy.deepcopy(good)
        plant(t)
        f = check(t)
        assert any(what in x for x in f), (what, f)
    t = copy.deepcopy(good)
    t["type"]["body"]["$value"]["size"] = [17, 14]
    assert any("type.body" in x for x in type_table(t)), "a type role that differs from SPEC's table"
    edited = kotlin(good).replace("0xFFEBEBEB", "0xFFECECEC", 1)
    assert kotlin(good) != edited and kotlin_drift(good, edited), "a hand edit to Tokens.kt"
    assert not kotlin_drift(good, kotlin(good))
    print(f"selftest: {len(cases) + 2} planted defects each caught")


def main(argv):
    if argv[:1] == ["--selftest"]:
        selftest()
        return 0
    t = load()
    if argv[:1] == ["css"]:
        print(css(t, 1 if argv[1:2] == ["compact"] else 0))
        return 0
    if argv[:1] == ["kotlin"]:
        if argv[1:2] == ["--write"]:
            KOTLIN.parent.mkdir(parents=True, exist_ok=True)
            KOTLIN.write_text(kotlin(t), encoding="utf-8", newline="\n")
            print("wrote " + str(KOTLIN.relative_to(ROOT)))
        else:
            print(kotlin(t))
        return 0
    if argv[:1] == ["md"]:
        if argv[1:2] == ["--write"]:
            s, block = spec_block()
            if block is None:
                print(f"SPEC.md has no {BEGIN} ... {END} block")
                return 1
            SPEC.write_text(s.replace(block, md(t), 1) if block else s.replace(BEGIN + "\n", BEGIN + "\n" + md(t) + "\n", 1),
                            encoding="utf-8", newline="\n")
            print("wrote SPEC.md's token tables")
        else:
            print(md(t))
        return 0
    f = check(t) + type_table(t)
    if (ROOT / "app").exists():
        f += kotlin_drift(t, KOTLIN.read_text(encoding="utf-8") if KOTLIN.exists() else "")
    _, block = spec_block()
    if block is not None and block != md(t):
        f.append("SPEC.md's token tables differ from design/tokens.json: run python tools/tokens.py md --write")
    for x in f:
        print(x)
    print(f"tokens: {len(list(raw_values(t)))} raw values, {sum(1 for g in GROUPS for _ in leaves(t.get(g, {}), (g,)))} tokens, {len(f)} finding(s)")
    return 1 if f else 0


if __name__ == "__main__":
    sys.exit(main(sys.argv[1:]))
