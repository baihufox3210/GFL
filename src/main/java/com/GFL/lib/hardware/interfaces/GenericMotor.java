package com.GFL.lib.hardware.interfaces;

public interface GenericMotor {
    void configure();

    void set(double present);
    
    void setPosition(double position);
    void setVelocity(double velocity);

    void setMotionMagicPosition(double position);
    void setMotionMagicVelocity(double velocity);

    void stop();

    GenericEncoder getEncoder();
}