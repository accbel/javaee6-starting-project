package br.accbel.javaee6sp.test;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.spec.WebArchive;

import br.accbel.javaee6sp.test.functional.CarTest;
import br.accbel.javaee6sp.test.utils.DeploymentUtils;

public class FunctionalTestLauncher extends BaseTest {

	@Override
	protected Class<?>[] getTestClasses() {
		return new Class<?>[] {
			CarTest.class
		};
	}

	@Deployment(testable=false)
    public static WebArchive createDeployment()
    {	
		return DeploymentUtils.getApplicationArchive();
    }
}
