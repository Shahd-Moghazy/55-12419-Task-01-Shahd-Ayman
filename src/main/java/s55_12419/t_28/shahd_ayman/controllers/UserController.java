package s55_12419.t_28.shahd_ayman.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import s55_12419.t_28.shahd_ayman.models.User;
import s55_12419.t_28.shahd_ayman.services.UserService;

import java.util.List;
import s55_12419.t_28.shahd_ayman.models.Note;
import s55_12419.t_28.shahd_ayman.services.NoteService;

@RestController
@RequestMapping("/users")   // All endpoints start with /users
public class UserController {

    private final UserService userService;
    private final NoteService noteService;

    // Constructor injection (best practice)
    public UserController(UserService userService, NoteService noteService) {
        this.userService = userService;
        this.noteService = noteService;
    }

    // 1. GET /users → all users
    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    // 2. GET /users/{id} → single user
    @GetMapping("/{id}")
    public User getUserById(@PathVariable String id) {
        return userService.getUserById(id);
    }

    // 3. POST /users → create user (body without id)
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)   // 201 Created
    public User createUser(@RequestBody User user) {
        // Note: user.id should be null/empty from client → repository generates it
        return userService.createUser(user);
    }

    // 4. PUT /users/{id} → update user
    @PutMapping("/{id}")
    public User updateUser(@PathVariable String id, @RequestBody User user) {
        return userService.updateUser(id, user);
    }

    // 5. DELETE /users/{id} → delete (returns 204 No Content by default)
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
    }

    // 6. GET /users/search?username=... → search by username (case-insensitive)
    @GetMapping("/search")
    public User searchByUsername(@RequestParam("username") String username) {
        return userService.getUserByUsername(username);  // ← you'll add this to service next
        // If not found → service throws 404 automatically
    }

    // 7. GET /users/{id}/notes → get notes belonging to a user
    @GetMapping("/{id}/notes")
    public List<Note> getUserNotes(@PathVariable String id) {
        return noteService.getNotesByUserId(id);
    }
}