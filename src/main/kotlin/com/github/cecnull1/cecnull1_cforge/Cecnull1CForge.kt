@file:OptIn(ExperimentalCapability::class)

package com.github.cecnull1.cecnull1_cforge

import com.github.cecnull1.cecnull1_cforge.core.CapabilityCore
import com.github.cecnull1.cecnull1_cforge.core.ExperimentalCapability
import com.mojang.logging.LogUtils.getLogger
import net.fabricmc.api.ModInitializer
import org.slf4j.Logger

class Cecnull1CForge : ModInitializer {
    val entityCapability = CapabilityCore.createCapabilityMap()

    override fun onInitialize() {
    }
}
val logger: Logger = getLogger()