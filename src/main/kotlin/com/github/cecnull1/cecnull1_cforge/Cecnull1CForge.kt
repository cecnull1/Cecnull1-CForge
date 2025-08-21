package com.github.cecnull1.cecnull1_cforge

import com.github.cecnull1.cecnull1_cforge.core.CapabilityCore.createCapabilityMap
import com.github.cecnull1.cecnull1_cforge.core.CapabilityCore.destroyCapabilityMap
import com.github.cecnull1.cecnull1_cforge.core.CapabilityCore.getCapability
import com.github.cecnull1.cecnull1_cforge.core.CapabilityCore.getCapabilityOrCreate
import com.github.cecnull1.cecnull1_cforge.core.CapabilityCore.getData
import com.github.cecnull1.cecnull1_cforge.core.CapabilityCore.initCapability
import com.github.cecnull1.cecnull1_cforge.core.CapabilityCore.removeCapability
import com.github.cecnull1.cecnull1_cforge.core.CapabilityCore.removeData
import com.github.cecnull1.cecnull1_cforge.core.CapabilityCore.setData
import com.github.cecnull1.cecnull1_cforge.core.ExperimentalCapability
import com.github.cecnull1.cecnull1_cforge.core.ICapabilityMap
import com.github.cecnull1.cecnull1_cforge.core.ResourceLocation
import com.mojang.logging.LogUtils.getLogger
import net.fabricmc.api.ModInitializer
import org.slf4j.Logger

class Cecnull1CForge : ModInitializer {
    val logger: Logger = getLogger()
    override fun onInitialize() {
    }
}


@OptIn(ExperimentalCapability::class)
fun main() {
    val c: ICapabilityMap = createCapabilityMap()
    val nums = ArrayList<Int>()
    nums.add(123)
    nums.add(753)
    f(nums,c)
    println(nums.getCapability(c)?.getData<Int>(ResourceLocation.fromString("s:e")))
    println(c)
    nums.getCapability(c)?.removeData(ResourceLocation.fromString("s:e"))
    println(c)
    nums.removeCapability(c)
    println(c)
    println(nums)
    c.destroyCapabilityMap()
}

@OptIn(ExperimentalCapability::class)
fun f(nums: ArrayList<Int>, c: ICapabilityMap) {
    nums.initCapability(c).setData<Int>(ResourceLocation.fromString("s:e"), 123)
}