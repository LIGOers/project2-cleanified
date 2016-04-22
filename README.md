# LIGO Interactive Presentation
This is our project for our CSC 4243 (Interaction Design) class.
## Notes
### Stretch and Squash
We integrated the Stretch and Squash application to our interactive LIGO presentation. The [Stretch and Squash application](http://www.gwoptics.org/processing/stretch_and_squash/stretchandsquash.php) was built at University of Birmingham, UK.
We manipulated the code to work with [Processing 3](https://processing.org). The Stretch and Squash application was embedded to our main Swing application using the `getJPanel` function we added in StretchAndSquash.java.
The video input for Processing 3 is using [GStreamer](https://gstreamer.freedesktop.org). To run on OS X, set the following classpath parameters to the appropriate plugin and native libraries location.
````````````````````````
-Dgstreamer.library.path
-Dgstreamer.plugin.path
````````````````````````
The gstreamer natives are included to this project under `gstreamerlibraries`.