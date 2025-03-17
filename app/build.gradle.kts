import java.util.Calendar

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.lnsk.labelprinting"
    compileSdk = rootProject.extra.get("compile_sdk") as Int

    defaultConfig {
        applicationId = "com.lnsk.labelprinting"
        minSdk = rootProject.extra.get("min_sdk") as Int
        targetSdk = rootProject.extra.get("target_sdk") as Int
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    signingConfigs {
        create("release") {
            storeFile = file("$rootDir/app/sign/lnsk_carbon.jks")
            keyAlias = "key0"
            keyPassword = "123123"
            storePassword = "123123"
            enableV1Signing = true
            enableV2Signing = true
//            enableV3Signing = true
//            enableV4Signing = true
        }
    }

    buildTypes {
        release {
            signingConfig = signingConfigs.getAt("release")
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
        jvmTarget = JavaVersion.VERSION_11.toString()// "11"
    }
    buildFeatures {
        compose = true
    }

}

dependencies {
    implementation(fileTree("dir" to "libs", "include" to listOf("*.jar", "*.aar")))

    implementation(project(":feature:deliver"))

    implementation(libs.capturable)


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
}

android.applicationVariants.configureEach {
    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH) + 1
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    val hour = calendar.get(Calendar.HOUR_OF_DAY)
    val minute = calendar.get(Calendar.MINUTE)
//    val second = calendar.get(Calendar.SECOND)

    outputs.configureEach {
        val variantTypeName = buildType.name
        val versionName = android.defaultConfig.versionName
        val appDirectory =
            "${rootDir}/apk/V${versionName}/${year}_${month}_${day}/${hour}_${minute}/${variantTypeName}"

        println("APP_PATH:${appDirectory}")

        assembleProvider.configure {
            copy {
                from(outputFile)
                into(appDirectory)
            }
        }
    }
}