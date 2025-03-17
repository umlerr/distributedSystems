package umlerr.servicecars.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Table(name = "cars")
public class Car {

    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Pattern(regexp = "^[A-HJ-NPR-Z0-9]{17}$", message = "VIN номер должен содержать 17 символов, исключая I, O, Q")
    @Column(nullable = false, unique = true)
    private String vin;

    @Pattern(regexp = "^[A-Za-z\\s]{1,20}$", message = "Бренд не должен содержать цифры и должен быть не более 20 символов")
    private String brand;

    @Pattern(regexp = "^[A-Za-z0-9\\s]{1,20}$", message = "Модель должна быть не более 20 символов и может содержать цифры")
    private String model;

    @Pattern(regexp = "^[0-9]{1,20}$", message = "Цена должна быть числом и не более 20 символов")
    private String price;

    @Min(value = 1885, message = "Год выпуска не может быть раньше 1885")
    @Max(value = 2025, message = "Год выпуска не может быть больше текущего года")
    private Integer year;

    @Pattern(regexp = "^[0-9]{1,8}$", message = "Пробег должен быть числом и не более 8 цифр")
    private Integer mileage;

    @Version
    private Long version;

    @Builder.Default
    private Boolean actual = true;
}
