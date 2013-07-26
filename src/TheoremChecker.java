import java.util.*;

public class TheoremChecker extends Expression{ //used to check whether Theorem matches expression
	
	private HashMap<String, subExpression> subValues;
	private Expression theorem;
	private Expression toEvaluate;
	
	public TheoremChecker() {
		subValues = new HashMap<String, subExpression>();
	}
	public TheoremChecker(Expression thm, Expression tbe) throws IllegalInferenceException{
		subValues = new HashMap<String, subExpression>();
		theorem = thm;
		toEvaluate = tbe;
		constructorHelper(thm.getRoot(), tbe.getRoot()); //fills subValues HashMap
	}
	public boolean containsKey(String name) {
		return subValues.containsKey(name);
	}
	public subExpression get(String name) {
		return subValues.get(name);
	}
	public String errorPrinting() {
		String returnVal = "";
		Set<String> keys = subValues.keySet();
		if (keys.isEmpty()) {
			return returnVal;
		}
		for (String key : keys) {
			subExpression temp = subValues.get(key);
			if (temp == null) {
				returnVal = returnVal + key + "= NOT SET!, ";
			} else {
				returnVal = returnVal + key + "=" + subValues.get(key).getTotal()+", ";
			}
		}
		returnVal = returnVal.substring(0,(returnVal.length()-2));
		return returnVal;
	}
	
	private void constructorHelper(subExpression thm, subExpression tbe) throws IllegalInferenceException{
		if (tbe == null) {
			throw new IllegalInferenceException("Bad theorem application "+errorPrinting());
		} else if (thm.isVariable()) {
			try { 
				subValues.put(thm.getName(),tbe);
			} catch (NullPointerException e) {
				throw new IllegalInferenceException("Bad theorem application "+errorPrinting());
			}
		} else if (thm.getName().equals("~")) {
			try {
				constructorHelper(thm.getLeft(),tbe.getLeft());
			} catch (NullPointerException e) {
				throw new IllegalInferenceException("Bad theorem application "+errorPrinting());
			}
		} else {
			try {
				constructorHelper(thm.getLeft(),tbe.getLeft());
				constructorHelper(thm.getRight(),tbe.getRight());
			} catch (NullPointerException e) {
				throw new IllegalInferenceException("Bad theorem application "+errorPrinting());
			}
		}
	}
	public boolean matchs() {
		return matchsHelper(theorem.getRoot(), toEvaluate.getRoot());
	}
	private boolean matchsHelper(subExpression thm, subExpression tbe) {
		if (thm.isVariable()) {
			return tbe.equals(subValues.get(thm.getName()));
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
