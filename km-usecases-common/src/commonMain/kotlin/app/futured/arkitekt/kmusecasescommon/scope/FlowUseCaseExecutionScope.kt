package app.futured.arkitekt.kmusecasescommon.scope

import app.futured.arkitekt.kmusecasescommon.FlowUseCase
import kotlinx.coroutines.flow.*
import kotlin.coroutines.cancellation.CancellationException

interface FlowUseCaseExecutionScope: CoroutineScopeOwner {
    fun <T : Any?> FlowUseCase<Unit, T>.execute(config: FlowUseCaseConfig.Builder<T>.() -> Unit) =
        execute(Unit, config)

    /**
     * Asynchronously executes use case and consumes data from flow on UI thread.
     * By default all previous pending executions are canceled, this can be changed
     * by [config]. When suspend function in use case finishes, onComplete is called
     * on UI thread. This version is gets initial arguments by [args].
     *
     * In case that an error is thrown during the execution of [FlowUseCase] then
     * [UseCaseErrorHandler.globalOnErrorLogger] is called with the error as an argument.
     *
     * @param args Arguments used for initial use case initialization.
     * @param config [FlowUseCaseConfig] used to process results of internal
     * Flow and to set configuration options.
     **/
    fun <ARGS, T : Any?> FlowUseCase<ARGS, T>.execute(
        args: ARGS,
        config: FlowUseCaseConfig.Builder<T>.() -> Unit
    ) {
        val flowUseCaseConfig = FlowUseCaseConfig.Builder<T>().run {
            config.invoke(this)
            return@run build()
        }

        if (flowUseCaseConfig.disposePrevious) {
            job?.cancel()
        }

        job = build(args)
            .flowOn(getWorkerDispatcher())
            .onStart { flowUseCaseConfig.onStart() }
            .onEach { flowUseCaseConfig.onNext(it) }
            .onCompletion { error ->
                when {
                    error is CancellationException -> {
                        // ignore this exception
                    }
                    error != null -> {
//                        UseCaseErrorHandler.globalOnErrorLogger(error) todo
                        flowUseCaseConfig.onError(error)
                    }
                    else -> flowUseCaseConfig.onComplete()
                }
            }
            .catch { /* handled in onCompletion */ }
            .launchIn(coroutineScope)
    }

    /**
     * Holds references to lambdas and some basic configuration
     * used to process results of Flow use case.
     * Use [FlowUseCaseConfig.Builder] to construct this object.
     */
    class FlowUseCaseConfig<T> private constructor(
        val onStart: () -> Unit,
        val onNext: (T) -> Unit,
        val onError: (Throwable) -> Unit,
        val onComplete: () -> Unit,
        val disposePrevious: Boolean
    ) {
        /**
         * Constructs references to lambdas and some basic configuration
         * used to process results of Flow use case.
         */
        class Builder<T> {
            private var onStart: (() -> Unit)? = null
            private var onNext: ((T) -> Unit)? = null
            private var onError: ((Throwable) -> Unit)? = null
            private var onComplete: (() -> Unit)? = null
            private var disposePrevious = true

            /**
             * Set lambda that is called right before
             * internal Job of Flow is launched.
             * @param onStart Lambda called right before Flow Job is launched.
             */
            fun onStart(onStart: () -> Unit) {
                this.onStart = onStart
            }

            /**
             * Set lambda that is called when internal Flow emits new value
             * @param onNext Lambda called for every new emitted value
             */
            fun onNext(onNext: (T) -> Unit) {
                this.onNext = onNext
            }

            /**
             * Set lambda that is called when some exception on
             * internal Flow occurs
             * @param onError Lambda called when exception occurs
             */
            fun onError(onError: (Throwable) -> Unit) {
                this.onError = onError
            }

            /**
             * Set lambda that is called when internal Flow is completed
             * without errors
             * @param onComplete Lambda called when Flow is completed
             * without errors
             */
            fun onComplete(onComplete: () -> Unit) {
                this.onComplete = onComplete
            }

            /**
             * Set whether currently running Job of internal Flow
             * should be canceled when execute is called repeatedly.
             * @param disposePrevious True if currently running
             * Job of internal Flow should be canceled
             */
            fun disposePrevious(disposePrevious: Boolean) {
                this.disposePrevious = disposePrevious
            }

            fun build(): FlowUseCaseConfig<T> {
                return FlowUseCaseConfig(
                    onStart ?: { },
                    onNext ?: { },
                    onError ?: { throw it },
                    onComplete ?: { },
                    disposePrevious
                )
            }
        }
    }
}
