package pl.com.app.service.listeners;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import pl.com.app.model.Visit;

@Getter
public class OnRemindVisitEvenData extends ApplicationEvent {

    private final Visit visit;

    public OnRemindVisitEvenData(Visit visit) {
        super(visit);
        this.visit = visit;
    }
}
