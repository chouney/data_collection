package draw;

import com.leapmotion.leap.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.applet.Applet;
import java.awt.*;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;
import java.io.File;
import java.io.IOException;

/**
 * Created by manatea on 2017/5/16.
 */
public class LeapPanel extends JPanel{
    private BufferedImage image;
    public void repaint(BufferedImage image){
        super.repaint();
        this.image = image;
    }
    /**
     * repaint方法会调用paint方法，并自动获得Graphics对像
     * 然后可以用该对像进行2D画图
     * 注：该方法是重写了JPanel的paint方法
     */
    public void paint(Graphics g)  {
        super.paint(g);
        g.drawImage(image,0,0,null);

    }

}
