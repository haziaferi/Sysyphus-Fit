plugins {
    alias(libs.plugins.android.application)
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
}
