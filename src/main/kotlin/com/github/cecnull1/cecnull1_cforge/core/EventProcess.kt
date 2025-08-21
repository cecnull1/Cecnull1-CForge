package com.github.cecnull1.cecnull1_cforge.core

import com.github.cecnull1.cecnull1_cforge.core.CForgeEventCore.registerEvents
import com.github.cecnull1.cecnull1_cforge.core.CForgeEventCore.registerFastEvents
import com.github.cecnull1.cecnull1_cforge.core.CForgeEventCore.post
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap
import kotlin.collections.forEach
import kotlin.collections.getOrPut
import kotlin.reflect.KClass

/**
 * 事件总线别名
 */
typealias IEventBus = ConcurrentMap<KClass<out IEvent>, MutableList<(IEvent) -> Unit>>

/**
 * 监听器接口
 *
 * @param isCanceled 事件是否被取消
 * @param isCancelable 事件是否可取消
 * @param isInterruptibleWhenCanceled 事件是否在取消时中断后续监听器的执行
 * */
interface IEvent {
    var isCanceled: Boolean
    val isCancelable: Boolean
    val isInterruptibleWhenCanceled: Boolean
        get() = false
}

/**
 * 简易内置事件总线
 *
 * 在大型业务逻辑中尽量使用自定义 [IEventBus] 处理事件。
 */
object CForgeEventBus {
    inline fun <reified T: IEvent> registerEvents(crossinline block: (T) -> Unit) = cForgeEventMap.registerEvents<T>(block)

    inline fun <reified T: IEvent> registerFastEvents(noinline block: (T) -> Unit) = cForgeEventMap.registerFastEvents<T>(block)

    inline fun IEvent.post() = this.post(cForgeEventMap)

    /*public!*/ val cForgeEventMap: IEventBus = ConcurrentHashMap(16)
}

/**
 * 事件总线核心逻辑
 */
object CForgeEventCore {
    /**
     * 在 [IEventBus] 中注册事件监听器
     *
     * @param block 监听器逻辑
     */
    inline fun <reified T: IEvent> IEventBus.registerEvents(crossinline block: (T) -> Unit) {
        this.getOrPut(T::class) { mutableListOf() }.add {
                event ->
            if (event is T) {
                block(event)
            }
        }
    }

    /**
     * 在 [IEventBus] 中广播触发监听器逻辑
     *
     * @param eventBus 事件总线
     */
    fun IEvent.post(eventBus: IEventBus) {
        // 重置事件状态（确保每次post都是初始状态）
        isCanceled = false

        // 获取当前事件类型对应的监听器列表，若为空则提前返回
        val listeners = eventBus[this::class] ?: return

        // 遍历所有监听器
        for (handler in listeners) {
            // 如果事件已被取消，则中断后续监听器的执行
            if (isCanceled && isInterruptibleWhenCanceled) break

            // 执行监听器逻辑（不捕获异常，由调用方处理）
            handler(this)

            // 在监听器执行后，检查事件取消的合法性
            if (isCanceled && !isCancelable) {
                // 抛出详细异常信息，帮助开发者快速定位问题
                throw kotlin.UnsupportedOperationException(
                    "不可取消事件 '${this::class.simpleName}' 被监听器取消。" +
                            "请检查以下可能：" +
                            "\n1. 该事件应声明为可取消: override val isCancelable = true" +
                            "\n2. 或监听器中不应设置 isCanceled = true"
                )
            }
        }
    }

    /**
     * 在 [IEventBus] 中注册快速事件监听器
     *
     * 调用方需保证类型安全
     *
     * @param block 监听器逻辑
     */
    inline fun <reified T: IEvent> IEventBus.registerFastEvents(noinline block: (T) -> Unit) {
        this.getOrPut(T::class) { mutableListOf() }.add(block as (IEvent) -> Unit)
    }

    /**
     * 快速初始化 [IEventBus]
     *
     * 如果想要自定义初始化逻辑，请用户自行实现。
     *
     * @return [IEventBus]
     */
    fun createEventBus(): IEventBus {
        return ConcurrentHashMap(16)
    }

    /**
     * 销毁 [IEventBus]
     *
     * 允许重复使用 [IEventBus] ，只要它被重新初始化
     */
    fun IEventBus.destroyEventBus() {
        this.forEach {
            this[it.key]?.clear()
        }
        this.clear()
    }
}