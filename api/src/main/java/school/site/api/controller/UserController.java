package school.site.api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import school.site.api.model.User;
import school.site.api.service.UserService;

import java.util.List;

public class UserController {

    private final UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    @GetMapping("/all")
    public List<User> findAllUsers(){
        return userService.findAll();
    }

    @PostMapping("/save")
    public void save(@RequestBody User user){
        userService.save(user);
    }

    @GetMapping("/delete/{id}")
    public void delete(@PathVariable int id){
        userService.deleteById(id);
    }

    @PostMapping("/updateUser/{user_id}")
    public void updateUser(@RequestBody User user, @PathVariable int user_id) {
        userService.update(user_id,user);
    }
}