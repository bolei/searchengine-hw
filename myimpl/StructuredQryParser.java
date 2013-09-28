package myimpl;

import java.io.IOException;
import java.util.LinkedList;
import java.util.ListIterator;

import javax.swing.tree.DefaultMutableTreeNode;

import myimpl.queryop.MyQryop;
import myimpl.queryop.MyQryopTerm;
import myimpl.queryop.MyQryopWeight;

public class StructuredQryParser implements QryParser {

	@Override
	public MyQryop parseQuery(String queryStr) throws IOException {
		queryStr = queryStr.trim();
		if (!queryStr.contains("#")) { // input query is not structured
			queryStr = MiscUtil.buildDefaultQueryString(queryStr);
		}
		return parseQueryExpr(queryStr);
	}

	private MyQryop parseQueryExpr(String queryExpr) throws IOException {
		DefaultMutableTreeNode root = constructTreeNode(queryExpr);
		return getQryop(root);
	}

	private DefaultMutableTreeNode constructTreeNode(String queryExpr) {
		int i = 0, j = 0;
		queryExpr = queryExpr.trim();
		int expLenth = queryExpr.length();
		LinkedList<Object> myStack = new LinkedList<Object>();
		while (i < expLenth && j < expLenth) {
			while (j < expLenth && queryExpr.charAt(j) != ' '
					&& queryExpr.charAt(j) != '(' && queryExpr.charAt(j) != ')') {
				j++;
			}
			String token = queryExpr.substring(i, j);
			if (token.startsWith("#")) {
				myStack.push(new DefaultMutableTreeNode(token, true));
			} else {
				if (MiscUtil.isStopWord(token) == false) {
					myStack.push(new DefaultMutableTreeNode(token, false));
				}
			}
			while (j < expLenth
					&& (queryExpr.charAt(j) == ' '
							|| queryExpr.charAt(j) == '(' || queryExpr
							.charAt(j) == ')')) {
				if (queryExpr.charAt(j) == '(') {
					myStack.push("(");
				} else if (queryExpr.charAt(j) == ')') {
					LinkedList<DefaultMutableTreeNode> children = new LinkedList<DefaultMutableTreeNode>();
					Object elm = myStack.poll();
					if (elm == null) {
						throw new RuntimeException(
								"parsing error: parenthesis don't match");
					}
					while (elm instanceof DefaultMutableTreeNode) {
						children.add((DefaultMutableTreeNode) elm);
						elm = myStack.poll();
					}
					elm = myStack.poll();
					if (elm instanceof DefaultMutableTreeNode == false) {
						throw new RuntimeException(
								"parsing error: bad query string format");
					}
					DefaultMutableTreeNode parent = (DefaultMutableTreeNode) elm;
					ListIterator<DefaultMutableTreeNode> it = children
							.listIterator(children.size());
					while (it.hasPrevious()) {
						parent.add(it.previous());
					}
					myStack.push(parent);
				}
				j++;
			}
			i = j;
		}
		return (DefaultMutableTreeNode) myStack.get(0);
	}

	private MyQryop getQryop(DefaultMutableTreeNode root) throws IOException {
		if (root.getAllowsChildren()) { // query operator
			String opName = root.getUserObject().toString();
			int argSize = root.getChildCount();
			if (opName.equals("#WEIGHT")) {
				if (argSize % 2 != 0) {
					throw new RuntimeException(
							"#weight operator argument illegal format");
				}
				double[] weights = new double[argSize / 2];
				MyQryop[] args = new MyQryop[argSize / 2];
				for (int i = 0; i < args.length; i++) {
					weights[i] = Double
							.parseDouble((String) ((DefaultMutableTreeNode) root
									.getChildAt(i * 2)).getUserObject());
					args[i] = getQryop((DefaultMutableTreeNode) root
							.getChildAt(i * 2 + 1));
				}
				return new MyQryopWeight(args, weights);
			} else {
				MyQryop[] args = new MyQryop[argSize];
				for (int i = 0; i < args.length; i++) {
					args[i] = getQryop((DefaultMutableTreeNode) root
							.getChildAt(i));
				}
				return MyQryop.createQryop(opName, args);
			}
		} else {
			String token = root.getUserObject().toString();
			int dotPos = token.indexOf(".");
			if (dotPos == -1) {
				return new MyQryopTerm(token);
			} else {
				return new MyQryopTerm(token.substring(0, dotPos),
						token.substring(dotPos) + 1);
			}
		}
	}
}
