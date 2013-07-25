import java.util.*;

public class TheoremChecker extends Expression{ //used to check whether Theorem matches expression
	
	public HashMap<String, subExpression> subValues;
	
	public TheoremChecker() {
		subValues = new HashMap<String, subExpression>();
	}
	public TheoremChecker(Expression thm, Expression tbe) {
		subValues = new HashMap<String, subExpression>();
		constructorHelper(thm.root, tbe.root); //fills subValues HashMap
	}
	
	private void constructorHelper(subExpression thm, subExpression tbe) {
		if (thm.isVariable()) {
			subValues.put(thm.name,tbe);
		} else {
			constructorHelper(thm.myLeft,tbe.myLeft);
			constructorHelper(thm.myRight,tbe.myRight);
		}
	}
	public static boolean matchs(Expression thm, Expression tbe) {
		return matchsHelper(thm.root, tbe.root);
	}
	private static boolean matchsHelper(subExpression thm, subExpression tbe) {
		if (thm.isVariable()) {
			return true;
		} else if (thm.name.equals(tbe.name)) {
			boolean lReturn = false;
			boolean rReturn = false;
			if (thm.myLeft != null && tbe.myLeft != null) {
				lReturn = matchsHelper(thm.myLeft, tbe.myLeft);
			}
			if (thm.myRight != null && tbe.myRight != null) {
				rReturn = matchsHelper(thm.myRight, tbe.myRight);
			}
			return lReturn && rReturn;
		} else {
			return false;
		}
	}
	
	public void applyThm(Expression thm, Expression tbe) {
		applyThmHelper(thm.root, tbe.root);
	}
	
	private void applyThmHelper(subExpression thm, subExpression tbe) {
		if (thm.isVariable()) {
			if(subValues.get(thm.name).equals(tbe)) {
				tbe.setValid();
			} else {
				throw new IllegalArgumentException("Theorem does not match input");
			}
		} else {
			if (tbe == null) {
				throw new IllegalArgumentException("Theorem does not match input");
			}
			if(thm.name.equals(tbe.name)) {
				tbe.value = true; //don't want to set everything true yet
				if(thm.myLeft != null) {
					applyThmHelper(thm.myLeft, tbe.myLeft);
				}
				if(thm.myRight != null) {
					applyThmHelper(thm.myRight, tbe.myRight);
				}
			} else {
				throw new IllegalArgumentException("Theorem does not match input");
			}
		}
	}
}
