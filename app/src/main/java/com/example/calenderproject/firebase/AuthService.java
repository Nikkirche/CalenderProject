package com.example.calenderproject.firebase;
import com.example.calenderproject.models.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class AuthService {
    public static void signOut() {
        FirebaseAuth.getInstance()
                .signOut();
    }

    public static Task<AuthResult> signInEmailAndPasword(final String email, String password, final String name) {
        return FirebaseAuth.getInstance()
                .createUserWithEmailAndPassword( email, password )
                .addOnSuccessListener( new OnSuccessListener<AuthResult>() {
                    @Override

                    public void onSuccess(AuthResult authResult) {
                        final String id = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        UserService.createNewUser( email, name, id );
                        User user = new User( id, name, email, null );
                    }
                } );
    }

    public static Task<AuthResult> signIn(final String email, String password) {
        return FirebaseAuth.getInstance()
                .signInWithEmailAndPassword( email, password );
    }

    public static boolean CheckStatusOfUser() {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        return firebaseAuth.getCurrentUser() != null;
    }

}