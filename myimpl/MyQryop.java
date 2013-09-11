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

	/**
	 * creates an empty query operator by its name
	 * 
	 * @param opName
	 * @return
	 */
	public static MyQryop createQryop(String opName, MyQryop... myQryops) {
		if (opName.equals("#OR")) {
			return new MyQryopOr(myQryops);
		} else if (opName.equals("#AND")) {
			return new MyQryopAnd(myQryops);
		} else if (opName.startsWith("#NEAR/")) {
			int n = Integer
					.parseInt(opName.substring(opName.lastIndexOf('/') + 1));
			return new MyQryopNear(n, myQryops);
		} else {
			throw new RuntimeException("unknown query operator name:" + opName);
		}
	}

}
