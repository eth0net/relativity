package io.github.eth0net.relativity.client

import io.github.eth0net.relativity.Relativity
import io.github.eth0net.relativity.client.input.keybind.KeyBinds
import io.github.eth0net.relativity.network.Channels
import net.minecraft.util.Identifier
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
            while (KeyBinds.STOP.wasPressed()) sendSetMessage(0)
            while (KeyBinds.NORMAL.wasPressed()) sendSetMessage(100)
            while (KeyBinds.SLOWER.wasPressed()) sendModMessage(-10)
            while (KeyBinds.FASTER.wasPressed()) sendModMessage(10)
        }

        Relativity.log.info("Relativity client initialized")
    }

    private fun sendModMessage(rate: Int) = sendIntMessage(Channels.MOD, rate)
    private fun sendSetMessage(rate: Int) = sendIntMessage(Channels.SET, rate)
    private fun sendIntMessage(channel: Identifier, rate: Int) {
        val buf = PacketByteBufs.create()
        buf.writeInt(rate)
        ClientPlayNetworking.send(channel, buf)
    }
}
