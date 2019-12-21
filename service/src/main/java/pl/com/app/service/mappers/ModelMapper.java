package pl.com.app.service.mappers;

import org.springframework.stereotype.Service;
import pl.com.app.dto.*;
import pl.com.app.model.*;

import java.time.LocalDateTime;
import java.util.HashSet;

@Service
public class ModelMapper {

    public RoleDTO fromRoleToRoleDTO(Role role) {
        return role == null ? null : RoleDTO.builder()
                .id(role.getId())
                .name(role.getName())
                .build();
    }

    public Role fromRoleDTOToRole(RoleDTO roleDto) {
        return roleDto == null ? null : Role.builder()
                .id(roleDto.getId())
                .name(roleDto.getName())
                .users(new HashSet<>())
                .build();
    }

    public AddressDTO fromAddressToAddressDTO(Address address) {
        return address == null ? null : AddressDTO.builder()
                .id(address.getId())
                .city(address.getCity())
                .street(address.getStreet())
                .number(address.getNumber())
                .postalCode(address.getPostalCode())
                .build();
    }

    public Address fromAddressDTOToAddress(AddressDTO addressDTO) {
        return addressDTO == null ? null : Address.builder()
                .id(addressDTO.getId())
                .city(addressDTO.getCity())
                .street(addressDTO.getStreet())
                .number(addressDTO.getNumber())
                .postalCode(addressDTO.getPostalCode())
                .workplaces(new HashSet<>())
                .build();
    }

    public DiseaseDTO fromDiseaseToDiseaseDTO(Disease disease) {
        return disease == null ? null : DiseaseDTO.builder()
                .id(disease.getId())
                .name(disease.getName())
                .importance(disease.getImportance())
                .specializationDTO(disease.getSpecialization() == null ? null : fromSpecializationToSpecializationDTO(disease.getSpecialization()))
                .build();
    }

    public Disease fromDiseaseDTOToDisease(DiseaseDTO diseaseDTO) {
        return diseaseDTO == null ? null : Disease.builder()
                .id(diseaseDTO.getId())
                .name(diseaseDTO.getName())
                .importance(diseaseDTO.getImportance())
                .specialization(diseaseDTO.getSpecializationDTO() == null ? null : fromSpecializationDTOToSpecialization(diseaseDTO.getSpecializationDTO()))
                .patientCards(new HashSet<>())
                .build();
    }

    public UserDTO fromUserToUserDTO(User user) {
        return user == null ? null : UserDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .surname(user.getSurname())
                .birthday(user.getBirthday())
                .userName(user.getUserName())
                .password(user.getPassword())
                .passwordConfirmation("")
                .email(user.getEmail())
                .active(user.getActive())
                .registrationDate(user.getRegistrationDate())
                .roleDTO(user.getRole() == null ? null : fromRoleToRoleDTO(user.getRole()))
                .build();
    }

    public User fromUserDTOToUser(UserDTO userDTO) {
        return userDTO == null ? null : User.builder()
                .id(userDTO.getId())
                .name(userDTO.getName())
                .surname(userDTO.getSurname())
                .birthday(userDTO.getBirthday())
                .userName(userDTO.getUserName())
                .password(userDTO.getPassword())
                .email(userDTO.getEmail())
                .active(userDTO.getActive())
                .registrationDate(userDTO.getRegistrationDate())
                .role(userDTO.getRoleDTO() == null ? null : fromRoleDTOToRole(userDTO.getRoleDTO()))
                .build();
    }

    public DoctorDTO fromDoctorToDoctorDTO(Doctor doctor) {
        return doctor == null ? null : new DoctorDTO(
                doctor.getId(),
                doctor.getName(),
                doctor.getSurname(),
                doctor.getBirthday(),
                doctor.getUserName(),
                doctor.getPassword(),
                "",
                doctor.getEmail(),
                doctor.getActive(),
                doctor.getRegistrationDate(),
                (doctor.getRole() == null ? null : fromRoleToRoleDTO(doctor.getRole())),
                doctor.getExperience(),
                doctor.getPhoto(),
                doctor.getDescription(),
                doctor.getCost(),
                (doctor.getSpecialization() == null ? null : fromSpecializationToSpecializationDTO(doctor.getSpecialization())),
                (doctor.getWorkplace() == null ? null : fromWorkplaceToWorkplaceDTO(doctor.getWorkplace())),
                (doctor.getNationality() == null ? null : fromNationalityToNationalityDTO(doctor.getNationality())));
    }

