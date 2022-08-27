package io.github.eth0net.relativity.item

import io.github.eth0net.relativity.Relativity
import net.minecraft.item.Item
import net.minecraft.item.ItemGroup
import net.minecraft.util.registry.Registry

object Items {
    val CORE = register("core", Item(Item.Settings().group(ItemGroup.MATERIALS)))
    val STAFF = register("staff", RelativityControlItem(Item.Settings().group(ItemGroup.TOOLS)))

    private fun register(id: String, item: Item): Item {
        return Registry.register(Registry.ITEM, Relativity.id(id), item)
    }
}
