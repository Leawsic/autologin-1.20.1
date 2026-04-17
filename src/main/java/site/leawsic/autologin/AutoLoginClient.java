package site.leawsic.autologin;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.minecraft.client.MinecraftClient;
import site.leawsic.autologin.command.AutoLoginCommand;
import site.leawsic.autologin.config.ModConfigData;
import site.leawsic.autologin.handler.LoginHandler;

public class AutoLoginClient implements ClientModInitializer {
    private static ModConfigData config;
    private static LoginHandler currentHandler = null;

    @Override
    public void onInitializeClient() {
        // 注册 Cloth Config
        AutoConfig.register(ModConfigData.class, GsonConfigSerializer::new);
        config = AutoConfig.getConfigHolder(ModConfigData.class).getConfig();

        registerEvents();
        registerCommands();
        AutoLogin.LOGGER.info("AutoLogin mod initialized");
    }

    public static ModConfigData getConfig() {
        return config;
    }

    private void registerEvents() {
        // 加入服务器时初始化登录处理器
        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
            if (client.getCurrentServerEntry() != null) {
                String serverAddress = client.getCurrentServerEntry().address;
                currentHandler = new LoginHandler(serverAddress);
                currentHandler.register();
                AutoLogin.LOGGER.info("LoginHandler created for server: {}", serverAddress);
            }
        });

        // 断开服务器时清理处理器
        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> {
            if (currentHandler != null) {
                currentHandler.unregister();
                currentHandler = null;
            }
        });
    }

    private void registerCommands() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) ->
                AutoLoginCommand.register(dispatcher));
    }

    // 刷新当前服务器的处理器（配置重载或密码更改后调用）
    public static void reloadForCurrentServer() {
        // 重新加载配置（确保是最新值）
        config = AutoConfig.getConfigHolder(ModConfigData.class).getConfig();
        MinecraftClient client = MinecraftClient.getInstance();
        if (client != null && client.getCurrentServerEntry() != null && currentHandler != null) {
            String serverAddress = client.getCurrentServerEntry().address;
            currentHandler.unregister();
            currentHandler = new LoginHandler(serverAddress);
            currentHandler.register();
            AutoLogin.LOGGER.info("LoginHandler reloaded for server: {}", serverAddress);
        }
    }
}
