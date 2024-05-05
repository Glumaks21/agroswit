package ua.com.agroswit.productservice.dto.request;

public interface Views {
    interface CreateView {}
    interface PartialUpdateView {}
    interface FullUpdateView {}
    interface FullView extends CreateView, PartialUpdateView, FullUpdateView {}
}