package eu.midnightdust.celestria.fabric;

import eu.midnightdust.celestria.Celestria;
import eu.midnightdust.celestria.CelestriaClient;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;

public class CelestriaFabric implements ModInitializer, ClientModInitializer {
    @Override
    public void onInitialize() {
        Celestria.init();
    }

    @Override
    public void onInitializeClient() {
        CelestriaClient.init();
    }
}
