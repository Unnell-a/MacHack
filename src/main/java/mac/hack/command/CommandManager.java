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
package mac.hack.command;

import mac.hack.command.commands.*;
import mac.hack.utils.MacLogger;

import java.util.Arrays;
import java.util.List;

public class CommandManager {

    private static List<Command> commands = Arrays.asList(
            new CmdBind(),
            new CmdCI(),
            new CmdCleanChat(),
            new CmdCustomChat(),
            new CmdDupe(),
            new CmdEnchant(),
            new CmdEntityStats(),
            new CmdFriends(),
            new CmdGamemode(),
            new CmdGive(),
            new CmdGuiReset(),
            new CmdHelp(),
            new CmdNBT(),
            new CmdNotebot(),
            new CmdNuker(),
            new CmdPeek(),
            new CmdPrefix(),
            new CmdRename(),
            new CmdRpc(),
            new CmdSetting(),
            new CmdSkull(),
            new CmdToggle(),
            new CmdSearch(),
            new CmdXray());

    public static List<Command> getCommands() {
        return commands;
    }

    public static void callCommand(String input) {
        String[] split = input.split(" ", -1);
        System.out.println(Arrays.asList(split));
        String command = split[0];
        String args = input.substring(command.length()).trim();
        for (Command c : getCommands()) {
            if (c.getAlias().equalsIgnoreCase(command)) {
                try {
                    c.onCommand(command, args.split(" "));
                } catch (Exception e) {
                    e.printStackTrace();
                    MacLogger.errorMessage("Invalid Syntax!");
                    MacLogger.infoMessage(c.getSyntax());
                }
                return;
            }
        }
        MacLogger.errorMessage("Command Not Found, Maybe Try " + Command.PREFIX + "help");
    }
}
