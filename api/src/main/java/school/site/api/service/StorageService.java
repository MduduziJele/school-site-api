package school.site.api.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;

public interface StorageService {
    void init();
    String store(MultipartFile file);
    Path load (String fileName);
    Resource loadAsResource(String filename);
    void delete(String filename);
}
