package app.futured.arkitekt.kmusecases

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

actual fun <T> T.freeze(): T = this

internal actual val workerDispatcher: CoroutineDispatcher = Dispatchers.IO
