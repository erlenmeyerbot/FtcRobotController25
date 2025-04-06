package org.firstinspires.ftc.teamcode.bots;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class CapstanBot extends BotBot{
    public DcMotor pivotMotor1 = null;
    private static final int TICKS_PER_REVOLUTION = 300; // 751

    public CapstanBot(LinearOpMode opMode)  {
        super(opMode);
    }

    public void init(HardwareMap ahwMap) {
        super.init(ahwMap);
        pivotMotor1 = ahwMap.get(DcMotor.class, "pivotMotor1");

        pivotMotor1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        pivotMotor1.setTargetPosition(0);
        pivotMotor1.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        pivotMotor1.setDirection(DcMotor.Direction.FORWARD);

        pivotMotor1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    public void up() {
        moveMotor(TICKS_PER_REVOLUTION, 0.5);
    }

    public void down() {
        moveMotor(-TICKS_PER_REVOLUTION, 0.3);
    }

    private void moveMotor(int tickCount, double power) {
        int newTarget = pivotMotor1.getCurrentPosition() + tickCount;
        pivotMotor1.setTargetPosition(newTarget);

        pivotMotor1.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        pivotMotor1.setPower(power);

        while (pivotMotor1.isBusy()) {
            // Optionally add telemetry or a timeout to prevent infinite loops
        }

        pivotMotor1.setPower(0.1);
    }

    public void stopMotor() {
        pivotMotor1.setPower(0);
        pivotMotor1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }
}
