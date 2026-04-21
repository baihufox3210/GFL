package com.GFL.lib.hardware.interfaces;

import edu.wpi.first.math.geometry.Rotation2d;

public interface GenericGyro {
    Rotation2d getRotation2d();
    
    double getAngle();
    double getYaw();

    void reset();
}
