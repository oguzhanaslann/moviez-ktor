package com.oguzhanaslann.util

fun <T> Result<T>.value(): T = getOrThrow()

fun <T> Result<T>.value(default: T): T = getOrDefault(default)

suspend fun <T> Result<T>.resolve(
    onSuccess: suspend (T) -> Unit,
    onFail: suspend () -> Unit
) {
    when {
        isSuccess -> {
            onSuccess(value())
        }

        isFailure -> {
            onFail()
        }
    }
}

suspend fun <T> Result<T>.resolve(
    onSuccess: suspend () -> Unit,
    onFail: suspend () -> Unit
) {
    when {
        isSuccess -> {
            onSuccess()
        }

        isFailure -> {
            onFail()
        }
    }
}

suspend fun <T, R> Result<T>.combine(
    otherResult: Result<R>,
    onSuccess: suspend () -> Unit,
    onMainResultFail: suspend () -> Unit,
    onOtherResultFail: suspend () -> Unit
) {
    when {
        this.isSuccess && otherResult.isSuccess -> {
            onSuccess()
        }

        isFailure -> {
            onMainResultFail()
        }

        otherResult.isFailure -> {
            onOtherResultFail()
        }
    }
}

suspend fun <T, R> Result<T>.combine(
    otherResult: Result<R>,
    onSuccess: suspend (T) -> Unit,
    onMainResultFail: suspend () -> Unit,
    onOtherResultFail: suspend () -> Unit
) {
    when {
        this.isSuccess && otherResult.isSuccess -> {
            onSuccess(this.value())
        }

        isFailure -> {
            onMainResultFail()
        }

        otherResult.isFailure -> {
            onOtherResultFail()
        }
    }
}

suspend fun <T, R> Result<T>.combine(
    otherResult: Result<R>,
    onSuccess: suspend (T, R) -> Unit,
    onMainResultFail: suspend () -> Unit,
    onOtherResultFail: suspend () -> Unit
) {
    when {
        this.isSuccess && otherResult.isSuccess -> {
            onSuccess(this.value(), otherResult.value())
        }

        isFailure -> {
            onMainResultFail()
        }

        otherResult.isFailure -> {
            onOtherResultFail()
        }
    }
}

suspend fun <T, R> Result<T>.chain(
    resultBuilderBlock: suspend () -> Result<R>,
    onSuccess: suspend () -> Unit,
    onMainResultFail: suspend () -> Unit,
    onOtherResultFail: suspend () -> Unit
) {
    when {
        isSuccess -> {
            val otherResult = resultBuilderBlock()
            otherResult.resolve(
                onSuccess = onSuccess,
                onFail = onOtherResultFail
            )
        }

        isFailure -> {
            onMainResultFail()
        }
    }
}

suspend fun <T, R> Result<T>.chain(
    resultBuilderBlock: suspend () -> Result<R>,
    onSuccess: suspend (T) -> Unit,
    onMainResultFail: suspend () -> Unit,
    onOtherResultFail: suspend () -> Unit
) {

    val block: suspend () -> Unit = {
        onSuccess(this.value())
    }

    when {
        isSuccess -> {
            val otherResult = resultBuilderBlock()
            otherResult.resolve(
                onSuccess = block,
                onFail = onOtherResultFail
            )
        }

        isFailure -> {
            onMainResultFail()
        }
    }
}

suspend fun <T, R> Result<T>.chain(
    resultBuilderBlock: suspend () -> Result<R>,
    onSuccess: suspend (T, R) -> Unit,
    onMainResultFail: suspend () -> Unit,
    onOtherResultFail: suspend () -> Unit
) {
    when {
        isSuccess -> {
            val otherResult = resultBuilderBlock()
            otherResult.resolve(
                onSuccess = { result ->
                    onSuccess(this.value(), result)
                },
                onFail = onOtherResultFail
            )
        }

        isFailure -> {
            onMainResultFail()
        }
    }
}

suspend fun <T, R> Result<T>.chainWithPredicate(
    predicate: Boolean,
    resultBuilderBlock: suspend () -> Result<R>,
    onSuccess: suspend () -> Unit,
    onPredicateFalse: suspend () -> Unit,
    onMainResultFail: suspend () -> Unit,
    onOtherResultFail: suspend () -> Unit
) {


    when {
        isSuccess && predicate -> {
            val otherResult = resultBuilderBlock()
            otherResult.resolve(
                onSuccess = onSuccess,
                onFail = onOtherResultFail
            )
        }

        isFailure -> {
            onMainResultFail()
        }

        !predicate -> onPredicateFalse()
    }
}

suspend fun <T, R> Result<T>.chainWithPredicate(
    predicate: Boolean,
    resultBuilderBlock: suspend () -> Result<R>,
    onSuccess: suspend (T) -> Unit,
    onPredicateFalse: suspend () -> Unit,
    onMainResultFail: suspend () -> Unit,
    onOtherResultFail: suspend () -> Unit
) {
    val block: suspend () -> Unit = {
        onSuccess(this.value())
    }

    when {
        isSuccess && predicate -> {
            val otherResult = resultBuilderBlock()
            otherResult.resolve(
                onSuccess = block,
                onFail = onOtherResultFail
            )
        }

        isFailure -> {
            onMainResultFail()
        }

        !predicate -> onPredicateFalse()
    }
}

suspend fun <T, R> Result<T>.chainWithPredicate(
    predicate: Boolean,
    resultBuilderBlock: suspend () -> Result<R>,
    onSuccess: suspend (T, R) -> Unit,
    onPredicateFalse: suspend () -> Unit,
    onMainResultFail: suspend () -> Unit,
    onOtherResultFail: suspend () -> Unit
) {
    when {
        isSuccess && predicate -> {
            val otherResult = resultBuilderBlock()
            otherResult.resolve(
                onSuccess = { result ->
                    onSuccess(this.value(), result)
                },
                onFail = onOtherResultFail
            )
        }

        isFailure -> {
            onMainResultFail()
        }

        !predicate -> onPredicateFalse()
    }
}

suspend fun <R> Result<Boolean>.chainBySelfPredicate(
    reversePredicate : Boolean = false,
    resultBuilderBlock: suspend () -> Result<R>,
    onSuccess: suspend (R) -> Unit,
    onPredicateFalse: suspend () -> Unit,
    onMainResultFail: suspend () -> Unit,
    onOtherResultFail: suspend () -> Unit
) {
    val predicate = getOrDefault(false).xor(reversePredicate)

    when {
        isSuccess && predicate -> {
            val otherResult = resultBuilderBlock()

            otherResult.resolve(
                onSuccess = { result ->
                    onSuccess(result)
                },
                onFail = onOtherResultFail
            )
        }

        isFailure -> onMainResultFail()

        !predicate -> onPredicateFalse()

    }
}


suspend fun <R> Result<Boolean>.chainBySelfPredicate(
    reversePredicate : Boolean = false,
    resultBuilderBlock: suspend () -> Result<R>,
    onSuccess: suspend () -> Unit,
    onPredicateFalse: suspend () -> Unit,
    onMainResultFail: suspend () -> Unit,
    onOtherResultFail: suspend () -> Unit
) {

    val predicate = getOrDefault(false).xor(reversePredicate)

    when {
        isSuccess && predicate -> {
            val otherResult = resultBuilderBlock()
            otherResult.resolve(
                onSuccess = onSuccess,
                onFail = onOtherResultFail
            )
        }

        isFailure -> {
            onMainResultFail()
        }

        !predicate -> onPredicateFalse()
    }
}
