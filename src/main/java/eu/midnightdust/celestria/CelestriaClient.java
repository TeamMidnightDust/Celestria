package eu.midnightdust.celestria;

import eu.midnightdust.celestria.config.CelestriaConfig;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Identifier;

public class CelestriaClient implements ClientModInitializer {
    private static boolean clientOnlyMode = true;
    public static int shootingStarProgress = 0;
    public static int shootingStarType = 0;
    public static int shootingStarX = 0;
    public static int shootingStarY = 0;

    @Override
    public void onInitializeClient() {
        FabricLoader.getInstance().getModContainer(Celestria.MOD_ID).ifPresent(modContainer -> {
            ResourceManagerHelper.registerBuiltinResourcePack(new Identifier(Celestria.MOD_ID,"realistic"), modContainer, ResourcePackActivationType.NORMAL);
            ResourceManagerHelper.registerBuiltinResourcePack(new Identifier(Celestria.MOD_ID,"pixelperfect"), modContainer, ResourcePackActivationType.NORMAL);
        });
        ClientPlayNetworking.registerGlobalReceiver(Celestria.SHOOTING_STAR_PACKET,
                (client, handler, attachedData, packetSender) -> {
                    int[] array = attachedData.readIntArray(3);
                    client.execute(() -> {
                        if (client.world != null) {
                            CelestriaClient.clientOnlyMode = false; // If the welcome packet wasn't received correctly for some reason, disable clientOnlyMode on shooting star occurrence
                            CelestriaClient.shootingStarX = array[0];
                            CelestriaClient.shootingStarY = array[1];
                            CelestriaClient.shootingStarType = array[2];
                            CelestriaClient.shootingStarProgress = 100 + shootingStarType * 10;
                        }
                    });
                });
        ClientPlayNetworking.registerGlobalReceiver(Celestria.WELCOME_PACKET,
                (client, handler, attachedData, packetSender) -> client.execute(() -> CelestriaClient.clientOnlyMode = false));
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (shootingStarProgress > 0) --shootingStarProgress;
            if (CelestriaClient.clientOnlyMode && client.world != null) {
                if (Celestria.shootingStarCooldown > 0) --Celestria.shootingStarCooldown;
                if (client.world.isNight() && Celestria.shootingStarCooldown <= 0 && client.world.random.nextInt(CelestriaConfig.shootingStarChance) == 0) {
                    CelestriaClient.shootingStarX = client.world.random.nextBetween(100, 150);
                    CelestriaClient.shootingStarY = client.world.random.nextInt(360);
                    CelestriaClient.shootingStarType = client.world.random.nextInt(3);
                    CelestriaClient.shootingStarProgress = 100 + shootingStarType * 10;
                    Celestria.shootingStarCooldown = CelestriaConfig.shootingStarCooldownLength;
                }
            }
        });
        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> CelestriaClient.clientOnlyMode = true);
    }
}
