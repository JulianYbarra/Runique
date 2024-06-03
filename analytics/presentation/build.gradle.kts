plugins {
    alias(libs.plugins.runique.android.feature.ui)
}

android {
    namespace = "com.junka.analytics.presentation"
}

dependencies {

    implementation(projects.analytics.domain)
}