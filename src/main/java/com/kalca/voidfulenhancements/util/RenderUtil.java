package com.kalca.voidfulenhancements.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.WorldRenderer;

public class RenderUtil {

    private static final Minecraft mc = Minecraft.getMinecraft();

    public static void drawOutlinedBox(WorldRenderer wr, double minX, double minY, double minZ, double maxX, double maxY, double maxZ, int r, int g, int b, int a) {
        line(wr, minX, minY, minZ, maxX, minY, minZ, r, g, b, a);
        line(wr, maxX, minY, minZ, maxX, minY, maxZ, r, g, b, a);
        line(wr, maxX, minY, maxZ, minX, minY, maxZ, r, g, b, a);
        line(wr, minX, minY, maxZ, minX, minY, minZ, r, g, b, a);
        line(wr, minX, maxY, minZ, maxX, maxY, minZ, r, g, b, a);
        line(wr, maxX, maxY, minZ, maxX, maxY, maxZ, r, g, b, a);
        line(wr, maxX, maxY, maxZ, minX, maxY, maxZ, r, g, b, a);
        line(wr, minX, maxY, maxZ, minX, maxY, minZ, r, g, b, a);
        line(wr, minX, minY, minZ, minX, maxY, minZ, r, g, b, a);
        line(wr, maxX, minY, minZ, maxX, maxY, minZ, r, g, b, a);
        line(wr, maxX, minY, maxZ, maxX, maxY, maxZ, r, g, b, a);
        line(wr, minX, minY, maxZ, minX, maxY, maxZ, r, g, b, a);
    }

    private static void line(WorldRenderer wr, double x1, double y1, double z1, double x2, double y2, double z2, int r, int g, int b, int a) {
        wr.pos(x1, y1, z1).color(r, g, b, a).endVertex();
        wr.pos(x2, y2, z2).color(r, g, b, a).endVertex();
    }

    public static void drawRect(float x, float y, float w, float h, int color) {
        if (w == 0 || h == 0) return;
        Gui.drawRect((int) x, (int) y, (int) (x + w), (int) (y + h), color);
    }

    public static void drawRoundedRect(float x, float y, float w, float h, float r, int color) {
        drawRect(x, y, w, h, color);
    }

    public static void drawString(String text, float x, float y, int color) {
        FontRenderer fr = mc.fontRendererObj;
        fr.drawStringWithShadow(text, (int) x, (int) y, color);
    }

    public static int getTextWidth(String text) {
        return mc.fontRendererObj.getStringWidth(text);
    }

    public static int getTextHeight() {
        return mc.fontRendererObj.FONT_HEIGHT;
    }
}