package ua.com.agroswit.orderservice.dto;


import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.Set;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.READ_ONLY;

public record OrderDTO(

        @JsonProperty(access = READ_ONLY)
        Integer id,

        @JsonProperty(access = READ_ONLY)
        Integer customerId,

        @JsonProperty(access = READ_ONLY)
        String state,

        Set<OrderItemDTO> items,

        @JsonProperty(access = READ_ONLY)
        LocalDateTime createdAt
) {
}
