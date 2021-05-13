package app.futured.arkitekt.kmusecases

import kotlinx.coroutines.CoroutineDispatcher
import kotlin.native.concurrent.SharedImmutable

expect fun <T> T.freeze() : T

@SharedImmutable
internal expect val workerDispatcher: CoroutineDispatcher