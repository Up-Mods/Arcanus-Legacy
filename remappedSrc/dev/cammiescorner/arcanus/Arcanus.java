package dev.cammiescorner.arcanus;

import dev.cammiescorner.arcanus.common.packets.CastSpellMessage;
import dev.cammiescorner.arcanus.core.util.Spell;
import dev.cammiescorner.arcanus.core.util.EventHandler;
import dev.cammiescorner.arcanus.core.config.ArcanusConfig;
import dev.cammiescorner.arcanus.core.registry.ModItems;
import dev.cammiescorner.arcanus.core.registry.ModSpells;
import dev.cammiescorner.arcanus.core.util.Pattern;
import dev.cammiescorner.arcanus.core.util.SpellBooks;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.ChatFormatting;
import net.minecraft.core.NonNullList;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class Arcanus implements ModInitializer
{
	//-----Custom Registries-----//
	public static final Registry<Spell> SPELL = createRegistry("spell", Spell.class);

	public static final String MOD_ID = "arcanus";
	public static final Logger LOGGER = LogManager.getLogger(MOD_ID);
	public static final CreativeModeTab ITEM_GROUP = FabricItemGroupBuilder.create(new ResourceLocation(MOD_ID, "general")).appendItems(list ->
	{
		Registry.ITEM.forEach(item -> item.fillItemCategory(Arcanus.ITEM_GROUP, (NonNullList<ItemStack>) list));
		Arcanus.SPELL.forEach(spell -> list.add(SpellBooks.getBookFromSpell(spell)));
	}).icon(() -> new ItemStack(ModItems.WAND)).build();
	public static ArcanusConfig config;

	@Override
	public void onInitialize()
	{
		AutoConfig.register(ArcanusConfig.class, GsonConfigSerializer::new);
		config = AutoConfig.getConfigHolder(ArcanusConfig.class).getConfig();

		ServerPlayNetworking.registerGlobalReceiver(CastSpellMessage.ID, CastSpellMessage::handle);

		ModItems.register();
		ModSpells.register();

		EventHandler.commonEvents();

		LOGGER.info("eee");
	}

	@SuppressWarnings("unchecked")
	private static <T> Registry<T> createRegistry(String name, Class<?> clazz)
	{
		Registry<?> registry = FabricRegistryBuilder.createSimple(clazz, new ResourceLocation(MOD_ID, name)).buildAndRegister();
		return (Registry<T>) registry;
	}

	public static MutableComponent getSpellInputs(List<Pattern> pattern, int index)
	{
		return index >= pattern.size() || pattern.get(index) == null ? new TextComponent("?").withStyle(ChatFormatting.GRAY, ChatFormatting.UNDERLINE) :
				new TextComponent(pattern.get(index).getSymbol()).withStyle(ChatFormatting.GREEN);
	}
}
