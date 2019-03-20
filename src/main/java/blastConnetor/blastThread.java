package blastConnetor;

import orFinderApp.ORF;
import orFinderApp.Query;
import orFinderApp.Result;
import org.biojava.nbio.ws.alignment.qblast.*;

import javax.swing.*;
import java.io.*;
import java.net.ConnectException;
import java.util.ArrayList;

/**
 * This Class creates a new Thread and start a new session to Blast the given ORFs.
 *
 * @author Lex Bosch
 * @version 1.0
 */

public class blastThread extends Thread {

    private static String noNetworkError = "Het is niet mogelijik om de" +
            " resultaten op te halen vanuit de NCBI servers. Kijk of u een connectie heeft met het" +
            " internet en probeer het later opnieuw. Als dit probleem blijvend is, " +
            "neem contact op met netwerkbeheer";

    /**
     * Amount of threads running at the same time.
     */
    private static int RunningThreads = 0;

    /**
     * Amount of times exception has been called regarding connection issue.
     */
    private static int countEx = 0;

    /**
     * Thread of this occurrence.
     */
    private Thread threadOcc;

    /**
     * Protein sequence of this occurence.
     */
    private String sequence;

    /**
     * Bufferreader for writing of the results.
     */
    private BufferedReader reader;

    /**
     * String containing the id of the BLAST.
     */
    private String rid;

    /**
     * Initialise service variable
     */
    private NCBIQBlastService service;

    /**
     * Initialise propterty variable.
     */
    private NCBIQBlastAlignmentProperties props;

    /**
     * Initialise output properties variable
     */
    private NCBIQBlastOutputProperties outputProps;

    //todo comments
    //Might remove
    private ORF occOrf;


    private Query occQuery;

    /**
     * Creates BLAST thread and sets parameters
     *
     * @param subSequence String containing the sequence to Blast.
     * @param occOrf ORF to be filled.
     * @param occQuery Query to be filled.
     */
    blastThread(String subSequence, ORF occOrf, Query occQuery) {
        this.RunningThreads++;
        this.sequence = subSequence;
        this.occOrf = occOrf;
        this.occQuery = occQuery;
        settings();
    }

    /**
     * Gets RunningThreads
     *
     * @return value of RunningThreads
     */
    public static int getRunningThreads() {
        return RunningThreads;
    }

    /**
     * Runs BLAST thread
     */
    public void run() {
//        rid = new String();
        rid = "940EEM65015";
        try {
                //todo Uncomment
//                rid = service.sendAlignmentRequest(this.sequence, props);
                System.out.println(rid);
//            while (!service.isReady(rid)) {
//                //todo write timeout handeling
//                System.out.println("ï be flossin...");
//                Thread.sleep(9000 / this.RunningThreads);
//            }

            System.out.println("blastcomplete");
            InputStream in = service.getAlignmentResults(rid, outputProps);
            reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder newXML = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                newXML.append(line);
            }
            ArrayList<Result> resultList = new ArrayList<>();
            resultList = new saveBlastToResults().saveBlastToResults(newXML.toString(), occOrf, 5);
            occOrf.setResultList(resultList);
            RunningThreads--;
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
            RunningThreads--;
        } catch (java.io.IOException e) {
            RunningThreads--;
            countEx++;
            if (countEx == 1) {
                JOptionPane.showMessageDialog(null, noNetworkError);
            }
        } catch (java.lang.Exception e) {
            countEx++;
            RunningThreads--;
            if (countEx == 1) {
                JOptionPane.showMessageDialog(null, noNetworkError);
            }
        } finally {
            Thread.currentThread().stop();
        }
    }

    /**
     * Start Thread of blastThread instance. Creates new thread and runs Blast.
     */
    @Override
    public void start() {
        if (threadOcc == null) {
            threadOcc = new Thread(this);
            threadOcc.start();
        }
    }

    /**
     * Contains settings for Blasting
     */
    public void settings() {
        props = new NCBIQBlastAlignmentProperties();
        outputProps = new NCBIQBlastOutputProperties();
        service = new NCBIQBlastService();
        /**
         * set Blast programm. Blastp in this case
         */
        props.setBlastProgram(BlastProgramEnum.blastp);
        /**
         * Set database, NR in this case
         */
        props.setBlastDatabase("nr");
        //props.setAlignmentOption(ENTREZ_QUERY, "\"serum albumin\"[Protein name] AND mammals[Organism]");
        /**
         * Set Entrez_Query, only prokaryota in this case
         */
        //   props.setAlignmentOption(ENTREZ_QUERY, "prokaryota[All Fields]");
        /**
         * Set maximum pvalue, 0.001 in this case
         */
        outputProps.setOutputOption(BlastOutputParameterEnum.EXPECT_HIGH, "0.001");
        /**
         * Set output format, xml in this case
         */
        outputProps.setOutputFormat(BlastOutputFormatEnum.XML);
        /**
         * Doesn't quite work, still working on BioJava docs
         * 5 Best hits are returned
         */
        //todo Biojava parametrs werken nog niet
        outputProps.setAlignmentNumber(1);

    }
}
