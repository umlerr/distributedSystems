package umlerr.servicelistings.service;

import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import umlerr.servicelistings.model.Listing;

@Service
@RequiredArgsConstructor
@Transactional
public class KafkaService {
    private final ListingsService listingService;
    private final RestTemplate restTemplate;
    private static final Logger log = LoggerFactory.getLogger(KafkaService.class);

    @KafkaListener(topics = "cars-topic", groupId = "listing-group")
    public void handleCarCreatedEvent(String carId) {
        UUID carUUID = UUID.fromString(carId);
        String url = "http://localhost:8081/api/cars/v1/car/" + carUUID;

        try {
            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                }
            );

            Map<String, Object> carData = response.getBody();

            if (carData == null) {
                log.warn("Автомобиль с ID {} не найден или данные пустые. Пропускаем создание объявления.", carUUID);
                return;
            }

            Listing listing = new Listing();
            listing.setCarId(carUUID);
            listingService.addListing(listing);

        } catch (HttpClientErrorException.NotFound e) {
            log.warn("Автомобиль с ID {} не найден или машина с таким VIN уже существует. Пропускаем создание объявления.", carUUID);
        } catch (HttpServerErrorException e) {
            log.error("Ошибка сервера при запросе автомобиля с ID {}: {}", carUUID, e.getMessage());
        } catch (Exception e) {
            log.error("Неожиданная ошибка при обработке события для автомобиля с ID {}: {}", carUUID, e.getMessage());
        }
    }

}
