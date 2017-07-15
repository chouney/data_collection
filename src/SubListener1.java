import com.leapmotion.leap.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by manatea on 2016/10/27.
 */
public class SubListener1 extends Listener {
    private Frame preFrame;
    private Frame currentFrame;
    private final static int DELTA = 5;
    private final static int THRESHOLD = 0;
    private final static int BUFFER =1000;
    private int count = 91;
    private int buffer = 0;
    private List<double[]> result = new ArrayList<double[]>();

    public void onConnect(Controller controller) {
        System.out.println("Connected ");
        super.onConnect(controller);
    }

    public void onFrame(Controller controller) {
        super.onFrame(controller);
        currentFrame = controller.frame();
        preFrame = controller.frame(DELTA);
        showHandAndFingerInfo(currentFrame.hands(), preFrame.hands());
    }


    public  void showHandAndFingerInfo(HandList currentHands, HandList preHands) {
        Hand hand = currentHands.rightmost();
        Hand prehand = preHands.rightmost();
        double velocity = getVelocity(hand, prehand);
        double directionRate = getDirectionRate(hand,prehand);
        if ((velocity > 10d ||  directionRate > 4d) &&  hand.grabStrength() <0.01) {
            synchronized (this) {
                System.out.println(directionRate + "  " + velocity);
                buildFeatureVector(hand);
            }
        } else {
            if (currentHands.count()==2&& !result.isEmpty()|| buffer++ >BUFFER && !result.isEmpty()) {
                synchronized (this) {
                    System.out.println("recorded");
                    importAndExportInteger("feature" + count++);
//                importAndExportVector(result,"test2.txt");
                    result.clear();
                    buffer = 0;
                }
            }
        }
    }

    private double getDirectionRate(Hand hand, Hand preHand){
        Finger indexFinger = hand.fingers().get(1);
        Finger preIndexFinger = preHand.fingers().get(1);
        Vector delaVector = indexFinger.tipPosition().minus(preIndexFinger.tipPosition());
        if(delaVector.getX()<1 && delaVector.getY()<1 && delaVector.getZ()<1){
            return 0;
        }
        if(delaVector.getX() < 0 && delaVector.getY() >0){
            return Math.atan(delaVector.getY()/delaVector.getX())+Math.PI;
        }else if(delaVector.getY() < 0 && delaVector.getX() > 0){
            return Math.atan(delaVector.getY()/delaVector.getX())+2*Math.PI;
        }else{
            return Math.atan(delaVector.getY()/delaVector.getX()*1.0d);
        }
    }

    private double getVelocity(Hand hand, Hand preHand) {
        Finger indexFinger = hand.fingers().get(1);
        Finger preIndexFinger = preHand.fingers().get(1);
        Vector deltaVector = indexFinger.tipPosition().minus(preIndexFinger.tipPosition());
        return Math.sqrt(Math.pow(deltaVector.getX(), 2) / DELTA * 1.0d + Math.pow(deltaVector.getY(), 2) / DELTA * 1.0d + Math.pow(deltaVector.getZ(), 2) / DELTA * 1.0d);
    }

    public void buildFeatureVector(Hand hand) {
        double[] featrueVector = new double[12];
        int i = 0;
        Vector palmNomarl = hand.palmNormal();
        featrueVector[i++] = palmNomarl.getX();
        featrueVector[i++] = palmNomarl.getY();
        featrueVector[i++] = palmNomarl.getZ();
        Vector palmVelocity = hand.palmVelocity();
        featrueVector[i++] = palmVelocity.getX();
        featrueVector[i++] = palmVelocity.getY();
        featrueVector[i++] = palmVelocity.getZ();
        float handRadius = hand.sphereRadius();
        featrueVector[i++] = handRadius;
        FingerList fingers = hand.fingers();
        Finger f1 = fingers.get(0);
        Finger f2 = fingers.get(1);
        Finger f3 = fingers.get(2);
        Finger f4 = fingers.get(3);
        Finger f5 = fingers.get(4);
        featrueVector[i++] = f1.isExtended() ? 1d : 0d;
        featrueVector[i++] = f2.isExtended() ? 1d : 0d;
        featrueVector[i++] = f3.isExtended() ? 1d : 0d;
        featrueVector[i++] = f4.isExtended() ? 1d : 0d;
        featrueVector[i] = f5.isExtended() ? 1d : 0d;
        result.add(featrueVector);
    }

    public void importAndExportInteger(String baseFileName) {
        try {
            File file = new File(baseFileName);
            RandomAccessFile out = new RandomAccessFile(file, "rw");
            out.seek(file.length());
            StringBuilder str = new StringBuilder();
            str.append("{\"cur").append("\"").append(":").append("[");
            for (double[] feature : result) {
                str.append("[");
                for (double d : feature) {
                    str.append(d).append(",");
                }
                str.deleteCharAt(str.lastIndexOf(",")).append("],\n");
            }
            str.deleteCharAt(str.lastIndexOf(",")).append("]}");
            out.writeBytes(str.toString());
            out.close();
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
