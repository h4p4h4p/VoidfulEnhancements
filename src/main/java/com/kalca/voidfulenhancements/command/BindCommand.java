package com.kalca.voidfulenhancements.command;

import com.kalca.voidfulenhancements.VoidfulEnhancements;
import com.kalca.voidfulenhancements.module.Module;
import org.lwjgl.input.Keyboard;

public class BindCommand extends ChatCommand {

    public BindCommand() {
        super("bind", "key");
    }

    @Override
    public String getUsage() {
        return "%bind <module> <key> | %bind <module> none";
    }

    @Override
    public void execute(String[] args) {
        if (args.length != 2) {
            CommandManager.sendMessage("\u00a7cUsage: " + getUsage());
            return;
        }
        if (VoidfulEnhancements.INSTANCE == null) return;
        Module module = VoidfulEnhancements.INSTANCE.moduleManager.getModule(args[0]);
        if (module == null) {
            CommandManager.sendMessage("\u00a7cModule '\u00a7f" + args[0] + "\u00a7c' not found. Type \u00a7f%help");
            return;
        }

        String keyName = args[1].toLowerCase();
        if (keyName.equals("none") || keyName.equals("unbind") || keyName.equals("delete")) {
            module.setKey(-1);
            CommandManager.sendMessage("\u00a7a" + module.getName() + "\u00a77 unbound");
            VoidfulEnhancements.scheduleSave();
            return;
        }

        int code = Keyboard.getKeyIndex(keyName);
        if (code == Keyboard.KEY_NONE) {
            try {
                code = Integer.parseInt(keyName);
            } catch (NumberFormatException ignored) {
                code = Keyboard.KEY_NONE;
            }
        }
        if (code == Keyboard.KEY_NONE) {
            CommandManager.sendMessage("\u00a7cInvalid key: " + args[1]);
            return;
        }
        module.setKey(code);
        CommandManager.sendMessage("\u00a7a" + module.getName() + "\u00a77 bound to \u00a7a[" + Keyboard.getKeyName(code) + "]");
        VoidfulEnhancements.scheduleSave();
    }
}