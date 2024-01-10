package school.site.api.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.jsonwebtoken.io.IOException;
import school.site.api.model.About;
import school.site.api.payload.response.MessageResponse;
import school.site.api.repository.AboutRepository;
import school.site.api.service.AboutService;

@RestController
@RequestMapping("/api/auth/")
public class AboutController {

    @Autowired
    private AboutService aboutService;

    @Autowired
    private AboutRepository aboutRepository;

    @GetMapping("/about/{aboutId}")
    public About getByAboutId(@PathVariable Integer aboutId) {
        return aboutService.getByAboutId(aboutId);
    }

    @PutMapping("/update/aboutus/{aboutId}")
    public About updateAboutUs(@PathVariable Integer aboutId, @RequestBody About about) {
        About existAbout = aboutService.getByAboutId(aboutId);
        existAbout.setAboutustext(about.getAboutustext());
        return aboutRepository.save(existAbout);

    }

    @PutMapping("/update/mission/{aboutId}")
    public About updateAboutMission(@PathVariable Integer aboutId, @RequestBody About about) {
        About existAbout = aboutService.getByAboutId(aboutId);
        existAbout.setMission(about.getMission());
        return aboutRepository.save(existAbout);

    }

    @PutMapping("/update/vision/{aboutId}")
    public About updateAboutVision(@PathVariable Integer aboutId, @RequestBody About about) {
        About existAbout = aboutService.getByAboutId(aboutId);
        existAbout.setVision(about.getVision());
        return aboutRepository.save(existAbout);
    }

    @PostMapping("/add/about/image")
    public ResponseEntity<?> addImage(@RequestPart("image") MultipartFile file, @RequestParam("option") String option)
            throws java.io.IOException {

//        String uploadDirectory = System.getProperty("user.dir") + File.separator
//                + "api/src/main/resources/static/about/";
//        Path imagePath = Paths.get(uploadDirectory, file.getOriginalFilename());
//        Files.write(imagePath, file.getBytes());
//
//        About myAbout = aboutRepository.findByAboutId(1);
//
//        if(option.equals("vision")){
//            myAbout.setImageUrlVision(file.getOriginalFilename());
//        } else {
//            myAbout.setImageUrlMission(file.getOriginalFilename());
//        }
//        aboutRepository.save(myAbout);

        String uploadDirectory = System.getProperty("user.dir") + File.separator + "api/src/main/resources/static/";
        Path imagePath = Paths.get(uploadDirectory, file.getOriginalFilename());
        Files.write(imagePath, file.getBytes());
        System.out.println("test completed");

        return ResponseEntity.ok().body(new MessageResponse("Done"));
    }

     @GetMapping("/image/mission/{id}")
    public ResponseEntity<byte[]> getMissionImage(@PathVariable("id") Integer id) throws java.io.IOException {
        String uploadDirectory = System.getProperty("user.dir") + File.separator +  "api/src/main/resources/static/about/";
        About about = aboutRepository.findByAboutId(id);
        String imageName = about.getImageUrlMission();
        byte[] image = new byte[0];
        try {
            File file = new File(uploadDirectory + imageName);
            if(file.exists()){
                image = Files.readAllBytes(Paths.get(uploadDirectory+imageName));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(image);
    }

    @GetMapping("/image/vision/{id}")
    public ResponseEntity<byte[]> getVisionImage(@PathVariable("id") Integer id) throws java.io.IOException {
        String uploadDirectory = System.getProperty("user.dir") + File.separator +  "api/src/main/resources/static/about/";
        About about = aboutRepository.findByAboutId(id);
        String imageName = about.getImageUrlVision();
        byte[] image = new byte[0];
        try {
            File file = new File(uploadDirectory + imageName);
            if(file.exists()){
                image = Files.readAllBytes(Paths.get(uploadDirectory+imageName));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(image);
    }

}
