package world.maryt.rawinput;

import net.java.games.input.Controller;
import net.java.games.input.DirectAndRawInputEnvironmentPlugin;
import net.java.games.input.Mouse;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.MouseHelper;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientConnectedToServerEvent;
import org.apache.commons.lang3.ArrayUtils;
import world.maryt.rawinput.config.Config;

public class RawInputHandler {
    public static Controller[] controllers;
    public static Controller[] mouseControllers;

    public static Mouse mouse;
    public static int dx = 0;
    public static int dy = 0;

    public static boolean disabledManually = false;

    public static void init() {
        startInputThread();
    }
    @SuppressWarnings("all")
    public static void startInputThread() {
        Thread inputThread = new Thread(() -> {
            while (true) {
                if (mouse != null && Minecraft.getMinecraft().currentScreen == null) {
                    mouse.poll();
                    dx += (int) mouse.getX().getPollData();
                    dy += (int) mouse.getY().getPollData();
                } else if (mouse != null) {
                    mouse.poll();
                }
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    RawInput.LOGGER.error(e.getStackTrace());
                }
            }
        });
        inputThread.setName("inputThread");
        inputThread.start();
    }

    public static void getMouse(String reason) {
        Thread getMouseThread = new Thread(() -> {
            DirectAndRawInputEnvironmentPlugin directEnv = new DirectAndRawInputEnvironmentPlugin();
            controllers = directEnv.getControllers();

            mouseControllers = null;
            mouse = null;

            for (Controller i : controllers) {
                if (i.getType() == Controller.Type.MOUSE) {
                    mouseControllers = ArrayUtils.add(mouseControllers, i);
                }
            }

            while (mouse == null) {
                if (mouseControllers != null) {
                    for (Controller i : mouseControllers) {
                        i.poll();
                        float mouseX = ((Mouse) i).getX().getPollData();

                        if (mouseX > 0.1f || mouseX < -0.1f) {
                            mouse = ((Mouse) i);
                        }
                    }
                }
            }
        });
        getMouseThread.setName("getMouseThread");
        getMouseThread.start();
        RawInput.LOGGER.debug("getMouse is fired by reason: {}", reason);
    }

    public static void toggleRawInput(boolean isManual) {
        EntityPlayerSP player = Minecraft.getMinecraft().player;
        float saveYaw = player.rotationYaw;
        float savePitch = player.rotationPitch;

        if (Minecraft.getMinecraft().mouseHelper instanceof RawMouseHelper) {
            Minecraft.getMinecraft().mouseHelper = new MouseHelper();
            Minecraft.getMinecraft().mouseHelper.grabMouseCursor();
            if(isManual) {
                Minecraft.getMinecraft().player.sendMessage(new TextComponentString("Toggled OFF"));
                disabledManually = true;
            }
        } else {
            Minecraft.getMinecraft().mouseHelper = new RawMouseHelper();
            Minecraft.getMinecraft().mouseHelper.grabMouseCursor();
            if(isManual) {
                Minecraft.getMinecraft().player.sendMessage(new TextComponentString("Toggled ON"));
                disabledManually = false;
            }
        }
        // Restore player's yaw and pitch
        player.rotationYaw = saveYaw;
        player.rotationPitch = savePitch;
    }

    @SubscribeEvent
    public void onClientConnectedToServer(ClientConnectedToServerEvent event) {
        getMouse("onClientConnectedToServer");
    }

    @SubscribeEvent
    public void onGuiOpen(GuiOpenEvent event) {
        if (disabledManually) return;
        if (event.getGui() != null) {
            if (Config.guiBlacklist.contains(event.getGui().getClass().getName()) &&
                    Minecraft.getMinecraft().mouseHelper instanceof RawMouseHelper) {
                toggleRawInput(false);
            }
        } else {
            if (!(Minecraft.getMinecraft().mouseHelper instanceof RawMouseHelper)) toggleRawInput(false);
        }
    }
}


