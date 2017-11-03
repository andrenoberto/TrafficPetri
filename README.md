# TrafficPetri
A Petri Net made using CPN Tools that simulates the communication between two traffic lights.

## CPN Tools

It uses CPN Tools to simulate our colored petri net.

You can download CPN Tools on it's [website][cpnwebsite].

To simulate, simply run the .cpn file and start the simulation.

## Java Project

There's also an in progress Java project that will make a communication with CPN Tools' server and will be able to show the simulation within a better GUI.

You can find the source code inside JavaPetri folder.

## Instructions

### First step

Run `JavaPetri.jar`

### Second step

Open `trafficlights.cpn` & start the simulation.

If everything is fine, the application will begin to sync it's traffic lights with the ones inside CPNTools.

Now you should see the application and CPNTools interacting.

# Features

### Menu bar
##### File opens a dropdown menu with a few options.
General keyboard shortcut `F`.
- `Open a New Connection` option closes and watches for a new connection. Keyboard shortcut `F5`.
- `Exit` option closes the application. Keyboard shortcut `CTRL + W`.
##### Help` option opens a dropdown menu with a few options.
General keyboard shortcut `H`.
- `About` option gets details about the release you're running. Keyboard shortcut `CTRL + A`.
- `Contribute` option opens a web page to pull request's section of this repo. Keyboard shortcut `CTRL + C`.
- `Report a Bug` option opens a web page to issues' section of this repo. Keyboard shortcut `CTRL + R`.
- `Help` option gets details about how to get futher information of this application. Keyboard Keyboard shortcut `F1`.

[cpnwebsite]: http://cpntools.org