package eu.midnightdust.celestria.util;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.util.Identifier;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class ClientUtils {
    @ExpectPlatform
    public static void registerBuiltinResourcePack(Identifier id) {
        throw new AssertionError();
    }
    @ExpectPlatform
    public static void registerClientTick(boolean endTick, Consumer<MinecraftClient> code) {
        throw new AssertionError();
    }
    @ExpectPlatform
    public static void registerDisconnectEvent(BiConsumer<ClientPlayNetworkHandler, MinecraftClient> function) {
        throw new AssertionError();
    }
}
