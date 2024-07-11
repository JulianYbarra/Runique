plugins {
    alias(libs.plugins.runique.android.library)
    alias(libs.plugins.runique.android.junit5)
}

android {
    namespace = "com.junka.core.android_testing"
}

dependencies {
    implementation(projects.auth.data)
    implementation(projects.core.domain)

    api(projects.core.testing)

    implementation(libs.ktor.client.mock)
    implementation(libs.bundles.ktor)
    implementation(libs.coroutines.test)
}