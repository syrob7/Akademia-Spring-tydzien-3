package pl.akademiaspring.task3.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.akademiaspring.task3.model.Car;
import pl.akademiaspring.task3.model.CarColorOnly;
import pl.akademiaspring.task3.service.CarService;
import pl.akademiaspring.task3.model.Color;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
@RequestMapping("/cars")
public class CarApi {

    private CarService carService;

    @Autowired
    public CarApi(CarService carService) {
        this.carService = carService;
    }

    @GetMapping(produces = {
            MediaType.APPLICATION_XML_VALUE,
            MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Resources> getCars() {

        for (Car car : carService.getCarList()) {
            Link linkAddCar = linkTo(methodOn(CarApi.class).addCar(car)).withRel("add car");
            Link linkGetCarById = linkTo(methodOn(CarApi.class).getCarById(car.getCarId())).withRel("get car by id");
            Link linkGetCarByColor = linkTo(methodOn(CarApi.class).getCarByColor(car.getColor())).withRel("get cars by color");
            Link linkPutCar = linkTo(methodOn(CarApi.class).modifyCar(car)).withRel("modify car");
            Link linkDeleteCar = linkTo(methodOn(CarApi.class).removeCar(car.getCarId())).withRel("delete car");

            car.add(linkAddCar, linkGetCarById, linkGetCarByColor, linkPutCar, linkDeleteCar);
        }

        Resources resourceList = new Resources<>(carService.getCarList());

        return new ResponseEntity(resourceList, HttpStatus.OK);
    }

    @GetMapping(produces = {
            MediaType.APPLICATION_XML_VALUE,
            MediaType.APPLICATION_JSON_VALUE},
            path = "/{id}")
    public ResponseEntity<Resource> getCarById(@PathVariable long id) {
        Optional<Car> first = carService.getCarList().stream().filter(c -> c.getCarId() == id).findFirst();

        if (first.isPresent()) {
            Resource<Car> resource = new Resource(first.get());

            Link linkTo = linkTo(methodOn(CarApi.class).getCars()).withRel("all-cars");
            resource.add(linkTo);

            return new ResponseEntity(resource, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(produces = {
            MediaType.APPLICATION_XML_VALUE,
            MediaType.APPLICATION_JSON_VALUE},
            path = "/color/{color}")
    public ResponseEntity<List<Car>> getCarByColor(@PathVariable Color color) {
        List<Car> colorList = carService.getCarList()
                .stream()
                .filter(c -> c.getColor().equals(color))
                .collect(Collectors.toList());

        if (colorList.size() > 0) {
            return new ResponseEntity(colorList, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity addCar(@RequestBody Car car) {
        boolean isAdd = carService.getCarList().add(car);

        if (isAdd) {
            URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                    .buildAndExpand(car.getCarId()).toUri();
            return ResponseEntity.created(location).build();
        }

        return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PutMapping
    public ResponseEntity modifyCar(@RequestBody Car car) {
        Optional<Car> first = carService.getCarList()
                .stream().filter(c -> c.getCarId() == car.getCarId()).findFirst();

        if(first.isPresent()) {
            carService.getCarList().remove(first.get());
            carService.getCarList().add(car);

            return new ResponseEntity(HttpStatus.OK);
        }

        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }

    @PatchMapping(produces = {
            MediaType.APPLICATION_XML_VALUE,
            MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity modifyColorCarById(@RequestBody CarColorOnly car) {
        Optional<Car> first = carService.getCarList().stream().filter(c -> c.getCarId() == car.getId()).findFirst();

        if(first.isPresent()) {
            first.get().setColor(car.getColor());

            return new ResponseEntity(first.get(), HttpStatus.OK);
        }

        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping(produces = {
            MediaType.APPLICATION_XML_VALUE,
            MediaType.APPLICATION_JSON_VALUE},
            path = "/{id}")
    public ResponseEntity removeCar(@PathVariable long id) {
        Optional<Car> first = carService.getCarList().stream().filter(c -> c.getCarId() == id).findFirst();

        if (first.isPresent()) {
            carService.getCarList().remove(first.get());
            return new ResponseEntity(first.get(), HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


}
