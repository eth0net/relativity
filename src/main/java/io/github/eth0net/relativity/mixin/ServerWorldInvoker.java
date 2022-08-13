package io.github.eth0net.relativity.mixin;

import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ServerWorld.class)
public interface ServerWorldInvoker {
	@Invoker
	void invokeTickTime();

	@Invoker(value = "method_39501")
	void invokeTickWeather();
}
