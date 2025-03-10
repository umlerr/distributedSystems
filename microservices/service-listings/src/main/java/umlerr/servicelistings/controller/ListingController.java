package umlerr.servicelistings.controller;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import umlerr.servicelistings.dto.ListingDTO;
import umlerr.servicelistings.model.Listing;
import umlerr.servicelistings.service.ListingsService;
import java.util.UUID;
import static umlerr.servicelistings.util.ListingsUtils.getListingDeleted;
import static umlerr.servicelistings.util.ListingsUtils.getListingEdited;

@RestController
@RequiredArgsConstructor
@RequestMapping("/catalog")
public class ListingController {

    private final ListingsService listingsService;

    @GetMapping("/listings")
    public ResponseEntity<?> getAllListings(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size
    ) {
        Page<Listing> listings = listingsService.getAllListings(page, size);
        return ResponseEntity.status(HttpStatus.OK).body(listings);
    }

    @GetMapping("/listings/count")
    public ResponseEntity<?> getListingsCount() {
        return ResponseEntity.status(HttpStatus.OK).body(listingsService.getListingsCount());
    }

    @GetMapping("/listing/{id}")
    public ResponseEntity<?> getListingById(@PathVariable UUID id) {
        return ResponseEntity.status(HttpStatus.OK).body(listingsService.getListingById(id));
    }


    @PutMapping("/update-listing/{id}")
    public ResponseEntity<?> updateListing(@PathVariable UUID id, @RequestBody ListingDTO listingDTO) {
        listingsService.updateListing(id, listingDTO);
        return ResponseEntity.status(HttpStatus.OK).body(getListingEdited());
    }

    @DeleteMapping("/delete-listing/{id}")
    public ResponseEntity<?> deleteListing(@PathVariable UUID id) {
        listingsService.deleteListing(id);
        return ResponseEntity.status(HttpStatus.OK).body(getListingDeleted(id));
    }
}
