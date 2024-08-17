package eu.midnightdust.celestria.util.neoforge;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.resource.DirectoryResourcePack;
import net.minecraft.resource.ResourcePackInfo;
import net.minecraft.resource.ResourcePackPosition;
import net.minecraft.resource.ResourcePackProfile;
import net.minecraft.resource.ResourcePackSource;
import net.minecraft.resource.ResourceType;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.event.AddPackFindersEvent;
import net.neoforged.neoforgespi.locating.IModFile;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static eu.midnightdust.celestria.Celestria.MOD_ID;

public class ClientUtilsImpl {
    public final static MinecraftClient client = MinecraftClient.getInstance();
    static List<Identifier> packsToRegister = new ArrayList<>();
    static List<BiConsumer<ClientPlayNetworkHandler, MinecraftClient>> disconnectHandlers = new ArrayList<>();
    static Set<Consumer<MinecraftClient>> endClientTickEvents = new HashSet<>();
    static Set<Consumer<MinecraftClient>> startClientTickEvents = new HashSet<>();

    public static void registerBuiltinResourcePack(Identifier id) {
        packsToRegister.add(id);
    }
    public static void registerClientTick(boolean endTick, Consumer<MinecraftClient> code) {
        if (endTick) endClientTickEvents.add(code);
        else startClientTickEvents.add(code);
    }
    public static void registerDisconnectEvent(BiConsumer<ClientPlayNetworkHandler, MinecraftClient> code) {
        disconnectHandlers.add(code);
    }

    @EventBusSubscriber(modid = MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public class ClientEvents {
        @SubscribeEvent
        public static void addPackFinders(AddPackFindersEvent event) {
            if (event.getPackType() == ResourceType.CLIENT_RESOURCES) {
                packsToRegister.forEach(id -> registerResourcePack(event, id, false));
                packsToRegister.clear();
            }
        }
        private static void registerResourcePack(AddPackFindersEvent event, Identifier id, boolean alwaysEnabled) {
            event.addRepositorySource(((profileAdder) -> {
                IModFile file = ModList.get().getModFileById(id.getNamespace()).getFile();
                try {
                    ResourcePackProfile.PackFactory pack = new DirectoryResourcePack.DirectoryBackedFactory(file.findResource("resourcepacks/" + id.getPath()));
                    ResourcePackInfo info = new ResourcePackInfo(id.toString(), Text.of(id.getNamespace()+"/"+id.getPath()), ResourcePackSource.BUILTIN, Optional.empty());
                    ResourcePackProfile packProfile = ResourcePackProfile.create(info, pack, ResourceType.CLIENT_RESOURCES, new ResourcePackPosition(alwaysEnabled, ResourcePackProfile.InsertionPosition.TOP, false));
                    if (packProfile != null) {
                        profileAdder.accept(packProfile);
                    }
                } catch (NullPointerException e) {e.fillInStackTrace();}
            }));
        }
    }
    @EventBusSubscriber(modid = MOD_ID, bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT)
    public class ClientGameEvents {
        @SubscribeEvent
        public static void onDisconnect(ClientPlayerNetworkEvent.LoggingOut event) {
            if (event.getPlayer() != null) disconnectHandlers.forEach(handler -> handler.accept(event.getPlayer().networkHandler, client));
        }
        @SubscribeEvent
        public static void startClientTick(ClientTickEvent.Pre event) {
            startClientTickEvents.forEach(code -> code.accept(client));
        }
        @SubscribeEvent
        public static void endClientTick(ClientTickEvent.Pre event) {
            endClientTickEvents.forEach(code -> code.accept(client));
        }
    }
}
