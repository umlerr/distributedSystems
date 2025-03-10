package umlerr.servicecars.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor @AllArgsConstructor
@Getter @Setter
public class CarDTO {
    private String vin;
    private String brand;
    private String model;
    private Double price;
    private Integer year;
    private Integer mileage;
}
