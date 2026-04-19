package com.GFL.lib.hardware.Encoder.Talon;

import com.GFL.lib.hardware.interfaces.GenericEncoder;
import com.ctre.phoenix6.hardware.TalonFX;

public class TalonFXRelativeEncoder implements GenericEncoder {
    private final TalonFX motor;

    public TalonFXRelativeEncoder(TalonFX motor) {
        this.motor = motor;
    }

    @Override
    public double getPosition() {
        return motor.getPosition().getValueAsDouble();
    }

    @Override
    public double getVelocity() {
        return motor.getVelocity().getValueAsDouble();
    }

    @Override
    public void setPosition(double position) {
        motor.setPosition(position);
    }
}
