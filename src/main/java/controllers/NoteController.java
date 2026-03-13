package controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import models.Note;
import services.NoteService;

import java.util.List;

@RestController
@RequestMapping("/notes")   // All endpoints start with /notes — correct
public class NoteController {

    private final NoteService noteService;

    // Constructor injection — best practice, correct
    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    // 1. GET /notes — returns all notes
    @GetMapping
    public List<Note> getAllNotes() {
        return noteService.getAllNotes();
    }

    // 2. GET /notes/{id} — returns a single note by id
    @GetMapping("/{id}")
    public Note getNoteById(@PathVariable String id) {
        return noteService.getNoteById(id);
    }

    // 3. POST /notes — accepts JSON body (including userId) and returns created note
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)  // 201 Created — good
    public Note createNote(@RequestBody Note note) {
        // note.id should be null/empty from client → repository generates it
        // note.userId must be provided in JSON body
        return noteService.createNote(note);
    }

    // 4. GET /notes/search?title=... — returns notes whose title contains the query (case-insensitive)
    @GetMapping("/search")
    public List<Note> searchByTitle(@RequestParam("title") String title) {
        return noteService.getNotesByTitle(title);
    }

    // 5. PUT /notes/{id} — accepts note body to update and returns modified note
    @PutMapping("/{id}")
    public Note updateNote(@PathVariable String id, @RequestBody Note note) {
        return noteService.updateNote(id, note);
    }

    // 6. DELETE /notes/{id} — removes the note and returns nothing (204 No Content)
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteNote(@PathVariable String id) {
        noteService.deleteNote(id);
    }
}