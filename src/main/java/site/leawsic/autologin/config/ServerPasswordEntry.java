package site.leawsic.autologin.config;

import me.shedaniel.autoconfig.annotation.ConfigEntry;

public class ServerPasswordEntry {
    @ConfigEntry.Gui.RequiresRestart(false)
    public String server = "";

    @ConfigEntry.Gui.RequiresRestart(false)
    public String password = "";

    // 必须有一个无参构造器供 Cloth Config 使用
    public ServerPasswordEntry() {}

    public ServerPasswordEntry(String server, String password) {
        this.server = server;
        this.password = password;
    }
}