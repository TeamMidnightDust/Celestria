package eu.midnightdust.celestria.util;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.world.World;

import java.util.function.Consumer;

public class CommonUtils {
    @ExpectPlatform
    public static void registerWorldTickEvent(boolean endTick, Consumer<World> code) {
        throw new AssertionError();
    }
}
