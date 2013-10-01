package myimpl.queryop;

public class MyQryopIndriAnd extends MyQryopWeight {

	public MyQryopIndriAnd(MyQryop[] q) {
		super(q);
		weights = new double[q.length];
		for (int i = 0; i < q.length; i++) {
			weights[i] = 1;
		}
		totalWeight = q.length;
	}

}
