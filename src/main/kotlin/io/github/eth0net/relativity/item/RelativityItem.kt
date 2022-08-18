package io.github.eth0net.relativity.item

import io.github.eth0net.relativity.Relativity
import io.github.eth0net.relativity.network.Channels
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.world.World
import org.quiltmc.qsl.networking.api.PacketByteBufs
import org.quiltmc.qsl.networking.api.client.ClientPlayNetworking

class RelativityItem(settings: Settings) : Item(settings) {
    override fun use(world: World, player: PlayerEntity, hand: Hand): TypedActionResult<ItemStack> {
        val buf = PacketByteBufs.create()
        buf.writeInt(if (Relativity.tickRate == 0) 100 else 0)
        ClientPlayNetworking.send(Channels.CONTROL, buf)
        return TypedActionResult.success(player.getStackInHand(hand))
    }
}

/* Recipes
* D: Diamond
* E: Echo Shard
* R: Redstone
*
* Shard:
* |R|E|R|
* |E|D|E|
* |R|E|R|
*
* X: Shard
* \: Stick
*
* Staff:
* |X| | |
* | |\| |
* | | |\|
* */
