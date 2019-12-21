package pl.com.app.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import pl.com.app.parsers.*;

@Configuration
@ComponentScan(basePackages = {"pl.com.app"})
public class ConverterConfig {

    @Bean
    public AddressesConverter addressesConverter() {
        return new AddressesConverter(FileNames.ADDRESSES);
    }

    @Bean
    public DiseaseConverter diseaseConverter() {
        return new DiseaseConverter(FileNames.DISEASES);
    }

    @Bean
    public DoctorsConverter doctorsConverter() {
        return new DoctorsConverter(FileNames.DOCTORS);
    }

    @Bean
    public UsersConverter usersConverter() {
        return new UsersConverter(FileNames.USERS);
    }

    @Bean
    public OpinionsConverter opinionsConverter() {
        return new OpinionsConverter(FileNames.OPINIONS);
    }

    @Bean
    public PatientsConverter patientsConverter() {
        return new PatientsConverter(FileNames.PATIENTS);
    }

    @Bean
    public SpecializationsConverter specializationsConverter() {
        return new SpecializationsConverter( FileNames.SPECIALIZATIONS);
    }

    @Bean
    public VisitsConverter visitsConverter() {
        return new VisitsConverter( FileNames.VISITS);
    }

    @Bean
    public VisitsConverter visitsDataConverter() {
        return new VisitsConverter(FileNames.VISIT_DATA);
    }

    @Bean
    public WorkplacesConverter workplacesConverter() {
        return new WorkplacesConverter( FileNames.WORKPLACES);
    }

    @Bean
    public PatientCardsConverter patientCardsConverter() {
        return new PatientCardsConverter( FileNames.PATIENT_CARDS);
    }

    @Bean
    public AdvicesConverter advicesConverter() {
        return new AdvicesConverter( FileNames.ADVICES);
    }

    @Bean
    public RolesConverter rolesConverter() {
        return new RolesConverter( FileNames.ROLES);
    }
}
