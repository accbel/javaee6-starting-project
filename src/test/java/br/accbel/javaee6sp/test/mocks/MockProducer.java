package br.accbel.javaee6sp.test.mocks;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;

import org.mockito.Mockito;

import br.accbel.javaee6sp.repositories.CarRepository;
import br.accbel.javaee6sp.test.annotations.stereotypes.Mock;

public class MockProducer {
	
	private static CarRepository carRepositoryMock;
	
	@Produces @RequestScoped @Mock
	public static CarRepository getCarRepositoryMock() {
		if (carRepositoryMock == null)
			carRepositoryMock = Mockito.mock(CarRepository.class);
		
		return carRepositoryMock;
	}
}
