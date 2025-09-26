// benchmark/EventBenchmark.kt
package com.github.cecnull1.cecull1_cforge

import com.github.cecnull1.cecnull1_cforge.core.CForgeEventBus
import com.github.cecnull1.cecnull1_cforge.core.CForgeEventCore
import com.github.cecnull1.cecnull1_cforge.core.CForgeEventCore.post
import com.github.cecnull1.cecnull1_cforge.core.CForgeEventCore.registerEvents
import net.minecraftforge.eventbus.api.BusBuilder
import net.minecraftforge.eventbus.api.Cancelable
import net.minecraftforge.eventbus.api.Event
import org.openjdk.jmh.annotations.*
import org.openjdk.jmh.infra.Blackhole
import java.util.concurrent.ThreadLocalRandom
import java.util.concurrent.TimeUnit

// ==============================
// CForge Benchmark
// ==============================

@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Fork(1)
@Warmup(iterations = 3, time = 1)
@Measurement(iterations = 5, time = 2)
open class CForgeBenchmark {

    private val cforgeSafeBus = CForgeEventCore.createEventBus()

    @Setup
    fun setup() {

        repeat(1_000_000) {
            CForgeEventBus.registerFastEvents<CForgeTestEvent> { event ->
                event.processedCount++
                if (event.value % 3 == 0) event.isCanceled = true // 用 3 增加不确定性
            }
            cforgeSafeBus.registerEvents<CForgeTestEvent> { event ->
                event.processedCount++
                if (event.value % 3 == 0) event.isCanceled = true
            }
        }
    }

    @Benchmark
    fun postSingleEvent_FastEvents(blackhole: Blackhole) {
        val event = CForgeTestEvent()
        // 🔥 引入随机性：让 JVM 无法预测 event.value
        event.value = ThreadLocalRandom.current().nextInt()
        event.post(CForgeEventBus.cForgeEventMap)
        // 🔥 消费所有状态：防止 DCE
        blackhole.consume(event.isCanceled)
        blackhole.consume(event.processedCount)
    }

    @Benchmark
    fun postSingleEvent_SafeEvents(blackhole: Blackhole) {
        val event = CForgeTestEvent()
        event.value = ThreadLocalRandom.current().nextInt()
        event.post(
            cforgeSafeBus)
        blackhole.consume(event.isCanceled)
        blackhole.consume(event.processedCount)
    }
}

@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Fork(1)
@Warmup(iterations = 3, time = 1)
@Measurement(iterations = 5, time = 2)
open class ForgeBenchmark {

    private val forgeBus = BusBuilder.builder().build()

    @Setup
    fun setup() {
        repeat(1_000_000) {
            forgeBus.addListener<ForgeTestEvent> { event ->
                event.processedCount++
                if (event.value % 3 == 0) event.isCanceled = true
            }
        }
    }

    @Benchmark
    fun postSingleEvent_Events(blackhole: Blackhole) {
        val event = ForgeTestEvent()
        forgeBus.post(event)
        blackhole.consume(event.isCanceled)
        blackhole.consume(event.processedCount)
    }
}

@Cancelable
class ForgeTestEvent : Event() {
    var value: Int = 0
    var processedCount: Int = 0
}