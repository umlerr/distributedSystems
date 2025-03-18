package umlerr.servicecars.controller;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import umlerr.servicecars.dto.CarDTO;
import umlerr.servicecars.model.Car;
import umlerr.servicecars.service.CarsService;
import java.util.List;
import java.util.UUID;
import static umlerr.servicecars.util.CarsUtils.getCarAdded;
import static umlerr.servicecars.util.CarsUtils.getCarDeleted;
import static umlerr.servicecars.util.CarsUtils.getCarEdited;

@RestController
@RequiredArgsConstructor
public class CarsController {

    private final CarsService carsService;

    @GetMapping("/cars")
    public ResponseEntity<?> getAllCars(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size
    ) {
        Page<Car> cars = carsService.getAllCars(page, size);
        return ResponseEntity.status(HttpStatus.OK).body(cars);
    }

    @GetMapping("/cars/by-brand/{brand}")
    public ResponseEntity<?> getCarsByBrand(@PathVariable String brand,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size
    ) {
        Page<Car> cars = carsService.getCarsByBrand(brand, page, size);
        return ResponseEntity.status(HttpStatus.OK).body(cars);
    }

    @GetMapping("/cars/count")
    public ResponseEntity<?> getCarsCount() {
        return ResponseEntity.status(HttpStatus.OK).body(carsService.getCarsCount());
    }

    @GetMapping("/car-by-id/{id}")
    public ResponseEntity<?> getCarById(@PathVariable UUID id) {
        return ResponseEntity.status(HttpStatus.OK).body(carsService.getCarById(id));
    }

    @GetMapping("/car-by-vin/{vin}")
    public ResponseEntity<?> getCarByVin(@PathVariable String vin) {
        return ResponseEntity.status(HttpStatus.OK).body(carsService.getCarByVin(vin));
    }

    @GetMapping("/all-vin")
    public ResponseEntity<?> getAllVin() {
        return ResponseEntity.status(HttpStatus.OK).body(carsService.getAllVin());
    }

    @PostMapping("/add-car")
    public ResponseEntity<?> addCar(@RequestBody CarDTO carDTO, @RequestHeader("Authorization") String authorizationHeader) {
        carsService.addCar(carDTO, authorizationHeader);
        return ResponseEntity.status(HttpStatus.CREATED).body(getCarAdded(carDTO.getBrand() + " " + carDTO.getModel()));
    }

    @PutMapping("/update-car/{id}")
    public ResponseEntity<?> updateCar(@PathVariable UUID id, @RequestBody CarDTO carDTO) {
        carsService.updateCar(id, carDTO);
        return ResponseEntity.status(HttpStatus.OK).body(getCarEdited());
    }

    @DeleteMapping("/delete-car/{id}")
    public ResponseEntity<?> deleteCar(@PathVariable UUID id) {
        carsService.deleteCar(id);
        return ResponseEntity.status(HttpStatus.OK).body(getCarDeleted(id));
    }
}
