package s55_12419.t_28.shahd_ayman.services;

import s55_12419.t_28.shahd_ayman.models.Note;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import s55_12419.t_28.shahd_ayman.repositories.NoteRepository;

import java.util.List;

@Service
public class NoteService {
    private final NoteRepository noteRepository;
    public NoteService(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    // 1. Get all notes
    public List<Note> getAllNotes() {
        return noteRepository.findAll();  // already returns a copy in repository — good
    }

    // 2. Get note by ID or throw 404
    public Note getNoteById(String id) {
        return noteRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Note with id " + id + " not found"
                ));
    }

    // 3. Get notes by userId — throws 404 if NONE found (per lab)
    public List<Note> getNotesByUserId(String userId) {
        List<Note> userNotes = noteRepository.findByUserId(userId);
        if (userNotes.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "No notes found for user with id " + userId
            );
        }
        return userNotes;
    }

    // 4. Create note
    public Note createNote(Note note) {
        return noteRepository.save(note);
    }

    // 5. Update note or throw 404
    public Note updateNote(String id, Note updatedNote) {
        return noteRepository.update(id, updatedNote)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Note with id " + id + " not found"
                ));
    }

    // 6. Delete note or throw 404
    public void deleteNote(String id) {
        boolean deleted = noteRepository.deleteById(id);
        if (!deleted) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Note with id " + id + " not found"
            );
        }
    }

    public List<Note> getNotesByTitle(String title) {
        List<Note> userNotes = noteRepository.findByTitle(title);
        if (userNotes.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "No notes found for title " + title
            );
        }
        return userNotes;
    }

}