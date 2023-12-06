package school.site.api.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import school.site.api.model.About;
import school.site.api.model.Post;
import school.site.api.repository.AboutRepository;

@Service
public class AboutService {
    
    @Autowired
    private AboutRepository aboutRepository;


    public About getByAboutId(Integer aboutId){
        return aboutRepository.findByAboutId(aboutId);
    }

    
}
