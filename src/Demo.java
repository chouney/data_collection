import com.leapmotion.leap.Controller;

import java.io.IOException;

/**
 * Created by manatea on 2016/10/27.
 */
public class Demo {
    public static void main(String[] args){
        Controller controller = new Controller();
        controller.setPolicy(Controller.PolicyFlag.POLICY_IMAGES);
        //手势采集数据监听器
//        SubListener1 listener = new SubListener1();
//        controller.addListener(listener);
        //图像采集数据监听器
        ImageListener listener = new ImageListener();
        controller.addListener(listener);
        System.out.println("Press Enter to quit ...");
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
        controller.removeListener(listener);
    }
}
