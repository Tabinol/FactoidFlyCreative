/*
 FactoidFlyCreative: Minecraft Factoid plugin for fly and creative control
 Copyright (C) 2014  Michel Blanchet
 Rebuild from ResCreative and ResFly by Kolorafa

 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package me.tabinol.factoidflycreative;

import java.io.IOException;

import me.tabinol.factoidflycreative.config.FlyCreativeConfig;
import me.tabinol.factoidflycreative.listeners.PlayerListener;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.mcstats.MetricsLite;

public class FactoidFlyCreative extends JavaPlugin implements Listener {

    private PlayerListener playerListener;
    private static FactoidFlyCreative thisPlugin;
    private static FlyCreativeConfig config;


    @Override
    public void onEnable() {
            
        thisPlugin = this;

        // Config
        config = new FlyCreativeConfig();
        config.loadConfig();

        // Activate listeners
        playerListener = new PlayerListener();
        getServer().getPluginManager().registerEvents(playerListener, this);

        // Start Plugin Metrics
        try {
            MetricsLite metrics = new MetricsLite(this);
            metrics.start();
        } catch (IOException e) {
            // Failed to submit the stats :-(
        }
    }

    public static FactoidFlyCreative getThisPlugin() {

        return thisPlugin;
    }
    
    public static FlyCreativeConfig getConf() {

        return config;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {

        if (cmd.getName().equalsIgnoreCase("fcreload")) {

        	FactoidFlyCreative.getConf().reLoadConfig();
            sender.sendMessage("Configuration reloaded!");

            return true;

        } else {
            return false;
        }
    }
}
