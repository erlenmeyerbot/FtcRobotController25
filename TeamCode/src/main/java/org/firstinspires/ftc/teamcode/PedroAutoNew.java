package org.firstinspires.ftc.teamcode;

import com.pedropathing.follower.Follower;
import com.pedropathing.localization.Pose;
import com.pedropathing.pathgen.BezierLine;
import com.pedropathing.pathgen.PathChain;
import com.pedropathing.pathgen.Point;
import com.pedropathing.util.Constants;
import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.acmerobotics.dashboard.FtcDashboard;
import com.qualcomm.robotcore.util.ElapsedTime;

import static org.firstinspires.ftc.teamcode.bots.BotBot.*;
import org.firstinspires.ftc.teamcode.bots.FSMBot;
import org.firstinspires.ftc.teamcode.pedroPathing.constants.FConstants;
import org.firstinspires.ftc.teamcode.pedroPathing.constants.LConstants;

@Autonomous(name = "Pedro +Auto Samples", group = "Auto")
public class PedroAutoNew extends LinearOpMode{
    protected FSMBot robot = new FSMBot(this);
    private int pathState;

    private Timer pathTimer;
    private Follower follower;

    private final Pose startPose = new Pose(8.5, 112, Math.toRadians(0));  // Starting position
    private final Pose scorePose = new Pose(18, 127, Math.toRadians(-45)); // Scoring position

    private final Pose pickup1Pose = new Pose(30, 130, Math.toRadians(0)); // First sample pickup

    private final Pose slidePickup1Pose = new Pose(30, 127, Math.toRadians(0)); // Slide to sample pickup

    private final Pose pickup2Pose = new Pose(30,130,Math.toRadians(0));

    private final Pose slidePickup2Pose = new Pose(30,134, Math.toRadians(0));

    private final Pose pickup3Pose = new Pose(36,130,Math.toRadians(45));




    private PathChain scorePreload, moveToPickup1, pickup1, scoreSample1,moveToPickup2, pickup2, scoreSample2, pickup3, scoreSample3;

    public void buildPaths() {

        scorePreload = follower.pathBuilder()
                .addPath(new BezierLine(new Point(startPose), new Point(scorePose)))
                .setLinearHeadingInterpolation(startPose.getHeading(), scorePose.getHeading())
                .build();
        moveToPickup1 = follower.pathBuilder()
                .addPath(new BezierLine(new Point(scorePose), new Point(pickup1Pose)))
                .setLinearHeadingInterpolation(scorePose.getHeading(), pickup1Pose.getHeading())
                .build();
        pickup1 = follower.pathBuilder()
                .addPath(new BezierLine(new Point(pickup1Pose), new Point(slidePickup1Pose)))
                .setConstantHeadingInterpolation(0)
                .build();
        scoreSample1 = follower.pathBuilder()
                .addPath(new BezierLine(new Point(slidePickup1Pose), new Point(scorePose)))
                .setLinearHeadingInterpolation(slidePickup1Pose.getHeading(), scorePose.getHeading())
                .build();
        moveToPickup2 = follower.pathBuilder()
                .addPath(new BezierLine(new Point(scorePose), new Point(pickup2Pose)))
                .setLinearHeadingInterpolation(scorePose.getHeading(), pickup1Pose.getHeading())
                .build();
        pickup2 = follower.pathBuilder()
                .addPath(new BezierLine(new Point(pickup2Pose), new Point(slidePickup2Pose)))
                .setConstantHeadingInterpolation(0)
                .build();
        scoreSample2 = follower.pathBuilder()
                .addPath(new BezierLine(new Point(slidePickup2Pose), new Point(scorePose)))
                .setLinearHeadingInterpolation(slidePickup2Pose.getHeading(), scorePose.getHeading())
                .build();
        pickup3 = follower.pathBuilder()
                .addPath(new BezierLine(new Point(scorePose), new Point(pickup3Pose)))
                .setLinearHeadingInterpolation(scorePose.getHeading(), pickup3Pose.getHeading())
                .build();
        scoreSample3 = follower.pathBuilder()
                .addPath(new BezierLine(new Point(pickup3Pose), new Point(scorePose)))
                .setLinearHeadingInterpolation(pickup3Pose.getHeading(), scorePose.getHeading())
                .build();

    }

