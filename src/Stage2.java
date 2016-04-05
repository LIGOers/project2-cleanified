import javax.swing.*;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import java.awt.*;
import java.io.File;
import java.lang.*;

/**
 * Created by samanthafadrigalan on 3/14/16.
 */
class Stage2 extends JPanel{
    private JLabel header, footer, navbtn01, navbtn02, navbtn03, navbtn04, stagetwotxt;
    private JPanel container, stage;
    
    public Stage2() {
        initLabels();
        initPanels();
        setStage();
        //setFooter();  // Using new footer for 4k display - Kyle
        setContainer();
        add(container);
        setVisible(true);
    }

    private void initLabels() {
        header = new JLabel(new ImageIcon("header.jpg"));
        footer = new JLabel(new ImageIcon("footer2.jpg"));
        navbtn01 = new JLabel(new ImageIcon("btn-01-normal.jpg"));
        navbtn02 = new JLabel(new ImageIcon("btn-02-normal.jpg"));
        navbtn03 = new JLabel(new ImageIcon("btn-03-normal.jpg"));
        navbtn04 = new JLabel(new ImageIcon("btn-04-normal.jpg"));
        stagetwotxt = new JLabel(new ImageIcon("01-image-text.jpg"));
    }

    private void initPanels() {
        container = new JPanel();
        stage = new JPanel();
    }

    private void setStage() {
        stage.setPreferredSize(new Dimension(1920, 700));
        stage.setBackground(Color.BLACK);
        initVideoPlayer();
        stage.add(stagetwotxt);
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

    private void initVideoPlayer() {
        JFXPanel fxPanel = new JFXPanel();

        stage.add(fxPanel);

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                initFX(fxPanel);
            }
        });
    }

    private static void initFX(JFXPanel fxPanel) {
        // This method is invoked on the JavaFX thread
        Scene scene = createScene();
        fxPanel.setScene(scene);
    }

    private static Scene createScene() {
        String workingDir = System.getProperty("user.dir");
        final File f = new File(workingDir, "video2.mp4");

        final Media m = new Media(f.toURI().toString());
        final MediaPlayer mp = new MediaPlayer(m);
        final MediaView mv = new MediaView(mp);

        final DoubleProperty width = mv.fitWidthProperty();
        final DoubleProperty height = mv.fitHeightProperty();

        width.bind(Bindings.selectDouble(mv.sceneProperty(), "width"));
        height.bind(Bindings.selectDouble(mv.sceneProperty(), "height"));

        mv.setPreserveRatio(true);


        StackPane root = new StackPane();
        root.getChildren().add(mv);

        Scene scene = new Scene(root, 900, 800);
        scene.setFill(javafx.scene.paint.Color.BLACK);

        mp.setCycleCount(MediaPlayer.INDEFINITE);

        mp.play();

        return (scene);
    }
}