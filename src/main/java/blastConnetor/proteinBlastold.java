package blastConnetor;

import static org.biojava.nbio.ws.alignment.qblast.BlastAlignmentParameterEnum.ENTREZ_QUERY;

import java.io.*;
import java.util.ArrayList;

import ORFinderApp.Query;
import org.biojava.nbio.core.sequence.io.util.IOUtils;
import org.biojava.nbio.ws.alignment.qblast.*;


public class proteinBlastold extends Thread {
    private static final String BLAST_OUTPUT_FILE = "saveto/prokaryota.html";    // file to save blast results to
    private static final String SEQUENCE = "MLDINAFLVEKGGEPDKIKVSQKKRGASVELVDEIINDYKNWTKIRFELDAKNKEINAVQKQIGQKFKAKEDASELLAQKDKLNAEKKELVEKEEKADGDLRFKVNQVGNIVHESVVDSQDEENNELLRTWKPEGLEEVGEVAAGTGAAAKLSHHEVLLRLDGYDPERGVKIVGHRGYFLRNYGVFLNQALVNYGLSFLAKNDYTPLQAPVMMNKEVMAKTAQLSQFDEELYKVIDGDDEKYLIATSEQPISAYHTGEWFESPQEQLPVRYAGFSSCFRREAGSHGKDAWGIFRVHAFEKIEQFVLAEPERSWEEFDRMIGLSEEFYKSLGLPYRIVGIVSGELNNAAAKKYDLEAWFPFQKEYKELVSCSNCTDYQSRNLEIRCGIKQQNQTEKKYVHCLNSTLSATQRTLCCILENYQTEDGLIVPEVLRRYIPGEPEFIPFAKELPKNSTSNKKKNKN";     // Blast query sequence
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
    //todo What does "rid" do
    private String rid;
    /**
     * Initialise output format variable
     */
    private FileWriter writer;
    /**
     * Initialise bufferreader variable
     */
    private BufferedReader reader;
    /**
     * Initilise Query query for sequence
     */
    private Query query;
    //todo test threads
    private Thread runThreads;
    //todo test this
    private ArrayList<String> ridTest;
    //todo make comments and better name
    private String tempSeq;

    /**
     * Constructs the proteinBlast object and sets settings as needed
     */
    public proteinBlastold(Query query) {
        this.query = query;
        service = new NCBIQBlastService();
        props = new NCBIQBlastAlignmentProperties();
        outputProps = new NCBIQBlastOutputProperties();
        rid = null;
        writer = null;
        reader = null;
        settings();

        // in this example we use default values set by constructor (XML format, pairwise alignment, 100 descriptions and alignments)
        // Example of two possible ways of setting output options
        // outputProps.setAlignmentNumber(200); // outputProps.setOutputOption(BlastOutputParameterEnum.ALIGNMENTS, “200”);
    }

    /**
     * Contains settings for Blasting
     */
    public void settings() {
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
        props.setAlignmentOption(ENTREZ_QUERY, "prokaryota[All Fields]");
        /**
         * Set maximum pvalue, 0.001 in this case
         */
        outputProps.setOutputOption(BlastOutputParameterEnum.EXPECT_HIGH, "0.001");
        /**
         * Set output format, HTML in this case
         */
        outputProps.setOutputFormat(BlastOutputFormatEnum.HTML);
    }

    /**
     * Returns Boolean depicting the state of the blasting operation
     *
     * @return True if done, false if not done
     */
    public boolean isReady() {
        try {
            return service.isReady(rid);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    /**
     * Start the blasting with the sequence with in the query object provided
     */
    public void blast() {
        try {

            for (String sequence : query.getSequenceList()) {
                this.tempSeq = sequence;

            }

            rid = service.sendAlignmentRequest(SEQUENCE, props);

            /**
             * Waits for the results to be ready. Refreshes every 5 seconds
             */
            while (!service.isReady(rid)) {
                //todo write timeout handeling
                Thread.sleep(5000);
            }
            // read results when they are ready
            InputStream in = service.getAlignmentResults(rid, outputProps);
            reader = new BufferedReader(new InputStreamReader(in));

            // write blast output to specified file
            File f = new File(BLAST_OUTPUT_FILE);
            System.out.println("Saving query results in file " + f.getAbsolutePath());
            writer = new FileWriter(f);

            String line;
            while ((line = reader.readLine()) != null) {
                writer.write(line + System.getProperty("line.separator"));
            }


        } catch (IOException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        } finally {
            // clean up
            IOUtils.close(writer);
            IOUtils.close(reader);

            // delete given alignment results from blast server (optional operation)
            service.sendDeleteRequest(rid);
        }
    }

    @Override
    public void run() {
        try {
            /**
             * Send blast request to the Blast server
             */
            ridTest.add(service.sendAlignmentRequest(SEQUENCE, props));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void start() {
        if (runThreads == null) {
            runThreads = new Thread(this, "name");
            runThreads.start();
        }
    }
}

public class proteinBlast