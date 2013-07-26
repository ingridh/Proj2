import java.util.*;

public class TheoremChecker extends Expression{ //used to check whether Theorem matches expression
	
	private HashMap<String, subExpression> subValues;
	
	public TheoremChecker() {
		subValues = new HashMap<String, subExpression>();
	}
	public TheoremChecker(Expression thm, Expression tbe) {
		subValues = new HashMap<String, subExpression>();
		constructorHelper(thm.getRoot(), tbe.getRoot()); //fills subValues HashMap
	}
	public boolean containsKey(String name) {
		return subValues.containsKey(name);
	}
	public subExpression get(String name) {
		return subValues.get(name);
	}
	
	private void constructorHelper(subExpression thm, subExpression tbe) {
		if (thm.isVariable()) {
			subValues.put(thm.getName(),tbe);
		} else {
			constructorHelper(thm.getLeft(),tbe.getLeft());
			constructorHelper(thm.getRight(),tbe.getRight());
		}
	}
	public static boolean matchs(Expression thm, Expression tbe) {
		return matchsHelper(thm.getRoot(), tbe.getRoot());
	}
	private static boolean matchsHelper(subExpression thm, subExpression tbe) {
		if (thm.isVariable()) {
			return true;
		} else if (thm.getName().equals(tbe.getName())) {
			boolean lReturn = false;
			boolean rReturn = false;
			if (thm.getLeft() != null && tbe.getLeft() != null) {
				lReturn = matchsHelper(thm.getLeft(), tbe.getLeft());
			}
			if (thm.getRight() != null && tbe.getRight() != null) {
				rReturn = matchsHelper(thm.getRight(), tbe.getRight());
			}
			return lReturn && rReturn;
		} else {
			return false;
		}
	}
	
//	public void applyThm(Expression thm, Expression tbe) {
//		applyThmHelper(thm.root, tbe.root);
//	}
//	
//	private void applyThmHelper(subExpression thm, subExpression tbe) {
//		if (thm.isVariable()) {
//			if(subValues.get(thm.name).equals(tbe)) {
//				tbe.setValid();
//			} else {
//				throw new IllegalArgumentException("Theorem does not match input");
//			}
//		} else {
//			if (tbe == null) {
//				throw new IllegalArgumentException("Theorem does not match input");
//			}
//			if(thm.name.equals(tbe.name)) {
//				tbe.value = true; //don't want to set everything true yet
//				if(thm.myLeft != null) {
//					applyThmHelper(thm.myLeft, tbe.myLeft);
//				}
//				if(thm.myRight != null) {
//					applyThmHelper(thm.myRight, tbe.myRight);
//				}
//			} else {
//				throw new IllegalArgumentException("Theorem does not match input");
//			}
//		}
//	}
}
