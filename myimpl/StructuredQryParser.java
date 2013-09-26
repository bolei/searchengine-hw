package myimpl;

import java.io.IOException;
import java.util.LinkedList;
import java.util.ListIterator;

import javax.swing.tree.DefaultMutableTreeNode;

import myimpl.queryop.MyQryop;
import myimpl.queryop.MyQryopTerm;

public class StructuredQryParser implements QryParser {

	@Override
	public MyQryop parseQuery(String queryStr) throws IOException {
		queryStr = queryStr.trim();
		if (!queryStr.contains("#")) { // input query is not structured
			String[] queryTokens = MiscUtil.tokenizeQuery(queryStr);

			// add #OR
			StringBuilder sb = new StringBuilder();
			sb.append("#OR(");
			for (String token : queryTokens) {
				sb.append(token + " ");
			}
			sb.append(")");

			// then parse it
			return parseQuery(sb.toString());
		}
		return parseQueryExpr(queryStr);
	}

	private MyQryop parseQueryExpr(String queryExpr) {
		DefaultMutableTreeNode root = constructTreeNode(queryExpr);
		return getQryop(root);
	}

	private DefaultMutableTreeNode constructTreeNode(String queryExpr) {
		int i = 0, j = 0;
		queryExpr = queryExpr.trim();
		LinkedList<Object> myStack = new LinkedList<Object>();
		while (i < queryExpr.length() && j < queryExpr.length()) {
			while (j < queryExpr.length() && queryExpr.charAt(j) != ' '
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
			while (j < queryExpr.length()
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

	private MyQryop getQryop(DefaultMutableTreeNode root) {
		if (!root.getAllowsChildren()) {
			String token = root.getUserObject().toString();
			int dotPos = token.indexOf(".");
			if (dotPos == -1) {
				return new MyQryopTerm(token);
			} else {
				return new MyQryopTerm(token.substring(0, dotPos),
						token.substring(dotPos) + 1);
			}
		} else {
			MyQryop[] args = new MyQryop[root.getChildCount()];
			for (int i = 0; i < args.length; i++) {
				args[i] = getQryop((DefaultMutableTreeNode) root.getChildAt(i));
			}
			String opName = root.getUserObject().toString();
			return MyQryop.createQryop(opName, args);
		}
	}
}
