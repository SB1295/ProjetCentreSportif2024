package be.atc.entities;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id", nullable = false)
    private int id;

    @Column(name = "date", nullable = false)
    private Instant date;

    @Lob
    @Column(name = "statut", nullable = false)
    private String statut;

    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "fk_user_id", nullable = false)
    private User fkUser;

    @OneToMany(mappedBy = "fkOrder")
    private Set<OrdersDiscount> ordersDiscounts = new LinkedHashSet<>();

    @OneToOne(mappedBy = "fkOrder")
    private OrdersSubscription ordersSubscription;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Instant getDate() {
        return date;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public User getFkUser() {
        return fkUser;
    }

    public void setFkUser(User fkUser) {
        this.fkUser = fkUser;
    }

    public Set<OrdersDiscount> getOrdersDiscounts() {
        return ordersDiscounts;
    }

    public void setOrdersDiscounts(Set<OrdersDiscount> ordersDiscounts) {
        this.ordersDiscounts = ordersDiscounts;
    }

    public OrdersSubscription getOrdersSubscription() {
        return ordersSubscription;
    }

    public void setOrdersSubscription(OrdersSubscription ordersSubscription) {
        this.ordersSubscription = ordersSubscription;
    }

}