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
      versionCode = 6
      versionName = "2.2"

      testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
   }

   buildTypes {
      release {
         isMinifyEnabled = false
         proguardFiles(
            getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
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
   implementation(libs.androidx.compose.runtime)
   implementation(libs.androidx.compose.ui)

   implementation(libs.material.icons.extended)

   implementation(libs.ktor.client.core)
   implementation(libs.ktor.client.cio)
   implementation(libs.ktor.client.json)
   implementation(libs.ktor.client.content.negotiation)
   implementation(libs.ktor.client.serialization)
   implementation("com.google.code.gson:gson:2.13.2")
   implementation("com.github.SignificoHealth:compose-html:1.0.3")

   implementation("androidx.datastore:datastore-preferences:1.1.7")
   implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.2")
   implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.10.2")
}