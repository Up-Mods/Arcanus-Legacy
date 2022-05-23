package dev.cammiescorner.arcanus.common;

import dev.cammiescorner.arcanus.api.ArcanusHelper;
import dev.cammiescorner.arcanus.common.components.world.AltarStructureComponent;
import dev.cammiescorner.arcanus.common.registry.ArcanusCommands;
import dev.cammiescorner.arcanus.common.registry.ArcanusComponents;
import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import net.minecraft.world.World;
import org.quiltmc.qsl.command.api.CommandRegistrationCallback;
import org.quiltmc.qsl.lifecycle.api.event.ServerWorldLoadEvents;
import org.quiltmc.qsl.networking.api.ServerPlayConnectionEvents;
import org.quiltmc.qsl.resource.loader.api.ResourceLoaderEvents;

public class CommonEvents {
	public static void events() {
		CommandRegistrationCallback.EVENT.register(ArcanusCommands::init);

		ServerWorldLoadEvents.LOAD.register((server, world) -> ArcanusHelper.constructStructureMap(world));
		ResourceLoaderEvents.END_DATA_PACK_RELOAD.register((server, manager, success) -> {
			if(server != null)
				server.getWorlds().forEach(ArcanusHelper::constructStructureMap);
		});

		ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
			World world = handler.player.world;
			ComponentKey<AltarStructureComponent> key = ArcanusComponents.ALTAR_STRUCTURE_COMPONENT;
			key.sync(world, key.get(world), player -> player.getUuid().equals(handler.player.getUuid()));
		});
	}
}
