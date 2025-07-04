package com.quiz.quiz_backend.Entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FileUploadEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int fileId;
    @Column(unique = true)
    private String name;
    @Column(unique = true)
    private String fileName;
    private String fileType;
    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] fileData;

}
