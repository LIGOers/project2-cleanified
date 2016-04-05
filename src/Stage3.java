import javax.swing.*;
import java.awt.*;

class Stage3 extends JPanel{
    private JLabel header, footer, navbtn01, navbtn02, navbtn03, navbtn04, stage3txt, stagemap;
    private JPanel container, stage;

    public Stage3() {
        initLabels();
        initPanels();
        setStage();
        setFooter();
        setContainer();
        add(container);
        setVisible(true);
    }

    private void initLabels() {
        header = new JLabel(new ImageIcon("header.jpg"));
        footer = new JLabel(new ImageIcon("footer.jpg"));
        navbtn01 = new JLabel(new ImageIcon("btn-01-normal.jpg"));
        navbtn02 = new JLabel(new ImageIcon("btn-02-normal.jpg"));
        navbtn03 = new JLabel(new ImageIcon("btn-03-normal.jpg"));
        navbtn04 = new JLabel(new ImageIcon("btn-04-normal.jpg"));
        stage3txt = new JLabel(new ImageIcon("stagethree-txt.jpg"));
        stagemap = new JLabel(new ImageIcon("map.jpg"));
    }

    private void initPanels() {
        container = new JPanel();
        stage = new JPanel();
    }

    private void setStage() {
        stage.setPreferredSize(new Dimension(1024, 436));
        stage.setBackground(Color.BLACK);
        stage.add(stage3txt);
        stage.add(stagemap);
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
        container.setPreferredSize(new Dimension(1024, 576));

        container.setBackground(Color.BLACK);

        container.setLayout(new BorderLayout());

        container.add(header, BorderLayout.NORTH);
        container.add(stage, BorderLayout.CENTER);
        container.add(footer, BorderLayout.SOUTH);

        container.setVisible(true);
    }
}
