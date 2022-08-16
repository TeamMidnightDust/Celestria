package eu.midnightdust.celestria;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import eu.midnightdust.lib.util.MidnightColorUtil;
import eu.midnightdust.celestria.config.CelestriaConfig;
import eu.midnightdust.celestria.effect.InsomniaStatusEffect;
import io.netty.buffer.Unpooled;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.registry.Registry;

import java.util.Collection;

public class Celestria implements ModInitializer {
    public static final String MOD_ID = "celestria";
    public static final Identifier SHOOTING_STAR_PACKET = new Identifier(MOD_ID, "shooting_star");
    public static final Identifier WELCOME_PACKET = new Identifier(MOD_ID, "welcome");
    public static final StatusEffect INSOMNIA = new InsomniaStatusEffect(StatusEffectCategory.HARMFUL, MidnightColorUtil.hex2Rgb("88A9C8").getRGB());
    public static int shootingStarCooldown = 0;
    private int prevPlayerAmount = 0;

    public void onInitialize() {
        CelestriaConfig.init(MOD_ID, CelestriaConfig.class);
        Registry.register(Registry.STATUS_EFFECT, new Identifier(MOD_ID, "insomnia"), INSOMNIA);
        LiteralArgumentBuilder<ServerCommandSource> command = CommandManager.literal("shootingStar");
        var commandPlayers = command.then(CommandManager.argument("players", EntityArgumentType.players()));
        var commandX = commandPlayers.then(CommandManager.argument("x", IntegerArgumentType.integer(90, 180)));
        var commandY = commandX.then(CommandManager.argument("y", IntegerArgumentType.integer(0, 360)));
        var commandType = commandY.then(CommandManager.argument("type", IntegerArgumentType.integer(0, 3)));
        LiteralArgumentBuilder<ServerCommandSource> finalized = CommandManager.literal("celestria").requires(source -> source.hasPermissionLevel(2)).then(commandType).executes(ctx ->
                createShootingStar(EntityArgumentType.getPlayers(ctx, "players"),
                        IntegerArgumentType.getInteger(ctx, "x"), IntegerArgumentType.getInteger(ctx, "y"), IntegerArgumentType.getInteger(ctx, "type")));

        CommandRegistrationCallback.EVENT.register((dispatcher, dedicated, registrationEnvironment) -> dispatcher.register(finalized));
        ServerTickEvents.END_WORLD_TICK.register(world -> {
            if (shootingStarCooldown > 0) --shootingStarCooldown;
            if (world.getPlayers().size() > prevPlayerAmount && CelestriaConfig.enableShootingStars) {
                world.getPlayers().forEach(player -> ServerPlayNetworking.send(player, WELCOME_PACKET, new PacketByteBuf(Unpooled.buffer())));
                prevPlayerAmount = world.getPlayers().size();
            }
            if (world.isNight() && CelestriaConfig.enableInsomnia && world.getMoonPhase() == 0) {
                for (ServerPlayerEntity player : world.getPlayers()) {
                    if (world.random.nextInt(CelestriaConfig.insomniaChance) == 0) {
                        player.addStatusEffect(new StatusEffectInstance(INSOMNIA, CelestriaConfig.insomniaDuration, 0, true, false, true));
                    }
                }
            }
            if (world.isNight() && CelestriaConfig.enableShootingStars && Celestria.shootingStarCooldown <= 0 && world.random.nextInt(CelestriaConfig.shootingStarChance) == 0) {
                int x = world.random.nextBetween(100, 150);
                int y = world.random.nextInt(360);
                int type = world.random.nextInt(3);
                createShootingStar(world.getPlayers(), x, y, type);
                Celestria.shootingStarCooldown = CelestriaConfig.shootingStarCooldownLength;
            }
        });
    }
    public int createShootingStar(Collection<ServerPlayerEntity> players, int x, int y, int type) {
        int[] array = new int[3];
        array[0] = x;
        array[1] = y;
        array[2] = type;
        PacketByteBuf passedData = new PacketByteBuf(Unpooled.buffer());
        passedData.writeIntArray(array);

        int message = Random.create().nextInt(CelestriaConfig.shootingStarMessages.size());
        players.forEach(player -> {
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.LUCK, CelestriaConfig.shootingStarLuckDuration, 0, true, false, true));
            ServerPlayNetworking.send(player, SHOOTING_STAR_PACKET, passedData);
            if (CelestriaConfig.sendChatMessages) player.sendMessageToClient(Text.literal("§f§l[§7§lC§8§le§7§ll§f§le§7§ls§8§lt§7§lr§f§li§7§la§8§l] ").append(Text.translatable(CelestriaConfig.shootingStarMessages.get(message))),false);
        });
        return 1;
    }
}
