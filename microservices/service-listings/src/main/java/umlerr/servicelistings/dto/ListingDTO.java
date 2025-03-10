package umlerr.servicelistings.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import umlerr.servicelistings.enums.Status;
import java.time.LocalDateTime;

@NoArgsConstructor @AllArgsConstructor
@Getter @Setter
public class ListingDTO {
    private LocalDateTime expiresAt;
    private Status status;
}
