import com.leapmotion.leap.*;
import java.io.IOException;
class Main {

    static class SampleListener extends Listener {
        int count = 0;

        public void onConnect(Controller controller) {
            System.out.println("Connected");
            controller.enableGesture(Gesture.Type.TYPE_SWIPE);
        }

        public void onFrame(Controller controller) {
//            System.out.println("Frame available");
            Frame frame = controller.frame();


//            System.out.println("Frame id: " + frame.id()
//                    + ", timestamp: " + frame.timestamp()
//                    + ", hands: " + frame.hands().count()
//                    + ", fingers: " + frame.fingers().count()
//                    + ", tools: " + frame.tools().count()
//                    + ", gestures " + frame.gestures().count());

            if(frame.gestures().count() > 0) {
                System.out.println("frame: "+ count++);

                System.out.println("**********************");
                System.out.println("Wow, a gesture!");
                Gesture gesture = frame.gestures().get(0);
                switch(gesture.type()) {
                    case TYPE_SCREEN_TAP:
                        System.out.println("You've made a screen tap gesture!");
                        break;
                    case TYPE_KEY_TAP:
                        System.out.println("You've made a key tap gesture!");
                        break;
                    case TYPE_CIRCLE:
                        System.out.println("You've made a circle gesture!");
                        break;
                    case TYPE_SWIPE:
                        System.out.println("You've made a swipe gesture!");
                        SwipeGesture swipe = new SwipeGesture(gesture);
                        Vector swipeDirection = swipe.direction();
                        System.out.println(swipeDirection.getX());
                        break;
                    default:
                        System.out.println("Broken gesture");
                        break;
                }
                System.out.println(gesture.type());
                ScreenTapGesture screentap = new ScreenTapGesture(frame.gestures().get(0));
                System.out.println(screentap.position());
                System.out.println("********************** \n \n");
            }


        }
    }

    public static void main(String[] args) throws java.net.SocketException, InterruptedException  {

        new MainFrame();


        // Create a sample listener and controller
//        SampleListener listener = new SampleListener();
//        Controller controller = new Controller();
//
//        // Have the sample listener receive events from the controller
//        controller.addListener(listener);
//
//        // Keep this process running until Enter is pressed
//        System.out.println("Press Enter to quit...");
//        try {
//            System.in.read();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        // Remove the sample listener when done
//        controller.removeListener(listener);
    }
}
