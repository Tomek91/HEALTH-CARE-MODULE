package pl.com.app.service.listeners;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import pl.com.app.dto.MailSenderDTO;
import pl.com.app.model.Advice;
import pl.com.app.service.MailService;

import static j2html.TagCreator.*;

@Component
@RequiredArgsConstructor
public class AnswerAdviceListener implements ApplicationListener<OnAnswerAdviceEvenData> {

    private final MailService mailService;

    @Override
    public void onApplicationEvent(OnAnswerAdviceEvenData data) {
        sendAnswerAdviceEmail(data);
    }

    private void sendAnswerAdviceEmail(OnAnswerAdviceEvenData data) {

        Advice advice = data.getAdvice();

        String recipientAddress = advice.getEmail();
        String subject = "HEALTH-CARE-APP: answer to your advice";

        String message =
                body(
                        h2("Hello!"),
                        h3("This is Health-Care-App."),
                        h3(
                                text("You asking about: " + advice.getQuestion() + ". "),
                                text("This is our answer: "),
                                text(advice.getAnswer())
                        )
                ).render();


        mailService.sendMail(
                MailSenderDTO.builder()
                        .recipientAddress(recipientAddress)
                        .subject(subject)
                        .message(message)
                        .build()
        );
    }
}
