package umlerr.servicelistings.service;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umlerr.servicelistings.model.Listing;

@Service
@RequiredArgsConstructor
@Transactional
public class KafkaService {
    private final ListingsService listingService;

    @KafkaListener(topics = "cars-topic", groupId = "listing-group")
    public void handleCarCreatedEvent(String carId) {
        Listing listing = new Listing();
        listing.setCarId(UUID.fromString(carId));
        listingService.addListing(listing);
    }
}
