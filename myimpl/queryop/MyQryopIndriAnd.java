package myimpl.queryop;

public class MyQryopIndriAnd extends MyQryopWeight {

	public MyQryopIndriAnd(MyQryop[] q) {
		super(q);
		weights = new Double[q.length];
		for (int i = 0; i < q.length; i++) {
			weights[i] = 1d;
		}
		totalWeight = q.length;
	}

}
