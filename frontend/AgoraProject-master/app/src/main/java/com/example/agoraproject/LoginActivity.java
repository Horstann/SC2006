package com.example.agoraproject;

import static com.google.android.gms.auth.api.signin.GoogleSignIn.getLastSignedInAccount;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.agoraproject.backend.AgoraBackEndInterface;
import com.example.agoraproject.backend.BackEndResource;
import com.example.agoraproject.backend.RetroFitClient;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * LoginActivity handles user login, the first screen a new user would see upon opening the
 * application. Allows a user to sign in via Google's Sign-In API. If a user has signed in previously,
 * this login screen is bypassed and the user is directly brought to MainPageActivity with
 * with the previously signed-in account.
 * @author Jun Wei Teo
 */

public class LoginActivity extends AppCompatActivity {
    GoogleSignInOptions gsio;
    GoogleSignInClient gsc;
    private String authToken;
    EditText et_UserName;
    EditText et_Password;
    Button  bt_Login;
    TextView tv_Signup;
    TextView tv_ForgotPassword;

    ImageButton googleSignInButton;

    private View.OnClickListener nextListener;
    private View.OnClickListener signupListener;
    private View.OnClickListener forgotListener;
    private View.OnClickListener loginListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        String client_ID = "143232307943-mnfh1u4sve8f166u8uvtntkntn5qa2uv.apps.googleusercontent.com";
        gsio = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(client_ID).requestEmail().build();
        gsc = GoogleSignIn.getClient(this,gsio);

        GoogleSignInAccount acct = getLastSignedInAccount(this);
        if(acct!=null){
            // post to firebase to check
            navigateToMainActivity(); // Bypass address page
        }
        googleSignInButton =  this.findViewById(R.id.googleSI_imageView);
        bt_Login = this.findViewById(R.id.login_button);
        bt_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this,MainPageActivity.class);
                startActivity(i);
                //Intent i = new Intent(LoginActivity.this,AddressActivity.class);
                //startActivity(i);
            }
        });

        googleSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1000){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                task.getResult(ApiException.class);
                //navigateToMainActivity();
            } catch (ApiException e) {
                Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }
            handleSignInResult(task);
        }

    }

    private void handleSignInResult(Task<GoogleSignInAccount> task) {
        try {
            GoogleSignInAccount account = task.getResult(ApiException.class);
            // query database on successful login
            authToken = account.getIdToken();
            Log.e("TEST TOKEN1: ",authToken);
            Log.e("TEST TOKEN1 NAME: ",account.getDisplayName());

            HashMap<String, Object> map =  new HashMap<>();
            AgoraBackEndInterface api = RetroFitClient.getRetrofitInstance().create(AgoraBackEndInterface.class);
            map.put("authKey",authToken);
            map.put("cmd","signIn");

            Call<BackEndResource> call = api.getInfo(map);
            call.enqueue(new Callback<BackEndResource>() {
                @Override
                public void onResponse(Call<BackEndResource> call, Response<BackEndResource> response) {
                    Log.e("TEST" , "onResponse code Login: "+response.code());
                    if(response.code()== 200){
                        Log.e("TEST" , "onResponse: get Sign In Status Login:"+response.body().getStatus());
                        if(response.body().getStatus() == 0){
                            // Successful Sign In, Has Account with Backend
                            Intent i  = new Intent(LoginActivity.this,MainPageActivity.class);
                            startActivity(i);
                        }
                        else if (response.body().getStatus() == 2){
                            // Sucessful Auth Key, but not tied to account, go address to enter
                            SharedPreferences pref = getSharedPreferences("UserAuthKey", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = pref.edit();
                            editor.putString("USER_AUTHKEY",authToken);
                            editor.commit();

                            Intent i = new Intent(LoginActivity.this,AddressActivity.class);
                            startActivity(i);
                        }
                        else{
                            Toast.makeText(LoginActivity.this,"Error Please Try Again Later! \nStatus: "+response.body().getStatus(),Toast.LENGTH_LONG).show();
                        }
                    }
                    else{
                        Toast.makeText(LoginActivity.this,"Error With Backend! "+response.code(),Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<BackEndResource> call, Throwable t) {
                    Log.e("TEST" , "onFailure: get hello :"+ t.getMessage());
                }
            });
            // Signed in successfully, show authenticated UI.
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("Error", "signInResult:failed code=" + e.getStatusCode());
        }
    }

    public void signIn(){
        Intent signInIntent = gsc.getSignInIntent();
        startActivityForResult(signInIntent,1000);
    }

    public void navigateToMainActivity(){
        finish();
        Intent intent = new Intent(LoginActivity.this,MainPageActivity.class);
        startActivity(intent);
    }
}