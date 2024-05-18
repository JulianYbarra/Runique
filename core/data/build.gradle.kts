plugins {
    alias(libs.plugins.runique.android.library)
    alias(libs.plugins.runique.jvm.ktor)
}

android {
    namespace = "com.junka.core.data"
}

dependencies {
    implementation(projects.core.domain)
    implementation(projects.core.database)

    // Timber
    implementation(libs.timber)

    implementation(libs.bundles.koin)
}