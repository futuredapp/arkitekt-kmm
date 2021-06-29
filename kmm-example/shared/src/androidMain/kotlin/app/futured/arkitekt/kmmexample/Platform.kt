package app.futured.arkitekt.kmmexample

actual class Platform actual constructor() {
    actual val platform: String = "Android ${android.os.Build.VERSION.SDK_INT}"
}

actual fun isMainThread(): Boolean {
    return Thread.currentThread().name.contains("main", true)
}
