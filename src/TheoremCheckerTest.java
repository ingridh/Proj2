import junit.framework.TestCase;


public class TheoremCheckerTest extends TestCase {
	public void testConstructor() throws IllegalLineException, IllegalInferenceException{
		Expression thm1 = new Expression("((p&(p=>q))=>q)");
		Expression exp1 = new Expression("((m&(m=>n))=>n)");
		//tests base case where letters are substituted
		TheoremChecker tester1 = new TheoremChecker(thm1,exp1);
		if (ProofChecker.iAmDebugging) {
			System.out.println(tester1.errorPrinting());
		}
		
		//checks if containsKey is working
		assertTrue(tester1.containsKey("p"));
		assertTrue(tester1.containsKey("q"));
		assertFalse(tester1.containsKey("m"));
		
		//checks whether get is working
		Expression m = new Expression("m");
		Expression n = new Expression("n");
		assertTrue(tester1.get("p").equals(m.getRoot()));
		assertTrue(tester1.get("q").equals(n.getRoot()));
		
		//tests for complete mismatch of theorem to expression
		//should throw IllegalInferenceException
		boolean thrown = false;
		Expression exp2 = new Expression("((b=>q)=>((a|b)=>q))");
		try {
			TheoremChecker tester2 = new TheoremChecker(thm1,exp2);
			System.out.println(tester2.errorPrinting());
			fail();
		} catch (IllegalInferenceException e) {
			System.out.println(e.getMessage());
			thrown = true;
		}
		assertTrue(thrown);
	}
	public void testCorrespondingExpression() throws IllegalLineException, IllegalInferenceException{
		Expression thm1 = new Expression("((x&y)=>x)");
		Expression exp1 = new Expression("(((a|b)&~c)=>(a|b))");
		TheoremChecker tester1 = new TheoremChecker(thm1,exp1);
		if (ProofChecker.iAmDebugging) {
			System.out.println(tester1.errorPrinting());
		}
		Expression r1 = new Expression("(a|b)");
		Expression r2 = new Expression("~c");
		assertTrue(tester1.get("x").equals(r1.getRoot()));
		assertTrue(tester1.get("y").equals(r2.getRoot()));
		
		Expression thm2 = new Expression("(((p=>q)&(~p=>q))=>q)");
		Expression exp2 = new Expression("((((r|s)=>(x|~y))&(~(r|s)=>(x|~y)))=>(x|~y))");
		TheoremChecker tester2 = new TheoremChecker(thm2,exp2);
		if (ProofChecker.iAmDebugging) {
			System.out.println(tester2.errorPrinting());
		}
		
	}
	public void testMatchs() throws IllegalLineException, IllegalInferenceException{
		Expression thm1 = new Expression("((x&y)=>x)");
		Expression exp1 = new Expression("(((a|b)&~c)=>(a|b))");
		Expression thm2 = new Expression("((p&(p=>q))=>q)");
		Expression exp2 = new Expression("((m&(m=>n))=>n)");
		TheoremChecker tester1 = new TheoremChecker(thm1,exp1);
		TheoremChecker tester2 = new TheoremChecker(thm2,exp2);
		
		//checks whether matchs correctly pattern matches
		assertTrue(tester1.matchs());
		assertTrue(tester2.matchs());
		
		//checks whether incorrect variable matching is caught
		Expression thm3 = new Expression("(~p=>(~q=>~(p|q)))");
		Expression exp3 = new Expression("(~a=>(~b=>~(b|b)))");
		TheoremChecker tester3 = new TheoremChecker(thm3,exp3);
		if (ProofChecker.iAmDebugging) {
			System.out.println(tester3.errorPrinting());
		}
		assertFalse(tester3.matchs());
		
		//checks if matchs returns false for arguments that are switched
		Expression exp4 = new Expression("((~c&(a|b))=>(a|b))");
		TheoremChecker tester4 = new TheoremChecker(thm1,exp4);
		assertFalse(tester4.matchs());
		if (ProofChecker.iAmDebugging) {
			System.out.println(tester4.errorPrinting());
		}
	}
}
