package io.github.eth0net.relativity

import io.github.eth0net.relativity.item.Items
import io.github.eth0net.relativity.network.Channels
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.text.Text
import net.minecraft.util.Identifier
import org.quiltmc.loader.api.ModContainer
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer
import org.quiltmc.qsl.lifecycle.api.event.ServerLifecycleEvents
import org.quiltmc.qsl.networking.api.ServerPlayNetworking
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Suppress("UNUSED")
object Relativity : ModInitializer {
    internal const val MOD_ID = "relativity"

    val log: Logger = LoggerFactory.getLogger(MOD_ID)

    private const val minTickRate = 0
    private const val maxTickRate = 1000
    private const val defaultTickRate = 100
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

        ServerLifecycleEvents.STARTING.register { tickRate = defaultTickRate }

        ServerPlayNetworking.registerGlobalReceiver(Channels.SET) { _, player, _, buf, _ ->
            player.setTickRate(buf.readInt())
        }
        ServerPlayNetworking.registerGlobalReceiver(Channels.MOD) { _, player, _, buf, _ ->
            player.setTickRate(tickRate + buf.readInt())
        }

        log.info("Relativity initialized")
    }

    internal fun id(path: String) = Identifier(MOD_ID, path)

    internal fun PlayerEntity.setTickRate(rate: Int): Boolean {
        if (server?.isSingleplayer == false && !hasPermissionLevel(4)) {
            sendMessage(Text.translatable("error.relativity.permission"), true)
            return false
        }
        tickRate = rate
        server?.playerManager?.playerList?.forEach {
            it.sendMessage(Text.translatable("message.relativity.rate", tickRate), true)
        }
        return true
    }
}
