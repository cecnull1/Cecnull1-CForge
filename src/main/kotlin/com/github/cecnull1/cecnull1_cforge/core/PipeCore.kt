package com.github.cecnull1.cecnull1_cforge.core

import com.github.cecnull1.cecnull1_cforge.core.PipeCore.process
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

@OptIn(ExperimentalContracts::class)
object PipeCore {

    inline infix fun <reified T, reified R> T.calc(block: T.() -> R): R {
        contract {
            callsInPlace(block, kotlin.contracts.InvocationKind.EXACTLY_ONCE)
            returnsNotNull()
        }
        return this.block()
    }

    inline infix fun <reified R, reified T> R.transform(block: R.() -> T): T {
        contract {
            callsInPlace(block, kotlin.contracts.InvocationKind.EXACTLY_ONCE)
            returnsNotNull()
        }
        return this.block()
    }

    inline infix fun <reified R, reified T> R.mutate(block: R.() -> T): T {
        contract {
            callsInPlace(block, kotlin.contracts.InvocationKind.EXACTLY_ONCE)
            returnsNotNull()
        }
        return this.block()
    }

    inline infix fun <reified R, reified T> R.pipe(block: R.() -> T): T {
        contract {
            callsInPlace(block, kotlin.contracts.InvocationKind.EXACTLY_ONCE)
            returnsNotNull()
        }
        return this.block()
    }

    inline infix fun <reified R> R.process(block: R.() -> Unit): R {
        contract {
            callsInPlace(block, kotlin.contracts.InvocationKind.EXACTLY_ONCE)
            returnsNotNull()
        }
        this.block()
        return this
    }

    inline fun process(block: Unit.() -> Unit) {
        contract {
            callsInPlace(block, kotlin.contracts.InvocationKind.EXACTLY_ONCE)
        }
        Unit.block()
    }

    inline fun <reified R> calc(block: Unit.() -> R): R {
        contract {
            callsInPlace(block, kotlin.contracts.InvocationKind.EXACTLY_ONCE)
        }
        return Unit.block()
    }

    inline fun <reified R> transform(block: Unit.() -> R): R {
        contract {
            callsInPlace(block, kotlin.contracts.InvocationKind.EXACTLY_ONCE)
        }
        return Unit.block()
    }

    inline fun <reified R> mutate(block: Unit.() -> R): R {
        contract {
            callsInPlace(block, kotlin.contracts.InvocationKind.EXACTLY_ONCE)
        }
        return Unit.block()
    }

    inline fun <reified R> pipe(block: Unit.() -> R): R {
        contract {
            callsInPlace(block, kotlin.contracts.InvocationKind.EXACTLY_ONCE)
        }
        return Unit.block()
    }

    inline infix operator fun <reified R, reified T> T.invoke(block: T.() -> R): R {
        return this.block()
    }

    inline operator fun <reified R, reified T, reified U> T.invoke(data: U, block: T.(U) -> R): R {
        return this.block(data)
    }
}

@OptIn(ExperimentalContracts::class)
object PipeCtrl {
    inline infix fun <reified R> R.pipeIf(block: R.() -> Boolean): R? {
        contract {
            callsInPlace(block, kotlin.contracts.InvocationKind.EXACTLY_ONCE)
        }
        return if (this.block()) this else null
    }

    inline infix fun <reified R> R.pipeIf(bool: Boolean): R? = if (bool) this else null

    inline infix fun <reified R> R.pipeUnless(block: R.() -> Boolean): R? {
        contract {
            callsInPlace(block, kotlin.contracts.InvocationKind.EXACTLY_ONCE)
        }
        return if (!this.block()) this else null
    }

    inline infix fun <reified R> R.pipeUnless(bool: Boolean): R? = if (!bool) this else null

    inline infix fun <reified R> R?.orElseProcess(block: Unit.() -> Unit): R? {
        contract {
            callsInPlace(block, kotlin.contracts.InvocationKind.AT_MOST_ONCE)
        }
        this ?: Unit.block()
        return this
    }

    inline infix fun <reified T> T?.orElseCalc(block: Unit.() -> T): T {
        contract {
            callsInPlace(block, kotlin.contracts.InvocationKind.AT_MOST_ONCE)
        }

        return this ?: Unit.block()
    }

    inline infix fun <reified T> T?.orElse(block: Unit.() -> T): T {
        contract {
            callsInPlace(block, kotlin.contracts.InvocationKind.AT_MOST_ONCE)
        }

        return this.orElseCalc(block)
    }

    inline infix fun <reified T> T?.then(block: T.() -> Unit): T? {
        contract {
            callsInPlace(block, kotlin.contracts.InvocationKind.AT_MOST_ONCE)
        }
        this?.let {
            this.block()
        }
        return this
    }
}