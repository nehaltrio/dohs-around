package com.ecom.dohsaround.model;

import javax.persistence.*;

@Entity
public class FileInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    private String name;
    @Lob
    @Column(columnDefinition = "MEDIUMBLOB")
    private String file;
    private String fileType;

    public FileInfo(String name, String file, String fileType) {
        this.name = name;
        this.file = file;
        this.fileType = fileType;
    }

    public FileInfo() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }
}