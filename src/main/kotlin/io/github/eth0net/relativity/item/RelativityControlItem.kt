package io.github.eth0net.relativity.item

import io.github.eth0net.relativity.Relativity.setTickRate
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.world.World

class RelativityControlItem(settings: Settings) : Item(settings) {
    override fun use(world: World, user: PlayerEntity, hand: Hand): TypedActionResult<ItemStack> {
        if (world.isClient()) return super.use(world, user, hand)

        val rate = if (user.isSneaking) 100 else 0
        val stack = user.getStackInHand(hand)

        return if (user.setTickRate(rate)) {
            TypedActionResult.success(stack)
        } else {
            TypedActionResult.fail(stack)
        }
    }
}
