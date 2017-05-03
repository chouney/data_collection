import com.leapmotion.leap.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by manatea on 2016/10/27.
 */
public class SubListener extends Listener {
    private Frame pastFrame;
    private Frame currentFrame;
    private List<Vector> result = new ArrayList<Vector>();
    private List<Integer> resultx = new ArrayList<Integer>();
    private List<Integer> resulty = new ArrayList<Integer>();
    private List<Integer> resultz = new ArrayList<Integer>();

    public void onConnect(Controller controller) {
        System.out.println("Connected ");
        super.onConnect(controller);
    }

    public void onFrame(Controller controller) {
        super.onFrame(controller);
        if (ThreadLocalRandom.current().nextInt(100) < 50) {
            currentFrame = controller.frame();
   /*         HandList hands = currentFrame.hands();
            Hand hand = hands.rightmost();
            Vector palmPos = hand.palmPosition();
            Vector palmNomarl = hand.palmNormal().times(100);
        int palmNomarlXor = (int)palmNomarl.getX()^(int)palmNomarl.getY()^(int)palmNomarl.getZ();
            Vector palmVelocity = hand.palmVelocity().divide(100).plus(new Vector(16,16,16));
//        int palmVelocityXor = (int)palmVelocity.getX()^(int)palmVelocity.getY()^(int)palmVelocity.getZ();
        float handRadius = hand.sphereRadius()/10.0f;
            FingerList fingers = hand.fingers();
            Finger f1 = fingers.get(0);
            Finger f2 = fingers.get(1);
            Finger f3 = fingers.get(2);
            Finger f4 = fingers.get(3);
            Finger f5 = fingers.get(4);
            System.out.println("掌心法向量：" + palmNomarl + " ，掌心移动速度：" + palmVelocity +
                    " ，手球半径：" + handRadius + " ，五指是否伸展：" + f1.isExtended() + "" + f2.isExtended() + "" + f3.isExtended() + "" +
                    f4.isExtended() + "" + f5.isExtended());*/
            pastFrame = controller.frame(10);
            showHandAndFingerInfo(currentFrame.hands());
        }
    }


    public void showHandAndFingerInfo(HandList hands) {
        Hand hand = hands.rightmost();
        Vector vector = hand.palmVelocity();
        if (hand.grabStrength() < 0.3 && (Math.abs(vector.getX()) > 100 || Math.abs(vector.getY()) > 100 || Math.abs(vector.getZ()) > 100)) {
            showFingerDisPlace(hand.fingers());
        } else {
            if (!result.isEmpty()) {
                importAndExportInteger("crosstest");
//                importAndExportVector(result,"test2.txt");
                result.clear();
            }
        }
//        showFingerDisPlace(hand.fingers());
    }

    public void showFingerDisPlace(FingerList fingers) {
        Finger ermuzhi = fingers.get(2);
        Vector pastErMuZhi = pastFrame.fingers().get(2).tipPosition();
        float x, y, z;
        x = ermuzhi.tipPosition().getX() - pastErMuZhi.getX();
        y = ermuzhi.tipPosition().getY() - pastErMuZhi.getY();
        z = ermuzhi.tipPosition().getZ() - pastErMuZhi.getZ();
        Vector out = new Vector(x, y, z);
        System.out.println(out);
        result.add(out);
    }

    public void importAndExportInteger(String baseFileName) {
        try {
            File filex = new File(baseFileName + "x");
            File filey = new File(baseFileName + "y");
            File filez = new File(baseFileName + "z");
            RandomAccessFile outx = new RandomAccessFile(filex, "rw");
            RandomAccessFile outy = new RandomAccessFile(filey, "rw");
            RandomAccessFile outz = new RandomAccessFile(filez, "rw");
            outx.seek(filex.length());
            outy.seek(filey.length());
            outz.seek(filez.length());
            StringBuilder strx = new StringBuilder();
            StringBuilder stry = new StringBuilder();
            StringBuilder strz = new StringBuilder();
            for (Vector vec : result) {
                strx.append(vec.getX()).append(" ; ");
                stry.append(vec.getY()).append(" ; ");
                strz.append(vec.getZ()).append(" ; ");

            }
            strx.append("\n");
            stry.append("\n");
            strz.append("\n");
            outx.writeBytes(strx.toString());
            outy.writeBytes(stry.toString());
            outz.writeBytes(strz.toString());
            outx.close();
            outy.close();
            outz.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void importAndExportVector(List<Vector> result, String fileName) {
        try {
            File file = new File(fileName);
//            BufferedReader in =  new InputStreamReader(new FileInputStream(file)));
//            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
            RandomAccessFile out = new RandomAccessFile(file, "rw");
            out.seek(file.length());
            StringBuilder str = new StringBuilder();
            for (Vector vec : result) {
                str.append(" [ ").append(vec.getX()).append(" ")
                        .append(vec.getY()).append(" ")
                        .append(vec.getZ()).append(" ] ;");
            }
            str.append("\n");
            out.writeBytes(str.toString());
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