    public Doctor fromDoctorDTOToDoctor(DoctorDTO doctorDTO) {
        return doctorDTO == null ? null : new Doctor(
                doctorDTO.getId(),
                doctorDTO.getName(),
                doctorDTO.getSurname(),
                doctorDTO.getBirthday(),
                doctorDTO.getUserName(),
                doctorDTO.getPassword(),
                doctorDTO.getEmail(),
                doctorDTO.getActive(),
                doctorDTO.getRegistrationDate(),
                (doctorDTO.getRoleDTO() == null ? null : fromRoleDTOToRole(doctorDTO.getRoleDTO())),
                doctorDTO.getExperience(),
                doctorDTO.getPhoto(),
                doctorDTO.getDescription(),
                doctorDTO.getCost(),
                (doctorDTO.getSpecializationDTO() == null ? null : fromSpecializationDTOToSpecialization(doctorDTO.getSpecializationDTO())),
                (doctorDTO.getWorkplaceDTO() == null ? null : fromWorkplaceDTOToWorkplace(doctorDTO.getWorkplaceDTO())),
                (doctorDTO.getNationalityDTO() == null ? null  : fromNationalityDTOToNationality(doctorDTO.getNationalityDTO())),
                new HashSet<>(),
                new HashSet<>(),
                new HashSet<>(),
                new HashSet<>());
    }

    public PatientDTO fromPatientToPatientDTO(Patient patient) {
        return patient == null ? null : new PatientDTO(
                patient.getId(),
                patient.getName(),
                patient.getSurname(),
                patient.getBirthday(),
                patient.getUserName(),
                patient.getPassword(),
                "",
                patient.getEmail(),
                patient.getActive(),
                patient.getRegistrationDate(),
                (patient.getRole() == null ? null : fromRoleToRoleDTO(patient.getRole())),
                patient.getCreditCardNumber(),
                patient.getInsuranceNumber(),
                patient.getGender(),
                patient.getWeight(),
                patient.getHeight(),
                (patient.getNationality() == null ? null : fromNationalityToNationalityDTO(patient.getNationality())));
    }

    public Patient fromPatientDTOToPatient(PatientDTO patientDTO) {
        return patientDTO == null ? null : new Patient(
                patientDTO.getId(),
                patientDTO.getName(),
                patientDTO.getSurname(),
                patientDTO.getBirthday(),
                patientDTO.getUserName(),
                patientDTO.getPassword(),
                patientDTO.getEmail(),
                patientDTO.getActive(),
                patientDTO.getRegistrationDate(),
                (patientDTO.getRoleDTO() == null ? null : fromRoleDTOToRole(patientDTO.getRoleDTO())),
                patientDTO.getCreditCardNumber(),
                patientDTO.getInsuranceNumber(),
                patientDTO.getGender(),
                patientDTO.getWeight(),
                patientDTO.getHeight(),
                (patientDTO.getNationalityDTO() == null ? null : fromNationalityDTOToNationality(patientDTO.getNationalityDTO())),
                new HashSet<>(),
                new HashSet<>(),
                new HashSet<>());
    }

    public NationalityDTO fromNationalityToNationalityDTO(Nationality nationality) {
        return nationality == null ? null : NationalityDTO.builder()
                .id(nationality.getId())
                .name(nationality.getName())
                .build();
    }

    public Nationality fromNationalityDTOToNationality(NationalityDTO nationalityDTO) {
        return nationalityDTO == null ? null : Nationality.builder()
                .id(nationalityDTO.getId())
                .name(nationalityDTO.getName())
                .build();
    }

