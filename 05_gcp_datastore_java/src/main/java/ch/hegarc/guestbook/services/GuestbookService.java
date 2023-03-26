package ch.hegarc.guestbook.services;

import java.util.List;

import ch.hegarc.guestbook.models.Entry;

public interface GuestbookService {
    public List<Entry> select();
    public void insert(Entry entry);
}
