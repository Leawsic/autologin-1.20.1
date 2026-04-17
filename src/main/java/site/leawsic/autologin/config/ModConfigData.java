package site.leawsic.autologin.config;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import site.leawsic.autologin.AutoLogin;

import java.util.ArrayList;
import java.util.List;

@Config(name = AutoLogin.MOD_ID)
public class ModConfigData implements ConfigData {
    public boolean enabled = true;

    @ConfigEntry.Gui.Tooltip
    public String defaultPassword = "";

    @ConfigEntry.Gui.Tooltip
    public List<ServerPasswordEntry> serverPasswords = new ArrayList<>();

    @ConfigEntry.Gui.Tooltip
    public String triggerPattern = ".*/(login|reg|register).*|.*未登录.*|.*Please login.*|.*密码.*|.*需要登录.*";

    @ConfigEntry.Gui.Tooltip
    public String successPattern = ".*登录成功.*|.*Login successful.*|.*已登录.*";

    @ConfigEntry.Gui.Tooltip
    public String loginCommandTemplate = "login %s";

    public String getPasswordForServer(String serverAddress) {
        String addr = normalizeAddress(serverAddress);
        for (ServerPasswordEntry entry : serverPasswords) {
            if (entry.server.equalsIgnoreCase(addr)) {
                return entry.password;
            }
        }
        return defaultPassword;
    }

    public void setPasswordForServer(String serverAddress, String password) {
        String addr = normalizeAddress(serverAddress);
        // 查找已有条目
        for (ServerPasswordEntry entry : serverPasswords) {
            if (entry.server.equalsIgnoreCase(addr)) {
                if (password == null || password.isEmpty()) {
                    serverPasswords.remove(entry);
                } else {
                    entry.password = password;
                }
                return;
            }
        }
        // 未找到且密码非空，添加新条目
        if (password != null && !password.isEmpty()) {
            serverPasswords.add(new ServerPasswordEntry(addr, password));
        }
    }

    private String normalizeAddress(String address) {
        if (address == null) return "";
        int colon = address.indexOf(':');
        return colon == -1 ? address : address.substring(0, colon);
    }

    public void save() {
        AutoConfig.getConfigHolder(ModConfigData.class).save();
    }

}