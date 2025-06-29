package org.firstinspires.ftc.teamcode.bots;

import android.content.Context;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.RobotLog;
import com.stormbots.MiniPID;

import java.io.OutputStreamWriter;

public class OdometryBot extends GyroBot {
    // Wheel diameters in mm
    final double SMALL_WHEEL_DIAMETER = 32; // Vertical wheel diameter
    final double LARGE_WHEEL_DIAMETER = 48; // Horizontal wheel diameter
    final double ORIGINAL_WHEEL_DIAMETER = 37; // Wheel diameter from previous season - used for conversion of ticks

    // Encoder ticks per revolution
    final double TICKS_PER_REV = 2048;

    // Conversion factors for ticks to distance (mm)
    final double SMALL_WHEEL_TICKS_TO_ORIGINAL = SMALL_WHEEL_DIAMETER /ORIGINAL_WHEEL_DIAMETER;
    final double LARGE_WHEEL_TICKS_TO_ORIGINAL = LARGE_WHEEL_DIAMETER /ORIGINAL_WHEEL_DIAMETER;

    //public Servo odometryRaise = null;

    String verticalLeftEncoderName = "v1", verticalRightEncoderName = "v2", horizontalEncoderName = "h";

    public double xBlue = 0, yBlue = 0, xBlueChange = 0, yBlueChange = 0, thetaDEG = 0, thetaRAD = 0;
    double xRed = 0, yRed = 0, xRedChange = 0, yRedChange = 0;
    double hError = 0;

    protected double[] driveAccelerationCurve = new double[]{0.5, 0.6, 0.8, 0.9, 0.8, 0.9};

    double savedXBlue, savedYBlue, savedThetaDEG;
    public double savedStartAngle;

    final int vLDirection = 1;
    final int vRDirection = -1;
    final int hDirection = -1;
    final double diameter = 3742; // actual diameter: 216/606 = d/10500
    final double hDiameter = 1732; //diameter of horizontal encoder: 50*2/606 = hD/10500
    final double leftX = -(diameter/2); //130mm
    final double rightX = (diameter/2); //130mm
    final double hY = -(hDiameter); //50mm

    double vLOffset, vROffset, hOffset = 0;

    public double previousVL = 0, previousVR = 0, previousH = 0;
    double angleChange = 0;

    double drive;
    double strafe;
    double twist;
    double driveAngle;
    double thetaDifference;
    double distanceToTarget;
    long startTime;
    long elapsedTime = 0;
    public boolean isCoordinateDriving = false;
    public boolean isTurningInPlace = false;

    MiniPID drivePID = new MiniPID(0.085, 0, 0);//i: 0.006 d: 0.06
    MiniPID twistPID = new MiniPID(0.025, 0, 0);

    double globalTargetX = 0;
    double globalTargetY = 0;
    double globalTargetTheta = 0;
    int globalTolerance = 0;
    double globalAngleTol = 0;
    double globalMagnitude = 0;

    ElapsedTime robotLogTimer = new ElapsedTime();

    OutputStreamWriter odometryWriter;

    public OdometryBot(LinearOpMode opMode) {
        super(opMode);
    }

    @Override
    public void init(HardwareMap ahwMap) {
        super.init(ahwMap);
        initDriveHardwareMap(ahwMap);
        context = hwMap.appContext;
        telemetry.addData("Status", "Init Complete");
        telemetry.update();
        robotLogTimer.reset();
    }

    private void initDriveHardwareMap(HardwareMap ahwMap){
//        verticalLeft = ahwMap.dcMotor.get(verticalLeftEncoderName);
//        verticalLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//        verticalLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
//        horizontal = ahwMap.get(DcMotorEx.class, "leftFront"); /**changed initialization because it was showing error might mess something uup idk*/
//        horizontal.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//        horizontal.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
//        verticalRight = ahwMap.get(DcMotorEx.class, "leftRear");
//        verticalRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//        verticalRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
//        verticalLeft = ahwMap.get(DcMotorEx.class, "leftRear");
//        verticalLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//        verticalLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        telemetry.addData("Status", "Hardware Map Init Complete");
        telemetry.update();
    }

