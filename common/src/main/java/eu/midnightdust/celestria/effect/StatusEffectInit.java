package eu.midnightdust.celestria.effect;

import eu.midnightdust.celestria.config.CelestriaConfig;
import eu.midnightdust.celestria.util.PolymerUtils;
import eu.midnightdust.lib.util.MidnightColorUtil;
import eu.midnightdust.lib.util.PlatformFunctions;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;

import static eu.midnightdust.celestria.Celestria.id;

public class StatusEffectInit {
    public static final StatusEffect INSOMNIA = initInsomnia();
    public static RegistryEntry<StatusEffect> INSOMNIA_EFFECT;

    public static void init() {
        if (!PlatformFunctions.getPlatformName().equals("neoforge")) {
            Registry.register(Registries.STATUS_EFFECT, id("insomnia"), INSOMNIA);
            INSOMNIA_EFFECT = Registries.STATUS_EFFECT.getEntry(INSOMNIA);
        }
    }
    public static StatusEffectInstance insomniaEffect() {
        return new StatusEffectInstance(INSOMNIA_EFFECT, CelestriaConfig.insomniaDuration, 0, true, false, true);
    }
    public static StatusEffect initInsomnia() {
        if (PlatformFunctions.isModLoaded("polymer-core")) return PolymerUtils.initInsomnia();
        else return new InsomniaStatusEffect(StatusEffectCategory.HARMFUL, MidnightColorUtil.hex2Rgb("88A9C8").getRGB());
    }
}
