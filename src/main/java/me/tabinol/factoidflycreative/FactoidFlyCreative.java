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

import me.tabinol.factoidflycreative.listeners.PlayerListener;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class FactoidFlyCreative extends JavaPlugin implements Listener {

    private PlayerListener playerListener;
    private static FactoidFlyCreative thisPlugin;

    @Override
    public void onEnable() {
            
        thisPlugin = this;

        // Load configuration
        this.saveDefaultConfig();
        loadFNCConfig();

        // Activate listeners
        playerListener = new PlayerListener();
        getServer().getPluginManager().registerEvents(playerListener, this);
    }

    private void loadFNCConfig() {

        this.reloadConfig();
    }

    public static FactoidFlyCreative getThisPlugin() {

        return thisPlugin;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {

        if (cmd.getName().equalsIgnoreCase("fcreload")) {

            loadFNCConfig();
            sender.sendMessage("Configuration reloaded!");

            return true;

        } else {
            return false;
        }
    }
}
