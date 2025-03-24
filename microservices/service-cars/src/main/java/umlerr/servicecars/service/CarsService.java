package umlerr.servicecars.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umlerr.servicecars.dto.CarDTO;
import umlerr.servicecars.dto.CarMapper;
import umlerr.servicecars.model.Car;
import umlerr.servicecars.repository.CarsRepository;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import static umlerr.servicecars.util.CarsUtils.getCarNotFound;

@Service
@RequiredArgsConstructor
public class CarsService {

    private final CarsRepository carsRepository;
    private final CarMapper carMapper;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Transactional(readOnly = true)
    public Page<Car> getAllCars(int page, int size) {
        return carsRepository.findAllByActualTrue(PageRequest.of(page, size));
    }

    @Transactional(readOnly = true)
    public Page<Car> getCarsByBrand(String brand, int page, int size) {
        return carsRepository.findAllByBrandAndActualTrue(brand, PageRequest.of(page, size));
    }

    @Transactional(readOnly = true)
    public Car getCarByVin(String vin) {
        return carsRepository.findByVin(vin);
    }

    public List<String> getAllVin() {
        return carsRepository.findAllVinNumbers();
    }

    @Transactional
    public long getCarsCount() {
        return carsRepository.countByActualTrue();
    }

    @Transactional(readOnly = true)
    public Car getCarById(UUID id) {
        return carsRepository.findById(id)
            .orElseThrow(() -> new NoSuchElementException(getCarNotFound(id)));
    }

    @Transactional
    public void updateCar(UUID id, CarDTO carDTO) {
        Car car = carsRepository.findById(id)
            .orElseThrow(() -> new NoSuchElementException(getCarNotFound(id)));
        if (carDTO.getPrice() != null) {
            car.setPrice(carDTO.getPrice());
        }
        if (carDTO.getMileage() != null) {
            car.setMileage(carDTO.getMileage());
        }
        carsRepository.save(car);
    }

    @Transactional
    public void deleteCar(UUID id) {
        Car car = carsRepository.findById(id)
            .orElseThrow(() -> new NoSuchElementException(getCarNotFound(id)));
        car.setActual(false);
        carsRepository.save(car);
    }

    @Transactional
    public void addCar(CarDTO carDTO, String authorizationHeader) {
        String token = null;
        String TOKEN_PREFIX = "Bearer ";

        if (authorizationHeader != null && authorizationHeader.startsWith(TOKEN_PREFIX)) {
            token = authorizationHeader.replace(TOKEN_PREFIX, "");
        }

        if (token == null) {
            throw new IllegalArgumentException("Токен не найден или неверный формат.");
        }

        Car car = carMapper.toEntity(carDTO);
        car = carsRepository.save(car);

        String carDataJson = String.format("{\"carId\":\"%s\", \"token\":\"%s\"}", car.getId().toString(), token);

        kafkaTemplate.send("cars-topic", carDataJson);
    }
}
