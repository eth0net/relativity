package io.github.eth0net.relativity.client.input.keybind

import com.mojang.blaze3d.platform.InputUtil
import io.github.eth0net.relativity.Relativity
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper
import net.minecraft.client.option.KeyBind
import org.lwjgl.glfw.GLFW

object KeyBinds {
    val STOP = bind("stop", GLFW.GLFW_KEY_MINUS)
    val NORMAL = bind("normal", GLFW.GLFW_KEY_EQUAL)
    val SLOWER = bind("slower", GLFW.GLFW_KEY_LEFT_BRACKET)
    val FASTER = bind("faster", GLFW.GLFW_KEY_RIGHT_BRACKET)

    private fun bind(name: String, key: Int) = KeyBind(key(name), InputUtil.Type.KEYSYM, key, category("general"))

    private fun key(name: String) = "key.${Relativity.MOD_ID}.$name"

    private fun category(name: String) = "category.${Relativity.MOD_ID}.$name"

    internal fun register() {
        KeyBindingHelper.registerKeyBinding(STOP)
        KeyBindingHelper.registerKeyBinding(NORMAL)
        KeyBindingHelper.registerKeyBinding(SLOWER)
        KeyBindingHelper.registerKeyBinding(FASTER)
    }
}
