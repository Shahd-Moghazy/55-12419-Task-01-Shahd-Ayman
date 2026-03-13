package s55_12419.t_28.shahd_ayman.repositories;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;
import s55_12419.t_28.shahd_ayman.models.Note;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class NoteRepository {

    private List<Note> notes = new ArrayList<>();
    private File jsonFile;

    public NoteRepository() {
        InputStream inputStream = getClass().getResourceAsStream("/notes.json");

        if (inputStream == null) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Unable to read notes.json"
            );
        }

        ObjectMapper objectMapper = new ObjectMapper();

        try {
            // Try to locate the real file path (works in IDE/development)
            URI uri = getClass().getResource("/notes.json").toURI();
            this.jsonFile = new File(uri);

            // Load the data
            this.notes = objectMapper.readValue(
                    inputStream,
                    new TypeReference<List<Note>>() {}
            );

        } catch (Exception e) {
            // Fallback for Docker/containerized runtime
            this.jsonFile = new File("/data/notes.json");

            // Still try to load from classpath (even in fallback)
            try (InputStream fallbackStream = getClass().getResourceAsStream("/notes.json")) {
                this.notes = objectMapper.readValue(
                        fallbackStream,
                        new TypeReference<List<Note>>() {}
                );
            } catch (Exception ex) {
                throw new ResponseStatusException(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        "Failed to parse notes.json",
                        ex
                );
            }
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (Exception ignored) {
            }
        }
    }

    // 1. Return all notes (defensive copy)
    public List<Note> findAll() {
        return new ArrayList<>(notes);
    }

    // 2. Find by ID
    public Optional<Note> findById(String id) {
        return notes.stream()
                .filter(note -> note.getId().equals(id))
                .findFirst();
    }

    // 3. Find all notes for a specific user
    public List<Note> findByUserId(String userId) {
        return notes.stream()
                .filter(note -> note.getUserId().equals(userId))
                .toList();
    }

    // Find all notes for a specific title
    public List<Note> findByTitle(String title) {
        return notes.stream()
                .filter(note -> note.getTitle().equals(title))
                .toList();
    }

    // 4. Save new note + write to file
    public Note save(Note note) {
        if (note.getId() == null || note.getId().isBlank()) {
            note.setId(UUID.randomUUID().toString());
        }
        notes.add(note);
        writeToFile();
        return note;
    }

    // 5. Update existing note + write to file
    public Optional<Note> update(String id, Note updated) {
        return findById(id).map(existing -> {
            // Update allowed fields (title, content, userId)
            if (updated.getTitle() != null) {
                existing.setTitle(updated.getTitle());
            }
            if (updated.getContent() != null) {
                existing.setContent(updated.getContent());
            }
            if (updated.getUserId() != null) {
                existing.setUserId(updated.getUserId());
            }
            // Note: usually don't update createdAt or id

            writeToFile();
            return existing;
        });
    }

    // 6. Delete by ID + write to file
    public boolean deleteById(String id) {
        boolean removed = notes.removeIf(note -> note.getId().equals(id));
        if (removed) {
            writeToFile();
        }
        return removed;
    }

    // Helper to persist changes back to JSON file
    private void writeToFile() {
        if (jsonFile == null) {
            return;
        }
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(jsonFile, notes);
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Failed to write to notes.json",
                    e
            );
        }
    }
}