package br.accbel.javaee6sp.test.incontainer.controller;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doThrow;

import java.util.Arrays;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.inject.Inject;

import junit.framework.Assert;

import org.jboss.arquillian.junit.Arquillian;
import org.jboss.seam.international.status.Level;
import org.jboss.seam.international.status.Message;
import org.jboss.seam.international.status.Messages;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import pl.com.it_crowd.arquillian.mock_contexts.FacesContextRequired;
import pl.com.it_crowd.arquillian.mock_contexts.MockFacesContextProducer;
import pl.com.it_crowd.arquillian.mock_contexts.ViewScopeRequired;
import br.accbel.javaee6sp.controllers.CarController;
import br.accbel.javaee6sp.model.Car;
import br.accbel.javaee6sp.repositories.CarRepository;
import br.accbel.javaee6sp.test.mocks.MockProducer;
import br.accbel.javaee6sp.test.utils.DeploymentUtils;

@RunWith(Arquillian.class)
public class CarControllerTest {
	
	@Inject
	CarController carController;
	
	CarRepository carRepositoryMock;
	
	@Inject
	Messages messages;
	
	List<Car> cars;
	
	@MockFacesContextProducer
    public FacesContext mockFacesContext()
    {
        return DeploymentUtils.getMockedFacesContext();
    }
	
	@Before
	public void beforeTest() throws Exception {
		messages.clear();
		
		carRepositoryMock = MockProducer.getCarRepositoryMock();
		
		Car savedCar = new Car("Saved");
		savedCar.setId(1);
		cars = Arrays.asList(new Car[] {savedCar});
		
		when(carRepositoryMock.findByCriteria(any(Car.class))).thenReturn(cars);
		when(carRepositoryMock.findAll()).thenReturn(cars);
	}
	
	@After
	public void afterTest() throws Exception {
		reset(carRepositoryMock);
	}
	
	@Test
	@ViewScopeRequired
	public void testCarSearch() throws Exception {
		Assert.assertNull(carController.search());
		verify(carRepositoryMock).findByCriteria(any(Car.class));
	}
	
	@Test
	@ViewScopeRequired
	@FacesContextRequired
	public void testCarEdit() throws Exception {
		carController.getList().setRowIndex(0);
		String result = carController.edit();
		
		Assert.assertEquals("/pages/car/form?faces-redirect=true", result);
		Assert.assertEquals(cars.get(0), carController.getCar());
		
		FacesContext context = FacesContext.getCurrentInstance();
		verify(context.getExternalContext().getFlash()).put("selectedCar", cars.get(0));
	}
	
	@Test
	@ViewScopeRequired
	public void testSavingNewCar() throws Exception {
		carController.getCar().setName("New");
		String result = carController.save();
		
		Assert.assertEquals("/pages/car/list?faces-redirect=true", result);
		for (Message m : messages.getAll()) {
			Assert.assertEquals(Level.INFO, m.getLevel());
			Assert.assertEquals("Operação realizada com sucesso", m.getText());
		}
		
		verify(carRepositoryMock).insert(carController.getCar());
	}
	
	@Test
	@ViewScopeRequired
	public void testEditingExistingCar() throws Exception {
		carController.getList().setRowIndex(0);
		
		carController.edit();
		when(carRepositoryMock.update(cars.get(0))).thenReturn(cars.get(0));
		String result = carController.save();
		
		Assert.assertEquals("/pages/car/list?faces-redirect=true", result);
		for (Message m : messages.getAll()) {
			Assert.assertEquals(Level.INFO, m.getLevel());
			Assert.assertEquals("Operação realizada com sucesso", m.getText());
		}
		
		verify(carRepositoryMock).update(carController.getCar());
	}
	
	@Test
	@ViewScopeRequired
	public void testErrorWhenSavingOrEditing() throws Exception {
		doThrow(new RuntimeException("Error")).when(carRepositoryMock).insert(any(Car.class));
		
		messages.getAll();
		String result = carController.save();
		
		Assert.assertNull(result);
		for (Message m : messages.getAll()) {
			Assert.assertEquals(Level.ERROR, m.getLevel());
			Assert.assertEquals("Error", m.getText());
		}
	}
	
	@Test
	@ViewScopeRequired
	public void testRemovingExistingCar() throws Exception {
		carController.getList().setRowIndex(0);
		cars = Arrays.asList(new Car[] {});
		when(carRepositoryMock.findAll()).thenReturn(cars);
		
		String result = carController.remove();
		Assert.assertNull(result);
	}
	
	@Test
	@ViewScopeRequired
	public void testErrorWhenRemoving() throws Exception {
		carController.getList();
		doThrow(new RuntimeException("Error")).when(carRepositoryMock).delete(any(Car.class));
		
		String result = carController.remove();
		Assert.assertNull(result);
		for (Message m : messages.getAll()) {
			Assert.assertEquals(Level.ERROR, m.getLevel());
			Assert.assertEquals("Error", m.getText());
		}
	}
}
