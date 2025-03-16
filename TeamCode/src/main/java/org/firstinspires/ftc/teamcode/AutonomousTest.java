package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;


import org.firstinspires.ftc.teamcode.bots.FSMBot;
import org.firstinspires.ftc.teamcode.pedroPathing.util.Timer;
//import org.firstinspires.ftc.teamcode.bots.HangBot;

@Autonomous(name = "Auto Test", group = "Auto")
public class AutonomousTest extends LinearOpMode {
    public ElapsedTime timer = new ElapsedTime();
    protected FSMBot robot = new FSMBot(this);
    @Override
    public void runOpMode() throws InterruptedException {

        robot.isAuto = true;
        robot.init(hardwareMap);



        while (!opModeIsActive()) {
            telemetry.addData("status", "started");
            telemetry.update();
            robot.currentState = FSMBot.gameState.SUBMERSIBLE_INTAKE_1;
            robot.sleep(500);
//            robot.currentState = FSMBot.gameState.SUBMERSIBLE_INTAKE_2;
            double ms = timer.milliseconds();
        }

        waitForStart();
        timer.reset();
//        while(opModeIsActive()){
//
//            robot.onLoop(0,"test");
//            telemetry.addData("loop time", timer.getElapsedTime());
////            robot.updateTelemetry();
////            robot.subIntake(true);
//        }
    robot.driveToCoordinate(300,0,0,20,1,true);
    robot.driveToCoordinate(300,300,0,20,1, true);
    robot.driveToCoordinate(0, 300, 0, 20, 1, true);
    robot.driveToCoordinate(0, 0, 0,20, 1, true);
//        robot.scoreBucket(true);
    }}