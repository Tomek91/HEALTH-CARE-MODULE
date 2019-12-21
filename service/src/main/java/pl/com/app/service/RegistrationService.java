package pl.com.app.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.com.app.dto.DoctorDTO;
import pl.com.app.dto.PatientDTO;
import pl.com.app.dto.UserDTO;
import pl.com.app.exceptions.ExceptionCode;
import pl.com.app.exceptions.MyException;
import pl.com.app.model.*;
import pl.com.app.repository.*;
import pl.com.app.service.listeners.OnRegistrationEvenData;
import pl.com.app.service.mappers.ModelMapper;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class RegistrationService {

    private final UserRepository userRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final RoleRepository roleRepository;
    private final NationalityRepository nationalityRepository;
    private final SpecializationRepository specializationRepository;
    private final WorkplaceRepository workplaceRepository;
    private final FileService fileService;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final ApplicationEventPublisher eventPublisher;


    public void createVerificationToken(User user, String token) {
        try {
            if (user == null) {
                throw new NullPointerException("USER IS NULL");
            }

            if (token == null) {
                throw new NullPointerException("TOKEN IS NULL");
            }
            verificationTokenRepository.findByUserId_Equals(user.getId())
                    .ifPresent(verificationTokenRepository::delete);

            verificationTokenRepository.save(VerificationToken.builder()
                    .user(user)
                    .token(token)
                    .expirationDateTime(LocalDateTime.now().plusDays(1L))
                    .build());
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
            throw new MyException(ExceptionCode.SERVICE, e.getMessage());
        }
    }

    public void confirmRegistration(String token) {
        try {
            if (token == null) {
                throw new NullPointerException("TOKEN IS NULL");
            }

            VerificationToken verificationToken
                    = verificationTokenRepository.findByToken(token)
                    .orElseThrow(() -> new NullPointerException("USER WITH TOKEN " + token + " DOESN'T EXIST"));

            if (verificationToken.getExpirationDateTime().isBefore(LocalDateTime.now())) {
                throw new NullPointerException("TOKEN HAS BEEN EXPIRED");
            }

            User user = verificationToken.getUser();
            user.setActive(true);
            userRepository.save(user);

            verificationToken.setToken(null);
            verificationTokenRepository.save(verificationToken);
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
            throw new MyException(ExceptionCode.SERVICE, e.getMessage());
        }
    }

    public void registerNewAdmin(UserDTO userDTO, HttpServletRequest request) {
        try {
            if (userDTO == null) {
                throw new NullPointerException("USER OBJECT IS NULL");
            }

            Role role = roleRepository
                    .findById(userDTO.getRoleDTO().getId())
                    .orElseThrow(NullPointerException::new);

            User user = modelMapper.fromUserDTOToUser(userDTO);
            user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
            user.setRole(role);
            user.setActive(false);
            user.setRegistrationDate(LocalDate.now());
            userRepository.save(user);

            final String url = "http://" + request.getServerName() + ":" + request.getServerPort() + "/" + request.getContextPath();
            eventPublisher.publishEvent(new OnRegistrationEvenData(url, user));
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
            throw new MyException(ExceptionCode.SERVICE, e.getMessage());
        }
    }

    public void registerNewDoctor(DoctorDTO doctorDTO, HttpServletRequest request) {
        try {
            if (doctorDTO == null) {
                throw new NullPointerException("DOCTOR OBJECT IS NULL");
            }

            if (doctorDTO.getNationalityDTO() == null) {
                throw new NullPointerException("NATIONALITY IS NULL");
            }

            if (doctorDTO.getNationalityDTO().getId() == null) {
                throw new NullPointerException("NATIONALITY ID IS NULL");
            }

            if (doctorDTO.getWorkplaceDTO() == null) {
                throw new NullPointerException("WORKPLACE IS NULL");
            }

            if (doctorDTO.getWorkplaceDTO().getId() == null) {
                throw new NullPointerException("WORKPLACE ID IS NULL");
            }

            if (doctorDTO.getSpecializationDTO() == null) {
                throw new NullPointerException("SPECIALIZATION IS NULL");
            }

            if (doctorDTO.getSpecializationDTO().getId() == null) {
                throw new NullPointerException("SPECIALIZATION ID IS NULL");
            }

            Nationality nationality = nationalityRepository
                    .findById(doctorDTO.getNationalityDTO().getId())
                    .orElseThrow(NullPointerException::new);
            Specialization specialization = specializationRepository
                    .findById(doctorDTO.getSpecializationDTO().getId())
                    .orElseThrow(NullPointerException::new);
            Workplace workplace = workplaceRepository
                    .findById(doctorDTO.getWorkplaceDTO().getId())
                    .orElseThrow(NullPointerException::new);
            Role role = roleRepository
                    .findById(doctorDTO.getRoleDTO().getId())
                    .orElseThrow(NullPointerException::new);

            Doctor doctor = modelMapper.fromDoctorDTOToDoctor(doctorDTO);
            String filename = fileService.addFile(doctorDTO.getFile());
            doctor.setPassword(passwordEncoder.encode(doctorDTO.getPassword()));
            doctor.setPhoto(filename);
            doctor.setNationality(nationality);
            doctor.setWorkplace(workplace);
            doctor.setSpecialization(specialization);
            doctor.setRole(role);
            doctor.setActive(false);
            doctor.setRegistrationDate(LocalDate.now());
            doctorRepository.save(doctor);

            final String url = "http://" + request.getServerName() + ":" + request.getServerPort() + "/" + request.getContextPath();
            eventPublisher.publishEvent(new OnRegistrationEvenData(url, doctor));
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
            throw new MyException(ExceptionCode.SERVICE, e.getMessage());
        }
    }

    public void registerNewPatient(PatientDTO patientDTO, HttpServletRequest request) {
        try {
            if (patientDTO == null) {
                throw new NullPointerException("PATIENT OBJECT IS NULL");
            }

            if (patientDTO.getNationalityDTO() == null) {
                throw new NullPointerException("NATIONALITY IS NULL");
            }

            if (patientDTO.getNationalityDTO().getId() == null) {
                throw new NullPointerException("NATIONALITY ID IS NULL");
            }

            Nationality nationality = nationalityRepository
                    .findById(patientDTO.getNationalityDTO().getId())
                    .orElseThrow(NullPointerException::new);
            Role role = roleRepository
                    .findById(patientDTO.getRoleDTO().getId())
                    .orElseThrow(NullPointerException::new);


            Patient patient = modelMapper.fromPatientDTOToPatient(patientDTO);
            patient.setPassword(passwordEncoder.encode(patientDTO.getPassword()));
            patient.setRole(role);
            patient.setActive(false);
            patient.setRegistrationDate(LocalDate.now());
            patient.setNationality(nationality);
            patient.setRegistrationDate(LocalDate.now());
            patientRepository.save(patient);

            final String url = "http://" + request.getServerName() + ":" + request.getServerPort() + "/" + request.getContextPath();
            eventPublisher.publishEvent(new OnRegistrationEvenData(url, patient));
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
            throw new MyException(ExceptionCode.SERVICE, e.getMessage());
        }
    }
}
