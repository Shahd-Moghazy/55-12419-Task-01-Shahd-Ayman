package services;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import models.User;
import repositories.UserRepository;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    // Constructor injection (this is the modern & recommended way)
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // 1. Get all users
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // 2. Get user by ID or throw 404
    public User getUserById(String id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "User with id " + id + " not found"
                ));
    }

    // 3. Create a new user
    public User createUser(User user) {
        // You can add validation here later if needed (e.g. username/email unique)
        return userRepository.save(user);
    }

    // 4. Update existing user or throw 404
    public User updateUser(String id, User updatedUser) {
        return userRepository.update(id, updatedUser)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "User with id " + id + " not found"
                ));
    }

    // Add this to UserService
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "User with username '" + username + "' not found"
                ));
    }

    // 5. Delete user or throw 404
    public void deleteUser(String id) {
        boolean deleted = userRepository.deleteById(id);
        if (!deleted) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "User with id " + id + " not found"
            );
        }
    }
}