package main;

import browser.NgordnetQuery;
import browser.NgordnetQueryHandler;
import ngrams.NGramMap;

import java.util.List;

public class HistoryTextHandler extends NgordnetQueryHandler {
	NGramMap ngm;
	public HistoryTextHandler(NGramMap ngm) {
		this.ngm = ngm;
	}
	@Override
	public String handle(NgordnetQuery q) {
		List<String> words = q.words();
		int startYear = q.startYear();
		int endYear = q.endYear();

		StringBuilder response = new StringBuilder();
		for (String word : words) {
			response.append(word).append(": ").append(ngm.weightHistory(word, startYear, endYear).toString()).append("\n");
		}
		return response.toString();
	}
}
