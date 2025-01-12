package ru.ikm.ikm.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@Embeddable
public class OrderItemId implements Serializable {
    private static final long serialVersionUID = -5111091256733010838L;
    @Column(name = "order_id", nullable = false)
    private Integer orderId;

    @Column(name = "menu_id", nullable = false)
    private Integer menuId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        OrderItemId entity = (OrderItemId) o;
        return Objects.equals(this.orderId, entity.orderId) &&
                Objects.equals(this.menuId, entity.menuId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderId, menuId);
    }

}