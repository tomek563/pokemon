package pokemon.pl.pokemon.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Entity;

import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.util.Objects;

@Entity
public class Card {

    @Id
    private String id;

    private String name;

    @JsonProperty(value = "imageUrl")
    private String url;

    private int price = 50; /*default value*/

    private boolean onSale = false;

    @ManyToOne
    private Coach coach;

    public Card() {
    }

    public Card(String id, String name, int price, boolean onSale) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.onSale = onSale;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public boolean isOnSale() {
        return onSale;
    }

    public void setOnSale(boolean onSale) {
        this.onSale = onSale;
    }

    public Coach getCoach() {
        return coach;
    }

    public void setCoach(Coach coach) {
        this.coach = coach;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return Objects.equals(id, card.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Card{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", price=" + price +
                ", onSale=" + onSale +
                ", coach=" + coach +
                '}';
    }
}
