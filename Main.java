import java.io.File;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class Main extends JavaPlugin implements Listener {
  Data crafts;
  
  ArrayList<Player> list;
  
  public void onEnable() {
    this.list = new ArrayList<>();
    getConfig().addDefault("item-must-be-in-hand", "&c[CustomCrafting] &fYou don't have any item in hand!");
    getConfig().addDefault("already-created", "&c[CustomCrafting] &fYou have already created recipe with this name!");
    getConfig().addDefault("recipe-created", "&a[CustomCrafting] &fRecipe has been created!");
    getConfig().addDefault("recipe-cancel", "&c[CustomCrafting] &fCreating recipe has been cancelled!");
    getConfig().addDefault("not-exits", "&c[CustomCrafting] &fCreating recipe name is not exits!");
    getConfig().addDefault("remove-succes", "&a[CustomCrafting] &fRecipe has been removed!");
    getConfig().addDefault("no-permission", "&a[CustomCrafting] &cNo Permission!");
    getConfig().addDefault("inv-full", "&cInventory full.");
    getConfig().addDefault("already-in-list", "&cYou already have this workbench on");
    getConfig().addDefault("successfully-registered", "&aYour workbench type has been changed");
    getConfig().addDefault("zone-time", "Europe/Madrid");
    getConfig().addDefault("workbench.title", "&rWORKBENCH");
    getConfig().addDefault("workbench.fill-item.material", "STAINED_GLASS_PANE");
    getConfig().addDefault("workbench.fill-item.data", Integer.valueOf(15));
    getConfig().addDefault("workbench.fill-item.name", "&r");
    getConfig().addDefault("workbench.fill-item.lore", Arrays.asList(new String[] { "&r" }));
    getConfig().addDefault("workbench.good-craft.material", "STAINED_GLASS_PANE");
    getConfig().addDefault("workbench.good-craft.data", Integer.valueOf(5));
    getConfig().addDefault("workbench.good-craft.name", "&r");
    getConfig().addDefault("workbench.good-craft.lore", Arrays.asList(new String[] { "&r" }));
    getConfig().addDefault("workbench.bad-craft.material", "STAINED_GLASS_PANE");
    getConfig().addDefault("workbench.bad-craft.data", Integer.valueOf(14));
    getConfig().addDefault("workbench.bad-craft.name", "&r");
    getConfig().addDefault("workbench.bad-craft.lore", Arrays.asList(new String[] { "&r" }));
    getConfig().addDefault("workbench.back-item.material", "BED");
    getConfig().addDefault("workbench.back-item.data", Integer.valueOf(0));
    getConfig().addDefault("workbench.back-item.name", "&c&lBack");
    getConfig().addDefault("workbench.back-item.lore", Arrays.asList(new String[] { "&r", "&7Back to menu", "&r" }));
    getConfig().addDefault("workbench.back-item.cmds", Arrays.asList(new String[] { "[player] menu" }));
    getConfig().options().copyDefaults(true);
    saveConfig();
    this.crafts = new Data(new File(getDataFolder() + "/data/crafts.yml"));
    this.crafts.getConfig().options().copyDefaults(true);
    this.crafts.save();
    getServer().getPluginManager().registerEvents(this, (Plugin)this);
    getServer().getPluginCommand("customcrafting").setExecutor((CommandExecutor)this);
    getServer().getPluginCommand("wbc").setExecutor((CommandExecutor)this);
  }
  
  public static boolean isInventoryEmpty(Inventory p) {
    ItemStack[] contents;
    for (int length = (contents = p.getContents()).length, i = 0; i < length; i++) {
      ItemStack item = contents[i];
      if (item != null)
        return false; 
    } 
    return true;
  }
  
  @EventHandler
  public void onclose(InventoryCloseEvent e) {
    if (this.list.contains(e.getPlayer()) && e.getInventory().getType() == InventoryType.DISPENSER) {
      if (isInventoryEmpty(e.getInventory())) {
        this.list.remove(e.getPlayer());
        e.getPlayer().sendMessage(getConfig().getString("recipe-cancel").replaceAll("&", "));
        List<String> h = this.crafts.getConfig().getStringList("crafts-list");
        h.remove(e.getInventory().getTitle());
        this.crafts.getConfig().set("crafts-list", h);
        this.crafts.save();
        return;
      } 
      this.crafts.getConfig().set("Shaped." + e.getInventory().getTitle() + ".1", e.getInventory().getItem(0));
      this.crafts.getConfig().set("Shaped." + e.getInventory().getTitle() + ".2", e.getInventory().getItem(1));
      this.crafts.getConfig().set("Shaped." + e.getInventory().getTitle() + ".3", e.getInventory().getItem(2));
      this.crafts.getConfig().set("Shaped." + e.getInventory().getTitle() + ".4", e.getInventory().getItem(3));
      this.crafts.getConfig().set("Shaped." + e.getInventory().getTitle() + ".5", e.getInventory().getItem(4));
      this.crafts.getConfig().set("Shaped." + e.getInventory().getTitle() + ".6", e.getInventory().getItem(5));
      this.crafts.getConfig().set("Shaped." + e.getInventory().getTitle() + ".7", e.getInventory().getItem(6));
      this.crafts.getConfig().set("Shaped." + e.getInventory().getTitle() + ".8", e.getInventory().getItem(7));
      this.crafts.getConfig().set("Shaped." + e.getInventory().getTitle() + ".9", e.getInventory().getItem(8));
      this.crafts.getConfig().set("Shaped." + e.getInventory().getTitle() + ".result", e.getPlayer().getItemInHand());
      this.crafts.save();
      this.list.remove(e.getPlayer());
      e.getPlayer().sendMessage(getConfig().getString("recipe-created").replaceAll("&", "));
    } 
    if (e.getInventory().getTitle() != null && !e.getInventory().getTitle().isEmpty() && e.getInventory().getTitle().equals(getConfig().getString("workbench.title").replaceAll("&", "))) {
      Inventory inv = e.getInventory();
      Player p = (Player)e.getPlayer();
      for (Integer i : Arrays.<Integer>asList(new Integer[] { Integer.valueOf(10), Integer.valueOf(11), Integer.valueOf(12), Integer.valueOf(19), Integer.valueOf(20), Integer.valueOf(21), Integer.valueOf(28), Integer.valueOf(29), Integer.valueOf(30) })) {
        if (inv.getItem(i.intValue()) != null && inv.getItem(i.intValue()).getType() != Material.AIR)
          p.getWorld().dropItem(p.getLocation(), inv.getItem(i.intValue())); 
      } 
    } 
  }
  
  public boolean invFull(Player p) {
    return !Arrays.<ItemStack>asList(p.getInventory().getContents()).contains(null);
  }
  
  @EventHandler
  public void onUpdateCraft(final InventoryClickEvent e) {
    if (((Player)e.getWhoClicked()).getOpenInventory().getTopInventory() != null && (
      (Player)e.getWhoClicked()).getOpenInventory().getTopInventory().getTitle().equals(getConfig().getString("workbench.title").replaceAll("&", "))) {
      if (e.getClickedInventory() != null && e.getClickedInventory().getType() == InventoryType.PLAYER && (
        e.getClick() == ClickType.SHIFT_LEFT || e.getClick() == ClickType.SHIFT_RIGHT))
        e.setCancelled(true); 
      if (e.getClickedInventory() != null && e.getClickedInventory().getTitle().equals(getConfig().getString("workbench.title").replaceAll("&", "))) {
        final Player p = (Player)e.getWhoClicked();
        if (e.getSlot() == 49) {
          e.setCancelled(true);
          p.closeInventory();
          if (getConfig().getStringList("workbench.back-item.cmds") != null && !getConfig().getStringList("workbench.back-item.cmds").isEmpty())
            for (String cmd : getConfig().getStringList("workbench.back-item.cmds")) {
              if (cmd.startsWith("[player] ")) {
                String form = cmd.replace("[player] ", "").replaceAll("<player>", p.getName());
                p.performCommand(form);
                continue;
              } 
              if (cmd.startsWith("[console] ")) {
                getServer().dispatchCommand((CommandSender)getServer().getConsoleSender(), cmd.replace("[console] ", "").replaceAll("<player>", p.getName()));
                continue;
              } 
              p.performCommand(cmd);
            }  
          return;
        } 
        if (e.getSlot() == 24) {
          e.setCancelled(true);
          if (e.getCurrentItem() != null && e.getCurrentItem().getType() != Material.AIR) {
            if (invFull(p)) {
              p.sendMessage(getConfig().getString("inv-full").replaceAll("&", "));
              return;
            } 
            (new BukkitRunnable() {
                public void run() {
                  String st = Main.this.getPathRecipe(e.getClickedInventory());
                  if (st != null && 
                    Main.this.existThisRecipe(e.getClickedInventory())) {
                    Inventory inv = e.getClickedInventory();
                    HashMap<Integer, ItemStack> ingredients = Main.this.getMap(st);
                    ItemStack badItem = new ItemStack(Material.valueOf(Main.this.getConfig().getString("workbench.bad-craft.material")), 1, (byte)Main.this.getConfig().getInt("workbench.bad-craft.data"));
                    ItemMeta badMeta = badItem.getItemMeta();
                    badMeta.setDisplayName(Main.this.getConfig().getString("workbench.bad-craft.name").replaceAll("&", "));
                    List<String> loreFixBad = new ArrayList<>();
                    for (String stLore : Main.this.getConfig().getStringList("workbench.bad-craft.lore"))
                      loreFixBad.add(stLore.replaceAll("&", ")); 
                    badMeta.setLore(loreFixBad);
                    badItem.setItemMeta(badMeta);
                    for (Integer i : Arrays.<Integer>asList(new Integer[] { Integer.valueOf(10), Integer.valueOf(11), Integer.valueOf(12), Integer.valueOf(19), Integer.valueOf(20), Integer.valueOf(21), Integer.valueOf(28), Integer.valueOf(29), Integer.valueOf(30) })) {
                      if (ingredients.containsKey(i)) {
                        if (inv.getItem(i.intValue()) != null && inv.getItem(i.intValue()).getType() != Material.AIR && inv.getItem(i.intValue()).isSimilar(ingredients.get(i)) && inv.getItem(i.intValue()).getAmount() >= ((ItemStack)ingredients.get(i)).getAmount()) {
                          Integer amount1 = Integer.valueOf(inv.getItem(i.intValue()).getAmount());
                          Integer amount2 = Integer.valueOf(((ItemStack)ingredients.get(i)).getAmount());
                          if (amount1.intValue() > amount2.intValue()) {
                            ItemStack newAm = inv.getItem(i.intValue());
                            newAm.setAmount(amount1.intValue() - amount2.intValue());
                            inv.clear(i.intValue());
                            inv.setItem(i.intValue(), newAm);
                            if (newAm.getAmount() < ((ItemStack)ingredients.get(i)).getAmount()) {
                              inv.clear(24);
                              for (Integer slot : Arrays.<Integer>asList(new Integer[] { Integer.valueOf(45), Integer.valueOf(46), Integer.valueOf(47), Integer.valueOf(48), Integer.valueOf(50), Integer.valueOf(51), Integer.valueOf(52), Integer.valueOf(53) }))
                                inv.setItem(slot.intValue(), badItem); 
                            } 
                            continue;
                          } 
                          inv.clear(i.intValue());
                          inv.clear(24);
                          for (Integer slot : Arrays.<Integer>asList(new Integer[] { Integer.valueOf(45), Integer.valueOf(46), Integer.valueOf(47), Integer.valueOf(48), Integer.valueOf(50), Integer.valueOf(51), Integer.valueOf(52), Integer.valueOf(53) }))
                            inv.setItem(slot.intValue(), badItem); 
                          continue;
                        } 
                        inv.clear(24);
                        for (Integer slot : Arrays.<Integer>asList(new Integer[] { Integer.valueOf(45), Integer.valueOf(46), Integer.valueOf(47), Integer.valueOf(48), Integer.valueOf(50), Integer.valueOf(51), Integer.valueOf(52), Integer.valueOf(53) }))
                          inv.setItem(slot.intValue(), badItem); 
                      } 
                    } 
                    ItemStack resultFix = Main.this.crafts.getConfig().getItemStack("Shaped." + st + ".result");
                    if (resultFix.getItemMeta() != null && resultFix.getItemMeta().getLore() != null && !resultFix.getItemMeta().getLore().isEmpty()) {
                      ItemMeta meta = resultFix.getItemMeta();
                      List<String> loreNew = new ArrayList<>();
                      for (String l : resultFix.getItemMeta().getLore())
                        loreNew.add(l.replaceAll("<player>", p.getName()).replaceAll("<date>", String.valueOf(LocalDate.now(ZoneId.of(Main.this.getConfig().getString("zone-time"))))).replaceAll("<time>", String.valueOf(LocalTime.now(ZoneId.of(Main.this.getConfig().getString("zone-time")))))); 
                      meta.setLore(loreNew);
                      resultFix.setItemMeta(meta);
                    } 
                    Main.this.checkCraft(resultFix, e.getClickedInventory(), ingredients, p, e.getView());
                    p.getInventory().addItem(new ItemStack[] { resultFix });
                  } 
                }
              }).runTask((Plugin)this);
          } 
          return;
        } 
        if (Arrays.<Integer>asList(new Integer[] { Integer.valueOf(10), Integer.valueOf(11), Integer.valueOf(12), Integer.valueOf(19), Integer.valueOf(20), Integer.valueOf(21), Integer.valueOf(28), Integer.valueOf(29), Integer.valueOf(30) }).contains(Integer.valueOf(e.getSlot()))) {
          (new BukkitRunnable() {
              public void run() {
                String st = Main.this.getPathRecipe(p.getOpenInventory().getTopInventory());
                if (st != null) {
                  e.setCancelled(false);
                  Inventory inv = e.getInventory();
                  HashMap<Integer, ItemStack> ingredients = Main.this.getMap(st);
                  ItemStack badItem = new ItemStack(Material.valueOf(Main.this.getConfig().getString("workbench.bad-craft.material")), 1, (byte)Main.this.getConfig().getInt("workbench.bad-craft.data"));
                  ItemMeta badMeta = badItem.getItemMeta();
                  badMeta.setDisplayName(Main.this.getConfig().getString("workbench.bad-craft.name").replaceAll("&", "));
                  List<String> loreFixBad = new ArrayList<>();
                  for (String stLore : Main.this.getConfig().getStringList("workbench.bad-craft.lore"))
                    loreFixBad.add(stLore.replaceAll("&", ")); 
                  badMeta.setLore(loreFixBad);
                  badItem.setItemMeta(badMeta);
                  for (Integer i : Arrays.<Integer>asList(new Integer[] { Integer.valueOf(10), Integer.valueOf(11), Integer.valueOf(12), Integer.valueOf(19), Integer.valueOf(20), Integer.valueOf(21), Integer.valueOf(28), Integer.valueOf(29), Integer.valueOf(30) })) {
                    if (ingredients.containsKey(i)) {
                      if (inv.getItem(i.intValue()) != null && inv.getItem(i.intValue()).getType() != Material.AIR && inv.getItem(i.intValue()).isSimilar(ingredients.get(i)) && 
                        inv.getItem(i.intValue()).getAmount() < ((ItemStack)ingredients.get(i)).getAmount()) {
                        inv.clear(24);
                        for (Integer slot : Arrays.<Integer>asList(new Integer[] { Integer.valueOf(45), Integer.valueOf(46), Integer.valueOf(47), Integer.valueOf(48), Integer.valueOf(50), Integer.valueOf(51), Integer.valueOf(52), Integer.valueOf(53) }))
                          inv.setItem(slot.intValue(), badItem); 
                        return;
                      } 
                      if (inv.getItem(i.intValue()) == null || inv.getItem(i.intValue()).getType() == Material.AIR || !inv.getItem(i.intValue()).isSimilar(ingredients.get(i))) {
                        if (inv.getItem(i.intValue()) != null && !inv.getItem(i.intValue()).isSimilar(ingredients.get(i))) {
                          inv.clear(24);
                          for (Integer slot : Arrays.<Integer>asList(new Integer[] { Integer.valueOf(45), Integer.valueOf(46), Integer.valueOf(47), Integer.valueOf(48), Integer.valueOf(50), Integer.valueOf(51), Integer.valueOf(52), Integer.valueOf(53) }))
                            inv.setItem(slot.intValue(), badItem); 
                        } 
                        return;
                      } 
                      continue;
                    } 
                    if (inv.getItem(i.intValue()) != null && inv.getItem(i.intValue()).getType() != Material.AIR) {
                      inv.clear(24);
                      for (Integer slot : Arrays.<Integer>asList(new Integer[] { Integer.valueOf(45), Integer.valueOf(46), Integer.valueOf(47), Integer.valueOf(48), Integer.valueOf(50), Integer.valueOf(51), Integer.valueOf(52), Integer.valueOf(53) }))
                        inv.setItem(slot.intValue(), badItem); 
                      return;
                    } 
                  } 
                  ItemStack result = Main.this.crafts.getConfig().getItemStack("Shaped." + st + ".result");
                  ItemStack resultFix = result;
                  if (result.getItemMeta() != null && result.getItemMeta().getLore() != null && !result.getItemMeta().getLore().isEmpty()) {
                    ItemMeta meta = resultFix.getItemMeta();
                    List<String> loreNew = new ArrayList<>();
                    for (String loSt : result.getItemMeta().getLore())
                      loreNew.add(loSt.replaceAll("<player>", p.getName()).replaceAll("<date>", String.valueOf(LocalDate.now(ZoneId.of(Main.this.getConfig().getString("zone-time"))))).replaceAll("<time>", String.valueOf(LocalTime.now(ZoneId.of(Main.this.getConfig().getString("zone-time")))))); 
                    meta.setLore(loreNew);
                    resultFix.setItemMeta(meta);
                  } 
                  Main.this.updateInventory(e.getInventory(), e.getView(), resultFix);
                } else {
                  ItemStack badItem = new ItemStack(Material.valueOf(Main.this.getConfig().getString("workbench.bad-craft.material")), 1, (byte)Main.this.getConfig().getInt("workbench.bad-craft.data"));
                  ItemMeta badMeta = badItem.getItemMeta();
                  badMeta.setDisplayName(Main.this.getConfig().getString("workbench.bad-craft.name").replaceAll("&", "));
                  List<String> loreFixBad = new ArrayList<>();
                  for (String stLore : Main.this.getConfig().getStringList("workbench.bad-craft.lore"))
                    loreFixBad.add(stLore.replaceAll("&", ")); 
                  badMeta.setLore(loreFixBad);
                  badItem.setItemMeta(badMeta);
                  e.getInventory().clear(24);
                  for (Integer slot : Arrays.<Integer>asList(new Integer[] { Integer.valueOf(45), Integer.valueOf(46), Integer.valueOf(47), Integer.valueOf(48), Integer.valueOf(50), Integer.valueOf(51), Integer.valueOf(52), Integer.valueOf(53) }))
                    e.getInventory().setItem(slot.intValue(), badItem); 
                } 
              }
            }).runTask((Plugin)this);
          return;
        } 
        e.setCancelled(true);
        return;
      } 
    } 
  }
  
  @EventHandler
  public void dragAdvancedTable(final InventoryDragEvent e) {
    if (e.getInventory() == null)
      return; 
    if (e.getView().getTitle().equals(getConfig().getString("workbench.title").replaceAll("&", ")) && e.getInventory().equals(e.getView().getTopInventory())) {
      boolean cancel = false;
      for (Integer slot : e.getNewItems().keySet()) {
        if (slot.intValue() == 24) {
          cancel = true;
          break;
        } 
      } 
      e.setCancelled(cancel);
      if (!cancel)
        (new BukkitRunnable() {
            public void run() {
              String st = Main.this.getPathRecipe(e.getView().getTopInventory());
              if (st != null) {
                Inventory inv = e.getInventory();
                HashMap<Integer, ItemStack> ingredients = Main.this.getMap(st);
                ItemStack badItem = new ItemStack(Material.valueOf(Main.this.getConfig().getString("workbench.bad-craft.material")), 1, (byte)Main.this.getConfig().getInt("workbench.bad-craft.data"));
                ItemMeta badMeta = badItem.getItemMeta();
                badMeta.setDisplayName(Main.this.getConfig().getString("workbench.bad-craft.name").replaceAll("&", "));
                List<String> loreFixBad = new ArrayList<>();
                for (String as : Main.this.getConfig().getStringList("workbench.bad-craft.lore"))
                  loreFixBad.add(as.replaceAll("&", ")); 
                badMeta.setLore(loreFixBad);
                badItem.setItemMeta(badMeta);
                for (Integer i : Arrays.<Integer>asList(new Integer[] { Integer.valueOf(10), Integer.valueOf(11), Integer.valueOf(12), Integer.valueOf(19), Integer.valueOf(20), Integer.valueOf(21), Integer.valueOf(28), Integer.valueOf(29), Integer.valueOf(30) })) {
                  if (ingredients.containsKey(i)) {
                    if (inv.getItem(i.intValue()) != null && inv.getItem(i.intValue()).getType() != Material.AIR && inv.getItem(i.intValue()).isSimilar(ingredients.get(i)) && 
                      inv.getItem(i.intValue()).getAmount() < ((ItemStack)ingredients.get(i)).getAmount()) {
                      inv.clear(24);
                      for (Integer slot : Arrays.<Integer>asList(new Integer[] { Integer.valueOf(45), Integer.valueOf(46), Integer.valueOf(47), Integer.valueOf(48), Integer.valueOf(50), Integer.valueOf(51), Integer.valueOf(52), Integer.valueOf(53) }))
                        inv.setItem(slot.intValue(), badItem); 
                      return;
                    } 
                    if (inv.getItem(i.intValue()) == null || inv.getItem(i.intValue()).getType() == Material.AIR || !inv.getItem(i.intValue()).isSimilar(ingredients.get(i))) {
                      if (inv.getItem(i.intValue()) != null && !inv.getItem(i.intValue()).isSimilar(ingredients.get(i))) {
                        inv.clear(24);
                        for (Integer slot : Arrays.<Integer>asList(new Integer[] { Integer.valueOf(45), Integer.valueOf(46), Integer.valueOf(47), Integer.valueOf(48), Integer.valueOf(50), Integer.valueOf(51), Integer.valueOf(52), Integer.valueOf(53) }))
                          inv.setItem(slot.intValue(), badItem); 
                      } 
                      return;
                    } 
                    continue;
                  } 
                  if (inv.getItem(i.intValue()) != null && inv.getItem(i.intValue()).getType() != Material.AIR) {
                    inv.clear(24);
                    for (Integer slot : Arrays.<Integer>asList(new Integer[] { Integer.valueOf(45), Integer.valueOf(46), Integer.valueOf(47), Integer.valueOf(48), Integer.valueOf(50), Integer.valueOf(51), Integer.valueOf(52), Integer.valueOf(53) }))
                      inv.setItem(slot.intValue(), badItem); 
                    return;
                  } 
                } 
                ItemStack result = Main.this.crafts.getConfig().getItemStack("Shaped." + st + ".result");
                ItemStack resultFix = result;
                if (result.getItemMeta() != null && result.getItemMeta().getLore() != null && !result.getItemMeta().getLore().isEmpty()) {
                  ItemMeta meta = resultFix.getItemMeta();
                  List<String> loreNew = new ArrayList<>();
                  for (String loSt : result.getItemMeta().getLore())
                    loreNew.add(loSt.replaceAll("<player>", e.getWhoClicked().getName()).replaceAll("<date>", String.valueOf(LocalDate.now(ZoneId.of(Main.this.getConfig().getString("zone-time"))))).replaceAll("<time>", String.valueOf(LocalTime.now(ZoneId.of(Main.this.getConfig().getString("zone-time")))))); 
                  meta.setLore(loreNew);
                  resultFix.setItemMeta(meta);
                } 
                Main.this.updateInventory(e.getView().getTopInventory(), e.getView(), Main.this.crafts.getConfig().getItemStack("Shaped." + st + ".result"));
              } else {
                ItemStack badItem = new ItemStack(Material.valueOf(Main.this.getConfig().getString("workbench.bad-craft.material")), 1, (byte)Main.this.getConfig().getInt("workbench.bad-craft.data"));
                ItemMeta badMeta = badItem.getItemMeta();
                badMeta.setDisplayName(Main.this.getConfig().getString("workbench.bad-craft.name").replaceAll("&", "));
                List<String> loreFixBad = new ArrayList<>();
                for (String stLore : Main.this.getConfig().getStringList("workbench.bad-craft.lore"))
                  loreFixBad.add(stLore.replaceAll("&", ")); 
                badMeta.setLore(loreFixBad);
                badItem.setItemMeta(badMeta);
                e.getInventory().clear(24);
                for (Integer slot : Arrays.<Integer>asList(new Integer[] { Integer.valueOf(45), Integer.valueOf(46), Integer.valueOf(47), Integer.valueOf(48), Integer.valueOf(50), Integer.valueOf(51), Integer.valueOf(52), Integer.valueOf(53) }))
                  e.getInventory().setItem(slot.intValue(), badItem); 
              } 
            }
          }).runTask((Plugin)this); 
    } 
  }
  
  public void updateInventory(final Inventory inventory, InventoryView inventoryView, final ItemStack result) {
    (new BukkitRunnable() {
        public void run() {
          inventory.clear(24);
          inventory.setItem(24, result);
          ItemStack goodItem = new ItemStack(Material.valueOf(Main.this.getConfig().getString("workbench.good-craft.material")), 1, (byte)Main.this.getConfig().getInt("workbench.good-craft.data"));
          ItemMeta goodMeta = goodItem.getItemMeta();
          goodMeta.setDisplayName(Main.this.getConfig().getString("workbench.good-craft.name").replaceAll("&", "));
          List<String> loreFix = new ArrayList<>();
          for (String st : Main.this.getConfig().getStringList("workbench.good-craft.lore"))
            loreFix.add(st.replaceAll("&", ")); 
          goodMeta.setLore(loreFix);
          goodItem.setItemMeta(goodMeta);
          for (Integer slot : Arrays.<Integer>asList(new Integer[] { Integer.valueOf(45), Integer.valueOf(46), Integer.valueOf(47), Integer.valueOf(48), Integer.valueOf(50), Integer.valueOf(51), Integer.valueOf(52), Integer.valueOf(53) }))
            inventory.setItem(slot.intValue(), goodItem); 
        }
      }).runTask((Plugin)this);
  }
  
  public void openCustomWorkbench(Player p) {
    Inventory inv = Bukkit.createInventory(null, 54, getConfig().getString("workbench.title").replaceAll("&", "));
    ItemStack fillitem = new ItemStack(Material.valueOf(getConfig().getString("workbench.fill-item.material")), 1, (byte)getConfig().getInt("workbench.fill-item.data"));
    ItemMeta meta = fillitem.getItemMeta();
    meta.setDisplayName(getConfig().getString("workbench.fill-item.name").replaceAll("&", "));
    List<String> lore = new ArrayList<>();
    for (String st : getConfig().getStringList("workbench.fill-item.lore"))
      lore.add(st.replaceAll("&", ")); 
    meta.setLore(lore);
    fillitem.setItemMeta(meta);
    ItemStack badItem = new ItemStack(Material.valueOf(getConfig().getString("workbench.bad-craft.material")), 1, (byte)getConfig().getInt("workbench.bad-craft.data"));
    ItemMeta badMeta = badItem.getItemMeta();
    badMeta.setDisplayName(getConfig().getString("workbench.bad-craft.name").replaceAll("&", "));
    List<String> loreFixBad = new ArrayList<>();
    for (String st : getConfig().getStringList("workbench.bad-craft.lore"))
      loreFixBad.add(st.replaceAll("&", ")); 
    badMeta.setLore(loreFixBad);
    badItem.setItemMeta(badMeta);
    for (Integer slot : Arrays.<Integer>asList(new Integer[] { 
          Integer.valueOf(0), Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(3), Integer.valueOf(4), Integer.valueOf(5), Integer.valueOf(6), Integer.valueOf(7), Integer.valueOf(8), Integer.valueOf(9), 
          Integer.valueOf(13), Integer.valueOf(14), Integer.valueOf(15), Integer.valueOf(16), Integer.valueOf(17), Integer.valueOf(18), Integer.valueOf(22), Integer.valueOf(23), Integer.valueOf(25), Integer.valueOf(26), 
          Integer.valueOf(27), Integer.valueOf(31), Integer.valueOf(32), Integer.valueOf(33), Integer.valueOf(34), Integer.valueOf(35), Integer.valueOf(36), Integer.valueOf(37), Integer.valueOf(38), Integer.valueOf(39), 
          Integer.valueOf(40), Integer.valueOf(41), Integer.valueOf(42), Integer.valueOf(43), Integer.valueOf(44), Integer.valueOf(45), Integer.valueOf(46), Integer.valueOf(47), Integer.valueOf(48), Integer.valueOf(50), 
          Integer.valueOf(51), Integer.valueOf(52), Integer.valueOf(53) })) {
      if (slot.intValue() >= 45) {
        inv.setItem(slot.intValue(), badItem);
        continue;
      } 
      inv.setItem(slot.intValue(), fillitem);
    } 
    ItemStack backItem = new ItemStack(Material.valueOf(getConfig().getString("workbench.back-item.material")), 1, (byte)getConfig().getInt("workbench.back-item.data"));
    ItemMeta meta2 = backItem.getItemMeta();
    meta2.setDisplayName(getConfig().getString("workbench.back-item.name").replaceAll("&", "));
    List<String> lore2 = new ArrayList<>();
    for (String st : getConfig().getStringList("workbench.back-item.lore"))
      lore2.add(st.replaceAll("&", ")); 
    meta2.setLore(lore2);
    backItem.setItemMeta(meta2);
    inv.setItem(49, backItem);
    p.openInventory(inv);
  }
  
  public boolean existThisResult(ItemStack result) {
    for (String st : this.crafts.getConfig().getConfigurationSection("Shaped").getKeys(false)) {
      if (this.crafts.getConfig().getItemStack("Shaped." + st + ".result").isSimilar(result))
        return true; 
    } 
    return false;
  }
  
  public String getPathRecipe(Inventory inv) {
    for (String st : this.crafts.getConfig().getConfigurationSection("Shaped").getKeys(false)) {
      boolean pathFound = false;
      HashMap<Integer, ItemStack> ingredients = getMap(st);
      for (Integer i : Arrays.<Integer>asList(new Integer[] { Integer.valueOf(10), Integer.valueOf(11), Integer.valueOf(12), Integer.valueOf(19), Integer.valueOf(20), Integer.valueOf(21), Integer.valueOf(28), Integer.valueOf(29), Integer.valueOf(30) })) {
        if (ingredients.containsKey(i)) {
          if (inv.getItem(i.intValue()) != null && inv.getItem(i.intValue()).getType() != Material.AIR && inv.getItem(i.intValue()).isSimilar(ingredients.get(i)) && inv.getItem(i.intValue()).getAmount() >= ((ItemStack)ingredients.get(i)).getAmount()) {
            pathFound = true;
            continue;
          } 
          pathFound = false;
          break;
        } 
        if (inv.getItem(i.intValue()) != null && inv.getItem(i.intValue()).getType() != Material.AIR) {
          pathFound = false;
          break;
        } 
      } 
      if (pathFound)
        return st; 
    } 
    return null;
  }
  
  public boolean existThisRecipe(Inventory inv) {
    Iterator<String> iterator = this.crafts.getConfig().getConfigurationSection("Shaped").getKeys(false).iterator();
    if (iterator.hasNext()) {
      String st = iterator.next();
      HashMap<Integer, ItemStack> ingredients = getMap(st);
      for (Integer i : Arrays.<Integer>asList(new Integer[] { Integer.valueOf(10), Integer.valueOf(11), Integer.valueOf(12), Integer.valueOf(19), Integer.valueOf(20), Integer.valueOf(21), Integer.valueOf(28), Integer.valueOf(29), Integer.valueOf(30) })) {
        if (ingredients.containsKey(i) ? (
          inv.getItem(i.intValue()) == null || inv.getItem(i.intValue()).getType() == Material.AIR || !inv.getItem(i.intValue()).isSimilar(ingredients.get(i))) : (
          
          inv.getItem(i.intValue()) != null && inv.getItem(i.intValue()).getType() != Material.AIR))
          break; 
      } 
      return true;
    } 
    return false;
  }
  
  public HashMap<Integer, ItemStack> getMap(String st) {
    HashMap<Integer, ItemStack> mapRecipes = new HashMap<>();
    for (String points : this.crafts.getConfig().getConfigurationSection("Shaped." + st).getKeys(false)) {
      if (!points.equals("result")) {
        int number = Integer.valueOf(points).intValue();
        if (number == 1)
          mapRecipes.put(Integer.valueOf(10), this.crafts.getConfig().getItemStack("Shaped." + st + "." + points)); 
        if (number == 2)
          mapRecipes.put(Integer.valueOf(11), this.crafts.getConfig().getItemStack("Shaped." + st + "." + points)); 
        if (number == 3)
          mapRecipes.put(Integer.valueOf(12), this.crafts.getConfig().getItemStack("Shaped." + st + "." + points)); 
        if (number == 4)
          mapRecipes.put(Integer.valueOf(19), this.crafts.getConfig().getItemStack("Shaped." + st + "." + points)); 
        if (number == 5)
          mapRecipes.put(Integer.valueOf(20), this.crafts.getConfig().getItemStack("Shaped." + st + "." + points)); 
        if (number == 6)
          mapRecipes.put(Integer.valueOf(21), this.crafts.getConfig().getItemStack("Shaped." + st + "." + points)); 
        if (number == 7)
          mapRecipes.put(Integer.valueOf(28), this.crafts.getConfig().getItemStack("Shaped." + st + "." + points)); 
        if (number == 8)
          mapRecipes.put(Integer.valueOf(29), this.crafts.getConfig().getItemStack("Shaped." + st + "." + points)); 
        if (number == 9)
          mapRecipes.put(Integer.valueOf(30), this.crafts.getConfig().getItemStack("Shaped." + st + "." + points)); 
      } 
    } 
    return mapRecipes;
  }
  
  public void checkCraft(final ItemStack result, final Inventory inv, final HashMap<Integer, ItemStack> ingredients, final Player p, final InventoryView view) {
    ItemStack goodItem = new ItemStack(Material.valueOf(getConfig().getString("workbench.good-craft.material")), 1, (byte)getConfig().getInt("workbench.good-craft.data"));
    ItemMeta goodMeta = goodItem.getItemMeta();
    goodMeta.setDisplayName(getConfig().getString("workbench.good-craft.name").replaceAll("&", "));
    List<String> loreFix = new ArrayList<>();
    for (String st : getConfig().getStringList("workbench.good-craft.lore"))
      loreFix.add(st.replaceAll("&", ")); 
    goodMeta.setLore(loreFix);
    goodItem.setItemMeta(goodMeta);
    final ItemStack badItem = new ItemStack(Material.valueOf(getConfig().getString("workbench.bad-craft.material")), 1, (byte)getConfig().getInt("workbench.bad-craft.data"));
    ItemMeta badMeta = badItem.getItemMeta();
    badMeta.setDisplayName(getConfig().getString("workbench.bad-craft.name").replaceAll("&", "));
    List<String> loreFixBad = new ArrayList<>();
    for (String st : getConfig().getStringList("workbench.bad-craft.lore"))
      loreFixBad.add(st.replaceAll("&", ")); 
    badMeta.setLore(loreFixBad);
    badItem.setItemMeta(badMeta);
    (new BukkitRunnable() {
        public void run() {
          for (Integer i : Arrays.<Integer>asList(new Integer[] { Integer.valueOf(10), Integer.valueOf(11), Integer.valueOf(12), Integer.valueOf(19), Integer.valueOf(20), Integer.valueOf(21), Integer.valueOf(28), Integer.valueOf(29), Integer.valueOf(30) })) {
            if (ingredients.containsKey(i)) {
              if (inv.getItem(i.intValue()) != null && inv.getItem(i.intValue()).getType() != Material.AIR && inv.getItem(i.intValue()).isSimilar((ItemStack)ingredients.get(i)) && 
                inv.getItem(i.intValue()).getAmount() < ((ItemStack)ingredients.get(i)).getAmount()) {
                inv.clear(24);
                for (Integer slot : Arrays.<Integer>asList(new Integer[] { Integer.valueOf(45), Integer.valueOf(46), Integer.valueOf(47), Integer.valueOf(48), Integer.valueOf(50), Integer.valueOf(51), Integer.valueOf(52), Integer.valueOf(53) }))
                  inv.setItem(slot.intValue(), badItem); 
                return;
              } 
              if (inv.getItem(i.intValue()) == null || inv.getItem(i.intValue()).getType() == Material.AIR || !inv.getItem(i.intValue()).isSimilar((ItemStack)ingredients.get(i))) {
                if (inv.getItem(i.intValue()) != null && !inv.getItem(i.intValue()).isSimilar((ItemStack)ingredients.get(i))) {
                  inv.clear(24);
                  for (Integer slot : Arrays.<Integer>asList(new Integer[] { Integer.valueOf(45), Integer.valueOf(46), Integer.valueOf(47), Integer.valueOf(48), Integer.valueOf(50), Integer.valueOf(51), Integer.valueOf(52), Integer.valueOf(53) }))
                    inv.setItem(slot.intValue(), badItem); 
                } 
                return;
              } 
              continue;
            } 
            if (inv.getItem(i.intValue()) != null && inv.getItem(i.intValue()).getType() != Material.AIR)
              return; 
          } 
          ItemStack resultFix = result;
          if (result.getItemMeta() != null && result.getItemMeta().getLore() != null && !result.getItemMeta().getLore().isEmpty()) {
            ItemMeta meta = resultFix.getItemMeta();
            List<String> loreNew = new ArrayList<>();
            for (String st : result.getItemMeta().getLore())
              loreNew.add(st.replaceAll("<player>", p.getName()).replaceAll("<date>", String.valueOf(LocalDate.now(ZoneId.of(Main.this.getConfig().getString("zone-time"))))).replaceAll("<time>", String.valueOf(LocalTime.now(ZoneId.of(Main.this.getConfig().getString("zone-time")))))); 
            meta.setLore(loreNew);
            resultFix.setItemMeta(meta);
          } 
          Main.this.updateInventory(inv, view, resultFix);
        }
      }).runTask((Plugin)this);
  }
  
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    if (!(sender instanceof Player)) {
      sender.sendMessage("Invaild Command.");
      return true;
    } 
    Player p = (Player)sender;
    if (label.equalsIgnoreCase("wbc")) {
      openCustomWorkbench(p);
      return true;
    } 
    if (label.equalsIgnoreCase("customcrafting")) {
      if (args.length <= 0) {
        if (!p.isOp()) {
          p.sendMessage(ChatColor.YELLOW + "CustomCrafting has been created by " + ChatColor.GREEN + "DeivydSao");
        } else {
          p.sendMessage(ChatColor.AQUA + "CustomCrafting has been created by " + ChatColor.YELLOW + "DeivydSao");
          p.sendMessage(ChatColor.GREEN + "/customcrafting " + ChatColor.WHITE + "Main command");
          p.sendMessage(ChatColor.GREEN + "/customcrafting createshaped <recipe-name> " + ChatColor.WHITE + "Create recipe");
          p.sendMessage(ChatColor.GREEN + "/customcrafting removerecipe " + ChatColor.WHITE + "Remove recipe");
        } 
        return true;
      } 
      if (p.isOp()) {
        if (args.length >= 2) {
          if (args[0].equalsIgnoreCase("wb")) {
            openCustomWorkbench(p);
            return true;
          } 
          if (args[0].equalsIgnoreCase("removerecipe")) {
            List<String> h = this.crafts.getConfig().getStringList("crafts-list");
            if (!h.contains(String.valueOf(args[1]))) {
              p.sendMessage(getConfig().getString("not-exits").replaceAll("&", "));
              return true;
            } 
            h.remove(args[1]);
            this.crafts.getConfig().set("crafts-list", h);
            this.crafts.save();
            if (this.crafts.getConfig().contains("Shaped." + args[1])) {
              this.crafts.getConfig().set("Shaped." + args[1], null);
              this.crafts.save();
            } 
            p.sendMessage(getConfig().getString("remove-succes").replaceAll("&", "));
            return true;
          } 
          if (args[0].equalsIgnoreCase("createshaped")) {
            if (p.getItemInHand() == null || p.getItemInHand().getType() == Material.AIR) {
              p.sendMessage(getConfig().getString("item-must-be-in-hand").replaceAll("&", "));
              return true;
            } 
            List<String> h = this.crafts.getConfig().getStringList("crafts-list");
            if (h.contains(String.valueOf(args[1]))) {
              p.sendMessage(getConfig().getString("already-created").replaceAll("&", "));
              return true;
            } 
            h.add(args[1]);
            this.crafts.getConfig().set("crafts-list", h);
            this.crafts.save();
            if (!this.list.contains(p))
              this.list.add(p); 
            Inventory inv = Bukkit.createInventory(null, InventoryType.DISPENSER, String.valueOf(args[1]));
            p.openInventory(inv);
            return true;
          } 
        } else {
          p.sendMessage(ChatColor.AQUA + "CustomCrafting has been created by " + ChatColor.YELLOW + "DeivydSao");
          p.sendMessage(ChatColor.GREEN + "/customcrafting " + ChatColor.WHITE + "main command");
          p.sendMessage(ChatColor.GREEN + "/customcrafting createshaped <recipe-name> " + ChatColor.WHITE + "create recipe");
          p.sendMessage(ChatColor.GREEN + "/customcrafting removerecipe " + ChatColor.WHITE + "remove recipe");
        } 
      } else {
        p.sendMessage(ChatColor.YELLOW + "CustomCrafting has been created by " + ChatColor.GREEN + "DeivydSao");
      } 
    } 
    return false;
  }
}
