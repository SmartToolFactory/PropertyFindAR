object Plugins {

    /*
       Project Level
    */
    const val GRADLE = "com.android.tools.build:gradle"
    const val DETEKT = "io.gitlab.arturbosch.detekt"
    const val KTLINT = "org.jlleitschuh.gradle.ktlint"
    const val GIT_HOOKS = "plugins.git-hooks"

    const val CLASSPATH_GRADLE = "com.android.tools.build:gradle:${PluginVersion.GRADLE_VERSION}"
    const val CLASSPATH_KTLINT =
        "org.jlleitschuh.gradle:ktlint-gradle:${PluginVersion.KTLINT_VERSION}"
    const val CLASSPATH_DAGGER_HILT =
        "com.google.dagger:hilt-android-gradle-plugin:${Version.DAGGER_HILT_VERSION}"
    const val CLASSPATH_NAV_SAFE_ARGS =
        "androidx.navigation:navigation-safe-args-gradle-plugin:${PluginVersion.NAV_SAFE_ARGS_VERSION}"

    const val CLASSPATH_MP_CHART = "com.github.dcendents:android-maven-gradle-plugin:2.1"

    /*
        Module Level
     */
    const val DAGGER_HILT_PLUGIN = "dagger.hilt.android.plugin"
    const val ANDROID_APPLICATION_PLUGIN = "com.android.application"
    const val ANDROID_DYNAMIC_FEATURE_PLUGIN = "com.android.dynamic-feature"
    const val ANDROID_LIBRARY_PLUGIN = "com.android.library"

    const val KOTLIN_ANDROID_PLUGIN = "kotlin-android"
    const val KOTLIN_ANDROID_EXTENSIONS_PLUGIN = "kotlin-android-extensions"
    const val KOTLIN_KAPT_PLUGIN = "kotlin-kapt"
    const val NAVIGATION_SAFE_ARGS = "androidx.navigation.safeargs.kotlin"
}
