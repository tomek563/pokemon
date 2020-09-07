package pokemon.pl.pokemon.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Coach {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String coachName;

    @OneToOne
    private AppUser appUser;

    private int amountMoney = 100;

    @OneToMany
    private List<Card> cards = new ArrayList<>();

    public Coach() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCoachName() {
        return coachName;
    }

    public void setCoachName(String coachName) {
        this.coachName = coachName;
    }

    public AppUser getAppUser() {
        return appUser;
    }

    public void setAppUser(AppUser appUser) {
        this.appUser = appUser;
    }

    public int getAmountMoney() {
        return amountMoney;
    }

    public void setAmountMoney(int amountMoney) {
        this.amountMoney = amountMoney;
    }

    public List<Card> getCards() {
        return cards;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }

    @Override
    public String toString() {
        return coachName;
    }
}
