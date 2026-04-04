package site.leawsic.autologin.command;

import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import site.leawsic.autologin.AutoLoginClient;
import site.leawsic.autologin.config.ModConfigData;

import java.util.Objects;

import static com.mojang.brigadier.arguments.StringArgumentType.getString;
import static com.mojang.brigadier.arguments.StringArgumentType.word;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

public class AutoLoginCommand {
    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(literal("autologin")
                // 重载配置
                .then(literal("reload")
                        .executes(context -> {
                            AutoLoginClient.reloadForCurrentServer();
                            context.getSource().sendFeedback(Text.literal("§aAutoLogin config reloaded!"));
                            return 1;
                        }))
                // 设置当前服务器的密码
                .then(literal("set")
                        .then(argument("password", word())
                                .executes(context -> {
                                    String password = getString(context, "password");
                                    MinecraftClient client = MinecraftClient.getInstance();
                                    if (client.getCurrentServerEntry() != null) {
                                        String server = client.getCurrentServerEntry().address;
                                        ModConfigData config = AutoLoginClient.getConfig();
                                        config.setPasswordForServer(server, password);
                                        config.save();  // 保存到文件
                                        context.getSource().sendFeedback(Text.literal("§aPassword set for server " + server));
                                        AutoLoginClient.reloadForCurrentServer(); // 刷新处理器
                                    } else {
                                        context.getSource().sendError(Text.literal("§cNot connected to any server"));
                                    }
                                    return 1;
                                })))
                // 查看状态
                .then(literal("status")
                        .executes(context -> {
                            ModConfigData config = AutoLoginClient.getConfig();
                            String status = config.enabled ? "§aEnabled" : "§cDisabled";
                            context.getSource().sendFeedback(Text.literal("§6AutoLogin status: " + status));
                            if (MinecraftClient.getInstance().getCurrentServerEntry() != null) {
                                String server = MinecraftClient.getInstance().getCurrentServerEntry().address;
                                String pwd = config.getPasswordForServer(server);
                                boolean hasPwd = pwd != null && !pwd.isEmpty();
                                context.getSource().sendFeedback(Text.literal("§6Current server: " + server + " - Password: " + (hasPwd ? "§a✔ set" : "§c✘ not set")));
                                if (Objects.equals(pwd, AutoLoginClient.getConfig().defaultPassword)) {
                                    context.getSource().sendFeedback(Text.literal("§c§lYou are using default " +
                                            "password!"));
                                }
                            }
                            return 1;
                        }))
        );
    }
}