    Context context;
    /**
     * Calculates a position and heading based on odometry pod encoder readings. Saves these to the
     * variables xBlue, yBlue, thetaRAD.
     * @param vL position of vertical left encoder
     * @param vR position of vertical right encoder
     * @param h position of horizontal encoder
     */
    public void calculateCaseThree(double vL, double vR, double h) {
        vL = vL * vLDirection * SMALL_WHEEL_TICKS_TO_ORIGINAL;
        vR = vR * vRDirection * SMALL_WHEEL_TICKS_TO_ORIGINAL;
        h = h * hDirection * SMALL_WHEEL_TICKS_TO_ORIGINAL;

        double lC = vL - previousVL;
        double rC = vR - previousVR;

//        angleChange = ((lC - rC) / (Math.PI * diameter * 2) * 360);
//        angleChange = 3.7715*(lC - rC)/(rightX - leftX);
        angleChange = -(lC - rC)/(rightX - leftX);

//        angleChange = (lC - rC)/(2 * diameter);
//
//        angleDEG = angleDEG + angleChange;
        thetaRAD += angleChange;
        thetaDEG = Math.toDegrees(thetaRAD);

        //thetaDEG = getDeltaAngle();

        //angleChange = angleDEG - previousThetaDEG;

        hError = -angleChange * hDiameter/2;

        double hC = h - previousH;

        xRedChange = hC + hError;
        //xRedChange = hC - (hY * angleChange);
        yRedChange = (lC + rC)/2;
        //yRedChange = ((lC * rightX) - (rC * leftX))/(rightX - leftX);

        xBlueChange = Math.cos(thetaRAD - (Math.PI/2)) * xRedChange + Math.cos(thetaRAD) * yRedChange;
        yBlueChange = Math.sin(thetaRAD) * yRedChange + Math.sin(thetaRAD - (Math.PI/2)) * xRedChange;
//        xBlueChange = xRedChange * Math.cos(thetaRAD) + yRedChange * Math.sin(thetaRAD);
//        yBlueChange = yRedChange * Math.cos(thetaRAD) + xRedChange * Math.sin(thetaRAD);

        xBlue = xBlue + yBlueChange;
        yBlue = yBlue + xBlueChange;
//        xBlue = xBlue + xBlueChange;
//        yBlue = yBlue + yBlueChange;

        previousVL = vL;
        previousVR = vR;
        previousH = h;

        //previousThetaDEG = angleDEG;
    }
    /**
     * Resets the odometry-calculated angle with a IMU gyro reading.
     * @param offset value to adjust the reset (in degrees), set to 0 for default
     */
    public void reAngle(double offset) {
        thetaRAD = Math.toRadians(getDeltaAngle() + offset);
    }

//    public void resetOdometry(boolean button) {
//
//        if (button) {
////            vLOffset = leftFront.getCurrentPosition();
////            vROffset = rightFront.getCurrentPosition();
////            hOffset = horizontal.getCurrentPosition() + 79000;
//
//            horizontal.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//            leftFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//            rightFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//
//            horizontal.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
//            leftFront.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
//            rightFront.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
//
//            previousVL = 0;
//            previousVR = 0;
//            previousH = 0;
//
//            xBlue = 79000;
//            yBlue = 0;
//
//            thetaDEG = 0;
//        }
//    }

