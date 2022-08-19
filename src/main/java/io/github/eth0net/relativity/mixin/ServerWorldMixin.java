package io.github.eth0net.relativity.mixin;

import io.github.eth0net.relativity.Relativity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.dragon.EnderDragonFight;
import net.minecraft.server.world.ServerChunkManager;
import net.minecraft.server.world.ServerEntityManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.village.raid.RaidManager;
import net.minecraft.world.border.WorldBorder;
import net.minecraft.world.tick.WorldTickScheduler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.BiConsumer;
import java.util.function.BooleanSupplier;

@Mixin(ServerWorld.class)
public class ServerWorldMixin {
	private final int fullTick = 100;
	private int tickProgress = 0;
	private int tickRate = Relativity.defaultTickRate; // 100 == normal speed

	@Inject(method = "tick", at = @At("HEAD"))
	private void onTickStart(BooleanSupplier shouldKeepTicking, CallbackInfo ci) {
		tickRate = Relativity.INSTANCE.getTickRate();
		if (shouldKeepTicking.getAsBoolean()) tickProgress += tickRate;
	}

	@Inject(method = "tick", at = @At("TAIL"))
	private void onTickEnd(BooleanSupplier shouldKeepTicking, CallbackInfo ci) {
		if (tickProgress > fullTick && tickRate > 0) tickProgress = tickProgress % tickRate;
	}

	@Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/border/WorldBorder;tick()V"))
	private void tickWorldBorder(WorldBorder worldBorder) {
		for (int progress = tickProgress; progress >= fullTick; progress -= fullTick) worldBorder.tick();
	}

	@Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;method_39501()V"))
	private void tickWeather(ServerWorld world) {
		for (int progress = tickProgress; progress >= fullTick; progress -= fullTick) {
			((ServerWorldInvoker) world).invokeTickWeather();
		}
	}

	@Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;tickTime()V"))
	private void tickTime(ServerWorld world) {
		for (int progress = tickProgress; progress >= fullTick; progress -= fullTick) {
			((ServerWorldInvoker) world).invokeTickTime();
		}
	}

	@Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/tick/WorldTickScheduler;tick(JILjava/util/function/BiConsumer;)V"))
	private <T> void tickWorldTickScheduler(WorldTickScheduler<T> instance, long time, int maxTicks, BiConsumer<BlockPos, T> ticker) {
		for (int progress = tickProgress; progress >= fullTick; progress -= fullTick) {
			instance.tick(time, maxTicks, ticker);
		}
	}

	@Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/village/raid/RaidManager;tick()V"))
	private void getRaidManager(RaidManager instance) {
		for (int progress = tickProgress; progress >= fullTick; progress -= fullTick) instance.tick();
	}

	@Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerChunkManager;tick(Ljava/util/function/BooleanSupplier;Z)V"))
	private void tickChunkManager(ServerChunkManager instance, BooleanSupplier shouldKeepTicking, boolean tickChunks) {
		for (int progress = tickProgress; progress >= fullTick; progress -= fullTick) {
			instance.tick(shouldKeepTicking, tickChunks);
		}
	}

	@Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/boss/dragon/EnderDragonFight;tick()V"))
	private void tickEnderDragonFight(EnderDragonFight instance) {
		for (int progress = tickProgress; progress >= fullTick; progress -= fullTick) instance.tick();
	}

	@Redirect(method = "tickEntity", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;tick()V"))
	private void tickEntity(Entity entity) {
		if (!entity.isPlayer()) {
			for (int progress = tickProgress; progress >= fullTick; progress -= fullTick) entity.tick();
		} else {
			entity.tick();
		}
	}

	@Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;tickBlockEntities()V"))
	private void tickBlockEntities(ServerWorld world) {
		for (int progress = tickProgress; progress >= fullTick; progress -= fullTick) {
			((WorldInvoker) world).invokeTickBlockEntities();
		}
	}

	@Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerEntityManager;tick()V"))
	private void tickEntityManager(ServerEntityManager<Entity> instance) {
		for (int progress = tickProgress; progress >= fullTick; progress -= fullTick) instance.tick();
	}
}
