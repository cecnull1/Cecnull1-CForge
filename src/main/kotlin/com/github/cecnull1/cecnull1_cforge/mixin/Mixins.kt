package com.github.cecnull1.cecnull1_cforge.mixin

import com.github.cecnull1.cecnull1_cforge.core.CForgeEventBus.post
import com.github.cecnull1.cecnull1_cforge.event.EntityMountEvent
import com.github.cecnull1.cecnull1_cforge.event.SlotTakeOffEvent
import net.minecraft.entity.Entity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.screen.slot.Slot
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable


@Mixin(targets = ["net.minecraft.screen.PlayerScreenHandler$1"])
abstract class PlayerScreenHandlerSlotMixin {
    @Inject(
        method = ["canTakeItems(Lnet/minecraft/entity/player/PlayerEntity;)Z"],
        at = [At("HEAD")],
        cancellable = true
    )
    private fun onCanTakeItems(player: PlayerEntity, cir: CallbackInfoReturnable<Boolean>) {
        // 1. 获取当前 Slot 实例
        val slot = this as Any as Slot


        // 2. 获取当前槽位的物品栈
        val stack = slot.stack


        // 3. 创建自定义事件（模拟Forge的TakeOffEvent）
        val event = SlotTakeOffEvent(player, player.entityWorld, slot, stack)

        // 4. 通过事件总线分发事件
        event.post()

        // 6. 处理取消情况
        if (event.isCanceled) {
            cir.returnValue = false
            cir.cancel()
        }
    }
}

@Mixin(Entity::class)
open class EntityMixin {
    @Inject(method = ["stopRiding"], at = [At("HEAD")], cancellable = true)
    private fun onStopRiding(callback: CallbackInfo) {
        val entity = this as Entity

        // 创建自定义事件（模拟Forge的EntityMountEvent）
        val event = EntityMountEvent(entity, entity.entityWorld, entity.vehicle, false)

        // 通过事件总线分发事件
        event.post()

        // 处理取消情况
        if (event.isCanceled) {
            callback.cancel()
        }
    }

    @Inject(method = ["startRiding"], at = [At("HEAD")], cancellable = true)
    private fun onStartRiding(entity: Entity, callback: CallbackInfoReturnable<Boolean>) {
        val selfEntity = this as Entity
        val event = EntityMountEvent(selfEntity, selfEntity.entityWorld, entity, true)
        event.post()
        if (event.isCanceled) {
            callback.returnValue = false
            callback.cancel()
        }
    }
}