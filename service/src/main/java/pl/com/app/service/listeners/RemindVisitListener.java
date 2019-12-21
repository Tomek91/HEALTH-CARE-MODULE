package pl.com.app.service.listeners;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import pl.com.app.dto.MailSenderDTO;
import pl.com.app.model.Visit;
import pl.com.app.service.MailService;

import static j2html.TagCreator.*;

@Component
@RequiredArgsConstructor
public class RemindVisitListener implements ApplicationListener<OnRemindVisitEvenData> {

    private final MailService mailService;

    @Override
    public void onApplicationEvent(OnRemindVisitEvenData data) {
        sendAnswerAdviceEmail(data);
    }

    private void sendAnswerAdviceEmail(OnRemindVisitEvenData data) {

        Visit visit = data.getVisit();

        String recipientAddress = visit.getPatient().getEmail();
        String subject = "HEALTH-CARE-APP: remind visit";

        String message =
                body(
                        h2("Hello!"),
                        h3("This is Health-Care-App."),
                        h3(
                                text("We remind you about visit at " + visit.getDateTime().toString())
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
