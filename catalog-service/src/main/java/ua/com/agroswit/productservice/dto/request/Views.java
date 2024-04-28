package ua.com.agroswit.productservice.dto.request;

public interface Views {
    interface Create {}
    interface PartialUpdate {}
    interface FullUpdate {}
    interface Full extends Create, PartialUpdate, FullUpdate {}
}