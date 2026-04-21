package com.GFL.lib.hardware.config;

public class GyroConfig {
    public boolean inverted = false;

    public GyroConfig setInverted(boolean inverted) {
        this.inverted = inverted;
        return this;
    }
}
