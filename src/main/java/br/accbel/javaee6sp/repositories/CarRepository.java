package br.accbel.javaee6sp.repositories;

import java.util.List;

import javax.ejb.TransactionAttribute;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.EntityType;

import br.accbel.javaee6sp.model.Car;

@RequestScoped
public class CarRepository {

	@Inject
	private EntityManager em;
	
	@TransactionAttribute
	public List<Car> findAll() {
		return em.createQuery("from Car", Car.class).getResultList();
	}
	
	@TransactionAttribute
	public Car findById(Integer id) {
		return em.find(Car.class, id);
	}
	
	@TransactionAttribute
	public List<Car> findByCriteria(Car car) {
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<Car> criteria = builder.createQuery(Car.class);
		EntityType<Car> type = em.getMetamodel().entity(Car.class);
		Root<Car> carRoot = criteria.from(Car.class);
		if (car.getName()!=null)
			criteria.where(builder.like(carRoot.get(type.getDeclaredSingularAttribute(Car.NAME, String.class)), "%"+car.getName()+"%"));
		
		return em.createQuery(criteria).getResultList();
	}
	
	@TransactionAttribute
	public void insert(Car car) {
		em.persist(car);
	}
	
	@TransactionAttribute
	public Car update(Car car) {
		return em.merge(car);
	}
	
	@TransactionAttribute
	public void delete(Car car) {
		car = em.find(Car.class, car.getId());
		em.remove(car);
	}
}
