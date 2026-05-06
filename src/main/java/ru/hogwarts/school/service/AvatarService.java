package ru.hogwarts.school.service;

import jakarta.transaction.Transactional;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
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
    @Value("avatar")
    private String avatarsDir;
    private final StudentRepository studentRepository;
    private final AvatarRepository avatarRepository;
    public AvatarService (StudentRepository studentRepository, AvatarRepository avatarRepository) {
        this.studentRepository = studentRepository;
        this.avatarRepository = avatarRepository;
    }
    public void uploadAvatar(Long studentId, MultipartFile file) throws IOException {

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Студент не найден"));


        Path uploadPath = Paths.get(avatarsDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }


        String fileName = studentId + "_" + file.getOriginalFilename();
        Path filePath = uploadPath.resolve(fileName);


        Files.write(filePath, file.getBytes());


        Avatar avatar = new Avatar();
        avatar.setFilePath(filePath.toString());
        avatar.setFileSize(file.getSize());
        avatar.setMediaType(file.getContentType());
        avatar.setData(file.getBytes());
        avatar.setStudent(student);

        avatarRepository.save(avatar);
    }


    public Avatar getAvatarFromDB(Long studentId) {
        return avatarRepository.findStudentById(studentId)
                .orElseThrow(() -> new RuntimeException("Аватар не найден"));
    }


    public byte[] getAvatarFromDirectory(Long studentId) throws IOException {
        Avatar avatar = avatarRepository.findStudentById(studentId)
                .orElseThrow(() -> new RuntimeException("Аватар не найден"));

        Path filePath = Paths.get(avatar.getFilePath());
        return Files.readAllBytes(filePath);
    }
}
