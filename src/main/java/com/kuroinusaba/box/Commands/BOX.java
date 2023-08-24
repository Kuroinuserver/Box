package com.kuroinusaba.box.Commands;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.kuroinusaba.box.Box.plugin;
import static com.kuroinusaba.box.Box.prefix;

public class BOX implements @Nullable CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) {
            Player player = (Player) sender;
            if (!(player.hasPermission("admin"))) {
                return true;
            }
            File playerdata = new File("plugins/Box/playerdata/" + player.getUniqueId() + ".yml");
            YamlConfiguration playerdataconfig = YamlConfiguration.loadConfiguration(playerdata);
            File boxdata = new File("plugins/Box/boxdata/" + player.getUniqueId() + "/" + playerdataconfig.getInt("open") + ".yml");
            YamlConfiguration boxdataconfig = YamlConfiguration.loadConfiguration(boxdata);
            Inventory gui = Bukkit.createInventory(null, 54, prefix + "§a§l" + player.getName() + "§r§aのBOX(" + playerdataconfig.getInt("open") + "/" + playerdataconfig.getInt("box") + ")");
            ItemStack empty = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
            ItemMeta emptyMeta = empty.getItemMeta();
            emptyMeta.setDisplayName("§r");
            empty.setItemMeta(emptyMeta);
            ItemStack boxadd = new ItemStack(Material.CHEST);
            ItemMeta boxaddMeta = boxadd.getItemMeta();
            boxaddMeta.setDisplayName("§a§lBOX容量を増やす");
            List<String> boxaddLore = new ArrayList<>();
            boxaddLore.add("§7§lBOXの容量を増やします。");
            boxaddLore.add("§7§l最大で§a§l" + plugin.getConfig().getInt("maxBox") + "§r§7§lまで増やすことができます。");
            boxaddLore.add("§c§l注意: §r§c§lBOX容量を増やす際には、");
            boxaddLore.add("§c§lお金がかかります。");
            boxaddMeta.setLore(boxaddLore);
            boxadd.setItemMeta(boxaddMeta);
            ItemStack boxsend = new ItemStack(Material.HOPPER);
            ItemMeta boxsendMeta = boxsend.getItemMeta();
            boxsendMeta.setDisplayName("§a§lBOX通信");
            List<String> boxsendLore = new ArrayList<>();
            boxsendLore.add("§7§lBOX通信を行います。");
            boxsendLore.add("§7§lBOX通信を行うと、他のプレイヤーと");
            boxsendLore.add("§7§lBOXを共有することができます。");
            boxsendLore.add("§c§l注意: §r§c§lBOX通信を行うと、");
            boxsendLore.add("§c§l他のプレイヤーがあなたのBOXを");
            boxsendLore.add("§c§l開くことができるようになります。");
            boxsendLore.add("§c§l他のプレイヤーにBOXを開かれたくない場合は");
            boxsendLore.add("§c§lBOX通信を行わないでください。");
            boxsendLore.add("§4§l※この機能は開発中です。");
            boxsendMeta.setLore(boxsendLore);
            boxsend.setItemMeta(boxsendMeta);
            ItemStack boxreceive = new ItemStack(Material.CAULDRON);
            ItemMeta boxreceiveMeta = boxreceive.getItemMeta();
            boxreceiveMeta.setDisplayName("§a§lBOX受信");
            List<String> boxreceiveLore = new ArrayList<>();
            boxreceiveLore.add("§7§lBOX受信を行います。");
            boxreceiveLore.add("§7§lBOX受信を行うと、他のプレイヤーから");
            boxreceiveLore.add("§7§l送られてきたBOXを受け取ることができます。");
            boxreceiveLore.add("§4§l※この機能は開発中です。");
            boxreceiveMeta.setLore(boxreceiveLore);
            boxreceive.setItemMeta(boxreceiveMeta);
            ItemStack nextbox = new ItemStack(Material.ARROW);
            ItemMeta nextboxMeta = nextbox.getItemMeta();
            nextboxMeta.setDisplayName("§a§l次のBOXへ");
            nextbox.setItemMeta(nextboxMeta);
            ItemStack previousbox = new ItemStack(Material.ARROW);
            ItemMeta previousboxMeta = previousbox.getItemMeta();
            previousboxMeta.setDisplayName("§a§l前のBOXへ");
            previousbox.setItemMeta(previousboxMeta);
            ItemStack boxinput = new ItemStack(Material.MINECART);
            ItemMeta boxinputMeta = boxinput.getItemMeta();
            boxinputMeta.setDisplayName("§a§lBOXにアイテムを入れる");
            List<String> boxinputLore = new ArrayList<>();
            boxinputLore.add("§7§lBOXにアイテムを入れることができます。");
            boxinputLore.add("§7§l現在のBOX: §a§l" + playerdataconfig.getInt("open") + "/" + playerdataconfig.getInt("box"));
            boxinputMeta.setLore(boxinputLore);
            boxinput.setItemMeta(boxinputMeta);
            ItemStack boxsort = new ItemStack(Material.CRAFTING_TABLE);
            ItemMeta boxsortMeta = boxsort.getItemMeta();
            boxsortMeta.setDisplayName("§a§lBOXを整理する");
            List<String> boxsortLore = new ArrayList<>();
            boxsortLore.add("§7§lBOXを整理することができます。");
            boxsortMeta.setLore(boxsortLore);
            boxsort.setItemMeta(boxsortMeta);
            ItemStack boxdelete = new ItemStack(Material.BARRIER);
            ItemMeta boxdeleteMeta = boxdelete.getItemMeta();
            boxdeleteMeta.setDisplayName("§c§k!!!§r§c§lBOXのアイテムを全削除する§r§c§k!!!");
            List<String> boxdeleteLore = new ArrayList<>();
            boxdeleteLore.add("§7§lBOXのアイテムを全削除します。");
            boxdeleteLore.add("§c§l注意: §r§c§lBOXのアイテムを全削除すると、");
            boxdeleteLore.add("§c§l元に戻すことができません。");
            boxdeleteLore.add("§7§l現在のBOX: §a§l" + playerdataconfig.getInt("open") + "/" + playerdataconfig.getInt("box"));
            boxdeleteLore.add("§7シフト左クリックで削除");
            boxdeleteMeta.setLore(boxdeleteLore);
            boxdelete.setItemMeta(boxdeleteMeta);
            for (int i = 0; i < 9; i++) {
                gui.setItem(i, empty);
            }
            gui.setItem(3, boxsend);
            gui.setItem(4, boxadd);
            gui.setItem(5, boxreceive);
            for (int i = 45; i < 54; i++) {
                gui.setItem(i, empty);
            }
            gui.setItem(45, previousbox);
            gui.setItem(48, boxdelete);
            gui.setItem(49, boxinput);
            gui.setItem(50, boxsort);
            gui.setItem(53, nextbox);
            List<ItemStack> box = (List<ItemStack>) boxdataconfig.getList("item");
            for (int i = 0; i < box.size(); i++) {
                gui.setItem(i + 9, box.get(i));
            }
            player.openInventory(gui);
        }
        if (args.length == 1) {
            if (args[0].equals("reload")) {
                plugin.reloadConfig();
                sender.sendMessage(prefix + "§a§lコンフィグをリロードしました。");
            }
        }
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        List<String> tab = new ArrayList<>();
        if (!(sender.hasPermission("admin"))) {
            return null;
        }
        if (args.length == 1) {
            tab.add("help");
            tab.add("give");
            tab.add("reload");
            tab.add("version");
            return tab;
        }
        return null;
    }
}
