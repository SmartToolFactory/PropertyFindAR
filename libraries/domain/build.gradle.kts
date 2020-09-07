import extension.addInstrumentationTestDependencies
import extension.addUnitTestDependencies

plugins {
    id(Plugins.ANDROID_LIBRARY_PLUGIN)
    id(Plugins.KOTLIN_ANDROID_PLUGIN)
    id(Plugins.KOTLIN_ANDROID_EXTENSIONS_PLUGIN)
    id(Plugins.KOTLIN_KAPT_PLUGIN)
    id(Plugins.DAGGER_HILT_PLUGIN)
}

android {

    compileSdkVersion(AndroidVersion.COMPILE_SDK_VERSION)
    defaultConfig {
        minSdkVersion(AndroidVersion.MIN_SDK_VERSION)
        targetSdkVersion(AndroidVersion.TARGET_SDK_VERSION)
        versionCode = AndroidVersion.VERSION_CODE
        versionName = AndroidVersion.VERSION_NAME
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    implementation(project(Modules.AndroidLibrary.DATA))

    implementation(Deps.KOTLIN)
    implementation(Deps.ANDROIDX_CORE_KTX)

    // Dagger
    implementation(Deps.DAGGER_HILT_ANDROID)
    kapt(Deps.DAGGER_HILT_COMPILER)

    // RxJava
    implementation(Deps.RX_JAVA3)
    // RxAndroid
    implementation(Deps.RX_JAVA3_ANDROID)

    // Coroutines
    implementation(Deps.COROUTINES_CORE)
    implementation(Deps.COROUTINES_ANDROID)

    testImplementation(project(Modules.AndroidLibrary.TEST_UTILS))

    testImplementation(Deps.GSON)

    addUnitTestDependencies()
    addInstrumentationTestDependencies()
}
