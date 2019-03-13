package blastConnetor;

import orFinderApp.ORF;
import org.biojava.nbio.ws.alignment.qblast.*;

import java.io.*;


public class blastThread extends Thread {

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
        settings();
    }

    /**
     * Runs BLAST thread
     */
    public void run() {
        rid = new String();
        try {
            rid = service.sendAlignmentRequest(this.sequence, props);
            System.out.println(rid);
            while (!service.isReady(rid)) {
                //todo write timeout handeling
                System.out.println("ï be flossin...");
                Thread.sleep(5000);
            }
            System.out.println("blastcomplete");
            InputStream in = service.getAlignmentResults(rid, outputProps);
            reader = new BufferedReader(new InputStreamReader(in));


            // write blast output to specified file
            String path = "src" + File.separator + "main" + File.separator + "java" + File.separator + "test" + File.separator + "saveFiles" + File.separator + "newFile" + String.valueOf(rid);
            File f = new File(path);
            System.out.println(f.getCanonicalPath());
            System.out.println("Saving query results in file " + f.getAbsolutePath());
            writer = new FileWriter(f);

            String line;
            while ((line = reader.readLine()) != null) {
                writer.write(line + System.getProperty("line.separator"));
            }
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            //todo Propper exception handling
            System.out.println("javalangexception");
            System.out.println(e.getMessage());
            System.out.println(e);
        }
    }


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
        outputProps.setOutputFormat(BlastOutputFormatEnum.Text);
        /**
         * 5 Best hits are returned
         */
        outputProps.setAlignmentNumber(1);
    }
}
