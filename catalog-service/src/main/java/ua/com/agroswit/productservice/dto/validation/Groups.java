package ua.com.agroswit.productservice.dto.validation;

public interface Groups {
    interface Create {}
    interface PartialUpdate {}
    interface FullUpdate {}
    interface Full extends Create, PartialUpdate, FullUpdate {}
}
