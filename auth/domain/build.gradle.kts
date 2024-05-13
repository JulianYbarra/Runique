plugins {
    alias(libs.plugins.runique.jvm.library)
    alias(libs.plugins.runique.jvm.ktor)
}

dependencies {
    implementation(projects.core.domain)
}