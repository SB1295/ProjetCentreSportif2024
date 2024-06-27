package be.atc.entities;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "subscriptions")
public class Subscription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "subscription_id", nullable = false)
    private int id;

    @Column(name = "label", nullable = false)
    private String label;

    @Column(name = "price", nullable = false)
    private int price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_sport_id")
    private Sport fkSport;

    @OneToOne(mappedBy = "fkSubscription")
    private OrdersSubscription ordersSubscription;

    @OneToMany(mappedBy = "fkSubscription")
    private Set<UsersSubscription> usersSubscriptions = new LinkedHashSet<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public Sport getFkSport() {
        return fkSport;
    }

    public void setFkSport(Sport fkSport) {
        this.fkSport = fkSport;
    }

    public OrdersSubscription getOrdersSubscription() {
        return ordersSubscription;
    }

    public void setOrdersSubscription(OrdersSubscription ordersSubscription) {
        this.ordersSubscription = ordersSubscription;
    }

    public Set<UsersSubscription> getUsersSubscriptions() {
        return usersSubscriptions;
    }

    public void setUsersSubscriptions(Set<UsersSubscription> usersSubscriptions) {
        this.usersSubscriptions = usersSubscriptions;
    }

}