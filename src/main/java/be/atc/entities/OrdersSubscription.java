package be.atc.entities;

import javax.persistence.*;

@Entity
@Table(name = "orders_subscriptions")
public class OrdersSubscription {
    @Id
    @Column(name = "order_subscription_id", nullable = false)
    private int id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "fk_subscription_id", nullable = false)
    private Subscription fkSubscription;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "fk_order_id", nullable = false)
    private Order fkOrder;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Subscription getFkSubscription() {
        return fkSubscription;
    }

    public void setFkSubscription(Subscription fkSubscription) {
        this.fkSubscription = fkSubscription;
    }

    public Order getFkOrder() {
        return fkOrder;
    }

    public void setFkOrder(Order fkOrder) {
        this.fkOrder = fkOrder;
    }

}