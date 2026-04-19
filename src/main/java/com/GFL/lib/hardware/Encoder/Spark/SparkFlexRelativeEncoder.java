package com.GFL.lib.hardware.Encoder.Spark;

import com.GFL.lib.hardware.interfaces.GenericEncoder;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.spark.SparkFlex;

public class SparkFlexRelativeEncoder implements GenericEncoder {
    private final RelativeEncoder encoder;

    public SparkFlexRelativeEncoder(SparkFlex motor) {
        this.encoder = motor.getEncoder();
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
        encoder.setPosition(position);
    }
}