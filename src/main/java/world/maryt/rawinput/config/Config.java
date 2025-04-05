package world.maryt.rawinput.config;

import com.google.common.collect.Sets;
import net.minecraftforge.common.MinecraftForge;

import net.minecraftforge.common.config.Configuration;

import java.io.File;
import java.util.Set;

public class Config {
    private static Configuration latestConfig;

    public static Set<String> guiBlacklist = Sets.newHashSet();
    public static boolean enableModFunction;
    public static String disableReason;

    private Config() {}

    @SuppressWarnings("InstantiationOfUtilityClass")
    public static void load(File file) {
        latestConfig = new Configuration(file);
        latestConfig.load();
        loadConfig();
        latestConfig.save();
        MinecraftForge.EVENT_BUS.register(new Config());
    }

    public static void loadConfig() {
        // GUI Blacklist
        String[] list = latestConfig.getStringList("guiBlackList",
                Configuration.CATEGORY_CLIENT,
                new String[]{"hellfirepvp.astralsorcery.client.gui.GuiObservatory"},
                "All GUIs whose class names are in this list will disable RawInput.");
        if (list.length > 0) {guiBlacklist = Sets.newHashSet(list);}

        // Operating System Check
        String enable = latestConfig.getString("iNeedThisMod",
                Configuration.CATEGORY_CLIENT,
                "auto",
                "Here are three options:\n" +
                        "\"true\": force entire functionality of mod to be enabled.\n" +
                        "\"false\": force entire functionality of mod to be disabled.\n" +
                        "\"auto\": let the mod detect if your operating system is Windows, and disable the mod if not.\n" +
                        "Anything other input than the three options above will be seen as default value \"auto\".\n" +
                        "Only change this when you know what you are doing.");
        switch (enable) {
            case "true": {
                enableModFunction = true;
                disableReason = "";
                break;
            }
            case "false": {
                enableModFunction = false;
                disableReason = "forced";
                break;
            }
            default: {
                enableModFunction = System.getProperty("os.name").toLowerCase().contains("windows");
                disableReason = "notWindows";
                break;
            }
        }
    }
}
