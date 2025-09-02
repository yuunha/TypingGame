package hello.typing_game_be.quote.service;

import java.time.LocalDate;

import org.springframework.stereotype.Service;

import hello.typing_game_be.common.exception.BusinessException;
import hello.typing_game_be.common.exception.ErrorCode;
import hello.typing_game_be.quote.dto.QuoteResponse;
import hello.typing_game_be.quote.entity.Quote;
import hello.typing_game_be.quote.repository.QuoteRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class QuoteService {
    private final QuoteRepository quoteRepository;

    //오늘의 글귀 조회
    public QuoteResponse getTodayQuote() {
        LocalDate today = LocalDate.now();
        long index = today.getDayOfYear() % quoteRepository.count(); //올해에서 며칠째인지 숫자로 반환

        Quote quote = quoteRepository.findById(index)
            .orElseThrow(()->new BusinessException(ErrorCode.QUOTE_NOT_FOUND));
        return new QuoteResponse(quote.getContent(), quote.getAuthor());
    }
}
