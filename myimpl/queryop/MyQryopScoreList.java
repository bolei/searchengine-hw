package myimpl.queryop;

import java.io.IOException;

import myimpl.queryresult.MyScoreList;

public abstract class MyQryopScoreList extends MyQryop {
	@Override
	public abstract MyScoreList evaluate() throws IOException;
}
