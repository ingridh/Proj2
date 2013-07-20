import junit.framework.TestCase;

public class ExpressionTest extends TestCase {

  public void testExpression () {
		boolean thrown = false;
		Expression r = new Expression();
		Expression s = new Expression();
		Expression t = new Expression();
		try {
			r = new Expression("~p");
			r.print();
			s = new Expression("(~q&(~(p=>q)))");
			s.print();
			t = new Expression("(~(BOB&(~FRANK=>BOB)))");
			t.print();
		} catch (IllegalLineException e) {
			thrown = true;
		}
		assertFalse(thrown);
		assertTrue(r.isOK());
		assertTrue(s.isOK());
		assertTrue(t.isOK());
		
		Expression u = new Expression();
		thrown = false;
		try {
			u = new Expression("p~");
			u.print();
		} catch (IllegalLineException e) {
			thrown = true;
		}
		assertTrue(thrown);
		
		thrown = false;
		try {
			u = new Expression(" ");
		} catch (IllegalLineException e) {
			thrown = true;
		}
		assertTrue(thrown);
		
		thrown = false;
		try {
			u = new Expression("(~)");
		} catch (IllegalLineException e) {
			thrown = true;
		}
		assertTrue(thrown);
		
		thrown = false;
		try {
			u = new Expression("(~&p)");
		} catch (IllegalLineException e) {
			thrown = true;
		}
		assertTrue(thrown);
		
		thrown = false;
		try {
			u = new Expression("(~q(&(~(p=>q)))");
		} catch (IllegalLineException e) {
			thrown = true;
		}
		assertTrue(thrown);
	}
}
