package dev.cammiescorner.arcanus.api.events.client;

import com.mojang.blaze3d.platform.InputUtil;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

public class KeyBindingCallback {
	public static final Event<UnpressedEvent> UNPRESSED = EventFactory.createArrayBacked(UnpressedEvent.class, callbacks -> (key, modifier) -> {
		for(UnpressedEvent callback : callbacks)
			callback.unpress(key, modifier);
	});

	public interface UnpressedEvent {
		void unpress(InputUtil.Key key, int modifiers);
	}
}
