package br.accbel.javaee6sp.test.utils;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;

import javax.el.ELContext;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;

import org.jboss.shrinkwrap.api.Filters;
import org.jboss.shrinkwrap.api.GenericArchive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.importer.ExplodedImporter;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.DependencyResolvers;
import org.jboss.shrinkwrap.resolver.api.maven.MavenDependencyResolver;
import org.jboss.shrinkwrap.resolver.api.maven.filter.ScopeFilter;

public class DeploymentUtils {
	
	public static WebArchive getApplicationArchive() {
		File[] compileLibs = DependencyResolvers.use(MavenDependencyResolver.class)
                .includeDependenciesFromPom("pom.xml")
                .resolveAsFiles(new ScopeFilter("compile"));
		
		File[] testLibs = DependencyResolvers.use(MavenDependencyResolver.class)
				.loadMetadataFromPom("pom.xml")
                .artifacts("org.mockito:mockito-all",
                		   "org.jboss.shrinkwrap.resolver:shrinkwrap-resolver-api",
                		   "org.seleniumhq.selenium:selenium-java",
                		   "junit:junit",
                		   "org.jboss.jsfunit:jboss-jsfunit-core",
                		   "net.sourceforge.cobertura:cobertura")
                .resolveAsFiles();
		
		WebArchive war = ShrinkWrap
	            .create(WebArchive.class, "javaee6sp.war")
	            .addAsLibraries(compileLibs)
	            .addAsLibraries(testLibs)
	            .addPackages(true, "br/accbel/javaee6sp")
	            .addAsWebInfResource("jbossas-ds.xml")
	            .addAsWebInfResource(new File("src/test/resources/WEB-INF/test-beans.xml"),"beans.xml")
	            .addAsResource(new File("src/test/resources/META-INF/test-persistence.xml"),"META-INF/persistence.xml")
	            .merge(ShrinkWrap.create(GenericArchive.class).as(ExplodedImporter.class)
	    			    .importDirectory("src/main/webapp").as(GenericArchive.class),
	    			    "/", Filters.exclude("/WEB-INF/beans.xml"));
		
		return war;
	}
	
	public static FacesContext getMockedFacesContext()
    {
        FacesContext mock = mock(FacesContext.class);
        when(mock.getELContext()).thenReturn(mock(ELContext.class));
        ExternalContext externalContext = mock(ExternalContext.class);
        when(mock.getExternalContext()).thenReturn(externalContext);
        when(externalContext.getFlash()).thenReturn(mock(Flash.class));
        return mock;
    }
}
