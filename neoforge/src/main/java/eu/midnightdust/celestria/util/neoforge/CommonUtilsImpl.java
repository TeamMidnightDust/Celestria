package eu.midnightdust.celestria.util.neoforge;

import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.LevelTickEvent;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

import static eu.midnightdust.celestria.Celestria.MOD_ID;

public class CommonUtilsImpl {
    static Set<Consumer<World>> startWorldTickEvents = new HashSet<>();
    static Set<Consumer<World>> endWorldTickEvents = new HashSet<>();

    public static void registerWorldTickEvent(boolean endTick, Consumer<World> code) {
        if (endTick) endWorldTickEvents.add(code);
        else startWorldTickEvents.add(code);
    }
    @EventBusSubscriber(modid = MOD_ID, bus = EventBusSubscriber.Bus.GAME)
    public class GameEvents {
        @SubscribeEvent
        public static void startWorldTick(LevelTickEvent.Pre event) {
            startWorldTickEvents.forEach(code -> code.accept(event.getLevel()));
        }
        @SubscribeEvent
        public static void endWorldTick(LevelTickEvent.Pre event) {
            endWorldTickEvents.forEach(code -> code.accept(event.getLevel()));
        }
    }
}
