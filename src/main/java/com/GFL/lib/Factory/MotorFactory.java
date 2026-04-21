package com.GFL.lib.Factory;

import com.GFL.lib.hardware.Motor.Spark.SparkFlexMotor;
import com.GFL.lib.hardware.Motor.Spark.SparkMaxMotor;
import com.GFL.lib.hardware.Motor.Talon.TalonFXMotor;
import com.GFL.lib.hardware.config.MotorConfig;
import com.GFL.lib.hardware.interfaces.GenericMotor;

public class MotorFactory {
    @FunctionalInterface
    public interface MotorConstructor {
        GenericMotor create(int id, MotorModel motorModel, MotorConfig motorConfig);        
    }

    public enum MotorModel {
        Neo550(20, 20, SparkMaxMotor::new),
        Neo(40, 44, SparkMaxMotor::new),
        NeoVortex(40, 44, SparkFlexMotor::new),
        Krakenx60(40, 44, TalonFXMotor::new);

        public final int defaultStatorCurrentLimit;
        public final int defaultSupplyCurrentLimit;
        
        public final MotorConstructor constructor;

        MotorModel(int defaultStatorCurrentLimit, int defaultSupplyCurrentLimit, MotorConstructor constructor) {
            this.defaultStatorCurrentLimit = defaultStatorCurrentLimit;
            this.defaultSupplyCurrentLimit = defaultSupplyCurrentLimit;
            this.constructor = constructor;
        }
    }

    public static GenericMotor createMotor(int id, MotorModel model, MotorConfig motorConfig) {
        return model.constructor.create(id, model, motorConfig);
    }
}
