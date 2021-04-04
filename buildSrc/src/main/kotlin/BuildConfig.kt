import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.kotlin.dsl.invoke

val defaultGroup = GithubRepo.name?.split('/')?.first()?.plus(".mpp") ?: "mpp"
val defaultVersion = GithubRepo.ref?.split('/')?.last() ?: "1.0-development"

object Android {
    const val compileSdk = 30
    const val targetSdk = 30
    const val minSdk = 21
}

object GithubRepo {
    val name: String? = System.getenv("GITHUB_REPOSITORY")
    val path: String = "https://www.github.com/$name"
    val packages: String = "https://maven.pkg.github.com/$name"
    val ref: String? = System.getenv("GITHUB_REF")
}

object Ios {
    const val deployTarget = "13.5"
    const val podfile =  "ios/Podfile"
}

object Jvm {
    val target = JavaVersion.VERSION_1_8
}

/**
 * Fixes a breaking conflict in AGP 7.0.0-alpha03 and Kotlin MPP
 */
fun Project.enableCompat() {
    configurations {
        maybeCreate("androidTestApi")
        maybeCreate("androidTestDebugApi")
        maybeCreate("androidTestReleaseApi")
        maybeCreate("testApi")
        maybeCreate("testDebugApi")
        maybeCreate("testReleaseApi")
    }
}