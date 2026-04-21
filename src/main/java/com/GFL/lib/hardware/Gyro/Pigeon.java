package com.GFL.lib.hardware.Gyro;

import com.GFL.lib.hardware.config.GyroConfig;
import com.GFL.lib.hardware.interfaces.GenericGyro;
import com.ctre.phoenix6.hardware.Pigeon2;

import edu.wpi.first.math.geometry.Rotation2d;

public class Pigeon implements GenericGyro {
    private final Pigeon2 gyro;

    private final boolean inverted;

    public Pigeon(int id, GyroConfig gyroConfig) {
        this.gyro = new Pigeon2(id);
        this.inverted = gyroConfig.inverted;
    }

    @Override
    public Rotation2d getRotation2d() {
        return inverted ? gyro.getRotation2d().unaryMinus() : gyro.getRotation2d();
    }

    @Override
    public double getAngle() {
        return gyro.getYaw().getValueAsDouble();
    }

    @Override
    public double getYaw() {
        return gyro.getYaw().getValueAsDouble();
    }

    @Override
    public void reset() {
        gyro.setYaw(0);
    }
}