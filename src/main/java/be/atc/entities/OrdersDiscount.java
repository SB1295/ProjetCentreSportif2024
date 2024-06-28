package be.atc.entities;

import javax.persistence.*;

@Entity
@Table(name = "orders_discounts")
public class OrdersDiscount {
    @Id
    @Column(name = "order_discount_id", nullable = false)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "fk_discount_id", nullable = false)
    private Discount fkDiscount;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "fk_order_id", nullable = false)
    private Order fkOrder;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Discount getFkDiscount() {
        return fkDiscount;
    }

    public void setFkDiscount(Discount fkDiscount) {
        this.fkDiscount = fkDiscount;
    }

    public Order getFkOrder() {
        return fkOrder;
    }

    public void setFkOrder(Order fkOrder) {
        this.fkOrder = fkOrder;
    }

}