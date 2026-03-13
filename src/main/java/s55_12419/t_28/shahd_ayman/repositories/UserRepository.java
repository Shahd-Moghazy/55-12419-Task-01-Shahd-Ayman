package s55_12419.t_28.shahd_ayman.repositories;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;
//import tools.jackson.databind.SerializationFeature;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;
import s55_12419.t_28.shahd_ayman.models.User;

import java.io.File;
import java.io.InputStream;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class UserRepository {

    private List<User> users = new ArrayList<>();
    private File jsonFile;

    // ───────────────────────────────────────────────
    //                Constructor
    // ───────────────────────────────────────────────
    public UserRepository() {
        InputStream inputStream = getClass().getResourceAsStream("/users.json");
        if (inputStream == null) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Unable to read users.json"
            );
        }

        ObjectMapper objectMapper = new ObjectMapper();
        // Optional but recommended for clean ISO dates without timestamps
//        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        try {
            URI uri = getClass().getResource("/users.json").toURI();
            this.jsonFile = new File(uri);

            this.users = objectMapper.readValue(
                    inputStream,
                    new TypeReference<List<User>>() {}
            );

        } catch (Exception e) {
            this.jsonFile = new File("/data/users.json");
            // Re-try loading in fallback (close previous stream if needed)
            try (InputStream fallbackStream = getClass().getResourceAsStream("/users.json")) {
                this.users = objectMapper.readValue(
                        fallbackStream,
                        new TypeReference<List<User>>() {}
                );
            } catch (Exception ex) {
                throw new ResponseStatusException(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        "Failed to parse users.json",
                        ex
                );
            }
        } finally {
            try {
                inputStream.close();
            } catch (Exception ignored) {}
        }
    }

    // ───────────────────────────────────────────────
    //                   Methods
    // ───────────────────────────────────────────────

    public List<User> findAll() {
        return new ArrayList<>(users);   // return copy — good practice
    }

    public Optional<User> findById(String id) {
        return users.stream()
                .filter(user -> user.getId().equals(id))
                .findFirst();
    }

    public Optional<User> findByUsername(String username) {
        if (username == null) return Optional.empty();

        return users.stream()
                .filter(user -> user.getUsername() != null &&
                        user.getUsername().equalsIgnoreCase(username.trim()))
                .findFirst();
    }

    public User save(User user) {
        if (user.getId() == null || user.getId().isBlank()) {
            user.setId(java.util.UUID.randomUUID().toString());
        }
        if (user.getCreatedAt() == null) {
            user.setCreatedAt(LocalDateTime.now());
        }

        users.add(user);

        // Persist to file
        writeToFile();

        return user;
    }

    public Optional<User> update(String id, User updated) {
        return findById(id).map(existing -> {
            // Update only allowed fields (username, email)
            if (updated.getUsername() != null) {
                existing.setUsername(updated.getUsername());
            }
            if (updated.getEmail() != null) {
                existing.setEmail(updated.getEmail());
            }
            // Note: we usually don't update createdAt

            writeToFile();
            return existing;
        });
    }

    public boolean deleteById(String id) {
        boolean removed = users.removeIf(user -> user.getId().equals(id));
        if (removed) {
            writeToFile();
        }
        return removed;
    }

    // ───────────────────────────────────────────────
    //         Helper - write list back to JSON
    // ───────────────────────────────────────────────
    private void writeToFile() {
        if (jsonFile == null) return;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(jsonFile, users);
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Failed to write users.json",
                    e
            );
        }
    }
}