/*
 * This file is part of SimpleClans2 (2012).
 *
 *     SimpleClans2 is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     SimpleClans2 is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with SimpleClans2.  If not, see <http://www.gnu.org/licenses/>.
 *
 *     Last modified: 07.01.13 14:39
 */

package com.p000ison.dev.simpleclans2.chat.listeners;


import com.p000ison.dev.simpleclans2.api.clanplayer.ClanPlayer;
import com.p000ison.dev.simpleclans2.chat.SimpleClansChat;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

@SuppressWarnings("unused")
public class SCCPlayerListener implements Listener {

    private SimpleClansChat plugin;

    public SCCPlayerListener(SimpleClansChat plugin) {
        this.plugin = plugin;
    }

    public void onPlayerChat(AsyncPlayerChatEvent event) {
        String format;
        Player player = event.getPlayer();
        ClanPlayer clanPlayer = plugin.getClanPlayerManager().getClanPlayer(player);
        int initialRetrieversSize = event.getRecipients().size();

        format = plugin.removeRetrievers(event, clanPlayer, player);

        if (format == null) {
            if (this.plugin.getSettingsManager().isCompatibilityMode()) {
                format = plugin.formatCompatibility(event.getFormat(), player.getName());
                if (plugin.getSettingsManager().isCancellingMode() && event.getRecipients().size() != initialRetrieversSize) {
                    String finalOutput = String.format(format, player.getDisplayName(), event.getMessage());
                    for (Player recipient : event.getRecipients()) {
                        recipient.sendMessage(finalOutput);
                    }
                    event.setCancelled(true);
                }
            } else if (this.plugin.getSettingsManager().isCompleteMode()) {
                format = plugin.formatComplete(plugin.getSettingsManager().getCompleteModeFormat(), player, clanPlayer);
                //colorize
                event.setMessage(SimpleClansChat.parseColors(player, event.getMessage()));
            }
        }

        if (format == null) {
            return;
        }

        event.setFormat(format);
    }
}