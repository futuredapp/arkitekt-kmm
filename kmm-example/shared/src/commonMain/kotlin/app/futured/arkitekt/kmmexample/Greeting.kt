package app.futured.arkitekt.kmmexample

class Greeting {
    fun greeting(): String {
        return "Hello, ${Platform().platform}!"
    }
}