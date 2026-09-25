package com.kalca.voidfulenhancements.command;

import com.kalca.voidfulenhancements.VoidfulEnhancements;
import com.kalca.voidfulenhancements.module.Module;
import org.lwjgl.input.Keyboard;

import java.util.List;

public class BindListCommand extends ChatCommand {

    public BindListCommand() {
        super("bindlist", "binds", "keylist");
    }

    @Override
    public String getUsage() {
        return "%bindlist";
    }

    @Override
    public void execute(String[] args) {
        if (VoidfulEnhancements.INSTANCE == null) return;
        List<Module> modules = VoidfulEnhancements.INSTANCE.moduleManager.getModules();
        boolean any = false;
        for (Module module : modules) {
            if (module.getKey() == -1) continue;
            any = true;
            CommandManager.sendMessage("\u00a7e" + module.getName() + "\u00a77 - \u00a7a[" + Keyboard.getKeyName(module.getKey()) + "]");
        }
        if (!any) CommandManager.sendMessage("\u00a77No modules are bound to a key.");
    }
}