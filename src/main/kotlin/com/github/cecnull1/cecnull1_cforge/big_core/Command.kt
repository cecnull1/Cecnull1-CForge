package com.github.cecnull1.cecnull1_cforge.big_core

import com.github.cecnull1.cecnull1_cforge.core.ResourceLocation

private typealias RL = ResourceLocation

object CommandCore {

    class Commands(val commands: MutableMap<RL, Command> = mutableMapOf())

    class Command(
        var name: String = "",
        var callback: (args: Array<String>) -> Unit = NONE_CALLBACK,
        var nextCommands: Commands = EMPTY_NEXT_COMMANDS,
    ) {
        companion object {
            val NONE_CALLBACK: (args: Array<String>) -> Unit = {}
            val EMPTY_NEXT_COMMANDS = Commands()
        }
    }

    fun commands(block: Commands.() -> Unit) = Commands().apply(block)

    fun Commands.command(rl: RL, block: Command.() -> Unit) {
        commands[rl] = Command().apply(block)
    }

    fun Command.callback(block: (args: Array<String>) -> Unit) {
        callback = block
    }

    fun Command.nextCommands(block: Commands.() -> Unit) {
        nextCommands = Commands().apply(block)
    }
}