package com.hazia.sisyphusfit.ui.stone

import java.io.File
import javax.xml.parsers.DocumentBuilderFactory
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.w3c.dom.Element

/** SPEC 0.37: our own set of 28 (30 while the module choice's mark is picked), a 24 grid, 2 dp strokes, no fill; object icons faceted from 0.53. */
class IconsTest {
    private val names = listOf(
        "back", "backspace", "backup", "caret", "check", "chevron", "clock", "close", "edit", "includes_lozenge", "includes_star", "info", "label", "minus",
        "module_daily", "module_measures", "module_mobility", "module_stamina", "module_strength", "play", "plus",
        "restore", "search", "settings", "stop", "tab_history", "tab_more", "tab_start", "tab_week", "timer",
    )
    private val drawables = File("src/main/res/drawable")

    private fun paths(name: String): List<Element> {
        val doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(File(drawables, "ic_$name.xml"))
        assertEquals("24", doc.documentElement.getAttribute("android:viewportWidth").removeSuffix(".0"))
        val nodes = doc.getElementsByTagName("path")
        return (0 until nodes.length).map { nodes.item(it) as Element }
    }

    @Test
    fun all28IconsExist() {
        assertEquals(30, names.size)
        names.forEach { assertTrue("ic_$it.xml", File(drawables, "ic_$it.xml").exists()) }
        assertEquals(names.map { "ic_$it.xml" }.sorted(), drawables.list()!!.filter { it.startsWith("ic_") }.sorted())
    }

    @Test
    fun iconsAreStrokeOnly() {
        names.forEach { name ->
            val ps = paths(name)
            assertTrue(name, ps.isNotEmpty())
            ps.forEach { p ->
                assertTrue("$name stroked", p.hasAttribute("android:strokeColor"))
                assertEquals("$name stroke 2", "2", p.getAttribute("android:strokeWidth").removeSuffix(".0"))
                val fill = p.getAttribute("android:fillColor")
                assertTrue("$name unfilled ($fill)", fill.isEmpty() || fill == "#00000000")
            }
        }
    }

    // 0.37/0.53: the path icons and the -/+ keep round caps; object icons are cut square.
    @Test
    fun pathIconsKeepRoundCapsObjectsAreCut() {
        listOf("back", "minus", "plus", "chevron", "tab_week").forEach { n ->
            paths(n).forEach { assertEquals(n, "round", it.getAttribute("android:strokeLineCap")) }
        }
        listOf("module_strength", "backup", "settings").forEach { n ->
            paths(n).forEach { assertEquals(n, "butt", it.getAttribute("android:strokeLineCap")) }
        }
    }
}
