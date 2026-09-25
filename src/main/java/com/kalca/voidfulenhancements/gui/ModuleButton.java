package com.kalca.voidfulenhancements.gui;

import com.kalca.voidfulenhancements.module.Module;
import com.kalca.voidfulenhancements.settings.BooleanSetting;
import com.kalca.voidfulenhancements.settings.ColorSetting;
import com.kalca.voidfulenhancements.settings.ModeSetting;
import com.kalca.voidfulenhancements.settings.Setting;
import com.kalca.voidfulenhancements.settings.SliderSetting;
import com.kalca.voidfulenhancements.util.RenderUtil;

import java.util.ArrayList;
import java.util.List;

public class ModuleButton {

    private static final float ROW_HEIGHT = 14f;
    private static final float EXPAND_ZONE = 12f;

    private final Module module;
    private final Panel panel;
    private final List<Widget> widgets = new ArrayList<>();
    private final List<Setting> builtSettings = new ArrayList<>();
    private BindWidget bindWidget;
    private boolean expanded;

    private float x;
    private float y;
    private float width;

    public ModuleButton(Module module, Panel panel) {
        this.module = module;
        this.panel = panel;
        refreshWidgets();
    }

    public void setPosition(float x, float y, float width) {
        this.x = x;
        this.y = y;
        this.width = width;
    }

    public float getHeight() {
        float h = ROW_HEIGHT;
        if (expanded) h += widgetsHeight();
        return h;
    }

    private float widgetsHeight() {
        float h = 0;
        for (Widget widget : widgets) h += widget.getHeight();
        return h;
    }

    private void refreshWidgets() {
        List<Setting> visible = new ArrayList<>();
        for (Setting setting : module.getSettings()) {
            if (module.isSettingVisible(setting)) visible.add(setting);
        }
        boolean same = builtSettings.size() == visible.size();
        if (same) {
            for (int i = 0; i < visible.size(); i++) {
                if (builtSettings.get(i) != visible.get(i)) {
                    same = false;
                    break;
                }
            }
        }
        if (same) return;

        widgets.clear();
        builtSettings.clear();
        for (Setting setting : visible) {
            if (setting instanceof SliderSetting) {
                widgets.add(new SliderWidget((SliderSetting) setting));
            } else if (setting instanceof BooleanSetting) {
                widgets.add(new BooleanWidget((BooleanSetting) setting));
            } else if (setting instanceof ModeSetting) {
                widgets.add(new ModeWidget((ModeSetting) setting));
            } else if (setting instanceof ColorSetting) {
                widgets.add(new ColorWidget((ColorSetting) setting));
            }
            builtSettings.add(setting);
        }
        if (module.canBind()) {
            bindWidget = new BindWidget(module);
            widgets.add(bindWidget);
        }
    }

    public boolean isExpanded() {
        return expanded;
    }

    public BindWidget getBindWidget() {
        return bindWidget;
    }

    public List<Widget> getWidgets() {
        return widgets;
    }

    public Module getModule() {
        return module;
    }

    public void draw(float mx, float my) {
        refreshWidgets();
        boolean hovered = mx >= x && mx <= x + width && my >= y && my <= y + ROW_HEIGHT;
        boolean hoverExpand = hovered && mx > x + width - EXPAND_ZONE;

        if (module.isEnabled()) {
            RenderUtil.drawRect(x, y, 2, ROW_HEIGHT, Theme.ACCENT);
        }

        if (hovered) {
            RenderUtil.drawRect(x + 2, y, width - 2, ROW_HEIGHT, Theme.HOVER);
        }

        int textColor = module.isEnabled() ? Theme.TEXT : Theme.TEXT_GRAY;
        RenderUtil.drawString(module.getName(), x + 7, y + (ROW_HEIGHT - RenderUtil.getTextHeight()) / 2f, textColor);

        String bind = bindWidget == null ? "" : bindWidget.keyName();
        int bindW = RenderUtil.getTextWidth(bind);
        int bindColor = module.isEnabled() ? Theme.TEXT_DIM : Theme.TEXT_GRAY;
        if (bindWidget != null) {
            RenderUtil.drawString(bind, x + width - EXPAND_ZONE - bindW - 8, y + (ROW_HEIGHT - RenderUtil.getTextHeight()) / 2f, bindColor);
        }

        RenderUtil.drawString("...", x + width - EXPAND_ZONE + 3, y + (ROW_HEIGHT - RenderUtil.getTextHeight()) / 2f, hoverExpand ? Theme.ACCENT : Theme.TEXT_GRAY);

        if (expanded) {
            float wh = widgetsHeight();
            RenderUtil.drawRect(x, y + ROW_HEIGHT, width, wh, Theme.BODY_BG);
            float wy = y + ROW_HEIGHT;
            for (Widget widget : widgets) {
                widget.setPosition(x + 4, wy, width - 8, widget.getHeight());
                widget.draw(mx, my);
                wy += widget.getHeight();
            }
            RenderUtil.drawRect(x + 4, y + ROW_HEIGHT + wh - 1, width - 8, 1, Theme.SEPARATOR);
        } else {
            RenderUtil.drawRect(x + 4, y + ROW_HEIGHT - 1, width - 8, 1, Theme.SEPARATOR);
        }
    }

    public boolean onClick(float mx, float my, int button) {
        if (my > y + ROW_HEIGHT) {
            if (!expanded) return false;
            for (Widget widget : widgets) {
                if (widget.contains(mx, my)) {
                    return widget.onClick(mx, my, button);
                }
            }
            return false;
        }

        if (button == 1) {
            expanded = !expanded;
            return true;
        }

        if (mx > x + width - EXPAND_ZONE) {
            expanded = !expanded;
            return true;
        }

        module.toggle();
        return true;
    }
}