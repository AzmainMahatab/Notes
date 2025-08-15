plugins {

    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.compose.compiler)


    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.google.ksp)
}

android {
    namespace = "com.example.notes"
    compileSdk = libs.versions.android.compileSdk.get().toInt()


    defaultConfig {
        applicationId = "com.example.notes"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()

        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
            )
        }
    }

//    compileOptions {
//        sourceCompatibility = JavaVersion.VERSION_17
//        targetCompatibility = JavaVersion.VERSION_17
//    }
//
//    kotlinOptions {
//        jvmTarget = "17"
//    }

    buildFeatures {
        compose = true
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            excludes += "META-INF/INDEX.LIST"
        }
    }

}

kotlin {
    jvmToolchain(libs.versions.jvm.get().toInt())
    compilerOptions {
        freeCompilerArgs.addAll(
            "-opt-in=kotlin.time.ExperimentalTime",
            "-XXLanguage:+ExplicitBackingFields",
        )
    }
}

//java {
//val javaVersion = JavaVersion.toVersion(libs.versions.jvm.get())
//sourceCompatibility = javaVersion
//targetCompatibility = javaVersion
//
////    toolchain {
////        languageVersion = JavaLanguageVersion.of(21)
////    }
//}

dependencies {
    implementation("org.example:android-std-ext:1.0-SNAPSHOT")
    implementation("org.example:compose-ext:1.0-SNAPSHOT")

    //Android + Compose
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime)
    implementation(libs.androidx.activity.compose)

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)


    // DateTime
    implementation(libs.kotlinx.datetime)


    //Test
    testImplementation(libs.junit4)
//    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.android.junit4)

    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)


    //    Compose Icon
    implementation(libs.compose.material.icons.extended)


    implementation(kotlin("reflect"))


    //    Room
//    implementation(libs.androidx.room.ktx) //With Coroutines
    implementation(libs.room.runtime)
    ksp(libs.room.compiler)


    // Koin
    implementation(libs.koin.androidx.compose)


    //Proto DataStore + kotlinx-serialization
    implementation(libs.androidx.datastore.android)
    implementation(libs.kotlinx.collections.immutable)
    implementation(libs.kotlinx.serialization.protobuf)


    //Navigation
//    implementation(libs.androidx.navigation.compose)
    implementation(libs.nav3.runtime)
    implementation(libs.nav3.ui)
    implementation(libs.androidx.lifecycle.viewmodel.nav3)

}