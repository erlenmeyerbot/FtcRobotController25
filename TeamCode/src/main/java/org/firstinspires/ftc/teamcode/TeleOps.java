package org.firstinspires.ftc.teamcode;


import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.telemetry;
import com.acmerobotics.dashboard.FtcDashboard;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.bots.GyroBot;
import org.firstinspires.ftc.teamcode.bots.HangBot;
import org.firstinspires.ftc.teamcode.bots.AutomationBot;
import org.firstinspires.ftc.teamcode.bots.LimelightBot;
import org.firstinspires.ftc.teamcode.bots.PivotBot;
import org.firstinspires.ftc.teamcode.bots.PinchBot;
import org.firstinspires.ftc.teamcode.sample.Sample;

@TeleOp(name = "Drive")
public class TeleOps extends LinearOpMode {
    private HangBot robot = new HangBot(this);

    private Sample lastSample = null;

    private boolean blueAlliance = true;

    private ElapsedTime scanTimer = new ElapsedTime();
    private ElapsedTime clawTimer = new ElapsedTime();
    private boolean clawBool = false;
    @Override
    public void runOpMode() throws InterruptedException {

        robot.isAuto = false;

        robot.init(hardwareMap);


        telemetry.addData("blue alliance:", blueAlliance);

        while (opModeInInit()) {

            if (gamepad1.dpad_right) {
                blueAlliance = !blueAlliance;

                telemetry.addData("blue alliance:", blueAlliance);

            }

            telemetry.update();

        }

        waitForStart();
        while(opModeIsActive()){


            telemetry.setMsTransmissionInterval(11);

            robot.resetAngle(gamepad1.x);

//            if (gamepad2.b) { robot.stopCoordinateDrive(); }

            robot.onLoop(0, "manual drive");

            robot.pivotControl(gamepad1.dpad_up, gamepad1.dpad_down);
            robot.slideControl(gamepad1.dpad_right, gamepad1.dpad_left);

            robot.pinchControl(gamepad1.a, gamepad1.b);

            robot.rotateControl((gamepad1.left_trigger > 0.5) || (gamepad2.left_trigger > 0.5), (gamepad1.right_trigger > 0.5) || (gamepad2.right_trigger > 0.5));
//            robot.readySpecimenPos(gamepad2.a, true);
            //robot.scoreSpecimenSimple(gamepad2.b);
           robot.scoreSpecimen(gamepad2.b);
            robot.scoreBucket(gamepad2.a);
            robot.goToDefaultPosition(gamepad2.y);
            robot.openPinchButton(gamepad2.x);
//            robot.hang(gamepad2.x);

            if (gamepad1.y) {
                robot.inAutoPickup = false;
                robot.openPinch();
                robot.rotateToVerticalPos();
                robot.relatePivotToSlide();
            }

            if (gamepad1.left_bumper && scanTimer.time() > 1){
                //robot.pickup(blueAlliance, true, false, telemetry, scanTimer);
                robot.alignWithSample(blueAlliance, true, false, telemetry);
            } else {
                robot.driveByHandFieldCentric(gamepad1.left_stick_x, gamepad1.left_stick_y,
                        gamepad1.right_stick_x*-0.7, gamepad1.left_stick_button, gamepad2.left_stick_x,
                        gamepad2.left_stick_y, gamepad2.right_stick_x, gamepad2.left_stick_button);
            }
//            if (gamepad2.a){
//                robot.rotateToPos(robot.rotateMaxPos);
//                robot.pivotToSearchPos();
//                robot.moveSlideToSearchPos();
//                robot.openPinch();
//            }
//            if (gamepad2.b){
//                robot.alignWithSample(true, true, false, telemetry);
//            }

            if (gamepad1.right_bumper){
                robot.pickup(false, telemetry);
            }
//            if (gamepad2.y){
//                robot.limelight.captureSnapshot("Pickup test");
//            }

//            if (gamepad2.left_bumper){
//                robot.detectOne(true, true, telemetry);
//            }
//            else if (gamepad2.right_bumper){
//                robot.detectOne(false, true, telemetry);
//            }

            telemetry.addData("slide position", robot.getSlidePosition());
            telemetry.addData("pivot position", robot.getPivotPosition());
            telemetry.addData("rotate position", robot.rotate.getPosition());
            telemetry.addData("vR",robot.rightFront.getCurrentPosition() );
            telemetry.addData("vL", robot.rightRear.getCurrentPosition());
            telemetry.addData("h", robot.leftRear.getCurrentPosition());
            telemetry.addData("scan timer", scanTimer.time());

            if (robot.pivotOutOfRange) {

                telemetry.addData("ERROR", "Pivot is out of Range");

            }
            telemetry.addData("hang position", robot.getCurrentPositionString());


        }
        robot.close();
    }

}
