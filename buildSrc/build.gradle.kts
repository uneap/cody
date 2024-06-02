plugins {
    `kotlin-dsl`
}

dependencies {
    implementation(plugin(libs.plugins.spring.boot))
}

fun plugin(notation: Provider<PluginDependency>): String {
    return notation.map {
        "${it.pluginId}:${it.pluginId}.gradle.plugin:${it.version}"
    }.get()
}
