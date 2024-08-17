package eu.midnightdust.celestria.util.fabric;

import eu.midnightdust.celestria.Celestria;
import eu.midnightdust.celestria.effect.InsomniaStatusEffect;
import eu.midnightdust.lib.util.MidnightColorUtil;
import eu.pb4.polymer.core.api.other.PolymerStatusEffect;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.Nullable;

public class PolymerUtilsImpl {
    public static StatusEffect initInsomnia() {
        return new PolymerInsomniaStatusEffect(StatusEffectCategory.HARMFUL, MidnightColorUtil.hex2Rgb("88A9C8").getRGB());
    }
    private static class PolymerInsomniaStatusEffect extends InsomniaStatusEffect implements PolymerStatusEffect {
        public PolymerInsomniaStatusEffect(StatusEffectCategory statusEffectCategory, int color) {
            super(statusEffectCategory, color);
        }
        public @Nullable StatusEffect getPolymerReplacement(ServerPlayerEntity player) {
            if (Celestria.playersWithMod.contains(player.getUuid())) return this;
            else return null;
        }
    }
}
