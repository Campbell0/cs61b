package ngrams;

import edu.princeton.cs.algs4.In;

import java.util.Collection;
import java.util.TreeMap;

/**
 * An object that provides utility methods for making queries on the
 * Google NGrams dataset (or a subset thereof).
 *
 * An NGramMap stores pertinent data from a "words file" and a "counts
 * file". It is not a map in the strict sense, but it does provide additional
 * functionality.
 *
 * @author Josh Hug
 */
public class NGramMap {

    TreeMap<String, TimeSeries> wordsData = new TreeMap<>();
    TimeSeries countsData = new TimeSeries();

    /**
     * Constructs an NGramMap from WORDSFILENAME and COUNTSFILENAME.
     */
    public NGramMap(String wordsFilename, String countsFilename) {
        In in = new In(wordsFilename);
        while (in.hasNextLine()) {
            String nextLine = in.readLine();

            String[] splitLine = nextLine.split("\t");
            String word = splitLine[0];
            Integer year = Integer.valueOf(splitLine[1]);
            Double appearTimes = Double.valueOf(splitLine[2]);

            if (wordsData.get(word) != null) {
                wordsData.get(word).put(year, appearTimes);
            }
            else {
                TimeSeries timeSeries = new TimeSeries();
                timeSeries.put(year, appearTimes);
                wordsData.put(word, timeSeries);
            }
        }
        in = new In(countsFilename);
        while (in.hasNextLine()) {
            String nextLine = in.readLine();
            String[] splitLine = nextLine.split(",");
            Integer year = Integer.valueOf(splitLine[0]);
            Double recordedNumber = Double.valueOf(splitLine[1]);
            countsData.put(year, recordedNumber);
        }
    }

    /**
     * Provides the history of WORD between STARTYEAR and ENDYEAR, inclusive of both ends. The
     * returned TimeSeries should be a copy, not a link to this NGramMap's TimeSeries. In other
     * words, changes made to the object returned by this function should not also affect the
     * NGramMap. This is also known as a "defensive copy". If the word is not in the data files,
     * returns an empty TimeSeries.
     */
    public TimeSeries countHistory(String word, int startYear, int endYear) {
        if (!wordsData.containsKey(word)) {
            return new TimeSeries();
        }
        TimeSeries timeSeries = new TimeSeries();
        TimeSeries ts = wordsData.get(word);
        for (Integer key : ts.keySet()) {
            if (key >= startYear && key <= endYear) {
                timeSeries.put(key, ts.get(key));
            }
        }
        return timeSeries;
    }

    /**
     * Provides the history of WORD. The returned TimeSeries should be a copy, not a link to this
     * NGramMap's TimeSeries. In other words, changes made to the object returned by this function
     * should not also affect the NGramMap. This is also known as a "defensive copy". If the word
     * is not in the data files, returns an empty TimeSeries.
     */
    public TimeSeries countHistory(String word) {
        if (!wordsData.containsKey(word)) {
            return new TimeSeries();
        }
        TimeSeries timeSeries = new TimeSeries();
        TimeSeries ts = wordsData.get(word);
        for (Integer key : ts.keySet()) {
            timeSeries.put(key, ts.get(key));
        }
        return timeSeries;
    }

    /**
     * Returns a defensive copy of the total number of words recorded per year in all volumes.
     */
    public TimeSeries totalCountHistory() {
		return new TimeSeries(countsData);
    }

    /**
     * Provides a TimeSeries containing the relative frequency per year of WORD between STARTYEAR
     * and ENDYEAR, inclusive of both ends. If the word is not in the data files, returns an empty
     * TimeSeries.
     */
    public TimeSeries weightHistory(String word, int startYear, int endYear) {
        if (!wordsData.containsKey(word)) {
            return new TimeSeries();
        }
        TimeSeries weighHistory = new TimeSeries();
        TimeSeries ts = wordsData.get(word);
        for (Integer key : ts.keySet()) {
            if (key >= startYear && key <= endYear) {
                weighHistory.put(key, ts.get(key) / countsData.get(key));
            }
        }
        return weighHistory;
    }

    /**
     * Provides a TimeSeries containing the relative frequency per year of WORD compared to all
     * words recorded in that year. If the word is not in the data files, returns an empty
     * TimeSeries.
     */
    public TimeSeries weightHistory(String word) {
        if (!wordsData.containsKey(word)) {
            return new TimeSeries();
        }
        TimeSeries weighHistory = new TimeSeries();
        TimeSeries ts = wordsData.get(word);
        for (Integer key : ts.keySet()) {
            weighHistory.put(key, ts.get(key) / countsData.get(key));
        }
        return weighHistory;
    }

    /**
     * Provides the summed relative frequency per year of all words in WORDS between STARTYEAR and
     * ENDYEAR, inclusive of both ends. If a word does not exist in this time frame, ignore it
     * rather than throwing an exception.
     */
    public TimeSeries summedWeightHistory(Collection<String> words,
                                          int startYear, int endYear) {
        TimeSeries summedWeightHistory = new TimeSeries();
        for (String word : words) {
            TimeSeries ts = weightHistory(word, startYear, endYear);
            summedWeightHistory = summedWeightHistory.plus(ts);
        }
        return summedWeightHistory;
    }

    /**
     * Returns the summed relative frequency per year of all words in WORDS. If a word does not
     * exist in this time frame, ignore it rather than throwing an exception.
     */
    public TimeSeries summedWeightHistory(Collection<String> words) {
        TimeSeries summedWeightHistory = new TimeSeries();
        for (String word : words) {
            TimeSeries ts = weightHistory(word);
            summedWeightHistory = summedWeightHistory.plus(ts);
        }
        return summedWeightHistory;
    }

}
