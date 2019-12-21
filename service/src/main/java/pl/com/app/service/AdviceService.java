package pl.com.app.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import pl.com.app.dto.AdviceAnswerDTO;
import pl.com.app.dto.AdviceDTO;
import pl.com.app.dto.DoctorDTO;
import pl.com.app.dto.OpinionDTO;
import pl.com.app.exceptions.ExceptionCode;
import pl.com.app.exceptions.MyException;
import pl.com.app.model.Advice;
import pl.com.app.model.Doctor;
import pl.com.app.model.Specialization;
import pl.com.app.repository.AdviceRepository;
import pl.com.app.repository.DoctorRepository;
import pl.com.app.repository.OpinionRepository;
import pl.com.app.repository.SpecializationRepository;
import pl.com.app.service.listeners.OnAnswerAdviceEvenData;
import pl.com.app.service.mappers.ModelMapper;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class AdviceService {
    private final AdviceRepository adviceRepository;
    private final DoctorRepository doctorRepository;
    private final OpinionRepository opinionRepository;
    private final SpecializationRepository specializationRepository;
    private final ModelMapper modelMapper;
    private final ApplicationEventPublisher eventPublisher;

    public Map<DoctorDTO, Long[]> getDoctorsSortedByOpinions() {
        try {
            return doctorRepository
                    .findAll()
                    .stream()
                    .map(modelMapper::fromDoctorToDoctorDTO)
                    .collect(Collectors.toMap(
                            Function.identity(),
                            this::numberOfOpinionPoints
                    ))
                    .entrySet()
                    .stream()
                    .sorted((x1, x2) -> sortedByNumberOfGoodOpinions(x2.getValue(), x1.getValue()))
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            Map.Entry::getValue,
                            (v1, v2) -> v1,
                            LinkedHashMap::new
                    ));
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
            throw new MyException(ExceptionCode.SERVICE, e.getMessage());
        }

    }

    public void addAdvice(AdviceDTO adviceDTO) {
        try {
            if (adviceDTO == null) {
                throw new NullPointerException("ADVICE IS NULL");
            }

            Advice advice = modelMapper.fromAdviceDTOToAdvice(adviceDTO);

            if (adviceDTO.getSpecializationDTO() != null && adviceDTO.getSpecializationDTO().getId() != null) {
                Specialization specialization = specializationRepository
                        .findById(adviceDTO.getSpecializationDTO().getId())
                        .orElseThrow(NullPointerException::new);
                advice.setSpecialization(specialization);

                Map<DoctorDTO, Long[]> numberOfOpinionsDoctorsMap = doctorRepository
                        .findAllBySpecialization_IdEquals(specialization.getId())
                        .stream()
                        .map(modelMapper::fromDoctorToDoctorDTO)
                        .collect(Collectors.toMap(
                                Function.identity(),
                                this::numberOfOpinionPoints
                        ));


                LinkedHashMap<DoctorDTO, Long> sortedDoctorsByGoodOpinionsMap = numberOfOpinionsDoctorsMap.entrySet()
                        .stream()
                        .sorted((x1, x2) -> sortedByNumberOfGoodOpinions(x2.getValue(), x1.getValue()))
                        .collect(Collectors.toMap(
                                Map.Entry::getKey,
                                e -> adviceRepository.countAllByDoctor_IdEqualsAndAnswerIsNull(e.getKey().getId()),
                                (v1, v2) -> v1,
                                LinkedHashMap::new
                        ));


                sortedDoctorsByGoodOpinionsMap.entrySet()
                        .stream()
                        .filter(x -> x.getValue() < 10)
                        .map(Map.Entry::getKey)
                        .findFirst()
                        .ifPresent(x -> {
                            Doctor doctor = doctorRepository
                                    .findById(x.getId())
                                    .orElseThrow(NullPointerException::new);
                            advice.setDoctor(doctor);
                        });
            } else {
                advice.setSpecialization(null);
            }

            advice.setQuestionDate(LocalDate.now());
            adviceRepository.save(advice);
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
            throw new MyException(ExceptionCode.SERVICE, e.getMessage());
        }
    }

    private int sortedByNumberOfGoodOpinions(Long[] v1, Long[] v2) {
        if (v1 == null || v2 == null) {
            throw new NullPointerException("VALUE IS NULL");
        }
        int retValue = 0;
        for (int i = v1.length - 1; i >= 0; i--) {
            if (v1[i] != null && v2[i] != null){
                if (v1[i] > v2[i]){
                    retValue = 1;
                    break;
                } else if ((v1[i] < v2[i])){
                    retValue = -1;
                    break;
                }
            } else if (v1[i] != null){
                retValue = 1;
                break;
            } else if (v2[i] != null){
                retValue = -1;
                break;
            }
        }
        return retValue;
    }

    private Long[] numberOfOpinionPoints(DoctorDTO doctorDTO) {
        if (doctorDTO == null || doctorDTO.getId() == null) {
            throw new NullPointerException("DOCTOR DTO IS NULL");
        }
        Long[] numberOfOpinions = new Long[5];
        opinionRepository
                .findAllByDoctor_IdEqualsOrderByQualityDesc(doctorDTO.getId())
                .stream()
                .unordered()
                .map(modelMapper::fromOpinionToOpinionDTO)
                .collect(Collectors.groupingBy(OpinionDTO::getQuality, Collectors.counting()))
                .forEach((k, v) -> numberOfOpinions[k - 1] = v);

        return numberOfOpinions;
    }

    public AdviceDTO getOneAdvice(Long adviceId) {
        try {
            if (adviceId == null) {
                throw new NullPointerException("ADVICE ID IS NULL");
            }

            return adviceRepository
                    .findById(adviceId)
                    .map(modelMapper::fromAdviceToAdviceDTO)
                    .orElseThrow(NullPointerException::new);
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
            throw new MyException(ExceptionCode.SERVICE, e.getMessage());
        }
    }

    public AdviceAnswerDTO getOneAdviceAnswer(Long adviceId) {
        try {
            if (adviceId == null) {
                throw new NullPointerException("ADVICE ID IS NULL");
            }

            return adviceRepository
                    .findById(adviceId)
                    .map(modelMapper::fromAdviceToAdviceAnswerDTO)
                    .orElseThrow(NullPointerException::new);
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
            throw new MyException(ExceptionCode.SERVICE, e.getMessage());
        }
    }

    public List<AdviceDTO> getAllAdvices() {
        try {
            return adviceRepository
                    .findAll()
                    .stream()
                    .map(modelMapper::fromAdviceToAdviceDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
            throw new MyException(ExceptionCode.SERVICE, e.getMessage());
        }
    }

    public void deleteAdvice(Long adviceId) {
        try {
            if (adviceId == null) {
                throw new NullPointerException("ADVICE ID IS NULL");
            }
            adviceRepository.deleteById(adviceId);
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
            throw new MyException(ExceptionCode.SERVICE, e.getMessage());
        }
    }

    public List<AdviceDTO> getAllNotAnswerAdvicesByDoctor(Long doctorId) {
        try {
            if (doctorId == null) {
                throw new NullPointerException("DOCTOR ID IS NULL");
            }
            return adviceRepository
                    .findAllByDoctor_IdEqualsAndAnswerIsNull(doctorId)
                    .stream()
                    .map(modelMapper::fromAdviceToAdviceDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
            throw new MyException(ExceptionCode.SERVICE, e.getMessage());
        }
    }

    public void addAnswerAdvice(AdviceAnswerDTO adviceAnswerDTO, HttpServletRequest request) {
        try {
            if (adviceAnswerDTO == null) {
                throw new NullPointerException("ADVICE ANSWER IS NULL");
            }
            if (adviceAnswerDTO.getId() == null) {
                throw new NullPointerException("ADVICE ANSWER ID IS NULL");
            }
            Advice advice = adviceRepository.findById(adviceAnswerDTO.getId()).orElseThrow(NullPointerException::new);
            advice.setAnswer(adviceAnswerDTO.getAnswer());
            adviceRepository.save(advice);

            final String url = "http://" + request.getServerName() + ":" + request.getServerPort() + "/" + request.getContextPath();
            eventPublisher.publishEvent(new OnAnswerAdviceEvenData(url, advice));
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
            throw new MyException(ExceptionCode.SERVICE, e.getMessage());
        }
    }

    public List<AdviceDTO> getNewestAdvicesWithAnswer() {
        try {
            return adviceRepository
                    .findFirst5ByAnswerIsNotNullOrderByQuestionDateDesc()
                    .stream()
                    .map(modelMapper::fromAdviceToAdviceDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
            throw new MyException(ExceptionCode.SERVICE, e.getMessage());
        }
    }

    public List<AdviceDTO> getAllNotAnswerAdmAdvices() {
        try {
            return adviceRepository
                    .findAllByDoctorIsNullAndAnswerIsNull()
                    .stream()
                    .map(modelMapper::fromAdviceToAdviceDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
            throw new MyException(ExceptionCode.SERVICE, e.getMessage());
        }
    }

    public List<AdviceDTO> getAdvicesByDoctor(Long id) {
        try {
            if (id == null) {
                throw new NullPointerException("DOCTORS ID IS NULL");
            }
            return adviceRepository
                    .findAllByDoctorId_Equals(id)
                    .stream()
                    .map(modelMapper::fromAdviceToAdviceDTO)
                    .sorted(Comparator.comparing(AdviceDTO::getQuestionDate, Comparator.reverseOrder()))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
            throw new MyException(ExceptionCode.SERVICE, e.getMessage());
        }
    }
}
