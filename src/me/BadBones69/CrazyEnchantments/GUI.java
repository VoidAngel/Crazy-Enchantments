package me.BadBones69.CrazyEnchantments;

import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class GUI implements Listener{
	static void openGUI(Player player){
		Inventory inv = Bukkit.createInventory(null, Main.settings.getConfig().getInt("Settings.GUISize"), Api.getInvName());
		for(String cat : Main.settings.getConfig().getConfigurationSection("Categories").getKeys(false)){
			inv.setItem(Main.settings.getConfig().getInt("Categories."+cat+".Slot")-1, Api.makeItem(Main.settings.getConfig().getString("Categories."+cat+".Item"), 1, 
					Main.settings.getConfig().getString("Categories."+cat+".Name"), Main.settings.getConfig().getStringList("Categories."+cat+".Lore")));
		}
		if(Main.settings.getConfig().contains("Settings.GUICustomization")){
			for(String custom : Main.settings.getConfig().getStringList("Settings.GUICustomization")){
				String name = "";
				String item = "1";
				int slot = 0;
				ArrayList<String> lore = new ArrayList<String>();
				String[] b = custom.split(", ");
				for(String i : b){
					if(i.contains("Item:")){
						i=i.replace("Item:", "");
						item=i;
					}
					if(i.contains("Name:")){
						i=i.replace("Name:", "");
						name=i;
					}
					if(i.contains("Slot:")){
						i=i.replace("Slot:", "");
						slot=Integer.parseInt(i);
					}
					if(i.contains("Lore:")){
						i=i.replace("Lore:", "");
						String[] d = i.split("_");
						for(String l : d){
							lore.add(l);
						}
					}
				}
				slot--;
				inv.setItem(slot, Api.makeItem(item, 1, name, lore));
			}
		}
		player.openInventory(inv);
	}
	void openInfo(Player player){
		Inventory inv = Bukkit.createInventory(null, 54, Api.getInvName());
		for(ItemStack i : addInfo()){
			inv.addItem(i);
		}
		player.openInventory(inv);
	}
	@EventHandler
	public void onInvClick(InventoryClickEvent e){
		ItemStack item = e.getCurrentItem();
		Inventory inv = e.getInventory();
		Player player = (Player) e.getWhoClicked();
		if(inv!=null){
			if(inv.getName().equals(Api.getInvName())){
				e.setCancelled(true);
				if(item==null)return;
				if(item.hasItemMeta()){
					if(item.getItemMeta().hasDisplayName()){
						String name = item.getItemMeta().getDisplayName();
						for(String cat : Main.settings.getConfig().getConfigurationSection("Categories").getKeys(false)){
							if(name.equals(Api.color(Main.settings.getConfig().getString("Categories."+cat+".Name")))){
								if(player.getGameMode() != GameMode.CREATIVE){
									if(Main.settings.getConfig().getString("Categories."+cat+".Lvl/Total").equalsIgnoreCase("Lvl")){
										if(Api.getXPLvl(player)<Main.settings.getConfig().getInt("Categories."+cat+".XP")){
											String xp = Main.settings.getConfig().getInt("Categories."+cat+".XP") - Api.getXPLvl(player)+"";
											player.sendMessage(Api.color(Main.settings.getMsg().getString("Messages.Need-More-XP-Lvls").replace("%XP%", xp).replace("%xp%", xp)));
											return;
										}
										Api.takeLvlXP(player, Main.settings.getConfig().getInt("Categories."+cat+".XP"));
									}
									if(Main.settings.getConfig().getString("Categories."+cat+".Lvl/Total").equalsIgnoreCase("Total")){
										if(player.getTotalExperience()<Main.settings.getConfig().getInt("Categories."+cat+".XP")){
											String xp = Main.settings.getConfig().getInt("Categories."+cat+".XP") - player.getTotalExperience()+"";
											player.sendMessage(Api.color(Main.settings.getMsg().getString("Messages.Need-More-Total-XP").replace("%XP%", xp).replace("%xp%", xp)));
											return;
										}
										Api.takeTotalXP(player, Main.settings.getConfig().getInt("Categories."+cat+".XP"));
									}
								}
								player.getInventory().addItem(ECControl.pick(Main.settings.getConfig().getInt("Categories."+cat+".EnchOptions.SuccessPercent.Max"),
										Main.settings.getConfig().getInt("Categories."+cat+".EnchOptions.SuccessPercent.Min"), cat));
								return;
							}
						}
					}
				}
			}
		}
	}
	@SuppressWarnings("deprecation")
	@EventHandler
	public void addEnchantment(InventoryClickEvent e){
		Inventory inv = e.getInventory();
		Player player = (Player) e.getWhoClicked();
		if(inv != null){
			if(e.getCursor() != null&&e.getCurrentItem() != null){
				ItemStack c = e.getCursor();
				ItemStack item  = e.getCurrentItem();
				if(c.hasItemMeta()){
					if(c.getItemMeta().hasDisplayName()){
						String name = c.getItemMeta().getDisplayName();
						for(String en : Main.settings.getEnchs().getConfigurationSection("Enchantments").getKeys(false)){
							if(name.contains(Main.settings.getEnchs().getString("Enchantments."+en+".Name"))){
								for(Material m : ECControl.allEnchantments().get(en)){
									if(item.getType() == m){
										if(c.getAmount() == 1){
											if(item.getItemMeta().hasLore()){
												for(String l:item.getItemMeta().getLore()){
													if(l.contains(en)){
														return;
													}
												}
											}
											e.setCancelled(true);
											if(Api.successChance(c) || player.getGameMode() == GameMode.CREATIVE){
												name = Api.removeColor(name);
												String[] breakdown = name.split(" ");
												String color = Main.settings.getEnchs().getString("Enchantments."+en+".Color");
												String enchantment = Main.settings.getEnchs().getString("Enchantments."+en+".Name");
												String lvl = breakdown[1];
												String full = Api.color(color+enchantment+" "+lvl);
												e.setCursor(new ItemStack(Material.AIR));
												Api.addLore(item, full);
												player.playSound(player.getLocation(), Sound.LEVEL_UP, 1, 1);
												return;
											}else{
												e.setCursor(new ItemStack(Material.AIR));
												player.playSound(player.getLocation(), Sound.ITEM_BREAK, 1, 1);
												return;
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
	}
	@EventHandler
	public void infoClick(InventoryClickEvent e){
		Inventory inv = e.getInventory();
		if(inv!=null){
			if(inv.getName().equals(Api.color("&6&lEnchantment Info"))){
				e.setCancelled(true);
				return;
			}
		}
	}
	public static ArrayList<ItemStack> addInfo(){
		ArrayList<ItemStack> items = new ArrayList<ItemStack>();
		ArrayList<ItemStack> swords = new ArrayList<ItemStack>();
		ArrayList<ItemStack> axes = new ArrayList<ItemStack>();
		ArrayList<ItemStack> bows = new ArrayList<ItemStack>();
		ArrayList<ItemStack> armor = new ArrayList<ItemStack>();
		ArrayList<ItemStack> helmets = new ArrayList<ItemStack>();
		ArrayList<ItemStack> boots = new ArrayList<ItemStack>();
		swords.add(Api.makeItem(Material.GOLD_SWORD, 1, 0, "&e&lViper", Arrays.asList("&c&lSwords Only", "&3Has a chance to give", "&3Your enemy Poison")));
		swords.add(Api.makeItem(Material.GOLD_SWORD, 1, 0, "&e&lSlowMo", Arrays.asList("&c&lSwords Only", "&3Has a chance to give", "&3Your enemy Slowness")));
		swords.add(Api.makeItem(Material.GOLD_SWORD, 1, 0, "&e&lVampire", Arrays.asList("&c&lSwords Only", "&3Has a chance to give", "&3To gain 1 heart when attaking")));
		swords.add(Api.makeItem(Material.GOLD_SWORD, 1, 0, "&e&lFast Turn", Arrays.asList("&c&lSwords Only", "&3Has a chance", "&3To deal more damage")));
		swords.add(Api.makeItem(Material.GOLD_SWORD, 1, 0, "&e&lBlindness", Arrays.asList("&c&lSwords Only", "&3Has a chance to give", "&3Your enemy Blindness")));
		swords.add(Api.makeItem(Material.GOLD_SWORD, 1, 0, "&e&lLife Steal", Arrays.asList("&c&lSwords Only", "&3Has a chance to take", "&3Your enemies health")));
		swords.add(Api.makeItem(Material.GOLD_SWORD, 1, 0, "&e&lLight Weight", Arrays.asList("&c&lSwords Only", "&3Has a chance to", "&3Give You Hast")));
		swords.add(Api.makeItem(Material.GOLD_SWORD, 1, 0, "&e&lDouble Damage", Arrays.asList("&c&lSwords Only", "&3Has a chance to", "&3Deal Double Damage")));
		axes.add(Api.makeItem(Material.GOLD_AXE, 1, 0, "&e&lRekt", Arrays.asList("&c&lAxes Only", "&3Has a chance to", "&3Deal Double Damage")));
		axes.add(Api.makeItem(Material.GOLD_AXE, 1, 0, "&e&lDizzy", Arrays.asList("&c&lAxes Only", "&3Has a chance to give", "&3Your enemy Confusion")));
		axes.add(Api.makeItem(Material.GOLD_AXE, 1, 0, "&e&lCursed", Arrays.asList("&c&lAxes Only", "&3Has a chance to give", "&3Your enemy Mining Fatigue")));
		axes.add(Api.makeItem(Material.GOLD_AXE, 1, 0, "&e&lFeedMe", Arrays.asList("&c&lAxes Only", "&3Has a chance to", "&3Give you food when you attack")));
		axes.add(Api.makeItem(Material.GOLD_AXE, 1, 0, "&e&lBlessed", Arrays.asList("&c&lAxes Only", "&3Has a chance to", "&3Remove all bad effects from you.")));
		axes.add(Api.makeItem(Material.GOLD_AXE, 1, 0, "&e&lBerserk", Arrays.asList("&c&lAxes Only", "&3Has a chance to", "&3Give you Strength and Mining Fatigue")));
		bows.add(Api.makeItem(Material.BOW, 1, 0, "&e&lBoom", Arrays.asList("&c&lBows Only", "&3Has a chance to", "&3Spawn Primed Tnt on hit")));
		bows.add(Api.makeItem(Material.BOW, 1, 0, "&e&lVenom", Arrays.asList("&c&lBows Only", "&3Has a chance to", "&3Give your enemy Poison")));
		bows.add(Api.makeItem(Material.BOW, 1, 0, "&e&lDoctor", Arrays.asList("&c&lBows Only", "&3Has a chance to", "&3To heal a player you hit")));
		bows.add(Api.makeItem(Material.BOW, 1, 0, "&e&lPiercing", Arrays.asList("&c&lBows Only", "&3Has a chance to", "&3Deal Double Damage")));
		helmets.add(Api.makeItem(Material.GOLD_HELMET, 1, 0, "&e&lMermaid", Arrays.asList("&c&lHelmets Only", "&3Will give you Water Breathing", "&3Once you put the helmet on")));
		helmets.add(Api.makeItem(Material.GOLD_HELMET, 1, 0, "&e&lGlowing", Arrays.asList("&c&lHelmets Only", "&3Will give you Night Vision", "&3Once you put the helmet on")));
		boots.add(Api.makeItem(Material.GOLD_BOOTS, 1, 0, "&e&lGears", Arrays.asList("&c&lBoots Only", "&3Will give you Speed Boost", "&3Once you put the Boots on")));
		boots.add(Api.makeItem(Material.GOLD_BOOTS, 1, 0, "&e&lSprings", Arrays.asList("&c&lBoots Only", "&3Will give you Jump Boost", "&3Once you put the Boots on")));
		boots.add(Api.makeItem(Material.GOLD_BOOTS, 1, 0, "&e&lAnti Gravity", Arrays.asList("&c&lBoots Only", "&3Will give you Higher Jump Boost", "&3Once you put the Boots on")));
		armor.add(Api.makeItem(Material.GOLD_CHESTPLATE, 1, 0, "&e&lHulk", Arrays.asList("&c&lArmor Only", "&3Will give you Strength and Slowness", "&3Once you put the Armor on")));
		armor.add(Api.makeItem(Material.GOLD_CHESTPLATE, 1, 0, "&e&lNinja", Arrays.asList("&c&lArmor Only", "&3Will give you Speed and Health Boost", "&3Once you put the Armor on")));
		armor.add(Api.makeItem(Material.GOLD_CHESTPLATE, 1, 0, "&e&lMolten", Arrays.asList("&c&lArmor Only", "&3Has a chance to", "&3Ignight your attacker")));
		armor.add(Api.makeItem(Material.GOLD_CHESTPLATE, 1, 0, "&e&lSavior", Arrays.asList("&c&lArmor Only", "&3Has a chance to", "&3Take less incoming damage at low health")));
		armor.add(Api.makeItem(Material.GOLD_CHESTPLATE, 1, 0, "&e&lFreeze", Arrays.asList("&c&lArmor Only", "&3Has a chance to", "&3Slow your attacker")));
		armor.add(Api.makeItem(Material.GOLD_CHESTPLATE, 1, 0, "&e&lNursery", Arrays.asList("&c&lArmor Only", "&3Has a chance to", "&3Heal you while you walk.")));
		armor.add(Api.makeItem(Material.GOLD_CHESTPLATE, 1, 0, "&e&lFortify", Arrays.asList("&c&lArmor Only", "&3Has a chance to", "&3Give your attaker Weakness")));
		armor.add(Api.makeItem(Material.GOLD_CHESTPLATE, 1, 0, "&e&lOverLoad", Arrays.asList("&c&lArmor Only", "&3Will give you Health Boost", "&3Once you put the Armor on")));
		armor.add(Api.makeItem(Material.GOLD_CHESTPLATE, 1, 0, "&e&lPain Giver", Arrays.asList("&c&lArmor Only", "&3Has a chance to", "&3Give your attacker Poison")));
		armor.add(Api.makeItem(Material.GOLD_CHESTPLATE, 1, 0, "&e&lBurn Shield", Arrays.asList("&c&lArmor Only", "&3Will give you Fire Resistance", "&3Once you put the Armor on")));
		armor.add(Api.makeItem(Material.GOLD_CHESTPLATE, 1, 0, "&e&lEnlightened", Arrays.asList("&c&lArmor Only", "&3Has a chance to", "&3Heal you when being attacked")));
		armor.add(Api.makeItem(Material.GOLD_CHESTPLATE, 1, 0, "&e&lSelf Destruct", Arrays.asList("&c&lArmor Only", "&3When you die your", "&3Body will explode with uder destruction")));
		items.addAll(armor);
		items.addAll(helmets);
		items.addAll(boots);
		items.addAll(swords);
		items.addAll(axes);
		items.addAll(bows);
		return items;
	}
}