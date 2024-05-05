package ua.com.agroswit.userdetails.dto.request;

import com.fasterxml.jackson.annotation.JsonView;
import ua.com.agroswit.userdetails.dto.view.Views.CreateView;
import ua.com.agroswit.userdetails.dto.view.Views.UpdateView;
import ua.com.agroswit.userdetails.model.enums.UkrainianDistrict;

@JsonView({CreateView.class, UpdateView.class})
public record ManagerModifyingDTO(
        @JsonView(CreateView.class)
        Integer userId,
        UkrainianDistrict district
) {
}
