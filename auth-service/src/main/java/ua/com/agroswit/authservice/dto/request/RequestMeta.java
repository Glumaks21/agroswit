package ua.com.agroswit.authservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record RequestMeta(

        @Pattern(regexp = "^((25[0-5]|(2[0-4]|1\\d|[1-9]|)\\d)\\.?\\b){4}$", message = "Incorrect IPv4 address")
        @NotBlank(message = "IP must not be blank")
        String ip,

        @NotBlank(message = "Footprint must not be blank")
        String footprint
) {
    public RequestMeta(String ip, String footprint) {
        this.ip = ip != null? ip.trim(): null;
        this.footprint = footprint != null? footprint.trim(): null;
    }
}
