package myimpl;

import java.io.IOException;

public class MyQryopTerm extends MyQryop {

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
}