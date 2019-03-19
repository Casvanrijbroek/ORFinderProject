package orFinderApp;

public class Result {

    public Result(){

    }

    /**
     * accession code of the result
     */
    private String accession;

    /**
     * description of the result
     */
    private String description;

    /**
     * score of the result
     */
    private int score;

    /**
     * queryCover value of the result
     */
    private int queryCover;

    /**
     * pValue value of the result
     */
    private float pValue;

    /**
     * identity percentage of the result
     */
    private int identity;

    /**
     * startPosition of the result
     */
    private int startPosition;

    /**
     * stopPosition of the result
     */
    private int stopPosition;

    /**
     * Gets accession
     *
     * @return value of accession
     */
    public String getAccession() {
        return accession;
    }

    /**
     * Sets accession
     *
     * @param accession sets this value into the class variable
     */
    public void setAccession(String accession) {
        this.accession = accession;
    }

    /**
     * Gets description
     *
     * @return value of description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets description
     *
     * @param description sets this value into the class variable
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets score
     *
     * @return value of score
     */
    public int getScore() {
        return score;
    }

    /**
     * Sets score
     *
     * @param score sets this value into the class variable
     */
    public void setScore(int score) {
        this.score = score;
    }

    /**
     * Gets queryCover
     *
     * @return value of queryCover
     */
    public int getQueryCover() {
        return queryCover;
    }

    /**
     * Sets queryCover
     *
     * @param queryCover sets this value into the class variable
     */
    public void setQueryCover(int queryCover) {
        this.queryCover = queryCover;
    }

    /**
     * Gets pValue
     *
     * @return value of pValue
     */
    public float getpValue() {
        return pValue;
    }

    /**
     * Sets pValue
     *
     * @param pValue sets this value into the class variable
     */
    public void setpValue(float pValue) {
        this.pValue = pValue;
    }

    /**
     * Gets identity
     *
     * @return value of identity
     */
    public int getIdentity() {
        return identity;
    }

    /**
     * Sets identity
     *
     * @param identity sets this value into the class variable
     */
    public void setIdentity(int identity) {
        this.identity = identity;
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
     * Sets startPosition
     *
     * @param startPosition sets this value into the class variable
     */
    public void setStartPosition(int startPosition) {
        this.startPosition = startPosition;
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
     * Sets stopPosition
     *
     * @param stopPosition sets this value into the class variable
     */
    public void setStopPosition(int stopPosition) {
        this.stopPosition = stopPosition;
    }


    /**
     * returns the accession and description
     *
     * @return value of accession and description
     */
    @Override
    public String toString() {
        return accession + " " + description;
    }
}
