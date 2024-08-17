package eu.midnightdust.celestria.util;

import eu.midnightdust.celestria.Celestria;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;

public record ShootingStarPayload(int x, int y, int type, int rotation, int size) implements CustomPayload {
    public static final CustomPayload.Id<ShootingStarPayload> PACKET_ID = new CustomPayload.Id<>(Celestria.id("shooting_star"));
    public static final PacketCodec<RegistryByteBuf, ShootingStarPayload> codec = PacketCodec.of(ShootingStarPayload::write, ShootingStarPayload::read);

    public static ShootingStarPayload read(RegistryByteBuf buf) {
        return new ShootingStarPayload(buf.readInt(), buf.readInt(), buf.readInt(), buf.readInt(), buf.readInt());
    }

    public void write(RegistryByteBuf buf) {
        buf.writeInt(x).writeInt(y).writeInt(type).writeInt(rotation).writeInt(size);
    }

    @Override
    public CustomPayload.Id<? extends CustomPayload> getId() {
        return PACKET_ID;
    }
}
