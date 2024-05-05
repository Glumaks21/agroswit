package ua.com.agroswit.userdetails.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.annotation.JsonView;
import ua.com.agroswit.userdetails.dto.view.Views.CreateView;
import ua.com.agroswit.userdetails.dto.view.Views.UpdateView;
import ua.com.agroswit.userdetails.model.enums.UkrainianDistrict;

@JsonView({CreateView.class, UpdateView.class})
public record EntrepreneurModifyingDTO(
        @JsonUnwrapped
        CustomerModifyingDTO customer,
        String companyName,
        String idNumber
) {
    @JsonCreator
    public EntrepreneurModifyingDTO(Integer userId,
                                    UkrainianDistrict district,
                                    String region,
                                    String settlement,
                                    String companyName,
                                    String idNumber) {
        this(new CustomerModifyingDTO(userId, district, region, settlement), companyName, idNumber);
    }

    public EntrepreneurModifyingDTO(CustomerModifyingDTO customer, String companyName, String idNumber) {
        this.customer = customer;
        this.companyName = companyName;
        this.idNumber = idNumber;
    }
}
