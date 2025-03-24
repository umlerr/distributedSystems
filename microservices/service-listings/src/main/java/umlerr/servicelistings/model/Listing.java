package umlerr.servicelistings.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import umlerr.servicelistings.enums.Status;

@Builder
@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Table(name = "listings")
public class Listing {

    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private UUID carId;

    private UUID userId;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @Builder.Default
    private LocalDateTime expiresAt = LocalDateTime.now().plusMonths(1);

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private Status status = Status.AVAILABLE;

    @Version
    @Builder.Default
    private Long version = 0L;

    @Builder.Default
    private Boolean actual = true;
}
