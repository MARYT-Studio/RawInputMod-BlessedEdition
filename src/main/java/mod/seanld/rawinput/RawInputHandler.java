package mod.seanld.rawinput;

import net.java.games.input.Controller;
import net.java.games.input.DirectAndRawInputEnvironmentPlugin;
import net.java.games.input.Mouse;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.MouseHelper;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientConnectedToServerEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientDisconnectionFromServerEvent;
import org.apache.commons.lang3.ArrayUtils;



import static mod.seanld.rawinput.RawInput.DEBUG;

public class RawInputHandler {
    public static Controller[] controllers;
    public static Controller[] mouseControllers;

    public static Mouse mouse;
    public static int dx = 0;
    public static int dy = 0;

    private int worldJoinTimer;

    public int getTimer() {
        return worldJoinTimer;
    }

    public void setTimer(int timer) {
        this.worldJoinTimer = timer;
    }

    public void ticTac() {
        this.worldJoinTimer -= 1;
    }


    private boolean shouldGetMouse = false;

    public boolean getShouldGetMouse() {
        return shouldGetMouse;
    }

    public void setShouldGetMouse(boolean shouldGetMouse) {
        this.shouldGetMouse = shouldGetMouse;
    }

    public static void init() {

        startInputThread();

    }
    @SuppressWarnings("")
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
        if (DEBUG) { RawInput.LOGGER.debug(String.format("Now getMouse is fired for reason %s Is the thread started to work: ", reason)); }
    }

    public static void toggleRawInput() {
        EntityPlayerSP player = Minecraft.getMinecraft().player;
        float saveYaw = player.rotationYaw;
        float savePitch = player.rotationPitch;

        if (Minecraft.getMinecraft().mouseHelper instanceof RawMouseHelper) {
            Minecraft.getMinecraft().mouseHelper = new MouseHelper();
            Minecraft.getMinecraft().mouseHelper.grabMouseCursor();
            Minecraft.getMinecraft().player.sendMessage(new TextComponentString("Toggled OFF"));
        } else {
            Minecraft.getMinecraft().mouseHelper = new RawMouseHelper();
            Minecraft.getMinecraft().mouseHelper.grabMouseCursor();
            Minecraft.getMinecraft().player.sendMessage(new TextComponentString("Toggled ON"));
        }
        player.rotationYaw = saveYaw;
        player.rotationPitch = savePitch;
    }
    @SubscribeEvent
    public void timer(ClientTickEvent event) {
        if (getTimer() >= 0) {
            ticTac();
        }
        if (getShouldGetMouse()) {
            getMouse("Client Tick Event");
            setShouldGetMouse(false);
        }
    }
    @SubscribeEvent
    public void onClientConnectedToServer(ClientConnectedToServerEvent event) {
        if (DEBUG) { RawInput.LOGGER.debug("Player Connected to Server"); }
        setTimer(3);
        setShouldGetMouse(true);
    }
    @SubscribeEvent
    public void onClientDisconnectionFromServer(ClientDisconnectionFromServerEvent event) {
        if (DEBUG) { RawInput.LOGGER.debug("Player Disconnected from Server. Is getMouse thread shutdown: " + !shouldGetMouse); }
        setShouldGetMouse(false);
    }


}


