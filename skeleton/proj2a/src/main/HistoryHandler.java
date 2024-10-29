package main;

import browser.NgordnetQuery;
import browser.NgordnetQueryHandler;
import ngrams.NGramMap;
import ngrams.TimeSeries;
import org.knowm.xchart.XYChart;
import plotting.Plotter;

import java.util.ArrayList;

public class HistoryHandler extends NgordnetQueryHandler {
	NGramMap ngm;
	public HistoryHandler(NGramMap ngm) {
		this.ngm = ngm;
	}
	@Override
	public String handle(NgordnetQuery q) {
		System.out.println("Got query that looks like:");
		System.out.println("Words: " + q.words());
		System.out.println("Start Year: " + q.startYear());
		System.out.println("End Year: " + q.endYear());

		ArrayList<TimeSeries> lts = new ArrayList<>();
		ArrayList<String> labels = new ArrayList<>();

		for (String word : q.words()) {
			lts.add(ngm.weightHistory(word, q.startYear(), q.endYear()));
			labels.add(word);
		}

		XYChart chart = Plotter.generateTimeSeriesChart(labels, lts);
		String encodedImage = Plotter.encodeChartAsString(chart);

		return encodedImage;
	}
}
