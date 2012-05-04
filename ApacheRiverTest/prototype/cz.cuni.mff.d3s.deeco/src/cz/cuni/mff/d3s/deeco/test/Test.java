package cz.cuni.mff.d3s.deeco.test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.rmi.RMISecurityManager;
import java.util.Arrays;
import java.util.List;

import cz.cuni.mff.d3s.deeco.annotations.DEECoInput;
import cz.cuni.mff.d3s.deeco.annotations.DEECoProcess;
import cz.cuni.mff.d3s.deeco.invokable.InvokableProcess;
import cz.cuni.mff.d3s.deeco.knowledge.Knowledge;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;
import cz.cuni.mff.d3s.deeco.knowledge.Tuple;
import cz.cuni.mff.d3s.deeco.knowledge.TSUtils;
import cz.cuni.mff.d3s.deeco.runtime.ClassParser;


import net.jini.core.lease.Lease;
import net.jini.core.transaction.Transaction;
import net.jini.space.JavaSpace;
import net.jini.space.JavaSpace05;
import net.jini.space.MatchSet;

public class Test {

	/**
	 * @param args
	 */
	
	static class A {
		public String a;
	}
	
	static class B extends A {
		public String b;
	}
	
	public static void main(String[] args) {
		System.out.println("Printing:");
		if (System.getSecurityManager() == null)
			System.setSecurityManager(new RMISecurityManager());
		try {
//			KnowledgeManager km = new KnowledgeManager();
//			Knowledge initialKnowledge = RobotFollowerComponent.getInitialKnowledge();
//			km.writeKnowledge(initialKnowledge);
//			km.registerRootKnowledge(initialKnowledge.id);
			JavaSpace05 space = TSUtils.getSpace();
			Transaction tx = TSUtils.createTransaction();
//			MatchSet result = space.contents(Arrays.asList(new Object[] {TSUtils.createTemplate(null)}), tx, Lease.FOREVER, Long.MAX_VALUE);
//			Tuple t = (Tuple) result.next();
//			System.out.println("Printing:");
//			while (t != null) {
//				System.out.println(t.key + ": " + t.value);
//				t = (Tuple) result.next();
//			}			
			Class tPath = (Class) ((Tuple) space.readIfExists(TSUtils.createTemplate("2.path.typeDefinition"), tx, Lease.FOREVER)).value;
			System.out.println(tPath.equals(Path.class));
			tx.commit();
		} catch (Exception e) {
			System.out.println("sdfasdfasdf");
		}
	}

}
