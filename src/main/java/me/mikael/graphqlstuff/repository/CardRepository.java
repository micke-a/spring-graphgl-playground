package me.mikael.graphqlstuff.repository;

import me.mikael.graphqlstuff.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CardRepository extends JpaRepository<Card,Long> {

    List<Card> findAllByCardHolder(String cardHolder);
}
