package br.accbel.javaee6sp.test;

import junit.framework.Assert;

import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.internal.TextListener;
import org.junit.runner.JUnitCore;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public abstract class BaseTest {
	
	protected abstract Class<?>[] getTestClasses();
	
	@Test
	public void testAll() throws Exception {
		JUnitCore unit = new JUnitCore();
		unit.addListener(new TextListener(System.out));
		Assert.assertTrue(unit.run(getTestClasses()).wasSuccessful());
	}
}
