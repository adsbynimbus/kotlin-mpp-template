allprojects {
    group = System.getenv("GITHUB_REPOSITORY")?.split('/')?.first()?.plus(".mpp") ?: "mpp"
    version = System.getenv("GITHUB_REF")?.split('/')?.last() ?: "1.0-development"
}

val clean by tasks.registering(Delete::class) {
    files(
        buildDir, "$xcodeproj/app.xcworkspace", "$xcodeproj/Podfile.lock", "$xcodeproj/Pods"
    ).let {
        destroyables.register(it)
        delete(it)
    }
}