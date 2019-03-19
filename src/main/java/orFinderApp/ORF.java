package orFinderApp;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class ORF {

    /**
     * Arraylist containing the results
     */
    private ArrayList<Result> resultList;

    /**
     * startPosition of the ORFinder.orFinderApp.ORF
     */
    private int startPosition;

    /**
     * stopPosition of the ORFinder.orFinderApp.ORF
     */
    private int stopPosition;

    /**
     * constructor of the ORFinder.orFinderApp.ORF
     *
     * @param startPosition
     * @param stopPosition
     */
    public ORF(int startPosition, int stopPosition) {
        setPositions(startPosition, stopPosition);
    }

    /**
     * Set start and stop position
     *
     * @param startPosition
     * @param stopPosition
     */
    private void setPositions(int startPosition, int stopPosition) {
        this.startPosition = startPosition;
        this.stopPosition = stopPosition;
    }

    /**
     * Gets resultList
     *
     * @return value of resultList
     */
    public ArrayList<Result> getResultList() {
        return resultList;
    }

    /**
     * Gets startPosition
     *
     * @return value of startPosition
     */
    public int getStartPosition() {
        return startPosition;
    }

    /**
     * Gets stopPosition
     *
     * @return value of stopPosition
     */
    public int getStopPosition() {
        return stopPosition;
    }

    /**
     * clear ArrayList resultList
     */
    public void clearResultList() {
        resultList = null;
    }

    public Result addResult(){
        this.resultList.add(new Result());
        return this.resultList.get(resultList.size() - 1);
    }

    public void setResultList(ArrayList<Result> resultList){
        this.resultList = resultList;
    }
}
