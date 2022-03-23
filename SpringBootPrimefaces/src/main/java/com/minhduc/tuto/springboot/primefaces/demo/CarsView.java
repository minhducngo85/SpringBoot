package com.minhduc.tuto.springboot.primefaces.demo;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.ocpsoft.pretty.faces.annotation.URLMapping;

@Controller
@Scope(value = "view")
@URLMapping(id = "cars", pattern = "/cars", viewId = "/cars.jsf")
public class CarsView implements Serializable {

  private static final long serialVersionUID = 1L;

  @Autowired
  private CarRepository carRepository;

  private List<Car> cars;

  @PostConstruct
  public void init() {
    cars = carRepository.findAll();
  }

  public List<Car> getCars() {
    return cars;
  }
}
