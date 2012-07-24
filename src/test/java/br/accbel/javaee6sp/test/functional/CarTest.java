package br.accbel.javaee6sp.test.functional;

import java.net.URL;

import junit.framework.Assert;

import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.thoughtworks.selenium.DefaultSelenium;

@RunWith(Arquillian.class)
public class CarTest {
	
	@Drone
    DefaultSelenium browser;

    @ArquillianResource
    URL deploymentURL;
	
    @Test
    @InSequence(1)
    @RunAsClient
    public void shouldOpenIndex() throws Exception {
    	browser.open(deploymentURL+"index.jsf");
    	Assert.assertTrue(browser.isTextPresent("Lista"));
    }
    
    @Test
    @InSequence(2)
    @RunAsClient
    public void shouldOpenList() throws Exception {
    	browser.click("lista");
    	browser.waitForPageToLoad("5000");
    }
}
