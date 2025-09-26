package com.github.cecnull1.cecnull1_cforge.core

import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap
import kotlin.reflect.KClass

private typealias RL = ResourceLocation
private typealias Type<T> = KClass<T>
typealias ComponentMap = WeakHashMap<Any, ComponentContainer>
typealias ComponentContainer = ConcurrentMap<Type<out IComponent>, ConcurrentMap<RL, IComponent>>

interface IComponent
object ComponentCore {
    fun createComponentMap(): ComponentMap {
        return WeakHashMap()
    }

    fun destroyComponentMap(componentMap: ComponentMap) {
        componentMap.clear()
    }

    fun Any.initComponent(componentMap: ComponentMap): ComponentMap {
        return componentMap.apply {
            this[this@initComponent] = ConcurrentHashMap()
        }
    }

    fun Any.addComponent(componentMap: ComponentMap, rl: RL, component: IComponent) {
        componentMap.getOrPut(this) { ConcurrentHashMap() }.addComponent(rl, component)
    }

    inline fun <reified T: IComponent> Any.getComponent(componentMap: ComponentMap, rl: RL): T? {
        return componentMap[this]?.getComponent(rl)
    }

    inline fun <reified T : IComponent> Any.hasComponent(
        componentMap: ComponentMap,
        rl: RL
    ): Boolean {
        return componentMap[this]?.hasComponent<T>(rl) == true
    }

    inline fun <reified T : IComponent> Any.removeComponent(
        componentMap: ComponentMap,
        rl: RL
    ): T? {
        return componentMap[this]?.removeComponent(rl)
    }

    fun Any.getComponents(componentMap: ComponentMap): ComponentContainer {
        return componentMap.getOrPut(this) { ConcurrentHashMap() }
    }

    fun ComponentContainer.addComponent(rl: RL, component: IComponent) {
        this.getOrPut(component::class) { ConcurrentHashMap() }[rl] = component
    }

    inline fun <reified T: IComponent> ComponentContainer.getComponent(rl: RL): T? {
        return this[T::class]?.get(rl) as? T
    }

    inline fun <reified T : IComponent> ComponentContainer.hasComponent(
        rl: RL
    ): Boolean {
        return this[T::class]?.containsKey(rl) == true
    }

    inline fun <reified T : IComponent> ComponentContainer.removeComponent(
        rl: RL
    ): T? {
        return this[T::class]?.remove(rl) as? T
    }
}