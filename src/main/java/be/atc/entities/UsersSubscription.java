package be.atc.entities;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "users_subscriptions")
public class UsersSubscription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_subscription_id", nullable = false)
    private int id;

    @Column(name = "begin_date_subscription", nullable = false)
    private Instant beginDateSubscription;

    @Column(name = "end_date_subscription", nullable = false)
    private Instant endDateSubscription;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_subscription_id")
    private Subscription fkSubscription;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_user_id")
    private User fkUser;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Instant getBeginDateSubscription() {
        return beginDateSubscription;
    }

    public void setBeginDateSubscription(Instant beginDateSubscription) {
        this.beginDateSubscription = beginDateSubscription;
    }

    public Instant getEndDateSubscription() {
        return endDateSubscription;
    }

    public void setEndDateSubscription(Instant endDateSubscription) {
        this.endDateSubscription = endDateSubscription;
    }

    public Subscription getFkSubscription() {
        return fkSubscription;
    }

    public void setFkSubscription(Subscription fkSubscription) {
        this.fkSubscription = fkSubscription;
    }

    public User getFkUser() {
        return fkUser;
    }

    public void setFkUser(User fkUser) {
        this.fkUser = fkUser;
    }

}