import extension.addBaseDynamicFeatureModuleDependencies
import extension.addInstrumentationTestDependencies
import extension.addUnitTestDependencies

plugins {
    id(Plugins.ANDROID_DYNAMIC_FEATURE_PLUGIN)
    id(Plugins.KOTLIN_ANDROID_PLUGIN)
    id(Plugins.KOTLIN_ANDROID_EXTENSIONS_PLUGIN)
    id(Plugins.KOTLIN_KAPT_PLUGIN)
    id(Plugins.DAGGER_HILT_PLUGIN)
    id(Plugins.NAVIGATION_SAFE_ARGS)
}

android {

    compileSdkVersion(AndroidVersion.COMPILE_SDK_VERSION)

    defaultConfig {
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

    packagingOptions {
        exclude("META-INF/AL2.0")
    }

    dataBinding.isEnabled = true
//    android.buildFeatures.viewBinding = true

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

    implementation(project(Modules.APP))
    implementation(project(Modules.AndroidLibrary.CORE))
    implementation(project(Modules.AndroidLibrary.DOMAIN))

    addBaseDynamicFeatureModuleDependencies()

    // Support and Widgets
    implementation(Deps.APPCOMPAT)
    implementation(Deps.MATERIAL)
    implementation(Deps.CONSTRAINT_LAYOUT)

    // Glide
    implementation(Deps.GLIDE)
    kapt(Deps.GLIDE_COMPILER)

    // Lottie
    implementation(Deps.LOTTIE)

    // Unit Tests
    addUnitTestDependencies()
    testImplementation(project(Modules.AndroidLibrary.TEST_UTILS))

    // Instrumentation Tests
    addInstrumentationTestDependencies()
    androidTestImplementation(project(Modules.AndroidLibrary.TEST_UTILS))
}
