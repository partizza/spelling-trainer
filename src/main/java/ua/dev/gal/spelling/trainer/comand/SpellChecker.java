package ua.dev.gal.spelling.trainer.comand;

import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.command.annotation.Command;
import org.springframework.shell.component.StringInput;
import org.springframework.shell.standard.AbstractShellComponent;
import ua.dev.gal.spelling.trainer.comand.renderer.SpellCheckInputRenderer;
import ua.dev.gal.spelling.trainer.openai.service.SpellCheckService;

import java.io.PrintWriter;

@Log4j2
@Command(group = "Spelling trainer")
public class SpellChecker extends AbstractShellComponent {

    static final String EXIT_KEYWORD = "exit";

    private final SpellCheckService spellCheckService;

    @Autowired
    public SpellChecker(@NonNull SpellCheckService spellCheckService) {
        this.spellCheckService = spellCheckService;
    }

    @Command(description = "Input word/phrase and then receive spell check result with corrected input, if mistake was made")
    public void spellCheck() {
        StringInput stringInput = new StringInput(getTerminal());
        stringInput.setRenderer(new SpellCheckInputRenderer(EXIT_KEYWORD));

        while (true) {
            StringInput.StringInputContext context = stringInput.run(StringInput.StringInputContext.empty());

            if (EXIT_KEYWORD.equalsIgnoreCase(context.getResultValue())) {
                LOGGER.debug("Exit spell checking, input: {}", context.getResultValue());
                break;
            }

            String checkResult = spellCheckService.check(context.getResultValue());
            LOGGER.debug("Input to check: '{}'. Check result: '{}'", context::getResultValue, () -> checkResult);
            print(checkResult);
        }
    }

    private void print(@NonNull String str) {
        PrintWriter writer = getTerminal().writer();
        writer.println(str);
        writer.println();
        writer.flush();
    }
}
