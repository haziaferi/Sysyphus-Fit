"""Design tokens (design/tokens.json, SPEC §10 rule 9): check the file and generate from it.

    python tools/tokens.py              # check: every reference resolves, every raw value is used by a role,
                                        # colours are #rrggbb or #rrggbbaa, density pairs are [balanced, compact],
                                        # SPEC.md's token tables match the file
    python tools/tokens.py css [compact] # CSS custom properties for one density, for the mockups
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
    print(f"selftest: {len(cases) + 1} planted defects each caught")


def main(argv):
    if argv[:1] == ["--selftest"]:
        selftest()
        return 0
    t = load()
    if argv[:1] == ["css"]:
        print(css(t, 1 if argv[1:2] == ["compact"] else 0))
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
    _, block = spec_block()
    if block is not None and block != md(t):
        f.append("SPEC.md's token tables differ from design/tokens.json: run python tools/tokens.py md --write")
    for x in f:
        print(x)
    print(f"tokens: {len(list(raw_values(t)))} raw values, {sum(1 for g in GROUPS for _ in leaves(t.get(g, {}), (g,)))} tokens, {len(f)} finding(s)")
    return 1 if f else 0


if __name__ == "__main__":
    sys.exit(main(sys.argv[1:]))
