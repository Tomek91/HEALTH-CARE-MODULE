package pl.com.app.service.listeners;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import pl.com.app.model.Advice;

@Getter
public class OnAnswerAdviceEvenData extends ApplicationEvent {

    private final String url;
    private final Advice advice;

    public OnAnswerAdviceEvenData(String url, Advice advice) {
        super(advice);
        this.url = url;
        this.advice = advice;
    }
}
