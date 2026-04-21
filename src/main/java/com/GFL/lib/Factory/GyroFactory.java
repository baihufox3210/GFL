package com.GFL.lib.Factory;

import com.GFL.lib.hardware.Gyro.Pigeon;
import com.GFL.lib.hardware.config.GyroConfig;
import com.GFL.lib.hardware.interfaces.GenericGyro;

public class GyroFactory {
    @FunctionalInterface
    public interface GyroConstructor {
        GenericGyro create(int id, GyroConfig gyroConfig);
    }

    public enum GyroModel {
        Pigeon(Pigeon::new);

        public final GyroConstructor constructor;

        GyroModel(GyroConstructor constructor) {
            this.constructor = constructor;
        }
    }

    public static GenericGyro createGyro(int id, GyroModel model, GyroConfig gyroConfig) {
        return model.constructor.create(id, gyroConfig);
    }
}
