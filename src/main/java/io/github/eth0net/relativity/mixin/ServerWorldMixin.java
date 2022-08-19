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
	private static final int fullTick = 100;
	private static int tickCount = 0;
	private static int tickProgress = 0;

	@Inject(method = "tick", at = @At("HEAD"))
	private void onTickStart(BooleanSupplier shouldKeepTicking, CallbackInfo ci) {
		if (shouldKeepTicking.getAsBoolean()) tickProgress += Relativity.INSTANCE.getTickRate();
		tickCount = tickProgress / fullTick;
		tickProgress = tickProgress % fullTick;
	}

	@Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/border/WorldBorder;tick()V"))
	private void tickWorldBorder(WorldBorder worldBorder) {
		for (int i = 0; i < tickCount; i++) worldBorder.tick();
	}

	@Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;method_39501()V"))
	private void tickWeather(ServerWorld world) {
		for (int i = 0; i < tickCount; i++) ((ServerWorldInvoker) world).invokeTickWeather();
	}

	@Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;tickTime()V"))
	private void tickTime(ServerWorld world) {
		for (int i = 0; i < tickCount; i++) ((ServerWorldInvoker) world).invokeTickTime();
	}

	@Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/tick/WorldTickScheduler;tick(JILjava/util/function/BiConsumer;)V"))
	private <T> void tickWorldTickScheduler(WorldTickScheduler<T> instance, long time, int maxTicks, BiConsumer<BlockPos, T> ticker) {
		for (int i = 0; i < tickCount; i++) instance.tick(time, maxTicks, ticker);
	}

	@Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/village/raid/RaidManager;tick()V"))
	private void getRaidManager(RaidManager instance) {
		for (int i = 0; i < tickCount; i++) instance.tick();
	}

	@Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerChunkManager;tick(Ljava/util/function/BooleanSupplier;Z)V"))
	private void tickChunkManager(ServerChunkManager instance, BooleanSupplier shouldKeepTicking, boolean tickChunks) {
		for (int i = 0; i < tickCount; i++) instance.tick(shouldKeepTicking, tickChunks);
	}

	@Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/boss/dragon/EnderDragonFight;tick()V"))
	private void tickEnderDragonFight(EnderDragonFight instance) {
		for (int i = 0; i < tickCount; i++) instance.tick();
	}

	@Redirect(method = "tickEntity", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;tick()V"))
	private void tickEntity(Entity entity) {
		if (!entity.isPlayer()) {
			for (int i = 0; i < tickCount; i++) entity.tick();
		} else {
			entity.tick();
		}
	}

	@Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;tickBlockEntities()V"))
	private void tickBlockEntities(ServerWorld world) {
		for (int i = 0; i < tickCount; i++) ((WorldInvoker) world).invokeTickBlockEntities();
	}

	@Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerEntityManager;tick()V"))
	private void tickEntityManager(ServerEntityManager<Entity> instance) {
		for (int i = 0; i < tickCount; i++) instance.tick();
	}
}
