package dev.cammiescorner.arcanus.util;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.cammiescorner.arcanus.Arcanus;
import dev.cammiescorner.arcanus.component.base.MagicCaster;
import dev.cammiescorner.arcanus.client.ArcanusClient;
import dev.cammiescorner.arcanus.component.ArcanusComponents;
import dev.cammiescorner.arcanus.item.WandItem;
import dev.cammiescorner.arcanus.loot.function.SetSpellBookNbtLootFunction;
import dev.cammiescorner.arcanus.registry.ArcanusEntityAttributes;
import dev.cammiescorner.arcanus.structure.processor.BookshelfReplacerStructureProcessor;
import dev.cammiescorner.arcanus.structure.processor.LecternStructureProcessor;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.levelgen.structure.pools.SinglePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import org.quiltmc.loader.api.QuiltLoader;
import org.quiltmc.loader.api.minecraft.ClientOnly;
import org.quiltmc.qsl.lifecycle.api.event.ServerLifecycleEvents;

import java.util.ArrayList;
import java.util.List;

public class EventHandler {
    private static final ResourceLocation HUD_ELEMENTS = Arcanus.id("textures/gui/hud_elements.png");

    @ClientOnly
    public static void clientEvents() {
        final Minecraft client = Minecraft.getInstance();

        HudRenderCallback.EVENT.register((matrices, tickDelta) -> {
            if (client.cameraEntity instanceof Player player && !player.isSpectator() && !player.isCreative()) {
                MagicCaster caster = player.getComponent(ArcanusComponents.MAGIC_CASTER);

                int mana = Math.min(caster.getMana(), caster.getMaxMana() - caster.getBurnout());
                int burnout = caster.getBurnout();
                int manaLock = ArcanusEntityAttributes.getManaLock(player);

                if (player.getMainHandItem().getItem() instanceof WandItem || mana < caster.getMaxMana())
                    ArcanusClient.manaTimer = Math.min(ArcanusClient.manaTimer + 1, 40);
                else
                    ArcanusClient.manaTimer = Math.max(ArcanusClient.manaTimer - 1, 0);

                if (ArcanusClient.shouldRenderManaBar()) {
                    int scaledWidth = client.getWindow().getGuiScaledWidth();
                    int scaledHeight = client.getWindow().getGuiScaledHeight();
                    int x = scaledWidth / 2 + 82;
                    int y = scaledHeight - (player.isCreative() ? 34 : 49);
                    float alpha = ArcanusClient.manaTimer > 20 ? 1F : ArcanusClient.manaTimer / 20F;

                    RenderSystem.enableBlend();
                    RenderSystem.setShaderTexture(0, HUD_ELEMENTS);
                    RenderSystem.setShaderColor(1F, 1F, 1F, alpha);

                    // Draw background
                    for (int i = 0; i < 10; i++)
                        GuiComponent.blit(matrices, x - (i * 8), y, 0, 15, 9, 9, 256, 256);

                    // Draw full mana orb
                    for (int i = 0; i < mana / 2; i++)
                        GuiComponent.blit(matrices, x - (i * 8), y, 0, 0, 8, 8, 256, 256);

                    // Draw half mana orb
                    if (mana % 2 == 1)
                        GuiComponent.blit(matrices, x - ((mana / 2) * 8), y, 8, 0, 8, 8, 256, 256);

                    boolean manaLockOdd = manaLock % 2 == 1;
                    boolean burnoutOdd = burnout % 2 == 1;
                    int adjustedBurnout = manaLockOdd && burnoutOdd ? burnout / 2 + 1 : burnout / 2;
                    int adjustedManaLock = (manaLock / 2) * 8;
                    x -= 72;

                    // Draw full burnout orb
                    for (int i = 0; i < adjustedBurnout; i++)
                        if (manaLockOdd && i == 0)
                            GuiComponent.blit(matrices, x + adjustedManaLock, y, 32, 0, 8, 8, 256, 256);
                        else
                            GuiComponent.blit(matrices, x + adjustedManaLock + (i * 8), y, 16, 0, 8, 8, 256, 256);

                    // Draw half burnout orb
                    if (burnoutOdd != manaLockOdd && burnout > 0)
                        GuiComponent.blit(matrices, x + adjustedManaLock + (adjustedBurnout * 8), y, 24, 0, 8, 8, 256, 256);

                    // Draw full mana lock orb
                    for (int i = 0; i < manaLock / 2; i++)
                        GuiComponent.blit(matrices, x + (i * 8), y, 40, 0, 8, 8, 256, 256);

                    // Draw half mana lock orb
                    if (manaLock % 2 == 1)
                        GuiComponent.blit(matrices, x + adjustedManaLock, y, 48, 0, 8, 8, 256, 256);
                }
            }
        });
    }

