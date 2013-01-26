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
 *     Last modified: 03.11.12 17:29
 */

package com.p000ison.dev.simpleclans2.updater;

import com.p000ison.dev.simpleclans2.util.DateHelper;
import com.p000ison.dev.simpleclans2.util.Logging;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Represents a AutoUpdater
 */
public class AutoUpdater {
    private Build toUpdate = null;
    private static String[] addons = {"SimpleClans2", "SimpleClansChat"};

    public AutoUpdater(Plugin plugin, String job, UpdateType type, boolean longReport) {
        String version = plugin.getDescription().getVersion();

        if (version.equals("unknown-version")) {
            Logging.debug("No maven, heh?");
            return;
        } else if (version.contains("UNKNOWN")) {
            Logging.debug("No jenkins build, heh?");
            return;
        }

        int versionValue = parseVersion(version);

        if (versionValue == -1) {
            Logging.debug("The version string is malformatted: %s", version);
            return;
        }

        try {
            Build update = getBuild(type, job, getAddonArtifacts());

            update.fetchInformation();

            if (versionValue < update.getBuildNumber()) {
                Logging.debug(getBuildInfo(update, longReport));
                toUpdate = update;
            }
        } catch (UnknownHostException e) {
            Logging.debug(e, true, "The jenkins is down! Please contact the developers!");
        } catch (FileNotFoundException e) {
            Logging.debug("The file was not found on the jenkins server! Please contact the developers");
        } catch (IOException e) {
            Logging.debug(e, true, "Failed at fetching the Update information! Maybe something is down. Please contact the developers");
        }
    }

    private Artifact[] getAddonArtifacts() {
        List<Artifact> artifacts = new ArrayList<Artifact>();

        for (String addon : addons) {
            String path = getFileName(addon);
            if (path != null) {
                artifacts.add(new Artifact(addon, path));
            }
        }

        return artifacts.toArray(new Artifact[artifacts.size()]);
    }


    public static String getBuildInfo(Build build, boolean longReport) {
        if (build == null) {
            return null;
        }

        StringBuilder updateInfo = new StringBuilder();

        updateInfo.append("------------------------------------------------------------------------------------------------------\n");
        updateInfo.append("There is a update for your SimpleClans version!\n");
        if (longReport) {
            updateInfo.append("Build:  ").append(build.getBuildNumber()).append('\n');
            updateInfo.append("Type:  ").append(build.getUpdateType() == UpdateType.LATEST ? "Unofficial\n" : "Official\n");
            updateInfo.append("Release date:  ").append(new Date(build.getStarted())).append('\n');
            updateInfo.append("Comment:  ").append(build.getComment()).append(" (").append(build.getCommitURL()).append(")\n");
        }
        updateInfo.append("------------------------------------------------------------------------------------------------------\n");
        return updateInfo.toString();
    }

    public boolean isUpdate() {
        return toUpdate != null;
    }

    public boolean update() {
        if (toUpdate == null) {
            return false;
        }

        try {
            File updateDirectory = Bukkit.getUpdateFolderFile();
            File pluginDirectory = updateDirectory.getParentFile();

            if (pluginDirectory.listFiles() == null || !pluginDirectory.isDirectory()) {
                return false;
            }

            if (!toUpdate.saveArtifactsToDirectory(updateDirectory)) {
                return false;
            }
        } catch (IOException e) {
            Logging.debug(e, true);
            return false;
        }
        return true;
    }

    public static String getFileName(Class<?> clazz) {
        String path = clazz.getProtectionDomain().getCodeSource().getLocation().getFile();
        return path.substring(path.lastIndexOf('/') + 1);
    }

    public static String getFileName(Plugin plugin) {
        if (plugin == null) {
            return null;
        }
        return getFileName(plugin.getClass());
    }

    public static String getFileName(String plugin) {
        return getFileName(Bukkit.getPluginManager().getPlugin(plugin));
    }

    private static int parseVersion(String version) {
        char[] versionArray = version.toCharArray();

        int versionNumber = 0;
        int run = 1;

        for (int i = versionArray.length - 1; i >= 0; i--) {
            char current = versionArray[i];

            switch (current) {
                case '-':
                case 'b':
                    break;
                case '.':
                    continue;
            }

            int number = DateHelper.getDigit(current);

            if (number == -1) {
                if (versionNumber == 0) {
                    return -1;
                }
                break;
            }

            versionNumber += number * run;
            run *= 10;
        }

        return versionNumber;
    }

    public static Build getBuild(UpdateType type, String job, Artifact... artifacts) {
        return new Build(job, type, artifacts);
    }
}
