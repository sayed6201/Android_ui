=====================================================================================================
Logout 
=====================================================================================================
FirebaseAuthController.signOut(ViewActivity.this);
            startActivity(new Intent(this, MainActivity.class));
            finish();







=====================================================================================================
FirebaseAuthController.java class to control auth
=====================================================================================================
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.sayed.smart_attendence_with_fr.R;


public class FirebaseAuthController {

    private static FirebaseAuth mAuth;
    private static FirebaseUser currentUser;

    private static GoogleSignInClient mGoogleSignInClient;


    public static FirebaseAuth getAuthInctance(){
        if(mAuth == null){
            mAuth = FirebaseAuth.getInstance();
        }
        return mAuth;
    }

    public static boolean isSignedIn(){

        return getAuthInctance().getCurrentUser() != null;

    }

    public static FirebaseUser getCurrentUser(){
        currentUser = getAuthInctance().getCurrentUser();
        return currentUser;
    }

    public static void signOut(final Context context){
//        if(mAuth != null){
//            mAuth.signOut();
//        }
        if (mGoogleSignInClient == null){
            mGoogleSignInClient = configureGoogleSignIn(context);
        }
        if(mGoogleSignInClient != null){
            getAuthInctance().signOut();
            mGoogleSignInClient.signOut().addOnCompleteListener((Activity) context,
                    new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete( Task<Void> task) {
                            Toast.makeText(context, ""+task.toString(), Toast.LENGTH_SHORT).show();
                            Log.i("sign_out",task.toString());
                        }
                    });
        }
    }

    private static GoogleSignInClient configureGoogleSignIn(Context context) {
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(context.getString(R.string.default_web_client_id))
                .requestEmail()//request email id
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
        return mGoogleSignInClient = GoogleSignIn.getClient(context, gso);
    }

}
