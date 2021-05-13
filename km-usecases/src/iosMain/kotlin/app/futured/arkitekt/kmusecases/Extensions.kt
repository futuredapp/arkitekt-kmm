package app.futured.arkitekt.kmusecases

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlin.native.concurrent.SharedImmutable
import kotlin.native.concurrent.freeze

actual fun <T> T.freeze(): T = freeze()

@SharedImmutable
internal actual val workerDispatcher: CoroutineDispatcher = Dispatchers.Default