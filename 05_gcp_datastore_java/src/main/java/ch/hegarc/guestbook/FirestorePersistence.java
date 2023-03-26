package ch.hegarc.guestbook;

import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;

public class FirestorePersistence {
    private static Firestore firestore;

    public static Firestore getFirestore() {
        if (firestore == null) {
            Firestore db =
                    FirestoreOptions.getDefaultInstance().getService();
            firestore = db;
        }

        return firestore;
    }

    public static void setFirestore(Firestore firestore) {
        FirestorePersistence.firestore = firestore;
    }
}
