package eu.midnightdust.celestria.neoforge;

import eu.midnightdust.celestria.Celestria;
import eu.midnightdust.celestria.CelestriaClient;
import eu.midnightdust.celestria.effect.StatusEffectInit;
import net.minecraft.registry.BuiltinRegistries;
import net.minecraft.registry.Registries;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.neoforged.neoforge.registries.RegisterEvent;

import static eu.midnightdust.celestria.Celestria.MOD_ID;
import static eu.midnightdust.celestria.Celestria.id;

@Mod(value = MOD_ID)
public class CelestriaNeoForge {
    public CelestriaNeoForge() {
        Celestria.init();
    }

    @Mod(value = MOD_ID, dist = Dist.CLIENT)
    public static class CelestriaClientNeoforge {
        public CelestriaClientNeoforge() {
            CelestriaClient.init();
        }
    }
    @EventBusSubscriber(modid = MOD_ID, bus = EventBusSubscriber.Bus.MOD)
    public class GameEvents {
        @SubscribeEvent
        public static void register(RegisterEvent event) {
            event.register(
                    Registries.STATUS_EFFECT.getKey(),
                    registry -> {
                        registry.register(id("insomnia"), StatusEffectInit.INSOMNIA);
                        StatusEffectInit.INSOMNIA_EFFECT = Registries.STATUS_EFFECT.getEntry(StatusEffectInit.INSOMNIA);
                    }
            );
        }
    }
}