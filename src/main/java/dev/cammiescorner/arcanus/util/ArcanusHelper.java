package dev.cammiescorner.arcanus.util;

import dev.cammiescorner.arcanus.Arcanus;
import dev.cammiescorner.arcanus.component.ArcanusComponents;
import dev.cammiescorner.arcanus.registry.ArcanusItems;
import dev.cammiescorner.arcanus.spell.Spell;
import net.minecraft.block.entity.LecternBlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.screen.LecternScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import org.jetbrains.annotations.ApiStatus;

import java.util.Set;

import static dev.cammiescorner.arcanus.registry.ArcanusEntityAttributes.*;

public class ArcanusHelper {

    public static final Set<EntityAttribute> INVERSE_ENTITY_ATTRIBUTES = Set.of(MANA_COST, MANA_REGEN, BURNOUT_REGEN, MANA_LOCK);

    public static HitResult raycast(Entity origin, double maxDistance, boolean hitsEntities) {
        Vec3d startPos = origin.getCameraPosVec(1F);
        Vec3d rotation = origin.getRotationVec(1F);
        Vec3d endPos = startPos.add(rotation.x * maxDistance, rotation.y * maxDistance, rotation.z * maxDistance);
        HitResult hitResult = origin.world.raycast(new RaycastContext(startPos, endPos, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, origin));

        if (hitResult.getType() != HitResult.Type.MISS)
            endPos = hitResult.getPos();

        maxDistance *= 5;
        HitResult entityHitResult = ProjectileUtil.raycast(origin, startPos, endPos, origin.getBoundingBox().stretch(rotation.multiply(maxDistance)).expand(1.0D, 1.0D, 1.0D), entity -> !entity.isSpectator(), maxDistance);

        if (hitsEntities && entityHitResult != null)
            hitResult = entityHitResult;

        return hitResult;
    }

    public static void drawLine(Vec3d start, Vec3d end, World world, double density, ParticleEffect particle) {
        double totalDistance = start.distanceTo(end);

        for (double distanceTraveled = 0; distanceTraveled < totalDistance; distanceTraveled += density) {
            double alpha = distanceTraveled / totalDistance;
            double x = interpolate(start.x, end.x, alpha);
            double y = interpolate(start.y, end.y, alpha);
            double z = interpolate(start.z, end.z, alpha);

            if (world.isClient())
                world.addParticle(particle, x, y, z, 0, 0, 0);
            else
                ((ServerWorld) world).spawnParticles(particle, x, y, z, 1, 0, 0, 0, 0);
        }
    }

    private static double interpolate(double start, double end, double alpha) {
        return start + (end - start) * alpha;
    }

    public static void addWandsToTab(ItemGroup.ItemStackCollector entries) {
        ItemStack stack = new ItemStack(ArcanusItems.INITIATE_WAND);
        entries.addStack(stack);

        stack = new ItemStack(ArcanusItems.ADEPT_WAND);
        NbtCompound tag = stack.getOrCreateSubNbt(Arcanus.MOD_ID);
        tag.putInt("Exp", 3200);
        entries.addStack(stack);

        stack = new ItemStack(ArcanusItems.MASTER_WAND);
        tag = stack.getOrCreateSubNbt(Arcanus.MOD_ID);
        tag.putInt("Exp", 6400);
        entries.addStack(stack);
    }

    @ApiStatus.Internal
    public static void onInteractLecternBlock(World world, BlockPos pos, PlayerEntity player) {
        if (!world.isClient() && world.getBlockEntity(pos) instanceof LecternBlockEntity lectern && player.currentScreenHandler instanceof LecternScreenHandler) {
            NbtCompound nbt = lectern.getBook().getNbt();

            if (nbt != null && nbt.contains("spell", NbtElement.STRING_TYPE)) {
                Spell spell = Arcanus.SPELL.get(new Identifier(nbt.getString("spell")));
                player.getComponent(ArcanusComponents.SPELL_MEMORY).unlockSpell(spell);
            }
        }
    }

    public static void giveOrDrop(ServerPlayerEntity player, ItemStack stack) {
        if (player.getInventory().insertStack(stack) && stack.isEmpty()) {
            stack.setCount(1);
            ItemEntity entity = player.dropItem(stack, false);

            if (entity != null)
                entity.setDespawnImmediately();

            player.world.playSoundFromEntity(null, player, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 0.2F, ((player.getRandom().nextFloat() - player.getRandom().nextFloat()) * 0.7F + 1.0F) * 2.0F);
            player.playerScreenHandler.sendContentUpdates();
        } else {
            ItemEntity entity = player.dropItem(stack, false);

            if (entity != null) {
                entity.resetPickupDelay();
                entity.setOwner(player.getUuid());
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
