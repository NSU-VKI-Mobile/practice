plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.symbol.processing)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "ci.nsu.mobile.main"
    compileSdk = 36

    defaultConfig {
        applicationId = "ci.nsu.mobile.main"
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
    // Ядро (обязательно)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat) // Оставляем для совместимости тем

    // Jetpack Compose (основные библиотеки UI)
    implementation(libs.androidx.material3) // Сами кнопки, тексты и цвета
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)

    // Room (База данных - без изменений)
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    debugImplementation(libs.androidx.ui.test.manifest)
    ksp(libs.androidx.room.compiler)

    // Navigation & MVVM для COMPOSE (это важно!)
    implementation(libs.androidx.navigation.compose)           // Навигация между функциями
    implementation(libs.androidx.lifecycle.viewmodel.compose) // Чтобы работала viewModel() в Compose
    implementation(libs.androidx.lifecycle.runtime.ktx)

    // Тесты (стандарт)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}