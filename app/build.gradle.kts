import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "kr.co.dangguel.memokinggpt"
    compileSdk = 35

    defaultConfig {
        applicationId = "kr.co.dangguel.memokinggpt"
        minSdk = 26
        targetSdk = 35
        versionCode = 101
        versionName = "1.01"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        // ✅ local.properties 값 가져오기
        val localProperties = Properties().apply {
            load(rootProject.file("local.properties").inputStream())
        }
        val decryptionKey = localProperties.getProperty("DECRYPTION_KEY")
        val decryptionSalt = localProperties.getProperty("DECRYPTION_SALT")

        buildConfigField("String", "DECRYPTION_KEY", "\"$decryptionKey\"")
        buildConfigField("String", "DECRYPTION_SALT", "\"$decryptionSalt\"")
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        buildConfig = true
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.4"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    kotlin {
        tasks.register("testClasses")
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

    // Navigation
    implementation("androidx.navigation:navigation-compose:2.6.0")

    // Room Database
    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    kapt("androidx.room:room-compiler:2.6.1")

    // Coil (이미지 로딩)
    implementation("io.coil-kt:coil-compose:2.4.0")

    // Hilt (의존성 주입)
    implementation("com.google.dagger:hilt-android:2.51")
    kapt("com.google.dagger:hilt-android-compiler:2.51")
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")

    // Material Icon
    implementation("androidx.compose.material:material-icons-extended:1.5.1")

    // Google Admob
    implementation("com.google.android.gms:play-services-ads:22.4.0")
    
    // MLkit (OCR) 및 google play service용 코루틴 확장 함수
    implementation("com.google.mlkit:text-recognition:16.0.1")
    implementation("com.google.mlkit:text-recognition-korean:16.0.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.6.4")

    // splash screen
    implementation("androidx.core:core-splashscreen:1.0.1")

    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    // JSON 변환을 위한 GSON Converter
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    // app update lib
    implementation("com.google.android.play:app-update:2.1.0")
    implementation("com.google.android.play:app-update-ktx:2.1.0")

    // in-app Review
    implementation("com.google.android.play:review:2.0.2")
    implementation("com.google.android.play:review-ktx:2.0.2")
}
