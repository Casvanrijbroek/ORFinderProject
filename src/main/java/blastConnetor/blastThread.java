package blastConnetor;

import orFinderApp.ORF;
import orFinderApp.Result;
import org.biojava.nbio.ws.alignment.qblast.*;

import java.io.*;
import java.util.ArrayList;

/**
 * This class contains the Threads using to blast. It starts the Blasting process and waits for the blast to be done
 * the results are passed to the saveBlastToResultsMethod class.
 *
 * @author Lex Bosch
 * @version 1.0
 * 29-03-2019
 */

public class blastThread extends Thread {
    /**
     * Amount of currently running Threads.
     */
    private static int RunningThreads = 0;
    /**
     * Counts the amount of exceptions occurred within the session.
     */
    private static int countEx = 0;
    /**
     * Amount of total Threads
     */
    private static int amThreads = 0;
    /**
     * Thread of this occurence
     */
    private Thread threadOcc;
    /**
     * Protein sequence of this occurence
     */
    private String sequence;
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
    /**
     * Initialise ORF to be filled
     */
    private ORF occOrf;

    /**
     * Creates BLAST thread and sets parameters
     *
     * @param subSequence the sequence to be blasted
     * @param occOrf an ORF object with locations on the sequence
     */
    blastThread(String subSequence, ORF occOrf) {
        RunningThreads++;
        sequence = subSequence;
        this.occOrf = occOrf;
        amThreads++;
        settings();
    }

    /**
     * Gets RunningThreads
     *
     * @return value of RunningThreads
     */
    static int getRunningThreads() {
        return RunningThreads;
    }

    /**
     * Runs BLAST thread
     */
    public void run() {
        ArrayList<Result> resultList;
        InputStream in;
        BufferedReader reader;
        StringBuilder newXML;
        String rid;
        String line;

        try {
            rid = service.sendAlignmentRequest(this.sequence, props);
            while (!service.isReady(rid)) {
                Thread.sleep(5000 / amThreads);
            }

            in = service.getAlignmentResults(rid, outputProps);
            reader = new BufferedReader(new InputStreamReader(in));
            newXML = new StringBuilder();

            while ((line = reader.readLine()) != null) {
                newXML.append(line);
            }

            resultList = new saveBlastToResults().saveBlastToResultsMethod(newXML.toString(), occOrf, 5);
            occOrf.setResultList(resultList);
            RunningThreads--;
        } catch (InterruptedException | IOException err) {
            System.out.println(err.getMessage());
        } catch (java.lang.Exception e) {
            countEx++;
            System.out.println(e.getMessage());
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
    private void settings() {
        props = new NCBIQBlastAlignmentProperties();
        outputProps = new NCBIQBlastOutputProperties();
        service = new NCBIQBlastService();
        props.setBlastProgram(BlastProgramEnum.blastx);
        props.setBlastDatabase("nr");
        outputProps.setOutputOption(BlastOutputParameterEnum.EXPECT_HIGH, "0.001");
        outputProps.setOutputFormat(BlastOutputFormatEnum.XML);
    }
}
