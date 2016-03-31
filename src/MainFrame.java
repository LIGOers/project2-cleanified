import javax.swing.*;
import java.awt.*;
import com.illposed.osc.*;

/**
 * Created by samanthafadrigalan on 3/14/16.
 */
public class MainFrame extends JFrame{
    private static JPanel rootPanel;
    private String TITLE = "LIGO";
    private int currentStage;
    private boolean fistBuild;
    private final int WINDOW_WIDTH = 1024, WINDOW_HEIGHT = 768;
    private final int BUFFER_WIDTH = 1024, BUFFER_HEIGHT = 170;
    private JPanel stage1, stage2, stage3, stage4;
    private int receiverPort = 8000;
    private OSCPortIn receiver;

    public MainFrame() throws java.net.SocketException {
        buildMainFrame();
        buildBuffer();
        buildRootPanel();
        initStages();
        setStartScreen();
        setOSCHandlers();
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
                stage1.setVisible(isStage1Visible);
                stage2.setVisible(isStage2Visible);
                stage3.setVisible(isStage3Visible);
                stage4.setVisible(isStage4Visible);
            }
        };
    }
}
