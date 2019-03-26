package blastConnetor;

import orFinderApp.ORF;
import orFinderApp.Query;
import orFinderApp.Result;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

/**
 * This class start the Threads running the Blast request, allowing for multiple Blast instances at the time.
 * Throws exceptions to be caugth. These exceptions have valid messasges to be displayed.
 *
 * @author Lex Bosch
 * @version 1.0
 */

public class proteinBlast {

    /**
     * Query impot of the Blast
     */
    private Query occQuery;

    /**
     * Protein sequence of the Blast
     */
    private String queryString;

    /**
     * List of blast results
     * May not be used in further versions
     */
    private ArrayList<blastThread> blastList;

    /**
     * Validates the users internet connectivity to the NCBI site. Return true if the connection if valid, returns
     * a exception if the connection is invalid.
     */
    private static boolean netIsAvailable() throws NoBlastConnectionException {
        try {
            final URL url = new URL("https://www.ncbi.nlm.nih.gov/");
            final URLConnection conn = url.openConnection();
            conn.connect();
            conn.getInputStream().close();
            return true;
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new NoBlastConnectionException("No connection can be established with the internet.\n" +
                    " Please make sure you have an adequate internet connection and try again later");
        }
    }

    /**
     * Constructor of the Blast module
     *
     * @param query input query to be BLASTed
     */
    public int blast(Query query) throws NoBlastConnectionException, InterruptedException, NullPointerException {
        if (!countORFs(query)) {
            throw new NullPointerException("No ORFs found within the query, please select the ORFs to be used.");
        }
        this.occQuery = query;

        this.queryString = this.occQuery.getSequence();
        this.proteinBlast();

        return checkResults(query);
    }

    /**
     * Counts the amount of Result within a given query.
     * @param query query to count the results of.
     * @return returns integer of amount of results of the given query
     */
    private int checkResults(Query query) {
        ArrayList<ORF> ORFList = query.getOrfList();
        int amResults = 0;
        for (ORF orfs : ORFList) {
            amResults += orfs.getResultList().size();
        }
        return amResults;
    }

    /**
     * Creates BLAST thread. The ORF of the query given at the contructor are blasted.
     * According  to the Blastdoc developer info, only one Blast request may be send once every 10 seccconds.
     * To make sure the user of the application will not be banned from the Blast services, this delay is set on
     * 10.5 seconds. See
     * https://blast.ncbi.nlm.nih.gov/Blast.cgi?CMD=Web&PAGE_TYPE=BlastDocs&DOC_TYPE=DeveloperInfo
     * for more info.
     * DO NOT REMOVE DELAY
     */
    private void proteinBlast() throws NoBlastConnectionException, InterruptedException {
        netIsAvailable();
        blastList = new ArrayList<blastThread>();
        for (ORF occOrf : occQuery.getOrfList()) {
            blastList.add(new blastThread(queryString.substring(occOrf.getStartPosition(), occOrf.getStopPosition()), occOrf, occQuery));
            blastList.get(blastList.size() - 1).start();
            Thread.sleep(10500);
        }
        while (!(blastThread.getRunningThreads() == 0)) {
            Thread.sleep(2000);
        }
    }

    /**
     * Method return true if there are ORFs present within the query. It returns False if there are not.
     *
     * @param query tests if there are ORFs present in this query.
     * @return true if there are ORFs present within the query. It returns False if there are not.
     */
    private boolean countORFs(Query query) {
        if (query.getOrfList().size() == 0) {
            return false;
        } else {
            return true;
        }
    }
}


