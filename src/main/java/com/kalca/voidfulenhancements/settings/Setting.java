package com.kalca.voidfulenhancements.settings;

public abstract class Setting {

    private final String name;

    protected Setting(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public abstract String toConfigString();

    public abstract void fromConfigString(String value);
}