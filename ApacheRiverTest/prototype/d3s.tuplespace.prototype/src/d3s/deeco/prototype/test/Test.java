package d3s.deeco.prototype.test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.rmi.RMISecurityManager;
import java.util.Arrays;
import java.util.List;

import d3s.deeco.prototype.annotations.DEECoInput;
import d3s.deeco.prototype.annotations.DEECoProcess;
import d3s.deeco.prototype.knowledge.Tuple;
import d3s.deeco.prototype.knowledge.TupleSpaceUtils;
import d3s.deeco.prototype.runtime.DEECoClassParser;
import d3s.deeco.prototype.runtime.DEECoInvokableProcess;

import net.jini.core.lease.Lease;
import net.jini.core.transaction.Transaction;
import net.jini.space.JavaSpace;
import net.jini.space.JavaSpace05;
import net.jini.space.MatchSet;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if (System.getSecurityManager() == null)
			System.setSecurityManager(new RMISecurityManager());
		try {
			JavaSpace05 space = TupleSpaceUtils.getSpace();
			Transaction tx = TupleSpaceUtils.createTransaction();
			MatchSet result = space.contents(Arrays.asList(new Object[] {TupleSpaceUtils.createTemplate(null)}), tx, Lease.FOREVER, Long.MAX_VALUE);
			Tuple t = (Tuple) result.next();
			while (t != null) {
				System.out.println(t.key + ": " + t.value);
				t = (Tuple) result.next();
			}
			tx.commit();
		} catch (Exception e) {
			System.out.println("sdfasdfasdf");
		}
	}

}
