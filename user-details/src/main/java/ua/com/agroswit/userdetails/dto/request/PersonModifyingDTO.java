package ua.com.agroswit.userdetails.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.annotation.JsonView;
import ua.com.agroswit.userdetails.dto.response.CustomerDTO;
import ua.com.agroswit.userdetails.dto.response.UserServiceDTO;
import ua.com.agroswit.userdetails.dto.view.Views.CreateView;
import ua.com.agroswit.userdetails.dto.view.Views.UpdateView;
import ua.com.agroswit.userdetails.model.enums.Sex;
import ua.com.agroswit.userdetails.model.enums.UkrainianDistrict;

import java.time.LocalDate;

@JsonView({CreateView.class, UpdateView.class})
public record PersonModifyingDTO(
        @JsonUnwrapped
        CustomerModifyingDTO customer,
        String idNumber,
        Sex sex,
        LocalDate birthDate
) {
    @JsonCreator
    public PersonModifyingDTO(Integer userId,
                              UkrainianDistrict district,
                              String region,
                              String settlement,
                              String idNumber,
                              Sex sex,
                              LocalDate birthDate) {
        this(new CustomerModifyingDTO(userId, district, region, settlement), idNumber, sex, birthDate);
    }

    public PersonModifyingDTO(CustomerModifyingDTO customer, String idNumber, Sex sex, LocalDate birthDate) {
        this.customer = customer;
        this.idNumber = idNumber;
        this.sex = sex;
        this.birthDate = birthDate;
    }
}