    public static void commonEvents() {
        ServerLifecycleEvents.STARTING.register(server -> EventHandler.addStructureProcessors(server.registryAccess().registryOrThrow(Registries.TEMPLATE_POOL)));

        if(ArcanusConfig.ruinedPortalsHaveBooks) {
            LootTableEvents.MODIFY.register((resourceManager, lootManager, id, tableBuilder, tableSource) -> {
                if (BuiltInLootTables.RUINED_PORTAL.equals(id)) {
                    LootPool.Builder poolBuilder = LootPool.lootPool().setRolls(ConstantValue.exactly(1)).conditionally(LootItemRandomChanceCondition.randomChance(0.1F).build()).with(createItemEntry(new ItemStack(Items.WRITTEN_BOOK)).build());
                    tableBuilder.withPool(poolBuilder);
                }
            });
        }

        if (ArcanusConfig.strongholdsHaveBooks && !QuiltLoader.isModLoaded("betterstrongholds")) {
            LootTableEvents.MODIFY.register((resourceManager, lootManager, id, tableBuilder, tableSource) -> {
                if (BuiltInLootTables.STRONGHOLD_LIBRARY.equals(id)) {
                    LootPool.Builder poolBuilder = LootPool.lootPool().setRolls(ConstantValue.exactly(4)).conditionally(LootItemRandomChanceCondition.randomChance(0.5F).build()).with(createItemEntry(new ItemStack(Items.WRITTEN_BOOK)).build());
                    tableBuilder.withPool(poolBuilder);
                }
            });
        }
    }

    private static LootItem.Builder<?> createItemEntry(ItemStack stack) {
        LootItem.Builder<?> builder = LootItem.lootTableItem(stack.getItem());

        builder.apply(new SetSpellBookNbtLootFunction.Builder());

        return builder;
    }

    //TODO make tag-like thing
    public static void addStructureProcessors(Registry<StructureTemplatePool> templatePoolRegistry) {
        templatePoolRegistry.forEach(pool -> pool.templates.forEach(element -> {
            if (element instanceof SinglePoolElement singleElement && singleElement.template.left().isPresent()) {
                String currentElement = singleElement.template.left().get().toString();
                StructureProcessorList originalProcessorList = singleElement.processors.value();
                List<StructureProcessor> mutableProcessorList = new ArrayList<>(originalProcessorList.list());

                if (ArcanusConfig.doLecternProcessor && !ArcanusConfig.excludeStructuresWithLecterns.contains(currentElement))
                    mutableProcessorList.add(LecternStructureProcessor.INSTANCE);
                if (ArcanusConfig.doBookshelfProcessor && !ArcanusConfig.excludeStructuresWithBookshelves.contains(currentElement))
                    mutableProcessorList.add(BookshelfReplacerStructureProcessor.INSTANCE);

                StructureProcessorList newProcessorList = new StructureProcessorList(mutableProcessorList);

                singleElement.processors = Holder.direct(newProcessorList);
            }
        }));
    }
}
