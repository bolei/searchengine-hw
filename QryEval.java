import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import myimpl.AbstractDefaultQryParser;
import myimpl.DefaultOrQryParser;
import myimpl.MiscUtil;

import org.apache.lucene.document.Document;
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

	public static AbstractDefaultQryParser defaultQueryParser = new DefaultOrQryParser();

	/**
	 * 
	 * @param args
	 *            The only argument should be one file name, which indicates the
	 *            parameter file.
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {

		// must supply parameter file
		if (args.length < 1) {
			System.err.println(usage);
			System.exit(1);
		}

		// read in the parameter file; one parameter per line in format of
		// key=value
		Map<String, String> params = new HashMap<String, String>();
		Scanner scan = new Scanner(new File(args[0]));
		String line = null;
		do {
			line = scan.nextLine();
			String[] pair = line.split("=");
			params.put(pair[0].trim(), pair[1].trim());
		} while (scan.hasNext());
		scan.close();

		// parameters required for this example to run
		if (!params.containsKey("indexPath")) {
			System.err.println("Error: Parameters were missing.");
			System.exit(1);
		}

		// open the index
		// READER = DirectoryReader.open(FSDirectory.open(new File(params
		// .get("indexPath"))));
		READER = MiscUtil.createIndexReader(params.get("indexPath"));

		if (READER == null) {
			System.err.println(usage);
			System.exit(1);
		}

		BufferedReader br = new BufferedReader(new FileReader(
				params.get("queryFilePath")));
		try {

			while ((line = br.readLine()) != null) {
				String[] strArr = line.split(":");
				// String queryId = strArr[0];
				String[] queryTokens = MiscUtil.tokenizeQuery(strArr[1]);
				MiscUtil.printResults(strArr[0],
						defaultQueryParser.parseQuery(queryTokens).evaluate());
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
		}

	}

	/**
	 * Get the external document id for a document specified by an internal
	 * document id. Ordinarily this would be a simple call to the Lucene index
	 * reader, but when the index was built, the indexer added "_0" to the end
	 * of each external document id. The correct solution would be to fix the
	 * index, but it's too late for that now, so it is fixed here before the id
	 * is returned.
	 * 
	 * @param iid
	 *            The internal document id of the document.
	 * @throws IOException
	 */
	static String getExternalDocid(int iid) throws IOException {
		Document d = QryEval.READER.document(iid);
		String eid = d.get("externalId");

		if ((eid != null) && eid.endsWith("_0"))
			eid = eid.substring(0, eid.length() - 2);

		return (eid);
	}

}
