import junit.framework.TestCase;

public class ExpressionTest extends TestCase {

	public void testExpression () {
		boolean thrown = false;
		Expression r = new Expression();
		Expression s = new Expression();
		Expression t = new Expression();
		try {
			r = new Expression("~p");
			//r.print();

			s = new Expression("(~q&~(p=>q))");
			//s.print();

			t = new Expression("~(p=>q)");
			//t.print();
		} catch (IllegalLineException e) {
			System.out.println(e.getMessage());
			thrown = true;
		}
		assertFalse(thrown);
		assertTrue(r.isOK());
		assertTrue(s.isOK());
		

		thrown = false;
		try {
			t = new Expression("(~(BOB&(~FRANK=>BOB)))");
			//t.print();
		} catch (IllegalLineException e) {
			assertEquals(e.getMessage(),"Error: proposition must be single lowercase letter");
			thrown = true;
		}
		
		Expression u = new Expression();
		thrown = false;
		try {
			u = new Expression("p~");
			//u.print();
		} catch (IllegalLineException e) {
			assertEquals(e.getMessage(),"Error: proposition must be single lowercase letter");
			thrown = true;
		}
		assertTrue(thrown);
		
		thrown = false;
		try {
			u = new Expression(" ");
		} catch (IllegalLineException e) {
			assertEquals(e.getMessage(),"Error: expression cannot have spaces");
			thrown = true;
		}
		assertTrue(thrown);
		
		thrown = false;
		try {
			u = new Expression("(~)");
		} catch (IllegalLineException e) {
			assertEquals(e.getMessage(),"Error: unbalanced parentheses or no operands");
			thrown = true;
		}
		assertTrue(thrown);
		
		thrown = false;
		try {
			u = new Expression("(~&p)");
		} catch (IllegalLineException e) {
			assertEquals(e.getMessage(),"Error: proposition must be single lowercase letter");
			thrown = true;
		}
		assertTrue(thrown);
		
		thrown = false;
		try {
			u = new Expression("(~q(&(~(p=>q)))");
		} catch (IllegalLineException e) {
			assertEquals(e.getMessage(),"Error: unbalanced parentheses or no operands");
			thrown = true;
		}
		assertTrue(thrown);
		
	}
	
	public void testSet() {
		boolean thrown = false;
		Expression r = new Expression();
		Expression s = new Expression();
		Expression t = new Expression();
		try {
			r = new Expression("~p");
			r.set("p", true);
			assertFalse(r.getRoot().getValue());
			assertFalse(r.getRoot().getIsSet());
			assertTrue(r.getRoot().getLeft().getValue());
			assertTrue(r.getRoot().getLeft().getIsSet());
			
			r = new Expression("~p");
			r.set("~p", true);
			assertTrue(r.getRoot().getValue());
			assertTrue(r.getRoot().getIsSet());
			assertFalse(r.getRoot().getLeft().getValue());
			assertTrue(r.getRoot().getLeft().getIsSet());
			
			s = new Expression("((p=>q)=>(p=>q))");
			s.set("(p=>q)", true);
			assertTrue(s.getRoot().getLeft().getValue());
			assertTrue(s.getRoot().getLeft().getIsSet());
			assertTrue(s.getRoot().getRight().getValue());	// sets all instances of (p=>q) in tree
			assertTrue(s.getRoot().getRight().getIsSet());
			
			t = new Expression("~(p=>q)");
			t.set("~(p=>q)", true);
			assertTrue(t.getRoot().getValue());
			assertTrue(t.getRoot().getIsSet());
			assertFalse(t.getRoot().getLeft().getValue());
			assertTrue(t.getRoot().getLeft().getIsSet());

		} catch (IllegalLineException e) {
			System.out.println(e.getMessage());
			thrown = true;
		}
		assertFalse(thrown);
		
	}
	  public void testSubExpressionEquals() {
			try {
				Expression e1 = new Expression("((x&y)=>x)");
				Expression e2 = new Expression("(x&y)");
				Expression e3 = new Expression("~((x&y)=>x)");
				assertTrue(e1.root.equals(e3.root.myLeft));
				assertTrue(e2.root.equals(e1.root.myLeft));
			} catch (IllegalLineException e) {
				fail();
			}
	  }
}
