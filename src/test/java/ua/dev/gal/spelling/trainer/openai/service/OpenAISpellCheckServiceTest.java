package ua.dev.gal.spelling.trainer.openai.service;

import com.theokanning.openai.completion.chat.ChatCompletionChoice;
import com.theokanning.openai.completion.chat.ChatCompletionResult;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;
import com.theokanning.openai.service.OpenAiService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OpenAISpellCheckServiceTest {

    @Mock
    private OpenAiService openAiServiceMock;
    @InjectMocks
    private OpenAISpellCheckService sut;

    @Test
    void shouldFailConstructionOnNull() {
        assertThrows(IllegalArgumentException.class, () -> new OpenAISpellCheckService(null));
    }

    @Test
    void shouldReturnEmptyStringOnNullInput() {
        assertEquals("", sut.check(null));
    }

    @Test
    void shouldReturnEmptyStringOnEmptyInput() {
        assertEquals("", sut.check(""));
        assertEquals("", sut.check("  "));
    }

    @Test
    void shouldCallOpenAIAndReturnResult() {
        String input = "text to check";
        String expectedResult = "It's OK.";

        ChatCompletionResult chatCompletionResult = new ChatCompletionResult();
        ChatCompletionChoice chatCompletionChoice = new ChatCompletionChoice();
        chatCompletionChoice.setMessage(new ChatMessage(ChatMessageRole.ASSISTANT.value(), expectedResult));
        chatCompletionResult.setChoices(Collections.singletonList(chatCompletionChoice));

        when(openAiServiceMock.createChatCompletion(argThat(req -> req.getMessages()
                .stream()
                .map(ChatMessage::getContent)
                .anyMatch(content -> content.contains(input))))
        ).thenReturn(chatCompletionResult);


        String result = sut.check(input);

        assertEquals(expectedResult, result);
        verify(openAiServiceMock, times(1)).createChatCompletion(any());
    }

    @Test
    void shouldCallOpenAIAndReturnResultIfResponseEmpty() {
        String input = "text to check";
        String expectedResult = "It's OK.";

        ChatCompletionResult chatCompletionResult = new ChatCompletionResult();
        chatCompletionResult.setChoices(Collections.emptyList());

        when(openAiServiceMock.createChatCompletion(argThat(req -> req.getMessages()
                .stream()
                .map(ChatMessage::getContent)
                .anyMatch(content -> content.contains(input))))
        ).thenReturn(chatCompletionResult);


        String result = sut.check(input);

        assertTrue(result.startsWith("Does not have check spelling results"));
        verify(openAiServiceMock, times(1)).createChatCompletion(any());
    }
}