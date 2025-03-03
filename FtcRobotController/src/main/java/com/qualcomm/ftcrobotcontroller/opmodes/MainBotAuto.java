package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
/**
 * Created by "M" on 2015.12.05
 */

public class MainBotAuto extends LinearOpMode {
    //TODO Merge common definitions and initializations into a single file (MainBotCORE)
    //TODO Create simple function definitions for linear movement, include in MainBotAutoCORE
    Servo   bxServo;
    DcMotor motorFR;
    DcMotor motorFL;
    DcMotor spooleo;

    @Override
    public void runOpMode() throws InterruptedException {
        bxServo = hardwareMap.servo  .get("BoxServo"  );
        motorFL = hardwareMap.dcMotor.get("FrontLeft" );
        motorFR = hardwareMap.dcMotor.get("FrontRight");
        spooleo = hardwareMap.dcMotor.get("RearSpool" );
        bxServo.setPosition(1);

        waitForStart();
        //start moving straight forward
        motorFL.setPower(0.75);
        motorFR.setPower(0.75);
        sleep(1500); //keep going straight
        motorFL.setPower(0.00); //start turning right
        sleep(500); //keep turning for half second
        motorFR.setPower(0.75); //start going straight
        sleep(3750); //keep going straight
        motorFR.setPower(0.00); //stop moving
        motorFL.setPower(0.00);
        spooleo.setPower(0.50); //start to raise box
        sleep(700); //keep raising box
        spooleo.setPower(0.00); //stop raising box
        bxServo.setPosition(0); //open box
    }
}

