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
 *     Last modified: 29.01.13 20:55
 */

package com.p000ison.dev.simpleclans2.api.command;

import org.bukkit.permissions.Permissible;

import java.util.Arrays;

public abstract class GenericCommand implements Command {
    private String name;
    private int minArgs, maxArgs;
    private String[] identifiers;
    private String[] usage;
    private String[] permissions;
    private Type type = Type.CLAN;

    protected GenericCommand(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setUsages(String... text) {
        this.usage = text;
    }

    @Override
    public void setIdentifiers(String... identifiers) {
        this.identifiers = identifiers;
    }

    @Override
    public boolean isIdentifier(String cmd) {
        if (identifiers == null) {
            throw new IllegalArgumentException("The identifiers are null!");
        }
        if (cmd == null) {
            return false;
        }

        for (String identifier : identifiers) {
            if (cmd.equalsIgnoreCase(identifier)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String[] getUsages() {
        return usage.clone();
    }

    @Override
    public int getMaxArguments() {
        return maxArgs;
    }

    @Override
    public int getMinArguments() {
        return minArgs;
    }

    @Override
    public void setArgumentRange(int min, int max) {
        minArgs = min;
        maxArgs = max;
    }

    @Override
    public boolean hasPermission(Permissible sender) {
        if (permissions == null) {
            return true;
        }

        for (String perm : permissions) {
            if (sender.hasPermission(perm)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public void setPermission(String... permissions) {
        this.permissions = permissions;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof GenericCommand && name.equals(((GenericCommand) obj).getName());
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public String toString() {
        return "GenericCommand{" +
                "name='" + name + '\'' +
                ", minArgs=" + minArgs +
                ", permission='" + Arrays.asList(permissions) + '\'' +
                ", identifiers=" + (identifiers == null ? null : Arrays.asList(identifiers)) +
                ", maxArgs=" + maxArgs +
                '}';
    }

    @Override
    public Type getType() {
        return type;
    }

    @Override
    public void setType(Type type) {
        this.type = type;
    }
}
