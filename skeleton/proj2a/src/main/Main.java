package main;

import browser.NgordnetServer;
import ngrams.NGramMap;

public class Main {
    public static void main(String[] args) {
        NgordnetServer hns = new NgordnetServer();

        /* The following code might be useful to you.

        String wordFile = "./data/ngrams/top_14377_words.csv";
        String countFile = "./data/ngrams/total_counts.csv";
        NGramMap ngm = new NGramMap(wordFile, countFile);

        */

        String wordsFile = "./data/ngrams/top_49887_words.csv";
        String countsFile = "./data/ngrams/total_counts.csv";
        NGramMap nGramMap = new NGramMap(wordsFile, countsFile);
        hns.startUp();
        hns.register("history", new HistoryHandler(nGramMap));
        hns.register("historytext", new HistoryTextHandler(nGramMap));

        System.out.println("Finished server startup! Visit http://localhost:4567/ngordnet_2a.html");
    }
}
