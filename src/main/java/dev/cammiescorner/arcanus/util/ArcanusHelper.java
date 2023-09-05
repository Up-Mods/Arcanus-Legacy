package dev.cammiescorner.arcanus.util;

import dev.cammiescorner.arcanus.Arcanus;
import dev.cammiescorner.arcanus.component.ArcanusComponents;
import dev.cammiescorner.arcanus.registry.ArcanusItems;
import dev.cammiescorner.arcanus.spell.Spell;
import org.jetbrains.annotations.ApiStatus;

import java.util.Set;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.inventory.LecternMenu;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.LecternBlockEntity;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import static dev.cammiescorner.arcanus.registry.ArcanusEntityAttributes.*;

public class ArcanusHelper {

    public static final Set<Attribute> INVERSE_ENTITY_ATTRIBUTES = Set.of(MANA_COST, MANA_REGEN, BURNOUT_REGEN, MANA_LOCK);

    public static HitResult raycast(Entity origin, double maxDistance, boolean hitsEntities) {
        Vec3 startPos = origin.getEyePosition(1F);
        Vec3 rotation = origin.getViewVector(1F);
        Vec3 endPos = startPos.add(rotation.x * maxDistance, rotation.y * maxDistance, rotation.z * maxDistance);
        HitResult hitResult = origin.level.clip(new ClipContext(startPos, endPos, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, origin));

        if (hitResult.getType() != HitResult.Type.MISS)
            endPos = hitResult.getLocation();

        maxDistance *= 5;
        HitResult entityHitResult = ProjectileUtil.getEntityHitResult(origin, startPos, endPos, origin.getBoundingBox().expandTowards(rotation.scale(maxDistance)).inflate(1.0D, 1.0D, 1.0D), entity -> !entity.isSpectator(), maxDistance);

        if (hitsEntities && entityHitResult != null)
            hitResult = entityHitResult;

        return hitResult;
    }

    public static void drawLine(Vec3 start, Vec3 end, Level world, double density, ParticleOptions particle) {
        double totalDistance = start.distanceTo(end);

        for (double distanceTraveled = 0; distanceTraveled < totalDistance; distanceTraveled += density) {
            double alpha = distanceTraveled / totalDistance;
            double x = interpolate(start.x, end.x, alpha);
            double y = interpolate(start.y, end.y, alpha);
            double z = interpolate(start.z, end.z, alpha);

            if (world.isClientSide())
                world.addParticle(particle, x, y, z, 0, 0, 0);
            else
                ((ServerLevel) world).sendParticles(particle, x, y, z, 1, 0, 0, 0, 0);
        }
    }

    private static double interpolate(double start, double end, double alpha) {
        return start + (end - start) * alpha;
    }

    public static void addWandsToTab(CreativeModeTab.Output entries) {
        ItemStack stack = new ItemStack(ArcanusItems.INITIATE_WAND);
        entries.accept(stack);

        stack = new ItemStack(ArcanusItems.ADEPT_WAND);
        CompoundTag tag = stack.getOrCreateTagElement(Arcanus.MOD_ID);
        tag.putInt("Exp", 3200);
        entries.accept(stack);

        stack = new ItemStack(ArcanusItems.MASTER_WAND);
        tag = stack.getOrCreateTagElement(Arcanus.MOD_ID);
        tag.putInt("Exp", 6400);
        entries.accept(stack);
    }

    @ApiStatus.Internal
    public static void onInteractLecternBlock(Level world, BlockPos pos, Player player) {
        if (!world.isClientSide() && world.getBlockEntity(pos) instanceof LecternBlockEntity lectern && player.containerMenu instanceof LecternMenu) {
            CompoundTag nbt = lectern.getBook().getTag();

            if (nbt != null && nbt.contains("spell", Tag.TAG_STRING)) {
                Spell spell = Arcanus.SPELL.get(new ResourceLocation(nbt.getString("spell")));
                player.getComponent(ArcanusComponents.SPELL_MEMORY).unlockSpell(spell);
            }
        }
    }

    public static void giveOrDrop(ServerPlayer player, ItemStack stack) {
        if (player.getInventory().add(stack) && stack.isEmpty()) {
            stack.setCount(1);
            ItemEntity entity = player.drop(stack, false);

            if (entity != null)
                entity.makeFakeItem();

            player.level.playSound(null, player, SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 0.2F, ((player.getRandom().nextFloat() - player.getRandom().nextFloat()) * 0.7F + 1.0F) * 2.0F);
            player.inventoryMenu.broadcastChanges();
        } else {
            ItemEntity entity = player.drop(stack, false);

            if (entity != null) {
                entity.setNoPickUpDelay();
                entity.setTarget(player.getUUID());
            }
        }
    }

    public static float[] getRGB(int color) {
        float r = ((color >> 16 & 255) / 255F);
        float g = ((color >> 8 & 255) / 255F);
        float b = ((color & 255) / 255F);

        return new float[] {r, g, b};
    }

    public static float[] getRGBMultiply(int color, float multiplier) {
        float[] values = getRGB(color);
        values[0] *= multiplier;
        values[1] *= multiplier;
        values[2] *= multiplier;
        return values;
    }
}