    protected void onTick(){
        RobotLog.d(String.format("Position, heading: %.2f, %.2f, %.2f", xBlue, yBlue, thetaDEG));
//
        telemetry.addData("X:", xBlue);
        telemetry.addData("Y:", yBlue);
        telemetry.addData("Theta:", thetaDEG);
        telemetry.addData("vL", leftFront.getCurrentPosition());
        telemetry.addData("vR", rightFront.getCurrentPosition());
        telemetry.addData("h", rightRear.getCurrentPosition());
//        telemetry.update();
//        telemetry.addData("h diameter", (int)((thetaDEG*360)/(horizontal.getCurrentPosition() * Math.PI)));
//        telemetry.update();

        //outputEncoders();
        super.onTick();
        //thetaDEG = -getDeltaAngle();
        calculateCaseThree(leftFront.getCurrentPosition() - vLOffset, rightFront.getCurrentPosition() - vROffset, rightRear.getCurrentPosition() - hOffset);
                /** must find a new motor encoders for the odometry pods */
        if (isCoordinateDriving) {
            driveToCoordinateUpdate(globalTargetX, globalTargetY, globalTargetTheta, globalTolerance, globalAngleTol, globalMagnitude);
        }
    }
    /**
     * Updates the odometry drive with new target parameters.
     * @param xTarget x-coordinate of desired position
     * @param yTarget y-coordinate of desired position
     * @param targetTheta orientation of robot at desired position (in degrees)
     * @param tolerance allowed deviation from desired position
     * @param angleTol allowed deviation from target orientation
     * @param magnitude maximum power
     * @param brake whether to brake or float when the desired position is reached
     */
    public void driveToCoordinate(double xTarget, double yTarget, double targetTheta, int tolerance, double angleTol, double magnitude, boolean brake) {
        if (brake) {
            leftFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            rightFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            leftRear.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            rightRear.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        } else {
            leftFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
            rightFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
            leftRear.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
            rightRear.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        }
        if (xBlue > xTarget) {
            distanceToTarget = - Math.sqrt(Math.pow(xBlue - xTarget, 2) + Math.pow(yBlue - yTarget, 2));
        } else {
            distanceToTarget = Math.sqrt(Math.pow(xBlue - xTarget, 2) + Math.pow(yBlue - yTarget, 2));
        }
        //RobotLog.d(String.format("BlueX: %f BlueY: %f Theta: %f", xBlue, yBlue, thetaDEG));
        globalTargetX = xTarget;
        globalTargetY = yTarget;
        globalTargetTheta = targetTheta;
        globalAngleTol = angleTol;
        globalTolerance = tolerance;
        globalMagnitude = magnitude;

        isCoordinateDriving = true;

//            driveToCoordinateUpdate(xTarget, yTarget, targetTheta, tolerance, magnitude);

//            elapsedTime = System.currentTimeMillis() - startTime;
//            if (elapsedTime > 10000) {
//                break;
//            }
    }
    /**
     * Simplified version of {@link #driveToCoordinate(double, double, double, int, double, double, boolean)}.
     */
    public void driveToCoordinate(double xTarget, double yTarget, double targetTheta, int tolerance, double magnitude, boolean brake) {
        driveToCoordinate(xTarget, yTarget, targetTheta, tolerance, 1, magnitude, brake);
    }
    /**
     * Controls the specifics of odometry driving.
     */
    public void driveToCoordinateUpdate(double xTarget, double yTarget, double targetTheta, int tolerance, double angleTol, double magnitude) {
        drivePID.setOutputLimits(magnitude);
        twistPID.setOutputLimits(0.35);
        thetaDifference = targetTheta - thetaDEG;
        twist = twistPID.getOutput(thetaDEG, targetTheta);
        double rawDriveAngle = Math.toDegrees(Math.atan2(xTarget - xBlue, yTarget - yBlue));
        driveAngle = -(rawDriveAngle - thetaDEG);
        magnitude = Math.min(1.0, Math.abs(drivePID.getOutput(distanceToTarget/1250, 0))*1.5);
        if (Math.abs(distanceToTarget) < 2000) {
            magnitude = Math.max(0.2, Math.min(1.0, Math.abs(drivePID.getOutput(distanceToTarget/500, 0))));
        }
        if (xBlue > xTarget) {
            distanceToTarget = - Math.sqrt(Math.pow(xBlue - xTarget, 2) + Math.pow(yBlue - yTarget, 2));
        } else {
            distanceToTarget = Math.sqrt(Math.pow(xBlue - xTarget, 2) + Math.pow(yBlue - yTarget, 2));
        }
        drive = (Math.cos(Math.toRadians(driveAngle)) * magnitude);
        strafe = Math.sin(Math.toRadians(driveAngle)) * magnitude;

        driveByVector(-drive, strafe, twist, 1);
        RobotLog.d(String.format("BlueX: %f BlueY: %f Theta: %f Angle: %f Drive: %f Strafe: %f Twist: %f", xBlue, yBlue, thetaDEG, driveAngle, drive, strafe, twist));
        RobotLog.d(String.format("Distance: %f Magnitude: %f", distanceToTarget, magnitude));

        if ((xTarget + tolerance > xBlue) && (xTarget - tolerance < xBlue) && (yTarget + tolerance > yBlue) && (yTarget - tolerance < yBlue) && Math.abs(thetaDifference) < angleTol) {
            isCoordinateDriving = false;
            driveByVector(0, 0, 0, 1);
            RobotLog.d("TARGET REACHED");
        } else {
            isCoordinateDriving = true;
        }
    }

