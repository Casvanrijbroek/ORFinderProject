package blastConnetor;

import orFinderApp.ORF;
import org.biojava.nbio.ws.alignment.qblast.*;

import java.io.*;
import java.net.ConnectException;


public class blastThread extends Thread{

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
     * Bufferreader for writing of the results
     */
    private BufferedReader reader;

    /**
     * Filewriter for writing of the results
     */
    private FileWriter writer;

    /**
     * String containing the code of the BLAST
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

    /**
     * Creates BLAST thread and sets parameters
     *
     * @param subSequence
     * @param occOrf
     */
    blastThread(String subSequence, ORF occOrf) {
        this.sequence = subSequence;
        this.occOrf = occOrf;
        amThreads++;
        settings();
    }

    /**
     * Runs BLAST thread
     */
    public void run() {
//        rid = new String();
        try {
//            rid = service.sendAlignmentRequest(this.sequence, props);
//            System.out.println(rid);
//            System.out.println(amThreads);
//            while (!service.isReady(rid)) {
//                //todo write timeout handeling
//                System.out.println("ï be flossin...");
//                Thread.sleep(5000/amThreads);
//            }

            System.out.println("blastcomplete");
            InputStream in = service.getAlignmentResults("8ZAPDFPA015", outputProps);
            reader = new BufferedReader(new InputStreamReader(in));
       //     new saveBlastToResults().saveBlastToResults(in, this.occOrf, 5);

            StringBuilder newXML = new StringBuilder();

            System.out.println("\\/ deez \\/");
            String line;
            while ((line = reader.readLine()) != null) {
                newXML.append(line);
            }
            new saveBlastToResults().saveBlastToResults(newXML.toString(), occOrf, 5);
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        } catch (java.io.IOException e){
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println(e);
            //todo Propper exception handling
            System.out.println("javalangexception");
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
