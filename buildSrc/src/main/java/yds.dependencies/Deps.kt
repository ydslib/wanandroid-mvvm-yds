package yds.dependencies

object Versions {
    const val compileSdk = 30
    const val buildTools = "30.0.3"
    const val minSdk = 21
    const val targetSdk = 30
    const val versionCode = 1
    const val versionName = "1.0"

    const val appcompat = "1.2.0"
    const val core_ktx = "1.3.0"
    const val constraintlayout = "2.0.4"
    const val test_junit = "4.13"
    const val androidTest_junit = "1.1.2"
    const val androidTest_espresso_core = "3.3.0"


}

object Deps {
    const val appcompat = "androidx.appcompat:appcompat:${Versions.appcompat}"
    const val core_ktx = "androidx.core:core-ktx:${Versions.core_ktx}"
    const val constraintlayout = "androidx.constraintlayout:constraintlayout:${Versions.constraintlayout}"


    const val test_junit = "junit:junit:${Versions.test_junit}"
    const val androidTest_junit = "androidx.test.ext:junit:${Versions.androidTest_junit}"
    const val androidTest_espresso_core = "androidx.test.espresso:espresso-core:${Versions.androidTest_espresso_core}"
}