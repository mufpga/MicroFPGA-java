package de.embl.rieslab.microfpga.examples;

import de.embl.rieslab.microfpga.MicroFPGAController;
import de.embl.rieslab.microfpga.devices.LaserParameters;
import de.embl.rieslab.microfpga.devices.LaserTriggerMode;

/**
 * Illustrate how to set the parameters of three lasers in passive camera
 * trigger mode.
 *
 * See Example01_SingleLaser for a description of the laser triggering.
 *
 * @author Joran Deschamps
 *
 */
public class Example02_MultipleLasers {

    public static void main(String[] args) {

        try {
            // connect to the FPGA by creating a controller
            MicroFPGAController controller = new MicroFPGAController(
                    3, // number of lasers
                    0,    // number of TTL signals
                    0, // number of servos
                    0,    // number of PWM signals
                    0,     // number of analog inputs (only Au and Au+)
                    false   // whether we need to change between active/passive camera synchronisation
            );

            if(controller.isConnected()){
                // print ID (Au, Au+ or Cu)
                System.out.println("Connected to " + controller.getID());

                // let's see what the three laser current states are
                System.out.println("Laser0: "+controller.getLaserParametersPretty(0));
                System.out.println("Laser1: "+controller.getLaserParametersPretty(1));
                System.out.println("Laser2: "+controller.getLaserParametersPretty(2));

                // now let us create sets of parameters
                LaserParameters rising = new LaserParameters(
                        LaserTriggerMode.RISING,    // there are five modes: On, Off, Rising, Falling and Following
                        2000,               // the duration is in us so 1000 us = 1ms
                        "1010101010101010"  // the sequence is 16 bits and corresponds to on or off frames
                );
                LaserParameters falling = new LaserParameters(
                        LaserTriggerMode.FALLING,
                        2000,
                        "0101010101010101"
                );
                LaserParameters following = new LaserParameters(
                        LaserTriggerMode.FOLLOWING,
                        0,                  // this value has no impact in FOLLOWING mode
                        "1100110011001100"
                );


                // apply the parameters
                controller.setLaserParameters(0, rising);
                controller.setLaserParameters(1, falling);
                controller.setLaserParameters(2, following);

                // and we check the current state
                System.out.println("Laser0: "+controller.getLaserParametersPretty(0));
                System.out.println("Laser1: "+controller.getLaserParametersPretty(1));
                System.out.println("Laser2: "+controller.getLaserParametersPretty(2));

                // disconnect from the port
                controller.disconnect();
                System.out.println("Disconnected");

                // even after disconnection, if it is powered, the FPGA will continue to generate the
                // laser trigger signals given the last known parameters and the external camera trigger.
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed to connect.");
        }
    }
}
