package org.firstinspires.ftc.teamcode.tests;


import com.pedropathing.follower.Follower;

import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.Path;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.classes.intaking;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

@TeleOp
public class limelightBallDetectingTest extends OpMode {

    private Follower follower;

    Limelight3A limelight;

    intaking transfer = new intaking();

    double camHeightIn = 0.0;
    double camPitchDeg = 0.0;
    double ballHeightIn = 0.0;

    

    @Override
    public void init() {
        follower = Constants.createFollower(hardwareMap);
        transfer.init(hardwareMap);

        follower.setStartingPose(new Pose(72, 72, Math.toRadians(0)));
        follower.update();
        limelight = hardwareMap.get(Limelight3A.class, "limelight");
        limelight.pipelineSwitch(5);
    }

    @Override
    public void start(){
        limelight.start();
    }

    @Override
    public void loop() {
        follower.update();

        Pose currentPose = follower.getPose();
        LLResult result = limelight.getLatestResult();
        if(result.isValid()){
            transfer.intakeOn();
            follower.setMaxPower(1);
            double tx = result.getTx();
            double ty = result.getTy();
            double angleToBallDeg = camPitchDeg + ty;
            double angleToBallRad = Math.toRadians(angleToBallDeg);
            double distance = (ballHeightIn - camHeightIn)/Math.tan(angleToBallRad);
            double lateralOffset = distance*Math.tan(Math.toRadians(tx));
            double fieldX = currentPose.getX()
                    + distance *Math.cos(currentPose.getHeading())
                    - lateralOffset * Math.sin(currentPose.getHeading());
            double fieldY = currentPose.getY()
                    + distance * Math.sin(currentPose.getHeading())
                    + lateralOffset * Math.cos(currentPose.getHeading());

            Pose ballPose = new Pose(fieldX, fieldY, currentPose.getHeading());

            Path pathToBall = new Path(
                    new BezierLine(
                            currentPose,
                            ballPose
                    )
            );
            pathToBall.setLinearHeadingInterpolation(
                    currentPose.getHeading(),
                    ballPose.getHeading()
            );
            follower.followPath(pathToBall, true);
        } else {
            follower.setMaxPower(0);
            transfer.intakeOff();
        }

    }
}
