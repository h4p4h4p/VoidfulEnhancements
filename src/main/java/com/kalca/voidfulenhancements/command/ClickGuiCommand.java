package com.kalca.voidfulenhancements.command;

import com.kalca.voidfulenhancements.VoidfulEnhancements;
import com.kalca.voidfulenhancements.gui.ClickGUI;
import net.minecraft.client.Minecraft;

public class ClickGuiCommand extends ChatCommand {

    public ClickGuiCommand() {
        super("clickgui", "gui", "config");
    }

    @Override
    public String getUsage() {
        return "%clickgui";
    }

    @Override
    public void execute(String[] args) {
        Minecraft mc = Minecraft.getMinecraft();
        mc.addScheduledTask(() -> {
            if (mc.currentScreen instanceof ClickGUI) return;
            if (VoidfulEnhancements.INSTANCE == null || VoidfulEnhancements.INSTANCE.clickGui == null) return;
            mc.displayGuiScreen(VoidfulEnhancements.INSTANCE.clickGui);
        });
    }
}