import org.gradle.api.Project

fun Project.intProperty(propName: String) = Integer.parseInt(property(propName) as String)

inline val Project.compileSdk: Int get() = intProperty("android.sdk.compile")
inline val Project.minSdk: Int get() = intProperty("android.sdk.min")
inline val Project.targetSdk: Int get() = intProperty("android.sdk.target")

fun Project.androidx(library: String) =
    "androidx.$library:$library:${property("androidx.$library")}"

