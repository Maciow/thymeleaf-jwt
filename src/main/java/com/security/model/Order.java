package com.security.model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Map;
import java.util.Objects;

@Entity
@Table(name = "orders")
@Getter
@Setter
@Builder
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class Order implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_order")
    private Long id;
    @ElementCollection
    @CollectionTable(name = "product_quantity",
    joinColumns = {@JoinColumn(name = "order_id")})
    @MapKeyColumn(name = "products")
    @Column(name = "quantity")
    private Map<Long, Integer> products;
    @Column(name = "order_state")
    @Enumerated(EnumType.STRING)
    private OrderState orderState;

    enum OrderState {
        CANCELED, PENDING, APPROVED, ON_THE_WAY, DELIVERED
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(id, order.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
