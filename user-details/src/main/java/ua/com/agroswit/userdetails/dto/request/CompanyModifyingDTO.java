package ua.com.agroswit.userdetails.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.annotation.JsonView;
import ua.com.agroswit.userdetails.dto.view.Views;
import ua.com.agroswit.userdetails.model.enums.UkrainianDistrict;

import java.time.LocalDate;

@JsonView({Views.CreateView.class, Views.UpdateView.class})
public record CompanyModifyingDTO(
        @JsonUnwrapped
        CustomerModifyingDTO customer,
        String egrpou,
        String companyName,
        LocalDate incorporationDate
) {
        @JsonCreator
        public CompanyModifyingDTO( Integer userId,
                                    UkrainianDistrict district,
                                    String region,
                                    String settlement,
                                    String egrpou,
                                    String companyName,
                                    LocalDate incorporationDate) {
                this(new CustomerModifyingDTO(userId, district, region, settlement), egrpou, companyName, incorporationDate);
        }

        public CompanyModifyingDTO(CustomerModifyingDTO customer, String egrpou, String companyName, LocalDate incorporationDate) {
                this.customer = customer;
                this.egrpou = egrpou;
                this.companyName = companyName;
                this.incorporationDate = incorporationDate;
        }
}
