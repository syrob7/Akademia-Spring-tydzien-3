package pl.akademiaspring.task3.service;

import org.springframework.stereotype.Service;
import pl.akademiaspring.task3.model.Car;
import pl.akademiaspring.task3.model.Color;

import java.util.ArrayList;
import java.util.List;

@Service
public class CarService {

    private List<Car> carList;

    public CarService() {
        this.carList = new ArrayList<>();
        carList.add(new Car(1L, "Opel", "Corse", Color.BLACK));
        carList.add(new Car(2L, "Ford", "Mondeo", Color.RED));
        carList.add(new Car(3L, "Toyota", "Auris", Color.BLUE));
    }

    public List<Car> getCarList() {
        return carList;
    }

    public void setCarList(List<Car> carList) {
        this.carList = carList;
    }

}
