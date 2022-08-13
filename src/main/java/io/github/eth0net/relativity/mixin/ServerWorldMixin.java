package io.github.eth0net.relativity.mixin;

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
	private final int tickRate = fullTick; // 100% tick rate
	private int tickProgress = 0;

	@Inject(method = "tick", at = @At("HEAD"))
	private void relativity_onTickStart(BooleanSupplier shouldKeepTicking, CallbackInfo ci) {
		if (shouldKeepTicking.getAsBoolean()) tickProgress += tickRate;
	}

	@Inject(method = "tick", at = @At("TAIL"))
	private void relativity_onTickEnd(BooleanSupplier shouldKeepTicking, CallbackInfo ci) {
		tickProgress = tickProgress % tickRate;
	}

	@Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/border/WorldBorder;tick()V"))
	private void relativity_tickWorldBorder(WorldBorder worldBorder) {
		for (int progress = tickProgress; progress >= fullTick; progress -= fullTick) worldBorder.tick();
	}

//	@Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;tickWeather()V"))
//	private void tickWeather(ServerWorld world) {
//
//	}

//	@Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;tickTime()V"))
//	private void relativity_tickTime(ServerWorld world) {
//		for (int progress = tickProgress; progress >= fullTick; progress -= fullTick) {
//			world.tickTime();
//		}
//	}

	@Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/tick/WorldTickScheduler;tick(JILjava/util/function/BiConsumer;)V"))
	private <T> void relativity_tickWorldTickScheduler(WorldTickScheduler<T> instance, long time, int maxTicks, BiConsumer<BlockPos, T> ticker) {
		for (int progress = tickProgress; progress >= fullTick; progress -= fullTick) {
			instance.tick(time, maxTicks, ticker);
		}
	}

	@Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/village/raid/RaidManager;tick()V"))
	private void relativity_getRaidManager(RaidManager instance) {
		for (int progress = tickProgress; progress >= fullTick; progress -= fullTick) instance.tick();
	}

	@Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerChunkManager;tick(Ljava/util/function/BooleanSupplier;Z)V"))
	private void relativity_tickChunkManager(ServerChunkManager instance, BooleanSupplier shouldKeepTicking, boolean tickChunks) {
		for (int progress = tickProgress; progress >= fullTick; progress -= fullTick) {
			instance.tick(shouldKeepTicking, tickChunks);
		}
	}

	@Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/boss/dragon/EnderDragonFight;tick()V"))
	private void relativity_tickEnderDragonFight(EnderDragonFight instance) {
		for (int progress = tickProgress; progress >= fullTick; progress -= fullTick) instance.tick();
	}

	@Redirect(method = "tickEntity", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;tick()V"))
	private void relativity_tickEntity(Entity entity) {
		if (!entity.isPlayer()) {
			for (int progress = tickProgress; progress >= fullTick; progress -= fullTick) entity.tick();
		} else {
			entity.tick();
		}
	}

//	@Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;tickBlockEntities()V"))
//	private void relativity_tickBlockEntities(ServerWorld world) {
//		for (int progress = tickProgress; progress >= fullTick; progress -= fullTick) {
//			world.tickBlockEntities();
//		}
//	}

	@Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerEntityManager;tick()V"))
	private void relativity_tickEntityManager(ServerEntityManager<Entity> instance) {
		for (int progress = tickProgress; progress >= fullTick; progress -= fullTick) instance.tick();
	}
}
