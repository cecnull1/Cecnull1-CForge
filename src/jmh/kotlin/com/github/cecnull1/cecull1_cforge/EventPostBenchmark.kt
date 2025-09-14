// benchmark/EventBenchmark.kt
package com.github.cecnull1.cecull1_cforge

import com.github.cecnull1.cecnull1_cforge.core.IEvent
import org.openjdk.jmh.annotations.*
import org.openjdk.jmh.infra.Blackhole
import java.util.concurrent.ThreadLocalRandom
import java.util.concurrent.TimeUnit
import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory

// ==============================
// 事件类（同前）
// ==============================

open class TestEvent {
    var isCanceled: Boolean = false
    val isCancelable: Boolean = true
    var value: Int = 0
    var processedCount: Int = 0
}

class CForgeTestEvent : TestEvent(), IEvent

// Fabric 事件类
class FabricTestEvent {
    var value: Int = 0
    var processedCount: Int = 0
    var isCanceled: Boolean = false
    val isCancelable: Boolean = true
}

// ==============================
// Fabric Callback 接口
// ==============================

// 定义 Fabric 事件回调接口
fun interface FabricEventHandler {
    fun handle(event: FabricTestEvent)
}

// 创建 Fabric 事件（使用 EventFactory）
val FABRIC_TEST_EVENT: Event<FabricEventHandler> = EventFactory.createArrayBacked(
    FabricEventHandler::class.java
) { callbacks ->
    FabricEventHandler { event ->
        for (callback in callbacks) {
            callback.handle(event)
            if (event.isCanceled) break // 模拟 interruptible
        }
    }
}

// ==============================
// Fabric Benchmark
// ==============================

@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Fork(1)
@Warmup(iterations = 3, time = 1)
@Measurement(iterations = 5, time = 2)
open class GFabricEventBenchmark {

    private lateinit var fabricEvent: Event<FabricEventHandler>

    @Setup
    fun setup() {
        // ✅ 使用 EventFactory 创建事件
        fabricEvent = FABRIC_TEST_EVENT

        repeat(5_000) { index ->
            fabricEvent.register { event ->
                event.processedCount++
                if (event.value % 3 == 0) {
                    event.isCanceled = true
                }
            }
        }
    }

    @Benchmark
    fun postSingleEvent_FabricEvent(blackhole: Blackhole) {
        val event = FabricTestEvent()
        event.value = ThreadLocalRandom.current().nextInt()
        fabricEvent.invoker().handle(event)
        blackhole.consume(event.isCanceled)
        blackhole.consume(event.processedCount)
    }
}