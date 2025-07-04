package com.quiz.quiz_backend.Controller;

import com.quiz.quiz_backend.Entity.FileUploadEntity;
import com.quiz.quiz_backend.Service.FileHandlingService;
import com.quiz.quiz_backend.dto.FileResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/file")
public class FileController
{

    @Value("${project.image}")
    private String path;

    @Autowired
    FileHandlingService fileHandlingService;

    @PostMapping("/uploadImage")
    public ResponseEntity<FileResponse> uploadImage(
            @RequestParam("image")MultipartFile file
            ) throws IOException {
        String fileName = fileHandlingService.uploadImage(path, file);
        return new ResponseEntity<>(new FileResponse(fileName,"image is uploaded successfully"),HttpStatus.OK);
    }

    @PostMapping("/uploadFile")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file,@RequestParam String name) throws IOException {
        return fileHandlingService.uploadFile(file,name);
    }

    @PostMapping("/getFileByName")
    public ResponseEntity<byte[]> getFile(@RequestParam int fileId)
    {
        return fileHandlingService.getFile(fileId);
    }

    @GetMapping("/getFiles")
    public ResponseEntity<List<FileUploadEntity>> getAllFiles()
    {
        return fileHandlingService.getAllFiles();
    }
}
