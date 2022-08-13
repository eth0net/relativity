package io.github.eth0net.relativity

import net.minecraft.util.Identifier
import org.quiltmc.loader.api.ModContainer
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer
import org.quiltmc.qsl.networking.api.ServerPlayNetworking
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Suppress("UNUSED")
object Relativity : ModInitializer {
    private const val MOD_ID = "relativity"

    val log: Logger = LoggerFactory.getLogger(MOD_ID)

    internal val controlChannel = id("control_channel")

    var tickRate = 100 // normal tick rate == 100

    override fun onInitialize(mod: ModContainer) {
        log.info("Relativity initializing...")

        ServerPlayNetworking.registerGlobalReceiver(controlChannel) { server, player, net, buf, send ->
            tickRate = buf.readInt()
        }

        log.info("Relativity initialized")
    }

    private fun id(path: String) = Identifier(MOD_ID, path)
}
