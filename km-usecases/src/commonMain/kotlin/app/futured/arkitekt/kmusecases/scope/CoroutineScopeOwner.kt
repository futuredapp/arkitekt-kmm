package app.futured.arkitekt.kmusecases.scope

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope

expect interface CoroutineScopeOwner {
    val coroutineScope: CoroutineScope

    /**
     * Provides Dispatcher for background tasks. This may be overridden for testing purposes
     */
    open fun getWorkerDispatcher(): CoroutineDispatcher
}
