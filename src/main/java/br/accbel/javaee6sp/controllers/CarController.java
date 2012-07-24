package br.accbel.javaee6sp.controllers;

import java.io.Serializable;

import javax.faces.bean.ViewScoped;
import javax.faces.context.Flash;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.inject.Inject;
import javax.inject.Named;

import org.jboss.seam.international.status.Messages;

import br.accbel.javaee6sp.model.Car;
import br.accbel.javaee6sp.repositories.CarRepository;

@Named
@ViewScoped
public class CarController implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3853320851479137873L;

	@Inject
	private CarRepository carRepository;
	
//	@Inject
//	public void setCarRepository(CarRepository carRepository) {
//		this.carRepository = carRepository;
//	}
	
	@Inject
	private Car car;
	
	@Inject
	Messages messages;
	
	@Inject
	private Flash flash;
	
	private DataModel<Car> list;
	
	public String search() {
		list = new ListDataModel<Car>(carRepository.findByCriteria(car));
		return null;
	}
	
	public String edit() {
		car = list.getRowData();
		flash.put("selectedCar", car);
		return "/pages/car/form?faces-redirect=true";
	}
	
	public String save() {
		try {
			if (car.getId()==null)
				carRepository.insert(car);
			else
				car = carRepository.update(car);
			
			messages.info("Operação realizada com sucesso");
			return "/pages/car/list?faces-redirect=true";
		} catch (RuntimeException e) {
			messages.error(e.getMessage());
			return null;
		}
	}
	
	public String remove() {
		try {
			carRepository.delete(list.getRowData());
		} catch (RuntimeException e) {
			messages.error(e.getMessage());
		} finally {
			list = new ListDataModel<Car>(carRepository.findAll());
		}
		
		return null;
	}

	public DataModel<Car> getList() {
		if (list==null) {
			list = new ListDataModel<Car>(carRepository.findAll());
		}

		return list;
	}

	public Car getCar() {
		if (flash.containsKey("selectedCar"))
			car = (Car)flash.get("selectedCar");
		
		return car;
	}
}