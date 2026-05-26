plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.compose.compiler)
}

android {
    namespace = "ci.nsu.mobile.auth"
    compileSdk = 36

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.runtime)
    // Hilt
    implementation(libs.hilt.android)
    implementation(libs.hilt.navigation.compose)
    // Network
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.scalars)
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging.interceptor)
    // Kotlinx Serialization
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.kotlinx.coroutines.android)
}