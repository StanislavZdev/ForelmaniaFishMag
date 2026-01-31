package org.example.forelmaniafishmag.tildawebhook.model;


import jakarta.persistence.*;
import lombok.*;
import org.example.forelmaniafishmag.tildawebhook.dto.TildaWebhookRequestDTO;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "orders")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class TildaWebhookData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "delivery", nullable = false)
    private String delivery;

    @Column(name = "delivery_price")
    private Double deliveryPrice;

    @Column(name = "orderid", nullable = false)
    private String orderId;

    @Column(name = "subtotal")
    private Double subtotal;

    @Column(name = "submitted_at")
    private LocalDateTime submittedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    @ToString.Exclude
    private ClientsModel client;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "webhook_product",
            joinColumns = @JoinColumn(name = "webhook_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    @Builder.Default
    @ToString.Exclude
    private Set<ProductsModel> products = new HashSet<>();

    public TildaWebhookData(ClientsModel client, String delivery,
                            Double deliveryPrice, String orderId,
                            Double subtotal, LocalDateTime submittedAt) {
        this.client = client;
        this.delivery = delivery;
        this.deliveryPrice = deliveryPrice;
        this.orderId = orderId;
        this.subtotal = subtotal;
        this.submittedAt = submittedAt;
    }
    public void addProduct(ProductsModel product) {
        this.products.add(product);
    }
}
