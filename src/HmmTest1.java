import be.ac.ulg.montefiore.run.jahmm.Hmm;
import be.ac.ulg.montefiore.run.jahmm.ObservationVector;
import be.ac.ulg.montefiore.run.jahmm.OpdfMultiGaussianFactory;
import be.ac.ulg.montefiore.run.jahmm.io.FileFormatException;
import be.ac.ulg.montefiore.run.jahmm.io.ObservationSequencesReader;
import be.ac.ulg.montefiore.run.jahmm.io.ObservationVectorReader;
import be.ac.ulg.montefiore.run.jahmm.learn.KMeansLearner;

import java.io.*;
import java.util.List;

/**
 * Created by manatea on 2016/12/14.
 */
public class HmmTest1 {
    private Hmm<ObservationVector> hmm1 = new Hmm(4,new OpdfMultiGaussianFactory(3));
    private Hmm<ObservationVector> hmm2 = new Hmm(4,new OpdfMultiGaussianFactory(3));
    private List<List<ObservationVector>> sequences ;
    private void testBaumWelch(String filename,Hmm hmm) throws IOException, FileFormatException {
    }
    private void testPropoties(String filename,Hmm hmm) throws IOException, FileFormatException {
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
    }


}
