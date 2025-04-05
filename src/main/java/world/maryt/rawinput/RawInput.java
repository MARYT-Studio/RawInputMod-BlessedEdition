package world.maryt.rawinput;

import world.maryt.rawinput.commands.RescanCommand;
import world.maryt.rawinput.commands.ToggleCommand;
import world.maryt.rawinput.config.Config;
import world.maryt.rawinput.keybinds.KeybindHandler;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = RawInput.MOD_ID, version = RawInput.VERSION, name = RawInput.MOD_NAME, acceptedMinecraftVersions = "[1.12.2]")
public class RawInput
{
    public static final String MOD_ID = Tags.MOD_ID;
	public static final String MOD_NAME = "Raw Input Mod";
    public static final String VERSION = Tags.VERSION;
	public static final Logger LOGGER = LogManager.getLogger(MOD_NAME);

	@SideOnly(Side.CLIENT)
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		Config.load(event.getSuggestedConfigurationFile());
		KeybindHandler.init();
	}

	@SideOnly(Side.CLIENT)
	@EventHandler
    public void init(FMLInitializationEvent event) {
		// If not enabled, this mod should register nothing.
		if (!Config.enableModFunction) {
			if (Config.disableReason.equals("forced")) LOGGER.info(MOD_NAME + "is disabled manually in config.");
			if (Config.disableReason.equals("notWindows")) LOGGER.info(MOD_NAME + "detected your operating system is not Windows and disabled itself. If you need this mod, set \"iNeedThisMod\" to true.");
			return;
		}

		LOGGER.info(MOD_NAME + "is enabled. Enjoy your smooth game control.");
		ClientCommandHandler.instance.registerCommand(new RescanCommand());
		ClientCommandHandler.instance.registerCommand(new ToggleCommand());
		Minecraft.getMinecraft().mouseHelper = new RawMouseHelper();
		MinecraftForge.EVENT_BUS.register(new KeybindHandler());
		MinecraftForge.EVENT_BUS.register(new RawInputHandler());

		RawInputHandler.init();
    }
}
