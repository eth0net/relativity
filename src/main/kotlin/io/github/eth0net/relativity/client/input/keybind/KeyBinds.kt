package io.github.eth0net.relativity.client.input.keybind

import com.mojang.blaze3d.platform.InputUtil
import io.github.eth0net.relativity.Relativity
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper
import net.minecraft.client.option.KeyBind
import org.lwjgl.glfw.GLFW

object KeyBinds {
    val STOP = register("stop", GLFW.GLFW_KEY_MINUS)
    val NORMAL = register("normal", GLFW.GLFW_KEY_EQUAL)
    val SLOWER = register("slower", GLFW.GLFW_KEY_LEFT_BRACKET)
    val FASTER = register("faster", GLFW.GLFW_KEY_RIGHT_BRACKET)

    private fun register(name: String, key: Int): KeyBind {
        return KeyBindingHelper.registerKeyBinding(
            KeyBind(key(name), InputUtil.Type.KEYSYM, key, category("general"))
        )
    }

    private fun key(name: String) = "key.${Relativity.MOD_ID}.$name"

    private fun category(name: String) = "category.${Relativity.MOD_ID}.$name"
}
