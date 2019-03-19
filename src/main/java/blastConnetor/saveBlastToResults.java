package blastConnetor;

import orFinderApp.ORF;
import orFinderApp.Result;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class saveBlastToResults {
    private static ArrayList<String> REGEXLIST = new ArrayList<String>(
            Arrays.asList("Hit_accession", "Hit_def", "Hsp_bit-score", "Hsp_query-from", "Hsp_query-to", "Hit_len", "Hsp_evalue", "Hsp_identity", "Hsp_hit-from", "Hsp_hit-to"));

    // accession = Hit_accession
    // description = Hit_def
    // score = Hsp_bit-score
    // queryCover = (Hsp_hit-to - Hsp_hit-from +1) / Hit_len * 100    (4 - 3 + 1)
    // eValue = Hsp_evalue
    // identity = Hsp_identity
    // startposition = Hsp_hit-from
    // stopposition = Hsp_hit-to

    public void saveBlastToResults(String xmlString, ORF OrfSave, int amResults) {
        List<String> allMatches = new ArrayList<String>();
        Matcher m = Pattern.compile("<Hit>[\\s\\S]*?<\\/Hit>")
                .matcher(xmlString);
        while (m.find()) {
            allMatches.add(m.group());
        }

        for (int countHit = 0; countHit < amResults; countHit++) {
            hitsToResults(allMatches.get(countHit), OrfSave);
        }
    }

    public void hitsToResults(String Hit, ORF orfSave) {
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
        tempResult.setIdentity(Math.round(Float.parseFloat(regexResults.get(7))));
        tempResult.setScore(Math.round(Float.parseFloat(regexResults.get(2))));
        tempResult.setQueryCover(((Integer.parseInt(regexResults.get(4)) - Integer.parseInt(regexResults.get(3))
                + 1) / Integer.parseInt(regexResults.get(5))) * 100);
        tempResult.setStartPosition(Integer.parseInt(regexResults.get(7)));
        tempResult.setStopPosition(Integer.parseInt(regexResults.get(8)));
        orfSave.addResult(tempResult);
    }


}
