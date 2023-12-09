package dev.cammiescorner.arcanus.spell;

import dev.cammiescorner.arcanus.component.base.MagicCaster;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.portal.PortalInfo;
import net.minecraft.world.phys.Vec3;
import org.quiltmc.qsl.worldgen.dimension.api.QuiltDimensions;

public class DreamWarpSpell extends Spell {

    public DreamWarpSpell(Pattern first, Pattern second, Pattern last, int manaCost) {
        super(first, second, last, manaCost);
    }

    @Override
    public void onCast(MagicCaster caster) {
        if (caster.asEntity() instanceof ServerPlayer serverPlayer) {
            ServerLevel serverWorld = serverPlayer.getServer().getLevel(serverPlayer.getRespawnDimension());
            BlockPos spawnPos = serverPlayer.getRespawnPosition();
            Vec3 rotation = serverPlayer.getViewVector(1F);
            float spawnAngle = serverPlayer.getRespawnAngle();
            boolean hasSpawnPoint = serverPlayer.isRespawnForced();

            if (serverWorld != null && spawnPos != null && serverWorld.isInWorldBounds(spawnPos)) {
                Vec3 spawnPoint = Player.findRespawnPositionAndUseSpawnBlock(serverWorld, spawnPos, spawnAngle, hasSpawnPoint, true).orElse(null);
                if (spawnPoint != null) {
                    Vec3 prevPos = serverPlayer.position();
                    QuiltDimensions.teleport(serverPlayer, serverWorld, new PortalInfo(spawnPoint, Vec3.ZERO, (float) rotation.x, (float) rotation.y));
                    serverWorld.playSound(null, prevPos.x(), prevPos.y(), prevPos.z(), SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 2F, 1F);
                    serverPlayer.level().playSound(null, serverPlayer, SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 2F, 1F);
                    return;
                }
            }

            serverPlayer.displayClientMessage(Component.translatable("block.minecraft.spawn.not_valid"), false);
        }
    }
}
