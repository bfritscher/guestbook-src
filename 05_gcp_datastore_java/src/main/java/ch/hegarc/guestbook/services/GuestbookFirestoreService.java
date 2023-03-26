package ch.hegarc.guestbook.services;

import ch.hegarc.guestbook.models.Entry;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import org.springframework.stereotype.Service;

import static ch.hegarc.guestbook.FirestorePersistence.getFirestore;

import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.List;


@Service
public class GuestbookFirestoreService implements GuestbookService {

    public List<Entry> select() {
        List<Entry> entries = new ArrayList<Entry>();
        ApiFuture<QuerySnapshot> query =
                getFirestore().collection("Reviews").orderBy("signed_on", Query.Direction.DESCENDING).get();
        try {
            QuerySnapshot querySnapshot = query.get();
            for (QueryDocumentSnapshot entry : querySnapshot.getDocuments()) {
                entries.add(entry.toObject(Entry.class));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return entries;
    }

    public void insert(Entry entry) {
        DocumentReference docRef = getFirestore().collection("Reviews").document();
        Map<String, Object> entryData = new HashMap<>();
        entryData.put("name", entry.getName());
        entryData.put("email", entry.getEmail());
        entryData.put("message", entry.getMessage());
        entryData.put("signed_on", entry.getSigned_on());
        ApiFuture<WriteResult> query = docRef.set(entryData);
        try {
            query.get();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}

