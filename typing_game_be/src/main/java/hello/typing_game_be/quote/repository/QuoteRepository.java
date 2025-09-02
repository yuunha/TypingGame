package hello.typing_game_be.quote.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import hello.typing_game_be.quote.entity.Quote;

public interface QuoteRepository extends JpaRepository<Quote, Long> {
}
