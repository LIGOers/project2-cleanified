import javax.swing.*;
import java.awt.*;
import java.io.IOException;

import com.illposed.osc.*;
import com.leapmotion.leap.*;
import com.leapmotion.leap.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
/**
 * Created by samanthafadrigalan on 3/14/16.
 */
public class MainFrame extends JFrame{
    private int currentStage;
    private static JPanel rootPanel;
    private String TITLE = "LIGO";
    private final int WINDOW_WIDTH = 1024, WINDOW_HEIGHT = 768;
    private final int BUFFER_WIDTH = 1024, BUFFER_HEIGHT = 170;
    private JPanel stage1, stage2, stage3, stage4;
    private int receiverPort = 8000;
    private OSCPortIn receiver;
    private int swipeCount;
    private long lastLeapSwipeTime, leapTimeNow;
    private float swipeDirection;
    private Controller controller;
    private final int LEAP_TIMER_DELAY = 10;

    public MainFrame() throws java.net.SocketException, InterruptedException{
        buildMainFrame();
        buildBuffer();
        buildRootPanel();
        initStages();
        setStartScreen();
        setOSCHandlers();
        setLeapController();
    }

    private void buildMainFrame() {
        setTitle(TITLE);
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
    }

    private void buildBuffer() {
        JPanel buffer = new JPanel();
        buffer.setPreferredSize(new Dimension(BUFFER_WIDTH, BUFFER_HEIGHT));
        buffer.setBackground(Color.black);
        add(buffer, BorderLayout.SOUTH);
    }

    private void buildRootPanel() {
        rootPanel = new JPanel();
        rootPanel.setLayout(new OverlayLayout(rootPanel));
    }

    private void initStages() {
        stage1 = new Stage1();
        stage2 = new Stage2();
        stage3 = new Stage3();
        stage4 = new Stage4();
    }

    private void setStartScreen() {
        rootPanel.add(stage1, BorderLayout.NORTH);
        rootPanel.add(stage2, BorderLayout.NORTH);
        rootPanel.add(stage3, BorderLayout.NORTH);
        rootPanel.add(stage4, BorderLayout.NORTH);

        stage2.setVisible(false);
        stage3.setVisible(false);
        stage4.setVisible(false);
        currentStage = 1;

        add(rootPanel);

        setResizable(false);

        setVisible(true);
    }

    private void setOSCHandlers() throws java.net.SocketException {
        receiver = new OSCPortIn(receiverPort);
        receiver.addListener("/1/push1", OSChandler(true, false, false, false));
        receiver.addListener("/1/push2", OSChandler(false, true, false, false));
        receiver.addListener("/1/push3", OSChandler(false, false, true, false));
        receiver.addListener("/1/push4", OSChandler(false, false, false, true));
        receiver.startListening();
        System.out.println("Server is listening on port " + receiverPort + "...");
    }

    private OSCListener OSChandler(boolean isStage1Visible, boolean isStage2Visible, boolean isStage3Visible, boolean isStage4Visible) {
        return new OSCListener() {
            public void acceptMessage(java.util.Date time, OSCMessage message) {
                changeScreen(isStage1Visible, isStage2Visible, isStage3Visible, isStage4Visible);
            }
        };
    }

    private void changeScreen(boolean isStage1Visible, boolean isStage2Visible, boolean isStage3Visible, boolean isStage4Visible) {
        stage1.setVisible(isStage1Visible);
        stage2.setVisible(isStage2Visible);
        stage3.setVisible(isStage3Visible);
        stage4.setVisible(isStage4Visible);

        if(isStage1Visible) {
            currentStage = 1;
        }
        else if(isStage2Visible) {
            currentStage = 2;
        }
        else if(isStage3Visible) {
            currentStage = 3;
        }
        else {
            currentStage = 4;
        }
    }

    private void setLeapController() {
        controller = new Controller();
        connectLeap();
        startLeapControl();
    }

    private void connectLeap() {
        final Timer timer = new Timer(LEAP_TIMER_DELAY, new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                System.out.println("not connected");
                if(controller.isConnected()) {
                    System.out.println("connected!");
                    controller.enableGesture(Gesture.Type.TYPE_SWIPE);
                    ((Timer)evt.getSource()).stop();
                }
            }
        });
        timer.start();
    }

    private void startLeapControl() {
        swipeCount = 0;
        lastLeapSwipeTime = 0;
        Timer detectLeapGestures = new Timer(LEAP_TIMER_DELAY, new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                Frame frame = controller.frame();
                if(isLeapGestureDetected(frame)) {
                    swipeCount++;
                    Gesture gesture = frame.gestures().get(0);
                    SwipeGesture swipe = new SwipeGesture(gesture);
                    swipeDirection = swipe.direction().getX();
                    if(isLeapSwipeFarEnoughFromLastScreenChange()) {
                        doLeapScreenChange();
                        outputLeapConsoleLog();
                        lastLeapSwipeTime = System.currentTimeMillis();
                    }
                    else{
                        outputLeapConsoleLog();
                    }
                }
            }
        });
        detectLeapGestures.start();
    }

    private void doLeapScreenChange() {
        if(swipeDirection > 0) {
            swipeLeftScreen();
        }
        else {
            swipeRightScreen();
        }
    }

    private boolean isLeapGestureDetected(Frame frame) {
        return frame.gestures().count() > 0;
    }

    private boolean isLeapSwipeFarEnoughFromLastScreenChange() {
        leapTimeNow = System.currentTimeMillis();
        return (leapTimeNow - lastLeapSwipeTime) > 200;
    }

    private void outputLeapConsoleLog() {
        System.out.println("\n**********************");
        System.out.println("Register LEAP CONTROL [" + swipeCount + "]");
        System.out.print("SWIPE DIRECTION: ");
        if(isLeapSwipeFarEnoughFromLastScreenChange()) {
            if (swipeDirection > 0) {
                System.out.println("RIGHT");
            }
            else{
                System.out.println("LEFT");
            }
        }
        else {
            System.out.println("NONE - Too soon from last swipe");
        }
        System.out.println("**********************\n");
    }

    private void swipeLeftScreen() {
        switch(currentStage) {
            case 1:
                changeScreen(false, false, false, true);
                break;
            case 2:
                changeScreen(true, false, false, false);
                break;
            case 3:
                changeScreen(false, true, false, false);
                break;
            case 4:
                changeScreen(false, false, true, false);
                break;
            default:
                System.err.println("Invalid Stage number");
                break;
        }
    }

    private void swipeRightScreen() {
        switch(currentStage) {
            case 1:
                changeScreen(false, true, false, false);
                break;
            case 2:
                changeScreen(false, false, true, false);
                break;
            case 3:
                changeScreen(false, false, false, true);
                break;
            case 4:
                changeScreen(true, false, false, false);
                break;
            default:
                System.err.println("Invalid Stage number");
                break;
        }
    }
}