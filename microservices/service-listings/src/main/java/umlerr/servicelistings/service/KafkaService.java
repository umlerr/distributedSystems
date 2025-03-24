package umlerr.servicelistings.service;

import java.util.Map;
import java.util.UUID;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import umlerr.jwt.JWTService;
import umlerr.servicelistings.model.Listing;

@Service
@RequiredArgsConstructor
@Transactional
public class KafkaService {
    private final ListingsService listingService;
    private final RestTemplate restTemplate;
    private final JWTService jwtService;
    private static final Logger log = LoggerFactory.getLogger(KafkaService.class);

    @KafkaListener(topics = "cars-topic", groupId = "listing-group")
    public void handleCarCreatedEvent(String carDataJson) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode carData = objectMapper.readTree(carDataJson);

            String carId = carData.get("carId").asText();
            String token = carData.get("token").asText();

            UUID carUUID = UUID.fromString(carId);
            UUID userID = UUID.fromString(jwtService.extractUserId(token));
            String url = "http://localhost:8081/api/cars/v1/car-by-id/" + carUUID;

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + token);

            HttpEntity<?> entity = new HttpEntity<>(headers);

            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<>() {}
            );

            Map<String, Object> carDetails = response.getBody();

            if (carDetails == null) {
                log.warn("Автомобиль с ID {} не найден или данные пустые. Пропускаем создание объявления.", carUUID);
                return;
            }

            Listing listing = new Listing();
            listing.setCarId(carUUID);
            listing.setUserId(userID);
            listingService.addListing(listing);

        } catch (HttpClientErrorException.NotFound e) {
            log.warn("Автомобиль не найден или машина с таким VIN уже существует. Пропускаем создание объявления.");
        } catch (HttpServerErrorException e) {
            log.error("Ошибка сервера при запросе автомобиля {}", e.getMessage());
        } catch (Exception e) {
            log.error("Неожиданная ошибка при обработке события для автомобиля {}", e.getMessage());
        }
    }

}
