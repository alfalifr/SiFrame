package sidev.lib.gradle

fun Project.android(configure: BaseAppModuleExtension.() -> Unit) =
    extensions.configure("android", configure)
