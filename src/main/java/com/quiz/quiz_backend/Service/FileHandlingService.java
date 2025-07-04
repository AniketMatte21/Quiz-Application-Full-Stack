package com.quiz.quiz_backend.Service;

import com.quiz.quiz_backend.Entity.FileUploadEntity;
import com.quiz.quiz_backend.Repository.FileUploadRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Service
public class FileHandlingService
{
    @Autowired
    FileUploadRepo fileUploadRepo;

    public String uploadImage(String path, MultipartFile file) throws IOException {
        //steps
        //filename
        String fileName=file.getOriginalFilename();

        if(!(fileName.toLowerCase().endsWith(".png") || fileName.toLowerCase().endsWith(".jpg") || fileName.toLowerCase().endsWith(".jpeg")))
        {
            return "Only images are allowed";
        }

        //FullPath
        String randomId = UUID.randomUUID().toString();
        String fileName1 = randomId.concat(fileName.substring(fileName.lastIndexOf('.')));

        String fullPath=path+ File.separator+fileName1;

        //Create folder if not created
        File f=new File(path);
        if(!f.exists())
        {
            f.mkdir();
        }

        //copy file
        Files.copy(file.getInputStream(), Paths.get(fullPath));

        return fileName;
    }

    public ResponseEntity<String> uploadFile(MultipartFile file,String name) throws IOException {

        fileUploadRepo.save(FileUploadEntity.builder()
                .fileName(file.getOriginalFilename())
                .fileType(file.getContentType())
                .name(name)
                .fileData(file.getBytes())
                .build());

        return ResponseEntity.ok("file save to db ");

    }

    public ResponseEntity<byte[]> getFile(int fileId)
    {
        FileUploadEntity file = fileUploadRepo.findById(fileId).orElseThrow(() -> new RuntimeException("File not found"));
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType((file.getFileType())))
                .header(HttpHeaders.CONTENT_DISPOSITION,"inline; filename=\"" + file.getFileName()+ "\"")
                .body(file.getFileData());
    }

    public ResponseEntity<List<FileUploadEntity>> getAllFiles()
    {
        List<FileUploadEntity> allFiles = fileUploadRepo.findAll();
        return ResponseEntity.ok(allFiles);
    }
}
