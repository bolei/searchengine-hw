package myimpl.queryop;

import java.io.IOException;

import myimpl.AlgorithmType;
import myimpl.MiscUtil;
import myimpl.queryresult.MyQryResult;

public abstract class MyQryop {

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
	 * @throws IOException
	 */
	public static MyQryop createQryop(String opName, MyQryop... myQryops)
			throws IOException {
		if (opName.equals("#OR")) {
			return new MyQryopOr(myQryops);
		} else if (opName.equals("#AND")) {
			if (MiscUtil.getAlgorithmType() == AlgorithmType.Indri) {
				return new MyQryopIndriAnd(myQryops);
			} else {
				return new MyQryopAnd(myQryops);
			}
		} else if (opName.startsWith("#NEAR/")) {
			int n = Integer
					.parseInt(opName.substring(opName.lastIndexOf('/') + 1));
			return new MyQryopNear(n, myQryops);
		} else if (opName.startsWith("#UW/")) {
			int n = Integer
					.parseInt(opName.substring(opName.lastIndexOf('/') + 1));
			return new MyQryopWindow(n, myQryops);
		} else if (opName.startsWith("#SUM")) {
			return new MyQryopSum(myQryops);
		} else {
			throw new RuntimeException("unknown query operator name:" + opName);
		}
	}

}
