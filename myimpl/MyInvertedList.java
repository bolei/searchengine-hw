package myimpl;

import java.io.IOException;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.lucene.index.DocsAndPositionsEnum;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.MultiFields;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.DocIdSetIterator;
import org.apache.lucene.util.BytesRef;

public class MyInvertedList implements MyQryResult {
	private TreeMap<Integer, TreeSet<Integer>> docPostings = new TreeMap<Integer, TreeSet<Integer>>();

	public MyInvertedList(String termString, String fieldString)
			throws IOException {
		// Prepare to access the index.
		BytesRef termBytes = new BytesRef(termString);
		Term term = new Term(fieldString, termBytes);

		IndexReader indexReader = MiscUtil.getIndexReader();

		if (indexReader.docFreq(term) < 1) {
			return;
		}

		// Lookup the inverted list.
		DocsAndPositionsEnum iList = MultiFields.getTermPositionsEnum(
				indexReader, MultiFields.getLiveDocs(indexReader), fieldString,
				termBytes);

		// Copy from Lucene inverted list format to our inverted list format.
		// This is a little
		// inefficient, but allows query operators such as #syn, #od/n, and
		// #uw/n to be insulated from
		// the details of Lucene inverted list implementations.
		while (iList.nextDoc() != DocIdSetIterator.NO_MORE_DOCS) {

			int docId = iList.docID();
			TreeSet<Integer> positions = new TreeSet<Integer>();
			int tf = iList.freq();
			for (int j = 0; j < tf; j++) {
				positions.add(iList.nextPosition());
			}

			docPostings.put(docId, positions);
		}
	}

	public TreeMap<Integer, TreeSet<Integer>> getDocPostings() {
		return docPostings;
	}

	public int getDf() {
		return docPostings.size();
	}

	public int getTf(int docId) {
		if (docPostings.containsKey(docId) == false) {
			return -1; // not found
		}
		return docPostings.get(docId).size();
	}

	public void merge(MyInvertedList obj) {
		for (Entry<Integer, TreeSet<Integer>> entry : obj.docPostings
				.entrySet()) {
			if (this.docPostings.containsKey(entry.getKey())) {
				this.docPostings.get(entry.getKey()).addAll(entry.getValue());
			} else {
				this.docPostings.put(entry.getKey(), entry.getValue());
			}
		}
	}

	public int getCtf() {
		int ctf = 0;
		for (Entry<Integer, TreeSet<Integer>> entry : docPostings.entrySet()) {
			ctf += entry.getValue().size();
		}
		return ctf;
	}

}
