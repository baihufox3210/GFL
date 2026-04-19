package com.GFL.lib.hardware.Encoder.Spark;

import com.GFL.lib.hardware.interfaces.GenericEncoder;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.spark.SparkMax;

public class SparkMaxRelativeEncoder implements GenericEncoder {
    private final RelativeEncoder encoder;

    public SparkMaxRelativeEncoder(SparkMax motor) {
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