    /**
     * Strafes to a given distance with help of odometry
     * @param distance In centimeters, positive is right, negative is left
     */
    public void strafing(double distance){
        double xTarget = xBlue + distance * CENTIMETER_TO_DRIVING_MOTOR_CONVERSION_RATE;
        driveToCoordinate(xTarget, yBlue, thetaDEG, 10, 1, false);
    }
    public void stopCoordinateDrive(){
        isCoordinateDriving = false;
    }

    public void startCoordinateDrive(){
        isCoordinateDriving = true;
    }
    /**
     * Wait for odometry drive to stop (target reached).
     */
    public void waitForCoordinateDrive() {
        while (opMode.opModeIsActive() && isCoordinateDriving) {
            sleep(0, "wait for drive");
        }
    }
    public void updateTelemetry(){
//        telemetry.addData("X:", xBlue);
//        telemetry.addData("Y:", yBlue);
//        telemetry.addData("Theta:", thetaDEG);
//        telemetry.addData("vL", leftFront.getCurrentPosition());
//        telemetry.addData("vR", rightFront.getCurrentPosition());
//        telemetry.addData("h", rightRear.getCurrentPosition());
//        telemetry.update();
    }

//    public void savePosition() {
////        try {
////            odometryWriter = new FileWriter("/sdcard/FIRST/odometry positions.txt", false);
////        } catch (IOException e) {
////            throw new RuntimeException("odometry file writer open failed: " + e.toString());
////        }
////        try {
////            RobotLog.d("odometryWriter.write");
////            odometryWriter.write(xBlue + "\n");
////            odometryWriter.write(yBlue + "\n");
////            odometryWriter.write(thetaDEG + "\n");
////            odometryWriter.write(getAngle() + "\n");
////        } catch (IOException e) {
////            throw new RuntimeException("odometry file writer write failed: " + e.toString());
////        }
////        try {
////            RobotLog.d("odometryWriter.close");
////            odometryWriter.close();
////        } catch (IOException e) {
////            throw new RuntimeException("odometry file writer close failed: " + e.toString());
////        }
//        try {
//            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("odometry positions.txt", Context.MODE_PRIVATE));
//
//            // write each configuration parameter as a string on its own line
//            outputStreamWriter.write(xBlue + "\n");
//            outputStreamWriter.write(yBlue + "\n");
//            outputStreamWriter.write(thetaDEG + "\n");
//            outputStreamWriter.write(getAngle() + "\n");
//
//            outputStreamWriter.close();
//        }
//        catch (IOException e) {
//            telemetry.addData("Exception", "Configuration file write failed: " + e.toString());
//        }
//
//    }

//    public void readPosition() {
//        try {
//            InputStream inputStream = context.openFileInput("odometry positions.txt");
//            if ( inputStream != null ) {
//                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
//                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
//
//                xBlue = Double.parseDouble(bufferedReader.readLine());
//                telemetry.addData("X:", xBlue);
//                yBlue = Double.parseDouble(bufferedReader.readLine());
//                telemetry.addData("Y:", yBlue);
//                telemetry.update();
//                RobotLog.d(String.format("odometry bodoo: %.2f, %.2f", xBlue, yBlue));
//                thetaDEG = Double.parseDouble(bufferedReader.readLine());
//                savedStartAngle = Double.parseDouble(bufferedReader.readLine());
//                thetaDEG = savedStartAngle;
//
//                inputStream.close();
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
}
