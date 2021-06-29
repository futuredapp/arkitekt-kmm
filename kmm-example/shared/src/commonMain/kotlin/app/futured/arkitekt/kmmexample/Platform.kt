package app.futured.arkitekt.kmmexample

expect class Platform() {
    val platform: String
}

expect fun isMainThread(): Boolean
