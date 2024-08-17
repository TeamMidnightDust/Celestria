package eu.midnightdust.celestria.util.fabric;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.world.World;

import java.util.function.Consumer;

public class CommonUtilsImpl {
    public static void registerWorldTickEvent(boolean endTick, Consumer<World> code) {
        if (endTick) ServerTickEvents.END_WORLD_TICK.register(code::accept);
        else ServerTickEvents.START_WORLD_TICK.register(code::accept);
    }
}
