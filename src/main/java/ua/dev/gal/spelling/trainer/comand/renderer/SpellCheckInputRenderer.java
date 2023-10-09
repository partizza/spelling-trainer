package ua.dev.gal.spelling.trainer.comand.renderer;

import lombok.NonNull;
import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStringBuilder;
import org.springframework.shell.component.StringInput;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

public final class SpellCheckInputRenderer implements Function<StringInput.StringInputContext, List<AttributedString>> {

    private final String exitKeyword;

    public SpellCheckInputRenderer(@NonNull String exitKeyword) {
        this.exitKeyword = exitKeyword;
    }

    @Override
    public List<AttributedString> apply(@NonNull StringInput.StringInputContext context) {
        AttributedStringBuilder builder = new AttributedStringBuilder();

        if (context.getResultValue() != null) {
            builder.append(context.getResultValue());
        } else {
            String input = context.getInput();
            if (StringUtils.hasText(input)) {
                builder.append(input);
            } else {
                builder.append(String.format("[Enter text to check spelling or '%s' to exit]", exitKeyword));
            }
        }

        return Arrays.asList(builder.toAttributedString());
    }
}
