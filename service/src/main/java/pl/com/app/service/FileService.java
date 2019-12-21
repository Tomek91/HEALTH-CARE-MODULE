package pl.com.app.service;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;
import pl.com.app.dto.VisitDTO;
import pl.com.app.exceptions.ExceptionCode;
import pl.com.app.exceptions.MyException;
import pl.com.app.parsers.FileNames;
import pl.com.app.parsers.VisitsConverter;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.List;

@Service
@Slf4j
public class FileService {

    @Value("${imgPath}")
    private String imgPath;

    @Autowired
    private VisitsConverter visitsDataConverter;

    private String createFilename(MultipartFile file) {
        final String originalFilename = file.getOriginalFilename();
        final String[] arr = originalFilename.split("\\.");
        final String extension = arr[arr.length - 1];
        final String filename = Base64.getEncoder().encodeToString(
                LocalDateTime.now()
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss"))
                        .getBytes()
        );
        return String.join(".", filename, extension);
    }

    public String addFile(MultipartFile file) {
        try {

            if (file == null) {
                throw new NullPointerException("FILE IS NULL");
            }

            final String filename = createFilename(file);
            final String fullPath = imgPath + filename;
            FileCopyUtils.copy(file.getBytes(), new File(fullPath));
            return filename;
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
            throw new MyException(ExceptionCode.FILE, e.getMessage());
        }
    }

    public String updateFile(MultipartFile file, String filename) {
        try {

            if (file == null || file.getBytes().length == 0) {
                return filename;
            }

            if (filename == null) {
                throw new NullPointerException("FILENAME IS NULL");
            }

            final String fullPath = imgPath + filename;
            FileCopyUtils.copy(file.getBytes(), new File(fullPath));
            return filename;
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
            throw new MyException(ExceptionCode.FILE, e.getMessage());
        }
    }

    public String deleteImg(String filename) {
        try {

            if (filename == null) {
                throw new NullPointerException("FILENAME IS NULL");
            }
            if (!filename.startsWith("pic")){
                final String fullPath = imgPath + filename;
                new File(fullPath).delete();
            }
            return filename;
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
            throw new MyException(ExceptionCode.FILE, e.getMessage());
        }
    }

    public void deleteFile(String fullPath) {
        try {

            if (fullPath == null) {
                throw new NullPointerException("PATH IS NULL");
            }
            new File(fullPath).delete();
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
            throw new MyException(ExceptionCode.FILE, e.getMessage());
        }
    }

    public void saveVisitDataToJson(VisitDTO visitData) {
        try {

            if (visitData == null) {
                throw new NullPointerException("VISIT DATA IS NULL");
            }
            visitsDataConverter.toJson(List.of(visitData));

        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
            throw new MyException(ExceptionCode.FILE, e.getMessage());
        }
    }

    public VisitDTO getVisitDataToJson() {
        try {
            return visitsDataConverter.fromJson()
                    .stream()
                    .flatMap(List::stream)
                    .findFirst()
                    .orElseThrow(NullPointerException::new);

        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
            throw new MyException(ExceptionCode.FILE, e.getMessage());
        }
    }

    public boolean isFileVisitDataExists() {
        try {

            File file = new File(FileNames.VISIT_DATA);

            return file.exists();
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
            throw new MyException(ExceptionCode.FILE, e.getMessage());
        }
    }
}
