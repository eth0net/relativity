package io.github.eth0net.relativity.client

import com.mojang.blaze3d.platform.InputUtil
import io.github.eth0net.relativity.Relativity
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper
import net.minecraft.client.option.KeyBind
import org.lwjgl.glfw.GLFW
import org.quiltmc.loader.api.ModContainer
import org.quiltmc.qsl.base.api.entrypoint.client.ClientModInitializer
import org.quiltmc.qsl.lifecycle.api.client.event.ClientTickEvents
import org.quiltmc.qsl.networking.api.PacketByteBufs
import org.quiltmc.qsl.networking.api.client.ClientPlayNetworking

@Suppress("UNUSED")
object RelativityClient : ClientModInitializer {
    private val stop = KeyBind(
        "key.relativity.stop", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_MINUS, "category.relativity.general"
    )

    private val normal = KeyBind(
        "key.relativity.normal", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_EQUAL, "category.relativity.general"
    )

    private val slow = KeyBind(
        "key.relativity.slow", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_LEFT_BRACKET, "category.relativity.general"
    )

    private val fast = KeyBind(
        "key.relativity.fast", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_RIGHT_BRACKET, "category.relativity.general"
    )

    override fun onInitializeClient(mod: ModContainer?) {
        Relativity.log.info("Relativity client initializing...")

        KeyBindingHelper.registerKeyBinding(stop)
        KeyBindingHelper.registerKeyBinding(normal)
        KeyBindingHelper.registerKeyBinding(slow)
        KeyBindingHelper.registerKeyBinding(fast)

        ClientTickEvents.END.register {
            while (stop.wasPressed()) {
                val buf = PacketByteBufs.create()
                buf.writeInt(0)
                ClientPlayNetworking.send(Relativity.controlChannel, buf)
            }
            while (normal.wasPressed()) {
                val buf = PacketByteBufs.create()
                buf.writeInt(50)
                ClientPlayNetworking.send(Relativity.controlChannel, buf)
            }
            while (slow.wasPressed()) {
                val buf = PacketByteBufs.create()
                buf.writeInt(100)
                ClientPlayNetworking.send(Relativity.controlChannel, buf)
            }
            while (fast.wasPressed()) {
                val buf = PacketByteBufs.create()
                buf.writeInt(200)
                ClientPlayNetworking.send(Relativity.controlChannel, buf)
            }
        }

        Relativity.log.info("Relativity client initialized")
    }
}
