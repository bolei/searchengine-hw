package myimpl.queryop;

public class MyQryopNear extends MyQryopTestNeighbor {

	public MyQryopNear(int n, MyQryop... myQryops) {
		super(n, myQryops);
	}

	@Override
	protected boolean isInNeighbor(int high, int low) {
		return (high - low) <= windowSize;
	}

}
