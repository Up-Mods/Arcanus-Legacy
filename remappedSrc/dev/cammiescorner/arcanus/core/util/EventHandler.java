package dev.cammiescorner.arcanus.core.util;

import dev.cammiescorner.arcanus.Arcanus;
import dev.cammiescorner.arcanus.core.registry.ModCommands;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.loot.v1.FabricLootPoolBuilder;
import net.fabricmc.fabric.api.loot.v1.event.LootTableLoadingCallback;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.loot.ConstantLootTableRange;
import net.minecraft.loot.UniformLootTableRange;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.functions.SetNbtFunction;
import java.util.Random;

public class EventHandler
{
	private static final ResourceLocation HUD_ELEMENTS = new ResourceLocation(Arcanus.MOD_ID, "textures/gui/hud_elements.png");
	private static final ResourceLocation DUNGEON_LOOT_TABLE = new ResourceLocation("minecraft", "chests/simple_dungeon");
	private static final ResourceLocation MINESHAFT_LOOT_TABLE = new ResourceLocation("minecraft", "chests/abandoned_mineshaft");
	private static final ResourceLocation TEMPLE_LOOT_TABLE = new ResourceLocation("minecraft", "chests/jungle_temple");
	private static final ResourceLocation PYRAMID_LOOT_TABLE = new ResourceLocation("minecraft", "chests/desert_pyramid");
	private static final ResourceLocation TREASURE_LOOT_TABLE = new ResourceLocation("minecraft", "chests/buried_treasure");
	private static final ResourceLocation STRONGHOLD_LOOT_TABLE = new ResourceLocation("minecraft", "chests/stronghold_library");
	private static final ResourceLocation BASTION_LOOT_TABLE = new ResourceLocation("minecraft", "chests/bastion_treasure");
	private static final ResourceLocation END_CITY_LOOT_TABLE = new ResourceLocation("minecraft", "chests/end_city_treasure");
	private static final ResourceLocation MANSION_LOOT_TABLE = new ResourceLocation("minecraft", "chests/woodland_mansion");
	private static final ResourceLocation FORTRESS_LOOT_TABLE = new ResourceLocation("minecraft", "chests/nether_bridge");
	private static final ResourceLocation RUIN_LOOT_TABLE = new ResourceLocation("minecraft", "chests/underwater_ruin_big");

	private static Minecraft client = Minecraft.getInstance();
	private static Random random = new Random();

	public static void clientEvents()
	{
		//-----HUD Render Callback-----//
		HudRenderCallback.EVENT.register((matrices, tickDelta) ->
		{
			Player player = (Player) client.cameraEntity;
			MagicUser user = (MagicUser) player;
			int mana = user.getMana();
			int maxMana = user.getMaxMana();
			int burnout = user.getBurnout();
			int maxBurnout = user.getMaxBurnout();
			int scaledWidth = client.getWindow().getGuiScaledWidth();
			int scaledHeight = client.getWindow().getGuiScaledHeight();
			int x = scaledWidth / 2 + 82;
			int y = scaledHeight - (player.isCreative() ? 34 : 49);

			client.getTextureManager().bindForSetup(HUD_ELEMENTS);

			for(int i = 0; i < maxMana / 2; i++)
			{
				int halfOrFullOrb = i * 2 + 1;
				GuiComponent.blit(matrices, x - (i * 8), y, 0, 15, 9, 9, 256, 256);

				if(halfOrFullOrb < mana)
				{
					GuiComponent.blit(matrices, x - (i * 8), y, 0, 0, 8, 8, 256, 256);
				}

				if(halfOrFullOrb == mana)
				{
					GuiComponent.blit(matrices, x - (i * 8), y, 8, 0, 8, 8, 256, 256);
				}
			}

			for(int i = 0; i < maxBurnout / 2; i++)
			{
				int halfOrFullOrb = i * 2 + 1;

				if(halfOrFullOrb < burnout)
				{
					GuiComponent.blit(matrices, (x - 72) + (i * 8), y, 16, 0, 8, 8, 256, 256);
				}

				if(halfOrFullOrb == burnout)
				{
					GuiComponent.blit(matrices, (x - 72) + (i * 8), y, 24, 0, 8, 8, 256, 256);
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
						.setRolls(range).withEntry(createItemEntry(selectBook()).build());
				supplier.withPool(poolBuilder.build());
			}
		});

		//-----Copy Player Data Callback-----//
		ServerPlayerEvents.COPY_FROM.register((oldPlayer, newPlayer, alive) ->
		{
			((MagicUser) newPlayer).setBurnout(((MagicUser) oldPlayer).getBurnout());
			((MagicUser) oldPlayer).getKnownSpells().forEach(spell -> ((MagicUser) newPlayer).setKnownSpell(Arcanus.SPELL.getKey(spell)));
		});

		//-----Command Callback-----//
		CommandRegistrationCallback.EVENT.register(ModCommands::init);
	}

	//TODO new loot pool entry
	private static LootItem.Builder<?> createItemEntry(ItemStack stack)
	{
		LootItem.Builder<?> builder = LootItem.lootTableItem(stack.getItem());
		
		if(stack.hasTag())
			builder.apply(SetNbtFunction.setTag(stack.getTag()));
		if(stack.getCount() > 1)
			builder.apply(SetItemCountFunction.setCount(ConstantLootTableRange.create(stack.getCount())));

		return builder;
	}

	private static ItemStack selectBook()
	{
		switch(random.nextInt(Arcanus.SPELL.keySet().size()))
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