    public OpinionDTO fromOpinionToOpinionDTO(Opinion opinion) {
        return opinion == null ? null : OpinionDTO.builder()
                .id(opinion.getId())
                .doctorDTO(opinion.getDoctor() == null ? null : fromDoctorToDoctorDTO(opinion.getDoctor()))
                .patientDTO(opinion.getPatient() == null ? null : fromPatientToPatientDTO(opinion.getPatient()))
                .description(opinion.getDescription())
                .quality(opinion.getQuality())
                .opinionDate(opinion.getOpinionDate())
                .build();
    }

    public Opinion fromOpinionDTOToOpinion(OpinionDTO opinionDTO) {
        return opinionDTO == null ? null : Opinion.builder()
                .id(opinionDTO.getId())
                .doctor(opinionDTO.getDoctorDTO() == null ? null : fromDoctorDTOToDoctor(opinionDTO.getDoctorDTO()))
                .patient(opinionDTO.getPatientDTO() == null ? null : fromPatientDTOToPatient(opinionDTO.getPatientDTO()))
                .description(opinionDTO.getDescription())
                .quality(opinionDTO.getQuality())
                .opinionDate(opinionDTO.getOpinionDate())
                .build();
    }

    public PatientCardDTO fromPatientCardToPatientCardDTO(PatientCard patientCard) {
        return patientCard == null ? null : PatientCardDTO.builder()
                .id(patientCard.getId())
                .doctorDTO(patientCard.getDoctor() == null ? null : fromDoctorToDoctorDTO(patientCard.getDoctor()))
                .patientDTO(patientCard.getPatient() == null ? null : fromPatientToPatientDTO(patientCard.getPatient()))
                .diseaseDTO(patientCard.getDisease() == null ? null : fromDiseaseToDiseaseDTO(patientCard.getDisease()))
                .description(patientCard.getDescription())
                .date(patientCard.getDate())
                .build();
    }

    public PatientCard fromPatientCardDTOToPatientCard(PatientCardDTO patientCardDTO) {
        return patientCardDTO == null ? null : PatientCard.builder()
                .id(patientCardDTO.getId())
                .doctor(patientCardDTO.getDoctorDTO() == null ? null : fromDoctorDTOToDoctor(patientCardDTO.getDoctorDTO()))
                .patient(patientCardDTO.getPatientDTO() == null ? null : fromPatientDTOToPatient(patientCardDTO.getPatientDTO()))
                .disease(patientCardDTO.getDiseaseDTO() == null ? null : fromDiseaseDTOToDisease(patientCardDTO.getDiseaseDTO()))
                .description(patientCardDTO.getDescription())
                .date(patientCardDTO.getDate())
                .build();
    }

    public SpecializationDTO fromSpecializationToSpecializationDTO(Specialization specialization) {
        return specialization == null ? null : SpecializationDTO.builder()
                .id(specialization.getId())
                .name(specialization.getName())
                .build();
    }

    public Specialization fromSpecializationDTOToSpecialization(SpecializationDTO specializationDTO) {
        return specializationDTO == null ? null : Specialization.builder()
                .id(specializationDTO.getId())
                .name(specializationDTO.getName())
                .diseases(new HashSet<>())
                .doctors(new HashSet<>())
                .advices(new HashSet<>())
                .build();
    }

    public VisitDTO fromVisitToVisitDTO(Visit visit) {
        return visit == null ? null : VisitDTO.builder()
                .id(visit.getId())
                .doctorDTO(visit.getDoctor() == null ? null : fromDoctorToDoctorDTO(visit.getDoctor()))
                .patientDTO(visit.getPatient() == null ? null : fromPatientToPatientDTO(visit.getPatient()))
                .cost(visit.getCost())
                .date(visit.getDateTime().toLocalDate())
                .time(visit.getDateTime().toLocalTime())
                .description(visit.getDescription())
                .isAfterVisit(visit.getIsAfterVisit())
                .isCanAddVisit(true)
                .build();
    }

