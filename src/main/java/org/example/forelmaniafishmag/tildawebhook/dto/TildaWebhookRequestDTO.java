package org.example.forelmaniafishmag.tildawebhook.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
public class TildaWebhookRequestDTO {

    @JsonProperty("Name")
    @NotBlank(message = "Name is required")
    private String name;

    @JsonProperty("Email")
    private String email;

    @JsonProperty("Phone")
    @NotBlank(message = "Phone is required")
    private String phone;

    @JsonProperty("payment")
    private PaymentDTO payment;

    @Data
    public static class PaymentDTO {

        @JsonProperty("orderid")
        @NotBlank(message = "orderId is required")
        private String orderId;

        @JsonProperty("products")
        private List<ProductDTO> products;

        @JsonProperty("subtotal")
        private String subtotal;

        @JsonProperty("delivery")
        @NotBlank(message = "delivery is required")
        private String delivery;

        @JsonProperty("delivery_price")
        private String deliveryPrice;

        @Data
        public static class ProductDTO {

            @JsonProperty("name")
            @NotBlank(message = "name of product is required")
            private String name;

            @JsonProperty("quantity")
            private String quantity;

            @JsonProperty("price")
            private String price;
        }
    }
}
