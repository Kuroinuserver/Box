package com.kuroinusaba.box;

import com.kuroinusaba.box.Commands.BOX;
import org.bukkit.plugin.java.JavaPlugin;

public final class Box extends JavaPlugin {
    public static Box plugin;
    private Listeners listeners;
    public static String prefix = "§7[§6Box§7]§r ";

    @Override
    public void onEnable() {
        // Plugin startup logic
        plugin = this;
        saveDefaultConfig();
        reloadConfig();
        try {
            this.listeners = new Listeners();
        } catch (Exception e) {
            getLogger().severe("Listenersのインスタンス化に失敗しました。");
            throw new RuntimeException(e);
        }
        getServer().getPluginManager().registerEvents(this.listeners, this);
        try {
            getCommand("box").setExecutor(new BOX());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        plugin.getLogger().info(prefix + "§aプラグインが有効になりました。");
        super.onEnable();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        plugin.getLogger().info(prefix + "§cプラグインが無効になりました。");
        super.onDisable();
    }
}
