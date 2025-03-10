package umlerr.servicelistings.service;

import java.util.NoSuchElementException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umlerr.servicelistings.dto.ListingDTO;
import umlerr.servicelistings.dto.ListingMapper;
import umlerr.servicelistings.model.Listing;
import umlerr.servicelistings.repository.ListingsRepository;
import static umlerr.servicelistings.util.ListingsUtils.getListingNotFound;

@Service
@RequiredArgsConstructor
public class ListingsService {

    private final ListingsRepository listingsRepository;
    private final ListingMapper listingMapper;

    @Transactional(readOnly = true)
    public Page<Listing> getAllListings(int page, int size) {
        return listingsRepository.findAll(PageRequest.of(page, size));
    }

    @Transactional
    public long getListingsCount() {
        return listingsRepository.count();
    }

    @Transactional(readOnly = true)
    public Listing getListingById(UUID id) {
        return listingsRepository.findById(id)
            .orElseThrow(() -> new NoSuchElementException(getListingNotFound(id)));
    }

    @Transactional
    public void updateListing(UUID id, ListingDTO listingDTO) {
        Listing listing = listingsRepository.findById(id)
            .orElseThrow(() -> new NoSuchElementException(getListingNotFound(id)));
        listingMapper.updateEntityFromDTO(listingDTO, listing);
        listingsRepository.save(listing);
    }

    @Transactional
    public void deleteListing(UUID id) {
        Listing listing = listingsRepository.findById(id)
            .orElseThrow(() -> new NoSuchElementException(getListingNotFound(id)));
        listing.setActual(false);
        listingsRepository.save(listing);
    }

    @Transactional
    public void addListing(Listing listing) {
        listingsRepository.save(listing);
    }
}
