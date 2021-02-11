/*
 * This file is part of the MacHack distribution (https://github.com/BleachDrinker420/bleachhack-1.14/).
 * Copyright (c) 2019 Bleach.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package mac.hack.command.commands;

import mac.hack.command.Command;
import mac.hack.utils.MacLogger;
import net.minecraft.world.GameMode;

public class CmdGamemode extends Command {

    @Override
    public String getAlias() {
        return "gm";
    }

    @Override
    public String getDescription() {
        return "Sets clientside gamemode.";
    }

    @Override
    public String getSyntax() {
        return "gm [0-3]";
    }

    @Override
    public void onCommand(String command, String[] args) throws Exception {
        int gm;

        try {
            gm = Integer.parseInt(args[0]);
        } catch (Exception e) {
            MacLogger.errorMessage("Unable to parse gamemode.");
            return;
        }

        if (gm == 0) {
            mc.player.setGameMode(GameMode.SURVIVAL);
            mc.interactionManager.setGameMode(GameMode.SURVIVAL);
            MacLogger.infoMessage("Set gamemode to survival.");
        } else if (gm == 1) {
            mc.player.setGameMode(GameMode.CREATIVE);
            mc.interactionManager.setGameMode(GameMode.CREATIVE);
            MacLogger.infoMessage("Set gamemode to creative.");
        } else if (gm == 2) {
            mc.player.setGameMode(GameMode.ADVENTURE);
            mc.interactionManager.setGameMode(GameMode.ADVENTURE);
            MacLogger.infoMessage("Set gamemode to adventure.");
        } else if (gm == 3) {
            mc.player.setGameMode(GameMode.SPECTATOR);
            mc.interactionManager.setGameMode(GameMode.SPECTATOR);
            MacLogger.infoMessage("Set gamemode to spectator.");
        } else {
            MacLogger.warningMessage("Unknown Gamemode Number.");
        }
    }

}
