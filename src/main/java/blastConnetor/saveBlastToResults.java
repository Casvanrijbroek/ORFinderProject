package blastConnetor;

import orFinderApp.ORF;
import orFinderApp.Result;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This Class creates Result objects to store the blast results in.
 *
 * @author Lex Bosch
 * @version 1.0
 */

public class saveBlastToResults {

    /**
     * Arraylist containing the results to be extracted from the xml file
     */
    private static final ArrayList<String> REGEXLIST = new ArrayList<String>(
            Arrays.asList("Hit_accession", "Hit_def", "Hsp_bit-score", "Hsp_query-from", "Hsp_query-to",
                    "Hit_len", "Hsp_evalue", "Hsp_identity", "Hsp_hit-from", "Hsp_hit-to"));

    // accession = (0)Hit_accession
    // description = (1)Hit_def
    // score = (2)Hsp_bit-score
    // queryCover = ((4)Hsp_hit-to - (3)Hsp_hit-from +1) / (5)Hit_len * 100
    // eValue = (6)Hsp_evalue
    // identity = (7)Hsp_identity
    // startposition = (8)Hsp_hit-from
    // stopposition = (9)Hsp_hit-to


    /**
     * Function isolating the individual hits and returning results.
     * @param xmlString String containing the xml data.
     * @param amResults Amount of results per hit to be saved
     * @return Arraylist containing the results of the xmlString
     */
    public ArrayList<Result> saveBlastToResults(String xmlString, ORF OrfSave, int amResults) {
        List<String> allMatches = new ArrayList<String>();
        Matcher m = Pattern.compile("<Hit>[\\s\\S]*?<\\/Hit>")
                .matcher(xmlString);
        while (m.find()) {
            allMatches.add(m.group());
        }
        ArrayList<Result> resultList = new ArrayList<>();
        for (int countHit = 0; countHit < amResults; countHit++) {
            resultList.add(hitsToResults(allMatches.get(countHit)));
        }
        return resultList;
    }

    /**
     *
     * @param Hit Hit to have the values extracted from.
     * @return Returns the Result object containing the values of the Hit string
     */
    public Result hitsToResults(String Hit) {
        ArrayList<String> regexResults = new ArrayList<>();
        for (String pattern : REGEXLIST) {
            Pattern sinPat = Pattern.compile("<" + pattern + ">([\\s\\S]*?)<\\/" + pattern + ">");
            Matcher sinHit = sinPat.matcher(Hit);
            if (sinHit.find()) {
                regexResults.add(sinHit.group(1));
            }
        }
        Result tempResult = new Result();
        tempResult.setAccession(regexResults.get(0));
        tempResult.setDescription(regexResults.get(1));
        tempResult.setpValue(Float.parseFloat(regexResults.get(6)));
        tempResult.setIdentity(Integer.parseInt(regexResults.get(7)));
        tempResult.setScore(Math.round(Float.parseFloat(regexResults.get(2))));
        tempResult.setQueryCover(Math.round(((Float.parseFloat(regexResults.get(4)) - Float.parseFloat(regexResults.get(3))
                + 1) / Float.parseFloat(regexResults.get(5))) * 100));
        tempResult.setStartPosition(Integer.parseInt(regexResults.get(8)));
        tempResult.setStopPosition(Integer.parseInt(regexResults.get(9)));
        return tempResult;
    }


}
