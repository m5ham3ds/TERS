plugins {
    alias(libs.plugins.android.library)
}
android {
    namespace = "com.aistudio.ultimate.domain"
    compileSdk = 36
    defaultConfig {
        minSdk = 26
    }
}
dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.kotlinx.coroutines.core)
    testImplementation(libs.junit)
    testImplementation(libs.kotlinx.coroutines.test)
}