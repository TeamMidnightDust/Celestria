package eu.midnightdust.celestria.util;

import eu.midnightdust.celestria.Celestria;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;

public record WelcomePayload() implements CustomPayload {
    public static final CustomPayload.Id<WelcomePayload> PACKET_ID = new CustomPayload.Id<>(Celestria.id("welcome"));
    public static final PacketCodec<RegistryByteBuf, WelcomePayload> codec = PacketCodec.of((a, b) -> {}, buf -> new WelcomePayload());

    @Override
    public Id<? extends CustomPayload> getId() {
        return PACKET_ID;
    }
}
