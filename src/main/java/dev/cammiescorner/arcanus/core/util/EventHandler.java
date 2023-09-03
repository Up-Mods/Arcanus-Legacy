package dev.cammiescorner.arcanus.core.util;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.cammiescorner.arcanus.Arcanus;
import dev.cammiescorner.arcanus.common.items.WandItem;
import dev.cammiescorner.arcanus.common.structure.processor.BookshelfReplacerStructureProcessor;
import dev.cammiescorner.arcanus.common.structure.processor.LecternStructureProcessor;
import dev.cammiescorner.arcanus.core.integration.ArcanusConfig;
import dev.cammiescorner.arcanus.core.registry.ModCommands;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.condition.RandomChanceLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.registry.Holder;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.structure.pool.SinglePoolElement;
import net.minecraft.structure.pool.StructurePool;
import net.minecraft.structure.processor.StructureProcessor;
import net.minecraft.structure.processor.StructureProcessorList;
import net.minecraft.util.Identifier;
import org.quiltmc.qsl.command.api.CommandRegistrationCallback;

import java.util.ArrayList;
import java.util.List;

public class EventHandler {
    private static final Identifier HUD_ELEMENTS = new Identifier(Arcanus.MOD_ID, "textures/gui/hud_elements.png");
    private static final Identifier RUINED_PORTAL_LOOT_TABLE = new Identifier("minecraft", "chests/ruined_portal");
    private static final Identifier STRONGHOLD_LIBRARY_LOOT_TABLE = new Identifier("minecraft", "chests/stronghold_library");

    @Environment(EnvType.CLIENT)
    public static void clientEvents() {
        final MinecraftClient client = MinecraftClient.getInstance();
        var manaTimer = new Object() {
            int value;
        };

        //-----HUD Render Callback-----//
        HudRenderCallback.EVENT.register((matrices, tickDelta) -> {
            if (client.cameraEntity instanceof PlayerEntity player && !player.isSpectator() && !player.isCreative()) {
                MagicUser user = (MagicUser) player;
                int mana = Math.min(user.getMana(), user.getMaxMana() - user.getBurnout());
                int burnout = user.getBurnout();
                int manaLock = ArcanusHelper.getManaLock(player);

                if (player.getMainHandStack().getItem() instanceof WandItem || mana < user.getMaxMana())
                    manaTimer.value = Math.min(manaTimer.value + 1, 40);
                else
                    manaTimer.value = Math.max(manaTimer.value - 1, 0);

                if (manaTimer.value > 0) {
                    user.shouldShowMana(true);
                    int scaledWidth = client.getWindow().getScaledWidth();
                    int scaledHeight = client.getWindow().getScaledHeight();
                    int x = scaledWidth / 2 + 82;
                    int y = scaledHeight - (player.isCreative() ? 34 : 49);
                    float alpha = manaTimer.value > 20 ? 1F : manaTimer.value / 20F;

                    RenderSystem.enableBlend();
                    RenderSystem.setShaderTexture(0, HUD_ELEMENTS);
                    RenderSystem.setShaderColor(1F, 1F, 1F, alpha);

                    // Draw background
                    for (int i = 0; i < 10; i++)
                        DrawableHelper.drawTexture(matrices, x - (i * 8), y, 0, 15, 9, 9, 256, 256);

                    // Draw full mana orb
                    for (int i = 0; i < mana / 2; i++)
                        DrawableHelper.drawTexture(matrices, x - (i * 8), y, 0, 0, 8, 8, 256, 256);

                    // Draw half mana orb
                    if (mana % 2 == 1)
                        DrawableHelper.drawTexture(matrices, x - ((mana / 2) * 8), y, 8, 0, 8, 8, 256, 256);

                    boolean manaLockOdd = manaLock % 2 == 1;
                    boolean burnoutOdd = burnout % 2 == 1;
                    int adjustedBurnout = manaLockOdd && burnoutOdd ? burnout / 2 + 1 : burnout / 2;
                    int adjustedManaLock = (manaLock / 2) * 8;
                    x -= 72;

                    // Draw full burnout orb
                    for (int i = 0; i < adjustedBurnout; i++)
                        if (manaLockOdd && i == 0)
                            DrawableHelper.drawTexture(matrices, x + adjustedManaLock, y, 32, 0, 8, 8, 256, 256);
                        else
                            DrawableHelper.drawTexture(matrices, x + adjustedManaLock + (i * 8), y, 16, 0, 8, 8, 256, 256);

                    // Draw half burnout orb
                    if (burnoutOdd != manaLockOdd && burnout > 0)
                        DrawableHelper.drawTexture(matrices, x + adjustedManaLock + (adjustedBurnout * 8), y, 24, 0, 8, 8, 256, 256);

                    // Draw full mana lock orb
                    for (int i = 0; i < manaLock / 2; i++)
                        DrawableHelper.drawTexture(matrices, x + (i * 8), y, 40, 0, 8, 8, 256, 256);

                    // Draw half mana lock orb
                    if (manaLock % 2 == 1)
                        DrawableHelper.drawTexture(matrices, x + adjustedManaLock, y, 48, 0, 8, 8, 256, 256);
                } else
                    user.shouldShowMana(false);
            }
        });
    }

