package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.bots.CapstanBot;
import org.firstinspires.ftc.teamcode.bots.FSMBot;
import org.firstinspires.ftc.teamcode.sample.Sample;

@TeleOp(name = "Capstan")
public class capstanTest extends LinearOpMode {
    private CapstanBot robot;

    @Override
    public void runOpMode() throws InterruptedException {

        robot = new CapstanBot(this);
        robot.init(hardwareMap);

        waitForStart();

        boolean isRunning = true;

        while (opModeIsActive()) {
            if(gamepad1.dpad_up){
                robot.up();
            }
            if(gamepad1.dpad_down){
                robot.down();
            }
            if(gamepad1.a){
                robot.stopMotor();
            }

            /*if (robot.getCapstanPosition() < robot.newTarget + 25 || robot.getCapstanPosition() > robot.newTarget - 25){

                robot.stopMotor();

            }*/

            telemetry.addData("capstan pos" ,robot.getCapstanPosition());
            telemetry.addData("target pos" , robot.newTarget);
            telemetry.update();
        }


        robot.close();
    }
}