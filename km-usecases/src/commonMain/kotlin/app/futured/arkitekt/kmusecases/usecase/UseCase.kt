package app.futured.arkitekt.kmusecases.usecase

import app.futured.arkitekt.kmusecases.scope.CoroutineScopeOwner
import kotlinx.coroutines.*

abstract class UseCase<Arg, ReturnType> {

    var job: Job? = null

    abstract suspend fun build(arg: Arg): ReturnType

    // because of iOS, not accessible from Android
    // on iOS will be compiled to UC.execute(scope: Scope, arg...
    fun CoroutineScopeOwner.execute(
        arg: Arg,
        config: UseCaseConfig.Builder<ReturnType>.() -> Unit
    ) {
        val useCaseConfig = UseCaseConfig.Builder<ReturnType>().run {
            config(this)
            return@run build()
        }
        if (useCaseConfig.disposePrevious) {
            job?.cancel()
        }
        useCaseConfig.onStart()
        job = getJob(arg, useCaseConfig.onSuccess, useCaseConfig.onError)
    }

    private fun CoroutineScopeOwner.getJob(
        arg: Arg,
        onSuccess: (ReturnType) -> Unit,
        onError: (Throwable) -> Unit
    ): Job = coroutineScope.async { buildOnBg(arg, getWorkerDispatcher()) }
        .also {
            coroutineScope.launch {
                kotlin.runCatching { it.await() }
                    .fold(onSuccess, onError)
            }
        }

    private suspend fun buildOnBg(arg: Arg, workerDispatcher: CoroutineDispatcher) = withContext(workerDispatcher) {
        this@UseCase.build(arg)
    }

    /**
     * Holds references to lambdas and some basic configuration
     * used to process results of Coroutine use case.
     * Use [UseCaseConfig.Builder] to construct this object.
     */
    class UseCaseConfig<T> private constructor(
        val onStart: () -> Unit,
        val onSuccess: (T) -> Unit,
        val onError: (Throwable) -> Unit,
        val disposePrevious: Boolean
    ) {
        /**
         * Constructs references to lambdas and some basic configuration
         * used to process results of Coroutine use case.
         */
        class Builder<T> {
            private var onStart: (() -> Unit)? = null
            private var onSuccess: ((T) -> Unit)? = null
            private var onError: ((Throwable) -> Unit)? = null
            private var disposePrevious = true

            /**
             * Set lambda that is called right before
             * the internal Coroutine is created
             * @param onStart Lambda called right before Coroutine is
             * created
             */
            fun onStart(onStart: () -> Unit) {
                this.onStart = onStart
            }

            /**
             * Set lambda that is called when internal Coroutine
             * finished without exceptions
             * @param onSuccess Lambda called when Coroutine finished
             */
            fun onSuccess(onSuccess: (T) -> Unit) {
                this.onSuccess = onSuccess
            }

            /**
             * Set lambda that is called when exception on
             * internal Coroutine occurs
             * @param onError Lambda called when exception occurs
             */
            fun onError(onError: (Throwable) -> Unit) {
                this.onError = onError
            }

            /**
             * Set whether currently active Job of internal Coroutine
             * should be canceled when execute is called repeatedly.
             * Default value is true.
             * @param disposePrevious True if active Job of internal
             * Coroutine should be canceled. Default value is true.
             */
            fun disposePrevious(disposePrevious: Boolean) {
                this.disposePrevious = disposePrevious
            }

            internal fun build(): UseCaseConfig<T> {
                return UseCaseConfig(
                    onStart ?: { },
                    onSuccess ?: { },
                    onError ?: { throw it },
                    disposePrevious
                )
            }
        }
    }
}
