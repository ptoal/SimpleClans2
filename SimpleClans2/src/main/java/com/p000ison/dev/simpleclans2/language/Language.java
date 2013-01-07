package com.p000ison.dev.simpleclans2.language;

import com.p000ison.dev.simpleclans2.util.Logging;
import org.bukkit.ChatColor;

import java.io.File;
import java.nio.charset.Charset;
import java.text.MessageFormat;
import java.util.logging.Level;

public class Language {
    private static LanguageMap defaultBundle;
    private static LanguageMap bundle;
    private static final String DEFAULT_FILE_NAME = "lang.properties";

    public static void setInstance(File folder, Charset charset)
    {
        defaultBundle = new LanguageMap("/languages/" + DEFAULT_FILE_NAME, true, charset);
        defaultBundle.load();
        bundle = new LanguageMap(new File(folder, DEFAULT_FILE_NAME).getAbsolutePath(), false, charset);
        bundle.setDefault(defaultBundle);
        bundle.load();
        bundle.save();
    }

    public static String getTranslation(String key, Object... args)
    {
        String bundleOutput = bundle.getValue(key);

        if (bundleOutput == null) {
            Logging.debug(Level.WARNING, ChatColor.RED + "The language for the key %s was not found!", key);

            if (defaultBundle != null) {
                String defaultBundleOutput = defaultBundle.getValue(key);
                if (defaultBundleOutput == null) {
                    Logging.debug(Level.WARNING, ChatColor.RED + "The language for the key %s was not found in the default bundle!", key);
                    return "Error!";
                }

                if (args.length > 0) {
                    return MessageFormat.format(defaultBundleOutput, args);
                }
                return defaultBundleOutput;
            }

            return "Error!";
        }

        if (args.length > 0) {
            return MessageFormat.format(bundleOutput, args);
        }
        return bundleOutput;
    }

    public static void clear()
    {
        defaultBundle = null;
        bundle = null;
    }
}