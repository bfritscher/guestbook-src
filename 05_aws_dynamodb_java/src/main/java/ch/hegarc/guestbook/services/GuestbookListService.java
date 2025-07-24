package ch.hegarc.guestbook.services;

import ch.hegarc.guestbook.models.Entry;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class GuestbookListService implements GuestbookService {

    private List<Entry> entries = new ArrayList<Entry>();

    public List<Entry> select() {
        return entries;
    }

    public void insert(Entry entry) {
        entries.add(entry);
    }
}

