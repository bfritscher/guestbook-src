package ch.hegarc.guestbook.models;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface EntryRepository extends CrudRepository<Entry, String>  {
    public List<Entry> findAllByOrderBySignedOnDesc();
}
