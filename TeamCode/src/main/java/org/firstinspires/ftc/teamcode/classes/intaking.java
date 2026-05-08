package org.firstinspires.ftc.teamcode.classes;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class intaking{

    private DcMotor intake;
    private Servo rollerThree;
    private Servo rollerTwo;

    public void init(HardwareMap hwMap){
        intake = hwMap.get(DcMotor.class, "intake");
        rollerThree = hwMap.get(Servo.class, "rollerThree");
        rollerTwo = hwMap.get(Servo.class, "rollerTwo");
    }

    public void intakeOn(){
        intake.setPower(-1);
        rollerThree.setPosition(1);
        rollerTwo.setPosition(0);
    }

    public void intakeOff(){
        intake.setPower(0);
        rollerTwo.setPosition(0.5);
        rollerThree.setPosition(0.5);
    }

}
