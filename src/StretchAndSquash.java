//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

import processing.awt.PSurfaceAWT;
import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PSurface;
import processing.video.Capture;

import javax.swing.*;


public class StretchAndSquash extends PApplet {
    private final int WIDTH, HEIGHT;
    private int _polar = 0;
    private int _imgOp = 2;
    private boolean paused = false;
    float def_amp = 0.18F;
    private float h_plus_amp;
    private float h_cross_amp;
    private float h_frequency;
    private float h_Phase;
    private final int XRes;
    private final int YRes;
    private PImage _imgMichelson;
    private PImage _imgGrid;
    private PImage _imgTestMass;
    private PImage _imgplus;
    private PImage _imgcross;
    private PImage _imgcirc;
    private PImage _ETLogo;
    private PImage _BHAMLogo;
    private Capture _camCapture;
    private boolean _useWebCam;
    public final int Num_Processors;
    protected PImage _img;
    protected PImage _inImg;
    private GWImageDistorter imgDst;
    private float _time;
    private Object _lock;
    boolean camera_found;

    public StretchAndSquash(int width, int height) {
        this.WIDTH = width;
        this.HEIGHT = height;

        this.h_plus_amp = this.def_amp;
        this.h_cross_amp = 0.0F;
        this.h_frequency = 0.025F;
        this.h_Phase = 0.0F;
        this.XRes = WIDTH;
        this.YRes = HEIGHT;
        this._lock = new Object();
        this.camera_found = true;
        this.Num_Processors = Runtime.getRuntime().availableProcessors();
        PApplet.println("Using " + this.Num_Processors + " Processors.");
    }

    public void settings() {
        size(WIDTH, HEIGHT);
    }

    public void setup() {
        this.frameRate(25.0F);
        System.out.println(sketchPath());
        this._imgcirc = this.loadImage("circular_label.png");
        this._imgplus = this.loadImage("plus_label.png");
        this._imgcross = this.loadImage("cross_label.png");
        this._imgMichelson = this.loadImage("michelson03.png");
        this._imgTestMass = this.loadImage("testmasses2.png");
        this._imgGrid = this.loadImage("grid.png");
        this._ETLogo = this.loadImage("et.jpg");
        this._BHAMLogo = this.loadImage("bhamlogo2.jpg");

        String var1 = System.getProperty("os.name");
        if(var1.equals("Mac OS X")) {
            println("Running on OS X, checking for webcam...");

            try {
                Capture.list();
            } catch (Exception var2) {
                this.camera_found = false;
                println("Web camera not found.");
            }
        } else {
            this.camera_found = false;
        }

        if(this.camera_found) {
            println("Web camera found.");
        } else {
            this._camCapture = null;
        }

        this._img = new PImage(WIDTH, HEIGHT);
        this.imgDst = new GWImageDistorter(this, 2, this._inImg, this._img);
        this.imgDst.setImageInput(this._imgTestMass);
    }

    public void captureEvent(Capture var1) {
        if(!this.paused) {
            if(this._imgOp == 0) {
                var1.read();
            }

            if(this.imgDst != null) {
                GWImageDistorter var2 = this.imgDst;
                synchronized(this.imgDst) {
                    if(this._imgOp == 0) {
                        this.imgDst.setImageInput(var1.get());
                    }
                }
            }
        }

    }

    public void keyPressed() {
        GWImageDistorter var1 = this.imgDst;
        synchronized(this.imgDst) {
            if(this.key == 32) {
                if(this.paused) {
                    this.unpause();
                } else {
                    this.pause();
                }
            } else if(this.key == 112) {
                if(this._polar == 0) {
                    this.h_cross_amp = this.def_amp;
                    this.h_plus_amp = 0.0F;
                    this.h_Phase = 0.0F;
                    this._polar = 1;
                } else if(this._polar == 1) {
                    this.h_cross_amp = 0.75F * this.def_amp;
                    this.h_plus_amp = 0.75F * this.def_amp;
                    this.h_Phase = -1.5707964F;
                    this._polar = 2;
                } else if(this._polar == 2) {
                    this.h_cross_amp = 0.0F;
                    this.h_plus_amp = this.def_amp;
                    this.h_Phase = 0.0F;
                    this._polar = 0;
                }

                this.unpause();
            } else if(this.key == 118) {
                if(this._imgOp == 0) {
                    this._imgOp = 1;
                    this._camCapture.stop();
                    this._camCapture.dispose();
                    this.imgDst.setImageInput(this._imgMichelson);
                } else if(this._imgOp == 1) {
                    this._imgOp = 2;
                    this.imgDst.setImageInput(this._imgTestMass);
                } else if(this._imgOp == 2) {
                    this._imgOp = 3;
                    this.imgDst.setImageInput(this._imgGrid);
                } else if(this._imgOp == 3) {
                    if(!this.camera_found) {
                        this._imgOp = 1;
                        this.imgDst.setImageInput(this._imgMichelson);
                    } else {
                        println("Switching to video");
//                        this._camCapture = new Capture(this, 960, 720, 60);
                        this._camCapture = new Capture(this, (int) ((double) WIDTH * 1.25),  (int) ((double) HEIGHT * 1.25), 60);
                        this._camCapture.start();
                        this._inImg = null;
                        this._imgOp = 0;
                    }
                }

                this.unpause();
            } else if(this.key == 105) {
                ImageWorker.interpol();
                this.unpause();
            }

        }
    }

    public void pause() {
        this.paused = true;
        this.imgDst.pause();
    }

    public void unpause() {
        this.paused = false;
        this.imgDst.play();
    }

    public void draw() {
        this.background(200);
        this.noStroke();
        GWImageDistorter var1 = this.imgDst;
        synchronized(this.imgDst) {
            this.imgDst.updateImage(this._time, this.h_frequency, this.h_plus_amp, this.h_cross_amp, 0.0F, this.h_Phase);
        }

        this.image(this._img, 0.0F, 0.0F);

        this.image(this._BHAMLogo, (float)(WIDTH - this._BHAMLogo.width), (float)(HEIGHT - this._BHAMLogo.height));
        switch(this._polar) {
            case 0:
                this.image(this._imgplus, 10.0F, 10.0F);
                break;
            case 1:
                this.image(this._imgcross, 10.0F, 17.0F);
                break;
            case 2:
                this.image(this._imgcirc, 10.0F, 15.0F);
        }

        if(!this.paused) {
            this._time += 0.25F;
        }

    }

    public JPanel getJPanel() {
        JPanel frame = new JPanel();

        //create your sketch
        StretchAndSquash pt = new StretchAndSquash(WIDTH, HEIGHT);

        //get the PSurface from the sketch
        PSurface ps = pt.initSurface();

        //initialize the PSurface
        ps.setSize(WIDTH, HEIGHT);

        //get the SmoothCanvas that holds the PSurface
        PSurfaceAWT.SmoothCanvas smoothCanvas = (PSurfaceAWT.SmoothCanvas)ps.getNative();

        //SmoothCanvas can be used as a Component
        frame.add(smoothCanvas);

        //make your JFrame visible
        frame.setSize(WIDTH, HEIGHT);
        frame.setVisible(true);

        //start your sketch
        ps.startThread();

        return frame;
    }
}
