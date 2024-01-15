package school.site.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import school.site.api.model.Post;
import school.site.api.repository.UserRepository;
import school.site.api.service.PostService;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/auth")
public class PostController {
    private final PostService postService;
    private final UserRepository userRepository;
    @Autowired
    public PostController(PostService postService, UserRepository userRepository){
        this.postService = postService;
        this.userRepository = userRepository;
    }

    @PostMapping("/post")
    public ResponseEntity<Post> addPost(@RequestParam("title") String title,
                                        @RequestParam("content") String content,
                                        @RequestParam("image") MultipartFile image,
                                        Principal principal
    ) {
        String email = principal.getName();
        postService.createPost(email,title,content,image);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @GetMapping("/post/{id}")
    @CrossOrigin(origins = "http://localhost:5173")
    public Post getPostById(@PathVariable Integer id) {
        return postService.findPostByID(id);
    }
    @PostMapping("/image")
    public ResponseEntity<?> addPicture(@RequestParam("image") MultipartFile image) {
        postService.uploadImage(image);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/image/{fileName}")
    public ResponseEntity<Resource> serverFile(@PathVariable String fileName) {
        Resource resource = postService.readFile(fileName);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + resource.getFilename() + "\"").body(resource);
    }
    @GetMapping("/all/post")
    public List<Post> posts(){
        return postService.posts();
    }

    @PutMapping("/{postId}")
    @CrossOrigin(origins = "http://localhost:5173")
    public ResponseEntity<?> updatePost(
            Principal principal,
            @PathVariable Integer postId,
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestParam("image") MultipartFile image) {
        String email = principal.getName();
        postService.updatePost(email,postId,title,content,image);
        return ResponseEntity.noContent().build();
    }
    @DeleteMapping("/{id}")
    @CrossOrigin(origins = "http://localhost:5173")
    public ResponseEntity<?> deletePost(@PathVariable Integer id,Principal principal) {
        String email = principal.getName();
        postService.deletePost(id,email);

        return ResponseEntity.noContent().build();
    }
}
