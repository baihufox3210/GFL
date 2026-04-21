package com.GFL.lib.hardware.Gyro;

import com.GFL.lib.hardware.config.GyroConfig;
import com.GFL.lib.hardware.interfaces.GenericGyro;
import com.studica.frc.AHRS;
import com.studica.frc.AHRS.NavXComType;

import edu.wpi.first.math.geometry.Rotation2d;

public class NavX implements GenericGyro {
    private final AHRS gyro;

    private final boolean inverted;

    public NavX(int id, GyroConfig gyroConfig) {
        this.gyro = new AHRS(NavXComType.kMXP_SPI);
        this.inverted = gyroConfig.inverted;
    }

    @Override
    public Rotation2d getRotation2d() {
        return inverted ? gyro.getRotation2d().unaryMinus() : gyro.getRotation2d();
    }

    @Override
    public double getAngle() {
        return gyro.getAngle();
    }

    @Override
    public double getYaw() {
        return gyro.getYaw();
    }

    @Override
    public void reset() {
        gyro.reset();
    }
}