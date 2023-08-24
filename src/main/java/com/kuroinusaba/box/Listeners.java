package com.kuroinusaba.box;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.kuroinusaba.box.Box.plugin;
import static com.kuroinusaba.box.Box.prefix;
import static java.lang.Integer.parseInt;

public class Listeners implements Listener {
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (event.getView().getTitle().contains(prefix)) {
            if (event.getView().getTitle().contains(prefix + "§a§lBOXにアイテムを入れる")) {
                return;
            } else if (event.getView().getTitle().contains(prefix + "§a§lBOX受信(中身)")) {
                // シフトクリックの時
                if (event.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY) {
                    event.setCancelled(true);
                    player.sendMessage(prefix + "§4§lクリックでアイテムを受け取ってください。");
                    return;
                } else {
                    event.setCancelled(true);
                    Inventory playerinv = player.getInventory();
                    Inventory boxinv = event.getClickedInventory();
                    ItemStack item = event.getCurrentItem();
                    if (item == null || item.getType() == Material.AIR) {
                        return;
                    }
                    // プレイヤーのインベントリに空きがあるか
                    if (playerinv.firstEmpty() == -1) {
                        player.sendMessage(prefix + "§4§lプレイヤーのインベントリに空きがありません。");
                        return;
                    } else {
                        playerinv.addItem(item);
                        boxinv.removeItem(item);
                        File playerdata = new File("plugins/Box/playerdata/" + player.getUniqueId() + ".yml");
                        YamlConfiguration playerdatayml = YamlConfiguration.loadConfiguration(playerdata);
                        File receivebox = new File("plugins/Box/receiveboxdata/" + player.getUniqueId() + "/" + playerdatayml.getInt("openreceivebox") + ".yml");
                        YamlConfiguration receiveboxyml = YamlConfiguration.loadConfiguration(receivebox);
                        List<ItemStack> items = (List<ItemStack>) receiveboxyml.getList("item");
                        items.remove(item);
                        // アイテムがなくなったらファイルを削除する
                        if (items.size() == 0) {
                            receivebox.delete();
                            try {
                                playerdatayml.save(playerdata);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            File receiveboxdata = new File("plugins/Box/receiveboxdata/" + player.getUniqueId());
                            for (File file : receiveboxdata.listFiles()) {
                                // ファイル名を取得
                                String filename = file.getName();
                                // ファイル名を数字に変換
                                if (parseInt(filename.replace(".yml", "")) < playerdatayml.getInt("openreceivebox")) {
                                    continue;
                                }
                                // ファイル名を変更
                                file.renameTo(new File("plugins/Box/receiveboxdata/" + player.getUniqueId() + "/" + (parseInt(filename.replace(".yml", "")) - 1) + ".yml"));
                            }
                            player.sendMessage(prefix + "§a§lアイテムを受け取りました。");
                            return;
                        }
                        receiveboxyml.set("item", items);
                        try {
                            receiveboxyml.save(receivebox);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        player.sendMessage(prefix + "§a§lアイテムを受け取りました。");
                    }
                }
            } else if (event.getView().getTitle().contains(prefix + "§a§lBOX受信")) {
                if (event.isLeftClick()) {
                    event.setCancelled(true);
                    ItemStack item = event.getCurrentItem();
                    int slot = event.getSlot();
                    File receivebox = new File("plugins/Box/receiveboxdata/" + player.getUniqueId() + "/" + (slot + 1) + ".yml");
                    YamlConfiguration receiveboxyml = YamlConfiguration.loadConfiguration(receivebox);
                    File playerdata = new File("plugins/Box/playerdata/" + player.getUniqueId() + ".yml");
                    YamlConfiguration playerdatayml = YamlConfiguration.loadConfiguration(playerdata);
                    playerdatayml.set("openreceivebox", slot + 1);
                    try {
                        playerdatayml.save(playerdata);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Inventory boxinv = Bukkit.createInventory(null, 36, prefix + "§a§lBOX受信(中身)");
                    List<ItemStack> itemlist = (List<ItemStack>) receiveboxyml.getList("item");
                    for (int i = 0; i < itemlist.size(); i++) {
                        if (itemlist.get(i) == null) {
                            continue;
                        }
                        boxinv.setItem(i, itemlist.get(i));
                    }
                    player.openInventory(boxinv);
                    return;
                } else {
                    event.setCancelled(true);
                    return;
                }
            } else {
                event.setCancelled(true);
            }
            ItemStack item = event.getCurrentItem();
            if (item.getItemMeta().getDisplayName().contains("§c§k!!!§r§c§lBOXのアイテムを全削除する§r§c§k!!!")) {
                if (event.isRightClick() && event.isShiftClick()) {
                    event.setCancelled(true);
                    Inventory box = event.getInventory();
                    for (int i = 9; i < 44; i++) {
                        box.setItem(i, null);
                    }
                    player.sendMessage(prefix + "§a§lBOXのアイテムを全削除しました。");
                    return;
                } else {
                    player.sendMessage(prefix + "§4§l右クリック+Shiftで実行してください。");
                    return;
                }
            }
            if (item == null || item.getType() == Material.AIR) {
                return;
            }
            if (item.getItemMeta().getDisplayName().contains("§a§lBOX容量を増やす")) {
                player.sendMessage(prefix + "§4§lこの機能は現在実装されていません。");
                return;
            }
            if (item.getItemMeta().getDisplayName().contains("§r")) {
                return;
            }
            if (item.getItemMeta().getDisplayName().contains("§a§lBOX通信")) {
                if (!(plugin.getConfig().getBoolean("flag.connectBox"))) {
                    player.sendMessage(prefix + "§4§lこの機能は現在実装されていません。");
                } else {
                    File playerdata = new File("plugins/Box/playerdata/" + player.getUniqueId() + ".yml");
                    YamlConfiguration playerdataconfig = YamlConfiguration.loadConfiguration(playerdata);
                    if (playerdataconfig.getInt("box") == 1) {
                        player.sendMessage(prefix + "§4§lBOXがありません。");
                    } else {
                        player.sendMessage(prefix + "§a§lBOX通信を開始します。");
                        player.sendMessage(prefix + "§a§l通信中...");
                        player.sendMessage(prefix + "§a§l通信完了");
                        player.sendMessage(prefix + "§a§lBOX通信を終了しました。");
                    }
                }
                return;
            }
            if (item.getItemMeta().getDisplayName().contains("§a§lBOX受信")) {
                Inventory inventory = Bukkit.createInventory(null, 54, prefix + "§a§lBOX受信");
                File receivebox = new File("plugins/Box/receiveboxdata/" + player.getUniqueId());
                File[] receiveboxlist = receivebox.listFiles();
                for (int i = 0; i < receiveboxlist.length; i++) {
                    File receiveboxdata = new File("plugins/Box/receiveboxdata/" + player.getUniqueId() + "/" + (i + 1) + ".yml");
                    YamlConfiguration receiveboxdataconfig = YamlConfiguration.loadConfiguration(receiveboxdata);
                    ItemStack receiveboxitem = new ItemStack(Material.CHEST);
                    ItemMeta receiveboxitemmeta = receiveboxitem.getItemMeta();
                    receiveboxitemmeta.setDisplayName(receiveboxdataconfig.getString("name"));
                    List<String> receiveboxitemlore = receiveboxdataconfig.getStringList("lore");
                    receiveboxitemlore.add("§7§l送信者: " + receiveboxdataconfig.getString("sender"));
                    receiveboxitemmeta.setLore(receiveboxitemlore);
                    receiveboxitem.setItemMeta(receiveboxitemmeta);
                    inventory.setItem(i, receiveboxitem);
                }
                player.openInventory(inventory);
                return;
            }
            if (item.getItemMeta().getDisplayName().contains("§a§lBOXにアイテムを入れる")) {
                Inventory inventory = Bukkit.createInventory(null, 36, prefix + "§a§lBOXにアイテムを入れる");
                player.openInventory(inventory);
                return;
            }
            if (item.getItemMeta().getDisplayName().contains("§a§lBOXを整理する")) {
                Inventory box = event.getInventory();
                return;
            }
            // シフトクリックの時
            if (event.getView().getTitle().contains(prefix + "§a§l" + player.getName() + "§r§aのBOX")) {
                if (event.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY) {
                    event.setCancelled(true);
                    player.sendMessage(prefix + "§4§lクリックでアイテムを受け取ってください。");
                    return;
                } else {
                    event.setCancelled(true);
                    Inventory playerinv = player.getInventory();
                    Inventory boxinv = event.getInventory();
                    Inventory clickedinv = event.getClickedInventory();
                    ItemStack clickitem = event.getCurrentItem();
                    if (item == null || item.getType() == Material.AIR) {
                        return;
                    }
                    // プレイヤーのインベントリに空きがあるか
                    if (playerinv.firstEmpty() == -1) {
                        player.sendMessage(prefix + "§4§lプレイヤーのインベントリに空きがありません。");
                        return;
                    } else {
                        if (clickedinv == playerinv) {
                            playerinv.removeItem(clickitem);
                            boxinv.addItem(clickitem);
                            File playerdata = new File("plugins/Box/playerdata/" + player.getUniqueId() + ".yml");
                            YamlConfiguration playerdatayml = YamlConfiguration.loadConfiguration(playerdata);
                            File box = new File("plugins/Box/boxdata/" + player.getUniqueId() + "/" + playerdatayml.getInt("open") + ".yml");
                            YamlConfiguration boxyml = YamlConfiguration.loadConfiguration(box);
                            List<ItemStack> items = new ArrayList<>();
                            for (int i = 9; i < 44; i++) {
                                if (boxinv.getItem(i) == null || boxinv.getItem(i).getType() == Material.AIR) {
                                    continue;
                                }
                                items.add(boxinv.getItem(i));
                            }
                            boxyml.set("item", items);
                            try {
                                boxyml.save(box);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            player.sendMessage(prefix + "§a§lアイテムをBOXに入れました。");
                            return;
                        } else {
                            if (event.getSlot() < 9 || event.getSlot() > 45) {
                                return;
                            }
                            boxinv.removeItem(clickitem);
                            playerinv.addItem(clickitem);
                            File playerdata = new File("plugins/Box/playerdata/" + player.getUniqueId() + ".yml");
                            YamlConfiguration playerdatayml = YamlConfiguration.loadConfiguration(playerdata);
                            File box = new File("plugins/Box/boxdata/" + player.getUniqueId() + "/" + playerdatayml.getInt("open") + ".yml");
                            YamlConfiguration boxyml = YamlConfiguration.loadConfiguration(box);
                            List<ItemStack> items = new ArrayList<>();
                            for (int i = 9; i < 44; i++) {
                                if (boxinv.getItem(i) == null || boxinv.getItem(i).getType() == Material.AIR) {
                                    continue;
                                }
                                items.add(boxinv.getItem(i));
                            }
                            boxyml.set("item", items);
                            try {
                                boxyml.save(box);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            player.sendMessage(prefix + "§a§lアイテムを受け取りました。");
                            return;
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();
        if (event.getView().getTitle().contains(prefix + "§a§lBOXにアイテムを入れる")) {
            Inventory inventory = event.getInventory();
            List<ItemStack> itemList = new ArrayList<>();
            for (int i = 0; i < 36; i++) {
                ItemStack item = inventory.getItem(i);
                if (item == null || item.getType() == Material.AIR) {
                    continue;
                }
                itemList.add(item);
            }
            File playerdata = new File("plugins/Box/playerdata/" + player.getUniqueId() + ".yml");
            YamlConfiguration playerdataconfig = YamlConfiguration.loadConfiguration(playerdata);
            File boxdata = new File("plugins/Box/boxdata/" + player.getUniqueId() + "/" + playerdataconfig.getInt("open") + ".yml");
            YamlConfiguration boxdataconfig = YamlConfiguration.loadConfiguration(boxdata);
            List<ItemStack> itemStackList = (List<ItemStack>) boxdataconfig.getList("item");
            if (itemStackList.size() + itemList.size() > 36) {
                File receivebox = new File("plugins/Box/receiveboxdata/" + player.getUniqueId());
                File[] receiveboxlist = receivebox.listFiles();
                File receiveboxdata = new File("plugins/Box/receiveboxdata/" + player.getUniqueId() + "/" + (receiveboxlist.length + 1) + ".yml");
                YamlConfiguration receiveboxdataconfig = YamlConfiguration.loadConfiguration(receiveboxdata);
                receiveboxdataconfig.set("item", itemList);
                receiveboxdataconfig.set("name", "§e§l溢れたアイテムの返却です！");
                List<String> lore = new ArrayList<>();
                lore.add("§7§lBOXに入れられるアイテムの数は36個までです。");
                lore.add("§7§l溢れたアイテムをBOX受信に送りました。");
                receiveboxdataconfig.set("lore", lore);
                receiveboxdataconfig.set("sender", "BOX");
                try {
                    receiveboxdataconfig.save(receiveboxdata);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                player.sendMessage(prefix + "§4§lBOXに入れられるアイテムの数は36個までです。");
                player.sendMessage(prefix + "§4§l溢れたアイテムをBOX受信に送りました。");
                return;
            }
            for (int i = 0; i < itemList.size(); i++) {
                itemStackList.add(itemList.get(i));
            }
            boxdataconfig.set("item", itemStackList);
            try {
                boxdataconfig.save(boxdata);
            } catch (Exception e) {
                e.printStackTrace();
            }
            player.sendMessage(prefix + "§a§lBOXにアイテムを入れました。");
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        File playerdata = new File("plugins/Box/playerdata/" + player.getUniqueId() + ".yml");
        if (!playerdata.exists()) {
            YamlConfiguration playerdataconfig = YamlConfiguration.loadConfiguration(playerdata);
            try {
                playerdataconfig.save(playerdata);
                playerdataconfig.set("name", player.getName());
                playerdataconfig.set("box", 1);
                playerdataconfig.set("open", 1);
                playerdataconfig.save(playerdata);
                File boxdata = new File("plugins/Box/boxdata/" + player.getUniqueId() + "/1.yml");
                YamlConfiguration boxdataconfig = YamlConfiguration.loadConfiguration(boxdata);
                List<ItemStack> itemStackList = new ArrayList<>();
                ItemStack itemStack1 = new ItemStack(Material.DIAMOND);
                itemStackList.add(itemStack1);
                boxdataconfig.set("item", itemStackList);
                boxdataconfig.save(boxdata);
                File receiveboxdata = new File("plugins/Box/receiveboxdata/" + player.getUniqueId() + "/1.yml");
                YamlConfiguration receiveboxdataconfig = YamlConfiguration.loadConfiguration(receiveboxdata);
                List<ItemStack> receiveitemStackList = new ArrayList<>();
                ItemStack itemStack = new ItemStack(Material.DIAMOND);
                receiveitemStackList.add(itemStack);
                receiveboxdataconfig.set("item", receiveitemStackList);
                receiveboxdataconfig.set("name", "§e§lBOX受信テスト");
                List<String> receiveboxlore = new ArrayList<>();
                receiveboxlore.add("§7§lBOX受信テストの記念品");
                receiveboxdataconfig.set("lore", receiveboxlore);
                receiveboxdataconfig.set("sender", "BOX受信テスト");
                receiveboxdataconfig.save(receiveboxdata);
                plugin.getLogger().info(prefix + "§a" + player.getName() + "のデータを作成しました。");
            } catch (Exception e) {
                plugin.getLogger().severe(prefix + "§c" + player.getName() + "のデータの作成に失敗しました。");
                throw new RuntimeException(e);
            }
        }
    }
}
