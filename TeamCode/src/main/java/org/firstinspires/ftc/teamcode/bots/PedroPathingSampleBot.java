//package org.firstinspires.ftc.teamcode.bots;
//
//import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
//import com.qualcomm.robotcore.hardware.HardwareMap;
//
//import org.firstinspires.ftc.teamcode.pedroPathing.follower.Follower;
//import org.firstinspires.ftc.teamcode.pedroPathing.localization.Pose;
//import org.firstinspires.ftc.teamcode.pedroPathing.pathGeneration.BezierCurve;
//import org.firstinspires.ftc.teamcode.pedroPathing.pathGeneration.BezierLine;
//import org.firstinspires.ftc.teamcode.pedroPathing.pathGeneration.Path;
//import org.firstinspires.ftc.teamcode.pedroPathing.pathGeneration.PathChain;
//import org.firstinspires.ftc.teamcode.pedroPathing.pathGeneration.Point;
//import org.firstinspires.ftc.teamcode.pedroPathing.util.Timer;
//
//public class PedroPathingSampleBot extends FSMBot {
//    private Follower follower;
//    private Timer pathTimer, actionTimer, opmodeTimer;
//
//    /** This is the variable where we store the state of our auto.
//     * It is used by the pathUpdate method. */
//    private int pathState;
//
//    private final Pose startPose = new Pose(7.7, 53.89, 0);// starting position of robot
//    private final Pose scoreSpecimenPose = new Pose(40, 66, Math.toRadians(180));// position where specimen is scored on submersible, robot is aligned to submerisble with back facing it
//
//    //    private final Pose sample1 = new Pose(35, 23,0); //these three poses are just behind the samples
//    private final Pose samplePivotPose = new Pose(35, 12.7,0); //pivot from one point to grab all 3 samples
////    private final Pose sample3 = new Pose(35, 6,0);
//
//    private final Pose dropSamplePose = new Pose(28, 17, Math.toRadians(180));
//
//    private final Pose loadSpecimenPose = new Pose(7.9, 23.6, 0);
//
//    private final Pose scoreSamplePose = new Pose(17.7, 122.3, Math.toRadians(-45));
//
//    private final Pose sampleCurveControlPoint = new Pose(21.8, 36.7, Math.toRadians(-36));
//
//    /** Park Pose for our robot, after we do all of the scoring. */
//    private final Pose parkPose = new Pose(60, 46, Math.toRadians(90));
//
//    /** coordinate to control bezier curve for parking, to go around the submersible must use bezier curve, this is mid point.*/
//    private final Pose parkControl = new Pose (37, 25, 0);
//
//
//
//    private Path scorePreload, park;
//
//    private PathChain pickup, dropoff, loadSpecimen, scoreSpecimen, scoreSample;
//
//    public void buildPaths(){
//        scorePreload = new Path(new BezierLine(new Point(startPose), new Point(scoreSpecimenPose)));
//        scorePreload.setLinearHeadingInterpolation(startPose.getHeading(), scoreSpecimenPose.getHeading());
//
//        pickup = follower.pathBuilder()
//                .addPath(new BezierLine(new Point(dropSamplePose), new Point (samplePivotPose)))
//                .setLinearHeadingInterpolation(scoreSpecimenPose.getHeading(), samplePivotPose.getHeading())
//                .build();
//
//        dropoff = follower.pathBuilder()
//                .addPath(new BezierLine(new Point(samplePivotPose), new Point(dropSamplePose)))
//                .setLinearHeadingInterpolation(samplePivotPose.getHeading(), dropSamplePose.getHeading())
//                .build();
//
//        loadSpecimen = follower.pathBuilder()
//                .addPath(new BezierLine(new Point(dropSamplePose), new Point(loadSpecimenPose)))
//                .setLinearHeadingInterpolation(dropSamplePose.getHeading(), loadSpecimenPose.getHeading())
//                .build();
//
//        scoreSpecimen = follower.pathBuilder()
//                .addPath(new BezierLine(new Point(loadSpecimenPose), new Point(scoreSpecimenPose)))
//                .setLinearHeadingInterpolation(loadSpecimenPose.getHeading(), scoreSpecimenPose.getHeading())
//                .build();
//
//        park = new Path(new BezierCurve(new Point(scoreSpecimenPose), /* Control Point */ new Point(parkControl), new Point(parkPose)));
//        park.setLinearHeadingInterpolation(scoreSpecimenPose.getHeading(), parkPose.getHeading());
//
//        scoreSample = follower.pathBuilder()
//                .addPath(new BezierCurve(new Point(samplePivotPose), new Point(sampleCurveControlPoint), new Point(scoreSamplePose)))
//                .build();
//    }
//
//
//
//
//    public PedroPathingSampleBot(LinearOpMode opMode) {
//        super(opMode);
//    }
//
//    public void init(HardwareMap ahwMap) {
//        super.init(ahwMap);
//        buildPaths();
//    }
//
//    protected void onTick() {
//        super.onTick();
//        // These loop the movements of the robot
//        follower.update();
//        autonomousPathUpdate();
//
//        // Feedback to Driver Hub
//        telemetry.addData("path state", EVENT_NAMES[pathState], pathState);
//        telemetry.addData("x", follower.getPose().getX());
//        telemetry.addData("y", follower.getPose().getY());
//        telemetry.addData("heading", follower.getPose().getHeading());
//        telemetry.update();
//    }
//
//    public void autonomousPathUpdate()  {
//        switch (pathState) {
//            case 0:
//                follower.followPath(scorePreload);
//                setPathState(1);
//                //Goes to submersible, in position to score preload
//                break;
//            case 1:
//
//                /* You could check for
//                - Follower State: "if(!follower.isBusy() {}"
//                - Time: "if(pathTimer.getElapsedTimeSeconds() > 1) {}"
//                - Robot Position: "if(follower.getPose().getX() > 36) {}"
//                */
//
//                /* This case checks the robot's position and will wait until the robot position is close (1 inch away) from the scorePose's position */
//                if (!follower.isBusy()/**||pathTimer.getElapsedTimeSeconds() > 2*/) {
//                    /* Score Preload */
//
//                    //INSERT 3DOF CODE HERE TO SCORE SPECIMEN
//
//                    /* Since this is a pathChain, we can have Pedro hold the end point while we are grabbing the sample */
////                         follower.followPath(grabPickup1,true);
//                    triggerEvent(EVENT_PRELOAD_SCORED, 2);
//                    setPathState(EVENT_PRELOAD_SCORED);
//                }
//                break;
//
//            case 2:
//
//                if (!follower.isBusy()) {
//                    follower.followPath(pickup, true);
//                    //pickup first sample
//
//                    triggerEvent(EVENT_SAMPLE_1_PICKEDUP, 3);
//                    setPathState(EVENT_SAMPLE_1_PICKEDUP);
//                }
//                break;
//
//            case 3:
//
//                if (!follower.isBusy()) {
//                    follower.followPath(scoreSample, true);
//                    //release sample with claw
//                    triggerEvent(EVENT_SAMPLE_1_SCORED , 4);
//                    setPathState(EVENT_SAMPLE_1_SCORED);
//                }
//                break;
//
//            case 4:
//
//                if (!follower.isBusy()) {
//                    follower.followPath(pickup, true);
//                    //pickup first sample
//                    triggerEvent(EVENT_SAMPLE_2_PICKEDUP, 5);
//                    setPathState(EVENT_SAMPLE_2_PICKEDUP);
//                }
//                break;
//
//            case 5:
//
//                if (!follower.isBusy()) {
//                    follower.followPath(scoreSample, true);
//                    //release sample with claw
//                    triggerEvent(EVENT_SAMPLE_2_SCORED , 6);
//                    setPathState(EVENT_SAMPLE_2_SCORED);
//                }
//                break;
//
//            case 6:
//
//                if (!follower.isBusy()) {
//                    follower.followPath(pickup, true);
//                    //pickup first sample
//                    triggerEvent(EVENT_SAMPLE_3_PICKEDUP, 7);
//                    setPathState(EVENT_SAMPLE_3_PICKEDUP);
//                }
//                break;
//
//            case 7:
//
//                if (!follower.isBusy()) {
//                    follower.followPath(scoreSample, true);
//                    //release sample with claw
//                    triggerEvent(EVENT_SAMPLE_3_SCORED , 8);
//                    setPathState(EVENT_SAMPLE_3_SCORED);
//                }
//                break;
//
//            case 8:
//                if(!follower.isBusy()){
//                    follower.followPath(park, true);
//                    triggerEvent(EVENT_PARKED);
//                    setPathState(-1);
//                }
//        }
//    }
//    private void setPathState(int pState) {
//        pathState = pState;
//        pathTimer.resetTimer();
//    }
//
//}
