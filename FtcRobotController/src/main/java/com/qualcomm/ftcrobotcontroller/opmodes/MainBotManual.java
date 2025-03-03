package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;
/**
 * Created by "M" on 2015.10.31
 */

public class MainBotManual extends OpMode {
    float powerScale (float initialPower) //Nonlinear scaling from PushBotHardware
    {
        // Remove illegal powers.
        float legalPower = Range.clip (initialPower, -1, 1);

        float[] scale =
                {
                        0.00f, 0.00f, 0.00f, 0.00f, 0.00f, 0.00f,
                        0.18f, 0.24f, 0.30f, 0.36f, 0.43f, 0.50f,
                        0.60f, 0.72f, 0.85f, 1.00f, 1.00f
                };

        // Get the corresponding index for the legalised power.
        int indexValue = (int)(legalPower * 16.0);
        if (indexValue < 0) { indexValue = -indexValue; }

        //Scale and return power.
        float finalPower;
        if (legalPower < 0) { finalPower = -scale[indexValue]; }
        else { finalPower = scale[indexValue]; }
        return finalPower;
    }
    DcMotor motorFR; //Motors for the drive system
    DcMotor motorFL;
    DcMotor motorRR;
    DcMotor motorRL;
    DcMotor spinner; //Front spinner motor
    DcMotor spooleo; //Pun on Romeo, spool motor
    Servo   bxServo; //"o"
    Servo   spoolEX;

    public MainBotManual(){}

    @Override public void init() {
        //initialize servo and motors
        bxServo = hardwareMap.servo  .get("BoxServo"  );
        spoolEX = hardwareMap.servo  .get("SrvoExtend");
        motorFL = hardwareMap.dcMotor.get("FrontLeft" );
        motorFR = hardwareMap.dcMotor.get("FrontRight");
        motorRL = hardwareMap.dcMotor.get("RearLeft"  );
        motorRR = hardwareMap.dcMotor.get("RearRight" );
        spinner = hardwareMap.dcMotor.get("FrontFlap" );
        spooleo = hardwareMap.dcMotor.get("RearSpool" );

        //reverse right type motors
        motorFR.setDirection(DcMotor.Direction.REVERSE);
        motorRR.setDirection(DcMotor.Direction.REVERSE);
        spinner.setDirection(DcMotor.Direction.REVERSE);
    }

    @Override public void loop() {
        //Analog control definition section
        float powerFL = powerScale(gamepad1.left_stick_y );
        float powerFR = powerScale(gamepad1.right_stick_y);
        float powerRL = gamepad1.left_trigger - gamepad1.right_trigger;
        float powerRR = gamepad1.left_trigger - gamepad1.right_trigger;
        float spoolPw = gamepad2.left_trigger - gamepad2.right_trigger;

        //Digital controlled section
        if (gamepad1.left_bumper) { motorRL.   setPower( 0.1); }
        if (gamepad1.right_bumper){ motorRR.   setPower( 0.1); }
        if      (gamepad2.a)      { spinner.   setPower( 1.0); }
        else if (gamepad2.b)      { spinner.   setPower(-1.0); }
        else                      { spinner.   setPower( 0.0); }
        if (gamepad2.right_bumper){ bxServo.setPosition( 0.0); }
        else                      { bxServo.setPosition( 1.0); }
        if      (gamepad2.x)      { spoolEX.setPosition( 0.0); }
        else if (gamepad2.y)      { spoolEX.setPosition( 1.0); }
        else                      { spoolEX.setPosition( 0.5); }

        //Analog controlled section
        motorRL.setPower(powerRL);
        motorRR.setPower(powerRR);
        motorFL.setPower(powerFL);
        motorFR.setPower(powerFR);
        spooleo.setPower(spoolPw);

        telemetry.addData("Front Power", powerFL);
        telemetry.addData("RearL Power", powerRL);
        telemetry.addData("RearR Power", powerRR);
        telemetry.addData("Spool Power", spoolPw);
    }
}