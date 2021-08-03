package dev.cammiescorner.arcanus.core.util;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.cammiescorner.arcanus.Arcanus;
import dev.cammiescorner.arcanus.core.registry.ModCommands;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.loot.v1.FabricLootPoolBuilder;
import net.fabricmc.fabric.api.loot.v1.event.LootTableLoadingCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.condition.RandomChanceLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.util.Identifier;

public class EventHandler {
	private static final Identifier HUD_ELEMENTS = new Identifier(Arcanus.MOD_ID, "textures/gui/hud_elements.png");
	private static final Identifier DUNGEON_LOOT_TABLE = new Identifier("minecraft", "chests/simple_dungeon");
	private static final Identifier MINESHAFT_LOOT_TABLE = new Identifier("minecraft", "chests/abandoned_mineshaft");
	private static final Identifier TEMPLE_LOOT_TABLE = new Identifier("minecraft", "chests/jungle_temple");
	private static final Identifier PYRAMID_LOOT_TABLE = new Identifier("minecraft", "chests/desert_pyramid");
	private static final Identifier TREASURE_LOOT_TABLE = new Identifier("minecraft", "chests/buried_treasure");
	private static final Identifier STRONGHOLD_LOOT_TABLE = new Identifier("minecraft", "chests/stronghold_library");
	private static final Identifier BASTION_LOOT_TABLE = new Identifier("minecraft", "chests/bastion_treasure");
	private static final Identifier END_CITY_LOOT_TABLE = new Identifier("minecraft", "chests/end_city_treasure");
	private static final Identifier MANSION_LOOT_TABLE = new Identifier("minecraft", "chests/woodland_mansion");
	private static final Identifier FORTRESS_LOOT_TABLE = new Identifier("minecraft", "chests/nether_bridge");
	private static final Identifier RUIN_LOOT_TABLE = new Identifier("minecraft", "chests/underwater_ruin_big");

	@Environment(EnvType.CLIENT)
	public static void clientEvents() {
		final MinecraftClient client = MinecraftClient.getInstance();

		//-----HUD Render Callback-----//
		HudRenderCallback.EVENT.register((matrices, tickDelta) -> {
			PlayerEntity player = (PlayerEntity) client.cameraEntity;

			if(player != null) {
				MagicUser user = (MagicUser) player;
				int mana = user.getMana();
				int maxMana = user.getMaxMana();
				int burnout = user.getBurnout();
				int maxBurnout = user.getMaxBurnout();
				int scaledWidth = client.getWindow().getScaledWidth();
				int scaledHeight = client.getWindow().getScaledHeight();
				int x = scaledWidth / 2 + 82;
				int y = scaledHeight - (player.isCreative() ? 34 : 49);

				RenderSystem.setShaderTexture(0, HUD_ELEMENTS);

				for(int i = 0; i < maxMana / 2; i++) {
					int halfOrFullOrb = i * 2 + 1;
					DrawableHelper.drawTexture(matrices, x - (i * 8), y, 0, 15, 9, 9, 256, 256);

					if(halfOrFullOrb < mana) {
						DrawableHelper.drawTexture(matrices, x - (i * 8), y, 0, 0, 8, 8, 256, 256);
					}

					if(halfOrFullOrb == mana) {
						DrawableHelper.drawTexture(matrices, x - (i * 8), y, 8, 0, 8, 8, 256, 256);
					}
				}

				for(int i = 0; i < maxBurnout / 2; i++) {
					int halfOrFullOrb = i * 2 + 1;

					if(halfOrFullOrb < burnout) {
						DrawableHelper.drawTexture(matrices, (x - 72) + (i * 8), y, 16, 0, 8, 8, 256, 256);
					}

					if(halfOrFullOrb == burnout) {
						DrawableHelper.drawTexture(matrices, (x - 72) + (i * 8), y, 24, 0, 8, 8, 256, 256);
					}
				}
			}
		});
	}

	public static void commonEvents() {
		//-----Loot Table Callback-----//
		LootTableLoadingCallback.EVENT.register((resourceManager, lootManager, id, supplier, setter) -> {
			if(DUNGEON_LOOT_TABLE.equals(id) || MINESHAFT_LOOT_TABLE.equals(id) || TEMPLE_LOOT_TABLE.equals(id) || PYRAMID_LOOT_TABLE.equals(id) || TREASURE_LOOT_TABLE.equals(id) || STRONGHOLD_LOOT_TABLE.equals(id) || BASTION_LOOT_TABLE.equals(id) || END_CITY_LOOT_TABLE.equals(id) || MANSION_LOOT_TABLE.equals(id) || FORTRESS_LOOT_TABLE.equals(id) || RUIN_LOOT_TABLE.equals(id)) {
				FabricLootPoolBuilder poolBuilder = FabricLootPoolBuilder.builder().rolls(ConstantLootNumberProvider.create(1)).withCondition(RandomChanceLootCondition.builder(0.15F).build()).withEntry(createItemEntry(new ItemStack(Items.WRITTEN_BOOK)).build());
				supplier.withPool(poolBuilder.build());
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

		if(stack.getCount() > 1)
			builder.apply(SetCountLootFunction.builder(ConstantLootNumberProvider.create(stack.getCount())));

		return builder;
	}
}
