package myimpl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class MyQryop {

	protected List<MyQryop> args = new ArrayList<MyQryop>();

	/**
	 * Evaluates the query operator, including any child operators and returns
	 * the result.
	 * 
	 * @return {@link MyQryResult} object
	 * @throws IOException
	 */
	public abstract MyQryResult evaluate() throws IOException;

}
