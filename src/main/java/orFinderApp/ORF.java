package orFinderApp;

import java.util.ArrayList;

/**
 * This class represents an Open Reading Frame.
 *
 * @author Lex Bosch
 * @version 1.0
 * 29-03-2019
 */
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
     * stopPosition of the ORFinder.orFinderApp.ORF.
     */
    private int stopPosition;

    /**
     * constructor of the ORFinder.orFinderApp.ORF.
     *
     * @param startPosition the start position
     * @param stopPosition the stop position
     */
    public ORF(int startPosition, int stopPosition) {
        setPositions(startPosition, stopPosition);
        resultList = new ArrayList<>();
    }

    /**
     * Set start and stop position.
     *
     * @param startPosition the start position
     * @param stopPosition the stop position
     */
    private void setPositions(int startPosition, int stopPosition) {
        this.startPosition = startPosition;
        this.stopPosition = stopPosition;
    }

    /**
     * Gets resultList.
     *
     * @return value of resultList
     */
    public ArrayList<Result> getResultList() {
        return resultList;
    }

    /**
     * Gets startPosition.
     *
     * @return value of startPosition
     */
    public int getStartPosition() {
        return startPosition;
    }

    /**
     * Gets stopPosition.
     *
     * @return value of stopPosition
     */
    public int getStopPosition() {
        return stopPosition;
    }

    /**
     * clear ArrayList resultList.
     */
    public void clearResultList() {
        resultList = null;
    }

    /**
     * Adds a Result to the resultList.
     *
     * @param addResult the result that is to be added
     */
    public void addResult(Result addResult){
        this.resultList.add(addResult);
    }

    /**
     * Sets the resultList.
     *
     * @param resultList ArrayList with Results to be set
     */
    public void setResultList(ArrayList<Result> resultList){
        this.resultList = resultList;
    }

    /**
     * Returns the object as a String visualising the positions of the ORF.
     *
     * @return the String that is to be returned
     */
    @Override
    public String toString() {
        return String.format("Pos %d - %d", startPosition, stopPosition);
    }
}
