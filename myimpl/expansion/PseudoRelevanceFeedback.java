package myimpl.expansion;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
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
	private final boolean usingLambda;
	private double lambda = 1.0d;

	public PseudoRelevanceFeedback(HashMap<String, String> properties)
			throws IOException {
		fbDocs = Integer.parseInt(properties.get("fbDocs"));
		mu = Double.parseDouble(properties.get("fbMu"));
		fbTerms = Integer.parseInt(properties.get("fbTerms"));
		fbOrigWeight = Double.parseDouble(properties.get("fbOrigWeight"));
		bout = new BufferedWriter(new FileWriter(new File(
				properties.get("fbFile"))));
		usingLambda = Boolean.parseBoolean(properties.get("fbUsingLambda"));
		if (usingLambda == true) {
			lambda = Double.parseDouble(properties.get("Indri:lambda"));
		}
	}

	public String expandQuery(int queryId, String queryString,
			TreeMap<Integer, Double> sortedDocScores) throws IOException {
		// process top n docs
		Iterator<Entry<Integer, Double>> entryIt = sortedDocScores.entrySet()
				.iterator();
		HashMap<String, Double> unsortedTermScores = new HashMap<String, Double>();
		for (int i = 0; i < fbDocs && entryIt.hasNext(); i++) { // for each doc
			Entry<Integer, Double> entry = entryIt.next();
			int docId = entry.getKey();
			double oldScore = entry.getValue();
			TermVector tv = new TermVector(docId, "body");
			int stemLen = tv.stemsLength();
			for (int j = 1; j < stemLen; j++) {
				double tf = tv.stemFreq(j);
				double docLen = tv.positionsLength();
				double pmlRgivenC = getPmleRgivenC(j, tv);
				double pDgivenI = Math.pow(Math.E, oldScore);
				double pRgivenD = (tf + mu * pmlRgivenC) / (docLen + mu);
				if (usingLambda == true) {
					pRgivenD = (lambda * pRgivenD + (1 - lambda) * pmlRgivenC);
				}
				double score = pRgivenD * pDgivenI;
				if (unsortedTermScores.containsKey(tv.stemString(j))) {
					unsortedTermScores.put(tv.stemString(j), score
							+ unsortedTermScores.get(tv.stemString(j)));
				} else {
					unsortedTermScores.put(tv.stemString(j), score);
				}
			}
		}

		// rank the query terms by score
		TreeMap<String, Double> termRank = new TreeMap<String, Double>(
				new ScoreDescComparator<String>(unsortedTermScores));
		termRank.putAll(unsortedTermScores);

		// create additional query string
		Iterator<Entry<String, Double>> termEntryIt = termRank.entrySet()
				.iterator();

		StringBuilder sb = new StringBuilder();
		sb.append("#WEIGHT (");
		for (int i = 0; i < fbTerms && termEntryIt.hasNext(); i++) {
			Entry<String, Double> entry = termEntryIt.next();
			String term = entry.getKey();
			double score = entry.getValue();
			sb.append(score + " " + term + " ");
		}
		sb.append(")");

		String addedQuery = sb.toString();
		// append additional query string to file
		bout.append(addedQuery + "\n");
		bout.flush();

		queryString = queryString.trim();
		if (!queryString.contains("#")) { // input query is not structured
			queryString = MiscUtil.buildDefaultQueryString(queryString);
		}

		// create a complete expanded query
		sb = new StringBuilder();
		sb.append("#WEIGHT(");
		sb.append(fbOrigWeight + " " + queryString + " " + (1 - fbOrigWeight)
				+ " " + addedQuery);
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

	private static double getPmleRgivenC(int j, TermVector tv)
			throws IOException {
		long ctf = tv.totalStemFreq(j);
		long lenTerm = MiscUtil.getIndexReader().getSumTotalTermFreq("body");
		return ctf / (double) lenTerm;
	}
}
