allprojects {
    group = System.getenv("GITHUB_REPOSITORY")?.split('/')?.first()?.plus(".mpp") ?: "mpp"
    version = System.getenv("GITHUB_REF")?.split('/')?.last() ?: "development"
}