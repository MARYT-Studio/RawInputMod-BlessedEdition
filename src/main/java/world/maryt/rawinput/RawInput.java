package world.maryt.rawinput;

import world.maryt.rawinput.commands.RescanCommand;
import world.maryt.rawinput.commands.ToggleCommand;
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

@Mod(modid = RawInput.MODID, version = RawInput.VERSION, name = RawInput.NAME, acceptedMinecraftVersions = "[1.12.2]")
public class RawInput
{
    public static final String MODID = "rawinput";
	public static final String NAME = "Raw Mouse Input";
    public static final String VERSION = "1.6.1";
	public static final Logger LOGGER = LogManager.getLogger(NAME);

	public static final boolean DEBUG = false;


	@SideOnly(Side.CLIENT)
	@EventHandler
    public void init(FMLInitializationEvent event) {
		ClientCommandHandler.instance.registerCommand(new RescanCommand());
		ClientCommandHandler.instance.registerCommand(new ToggleCommand());
		Minecraft.getMinecraft().mouseHelper = new RawMouseHelper();
		MinecraftForge.EVENT_BUS.register(new KeybindHandler());
		MinecraftForge.EVENT_BUS.register(new RawInputHandler());

		RawInputHandler.init();
    }

	@SideOnly(Side.CLIENT)
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		KeybindHandler.init();
	}
}
