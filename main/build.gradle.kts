plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
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

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"  // ИСПРАВЛЕНО
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
}

dependencies {
    // УДАЛИТЕ эту строку - это старая support библиотека:
    // implementation("com.android.support:appcompat-v7:28.0.0")

    // ОСТАВЬТЕ эти - они из AndroidX:
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.material)  // material.google.com - это тоже AndroidX
    implementation(libs.androidx.fragment.ktx)

    // Для тестов тоже нужно заменить support на androidx:ы
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test:runner:1.5.2")        // Вместо 1.0.2
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")  // Вместо 3.0.2
}