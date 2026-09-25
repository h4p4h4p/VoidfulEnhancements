package com.kalca.voidfulenhancements.gui;

import com.kalca.voidfulenhancements.VoidfulEnhancements;
import com.kalca.voidfulenhancements.settings.SliderSetting;
import com.kalca.voidfulenhancements.util.RenderUtil;
import net.minecraft.util.MathHelper;
import org.lwjgl.input.Mouse;

public class SliderWidget extends Widget {

    private final SliderSetting setting;
    private float trackX;
    private float trackW;
    private boolean dragging;

    public SliderWidget(SliderSetting setting) {
        this.setting = setting;
    }

    public boolean isDragging() {
        return dragging;
    }

    public void setDragging(boolean dragging) {
        this.dragging = dragging;
        if (!dragging) {
            VoidfulEnhancements.scheduleSave();
        }
    }

    @Override
    public void draw(float mx, float my) {
        if (dragging) {
            if (!Mouse.isButtonDown(0)) {
                dragging = false;
                VoidfulEnhancements.scheduleSave();
            } else {
                update(mx);
            }
        }

        boolean hovered = contains(mx, my);
        computeTrack();

        float ty = y + height / 2f - 2f;

        RenderUtil.drawString(setting.getName(), x + 6, y + 4, hovered ? Theme.TEXT : Theme.TEXT_GRAY);

        double range = setting.getMax() - setting.getMin();
        double frac = (setting.getValue() - setting.getMin()) / range;
        float fill = (float) (trackW * frac);

        RenderUtil.drawRect(trackX, ty, trackW, 4, 0xFF565656);
        if (fill > 0.5f) {
            RenderUtil.drawRect(trackX, ty, Math.min(fill, trackW), 4, dragging || hovered ? 0xFF33D6FF : Theme.ACCENT);
        }

        float knobSize = 6f;
        float knobX = trackX + fill - knobSize / 2f;
        knobX = Math.max(trackX, Math.min(knobX, trackX + trackW - knobSize));
        RenderUtil.drawRect(knobX, ty - 2, knobSize, 8, Theme.KNOB);

        String value = formatValue();
        int vw = RenderUtil.getTextWidth(value);
        RenderUtil.drawString(value, x + width - 6 - vw, y + 4, Theme.ACCENT);
    }

    private String formatValue() {
        int decimals = setting.getStep() >= 1 ? 0 : (setting.getStep() >= 0.1 ? 1 : 2);
        return String.format("%." + decimals + "f", setting.getValue());
    }

    private void computeTrack() {
        float labelZone = RenderUtil.getTextWidth(setting.getName()) + 10;
        trackX = x + labelZone;
        float vw = RenderUtil.getTextWidth(formatValue());
        trackW = Math.max((x + width - 12 - vw) - trackX, 4);
    }

    @Override
    public float getPreferredWidth() {
        return Math.max(110, RenderUtil.getTextWidth(setting.getName()) + 10 + 48 + RenderUtil.getTextWidth(formatValue()) + 10);
    }

    private boolean onTrack(float mx, float my) {
        float ty = y + height / 2f - 2f;
        return mx >= trackX - 4 && mx <= trackX + trackW + 4 && my >= ty - 4 && my <= ty + 8;
    }

    @Override
    public boolean onClick(float mx, float my, int button) {
        if (button != 0) return false;
        computeTrack();
        if (!onTrack(mx, my)) return false;
        dragging = true;
        update(mx);
        return true;
    }

    private void update(float mx) {
        double frac = MathHelper.clamp_double((mx - trackX) / Math.max(trackW, 1f), 0, 1);
        double range = setting.getMax() - setting.getMin();
        double raw = setting.getMin() + frac * range;
        double snapped = Math.round(raw / setting.getStep()) * setting.getStep();
        setting.setValue(snapped);
    }
}