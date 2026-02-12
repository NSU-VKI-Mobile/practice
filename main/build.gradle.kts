plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "ci.nsu.moble.main"
    compileSdk = 34  // НЕ ИСПОЛЬЗУЙТЕ 36, стабильнее 34

    defaultConfig {
        applicationId = "ci.nsu.moble.main"
        minSdk = 24
        targetSdk = 34  // Тоже 34
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
    // УДАЛИТЕ ЭТУ СТРОКУ:
    // implementation("com.android.support:appcompat-v7:28.0.0")

    // ОСТАВЬТЕ ТОЛЬКО ANDROIDX:
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.material)
    implementation(libs.androidx.fragment.ktx)

    // ЗАМЕНИТЕ ТЕСТОВЫЕ ЗАВИСИМОСТИ:
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test:runner:1.5.2")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}