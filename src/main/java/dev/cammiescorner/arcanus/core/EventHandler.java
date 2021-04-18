package dev.cammiescorner.arcanus.core;

import dev.cammiescorner.arcanus.Arcanus;
import dev.cammiescorner.arcanus.core.registry.ModCommands;
import dev.cammiescorner.arcanus.core.util.MagicUser;
import dev.cammiescorner.arcanus.core.util.SpellBooks;
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
import net.minecraft.loot.ConstantLootTableRange;
import net.minecraft.loot.UniformLootTableRange;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.function.SetNbtLootFunction;
import net.minecraft.util.Identifier;

import java.util.Random;

public class EventHandler
{
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

	private static MinecraftClient client = MinecraftClient.getInstance();
	private static Random random = new Random();

	public static void clientEvents()
	{
		//-----HUD Render Callback-----//
		HudRenderCallback.EVENT.register((matrices, tickDelta) ->
		{
			PlayerEntity player = (PlayerEntity) client.cameraEntity;
			MagicUser user = (MagicUser) player;
			int mana = user.getMana();
			int maxMana = user.getMaxMana();
			int burnout = user.getBurnout();
			int maxBurnout = user.getMaxBurnout();
			int scaledWidth = client.getWindow().getScaledWidth();
			int scaledHeight = client.getWindow().getScaledHeight();
			int x = scaledWidth / 2 + 82;
			int y = scaledHeight - 49;

			client.getTextureManager().bindTexture(HUD_ELEMENTS);

			for(int i = 0; i < maxMana / 2; i++)
			{
				int halfOrFullOrb = i * 2 + 1;
				DrawableHelper.drawTexture(matrices, x - (i * 8), y, 0, 15, 9, 9, 256, 256);

				if(halfOrFullOrb < mana)
				{
					DrawableHelper.drawTexture(matrices, x - (i * 8), y, 0, 0, 8, 8, 256, 256);
				}

				if(halfOrFullOrb == mana)
				{
					DrawableHelper.drawTexture(matrices, x - (i * 8), y, 8, 0, 8, 8, 256, 256);
				}
			}

			for(int i = 0; i < maxBurnout / 2; i++)
			{
				int halfOrFullOrb = i * 2 + 1;

				if(halfOrFullOrb < burnout)
				{
					DrawableHelper.drawTexture(matrices, (x - 72) + (i * 8), y, 16, 0, 8, 8, 256, 256);
				}

				if(halfOrFullOrb == burnout)
				{
					DrawableHelper.drawTexture(matrices, (x - 72) + (i * 8), y, 24, 0, 8, 8, 256, 256);
				}
			}
		});
	}

	public static void commonEvents()
	{
		UniformLootTableRange range = UniformLootTableRange.between(0, 1);

		//-----Loot Table Callback-----//
		LootTableLoadingCallback.EVENT.register((resourceManager, lootManager, id, supplier, setter) ->
		{
			if(DUNGEON_LOOT_TABLE.equals(id) || MINESHAFT_LOOT_TABLE.equals(id) || TEMPLE_LOOT_TABLE.equals(id) ||
				PYRAMID_LOOT_TABLE.equals(id) || TREASURE_LOOT_TABLE.equals(id) || STRONGHOLD_LOOT_TABLE.equals(id) ||
				BASTION_LOOT_TABLE.equals(id) || END_CITY_LOOT_TABLE.equals(id) || MANSION_LOOT_TABLE.equals(id) ||
				FORTRESS_LOOT_TABLE.equals(id) || RUIN_LOOT_TABLE.equals(id))
			{
				FabricLootPoolBuilder poolBuilder = FabricLootPoolBuilder.builder()
						.rolls(range).withEntry(createItemEntry(selectBook()).build());
				supplier.withPool(poolBuilder.build());
			}
		});

		//-----Copy Player Data Callback-----//
		ServerPlayerEvents.COPY_FROM.register((oldPlayer, newPlayer, alive) ->
		{
			((MagicUser) newPlayer).setBurnout(((MagicUser) oldPlayer).getBurnout());
			((MagicUser) oldPlayer).getKnownSpells().forEach(spell -> ((MagicUser) newPlayer).setKnownSpell(Arcanus.SPELL.getId(spell)));
		});

		//-----Command Callback-----//
		CommandRegistrationCallback.EVENT.register(ModCommands::init);
	}

	private static ItemEntry.Builder<?> createItemEntry(ItemStack stack)
	{
		ItemEntry.Builder<?> builder = ItemEntry.builder(stack.getItem());

		if(stack.hasTag())
			builder.apply(SetNbtLootFunction.builder(stack.getTag()));
		if(stack.getCount() > 1)
			builder.apply(SetCountLootFunction.builder(ConstantLootTableRange.create(stack.getCount())));

		return builder;
	}

	private static ItemStack selectBook()
	{
		switch(random.nextInt(Arcanus.SPELL.getIds().size()))
		{
			case 0:
				return SpellBooks.getLungeBook();
			case 1:
				return SpellBooks.getFissureBook();
			case 2:
				return SpellBooks.getMagicMissileBook();
			case 3:
				return SpellBooks.getVanishBook();
			case 4:
				return SpellBooks.getHealBook();
			case 5:
				return SpellBooks.getMeteorBook();
			case 6:
				return SpellBooks.getIceSpireBook();
			case 7:
				return SpellBooks.getMineBook();
		}

		return new ItemStack(Items.AIR);
	}
}
