package world.maryt.rawinput.config;

import com.google.common.collect.Sets;
import net.minecraftforge.common.MinecraftForge;

import net.minecraftforge.common.config.Configuration;

import java.io.File;
import java.util.Set;

public class Config {
    private static Configuration latestConfig;

    public static Set<String> guiBlacklist = Sets.newHashSet();

    private Config() {}

    public static void load(File file) {
        latestConfig = new Configuration(file);
        latestConfig.load();
        loadConfig();
        latestConfig.save();
        MinecraftForge.EVENT_BUS.register(new Config());
    }

    public static void loadConfig() {
        String[] list = latestConfig.getStringList("guiBlackList", Configuration.CATEGORY_CLIENT, new String[]{"hellfirepvp.astralsorcery.client.gui.GuiObservatory"}, "All GUIs whose class names are in this list will disable RawInput.");
        if (list.length > 0) {guiBlacklist = Sets.newHashSet(list);}
    }
}
