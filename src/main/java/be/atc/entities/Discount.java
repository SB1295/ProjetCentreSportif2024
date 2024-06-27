package be.atc.entities;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "discounts")
public class Discount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "discount_id", nullable = false)
    private int id;

    @Column(name = "discount_label", nullable = false)
    private String discountLabel;

    @Column(name = "percent", nullable = false, precision = 5, scale = 2)
    private BigDecimal percent;

    @OneToMany(mappedBy = "fkDiscount")
    private Set<OrdersDiscount> ordersDiscounts = new LinkedHashSet<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDiscountLabel() {
        return discountLabel;
    }

    public void setDiscountLabel(String discountLabel) {
        this.discountLabel = discountLabel;
    }

    public BigDecimal getPercent() {
        return percent;
    }

    public void setPercent(BigDecimal percent) {
        this.percent = percent;
    }

    public Set<OrdersDiscount> getOrdersDiscounts() {
        return ordersDiscounts;
    }

    public void setOrdersDiscounts(Set<OrdersDiscount> ordersDiscounts) {
        this.ordersDiscounts = ordersDiscounts;
    }

}