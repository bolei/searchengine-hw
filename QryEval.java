import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

import myimpl.MiscUtil;
import myimpl.QryParser;
import myimpl.StructuredQryParser;
import myimpl.expansion.PseudoRelevanceFeedback;
import myimpl.queryresult.MyScoreList;

import org.apache.lucene.index.IndexReader;

public class QryEval {

	static String usage = "Usage:  java "
			+ System.getProperty("sun.java.command") + " paramFile\n\n";

	/**
	 * The index file reader is accessible via a global variable. This isn't
	 * great programming style, but the alternative is for every query operator
	 * to store or pass this value, which creates its own headaches.
	 */
	public static IndexReader READER;

	public static QryParser defaultQueryParser = new StructuredQryParser();

	/**
	 * 
	 * @param args
	 *            The only argument should be one file name, which indicates the
	 *            parameter file.
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {

		boolean debug = false;

		// must supply parameter file
		if (args.length < 1) {
			System.err.println(usage);
			System.exit(1);
		}

		// read in the parameter file; one parameter per line in format of
		// key=value
		HashMap<String, String> params = new HashMap<String, String>();
		Scanner scan = new Scanner(new File(args[0]));
		String line = null;
		do {
			line = scan.nextLine();
			String[] pair = line.split("=");
			params.put(pair[0].trim(), pair[1].trim());
		} while (scan.hasNext());
		scan.close();

		MiscUtil.setProp(params);

		// open the index
		// READER = DirectoryReader.open(FSDirectory.open(new File(params
		// .get("indexPath"))));
		READER = MiscUtil.getIndexReader();

		if (READER == null) {
			System.err.println(usage);
			System.exit(1);
		}

		BufferedReader br = new BufferedReader(new FileReader(
				params.get("queryFilePath")));
		boolean fb = Boolean.parseBoolean(params.get("fb"));
		long begin = System.currentTimeMillis();
		PseudoRelevanceFeedback prf = null;
		if (fb == true) {
			prf = new PseudoRelevanceFeedback(MiscUtil.getProp());
		}
		try {
			while ((line = br.readLine()) != null) {
				if (line.isEmpty()) {
					continue;
				}
				String[] strArr = line.split(":");
				String queryId = strArr[0];
				String queryStr = strArr[1];
				MyScoreList scoreList = (MyScoreList) defaultQueryParser
						.parseQuery(queryStr).evaluate();
				if (fb == true) {
					String expandedQuery = prf.expandQuery(
							Integer.parseInt(queryId), queryStr,
							scoreList.getSortedScores());
					scoreList = (MyScoreList) defaultQueryParser.parseQuery(
							expandedQuery).evaluate();
				}
				MiscUtil.printResults(queryId, scoreList, debug);
			}
		} finally {
			if (br != null) {
				try {
					br.close();
					br = null;
				} catch (IOException ioe) {
					throw ioe;
				}
			}
			long end = System.currentTimeMillis();
			System.err.println(String.format("time used: %d millis",
					(end - begin)));
		}

	}

}
