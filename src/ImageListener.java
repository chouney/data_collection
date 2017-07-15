import com.jhlabs.image.ImageUtils;
import com.jhlabs.image.InvertFilter;
import com.jhlabs.image.PixelUtils;
import com.leapmotion.leap.*;
import com.leapmotion.leap.Frame;
import com.leapmotion.leap.Image;

import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageOutputStream;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import com.jhlabs.*;
import draw.LeapPanel;

/**
 * Created by manatea on 2016/10/27.
 */
public class ImageListener extends Listener {
    LeapPanel leapPanel;
    JFrame jf;
    public static int count = 0;

    public void onConnect(Controller controller) {
        System.out.println("Connected ");
        jf = new JFrame();
        leapPanel = new LeapPanel();
        jf.setSize(640, 240);
        jf.add(leapPanel);
        jf.setVisible(true);
        jf.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        super.onConnect(controller);
    }

    public void onImages(Controller controller) {
        Frame frame = controller.frame();
        if (frame.isValid()) {
            ImageList images = frame.images();
            Image image = images.get(0);
//            for (Image image : images) {
                byte[] data = image.data();
                int[] rgb = new int[data.length];
                for (int i = 0; i < image.width() * image.height(); i++) {
                    int r = (data[i] & 0xFF) << 16; //convert to unsigned and shift into place
                    int g = (data[i] & 0xFF) << 8;
                    int b = data[i] & 0xFF;
                    rgb[i] = r | g | b;
                }
                BufferedImage bufferedImage = (BufferedImage) leapPanel.createImage(image.width(), image.height());
                bufferedImage.setRGB(0, 0, image.width(), image.height(), rgb, 0, image.width());
                try {
                    ImageIO.write(bufferedImage,"jpg",new File("picture"+count+++".jpg"));
                    if(count == 100){
                        count = 0;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
//                System.out.println(image.width());
//                System.out.println(image.height());
                leapPanel.repaint(bufferedImage);
//            }

        }
    }

}
