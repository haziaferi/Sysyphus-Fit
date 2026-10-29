package com.hazia.sisyphusfit.ui.modules

import androidx.annotation.DrawableRes
import com.hazia.sisyphusfit.R
import com.hazia.sisyphusfit.core.Module

/** A module as the person reads it (owner's wording, 2026-10-29): its name, one line, and what it includes. */
data class ModuleText(val name: String, val line: String, val includes: String, @DrawableRes val icon: Int)

val ModuleTexts: Map<Module, ModuleText> = mapOf(
    Module.DAILY_MOVEMENT to ModuleText(
        "Daily movement",
        "Creating mindful interludes to get up and break stillness",
        "Includes: short walks, daily steps and getting up during long sitting sessions",
        R.drawable.ic_module_daily,
    ),
    Module.STRENGTH to ModuleText(
        "Strength",
        "Working out with weights and planks",
        "Includes: customizable routines, rest timer, plank timer and tracking progress",
        R.drawable.ic_module_strength,
    ),
    Module.STAMINA to ModuleText(
        "Stamina",
        "Tracking cardio at every pace",
        "Includes: any aerobic activity, steady rhythm, interval timers and offline GPS tracking",
        R.drawable.ic_module_stamina,
    ),
    Module.MOBILITY_BALANCE to ModuleText(
        "Mobility and balance",
        "Training range of motion and stability",
        "Includes: customizable routines, suggested exercises and simple self-checks to track your progress",
        R.drawable.ic_module_mobility,
    ),
    Module.BODY_MEASURES to ModuleText(
        "Body measures",
        "Tracking personal progress through body measurements and/or weight",
        "Includes: records and trends, optionally shown in correlation to your activities",
        R.drawable.ic_module_measures,
    ),
)

/** The modules this build can use; the others are offered and say they arrive later (owner's call 3). */
val BuiltModules: Set<Module> = setOf(Module.STRENGTH)
