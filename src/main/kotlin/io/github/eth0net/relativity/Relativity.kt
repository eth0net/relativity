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

    private val minTickRate = 0
    private val maxTickRate = 1000
    private val defaultTickRate = 100
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

        ServerPlayNetworking.registerGlobalReceiver(Channels.CONTROL) { _, player, _, buf, _ ->
            tickRate = buf.readInt()
            player.sendMessage(Text.literal("Relativity: ${tickRate}%"), true)
        }

        log.info("Relativity initialized")
    }

    internal fun id(path: String) = Identifier(MOD_ID, path)
}
