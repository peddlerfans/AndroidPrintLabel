plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.lnsk.deliver"
    compileSdk = rootProject.extra.get("compile_sdk") as Int

    defaultConfig {
        minSdk = rootProject.extra.get("min_sdk") as Int

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
//        jvmTarget = "1.8"
//        jvmTarget = JavaVersion.VERSION_11.majorVersion// "11"
        jvmTarget = JavaVersion.VERSION_11.toString()// "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {


    api(project(":core:network"))
    api(project(":core:navigation"))


    api(project(":hardware:JCPrint"))
    api(project(":hardware:ble"))



    implementation(libs.king.zxing.lite)

    implementation(libs.jetbrains.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)

    implementation(libs.lifecycle.viewmodel.compose)

    // 加载网络图片
    implementation(libs.coil.compose)

    implementation(libs.androidx.lifecycle.lifecycle.viewmodel.compose)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))

    implementation(libs.material)

    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.material3)

    implementation(libs.androidx.ui.tooling.preview)


    testImplementation(libs.junit)
    debugImplementation(libs.androidx.ui.tooling)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}