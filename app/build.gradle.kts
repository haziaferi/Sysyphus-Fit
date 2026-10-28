plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.hazia.sisyphusfit"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.hazia.sisyphusfit"
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
        versionCode = 1
        versionName = "0.0.1"
    }

    buildFeatures {
        compose = true
    }

    // Robolectric renders the Compose tests and Roborazzi's screenshots on the JVM. Roborazzi's Gradle plugin
    // needs a Kotlin stdlib the offline cache lacks, so its modes are set here: tests verify against the committed
    // goldens, and -Proborazzi.record=true records them instead.
    testOptions {
        unitTests.isIncludeAndroidResources = true
        unitTests.all {
            val record = project.findProperty("roborazzi.record") == "true"
            it.systemProperty("roborazzi.test.record", record.toString())
            it.systemProperty("roborazzi.test.verify", (!record).toString())
            it.systemProperty("roborazzi.record.filePathStrategy", "relativePathFromRoborazziContextOutputDirectory")
            it.systemProperty("roborazzi.output.dir", file("src/test/screenshots").path)
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    lint {
        warningsAsErrors = true
        abortOnError = true
        // Version-currency notices depend on the date, not on this tree. AndroidGradlePluginVersion
        // is the fourth id: without it the gate fails on its own when Google ships a new AGP.
        informational += listOf(
            "GradleDependency", "NewerVersionAvailable", "OldTargetApi", "AndroidGradlePluginVersion",
        )
    }
}

dependencies {
    implementation(project(":data"))
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.compose.animation)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.core.ktx)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    testImplementation(libs.junit)
    testImplementation(libs.robolectric)
    testImplementation(platform(libs.androidx.compose.bom))
    testImplementation(libs.androidx.compose.ui.test.junit4)
    testImplementation(libs.roborazzi)
    testImplementation(libs.roborazzi.compose)
    testImplementation(libs.roborazzi.junit.rule)
}
