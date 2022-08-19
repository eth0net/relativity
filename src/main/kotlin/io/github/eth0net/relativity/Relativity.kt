package io.github.eth0net.relativity

import io.github.eth0net.relativity.item.Items
import io.github.eth0net.relativity.network.Channels
import net.minecraft.text.Text
import net.minecraft.util.Identifier
import org.quiltmc.loader.api.ModContainer
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer
import org.quiltmc.qsl.networking.api.ServerPlayNetworking
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Suppress("UNUSED")
object Relativity : ModInitializer {
    internal const val MOD_ID = "relativity"

    val log: Logger = LoggerFactory.getLogger(MOD_ID)

    private const val minTickRate = 0
    private const val maxTickRate = 1000
    internal const val defaultTickRate = 100
    var tickRate = defaultTickRate // normal tick rate == 100
        set(value) {
            field = if (value < minTickRate) {
                minTickRate
            } else if (value > maxTickRate) {
                maxTickRate
            } else {
                value
            }
        }

    override fun onInitialize(mod: ModContainer) {
        log.info("Relativity initializing...")

        Items

        ServerPlayNetworking.registerGlobalReceiver(Channels.SET) { server, _, _, buf, _ ->
            tickRate = buf.readInt()
            server.playerManager.playerList.forEach {
                it.sendMessage(Text.literal("Relativity: ${tickRate}%"), true)
            }
        }
        ServerPlayNetworking.registerGlobalReceiver(Channels.MOD) { server, _, _, buf, _ ->
            tickRate += buf.readInt()
            server.playerManager.playerList.forEach {
                it.sendMessage(Text.literal("Relativity: ${tickRate}%"), true)
            }
        }

        log.info("Relativity initialized")
    }

    internal fun id(path: String) = Identifier(MOD_ID, path)
}
