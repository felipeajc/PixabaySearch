import java.util.Properties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
}

val pixabayFile = rootProject.file("security/pixabay.properties")
val pixabayProps = Properties()
pixabayFile.inputStream().use { pixabayProps.load(it) }

android {
    namespace = "com.challenge.felipeajc.pixabaysearch"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.challenge.felipeajc.pixabaysearch"
        minSdk = 23
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        val apiKey: String =
            pixabayProps["API_KEY"]?.toString() ?: throw IllegalArgumentException("API_KEY not found in pixabay.properties")
        val baseUrl: String =
            pixabayProps["BASE_URL"]?.toString() ?: throw IllegalArgumentException("BASE_URL not found in pixabay.properties")
        val apiKeyParameterName: String =
            pixabayProps["API_KEY_PARAMETER_NAME"]?.toString()
                ?: throw IllegalArgumentException("API_KEY_PARAMETER_NAME not found in pixabay.properties")

        buildConfigField("String", "API_KEY", apiKey)
        buildConfigField("String", "BASE_URL", baseUrl)
        buildConfigField("String", "API_KEY_PARAMETER_NAME", apiKeyParameterName)
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
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
    implementation(libs.dagger.hilt.android)
    testImplementation("org.junit.jupiter:junit-jupiter:5.8.1")
    testImplementation("org.junit.jupiter:junit-jupiter:5.8.1")
    kapt(libs.dagger.hilt.android.compiler)
    implementation(libs.hilt.navigation.compose)
    implementation(libs.androidx.room.ktx)
    implementation(libs.timber)
    implementation(libs.retrofit)
    implementation(libs.converter.moshi)
    implementation(libs.logging.interceptor)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    implementation(libs.accompanist.coil)

    kapt("androidx.room:room-compiler:2.6.1")

    testImplementation(libs.mockito.core)

    // Dependências para testes de coroutines
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.androidx.core.testing)
    testImplementation(libs.mockk)
}