    public void autonomousPathUpdate() {
        switch (pathState) {
            case 0: // Move from start to scoring position
                follower.setMaxPower(0.7);
                follower.followPath(scorePreload);
                setPathState(1);
                break;
            case 1:
                if(!follower.isBusy()){
                    robot.currentState = FSMBot.gameState.SAMPLE_SCORING_HIGH_2;
                    if(robot.getSlidePosition() > 900){
                        robot.currentState = FSMBot.gameState.SAMPLE_SCORING_HIGH_3;
                    }
                    if(robot.currentState == FSMBot.gameState.ARM_DOWN) {
                        follower.followPath(moveToPickup1);
                        setPathState(2);
                    }
                }
            case 2:
                if(!follower.isBusy()){
                    robot.currentState = FSMBot.gameState.SUBMERSIBLE_INTAKE_3;
                    robot.pitchTo(55);
                    follower.followPath(pickup1);
                    setPathState(3);
                }
            case 3:
                if(!follower.isBusy()){
                    follower.followPath(scoreSample1);
                    setPathState(4);
                }
            case 4:
                if(!follower.isBusy()){
                    robot.currentState = FSMBot.gameState.SAMPLE_SCORING_HIGH_2;
                    if(robot.getSlidePosition() > 900){
                        robot.currentState = FSMBot.gameState.SAMPLE_SCORING_HIGH_3;
                    }
                    if(robot.currentState == FSMBot.gameState.ARM_DOWN) {
                        follower.followPath(moveToPickup2);
                        setPathState(5);
                    }

                }
            case 5:
                if(!follower.isBusy()){
                    robot.currentState = FSMBot.gameState.SUBMERSIBLE_INTAKE_3;
                    robot.pitchTo(55);
                    follower.followPath(pickup2);
                    setPathState(6);
                }
            case 6:
                if(!follower.isBusy()){
                    robot.currentState = FSMBot.gameState.SAMPLE_SCORING_HIGH_2;
                    if(robot.getSlidePosition() > 900){
                        robot.currentState = FSMBot.gameState.SAMPLE_SCORING_HIGH_3;
                    }
                    if(robot.currentState == FSMBot.gameState.ARM_DOWN) {
                        follower.followPath(scoreSample2);
                        setPathState(7);
                    }
                }
            case 7:
                if(!follower.isBusy()){
                    follower.followPath(pickup3);
                    setPathState(8);
                }
            case 8:
                if(!follower.isBusy()){
                    robot.currentState = FSMBot.gameState.SUBMERSIBLE_INTAKE_3;
                    robot.pitchTo(-27.5);
                    robot.slideRunToPosition(300);
                    if(robot.getSlidePosition() > 250) {
                        follower.followPath(scoreSample3);
                        setPathState(9);
                    }
                }
            case 9:
                if(!follower.isBusy()) {
                    robot.currentState = FSMBot.gameState.SAMPLE_SCORING_HIGH_2;
                    if (robot.getSlidePosition() > 900) {
                        robot.currentState = FSMBot.gameState.SAMPLE_SCORING_HIGH_3;
                    }
                    if (robot.currentState == FSMBot.gameState.ARM_DOWN) {
                        setPathState(-1);
                    }
                }
        }
    }

    public void setPathState(int pState) {
        pathState = pState;
        pathTimer.resetTimer();
    }
    @Override
    public void runOpMode() throws InterruptedException {
        robot.isAuto = true;
        robot.init(hardwareMap);
        pathTimer = new Timer();
        Timer opmodeTimer = new Timer();
        opmodeTimer.resetTimer();
        ElapsedTime initTimer = new ElapsedTime();
        initTimer.reset();
        follower = new Follower(hardwareMap, FConstants.class, LConstants.class);
        follower.setStartingPose(startPose);
        robot.currentState = FSMBot.gameState.DRIVE;
        buildPaths();
        setPathState(0);
        waitForStart();
        while (opModeIsActive()&&initTimer.milliseconds()>100) {
            robot.updateStates();
            telemetry.update();
            follower.update();
            autonomousPathUpdate();
        }
    }

}