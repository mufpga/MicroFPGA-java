package de.embl.rieslab.microfpga.examples;

import de.embl.rieslab.microfpga.MicroFPGAController;

import java.util.ArrayList;

/**
 * Illustrate how to query measurements from an analog input channel.
 *
 * @author Joran Deschamps
 *
 */
public class Example05_AnalogInput {

    public static void main(String[] args) {

        try {
            // connect to the FPGA by creating a controller
            MicroFPGAController controller = new MicroFPGAController(
                    0, // number of lasers
                    0,    // number of TTL signals
                    0, // number of servos
                    0,    // number of PWM signals
                    1,     // number of analog inputs (only Au and Au+)
                    false   // whether we need to change between active/passive camera trigger
            );

            if(controller.isConnected()){
                // print ID (Au, Au+ or Cu)
                System.out.println("Connected to " + controller.getID());

                // we defined only 1 analog input channel, so its id is 0
                final int aiID = 0;

                // we can query the value
                final int N = 10;
                ArrayList<Double> values = new ArrayList<>(N);
                for(int i=0; i<N; i++){
                    // query the latest known value (it is comprised between 0 and 65535)
                    int measurement = controller.getAnalogInputState(aiID);

                    // the value can be converted to pseudo-volts since 65535 corresponds to 1 V
                    double value = measurement / 65535.;

                    // add it to the list
                    values.add(value);

                    // print it
                    System.out.println(value);
                }

                // print the measurements
                System.out.println(values);

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