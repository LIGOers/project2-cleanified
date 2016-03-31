//import com.leapmotion.leap.*;
//
///**
// * Created by samanthafadrigalan on 3/31/16.
// */
//class LeapListener extends Listener{
//
//    public void onConnect(Controller controller) {
//        System.out.println("Connected");
//        controller.enableGesture(Gesture.Type.TYPE_SWIPE);
//    }
//
//    public void onFrame(Controller controller) {
//        Frame frame = controller.frame();
//
//
//
//        if(frame.gestures().count() > 0) {
//
//            System.out.println("**********************");
//            System.out.println("Wow, a gesture!");
//            Gesture gesture = frame.gestures().get(0);
//
//            System.out.println("You've made a swipe gesture!");
//            SwipeGesture swipe = new SwipeGesture(gesture);
//            Vector swipeDirection = swipe.direction();
//            System.out.println(swipeDirection.getX());
//
//            if(swipeDirection.getX() > 0) {
//                System.out.println("right");
//            }
//            else {
//                System.out.println("left");
//            }
//
//            System.out.println(gesture.type());
//            ScreenTapGesture screentap = new ScreenTapGesture(frame.gestures().get(0));
//            System.out.println(screentap.position());
//            System.out.println("********************** \n \n");
//        }
//
//
//    }
//}
