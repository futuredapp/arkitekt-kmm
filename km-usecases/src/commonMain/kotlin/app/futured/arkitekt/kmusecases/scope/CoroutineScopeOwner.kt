package app.futured.arkitekt.kmusecases.scope

import kotlinx.coroutines.CoroutineScope

expect interface CoroutineScopeOwner {
    val coroutineScope: CoroutineScope
}