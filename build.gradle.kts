val clean by tasks.registering(Delete::class) {
    files(
        buildDir, "ios/app.xcworkspace", "ios/Podfile.lock", "ios/Pods"
    ).let {
        destroyables.register(it)
        delete(it)
    }
}