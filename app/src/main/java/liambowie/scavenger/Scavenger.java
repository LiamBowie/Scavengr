package liambowie.scavenger;

import com.firebase.client.Firebase;

/**
 * Created by Liam on 29/04/2016.
 */
public class Scavenger extends android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);
    }
}
