plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "ru.gorbulevsv.composeukazania"
    compileSdk = 36

    defaultConfig {
        applicationId = "ru.gorbulevsv.androidukazaniaseptember"
        minSdk = 27
        targetSdk = 36
        versionCode = 5
        versionName = "2.1"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.material.icons.extended)

    implementation("androidx.compose.material3:material3:1.4.0")

    val ktor_v = "3.3.0"
    implementation("io.ktor:ktor-client-core:$ktor_v")
    implementation("io.ktor:ktor-client-cio:$ktor_v")
    implementation("io.ktor:ktor-client-json:$ktor_v")
    implementation("io.ktor:ktor-client-content-negotiation:$ktor_v")
    implementation("io.ktor:ktor-client-serialization:$ktor_v")
    implementation("com.google.code.gson:gson:2.13.2")
    implementation("com.github.SignificoHealth:compose-html:1.0.3")
}