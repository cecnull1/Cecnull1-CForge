package com.github.cecnull1.cecnull1_cforge.core

import com.github.cecnull1.cecnull1_cforge.core.CForgeEventCore.post
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap
import kotlin.collections.set
import kotlin.to

/**
 * 注册表容器
 */
typealias IRegistry<T> = Pair<ResourceLocation, IAnonymousRegistry<T>>
/**
 * 匿名注册表容器
 */
typealias IAnonymousRegistry<T> = ConcurrentMap<ResourceLocation, T>

/**
 * 注册表核心逻辑
 */
@ExperimentalRegistry
object RegistryCore {
    /**
     * 快速创建一个注册表
     *
     * 如果要自定义创建注册表，请用户手动声明并创建
     *
     * @return [IRegistry]
     */
    inline fun <reified T> createRegistry(resourceLocation: ResourceLocation): IRegistry<T> {
        return Pair(resourceLocation, ConcurrentHashMap(16))
    }

    /**
     * 快速创建一个匿名注册表
     *
     * 如果要自定义创建匿名注册表，请用户手动声明并创建
     *
     * @return [IAnonymousRegistry]
     */
    inline fun <reified T> createAnonymousRegistry(): IAnonymousRegistry<T> {
        return ConcurrentHashMap(16)
    }

    /**
     * 在 [IRegistry] 注册一个元素
     *
     * 可以用内置的集合方法 [get] 访问资源相应的元素
     *
     * @param resourceLocation 资源位置
     * @param value 元素
     * @return [Pair] 注册后的元素
     */
    inline fun <reified T> IRegistry<T>.register(resourceLocation: ResourceLocation, value: T): Pair<ResourceLocation, T> {
        return this.second.register(resourceLocation, value)
    }

    /**
     * 在 [IAnonymousRegistry] 注册一个元素
     *
     * 可以用内置的集合方法 [get] 访问资源相应的元素
     *
     * @param resourceLocation 资源位置
     * @param value 元素
     * @return [Pair] 注册后的元素
     */
    inline fun <reified T> IAnonymousRegistry<T>.register(resourceLocation: ResourceLocation, value: T): Pair<ResourceLocation, T> {
        this[resourceLocation] = value
        return resourceLocation to value
    }

    /**
     * 在 [IRegistry] 注册一个元素
     *
     * 使用 [Pair] 直观传递参数
     *
     * @param entry [Pair]
     * @return [Pair] 注册后的元素
     */
    inline fun <reified T> IRegistry<T>.register(entry: Pair<ResourceLocation, T>): Pair<ResourceLocation, T> {
        return this.second.register(entry)
    }

    /**
     * 在 [IAnonymousRegistry] 注册一个元素
     *
     * 使用 [Pair] 直观传递参数
     *
     * @param entry [Pair]
     * @return [Pair] 注册后的元素
     */
    inline fun <reified T> IAnonymousRegistry<T>.register(entry: Pair<ResourceLocation, T>): Pair<ResourceLocation, T> {
        this.register(entry.first, entry.second)
        return entry
    }

    /**
     * 在 [IRegistry] 注册多个元素
     *
     * @param entries 每个参数都是 [Pair] 用于注册每一个元素
     * @return [Array] 注册后的多种元素
     */
    inline fun <reified T> IRegistry<T>.registers(vararg entries: Pair<ResourceLocation, T>): Array<out Pair<ResourceLocation, T>> {
        return this.second.registers(*entries)
    }

    /**
     * 在 [IAnonymousRegistry] 注册多个元素
     *
     * @param entries 每个参数都是 [Pair] 用于注册每一个元素
     * @return [Array] 注册后的多种元素
     */
    inline fun <reified T> IAnonymousRegistry<T>.registers(vararg entries: Pair<ResourceLocation, T>): Array<out Pair<ResourceLocation, T>> {
        for (entry in entries) {
            register(entry)
        }
        return entries
    }

    inline operator fun <reified T> IRegistry<T>.get(resourceLocation: ResourceLocation): T? {
        return this.second[resourceLocation]
    }
}

@ExperimentalRegistry
object RegistryEvent {
    /**
     * 将 [IRegistry] 注册到 [IEventBus]
     */
    inline fun <reified T> IRegistry<T>.registerToEventBus(eventBus: IEventBus) {
        RegistryRegisterEvent(this).post(eventBus)
    }

    /**
     * 注册表最终注册事件
     *
     * @param registry 注册表
     */
    data class RegistryRegisterEvent<T>(val registry: IRegistry<T>) : IEvent {
        override val isCancelable: Boolean = false
        override var isCanceled: Boolean = false
    }
}

@RequiresOptIn("目前注册表系统思路还没设定好，将来可能会尝试其他方案，甚至破坏性变更方案。\n如果现在使用此系统而不做好准备，将来可能会引发不可预知的问题，请谨慎对待。")
annotation class ExperimentalRegistry
