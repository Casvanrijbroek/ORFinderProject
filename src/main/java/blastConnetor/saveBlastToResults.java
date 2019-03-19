package blastConnetor;

import orFinderApp.ORF;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class saveBlastToResults {
    private ArrayList<String> Hits;
    private Matcher Match;
    List<String> hitMatches;

    String[] matches;


    public void saveBlastToResults(String xmlString, ORF OrfSave, int amResults) {
        this.Hits = new ArrayList<>();
        this.hitMatches = new ArrayList<String>();
        Matcher m = Pattern.compile("<Hit>[\\s\\S]*?<\\/Hit>")
                .matcher(xmlString);
        while (m.find()) {
            this.hitMatches.add(m.group());
        }

        for(int hitNumber = 0; hitNumber < amResults; hitNumber++){
            resultPerHit(this.hitMatches.get(hitNumber));
        }

    }


    public void resultPerHit(String singleHit){

    }
}
