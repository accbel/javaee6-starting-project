package br.accbel.javaee6sp.test;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.spec.WebArchive;

import br.accbel.javaee6sp.test.incontainer.controller.CarControllerTest;
import br.accbel.javaee6sp.test.incontainer.integration.CarTest;
import br.accbel.javaee6sp.test.incontainer.model.CarModelTest;
import br.accbel.javaee6sp.test.utils.DeploymentUtils;

public class InContainerTestLauncher extends BaseTest {
	
	@Override
	protected Class<?>[] getTestClasses() {
		return new Class<?>[] {
			CarModelTest.class,
			CarControllerTest.class,
			CarTest.class
		};
	}

	@Deployment
    public static WebArchive createDeployment()
    {	
		return DeploymentUtils.getApplicationArchive();
    }
}