package br.accbel.javaee6sp.test.incontainer.integration;

import java.io.IOException;

import javax.faces.component.UIComponent;

import junit.framework.Assert;

import org.jboss.arquillian.junit.Arquillian;
import org.jboss.jsfunit.api.InitialPage;
import org.jboss.jsfunit.jsfsession.JSFClientSession;
import org.jboss.jsfunit.jsfsession.JSFServerSession;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class CarTest {

	@Test
	@InitialPage("/index.jsf")
	public void testInitialPage(JSFServerSession server, JSFClientSession client)
			throws IOException {
		// Test navigation to initial viewID
		Assert.assertEquals("/index.xhtml", server.getCurrentViewID());

		// Assert that the greeting component is in the component tree and
		// rendered
		UIComponent listLink = server.findComponent("lista");
		Assert.assertTrue(listLink.isRendered());
	}

}
