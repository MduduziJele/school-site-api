package school.site.api.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import school.site.api.model.GalleryImage;
import school.site.api.service.GalleryService;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class GalleryImageController {
    @Autowired
    private GalleryService galleryService;

    @PostMapping("/upload")
    public ResponseEntity<GalleryImage> addGallery(@RequestParam(name = "file") MultipartFile multipartFile) {
        galleryService.saveImageGallery(multipartFile);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/update/{galleryId}")
    public ResponseEntity<Void> updateGallery(
            @PathVariable Long galleryId,
            @RequestParam(name = "file", required = false) MultipartFile file) {
        galleryService.updateGallery(galleryId, file);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteGallery(@PathVariable Long id) {
        galleryService.deletePost(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/galleryImages")
    public ResponseEntity<List<GalleryImage>> getAllGalleryImages() {
        List<GalleryImage> galleryImages = galleryService.galleryImages();
        return new ResponseEntity<>(galleryImages, HttpStatus.OK);
    }

    @GetMapping("/gallery/{id}")
    public ResponseEntity<GalleryImage> getGalleryById(@PathVariable Long id) {
        GalleryImage galleryImage = galleryService.findGalleryById(id);
        return new ResponseEntity<>(galleryImage, HttpStatus.OK);
    }
    @GetMapping("/galleries/{fileName}")
    public ResponseEntity<Resource> serverFile(@PathVariable String fileName) {
        Resource resource = galleryService.readFile(fileName);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment;filename=\"" +
                        resource.getFilename()+"\"").body(resource);
    }
}



