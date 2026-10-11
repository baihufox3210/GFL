# GFL

GFL 是一個供 WPILib Java 專案使用的 FRC 硬體抽象函式庫，提供統一的馬達、編碼器與陀螺儀介面，並以 factory 和設定類別簡化裝置建立與設定。

## 功能

- **馬達**：REV Spark MAX、Spark Flex、CTRE Talon FX
- **馬達型號**：NEO 550、NEO、NEO Vortex、Kraken X60
- **陀螺儀**：Pigeon 2、NavX 2
- **編碼器**：Spark 內建相對／絕對編碼器、Talon FX 相對編碼器、CTRE CANcoder
- **馬達設定**：電流限制、煞車／滑行模式、反向、PID 與前饋、Motion Profile、連續輸入，以及編碼器設定

## 環境需求

- WPILib 2026 Java 專案
- Java 17
- 使用到的硬體廠商函式庫（REVLib、Phoenix 6、Studica/NavX）

## 安裝

在 WPILib 專案中開啟 **WPILib: Manage Vendor Libraries**，選擇線上安裝（Install new libraries (online)），並輸入：

```text
https://raw.githubusercontent.com/baihufox3210/GFL/main/GFL.json
```

也可以將此網址提供的 `GFL.json` 加入機器人專案的 `vendordeps`。函式庫版本與 Maven 座標會記錄在該檔案中。

## 快速開始

```java
import com.GFL.lib.Factory.MotorFactory;
import com.GFL.lib.Factory.GyroFactory;
import com.GFL.lib.hardware.config.GyroConfig;
import com.GFL.lib.hardware.config.MotorConfig;
import com.GFL.lib.hardware.interfaces.GenericGyro;
import com.GFL.lib.hardware.interfaces.GenericMotor;

// 在 robot subsystem 或其他適當位置建立硬體物件
MotorConfig motorConfig = new MotorConfig()
    .setCurrentLimits(40, 44)
    .setNeutralMode(MotorConfig.NeutralMode.BRAKE)
    .withKP(0.1)
    .setConversion(1.0, 1.0);

GenericMotor motor = MotorFactory.createMotor(
    1,
    MotorFactory.MotorModel.Neo,
    motorConfig
);
motor.configure();

GenericGyro gyro = GyroFactory.createGyro(
    0,
    GyroFactory.GyroModel.Pigeon2,
    new GyroConfig()
);

// 控制及讀取
motor.set(0.5);                       // 輸出百分比
motor.setPosition(10.0);              // 閉迴路位置目標
motor.setVelocity(5.0);               // 閉迴路速度目標
motor.setMotionMagicPosition(10.0);   // Motion profile 位置目標
motor.stop();

double position = motor.getEncoder().getPosition();
double velocity = motor.getEncoder().getVelocity();
double yaw = gyro.getYaw();
```

`setPosition`、`setVelocity` 與 Motion Profile 控制使用的單位，取決於硬體回授及 `MotorConfig` 中的編碼器比例設定。請依照機構與感測器規格設定並驗證單位、方向及控制器參數。`MotorConfig` 提供 fluent 設定方法；PID、Motion Profile 與編碼器進階選項也可透過 `pidConfig`、`motionConfig`、`encoderConfig` 設定。

### 使用遠端編碼器

使用 Talon FX 搭配 CANcoder 時，可指定遠端感測器 ID 與回授模式：

```java
MotorConfig motorConfig = new MotorConfig()
    .setSensorSource(MotorConfig.SensorSource.REMOTE)
    .setSensorMode(MotorConfig.SensorMode.FUSED);

motorConfig.encoderConfig.remoteSensorID = 5;

GenericMotor motor = MotorFactory.createMotor(
    1,
    MotorFactory.MotorModel.Krakenx60,
    motorConfig
);
motor.configure();
```

## 主要 API

- `GenericMotor`：輸出控制、位置／速度閉迴路控制、停止，以及取得編碼器
- `GenericEncoder`：讀取位置與速度，或設定位置
- `GenericGyro`：讀取 `Rotation2d`、角度與 yaw，並重設陀螺儀
- `MotorFactory` / `GyroFactory`：依型號建立對應裝置
- `MotorConfig` / `GyroConfig`：設定裝置行為