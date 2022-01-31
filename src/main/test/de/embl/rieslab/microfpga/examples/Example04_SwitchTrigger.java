package de.embl.rieslab.microfpga.examples;

import de.embl.rieslab.microfpga.MicroFPGAController;
import de.embl.rieslab.microfpga.devices.CameraParameters;
import de.embl.rieslab.microfpga.devices.LaserParameters;
import de.embl.rieslab.microfpga.devices.LaserTriggerMode;

/**
 * Illustrate how to switch camera trigger mode.
 *
 * @author Joran Deschamps
 *
 */
public class Example04_SwitchTrigger {

    public static void main(String[] args) {

        try {
            // connect to the FPGA by creating a controller
            MicroFPGAController controller = new MicroFPGAController(
                    1, // number of lasers
                    0,    // number of TTL signals
                    0, // number of servos
                    0,    // number of PWM signals
                    0,     // number of analog inputs (only Au and Au+)
                    true   // we need the camera in order to switch modes
            );

            if(controller.isConnected()){
                // print ID (Au, Au+ or Cu)
                System.out.println("Connected to " + controller.getID());

                // first we make sure that the camera trigger mode is set to active
                controller.setActiveTrigger();

                // then we create the parameters
                CameraParameters p = new CameraParameters(
                        1,      // pulse in ms length of the signal triggering the camera
                        31.5,   // period in ms of the signal triggering the camera
                        30,   // exposure in ms
                        0.5      // delay of the laser trigger signal with respect to the camera trigger signal
                );

                // the parameters need to be applied
                controller.setCameraTriggerParameters(p);

                // let's check that they have been set properly
                System.out.println("Camera parameters: "+controller.getCameraTriggerParametersPretty());

                // now let us create parameters for the laser
                final int laserID = 0;
                LaserParameters rising = new LaserParameters(
                        LaserTriggerMode.RISING,    // there are five modes: On, Off, Rising, Falling and Following
                        2000,               // the duration is in us so 1000 us = 1ms
                        "1010101010101010"  // the sequence is 16 bits and corresponds to on or off frames
                );

                // apply the laser parameters
                controller.setLaserParameters(laserID, rising);

                // start the camera
                controller.startCamera();
                System.out.println("Camera running");

                // wait for 2 s
                // in the meantime, both camera and laser output triggers are generated
                Thread.sleep(2000);

                // stop the camera, both camera and lasers signals are now 0
                controller.stopCamera();
                System.out.println("Camera stopped");

                // we can set the camera trigger mode to passive
                System.out.println("Active camera trigger mode: "+controller.isActiveTrigger());
                controller.setPassiveTrigger();
                System.out.println("Active camera trigger mode: "+controller.isActiveTrigger());

                // now the lasers are only triggered if an external camera trigger is given in
                // input to the FPGA
                Thread.sleep(500);

                // if we switch back to active trigger, the previous camera parameters are still in effect
                controller.setActiveTrigger();
                System.out.println("Active camera trigger mode: "+controller.isActiveTrigger());

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