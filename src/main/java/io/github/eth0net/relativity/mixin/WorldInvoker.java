package io.github.eth0net.relativity.mixin;

import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(World.class)
public interface WorldInvoker {
	@Invoker
	void invokeTickBlockEntities();
}
