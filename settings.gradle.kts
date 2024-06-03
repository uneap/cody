rootProject.name = "cody"

pluginManagement {
    repositories {
        gradlePluginPortal()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.6.0"
}
module(name = ":frontend", "frontend")

module(name = ":common", "common")
module(name = ":common:core", "common/core")

module(name = ":backend", "backend")
module(name = ":backend:bff", "backend/bff")
module(name = ":backend:cache-stream", "backend/cache-stream")
module(name = ":backend:display", "backend/display")
module(name = ":backend:storage", "backend/storage")
module(name = ":backend:full-cache-batch", "backend/full-cache-batch")

module(name = ":domain", "domain")
module(name = ":domain:store", "domain/store")

module(name = ":resource", "resource")
module(name = ":resource:kafka", "resource/kafka")
module(name = ":resource:redis", "resource/redis")
module(name = ":resource:db", "resource/db")
dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

fun module(name: String, path: String) {
    include(name)
    project(name).projectDir = file(path)
}