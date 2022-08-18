package io.github.eth0net.relativity.client

import io.github.eth0net.relativity.Relativity
import io.github.eth0net.relativity.client.input.keybind.KeyBinds
import io.github.eth0net.relativity.network.Channels
import org.quiltmc.loader.api.ModContainer
import org.quiltmc.qsl.base.api.entrypoint.client.ClientModInitializer
import org.quiltmc.qsl.lifecycle.api.client.event.ClientTickEvents
import org.quiltmc.qsl.networking.api.PacketByteBufs
import org.quiltmc.qsl.networking.api.client.ClientPlayNetworking

@Suppress("UNUSED")
object RelativityClient : ClientModInitializer {
    override fun onInitializeClient(mod: ModContainer?) {
        Relativity.log.info("Relativity client initializing...")

        ClientTickEvents.END.register {
            while (KeyBinds.STOP.wasPressed()) sendControlMessage(0)
            while (KeyBinds.NORMAL.wasPressed()) sendControlMessage(100)
            while (KeyBinds.SLOW.wasPressed()) sendControlMessage(50)
            while (KeyBinds.FAST.wasPressed()) sendControlMessage(200)
        }

        Relativity.log.info("Relativity client initialized")
    }

    private fun sendControlMessage(rate: Int) {
        val buf = PacketByteBufs.create()
        buf.writeInt(rate)
        ClientPlayNetworking.send(Channels.CONTROL, buf)
    }
}
