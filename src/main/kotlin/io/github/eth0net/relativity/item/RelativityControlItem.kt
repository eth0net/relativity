package io.github.eth0net.relativity.item

import io.github.eth0net.relativity.Relativity
import io.github.eth0net.relativity.Relativity.sendRateMessage
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.text.Text
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.world.World

class RelativityControlItem(settings: Settings) : Item(settings) {
    override fun use(world: World, user: PlayerEntity, hand: Hand): TypedActionResult<ItemStack> {
        if (world.isClient()) return super.use(world, user, hand)
        if (world.server?.isSingleplayer == true || user.hasPermissionLevel(4)) {
            Relativity.tickRate = if (user.isSneaking) 100 else 0
            user.sendRateMessage()
            return TypedActionResult.success(user.getStackInHand(hand))
        }
        user.sendMessage(Text.translatable("error.relativity.permission"), true)
        return TypedActionResult.fail(user.getStackInHand(hand))
    }
}
