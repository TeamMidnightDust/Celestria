package eu.midnightdust.celestria;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import eu.midnightdust.celestria.effect.StatusEffectInit;
import eu.midnightdust.celestria.util.CommonUtils;
import eu.midnightdust.celestria.util.PacketUtils;
import eu.midnightdust.celestria.util.ShootingStarPayload;
import eu.midnightdust.celestria.util.WelcomePayload;
import eu.midnightdust.celestria.config.CelestriaConfig;
import eu.midnightdust.lib.util.PlatformFunctions;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Celestria {
    public static final String MOD_ID = "celestria";
    public static final Random random = Random.create();
    private static int prevPlayerAmount = 0;
    public static final List<PlayerEntity> playersWithMod = new ArrayList<>();

    public static void init() {
        CelestriaConfig.init(MOD_ID, CelestriaConfig.class);
        if (CelestriaConfig.enableInsomnia || PlatformFunctions.isClientEnv()) StatusEffectInit.init();
        PacketUtils.registerPayloadCommon(WelcomePayload.PACKET_ID, WelcomePayload.codec);
        PacketUtils.registerPayloadS2C(ShootingStarPayload.PACKET_ID, ShootingStarPayload.codec);
        PacketUtils.registerServerGlobalReceiver(WelcomePayload.PACKET_ID, (payload, player) -> playersWithMod.add(player));

        LiteralArgumentBuilder<ServerCommandSource> command = CommandManager.literal("shootingStar")
                .then(CommandManager.argument("players", EntityArgumentType.players()).requires(source -> source.hasPermissionLevel(2)).executes(ctx ->
                                createShootingStar(random.nextBetween(100, 150),
                                        random.nextInt(360),
                                        random.nextInt(3),
                                        random.nextBetween(10, 170),
                                        random.nextBetween(Math.min(CelestriaConfig.shootingStarMinSize, CelestriaConfig.shootingStarMaxSize),
                                                Math.max(CelestriaConfig.shootingStarMaxSize, CelestriaConfig.shootingStarMinSize)),
                                        EntityArgumentType.getPlayers(ctx, "players").toArray(new ServerPlayerEntity[0])))
                .then(CommandManager.argument("x", IntegerArgumentType.integer(90, 180))
                        .then(CommandManager.argument("y", IntegerArgumentType.integer(0, 360))
                                .then(CommandManager.argument("type", IntegerArgumentType.integer(0, 3))
                                        .then(CommandManager.argument("rotation", IntegerArgumentType.integer(10, 170))
                                            .then(CommandManager.argument("size", IntegerArgumentType.integer(1, 250))
                                                .requires(source -> source.hasPermissionLevel(2)).executes(ctx -> createShootingStar(
                                                        IntegerArgumentType.getInteger(ctx, "x"),
                                                        IntegerArgumentType.getInteger(ctx, "y"),
                                                        IntegerArgumentType.getInteger(ctx, "type"),
                                                        IntegerArgumentType.getInteger(ctx, "rotation"),
                                                        IntegerArgumentType.getInteger(ctx, "size"),
                                                        EntityArgumentType.getPlayers(ctx, "players").toArray(new ServerPlayerEntity[0])))))))));
        LiteralArgumentBuilder<ServerCommandSource> finalized = CommandManager.literal("celestria").then(command).requires(source -> source.hasPermissionLevel(2));
        PlatformFunctions.registerCommand(finalized);

        CommonUtils.registerWorldTickEvent(true, world -> {
            if (world.getPlayers().size() > prevPlayerAmount && CelestriaConfig.enableShootingStars) {
                playersWithMod.clear();
                world.getPlayers().forEach(player -> PacketUtils.sendPlayPayloadS2C((ServerPlayerEntity) player, new WelcomePayload()));
                prevPlayerAmount = world.getPlayers().size();
            }
            if (world.isNight() && CelestriaConfig.enableInsomnia && world.getMoonPhase() == 0) {
                for (PlayerEntity player : world.getPlayers()) {
                    if (world.random.nextInt(CelestriaConfig.insomniaChance) == 0) {
                        player.addStatusEffect(StatusEffectInit.insomniaEffect());
                    }
                }
            }
            if (world.isNight() && CelestriaConfig.enableShootingStars && world.random.nextInt(getChance(world)) == 0) {
                int x = world.random.nextBetween(100, 150);
                int y = world.random.nextInt(360);
                int type = world.random.nextInt(3);
                int rotation = world.random.nextBetween(10, 170);
                int size = world.random.nextBetween(Math.min(CelestriaConfig.shootingStarMinSize, CelestriaConfig.shootingStarMaxSize), Math.max(CelestriaConfig.shootingStarMaxSize, CelestriaConfig.shootingStarMinSize));
                createShootingStar(x, y, type, rotation, size, playersWithMod.toArray(new ServerPlayerEntity[0]));
            }
        });
    }

    public static int createShootingStar(int x, int y, int type, int rotation, int size, ServerPlayerEntity... players) {
        int message = Random.create().nextInt(CelestriaConfig.shootingStarMessages.size());
        Arrays.stream(players).forEach(player -> {
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.LUCK, CelestriaConfig.shootingStarLuckDuration, 0, true, false, true));
            PacketUtils.sendPlayPayloadS2C(player, new ShootingStarPayload(x, y, type, rotation, size));

            if (CelestriaConfig.sendChatMessages) player.sendMessageToClient(Text.translatable(CelestriaConfig.shootingStarMessages.get(message)),true);
        });
        return 1;
    }
    public static int getChance(World world) {
        // Stellar nights will occur when the moon is in the waxing crescent phase
        return (world.getMoonPhase() == 5) ? CelestriaConfig.shootingStarChance / 200 : CelestriaConfig.shootingStarChance;
    }
    public static Identifier id(String path) {
        return Identifier.of(MOD_ID, path);
    }

}
