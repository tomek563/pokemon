//package pokemon.pl.pokemon.model;
//
//import javax.persistence.*;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//@Entity
//public class Market {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @OneToMany
//    private List<Card> cards;
//
//    public Market() {
//        this.cards = new ArrayList<>();
//    }
//
//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }
//
//    public List<Card> getCards() {
//        return cards;
//    }
//
//    public void setCards(List<Card> cards) {
//        this.cards = cards;
//    }
//
//
//    @Override
//    public String toString() {
//        return "Market{" +
//                "cards=" + cards +
//                '}';
//    }
//}
