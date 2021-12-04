package de.embl.rieslab.microfrpga.examples;

import de.embl.rieslab.microfpga.MicroFPGAController;
import de.embl.rieslab.microfpga.devices.CameraParameters;
import de.embl.rieslab.microfpga.devices.LaserParameters;
import de.embl.rieslab.microfpga.devices.LaserTriggerMode;

/**
 * Illustrate how to set the parameters of the camera and of the lasers
 * in active camera trigger mode.
 *
 * In active mode, the FPGA generates a camera trigger signal, as well as
 * an exposure signal. The camera trigger signal is an output of the
 * FPGA and can be directly connected to the external trigger of a camera.
 * The exposure signal generated is purely internal and is processed
 * by the laser trigger modules (see Example01 and Example02).
 *
 * In addition to the laser trigger parameters, we need to set a number of
 * camera trigger parameters:
 *  - pulse: pulse length in ms of the camera trigger signal, comprised
 *           between 0 and 6553.5 ms in steps of 0.1 ms.
 *  - period: period in ms of the camera trigger signal, comprised
 *  *         between 0 and 6553.5 ms in steps of 0.1 ms.
 *  - exposure: pulse length in ms of the exposure signal, the period is
 *              the same as the camera trigger signal, and the value is
 *              comprised between 0 and 6553.5 ms in steps of 0.1 ms.
 *  - delay: delay in ms of the exposure signal with respect to the camera
 *          trigger rising edge, comprised between 0 and 655.35 ms in steps
 *          of 0.01 ms.
 *
 *  The signals therefore look like the following:
 *                 <-------------period------------>
 *                <-pulse->
 *                ---------                        ---------      high
 *               |         |                      |         |
 *    camera -----         ------------------------         ----- low
 *
 *                  <---------exposure--------->
 *                   ---------------------------      ----------- high
 *                  |                           |    |
 *  exposure --------                           ------            low
 *               <->delay
 *
 * @author Joran Deschamps
 *
 */
public class Example03_ActiveTrigger {

    public static void main(String[] args) {

        try {
            // connect to the FPGA by creating a controller
            MicroFPGAController controller = new MicroFPGAController(
                    3, // number of lasers
                    0,    // number of TTL signals
                    0, // number of servos
                    0,    // number of PWM signals
                    0,     // number of analog inputs (only Au and Au+)
                    true   // in order to use the active camera trigger we need to use the camera module
            );

            if(controller.isConnected()){
                // print ID (Au, Au+ or Cu)
                System.out.println("Connected to " + controller.getID());

                // first we make sure that the camera trigger mode is set to active
                controller.setActiveTrigger();

                // then we create the parameters
                CameraParameters p = new CameraParameters(
                        2,      // pulse in ms length of the signal triggering the camera
                        40,   // period in ms of the signal triggering the camera
                        25.5,   // exposure in ms
                        1.2      // delay of the laser trigger signal with respect to the camera trigger signal
                );

                // they can later be changed
                p.setPulseMs(1);
                p.setPeriodMs(31.5);
                p.setExposureMs(30);
                p.setDelayMs(0.5);

                // the parameters need to be applied
                controller.setCameraTriggerParameters(p);

                // let's check that they have been set properly
                System.out.println("Camera parameters: "+controller.getCameraTriggerParametersPretty());

                // now let us create sets of parameters for the lasers
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

                // apply the laser parameters
                controller.setLaserParameters(0, rising);
                controller.setLaserParameters(1, falling);
                controller.setLaserParameters(2, following);

                // start the camera
                controller.startCamera();
                System.out.println("Camera running");

                // wait for 2 s
                // in the meantime, both camera and laser output triggers are generated
                Thread.sleep(2000);

                // stop the camera, both camera and lasers signals are now 0
                controller.stopCamera();
                System.out.println("Camera stopped");

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

