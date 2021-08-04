package com.example.ordersystem.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.stream.Stream;

public interface FilesStorageService {
    public Path init(String id);

    public void save(MultipartFile file, String id);

    public Resource load(String filename);

    public void deleteAll(String id);

    public Stream<Path> loadAll();
}
