package com.github.cecnull1.cecnull1_cforge.event

import com.github.cecnull1.cecnull1_cforge.core.IEvent
import net.minecraft.entity.Entity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.screen.slot.Slot
import net.minecraft.world.World

interface IEntityEvent {
    val entity: Entity
}

interface IWorldEvent {
    val world: World
}

class SlotTakeOffEvent(override val entity: PlayerEntity, override val world: World, val slot: Slot, val item: ItemStack) :
    IEvent,
    IEntityEvent,
    IWorldEvent {
    override var isCanceled = false
    override val isCancelable = true
}

class EntityMountEvent(override val entity: Entity, override val world: World, val entityBeingMounted: Entity?, val isMounting: Boolean) :
    IEvent,
    IEntityEvent,
    IWorldEvent {
    override var isCanceled = false
    override val isCancelable = true
}