package eu.midnightdust.celestria.config;

import com.google.common.collect.Lists;
import eu.midnightdust.lib.config.MidnightConfig;

import java.util.List;

public class CelestriaConfig extends MidnightConfig {
    public static final String STARS = "stars";
    public static final String INSOMNIA = "insomnia";
    @Entry public static boolean sendChatMessages = true;
    @Entry(category = STARS) public static boolean enableShootingStars = true;
    @Entry(category = STARS, isSlider = true, min = 0f, max = 6f) public static float shootingStarDistance = 2f;
    @Entry(category = STARS, isSlider = true, min = 1, max = 250) public static int shootingStarMinSize = 25;
    @Entry(category = STARS, isSlider = true, min = 1, max = 250) public static int shootingStarMaxSize = 125;
    @Entry(category = STARS, isSlider = true, min = 0f, max = 10f) public static float shootingStarSpeed = 6.0f;
    @Entry(category = STARS, isSlider = true, min = 0, max = 500) public static int shootingStarPathLength = 50;
    @Entry(category = STARS) public static int shootingStarChance = 20000;
    @Entry(category = STARS) public static int shootingStarLuckDuration = 1000;
    @Entry(category = STARS) public static List<String> shootingStarMessages = Lists.newArrayList("celestria.shootingStar.1", "celestria.shootingStar.2", "celestria.shootingStar.3");
    @Entry(category = INSOMNIA) public static boolean enableInsomnia = true;
    @Entry(category = INSOMNIA) public static int insomniaChance = 30000;
    @Entry(category = INSOMNIA, isSlider = true, min = 0, max = 5000) public static int insomniaDuration = 1000;
    @Entry(category = INSOMNIA) public static List<String> insomniaMessages = Lists.newArrayList("celestria.insomnia.1", "celestria.insomnia.2");
}