    public static void commonEvents() {
        //-----Server Starting Callback-----//
        ServerLifecycleEvents.SERVER_STARTING.register(server -> EventHandler.addStructureProcessors(server.getRegistryManager().get(RegistryKeys.STRUCTURE_POOL)));

        //-----Loot Table Callback-----//
        LootTableEvents.MODIFY.register((resourceManager, lootManager, id, tableBuilder, tableSource) -> {
            if (ArcanusConfig.strongholdsHaveBooks && STRONGHOLD_LIBRARY_LOOT_TABLE.equals(id) && !FabricLoader.getInstance().isModLoaded("betterstrongholds")) {
                LootPool.Builder poolBuilder = LootPool.builder().rolls(ConstantLootNumberProvider.create(4)).conditionally(RandomChanceLootCondition.builder(0.5F).build()).with(createItemEntry(new ItemStack(Items.WRITTEN_BOOK)).build());
                tableBuilder.pool(poolBuilder);
            }

            if (ArcanusConfig.ruinedPortalsHaveBooks && RUINED_PORTAL_LOOT_TABLE.equals(id)) {
                LootPool.Builder poolBuilder = LootPool.builder().rolls(ConstantLootNumberProvider.create(1)).conditionally(RandomChanceLootCondition.builder(0.1F).build()).with(createItemEntry(new ItemStack(Items.WRITTEN_BOOK)).build());
                tableBuilder.pool(poolBuilder);
            }
        });

        //-----Copy Player Data Callback-----//
        ServerPlayerEvents.COPY_FROM.register((oldPlayer, newPlayer, alive) -> {
            ((MagicUser) newPlayer).setMana(((MagicUser) oldPlayer).getMana());
            ((MagicUser) newPlayer).setBurnout(((MagicUser) oldPlayer).getBurnout());
            ((MagicUser) oldPlayer).getKnownSpells().forEach(spell -> ((MagicUser) newPlayer).setKnownSpell(Arcanus.SPELL.getId(spell)));
        });

        //-----Command Callback-----//
        CommandRegistrationCallback.EVENT.register(ModCommands::init);
    }

    private static ItemEntry.Builder<?> createItemEntry(ItemStack stack) {
        ItemEntry.Builder<?> builder = ItemEntry.builder(stack.getItem());

        builder.apply(new ArcanusLootFunction.Builder());

        return builder;
    }

    public static void addStructureProcessors(Registry<StructurePool> templatePoolRegistry) {
        templatePoolRegistry.forEach(pool -> pool.elements.forEach(element -> {
            if (element instanceof SinglePoolElement singleElement && singleElement.template.left().isPresent()) {
                String currentElement = singleElement.template.left().get().toString();

                if (ArcanusConfig.structuresWithBookshelves.contains(currentElement) || ArcanusConfig.structuresWithLecterns.contains(currentElement)) {
                    StructureProcessorList originalProcessorList = singleElement.processors.value();
                    List<StructureProcessor> mutableProcessorList = new ArrayList<>(originalProcessorList.getList());

                    if (ArcanusConfig.doLecternProcessor)
                        mutableProcessorList.add(LecternStructureProcessor.INSTANCE);
                    if (ArcanusConfig.doBookshelfProcessor)
                        mutableProcessorList.add(BookshelfReplacerStructureProcessor.INSTANCE);

                    StructureProcessorList newProcessorList = new StructureProcessorList(mutableProcessorList);

                    singleElement.processors = Holder.createDirect(newProcessorList);
                }
            }
        }));
    }
}
