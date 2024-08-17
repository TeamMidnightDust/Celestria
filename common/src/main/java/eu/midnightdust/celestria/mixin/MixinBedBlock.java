package eu.midnightdust.celestria.mixin;

import eu.midnightdust.celestria.config.CelestriaConfig;
import eu.midnightdust.celestria.effect.StatusEffectInit;
import net.minecraft.block.BedBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BedBlock.class)
public abstract class MixinBedBlock {
    @Inject(at = @At("HEAD"), method = "onUse", cancellable = true)
    public void celestria$onBedUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit, CallbackInfoReturnable<ActionResult> cir) {
        if (CelestriaConfig.enableInsomnia && !world.isClient() && player instanceof ServerPlayerEntity serverPlayer && player.hasStatusEffect(StatusEffectInit.INSOMNIA_EFFECT)) {
            if (CelestriaConfig.sendChatMessages) serverPlayer.sendMessageToClient(Text.literal("§f§l[§7§lC§8§le§7§ll§f§le§7§ls§8§lt§7§lr§f§li§7§la§8§l] ").append(Text.translatable(CelestriaConfig.insomniaMessages.get(world.random.nextInt(CelestriaConfig.insomniaMessages.size())))),false);
            cir.setReturnValue(ActionResult.CONSUME);
        }
    }
}
