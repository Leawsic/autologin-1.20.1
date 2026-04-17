package site.leawsic.autologin.handler;

import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import site.leawsic.autologin.AutoLogin;
import site.leawsic.autologin.AutoLoginClient;
import site.leawsic.autologin.config.ModConfigData;

import java.util.regex.Pattern;

public class LoginHandler {
    private final String serverAddress;
    private boolean hasAttemptedLogin = false;
    private boolean isLoggedIn = false;
    private Pattern triggerPattern;
    private Pattern successPattern;
    private boolean listening = false;

    public LoginHandler(String serverAddress) {
        this.serverAddress = serverAddress;
        updatePatterns();
    }

    private void updatePatterns() {
        ModConfigData config = AutoLoginClient.getConfig();
        this.triggerPattern = Pattern.compile(config.triggerPattern, Pattern.CASE_INSENSITIVE);
        this.successPattern = Pattern.compile(config.successPattern, Pattern.CASE_INSENSITIVE);
    }

    public void register() {
        if (listening) return;
        listening = true;
        ClientReceiveMessageEvents.GAME.register((message, overlay) -> {
            if (!listening) return;
            onChatMessage(message);
        });
        ClientReceiveMessageEvents.CHAT.register((message, signedMessage, sender, params, receptionTimestamp) -> {
            if (!listening) return;
            onChatMessage(message);
        });
        AutoLogin.LOGGER.info("Chat listener registered for {}", serverAddress);
    }

    public void unregister() {
        listening = false;
    }

    private void onChatMessage(Text message) {
        ModConfigData config = AutoLoginClient.getConfig();
        if (!config.enabled) return;
        if (isLoggedIn) return;

        String rawMessage = message.getString();

        // 检查是否已登录成功
        if (successPattern.matcher(rawMessage).find()) {
            isLoggedIn = true;
            return;
        }

        // 检查是否需要登录
        if (!hasAttemptedLogin && triggerPattern.matcher(rawMessage).find()) {
            String password = config.getPasswordForServer(serverAddress);
            if (password == null || password.isEmpty()) {
                AutoLogin.LOGGER.warn("No password configured for server {}, cannot auto-login", serverAddress);
                return;
            }

            hasAttemptedLogin = true;
            String command = String.format(config.loginCommandTemplate, password);
            AutoLogin.LOGGER.info("Auto-login triggered for server {}", serverAddress);
            MinecraftClient client = MinecraftClient.getInstance();
            if (client.player != null) {
                client.player.networkHandler.sendChatCommand("login " + command);
            }
        }
    }
}
