package ch.hegarc.guestbook.services;

import ch.hegarc.guestbook.models.Entry;
import ch.hegarc.guestbook.models.EntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;


@Service
public class GuestbookSqlService implements GuestbookService {

    @Autowired
    private EntryRepository entryRepository;

    public List<Entry> select() {
        return  entryRepository.findAllByOrderBySignedOnDesc();
    }

    public void insert(Entry entry) {
        entryRepository.save(entry);
    }
}

