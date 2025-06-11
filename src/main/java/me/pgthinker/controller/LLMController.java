package me.pgthinker.controller;

import lombok.RequiredArgsConstructor;
import me.pgthinker.service.LLMService;
import org.springframework.ai.chat.model.Generation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

/**
 * @author NingNing0111
 * @package me.pgthinker.controller
 * @className me.pgthinker.controller.LLMController
 */
@RestController
@RequestMapping("/llm")
@RequiredArgsConstructor
public class LLMController {

    private final LLMService llmService;

    @PostMapping(value="/createMind",produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Generation> createMind(@RequestParam(name = "topic") String topic) {
        return llmService.generateOutlineWithTopic(topic).flatMap(chatResponse -> {
            if (chatResponse == null) {
                return Flux.empty();
            }
            Generation result = chatResponse.getResult();
            if (result == null) {
                return Flux.empty();
            }
            return Flux.just(result);
        });
    }
}
