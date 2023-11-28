package world.maryt.rawinput.commands;

import world.maryt.rawinput.RawInputHandler;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

public class ToggleCommand extends CommandBase {
    @Override
    public String getName() {
        return "rawinput";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "Toggles Raw Input (/rawinput)";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        RawInputHandler.toggleRawInput();
    }
    @Override
    public int getRequiredPermissionLevel() {
        return -1;
    }
}
