package br.accbel.javaee6sp.test.incontainer.model;

import java.util.List;
import java.util.Set;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.groups.Default;

import junit.framework.Assert;

import org.jboss.arquillian.junit.Arquillian;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import pl.com.it_crowd.arquillian.mock_contexts.ConversationScopeRequired;
import br.accbel.javaee6sp.model.Car;
import br.accbel.javaee6sp.repositories.CarRepository;

@RunWith(Arquillian.class)
public class CarModelTest {
    
	CarRepository carRepository;
	
	@Inject
	BeanManager beanManager;
	
    @Inject
    Validator validator;
    
    @Before
    public void beforeTest() {
    	Set<Bean<?>> beans = beanManager.getBeans(CarRepository.class);
    	
    	for (Bean<?> b : beans) {
    		if (!b.isAlternative()) {
    			CreationalContext<?> cc = beanManager.createCreationalContext(b);
    			carRepository = (CarRepository)beanManager.getReference(b, CarRepository.class, cc);
    		}
    	}
    }
    
    @Test
    public void testCarNameIsRequired() throws Exception {
    	Car car = new Car();
    	
    	Set<ConstraintViolation<Car>> violations = validator.validateProperty(car, "name", Default.class);
    	Assert.assertFalse(violations.isEmpty());
    }
	
	@Test
	@ConversationScopeRequired
    public void testCarPersist() throws Exception {
    	Car car = new Car("Fusca");
    	
    	carRepository.insert(car);
    	
    	Assert.assertNotNull(car.getId());
    	Assert.assertNotNull(carRepository.findByCriteria(car));
    }
	
	@Test
	@ConversationScopeRequired
	public void testFindCarByCriteria() throws Exception {
		List<Car> result = carRepository.findByCriteria(new Car("Fusca"));
		
		Assert.assertFalse(result.isEmpty());
		Assert.assertTrue(result.get(0).getName().equals("Fusca"));
		
		result = carRepository.findByCriteria(new Car());
		Assert.assertFalse(result.isEmpty());
		Assert.assertTrue(result.equals(carRepository.findAll()));
	}
	
	@Test
	@ConversationScopeRequired
	public void testCarUpdated() throws Exception {
		Car car = carRepository.findAll().get(0);
		car.setName("Nome alterado");
		
		car = carRepository.update(car);
		
		Assert.assertNotNull(car);
		Assert.assertEquals("Nome alterado", car.getName());
	}
	
	@Test
	@ConversationScopeRequired
	public void testCarDeleted() throws Exception {
		Car car = carRepository.findAll().get(0);
		
		carRepository.delete(car);
		
		Assert.assertNull(carRepository.findById(car.getId()));
	}
}