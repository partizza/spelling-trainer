package ua.dev.gal.spelling.trainer.openai.service;

import com.theokanning.openai.completion.chat.ChatCompletionChoice;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;
import com.theokanning.openai.service.OpenAiService;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Collections;

@Log4j2
@Service
public class OpenAISpellCheckService implements SpellCheckService {

    private final OpenAiService openAiService;

    @Autowired
    public OpenAISpellCheckService(@NonNull OpenAiService openAiService) {
        this.openAiService = openAiService;
    }

    @Override
    public String check(String str) {
        if (!StringUtils.hasText(str)) {
            LOGGER.debug("Skip request to OpenAI - input is empty");
            return "";
        }

        LOGGER.debug("Sending request to OpenAI, input: {}", str);
        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder()
                .model("gpt-3.5-turbo")
                .temperature(0.0d)
                .messages(Collections.singletonList(new ChatMessage(ChatMessageRole.USER.value(), String.format("Check spelling of \"%s\"", str))))
                .build();

        return openAiService.createChatCompletion(chatCompletionRequest)
                .getChoices()
                .stream()
                .findFirst()
                .map(ChatCompletionChoice::getMessage)
                .map(ChatMessage::getContent)
                .orElse(String.format("Does not have check spelling results of \"%s\"", str));
    }
}
