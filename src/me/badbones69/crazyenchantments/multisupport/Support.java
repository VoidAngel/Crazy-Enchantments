package me.badbones69.crazyenchantments.multisupport;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import me.badbones69.crazyenchantments.Main;

public class Support {
	
	public static boolean hasEpicSpawners(){
		if(Bukkit.getServer().getPluginManager().getPlugin("EpicSpawners")!=null){
			return true;
		}
		return false;
	}
	
	public static boolean hasAAC(){
		if(Bukkit.getServer().getPluginManager().getPlugin("AAC")!=null){
			return true;
		}
		return false;
	}
	
	public static boolean hasDakata(){
		if(Bukkit.getServer().getPluginManager().getPlugin("DakataAntiCheat")!=null){
			return true;
		}
		return false;
	}
	
	public static boolean hasNoCheatPlus(){
		if(Bukkit.getServer().getPluginManager().getPlugin("NoCheatPlus")!=null){
			return true;
		}
		return false;
	}
	
	public static boolean hasVault(){
		if(Bukkit.getServer().getPluginManager().getPlugin("Vault")!=null){
			return true;
		}
		return false;
	}
	
	public static boolean hasWorldEdit(){
		if(Bukkit.getServer().getPluginManager().getPlugin("WorldEdit")!=null){
			return true;
		}
		return false;
	}
	
	public static boolean hasWorldGuard(){
		if(Bukkit.getServer().getPluginManager().getPlugin("WorldGuard")!=null){
			return true;
		}
		return false;
	}
	
	public static boolean hasFactions(){
		if(Bukkit.getServer().getPluginManager().getPlugin("Factions")!=null){
			return true;
		}
		return false;
	}
	
	public static boolean hasFeudal(){
		if(Bukkit.getServer().getPluginManager().getPlugin("Feudal")!=null){
			return true;
		}
		return false;
	}
	
	public static boolean hasAcidIsland(){
		if(Bukkit.getServer().getPluginManager().getPlugin("AcidIsland")!=null){
			return true;
		}
		return false;
	}
	
	public static boolean hasASkyBlock(){
		if(Bukkit.getServer().getPluginManager().getPlugin("ASkyBlock")!=null){
			return true;
		}
		return false;
	}
	
	public static boolean hasKingdoms(){
		if(Bukkit.getServer().getPluginManager().getPlugin("Kingdoms")!=null){
			return true;
		}
		return false;
	}
	
	public static boolean hasSilkSpawner(){
		if(Bukkit.getServer().getPluginManager().getPlugin("SilkSpawners")!=null){
			return true;
		}
		return false;
	}
	
	public static boolean hasSpartan(){
		if(Bukkit.getServer().getPluginManager().getPlugin("Spartan")!=null){
			return true;
		}
		return false;
	}
	
	public static boolean hasMobStacker() {
		if(Bukkit.getServer().getPluginManager().getPlugin("MobStacker")!=null){
			return true;
		}
		return false;
	}
	
	public static boolean hasMobStacker2() {
		if(Bukkit.getServer().getPluginManager().getPlugin("MobStacker2")!=null){
			return true;
		}
		return false;
	}
	
	public static boolean hasStackMob() {
		if(Bukkit.getServer().getPluginManager().getPlugin("StackMob")!=null){
			return true;
		}
		return false;
	}
	
	public static boolean hasMegaSkills() {
		if(Bukkit.getServer().getPluginManager().getPlugin("MegaSkills")!=null){
			return true;
		}
		return false;
	}
	
	public static boolean inTerritory(Player player){
		if(hasASkyBlock()){
			if(ASkyBlockSupport.inTerritory(player)){
				return true;
			}
		}
		if(hasKingdoms()){
			if(KingdomSupport.inTerritory(player)){
				return true;
			}
		}
		return false;
	}
	
	public static boolean isFriendly(Entity P, Entity O){
		if(P instanceof Player&&O instanceof Player){
			Player player = (Player) P;
			Player other = (Player) O;
			if(hasASkyBlock()){
				if(ASkyBlockSupport.isFriendly(player, other)){
					return true;
				}
			}
			if(hasKingdoms()){
				if(KingdomSupport.isFriendly(player, other)){
					return true;
				}
			}
		}
		return false;
	}
	
	public static boolean canBreakBlock(Player player, Block block){
		if(hasKingdoms()){
			if(KingdomSupport.canBreakBlock(player, block)){
				return true;
			}
		}
		return true;
	}
	
	public static boolean allowsPVP(Location loc){
		if(hasWorldEdit() && hasWorldGuard()){
			if(WorldGuard.allowsPVP(loc))return true;
			if(!WorldGuard.allowsPVP(loc))return false;
		}
		return true;
	}
	
	public static boolean allowsBreak(Location loc){
		if(hasWorldEdit() && hasWorldGuard()){
			if(WorldGuard.allowsBreak(loc))return true;
			if(!WorldGuard.allowsBreak(loc))return false;
		}
		return true;
	}
	
	public static boolean allowsExplotions(Location loc){
		if(hasWorldEdit() && hasWorldGuard()){
			if(WorldGuard.allowsExplosions(loc))return true;
			if(!WorldGuard.allowsExplosions(loc))return false;
		}
		return true;
	}
	
	public static boolean inWingsRegion(Player player){
		if(Main.settings.getConfig().contains("Settings.EnchantmentOptions.Wings.Regions")){
			for(String rg : Main.settings.getConfig().getStringList("Settings.EnchantmentOptions.Wings.Regions")){
				if(hasWorldEdit() && hasWorldGuard()){
					if(WorldGuard.inRegion(rg, player.getLocation())){
						return true;
					}else{
						if(Main.settings.getConfig().contains("Settings.EnchantmentOptions.Wings.Members-Can-Fly")){
							if(Main.settings.getConfig().getBoolean("Settings.EnchantmentOptions.Wings.Members-Can-Fly")){
								if(WorldGuard.isMember(player)){
									return true;
								}
							}
						}
						if(Main.settings.getConfig().contains("Settings.EnchantmentOptions.Wings.Owners-Can-Fly")){
							if(Main.settings.getConfig().getBoolean("Settings.EnchantmentOptions.Wings.Owners-Can-Fly")){
								if(WorldGuard.isOwner(player)){
									return true;
								}
							}
						}
					}
				}
			}
		}
		return false;
	}
	
	public static void noStack(Entity en) {
		if (hasMobStacker()) {
			MobStacker.noStack(en);
			return;
		}
		if (hasMobStacker2()) {
			MobStacker2.noStack(en);
			return;
		}
	}
	
}