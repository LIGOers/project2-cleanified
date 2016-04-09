import javax.swing.*;
import java.awt.*;

class Stage1 extends JPanel{
    private JLabel button, stage;
    private JPanel rightPane;

    public Stage1() {
        initLabels();
        initPanels();
        setButton();
        setStage();
        setRightPane();
        add(stage, BorderLayout.NORTH);
        setVisible(true);
    }

    public void init() {
        initLabels();
        initPanels();
        setButton();
        setStage();
        setRightPane();
        add(stage, BorderLayout.NORTH);
        setVisible(true);
    }

    private void initLabels() {
        button = new JLabel(new ImageIcon("s1-btn-n1.jpg"));
        stage = new JLabel(new ImageIcon("tp1-sketch1.jpg"));
    }

    private void initPanels() {
        rightPane = new JPanel();
        rightPane.setPreferredSize(new Dimension(675, 1080));
        rightPane.setOpaque(false);
    }

    private void setButton(){
        button.setPreferredSize(new Dimension(371, 84));
    }

    private void setStage() {
        stage.setLayout(new BorderLayout());
        stage.add(rightPane, BorderLayout.EAST);
    }

    private void setRightPane() {
        rightPane.add(Box.createRigidArea(new Dimension(600, 800)));
        rightPane.add(button);
        stage.add(rightPane, BorderLayout.EAST);
    }
}
