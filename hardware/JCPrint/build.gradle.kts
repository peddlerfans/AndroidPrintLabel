plugins {
//    alias(libs.plugins.android.library)
//    alias(libs.plugins.kotlin.android)

    id("com.android.library")
    id("kotlin-android")
}

android {
    namespace = "com.hardware.print.jc"
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
}

dependencies {
    compileOnly(fileTree("dir" to "libs", "include" to listOf("*.jar", "*.aar")))
//    implementation(fileTree(mapOf("dir" to "../libs", "include" to listOf("**/*.aar", "**/*.jar"))))


    //打印库
//    implementation(files 'libs/3.2.4-release.aar')
    //B50系列（B50W/B50/T6/T7/T8）/B11系列机型引入，非对应机型可以不引入
//    implementation(files ('libs/LPAPI-2019-11-20-R.jar'))
//    implementation(files ('libs/image-1.8.7.aar'))
//
//    implementation(
//        files(
//            "/libs/3.2.4-release.aar",
//            "/libs/LPAPI-2019-11-20-R.jar",
//            "/libs/image-1.8.7.aar"
//        )
//    )

//    implementation(files("libs/3.2.4-release.aar"))
    //B50系列（B50W/B50/T6/T7/T8）/B11系列机型引入，非对应机型可以不引入
//    implementation(files("libs/LPAPI-2019-11-20-R.jar"))
//    implementation(files("libs/image-1.8.7.aar"))

    implementation(libs.androidx.core.ktx)
    implementation(platform(libs.kotlin.bom))
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}