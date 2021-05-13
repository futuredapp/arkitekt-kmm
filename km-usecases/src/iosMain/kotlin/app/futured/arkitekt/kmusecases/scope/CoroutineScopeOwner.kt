package app.futured.arkitekt.kmusecases.scope

import kotlinx.coroutines.CoroutineScope

actual interface CoroutineScopeOwner {
    actual val coroutineScope: CoroutineScope
}