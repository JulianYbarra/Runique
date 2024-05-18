plugins {
    alias(libs.plugins.runique.android.library)
    alias(libs.plugins.runique.jvm.ktor)
}

android {
    namespace = "com.junka.auth.data"
}

dependencies {
    implementation(projects.auth.domain)
    implementation(projects.core.data)
    implementation(projects.core.domain)

    implementation(libs.bundles.koin)
}