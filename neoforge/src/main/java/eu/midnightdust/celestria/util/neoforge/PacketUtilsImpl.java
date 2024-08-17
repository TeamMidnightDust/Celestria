package eu.midnightdust.celestria.util.neoforge;

import eu.midnightdust.lib.util.PlatformFunctions;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.NetworkSide;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

import static eu.midnightdust.celestria.Celestria.MOD_ID;

public class PacketUtilsImpl {
    static Map<CustomPayload.Id, PayloadStorage> payloads = new HashMap<>();
    private record PayloadStorage <T extends CustomPayload> (boolean client, boolean server, PacketCodec<RegistryByteBuf, T> codec, BiConsumer<CustomPayload, PlayerEntity> clientReceiver, BiConsumer<CustomPayload, PlayerEntity> serverReceiver) {}
    // Common
    public static <T extends CustomPayload> void registerPayloadCommon(CustomPayload.Id<T> id, PacketCodec<RegistryByteBuf, T> codec) {
        payloads.put(id, new PayloadStorage<>(true, true, codec, (p, e) -> {}, (p, e) -> {}));
    }

    // Server
    public static <T extends CustomPayload> void registerPayloadS2C(CustomPayload.Id<T> id, PacketCodec<RegistryByteBuf, T> codec) {
        payloads.put(id, new PayloadStorage<>(false, true, codec, (p, e) -> {}, (p, e) -> {}));
    }
    public static void sendPlayPayloadS2C(ServerPlayerEntity player, CustomPayload payload) {
        player.networkHandler.send(payload);
    }
    public static void registerServerGlobalReceiver(CustomPayload.Id<?> type, BiConsumer<CustomPayload, PlayerEntity> code) {
        payloads.compute(type, (k, data) -> new PayloadStorage<>(data.client, data.server, data.codec, data.clientReceiver, code));
    }

    // Client
    public static <T extends CustomPayload> void registerPayloadC2S(CustomPayload.Id<T> id, PacketCodec<RegistryByteBuf, T> codec) {
        payloads.put(id, new PayloadStorage<>(true, false, codec, (p, e) -> {}, (p, e) -> {}));
    }
    public static void sendPlayPayloadC2S(CustomPayload payload) {
        if (PlatformFunctions.isClientEnv() && ClientUtilsImpl.client.getNetworkHandler() != null) ClientUtilsImpl.client.getNetworkHandler().send(payload);
    }
    public static void registerClientGlobalReceiver(CustomPayload.Id<?> type, BiConsumer<CustomPayload, PlayerEntity> code) {
        payloads.compute(type, (k, data) -> new PayloadStorage<>(data.client, data.server, data.codec, code, data.serverReceiver));
    }

    @EventBusSubscriber(modid = MOD_ID, bus = EventBusSubscriber.Bus.MOD)
    public class CommonEvents {
        @SubscribeEvent
        public static void registerPayloads(RegisterPayloadHandlersEvent event) {
            PayloadRegistrar registrar = event.registrar("1");
            payloads.forEach((id, payload) -> {
                if (payload.client && payload.server) {
                    registrar.playBidirectional(id, payload.codec, (data, context) -> {
                        if (context.flow() == NetworkSide.CLIENTBOUND) payload.clientReceiver.accept(data, context.player());
                        else payload.serverReceiver.accept(data, context.player());
                    });
                }
                else if (payload.client) {
                    registrar.playToServer(id, payload.codec, (data, context) -> {
                        payload.clientReceiver.accept(data, context.player());
                    });
                }
                else {
                    registrar.playToClient(id, payload.codec, (data, context) -> {
                        payload.clientReceiver.accept(data, context.player());
                    });
                }
            });
        }
    }
}
