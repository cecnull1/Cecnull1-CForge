package com.github.cecnull1.cecnull1_cforge.core

import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap

typealias ICapabilityMap = ConcurrentMap<Any, ICapability>

typealias ICapability = ConcurrentMap<ResourceLocation, Any>

@ExperimentalCapability
object CapabilityCore {
    fun createCapabilityMap(): ICapabilityMap {
        return ConcurrentHashMap(16)
    }

    fun ICapabilityMap.destroyCapabilityMap() {
        this.forEach {
            it.value.destroyCapability()
        }
        this.clear()
    }

    fun createCapability(): ICapability {
        return ConcurrentHashMap(16)
    }

    fun ICapability.destroyCapability() {
        this.clear()
    }

    inline fun <reified T: Any> T.initCapability(capabilityMap: ICapabilityMap): ICapability {
        return capabilityMap[this] ?: createCapability().also { capabilityMap[this] = it }
    }

    inline fun <reified T: Any> T.getCapability(capabilityMap: ICapabilityMap): ICapability? {
        return capabilityMap[this]
    }

    inline fun <reified T: Any> T.getCapabilityOrCreate(capabilityMap: ICapabilityMap): ICapability {
        return this.getCapability<T>(capabilityMap) ?: this.initCapability(capabilityMap)
    }

    inline fun <reified T> ICapability.getData(resourceLocation: ResourceLocation): T? {
        return this[resourceLocation] as? T
    }

    inline fun <reified T> ICapability.setData(resourceLocation: ResourceLocation, value: T) {
        this[resourceLocation] = value
    }

    inline fun <reified T: Any> T.removeCapability(capabilityMap: ICapabilityMap) {
        capabilityMap.remove( this)
    }

    fun ICapability.removeData(resourceLocation: ResourceLocation) {
        this.remove(resourceLocation)
    }
}

@RequiresOptIn("文档都没写好呢……仅经过MVP测验")
annotation class ExperimentalCapability