//package org.firstinspires.ftc.teamcode;
//
//import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
//import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
//import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
//import com.qualcomm.robotcore.util.ElapsedTime;
//
//import org.firstinspires.ftc.teamcode.bots.FSMBot;
//import org.firstinspires.ftc.teamcode.pedroPathing.follower.Follower;
//import org.firstinspires.ftc.teamcode.pedroPathing.localization.Pose;
//import org.firstinspires.ftc.teamcode.pedroPathing.pathGeneration.BezierLine;
//import org.firstinspires.ftc.teamcode.pedroPathing.pathGeneration.Path;
//import org.firstinspires.ftc.teamcode.pedroPathing.pathGeneration.PathChain;
//import org.firstinspires.ftc.teamcode.pedroPathing.pathGeneration.Point;
//import org.firstinspires.ftc.teamcode.pedroPathing.util.Timer;
//import org.firstinspires.ftc.teamcode.pedroPathing.follower.Follower;
//import org.firstinspires.ftc.teamcode.pedroPathing.localization.Pose;
//import org.firstinspires.ftc.teamcode.pedroPathing.pathGeneration.BezierCurve;
//import org.firstinspires.ftc.teamcode.pedroPathing.pathGeneration.BezierLine;
//import org.firstinspires.ftc.teamcode.pedroPathing.pathGeneration.Path;
//import org.firstinspires.ftc.teamcode.pedroPathing.pathGeneration.PathChain;
//import org.firstinspires.ftc.teamcode.pedroPathing.pathGeneration.Point;
//import org.firstinspires.ftc.teamcode.pedroPathing.util.Timer;
//import org.firstinspires.ftc.teamcode.sample.Sample;
//import com.acmerobotics.dashboard.FtcDashboard;
//import com.qualcomm.robotcore.util.ElapsedTime;
//
//
//@Autonomous(name = "Pedro test", group = "Auto")
//
//public class AutoPedroTest extends LinearOpMode {
//    private Follower follower;
//    private Timer pathTimer, actionTimer, opmodeTimer;
//
//    private ElapsedTime initTimer;
//
//    private boolean isReady = false;
//
//
//    private ElapsedTime actiontime = new ElapsedTime();
//
//    /**
//     * This is the variable where we store the state of our auto.
//     * It is used by the pathUpdate method.
//     */
//    private int pathState;
//
//    private final Pose pos1 = new Pose(10, 0, 0);
//    private final Pose pos2 = new Pose(10, 10, 0);
//    private final Pose pos3 = new Pose(0, 10, 0);
//    private final Pose pos4 = new Pose(0, 0, 0);
//
//    private final Pose startingPose = new Pose(0, 0, 0);
//
//
//    private PathChain path1, path2, path3, path4;
//
//    public void buildPaths() {
//        path1 = follower.pathBuilder()
//                .addPath(new BezierLine(new Point(pos4), new Point(pos1)))
//                .setLinearHeadingInterpolation(pos4.getHeading(), pos1.getHeading())
//                .build();
//        path2 = follower.pathBuilder()
//                .addPath(new BezierLine(new Point(pos1), new Point(pos2)))
//                .setLinearHeadingInterpolation(pos1.getHeading(), pos2.getHeading())
//                .build();
//        path3 = follower.pathBuilder()
//                .addPath(new BezierLine(new Point(pos2), new Point(pos3)))
//                .setLinearHeadingInterpolation(pos2.getHeading(), pos3.getHeading())
//                .build();
//        path4 = follower.pathBuilder()
//                .addPath(new BezierLine(new Point(pos3), new Point(pos4)))
//                .setLinearHeadingInterpolation(pos3.getHeading(), pos4.getHeading())
//                .build();
//
//    }
//
//    protected AutonBot robot = new AutonBot(this);
//
//    public void runOpMode() throws InterruptedException {
//
//        robot.isAuto = true;
//
//        waitForStart();
//
//        robot.init(hardwareMap);
//        pathTimer = new Timer();
//        opmodeTimer = new Timer();
//        opmodeTimer.resetTimer();
//        initTimer = new ElapsedTime();
//        initTimer.reset();
//
//        follower = new Follower(hardwareMap);
//        follower.setStartingPose(startingPose);
//        robot.currentState = FSMBot.gameState.DRIVE;
//        buildPaths();
//        opmodeTimer.resetTimer();
//        pathState = 0;
//        ElapsedTime timer = new ElapsedTime();
//        while (opModeIsActive()&&initTimer.milliseconds()>100) {
////            telemetry.addData("pivot pos", robot.getPivotPosition());
////            telemetry.addData("slide pos", robot.getSlidePosition());
////            telemetry.addData("Pivot target", robot.pivotTarget);
////            telemetry.addData("path state", pathState);
////            telemetry.addData("follower busy", follower.isBusy());
//            telemetry.addData("x", follower.getPose().getX());
//            telemetry.addData("y", follower.getPose().getY());
////            telemetry.addData("heading", follower.getPose().getHeading());
////            telemetry.addData("Follower path", follower.getCurrentPath());
////            telemetry.addData("state", robot.currentState);
////            telemetry.addData("action time", actiontime.milliseconds());
//
//timer.reset();
//            robot.updateStates();
//            telemetry.addData("loop time", timer.milliseconds());
//            telemetry.update();
//            follower.update();
//            autonomousPathUpdate();
//        }
//    }
//    public void autonomousPathUpdate() {
//    switch (pathState){
//        case 0:
//            follower.followPath(path1);
//            if(!follower.isBusy()){
//                pathState = 1;
//            }
//        case 1:
//            follower.followPath(path2);
//            if(!follower.isBusy()){
//                pathState = 2 ;
//            }
//        case 2:
//            follower.followPath(path3);
//            if(!follower.isBusy()){
//                pathState = 3;
//            }
//        case 3:
//            follower.followPath(path4);
//            if(!follower.isBusy()){
//                pathState = 4;
//            }
//        case 4:
//            robot.sleep(100);
//    }
//
//
//
//        }
//    }
