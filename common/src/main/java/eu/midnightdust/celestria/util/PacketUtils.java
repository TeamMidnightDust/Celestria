package eu.midnightdust.celestria.util;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.function.BiConsumer;

public class PacketUtils {
    // Common
    @ExpectPlatform
    public static <T extends CustomPayload> void registerPayloadCommon(CustomPayload.Id<T> id, PacketCodec<RegistryByteBuf, T> codec) {
        throw new AssertionError();
    }

    // Server
    @ExpectPlatform
    public static <T extends CustomPayload> void registerPayloadS2C(CustomPayload.Id<T> id, PacketCodec<RegistryByteBuf, T> codec) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static void sendPlayPayloadS2C(ServerPlayerEntity player, CustomPayload payload) {
        throw new AssertionError();
    }
    @ExpectPlatform
    public static <T extends CustomPayload> void registerServerGlobalReceiver(CustomPayload.Id<T> id, BiConsumer<T, PlayerEntity> code) {
        throw new AssertionError();
    }

    // Client
    @ExpectPlatform
    public static <T extends CustomPayload> void registerPayloadC2S(CustomPayload.Id<T> id, PacketCodec<RegistryByteBuf, T> codec) {
        throw new AssertionError();
    }
    @ExpectPlatform
    public static void sendPlayPayloadC2S(CustomPayload payload) {
        throw new AssertionError();
    }
    @ExpectPlatform
    public static <T extends CustomPayload> void registerClientGlobalReceiver(CustomPayload.Id<T> id, BiConsumer<T, PlayerEntity> code) {
        throw new AssertionError();
    }
}
