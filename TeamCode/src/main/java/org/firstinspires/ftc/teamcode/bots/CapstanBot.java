package org.firstinspires.ftc.teamcode.bots;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.DcMotorControllerEx;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.PIDCoefficients;

public class CapstanBot extends BotBot{
    public DcMotorEx pivotMotor1 = null;
    public DcMotorEx pivotMotor2 = null;

    public static final double NEW_P = 10;
    public static final double NEW_I = 0;
    public static final double NEW_D = 0;

    private static final int TICKS_PER_REVOLUTION = 50; // 751

    public int maxPos = 2000;
    public int minPos = 0;

    public int newTarget = 0;

    public CapstanBot(LinearOpMode opMode)  {
        super(opMode);
    }

    public void init(HardwareMap ahwMap) {
        super.init(ahwMap);
        pivotMotor1 = ahwMap.get(DcMotorEx.class, "pivot1");
        pivotMotor2 = ahwMap.get(DcMotorEx.class, "pivot2");

        pivotMotor1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        pivotMotor1.setTargetPosition(0);
        pivotMotor1.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        pivotMotor2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        pivotMotor2.setTargetPosition(0);
        pivotMotor2.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        pivotMotor1.setDirection(DcMotor.Direction.FORWARD);
        pivotMotor2.setDirection(DcMotor.Direction.FORWARD);

        pivotMotor1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        DcMotorControllerEx motorControllerEx = (DcMotorControllerEx)pivotMotor1.getController();

        // change coefficients using methods included with DcMotorEx class.
        PIDCoefficients pidNew = new PIDCoefficients(NEW_P, NEW_I, NEW_D);
        pivotMotor1.setPIDCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidNew);
    }

    public void up() {
        moveMotor(TICKS_PER_REVOLUTION, 0.5);
    }

    public void down() {
        moveMotor(-TICKS_PER_REVOLUTION, 0.3);
    }

    private void moveMotor(int tickCount, double power) {

        newTarget = getCapstanPosition() + tickCount;

        if (newTarget > maxPos){
            newTarget = maxPos;
        }
        if (newTarget < minPos){
            newTarget = minPos;
        }

        pivotMotor1.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        pivotMotor1.setTargetPosition(newTarget);
        pivotMotor1.setPower(power);



    }

    public void stopMotor() {
        newTarget = getCapstanPosition();
        pivotMotor1.setTargetPosition(getCapstanPosition());
        pivotMotor1.setPower(0);
        pivotMotor1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public int getCapstanPosition() {
        return pivotMotor1.getCurrentPosition();
    }
    public void pivotToUpPos(boolean input) {
        if (input) {
            int pivotTarget = 500;
            pivotMotor1.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            pivotMotor1.setTargetPosition(pivotTarget);
            pivotMotor1.setPower(1);
        }
    }
    public void pivotToDownPos(boolean input) {
        if (input) {
            int pivotTarget = 500;
            pivotMotor2.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            pivotMotor2.setTargetPosition(pivotTarget);
            pivotMotor2.setPower(1);
        }
    }
}
