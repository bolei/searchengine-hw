package myimpl.queryop;

import java.io.IOException;

import myimpl.queryresult.MyInvertedList;

public class MyQryopTerm extends MyQryopInvertedList {

	private String term;
	private String field;

	/* Constructors */
	public MyQryopTerm(String t) {
		this.term = t;
		this.field = "body"; /* Default field if none is specified */
	}

	/* Constructor */
	public MyQryopTerm(String t, String f) {
		this.term = t;
		this.field = f;
	}

	/**
	 * Evaluate the query operator.
	 */
	public MyInvertedList evaluate() throws IOException {
		MyInvertedList result = new MyInvertedList(this.term, this.field);
		return result;
	}

	@Override
	public String toString() {
		return String.format("#MyQryopTerm(term=%s, field=%s)", term, field);
	}
}
