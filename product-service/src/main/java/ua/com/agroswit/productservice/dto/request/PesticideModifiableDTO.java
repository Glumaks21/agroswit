package ua.com.agroswit.productservice.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
public class PesticideModifiableDTO extends ProductModifiableDTO {

    @NotEmpty(message = "Pests must not be empty")
    Set<Integer> pestIds;

    @Valid
    @NotEmpty(message = "Cultures must not be empty")
    Set<PesticideCultureDTO> cultures;

    @Data
    public static class PesticideCultureDTO {

        private Integer cultureId;
        private Double minVolume;
        private Double maxVolume;

    }
}
