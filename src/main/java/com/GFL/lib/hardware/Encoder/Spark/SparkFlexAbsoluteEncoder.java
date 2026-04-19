package com.GFL.lib.hardware.Encoder.Spark;

import com.GFL.lib.hardware.interfaces.GenericEncoder;
import com.revrobotics.AbsoluteEncoder;
import com.revrobotics.spark.SparkFlex;

import edu.wpi.first.wpilibj.DriverStation;

public class SparkFlexAbsoluteEncoder implements GenericEncoder {
    private final AbsoluteEncoder encoder;

    public SparkFlexAbsoluteEncoder(SparkFlex motor) {
        this.encoder = motor.getAbsoluteEncoder();
    }

    @Override
    public double getPosition() {
        return encoder.getPosition();
    }

    @Override
    public double getVelocity() {
        return encoder.getVelocity();
    }
    
    @Override
    public void setPosition(double position) {
        DriverStation.reportError("Cannot set position of an absolute encoder", false);
    }
}
