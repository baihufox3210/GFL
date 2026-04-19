package com.GFL.lib.hardware.Encoder.CANCoder;

import com.GFL.lib.hardware.interfaces.GenericEncoder;
import com.ctre.phoenix6.hardware.CANcoder;

public class CANCoderEncoder implements GenericEncoder {
    private final CANcoder encoder;

    public CANCoderEncoder(CANcoder encoder) {
        this.encoder = encoder;
    }

    @Override
    public double getPosition() {
        return encoder.getPosition().getValueAsDouble();
    }

    @Override
    public double getVelocity() {
        return encoder.getVelocity().getValueAsDouble();
    }

    @Override
    public void setPosition(double position) {
        encoder.setPosition(position);
    }
}