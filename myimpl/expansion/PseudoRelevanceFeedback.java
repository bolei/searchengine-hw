package myimpl.expansion;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeMap;

import myimpl.MiscUtil;
import myimpl.ScoreDescComparator;
import myimpl.TermVector;

public class PseudoRelevanceFeedback {
	private final int fbDocs;
	private final int fbTerms;
	private final double mu;
	private final double fbOrigWeight;
	private final BufferedWriter bout;

	public PseudoRelevanceFeedback(HashMap<String, String> properties)
			throws IOException {
		fbDocs = Integer.parseInt(properties.get("fbDocs"));
		mu = Double.parseDouble(properties.get("fbMu"));
		fbTerms = Integer.parseInt(properties.get("fbTerms"));
		fbOrigWeight = Double.parseDouble(properties.get("fbOrigWeight"));
		bout = new BufferedWriter(new FileWriter(new File(
				properties.get("fbFile"))));
	}

	public String expandQuery(int queryId, String queryString,
			TreeMap<Integer, Double> sortedDocScores) throws IOException {
		// process top n docs
		Iterator<Integer> docIdIt = sortedDocScores.keySet().iterator();
		HashMap<String, Double> unsortedTermScores = new HashMap<String, Double>();
		for (int i = 0; i < fbDocs && docIdIt.hasNext(); i++) { // for each doc
			int docId = docIdIt.next();
			double oldScore = sortedDocScores.get(docId);
			TermVector tv = new TermVector(docId, "body");
			int stemLen = tv.stemsLength();
			for (int j = 0; j < stemLen; j++) {
				double tf = tv.stemFreq(i);
				double docLen = tv.positionsLength();
				double score = Math.pow(Math.E, oldScore)
						* (tf + mu * getPmleRgivenC(j, tv)) / (docLen + mu);
				if (unsortedTermScores.containsKey(tv.stemString(i))) {
					unsortedTermScores.put(tv.stemString(j), score
							+ unsortedTermScores.get(tv.stemString(i)));
				} else {
					unsortedTermScores.put(tv.stemString(j), score);
				}
			}
		}

		// rank the query terms by score
		TreeMap<String, Double> termRank = new TreeMap<String, Double>(
				new ScoreDescComparator<String>(unsortedTermScores));

		// create additional query string
		Iterator<String> termIt = termRank.keySet().iterator();
		StringBuilder sb = new StringBuilder();
		sb.append("#WEIGHT (");
		for (int i = 0; i < fbTerms && termIt.hasNext(); i++) {
			String term = termIt.next();
			double score = termRank.get(term);
			sb.append(score + " " + term + " ");
		}
		sb.append(")");

		String addedQuery = sb.toString();
		// append additional query string to file
		bout.append(addedQuery + "\n");
		bout.flush();

		// create a complete expanded query
		sb = new StringBuilder();
		sb.append("#WEIGHT(");
		sb.append(fbOrigWeight + " " + queryString + " " + (1 - fbOrigWeight)
				+ addedQuery);
		sb.append(")");
		return sb.toString();
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		if (bout != null) {
			bout.close();
		}
	}

	private static double getPmleRgivenC(int i, TermVector tv)
			throws IOException {
		long ctf = tv.totalStemFreq(i);
		long lenTerm = MiscUtil.getIndexReader().getSumTotalTermFreq("body");
		return ctf / (double) lenTerm;
	}
}
