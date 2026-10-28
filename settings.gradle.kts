pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
    // The plugin's marker file is not in the offline cache, its module is: resolve the id to the module.
    resolutionStrategy {
        eachPlugin {
            if (requested.id.id == "org.jetbrains.kotlin.plugin.compose") {
                useModule("org.jetbrains.kotlin:compose-compiler-gradle-plugin:${requested.version}")
            }
            if (requested.id.id == "org.jetbrains.kotlin.plugin.serialization") {
                useModule("org.jetbrains.kotlin:kotlin-serialization:${requested.version}")
            }
        }
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

// CodeQL reads Kotlin by watching the compiler run; a build-cache hit compiles nothing and leaves it no
// code to scan. The CodeQL action exports CODEQL_ACTION_INIT_HAS_RUN to its later steps, so only its build
// skips the cache.
buildCache {
    local {
        isEnabled = System.getenv("CODEQL_ACTION_INIT_HAS_RUN") == null
    }
}

rootProject.name = "sisyphusfit"

// SPEC §1: pure rules, the database, the Android app. A Windows app would join as :windows.
include(":core", ":data", ":app")