    public Visit fromVisitDTOToVisit(VisitDTO visitDTO) {
        return visitDTO == null ? null : Visit.builder()
                .id(visitDTO.getId())
                .doctor(visitDTO.getDoctorDTO() == null ? null : fromDoctorDTOToDoctor(visitDTO.getDoctorDTO()))
                .patient(visitDTO.getPatientDTO() == null ? null : fromPatientDTOToPatient(visitDTO.getPatientDTO()))
                .cost(visitDTO.getCost())
                .dateTime(LocalDateTime.of(visitDTO.getDate(), visitDTO.getTime()))
                .description(visitDTO.getDescription())
                .isAfterVisit(visitDTO.getIsAfterVisit())
                .build();
    }

    public WorkplaceDTO fromWorkplaceToWorkplaceDTO(Workplace workplace) {
        return workplace == null ? null : WorkplaceDTO.builder()
                .id(workplace.getId())
                .name(workplace.getName())
                .addressDTO(workplace.getAddress() == null ? null : fromAddressToAddressDTO(workplace.getAddress()))
                .description(workplace.getDescription())
                .build();
    }

    public Workplace fromWorkplaceDTOToWorkplace(WorkplaceDTO workplaceDTO) {
        return workplaceDTO == null ? null : Workplace.builder()
                .id(workplaceDTO.getId())
                .name(workplaceDTO.getName())
                .address(workplaceDTO.getAddressDTO() == null ? null : fromAddressDTOToAddress(workplaceDTO.getAddressDTO()))
                .description(workplaceDTO.getDescription())
                .doctors(new HashSet<>())
                .build();
    }

    public AdviceDTO fromAdviceToAdviceDTO(Advice advice) {
        return advice == null ? null : AdviceDTO.builder()
                .id(advice.getId())
                .email(advice.getEmail())
                .question(advice.getQuestion())
                .answer(advice.getAnswer())
                .questionDate(advice.getQuestionDate())
                .doctorDTO(advice.getDoctor() == null ? null : fromDoctorToDoctorDTO(advice.getDoctor()))
                .specializationDTO(advice.getSpecialization() == null ? null : fromSpecializationToSpecializationDTO(advice.getSpecialization()))
                .build();
    }

    public Advice fromAdviceDTOToAdvice(AdviceDTO adviceDTO) {
        return adviceDTO == null ? null : Advice.builder()
                .id(adviceDTO.getId())
                .email(adviceDTO.getEmail())
                .question(adviceDTO.getQuestion())
                .answer(adviceDTO.getAnswer())
                .questionDate(adviceDTO.getQuestionDate())
                .doctor(adviceDTO.getDoctorDTO() == null ? null : fromDoctorDTOToDoctor(adviceDTO.getDoctorDTO()))
                .specialization(adviceDTO.getSpecializationDTO() == null ? null : fromSpecializationDTOToSpecialization(adviceDTO.getSpecializationDTO()))
                .build();
    }

    public AdviceAnswerDTO fromAdviceToAdviceAnswerDTO(Advice advice) {
        return advice == null ? null : AdviceAnswerDTO.builder()
                .id(advice.getId())
                .email(advice.getEmail())
                .question(advice.getQuestion())
                .answer(advice.getAnswer())
                .questionDate(advice.getQuestionDate())
                .doctorDTO(advice.getDoctor() == null ? null : fromDoctorToDoctorDTO(advice.getDoctor()))
                .specializationDTO(advice.getSpecialization() == null ? null : fromSpecializationToSpecializationDTO(advice.getSpecialization()))
                .build();
    }

    public Advice fromAdviceDTOToAdvice(AdviceAnswerDTO adviceAnswerDTO) {
        return adviceAnswerDTO == null ? null : Advice.builder()
                .id(adviceAnswerDTO.getId())
                .email(adviceAnswerDTO.getEmail())
                .question(adviceAnswerDTO.getQuestion())
                .answer(adviceAnswerDTO.getAnswer())
                .questionDate(adviceAnswerDTO.getQuestionDate())
                .doctor(adviceAnswerDTO.getDoctorDTO() == null ? null : fromDoctorDTOToDoctor(adviceAnswerDTO.getDoctorDTO()))
                .specialization(adviceAnswerDTO.getSpecializationDTO() == null ? null : fromSpecializationDTOToSpecialization(adviceAnswerDTO.getSpecializationDTO()))
                .build();
    }
}
