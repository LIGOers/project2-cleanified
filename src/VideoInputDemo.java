import com.sleepingdumpling.jvideoinput.VideoFrame;
import com.sleepingdumpling.jvideoinput.VideoInput;
import com.sleepingdumpling.jvideoinput.VideoInputException;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Transparency;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.awt.image.WritableRaster;
import java.text.DecimalFormat;
import java.util.concurrent.locks.LockSupport;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class VideoInputDemo extends JPanel {

    private BufferedImage displayImage;
    private double videoFps;
    private boolean mirrorImage = true;
    private DecimalFormat fpsFormat;
    private final static long ONE_SECOND_IN_NANOS = 1000000000L;

    public VideoInputDemo(int width, int height, final int fps) {
        System.out.println("in here");
        this.fpsFormat = new DecimalFormat("#0.0");
        this.setPreferredSize(new Dimension(width, height));
        this.startRetrieverThread(width, height, fps);
    }

    private void startRetrieverThread(final int width, final int height, final int fps) {
        Thread videoFrameRetrieverThread = new Thread() {

            public void run() {
                retrieveAndDisplay(width, height, fps);
            }
        };
        videoFrameRetrieverThread.start();

    }

    private void retrieveAndDisplay(int width, int height, int fps) {
        try {
            final VideoInput videoInput = new VideoInput(width, height);
            final long interval = ONE_SECOND_IN_NANOS / fps;
            long lastReportTime = -1;
            long imgCnt = 0;

            while (true) {
                long start = System.nanoTime();
                try {
                    VideoFrame vf = videoInput.getNextFrame(null);
                    if (vf != null) {
                        this.displayImage = getRenderingBufferedImage(vf);
                        imgCnt++;

                        long now = System.nanoTime();
                        if (lastReportTime != -1 &&  now - lastReportTime >= ONE_SECOND_IN_NANOS) {
                            videoFps = ((double)imgCnt*ONE_SECOND_IN_NANOS)/(now - lastReportTime);
                            imgCnt = 0;
                            lastReportTime = now;
                        }
                        else if (lastReportTime == -1) {
                            lastReportTime = now;
                        }

                        SwingUtilities.invokeAndWait(new Runnable() {
                            public void run() {
                                paintImmediately(0, 0, getWidth(), getHeight());
                            }
                        });
                    }
                }
                catch (Exception ex) {
                    ex.printStackTrace();
                }
                finally {
                    long end = System.nanoTime();
                    long waitTime = interval - (end - start);
                    if (waitTime > 0) {
                        LockSupport.parkNanos(waitTime);
                    }
                }
            }
        }
        catch (VideoInputException e1) {
            e1.printStackTrace();
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (displayImage != null) {
            Graphics2D g2 = (Graphics2D) g.create();
            if (this.mirrorImage) {
                //draw image, mirror it...
                AffineTransform xform = AffineTransform.getTranslateInstance(displayImage.getWidth(),0);
                xform.scale(-1, 1);
                g2.drawImage(displayImage, xform, null);
            }
            else {
                g2.drawImage(displayImage, 0, 0, null);
            }

            String fpsString = this.fpsFormat.format(this.videoFps) + " fps";
            int textWidth = (int) g2.getFontMetrics().getStringBounds(fpsString, g2).getWidth();
            g2.setColor(Color.BLACK);
            g2.drawString(fpsString, getWidth() - textWidth - 5, getHeight() - 5);
            g2.setColor(Color.ORANGE);
            g2.drawString(fpsString, getWidth() - textWidth - 6, getHeight() - 6);
            g2.dispose();
        }
    }

    public void setMirror(boolean mirror) {
        this.mirrorImage = mirror;
        this.repaint();
    }

    public boolean isMirror() {
        return this.mirrorImage;
    }

    public BufferedImage getRenderingBufferedImage(VideoFrame videoFrame) {
        GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
        BufferedImage img = gc.createCompatibleImage(videoFrame.getWidth(), videoFrame.getHeight(), Transparency.TRANSLUCENT);
        if (img.getType() == BufferedImage.TYPE_INT_ARGB
                || img.getType() == BufferedImage.TYPE_INT_ARGB_PRE
                || img.getType() == BufferedImage.TYPE_INT_RGB) {
            WritableRaster raster = img.getRaster();
            DataBufferInt dataBuffer = (DataBufferInt) raster.getDataBuffer();

            byte[] data = videoFrame.getRawData();
            addAlphaChannel(data, data.length, dataBuffer.getData());
            return img; //convert the data ourselves, the performance is much better
        }
        else {
            return videoFrame.getBufferedImage(); //much slower when drawing it on the screen.
        }
    }

    private void addAlphaChannel(byte[] rgbBytes, int bytesLen, int[] argbInts) {
        for (int i = 0, j = 0; i < bytesLen; i += 3, j++) {
            argbInts[j] = ((byte) 0xff) << 24 			|		// Alpha
                    (rgbBytes[i] << 16) & (0xff0000) 	|		// Red
                    (rgbBytes[i + 1] << 8) & (0xff00) 	|		// Green
                    (rgbBytes[i + 2]) & (0xff); 				// Blue
        }
    }
}