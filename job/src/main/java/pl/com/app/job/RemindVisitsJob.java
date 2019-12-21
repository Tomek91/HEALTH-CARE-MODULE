package pl.com.app.job;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pl.com.app.service.listeners.OnRemindVisitEvenData;
import pl.com.app.service.VisitService;
import pl.com.app.service.mappers.ModelMapper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
@Slf4j
@RequiredArgsConstructor
public class RemindVisitsJob {

    private final ApplicationEventPublisher eventPublisher;
    private final VisitService visitService;
    private final ModelMapper modelMapper;

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @Scheduled(cron = "0 0 9 * * *")
    public void reportCurrentTime() {

        visitService.getAllVisits()
                .stream()
                .filter(x -> LocalDate.now().plusDays(1L).equals(x.getDate()))
                .map(modelMapper::fromVisitDTOToVisit)
                .forEach(visit -> {
                    eventPublisher.publishEvent(new OnRemindVisitEvenData(visit));
                    log.info("{} - send remind mail to {}, visit at {} - ",
                            dateTimeFormatter.format(LocalDateTime.now()),
                            visit.getPatient().toString(),
                            dateTimeFormatter.format(visit.getDateTime()));
                });
    }
}
