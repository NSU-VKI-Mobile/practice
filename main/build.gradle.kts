plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "ci.nsu.moble.main"
    compileSdk = 36

    defaultConfig {
        applicationId = "ci.nsu.moble.main"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

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
    // ✅ Только один BOM - для Compose
    implementation(platform("androidx.compose:compose-bom:2024.10.00"))

    // --- AndroidX ---
    implementation("androidx.appcompat:appcompat:1.7.0")

    // Lifecycle - указываем конкретные версии, БЕЗ BOM
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.0")  // Используем 2.8.0, она существует
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.0")

    // Compose (версии из compose-bom)
    implementation("androidx.activity:activity-compose:1.9.0")
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.0")

    // Testing
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test:runner:1.5.2")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation(platform("androidx.compose:compose-bom:2024.10.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")

    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}

// Стратегия разрешения конфликтов
configurations.all {
    resolutionStrategy {
        force("androidx.lifecycle:lifecycle-runtime:2.8.0")
        force("androidx.lifecycle:lifecycle-runtime-ktx:2.8.0")
        force("androidx.lifecycle:lifecycle-viewmodel:2.8.0")
        force("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.0")
        force("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.0")
        force("androidx.lifecycle:lifecycle-common:2.8.0")
    }

    exclude(group = "com.android.support")
}