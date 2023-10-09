package ua.dev.gal.spelling.trainer.openai;

import com.theokanning.openai.service.OpenAiService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAIConf {

    @Bean
    public OpenAiService openAiService(@Value("${OPENAI_TOKEN}") String token) {
        return new OpenAiService(token);
    }
}
