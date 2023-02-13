package app.futured.arkitekt.kmusecasescommon.scope

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

interface CoroutineScopeOwner {
    /**
     * [CoroutineScope] scope used to execute coroutine based use cases. It is your responsibility to cancel it when all running
     * tasks should be stopped
     */
    val coroutineScope: CoroutineScope

    /**
     * Provides Dispatcher for background tasks. This may be overridden for testing purposes
     */
    fun getWorkerDispatcher() = Dispatchers.Default
}

