package com.github.cecnull1.cecnull1_cforge.core

import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap

typealias ICapabilityMap = ConcurrentMap<Any, ICapability>

typealias ICapability = ConcurrentMap<ResourceLocation, Any>

@ExperimentalCapability
object CapabilityCore {
    /**
     * 快速创建能力表数据结构
     *
     * @return [ICapabilityMap] 能力表数据结构
     */
    fun createCapabilityMap(): ICapabilityMap {
        return ConcurrentHashMap(16)
    }

    /**
     * 销毁能力表数据结构
     */
    fun ICapabilityMap.destroyCapabilityMap() {
        this.forEach {
            it.value.destroyCapability()
        }
        this.clear()
    }

    /**
     * 快速创建能力
     *
     * @return [ICapability] 能力
     */
    fun createCapability(): ICapability {
        return ConcurrentHashMap(16)
    }

    /**
     * 销毁能力
     */
    fun ICapability.destroyCapability() {
        this.clear()
    }

    /**
     * 对一个对象初始化能力
     *
     * @param capabilityMap 能力表数据结构
     * @return [ICapability] 能力
     */
    inline fun <reified T: Any> T.initCapability(capabilityMap: ICapabilityMap): ICapability {
        return capabilityMap[this] ?: createCapability().also { capabilityMap[this] = it }
    }

    /**
     * 获取一个对象的能力
     *
     * @param capabilityMap 能力表数据结构
     * @return [ICapability]? 能力
     */
    inline fun <reified T: Any> T.getCapability(capabilityMap: ICapabilityMap): ICapability? {
        return capabilityMap[this]
    }

    /**
     * 获取一个对象的能力
     *
     * 如果不存在的话，则创建一个能力
     *
     * @param capabilityMap 能力表数据结构
     * @return [ICapability] 能力
     */
    inline fun <reified T: Any> T.getCapabilityOrCreate(capabilityMap: ICapabilityMap): ICapability {
        return this.getCapability<T>(capabilityMap) ?: this.initCapability(capabilityMap)
    }

    /**
     * 获取能力中的数据。如果不存在或者类型不匹配，则返回 null
     *
     * @param resourceLocation 能力中的数据资源位置
     * @return [T]? 能力中的数据
     */
    inline fun <reified T> ICapability.getData(resourceLocation: ResourceLocation): T? {
        return this[resourceLocation] as? T
    }

    /**
     * 设置能力中的数据。
     *
     * @param resourceLocation 能力中的数据资源位置
     * @param value 要写入的值
     */
    inline fun <reified T> ICapability.setData(resourceLocation: ResourceLocation, value: T) {
        this[resourceLocation] = value
    }

    /**
     * 删除一个对象的能力
     *
     * @param capabilityMap 能力表数据结构
     */
    inline fun <reified T: Any> T.removeCapability(capabilityMap: ICapabilityMap) {
        capabilityMap[this]?.destroyCapability()
        capabilityMap.remove( this)
    }

    /**
     * 删除能力中的数据
     */
    fun ICapability.removeData(resourceLocation: ResourceLocation) {
        this.remove(resourceLocation)
    }
}

@RequiresOptIn("应该马上可用了吧，但是目前并未进行深度测试。")
annotation class ExperimentalCapability