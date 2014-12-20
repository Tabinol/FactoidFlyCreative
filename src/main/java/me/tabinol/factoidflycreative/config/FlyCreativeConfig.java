package me.tabinol.factoidflycreative.config;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;
import java.util.logging.Level;

import me.tabinol.factoidflycreative.FactoidFlyCreative;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;

public class FlyCreativeConfig {

	private final FactoidFlyCreative thisPlugin;
    private FileConfiguration config;
    private Set<GameMode> ignoredGameMode;
    private boolean noDrop;
    private boolean noOpenChest;
    private boolean noBuildOutside;
    private Set<Material> bannedItems;
	
    public FlyCreativeConfig() {

        thisPlugin = FactoidFlyCreative.getThisPlugin();
        thisPlugin.saveDefaultConfig();
        config = thisPlugin.getConfig();
    }

    public void reLoadConfig() {

        thisPlugin.reloadConfig();
        config = thisPlugin.getConfig();
        loadConfig();
    }

    public void loadConfig() {

        ignoredGameMode = getGameModeList();
        noDrop = config.getBoolean("Creative.NoDrop");
        noOpenChest = config.getBoolean("Creative.NoOpenChest");
        noBuildOutside = config.getBoolean("Creative.NoBuildOutside");
        bannedItems = getMaterialList();
    }
    
    private Set<GameMode> getGameModeList() {
    	
    	Set<GameMode> gameModeList = Collections.synchronizedSet(EnumSet.noneOf(GameMode.class));; 
    	
    	for(String gameModeName : config.getStringList("IgnoredGameMode")) {
    		try {
    			gameModeList.add(GameMode.valueOf(gameModeName.toUpperCase()));
    		} catch(IllegalArgumentException ex) {
    			thisPlugin.getLogger().log(Level.WARNING, gameModeName + " is not a valid game mode!");
    		}
    	}
    	
    	return gameModeList;
    }
    
    private Set<Material> getMaterialList() {
    	
    	Set<Material> materialList = Collections.synchronizedSet(EnumSet.noneOf(Material.class));; 
    	
    	for(String materialName : config.getStringList("Creative.bannedItems")) {
    		try {
    			materialList.add(Material.valueOf(materialName.toUpperCase()));
    		} catch(IllegalArgumentException ex) {
    			thisPlugin.getLogger().log(Level.WARNING, materialName + " is not a valid material!");
    		}
    	}
    	
    	return materialList;
    }
    /*************************************************************************
     * Get config
     ************************************************************************/
    
    public Set<GameMode> getIgnoredGameMode() {
    	
    	return ignoredGameMode;
    }
    
    public boolean isNoDrop() {
    	
    	return noDrop;
    }

    public boolean isNoOpenChest() {
    	
    	return noOpenChest;
    }

    public boolean isNoBuildOutside() {
    	
    	return noBuildOutside;
    }

    public Set<Material> getBannedItems() {
    	
    	return bannedItems;
    }
}
