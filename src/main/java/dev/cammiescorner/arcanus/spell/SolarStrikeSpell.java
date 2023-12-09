package dev.cammiescorner.arcanus.spell;

import dev.cammiescorner.arcanus.Arcanus;
import dev.cammiescorner.arcanus.component.base.MagicCaster;
import dev.cammiescorner.arcanus.entity.SolarStrikeEntity;
import dev.cammiescorner.arcanus.util.ArcanusConfig;
import dev.cammiescorner.arcanus.util.ArcanusHelper;
import dev.cammiescorner.arcanus.util.CanBeDisabled;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

public class SolarStrikeSpell extends Spell implements CanBeDisabled {

    public SolarStrikeSpell(Pattern first, Pattern second, Pattern last, int manaCost) {
        super(first, second, last, manaCost);
    }

    @Override
    public void onCast(MagicCaster caster) {
        LivingEntity entity = caster.asEntity();
        HitResult result = ArcanusHelper.raycast(entity, 640F, false);

        if (result.getType() == HitResult.Type.BLOCK && result instanceof BlockHitResult blockHitResult) {
            if (entity instanceof Player player && !player.mayInteract(player.level(), blockHitResult.getBlockPos())) {
                player.displayClientMessage(Arcanus.translate("spell", "cannot_interact"), false);
                return;
            }

            ChunkPos chunkPos = new ChunkPos(blockHitResult.getBlockPos());
            ((ServerLevel) entity.level()).setChunkForced(chunkPos.x, chunkPos.z, true);
            SolarStrikeEntity solarStrike = new SolarStrikeEntity(entity, entity.level());
            solarStrike.setPos(result.getLocation());
            entity.level().addFreshEntity(solarStrike);
        } else {
            if (entity instanceof Player player) {
                player.displayClientMessage(Arcanus.translate("spell", "no_target"), false);
            }
        }
    }

    @Override
    public boolean enabled() {
        return ArcanusConfig.enableSolarStrike;
    }
}
