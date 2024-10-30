import java.util.Properties

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.compose.compiler)
}


val localProperties = rootProject.file("local.properties")
val properties = Properties()

if (localProperties.exists()) {
    properties.load(localProperties.inputStream())
}

val supabaseKey: String = properties["SUPABASE_KEY"] as String? ?: ""
val supabaseUrl: String = properties["SUPABASE_URL"] as String? ?: ""

android {
    namespace = "com.example.digital_mentor"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.digital_mentor"
        minSdk = 24
        targetSdk = 34
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
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            // Creating a buildconfig Field to get values
            buildConfigField("String", "supabaseKey", "\"$supabaseKey\"")
            buildConfigField("String", "supabaseUrl", "\"$supabaseUrl\"")
        }

        debug {
            // Creating a buildconfig Field to get values
            buildConfigField("String", "supabaseKey", "\"$supabaseKey\"")
            buildConfigField("String", "supabaseUrl", "\"$supabaseUrl\"")
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
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // Retrofit
    implementation(libs.bundles.retrofit)

    // Koin
    implementation(libs.bundles.koin)

    // Navigation compose
    implementation(libs.bundles.navigationCompose)

    // Material icons extended
    implementation(libs.material.icons.extended)

    // Supabase
    implementation(platform("io.github.jan-tennert.supabase:bom:2.5.4"))
    implementation("io.github.jan-tennert.supabase:postgrest-kt")
    implementation("io.github.jan-tennert.supabase:auth-kt:3.0.1")
    implementation("io.ktor:ktor-client-android:2.3.12")

    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.6")

}