package school.site.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import school.site.api.model.GalleryImage;
import school.site.api.repository.GalleryImageRepository;

import java.util.List;

@Service
public class GalleryService {
    @Autowired
    private GalleryImageRepository galleryImageRepository;
    @Autowired
    private StorageService storageService;
    public GalleryImage saveImageGallery(MultipartFile file) {
        String fileName = storageService.store(file);
        GalleryImage galleryImage = new GalleryImage();
        galleryImage.setImagePath(fileName);
        return galleryImageRepository.save(galleryImage);
    }
    public String uploadImage(MultipartFile file) {
        return storageService.store(file);
    }

    public void deleteImage(String imageName) {
        storageService.delete(imageName);
    }
    public void updateGallery(Long galleryId, MultipartFile file) {
        GalleryImage image = galleryImageRepository.findById(galleryId).orElseThrow(() ->
                new RuntimeException("gallery image with id [%s] not found".formatted(galleryId)
                ));
        String oldImage = image.getImagePath();

        if(!file.isEmpty()) {
            image.setImagePath(uploadImage(file));
            if(oldImage != null && !oldImage.equals("")) {
                deleteImage(oldImage);
            }
        }
    }

    public void deletePost(Long id) {
        GalleryImage galleryImage = galleryImageRepository.findById(id).orElseThrow(() ->
                new RuntimeException("Gallery "));
        if(galleryImage.getImagePath() != null && !galleryImage.getImagePath().equals("")) {
            deleteImage(galleryImage.getImagePath());
        }
        galleryImageRepository.delete(galleryImage);
    }

    public List<GalleryImage> galleryImages () {
        return galleryImageRepository.findAll();
    }
    public Resource readFile(String fileName) {
        Resource resources = storageService.loadAsResource(fileName);
        return resources;
    }

    public GalleryImage findGalleryById(Long id) {
        return galleryImageRepository.findById(id).get();
    }
}
