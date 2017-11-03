package com.main;

import com.trafficlights.TrafficLights;

import java.io.IOException;

public class Main {
    public static class TrafficLightsRunnable implements Runnable {
        @Override
        public void run() {
            TrafficLights trafficLights = new TrafficLights(false);
            trafficLights.setContentPane(trafficLights.getMainPanel());
            trafficLights.pack();
            trafficLights.setVisible(true);
            trafficLights.get_fOpenClose().setEnabled(false);
            /*
                CPNTools communication stuff
             */
            trafficLights.setTrafficLightsToDefault();
            while (trafficLights.isApplicationRunning()) {
                try {
                    trafficLights.getCPNToolsObject().accept(9000);
                    trafficLights.setConnectedToCPN(true);
                    trafficLights.get_fOpenClose().setEnabled(true);
                    trafficLights.setStatusMessageLabel("connected to CPNTools.");
                } catch (IOException e) {
                    trafficLights.setStatusMessageLabel("could not connect/disconnect CPNTools to this application.");
                }

                while (trafficLights.getConnectedToCPN()) {
                    trafficLights.communicateWithCPNTools();
                }
            }
        }
    }

    public static void main(String[] args) {
        Thread thread = new Thread(new TrafficLightsRunnable());
        thread.start();
    }
}
