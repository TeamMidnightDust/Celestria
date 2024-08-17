package eu.midnightdust.celestria.util.fabric;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.function.BiConsumer;

public class PacketUtilsImpl {
    // Common
    public static <T extends CustomPayload> void registerPayloadCommon(CustomPayload.Id<T> id, PacketCodec<RegistryByteBuf, T> codec) {
        PayloadTypeRegistry.playC2S().register(id, codec);
        PayloadTypeRegistry.playS2C().register(id, codec);
    }

    // Server
    public static <T extends CustomPayload> void registerPayloadS2C(CustomPayload.Id<T> id, PacketCodec<RegistryByteBuf, T> codec) {
        PayloadTypeRegistry.playS2C().register(id, codec);
    }
    public static void sendPlayPayloadS2C(ServerPlayerEntity player, CustomPayload payload) {
        ServerPlayNetworking.send(player, payload);
    }
    public static void registerServerGlobalReceiver(CustomPayload.Id<?> type, BiConsumer<CustomPayload, PlayerEntity> code) {
        ServerPlayNetworking.registerGlobalReceiver(type, (payload, context) -> code.accept(payload, context.player()));
    }

    // Client
    public static <T extends CustomPayload> void registerPayloadC2S(CustomPayload.Id<T> id, PacketCodec<RegistryByteBuf, T> codec) {
        PayloadTypeRegistry.playC2S().register(id, codec);
    }
    public static void sendPlayPayloadC2S(CustomPayload payload) {
        ClientPlayNetworking.send(payload);
    }
    public static void registerClientGlobalReceiver(CustomPayload.Id<?> type, BiConsumer<CustomPayload, PlayerEntity> code) {
        ClientPlayNetworking.registerGlobalReceiver(type, (payload, context) -> code.accept(payload, context.player()));
    }
}
