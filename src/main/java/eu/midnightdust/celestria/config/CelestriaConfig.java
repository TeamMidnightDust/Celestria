package eu.midnightdust.celestria.config;

import com.google.common.collect.Lists;
import eu.midnightdust.lib.config.MidnightConfig;

import java.util.List;

public class CelestriaConfig extends MidnightConfig {
    @Entry public static boolean sendChatMessages = true;
    @Entry public static boolean enableShootingStars = true;
    @Entry public static float shootingStarScale = 6f;
    @Entry public static float shootingStarSpeed = 0.5f;
    @Entry public static int shootingStarPathLength = 100;
    @Entry public static int shootingStarChance = 20000;
    @Entry public static int shootingStarCooldownLength = 1000;
    @Entry public static int shootingStarLuckDuration = 1000;
    @Entry public static List<String> shootingStarMessages = Lists.newArrayList("celestria.shootingStar.1", "celestria.shootingStar.2", "celestria.shootingStar.3");
    @Entry public static boolean enableInsomnia = true;
    @Entry public static int insomniaChance = 30000;
    @Entry public static int insomniaDuration = 1000;
    @Entry public static List<String> insomniaMessages = Lists.newArrayList("celestria.insomnia.1", "celestria.insomnia.2");
}
