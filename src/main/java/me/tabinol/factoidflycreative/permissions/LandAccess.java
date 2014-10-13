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
package me.tabinol.factoidflycreative.permissions;

import me.tabinol.factoidapi.FactoidAPI;
import me.tabinol.factoidapi.lands.IDummyLand;
import me.tabinol.factoidapi.parameters.IPermissionType;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class LandAccess {

    private final IDummyLand dummyLand;

    public LandAccess(Location location) {

        this.dummyLand = FactoidAPI.iLands().getLandOrOutsideArea(location);
    }

    public LandAccess(IDummyLand dummyLand) {

        this.dummyLand = dummyLand;
    }

    public boolean isPermissionTrue(IPermissionType permissionType, Player player) {

        Boolean result = dummyLand.checkPermissionAndInherit(player, permissionType);
        
        if (result != null && result != permissionType.getDefaultValue()) {
            return true;
        }

        return false;
    }
}
