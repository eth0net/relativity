package io.github.eth0net.relativity.item

import io.github.eth0net.relativity.Relativity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.text.Text
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.world.World

class RelativityItem(settings: Settings) : Item(settings) {
    override fun use(world: World, user: PlayerEntity, hand: Hand): TypedActionResult<ItemStack> {
        if (world.isClient()) return super.use(world, user, hand)
        Relativity.tickRate = if (user.isSneaking) 100 else 0
        user.sendMessage(Text.literal("Relativity: ${Relativity.tickRate}%"), true)
        return TypedActionResult.success(user.getStackInHand(hand))
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
