package eu.midnightdust.celestria;

import eu.midnightdust.celestria.config.CelestriaConfig;
import eu.midnightdust.celestria.util.ClientUtils;
import eu.midnightdust.celestria.util.PacketUtils;
import eu.midnightdust.celestria.util.ShootingStarPayload;
import eu.midnightdust.celestria.util.WelcomePayload;

import java.util.HashSet;
import java.util.Set;

import static eu.midnightdust.celestria.Celestria.id;
import static eu.midnightdust.celestria.Celestria.random;

public class CelestriaClient {
    private static boolean clientOnlyMode = true;
    public static Set<ShootingStar> shootingStars = new HashSet<>();

    public static void init() {
        ClientUtils.registerBuiltinResourcePack(id("realistic"));
        ClientUtils.registerBuiltinResourcePack(id("pixelperfect"));
        PacketUtils.registerClientGlobalReceiver(WelcomePayload.PACKET_ID,
                (payload, player) -> {
            PacketUtils.sendPlayPayloadC2S(payload);
            CelestriaClient.clientOnlyMode = false;
        });
        PacketUtils.registerClientGlobalReceiver(ShootingStarPayload.PACKET_ID,
                (payload, player) -> {
                    CelestriaClient.clientOnlyMode = false; // If the welcome packet wasn't received correctly for some reason, disable clientOnlyMode on shooting star occurrence
                    shootingStars.add(new ShootingStar(CelestriaConfig.shootingStarPathLength, payload.type(), payload.x(), payload.y(), payload.rotation(), payload.size()));
                });
        ClientUtils.registerClientTick(true, (client) -> {
            shootingStars.forEach(ShootingStar::tick);
            shootingStars.removeAll(shootingStars.stream().filter(star -> star.progress <= 0).toList());
            if (CelestriaClient.clientOnlyMode && CelestriaConfig.enableShootingStars && client.world != null) {
                float tickDelta = client.getRenderTickCounter().getTickDelta(true);
                if ((180 < client.world.getSkyAngle(tickDelta) * 360 && 270 > client.world.getSkyAngle(tickDelta) * 360) && random.nextInt(Celestria.getChance(client.world)) == 0) {
                    shootingStars.add(new ShootingStar(CelestriaConfig.shootingStarPathLength, random.nextInt(3), random.nextBetween(100, 150), random.nextInt(360), random.nextBetween(10, 170), random.nextBetween(Math.min(CelestriaConfig.shootingStarMinSize, CelestriaConfig.shootingStarMaxSize), Math.max(CelestriaConfig.shootingStarMaxSize, CelestriaConfig.shootingStarMinSize))));
                }
            }
        });
        ClientUtils.registerDisconnectEvent((handler, client) -> CelestriaClient.clientOnlyMode = true);
    }
}
