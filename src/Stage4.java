import javax.swing.*;
import java.awt.*;

class Stage4 extends JPanel{
    private JLabel header, footer, navbtn01, navbtn02, navbtn03, navbtn04;
    private JPanel container, stage;
    private JPanel stretchAndSquashPanel;

    public Stage4() {
        initLabels();
        initPanels();
        setStage();
        //setFooter(); // Using new footer for 4k display - Kyle
        setContainer();
        add(container);
        setVisible(true);
    }

    private void initLabels() {
        header = new JLabel(new ImageIcon("header2.jpg"));
        footer = new JLabel(new ImageIcon("footer4-inst.jpg"));
        navbtn01 = new JLabel(new ImageIcon("btn-01-normal.jpg"));
        navbtn02 = new JLabel(new ImageIcon("btn-02-normal.jpg"));
        navbtn03 = new JLabel(new ImageIcon("btn-03-normal.jpg"));
        navbtn04 = new JLabel(new ImageIcon("btn-04-normal.jpg"));
    }

    private void initPanels() {
        container = new JPanel();
        stage = new JPanel();
    }

    private void setStage() {
        stage.setPreferredSize(new Dimension(1920, 1080));
        stage.setBackground(Color.BLACK);
        initStretchAndSquash();
        stage.add(stretchAndSquashPanel, BorderLayout.CENTER );
        stage.repaint();
    }

    private void setFooter() {
        footer.setLayout(new FlowLayout());

        footer.add(Box.createRigidArea(new Dimension(100, 85)));
        footer.add(navbtn01);
        footer.add(Box.createRigidArea(new Dimension(100, 25)));
        footer.add(navbtn02);
        footer.add(Box.createRigidArea(new Dimension(100, 25)));
        footer.add(navbtn03);
        footer.add(Box.createRigidArea(new Dimension(100, 25)));
        footer.add(navbtn04);
        footer.add(Box.createRigidArea(new Dimension(100, 25)));

        footer.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
    }

    private void setContainer() {
        container.setPreferredSize(new Dimension(1920, 1080));

        container.setBackground(Color.BLACK);

        container.setLayout(new BorderLayout());

        container.add(header, BorderLayout.NORTH);
        container.add(stage, BorderLayout.CENTER);
        container.add(footer, BorderLayout.SOUTH);

        container.setVisible(true);
    }

    private void initStretchAndSquash() {
        int width = 1280;
        int height = 800;
        int fps = 30;
        StretchAndSquash stretchAndSquashApp = new StretchAndSquash(width, height);
        stretchAndSquashPanel = stretchAndSquashApp.getJPanel();
    }
}
