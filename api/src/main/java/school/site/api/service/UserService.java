package school.site.api.service;

import org.springframework.stereotype.Service;
import school.site.api.model.User;
import school.site.api.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public User save(User user) {
        userRepository.save(user);
        return null;
    }

    public List<User> saveAll(List<User> userList) {

        userRepository.saveAll(userList);
        return null;
    }

    public Optional<User> findById(int id) {
        return userRepository.findById(id);

    }

    public List<User> findAll() {
        return (List<User>) userRepository.findAll();
    }

    public User update(int id, User user) {

        User foundUser = userRepository.findById(id).get();
        foundUser.setEmail(user.getEmail());
        foundUser.setFirst_name(user.getLast_name());
        foundUser.setLast_name(user.getLast_name());
        userRepository.save(foundUser);
        return user;
    }

    public void delete(User user) {
        userRepository.delete(user);
    }

    public void deleteById(int id){
        userRepository.deleteById(id);
    }
}