package ru.hogwarts.school.service;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.AvatarRepository;
import ru.hogwarts.school.repository.StudentRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@Transactional
public class AvatarService {
    private static final Logger logger = LoggerFactory.getLogger(AvatarService.class);
    @Value("${avatars.derictories.path}")
    private String avatarsDir;
    private final StudentRepository studentRepository;
    private final AvatarRepository avatarRepository;
    public AvatarService (StudentRepository studentRepository, AvatarRepository avatarRepository) {
        this.studentRepository = studentRepository;
        this.avatarRepository = avatarRepository;
    }
    public void uploadAvatar(Long studentId, MultipartFile file) throws IOException {
        logger.info("Was invoked method for upload avatar for student id = {}", studentId);

        if (file == null || file.isEmpty()) {
            logger.warn("Attempt to upload empty file for student id = {}", studentId);
            throw new IOException("Файл не может быть пустым");
        }

        logger.debug("File name: {}, size: {}, type: {}", file.getOriginalFilename(), file.getSize(), file.getContentType());

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> {
                    logger.error("Student with id = {} not found for avatar upload", studentId);
                    return new RuntimeException("Студент не найден");
                });

        Path uploadPath = Paths.get(avatarsDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
            logger.debug("Created directory for avatars: {}", uploadPath);
        }

        String fileName = studentId + "_" + file.getOriginalFilename();
        Path filePath = uploadPath.resolve(fileName);
        Files.write(filePath, file.getBytes());
        logger.debug("File saved to: {}", filePath);

        Avatar avatar = new Avatar();
        avatar.setFilePath(filePath.toString());
        avatar.setFileSize(file.getSize());
        avatar.setMediaType(file.getContentType());
        avatar.setData(file.getBytes());
        avatar.setStudent(student);

        Avatar savedAvatar = avatarRepository.save(avatar);
        logger.info("Avatar successfully saved for student id = {}", studentId);
        logger.debug("Saved avatar id = {}", savedAvatar.getId());
    }


    public Avatar getAvatarFromDB(Long studentId) {
        logger.info("Was invoked method for get avatar from DB");
        return avatarRepository.findStudentById(studentId)
                .orElseThrow(() -> new RuntimeException("Аватар не найден"));
    }


    public byte[] getAvatarFromDirectory(Long studentId) throws IOException {
        logger.info("Was invoked method for get avatar from directory for student id = {}", studentId);

        Avatar avatar = avatarRepository.findStudentById(studentId)
                .orElseThrow(() -> {
                    logger.error("Avatar not found in directory for student id = {}", studentId);
                    return new RuntimeException("Аватар не найден");
                });

        logger.debug("Avatar file path: {}", avatar.getFilePath());
        Path filePath = Paths.get(avatar.getFilePath());

        if (!Files.exists(filePath)) {
            logger.error("Avatar file not found on disk: {}", filePath);
            throw new RuntimeException("Файл аватара не найден");
        }

        return Files.readAllBytes(filePath);
    }
    public Page<Avatar> getAllAvatars(int page, int size) {
        logger.info("Was invoked method for get all avatars");
        Pageable pageable = PageRequest.of(page, size);
        return avatarRepository.findAll(pageable);
    }
}
