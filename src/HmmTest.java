import be.ac.ulg.montefiore.run.jahmm.*;
import be.ac.ulg.montefiore.run.jahmm.io.FileFormatException;
import be.ac.ulg.montefiore.run.jahmm.io.ObservationIntegerReader;
import be.ac.ulg.montefiore.run.jahmm.io.ObservationSequencesReader;
import be.ac.ulg.montefiore.run.jahmm.io.ObservationVectorReader;
import be.ac.ulg.montefiore.run.jahmm.learn.BaumWelchLearner;
import be.ac.ulg.montefiore.run.jahmm.learn.BaumWelchScaledLearner;
import be.ac.ulg.montefiore.run.jahmm.learn.KMeansLearner;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by manatea on 2016/12/14.
 */
public class HmmTest {
    private Hmm<ObservationDiscrete> hmm1 = new Hmm(1,new OpdfDiscreteFactory(Packet.class));
    private Hmm<ObservationDiscrete> hmm2 = new Hmm(1,new OpdfDiscreteFactory(Packet.class));
    private Hmm<ObservationDiscrete> hmm3 = new Hmm(1,new OpdfDiscreteFactory(Packet.class));
    private Hmm<ObservationDiscrete> hmm4 = new Hmm(1,new OpdfDiscreteFactory(Packet.class));
    private Hmm<ObservationDiscrete> hmm5 = new Hmm(1,new OpdfDiscreteFactory(Packet.class));
    private Hmm<ObservationDiscrete> hmm6 = new Hmm(1,new OpdfDiscreteFactory(Packet.class));
    private List<List<ObservationDiscrete>> sequences = new ArrayList<List<ObservationDiscrete>>();
    private void testBaumWelch(String filename,Hmm hmm) throws IOException, FileFormatException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(filename))));
        List<List<ObservationInteger>> sequences = ObservationSequencesReader.readSequences(new ObservationIntegerReader(), reader);
        reader.close();
        sequenceConvert(sequences,filename);
        BaumWelchLearner bwl = new BaumWelchLearner();
        hmm = bwl.learn(hmm,this.sequences);
        System.out.println(hmm);
    }
    private void testPropoties(String filename,Hmm hmm) throws IOException, FileFormatException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(filename))));
        List<List<ObservationInteger>> sequences1 = ObservationSequencesReader.readSequences(new ObservationIntegerReader(), reader);
        reader.close();
        sequenceConvert(sequences1,filename);
        System.out.println(hmm.probability(this.sequences.get(0)));
    }

    private void sequenceConvert(List<List<ObservationInteger>> lists,String fileName){
        sequences.clear();
        for(List<ObservationInteger> subList : lists){
            List<ObservationDiscrete> tmpList = new ArrayList<ObservationDiscrete>();
            for(ObservationInteger integer : subList){
                if(fileName.endsWith("x")) {
                    tmpList.add(new ObservationDiscrete<Packet>(integer.value > 0 ? Packet.Y : Packet.Z));
                }
                else if(fileName.endsWith("y")){
                    tmpList.add(new ObservationDiscrete<Packet>(integer.value > 0 ? Packet.S : Packet.X));
                }else{
                    tmpList.add(new ObservationDiscrete<Packet>(integer.value > 0 ? Packet.Q : Packet.H));
                }
            }
            this.sequences.add(tmpList);
        }
    }

    private void fixList(List<List<ObservationVector>> list,int step){
        for(List sublist : list){
            int k = 0;
            for(int i = 0;i<sublist.size();i++,k++){
                if(k==step){
                    sublist.remove(i);
                    k=0;
                }
            }
        }
    }

    public static void main(String[] args) throws IOException, FileFormatException {
        HmmTest h = new HmmTest();
        h.testBaumWelch("bingox",h.hmm1);
        h.testBaumWelch("bingoy",h.hmm2);
        h.testBaumWelch("bingoz",h.hmm3);
        h.testBaumWelch("crossx",h.hmm4);
        h.testBaumWelch("crossy",h.hmm5);
        h.testBaumWelch("crossz",h.hmm6);
        h.testPropoties("bingotestx",h.hmm1);
        h.testPropoties("bingotesty",h.hmm2);
        h.testPropoties("bingotestz",h.hmm3);
        h.testPropoties("bingotestx",h.hmm4);
        h.testPropoties("bingotesty",h.hmm5);
        h.testPropoties("bingotestz",h.hmm6);
        h.testPropoties("crosstestx",h.hmm1);
        h.testPropoties("crosstesty",h.hmm2);
        h.testPropoties("crosstestz",h.hmm3);
        h.testPropoties("crosstestx",h.hmm4);
        h.testPropoties("crosstesty",h.hmm5);
        h.testPropoties("crosstestz",h.hmm6);
//        h.testPropoties("test2.txt",h.hmm2);
    }

    public static enum Packet {
        S,
        X,
        Z,
        Y,
        Q,
        H;

        private Packet() {
        }

        public ObservationDiscrete<Packet> observation() {
            return new ObservationDiscrete(this);
        }
    }

}
