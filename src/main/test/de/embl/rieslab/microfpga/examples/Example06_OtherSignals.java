package de.embl.rieslab.microfpga.examples;

import de.embl.rieslab.microfpga.MicroFPGAController;

/**
 * Illustrate how to set the states of a TTL, a servo and a PWM signal.
 *
 * @author Joran Deschamps
 *
 */
public class Example06_OtherSignals {

    public static void main(String[] args) {

        try {
            // connect to the FPGA by creating a controller
            MicroFPGAController controller = new MicroFPGAController(
                    0, // number of lasers
                    2,    // number of TTL signals
                    2, // number of servos
                    3,    // number of PWM signals
                    0,     // number of analog inputs (only Au and Au+)
                    false   // whether we need to change between active/passive camera trigger
            );

            if(controller.isConnected()){
                // print ID (Au, Au+ or Cu)
                System.out.println("Connected to " + controller.getID());

                // the TTL signal has only two states: on (true) or off (false)
                final int ttlID = 0; // we will only use the first channel here
                boolean ttlState = controller.getTTLState(ttlID);
                System.out.println("Is TTL0 on? "+ttlState);

                // set it to the other position
                controller.setTTLState(ttlID, !ttlState);
                System.out.println("Current TTL0 state: "+controller.getTTLState(ttlID));

                // PWM signals go from 0 (0%) to 255 (100%), they can be used together
                // with a low-pass filter to create an analog signal, or simply directly
                // used with certain devices (e.g. some LEDs).
                final int pwmID = 2; // let's use the third channel here
                int pwmState = controller.getPWMState(pwmID);
                System.out.println("PWM2 state: "+pwmState);

                // let's change the value
                pwmState = (pwmState+120) % 255; // make sure that the value is not > 255 otherwise nothing will change
                controller.setPWMState(pwmID, pwmState);
                System.out.println("PWM2 state: "+controller.getPWMState(pwmID));

                // finally, servo signals are used to move servomotors and their value is
                // comprised between 0 and 65535.
                final int servoID = 1; // let's use the second channel here
                int servoState = controller.getServoState(servoID);
                System.out.println("Servo1 state: "+servoState);

                // let's change the value
                servoState = (servoState+25000) % 65535; // make sure that the value is not > 65535
                controller.setServoState(servoID, servoState);
                System.out.println("Servo1 state: "+controller.getServoState(servoID));

                // disconnect from the port
                controller.disconnect();
                System.out.println("Disconnected");

            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed to connect.");
        }
    }
}