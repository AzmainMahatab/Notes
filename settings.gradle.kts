pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }

    versionCatalogs {
        create("libs") {
            from(files("$rootDir/../gradle/libs.versions.toml")) // Adjust path if needed
        }
    }

}


//plugins {
//        id("org.gradle.toolchains.foojay-resolver-convention") version ("1.0.0")
//}
rootProject.name = "notes"

includeBuild("../std-plus")
includeBuild("../android-std-ext")
includeBuild("../compose-ext")

