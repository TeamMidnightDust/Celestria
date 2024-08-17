package eu.midnightdust.celestria.util.fabric;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.util.Identifier;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static eu.midnightdust.celestria.Celestria.MOD_ID;

public class ClientUtilsImpl {
    public static void registerBuiltinResourcePack(Identifier id) {
        FabricLoader.getInstance().getModContainer(MOD_ID).ifPresent(container -> {
            ResourceManagerHelper.registerBuiltinResourcePack(id, container, ResourcePackActivationType.NORMAL);
        });
    }
    public static void registerClientTick(boolean endTick, Consumer<MinecraftClient> code) {
        if (endTick) ClientTickEvents.END_CLIENT_TICK.register(code::accept);
        else ClientTickEvents.START_CLIENT_TICK.register(code::accept);
    }
    public static void registerDisconnectEvent(BiConsumer<ClientPlayNetworkHandler, MinecraftClient> code) {
        ClientPlayConnectionEvents.DISCONNECT.register(code::accept);
    }
}
