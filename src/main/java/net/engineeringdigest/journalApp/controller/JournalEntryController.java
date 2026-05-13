package net.engineeringdigest.journalApp.controller;

import net.engineeringdigest.journalApp.entity.JournalEntry;
import net.engineeringdigest.journalApp.service.JournalEntryService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/journal")
public class JournalEntryController {

    @Autowired
    private JournalEntryService journalEntryService;

    @GetMapping
    public ResponseEntity<?> getAll() {
        List<JournalEntry> all = journalEntryService.getAll();
        if(all != null&& !all.isEmpty()){
            return new ResponseEntity<>(all, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<JournalEntry> createEntry(@RequestBody JournalEntry myEntry) {
        try {
            myEntry.setDate(LocalDateTime.now());
            journalEntryService.saveEntry(myEntry);
            return  new  ResponseEntity<>(myEntry, HttpStatus.CREATED);
        }catch (Exception e){
            return  new  ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }


    }

    @GetMapping("id/{myId}")
    public ResponseEntity<JournalEntry> getJournalEntryById(@PathVariable ObjectId myId) {
        Optional<JournalEntry> entry = journalEntryService.findById(myId);
        if (entry.isPresent()) {
            return new ResponseEntity<>(entry.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("id/{myId}")
    public ResponseEntity<JournalEntry> deleteJournalEntryById(@PathVariable ObjectId myId) {
         journalEntryService.deleteById(myId);
         return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/id/{Id}")
    public ResponseEntity<JournalEntry> updateJournalById(@PathVariable ObjectId Id,@RequestBody JournalEntry newEntry) {
        JournalEntry old = journalEntryService.findById(Id).orElse(null);
        if(old != null){
            old.setTitle(newEntry.getTitle() !=null && !newEntry.getTitle().equals("") ? newEntry.getTitle() : old.getTitle());
            old.setContent(newEntry.getContent() !=null && !newEntry.getContent().equals("") ? newEntry.getContent() : old.getContent());
            journalEntryService.saveEntry(old);
            return  new ResponseEntity<>(old, HttpStatus.OK);
        }
        return  new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }

}
