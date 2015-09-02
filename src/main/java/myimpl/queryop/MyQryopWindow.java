package myimpl.queryop;

public class MyQryopWindow extends MyQryopTestNeighbor {

	public MyQryopWindow(int n, MyQryop[] myQryops) {
		super(n, myQryops);
	}

	@Override
	protected boolean isInNeighbor(int high, int low) {
		return Math.abs(high - low) < windowSize;
	}

}
