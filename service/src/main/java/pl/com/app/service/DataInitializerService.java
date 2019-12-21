package pl.com.app.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.com.app.dto.*;
import pl.com.app.exceptions.ExceptionCode;
import pl.com.app.exceptions.MyException;
import pl.com.app.model.ENationalityName;
import pl.com.app.parsers.*;
import pl.com.app.repository.*;
import pl.com.app.service.mappers.ModelMapper;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
//@Transactional
@RequiredArgsConstructor
public class DataInitializerService {
    private final NationalityRepository nationalityRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final RoleRepository roleRepository;
    private final AddressRepository addressRepository;
    private final DoctorRepository doctorRepository;
    private final PatientCardRepository patientCardRepository;
    private final UserRepository userRepository;
    private final PatientRepository patientRepository;
    private final VisitRepository visitRepository;
    private final WorkplaceRepository workplaceRepository;
    private final SpecializationRepository specializationRepository;
    private final AdviceRepository adviceRepository;
    private final OpinionRepository opinionRepository;
    private final DiseaseRepository diseaseRepository;
    private final AddressesConverter addressesConverter;
    private final DiseaseConverter diseaseConverter;
    private final DoctorsConverter doctorsConverter;
    private final OpinionsConverter opinionsConverter;
    private final PatientCardsConverter patientCardsConverter;
    private final PatientsConverter patientsConverter;
    private final RolesConverter rolesConverter;
    private final SpecializationsConverter specializationsConverter;
    private final VisitsConverter visitsConverter;
    private final WorkplacesConverter workplacesConverter;
    private final AdvicesConverter advicesConverter;
    private final UsersConverter usersConverter;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    public void initData() {
        try {
            adviceRepository.deleteAll();
            verificationTokenRepository.deleteAll();
            opinionRepository.deleteAll();
            patientCardRepository.deleteAll();
            diseaseRepository.deleteAll();
            visitRepository.deleteAll();
            doctorRepository.deleteAll();
            patientRepository.deleteAll();
            specializationRepository.deleteAll();
            workplaceRepository.deleteAll();
            addressRepository.deleteAll();
            nationalityRepository.deleteAll();
            //roleRepository.deleteAll();
            userRepository.deleteAll();


            List<AddressDTO> addressDTOList = addressesConverter.fromJson().orElseThrow(NullPointerException::new);
            List<DiseaseDTO> diseaseDTOList = diseaseConverter.fromJson().orElseThrow(NullPointerException::new);
            List<DoctorDTO> doctorDTOList = doctorsConverter.fromJson().orElseThrow(NullPointerException::new);
            List<OpinionDTO> opinionDTOList = opinionsConverter.fromJson().orElseThrow(NullPointerException::new);
            List<PatientCardDTO> patientCardDTOList = patientCardsConverter.fromJson().orElseThrow(NullPointerException::new);
            List<PatientDTO> patientDTOList = patientsConverter.fromJson().orElseThrow(NullPointerException::new);
            List<SpecializationDTO> specializationDTOList = specializationsConverter.fromJson().orElseThrow(NullPointerException::new);
            List<VisitDTO> visitDTOList = visitsConverter.fromJson().orElseThrow(NullPointerException::new);
            List<WorkplaceDTO> workplaceDTOList = workplacesConverter.fromJson().orElseThrow(NullPointerException::new);
            List<AdviceDTO> adviceDTOList = advicesConverter.fromJson().orElseThrow(NullPointerException::new);
            List<RoleDTO> roleDTOList = rolesConverter.fromJson().orElseThrow(NullPointerException::new);
            List<UserDTO> userDTOList = usersConverter.fromJson().orElseThrow(NullPointerException::new);


            Arrays.stream(ENationalityName.values())
                    .map(x -> NationalityDTO.builder().name(x).build())
                    .map(modelMapper::fromNationalityDTOToNationality)
                    .forEach(nationalityRepository::save);

            addressRepository.saveAll(
                    addressDTOList
                            .stream()
                            .map(modelMapper::fromAddressDTOToAddress)
                            .collect(Collectors.toList())
            );

//            roleRepository.saveAll(
//                    roleDTOList
//                            .stream()
//                            .map(modelMapper::fromRoleDTOToRole)
//                            .collect(Collectors.toList())
//            );

            workplaceRepository.saveAll(
                    workplaceDTOList
                            .stream()
                            .map(modelMapper::fromWorkplaceDTOToWorkplace)
                            .peek(x -> x.setAddress(addressRepository.findByStreet(x.getAddress().getStreet()).orElseThrow(NullPointerException::new)))
                            .collect(Collectors.toList())
            );

            specializationRepository.saveAll(
                    specializationDTOList
                            .stream()
                            .map(modelMapper::fromSpecializationDTOToSpecialization)
                            .collect(Collectors.toList())
            );

            userRepository.saveAll(
                    userDTOList
                            .stream()
                            .map(modelMapper::fromUserDTOToUser)
                            .peek(x -> x.setRole(roleRepository.findByName(x.getRole().getName()).orElseThrow(NullPointerException::new)))
                            .peek(x -> x.setPassword(passwordEncoder.encode(x.getPassword())))
                            .collect(Collectors.toList())
            );

            patientRepository.saveAll(
                    patientDTOList
                            .stream()
                            .map(modelMapper::fromPatientDTOToPatient)
                            .peek(x -> x.setNationality(nationalityRepository.findByName(x.getNationality().getName()).orElseThrow(NullPointerException::new)))
                            .peek(x -> x.setRole(roleRepository.findByName(x.getRole().getName()).orElseThrow(NullPointerException::new)))
                            .peek(x -> x.setPassword(passwordEncoder.encode(x.getPassword())))
                            .collect(Collectors.toList())
            );

            doctorRepository.saveAll(
                    doctorDTOList
                            .stream()
                            .map(modelMapper::fromDoctorDTOToDoctor)
                            .peek(x -> x.setNationality(nationalityRepository.findByName(x.getNationality().getName()).orElseThrow(NullPointerException::new)))
                            .peek(x -> x.setSpecialization(specializationRepository.findByName(x.getSpecialization().getName()).orElseThrow(NullPointerException::new)))
                            .peek(x -> x.setWorkplace(workplaceRepository.findByName(x.getWorkplace().getName()).orElseThrow(NullPointerException::new)))
                            .peek(x -> x.setRole(roleRepository.findByName(x.getRole().getName()).orElseThrow(NullPointerException::new)))
                            .peek(x -> x.setPassword(passwordEncoder.encode(x.getPassword())))
                            .collect(Collectors.toList())
            );

            diseaseRepository.saveAll(
                    diseaseDTOList
                            .stream()
                            .map(modelMapper::fromDiseaseDTOToDisease)
                            .peek(x -> x.setSpecialization(specializationRepository.findByName(x.getSpecialization().getName()).orElseThrow(NullPointerException::new)))
                            .collect(Collectors.toList())
            );

            visitRepository.saveAll(
                    visitDTOList
                            .stream()
                            .map(modelMapper::fromVisitDTOToVisit)
                            .peek(x -> x.setPatient(patientRepository.findByNameAndSurname(x.getPatient().getName(), x.getPatient().getSurname()).orElseThrow(NullPointerException::new)))
                            .peek(x -> x.setDoctor(doctorRepository.findByNameAndSurname(x.getDoctor().getName(), x.getDoctor().getSurname()).orElseThrow(NullPointerException::new)))
                            .collect(Collectors.toList())
            );

            opinionRepository.saveAll(
                    opinionDTOList
                            .stream()
                            .map(modelMapper::fromOpinionDTOToOpinion)
                            .peek(x -> x.setPatient(patientRepository.findByNameAndSurname(x.getPatient().getName(), x.getPatient().getSurname()).orElseThrow(NullPointerException::new)))
                            .peek(x -> x.setDoctor(doctorRepository.findByNameAndSurname(x.getDoctor().getName(), x.getDoctor().getSurname()).orElseThrow(NullPointerException::new)))
                            .collect(Collectors.toList())
            );

            adviceRepository.saveAll(
                    adviceDTOList
                            .stream()
                            .map(modelMapper::fromAdviceDTOToAdvice)
                            .map(x -> {
                                if (x.getSpecialization() != null){
                                    x.setSpecialization(specializationRepository.findByName(x.getSpecialization().getName()).orElseThrow(NullPointerException::new));
                                }
                                return x;
                            })
                            .map(x -> {
                                if (x.getDoctor() != null){
                                    x.setDoctor(doctorRepository.findByNameAndSurname(x.getDoctor().getName(), x.getDoctor().getSurname()).orElseThrow(NullPointerException::new));
                                }
                                return x;
                            })
//                            .peek(x -> x.setSpecialization(specializationRepository.findByName(x.getSpecialization().getName()).orElseThrow(NullPointerException::new)))
//                            .peek(x -> x.setDoctor(doctorRepository.findByNameAndSurname(x.getDoctor().getName(), x.getDoctor().getSurname()).orElseThrow(NullPointerException::new)))
                            .collect(Collectors.toList())
            );

            patientCardRepository.saveAll(
                    patientCardDTOList
                            .stream()
                            .map(modelMapper::fromPatientCardDTOToPatientCard)
                            .peek(x -> x.setPatient(patientRepository.findByNameAndSurname(x.getPatient().getName(), x.getPatient().getSurname()).orElseThrow(NullPointerException::new)))
                            .peek(x -> x.setDoctor(doctorRepository.findByNameAndSurname(x.getDoctor().getName(), x.getDoctor().getSurname()).orElseThrow(NullPointerException::new)))
                            .peek(x -> x.setDisease(diseaseRepository.findByName(x.getDisease().getName()).orElseThrow(NullPointerException::new)))
                            .collect(Collectors.toList())
            );
        } catch (Exception e) {
            e.printStackTrace();
            log.error(ExceptionUtils.getStackTrace(e));
            throw new MyException(ExceptionCode.SERVICE, e.getMessage());
        }
    }
}
