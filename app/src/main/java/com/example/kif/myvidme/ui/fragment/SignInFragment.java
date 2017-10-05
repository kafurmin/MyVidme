package com.example.kif.myvidme.ui.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.kif.myvidme.BuildConfig;
import com.example.kif.myvidme.R;
import com.example.kif.myvidme.api.ApiClient;
import com.example.kif.myvidme.model.Auth;
import com.example.kif.myvidme.model.Response;
import com.example.kif.myvidme.api.VidmeApi;
import com.example.kif.myvidme.ui.activity.MainActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;



public class SignInFragment extends Fragment {

    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mLoginFormView;

    public String sharedPreferencesUsername;
    public String sharedPreferencesPassword;
    public String usernameValue;
    public String passwordValue;
    public Call<Response> call;
    public String token;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_signin, container, false);

        setHasOptionsMenu(false);

        mEmailView = (AutoCompleteTextView) rootView.findViewById(R.id.username);

        mPasswordView = (EditText) rootView.findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    SignIn();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = rootView.findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SignIn();
            }
        });

        mLoginFormView = rootView.findViewById(R.id.login_form);

        return rootView;
    }

    public void SignIn() {

        attemptLogin();
/*

        Retrofit retrofitAdapter = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BuildConfig.API_ENDPOINT)
                .build();

        final VidmeApi vidmeApi = retrofitAdapter.create(VidmeApi.class);


         usernameValue = mEmailView.getText().toString();
         passwordValue = mPasswordView.getText().toString();

        if(sharedPreferencesUsername ==null || sharedPreferencesPassword ==null) {
            call = vidmeApi.authCreate(usernameValue, passwordValue);
        }
        else{
            call = vidmeApi.authCreate(sharedPreferencesUsername, sharedPreferencesPassword);
        }
        call.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                showProgress(false);
                    if (response.body().getStatus()) {

                        token = response.body().getAuth().getToken();

                        Intent i = new Intent().putExtra("token", token);

                        Fragment targetFragment = getTargetFragment();
                        if (targetFragment != null) {
                            targetFragment.onActivityResult(1001, 1, i);
                        }

                        FeedFragment feedFragment = FeedFragment.newInstance(token);

                        getFragmentManager().beginTransaction().replace(R.id.signin_root, feedFragment).commitNow();
                        MainActivity.imageButton.setVisibility(View.VISIBLE);
                    }
            }
            @Override
            public void onFailure(Call<Response> call, Throwable t) {

                call.cancel();
            }
        });
*/      usernameValue = mEmailView.getText().toString();
        passwordValue = mPasswordView.getText().toString();

        ApiClient.getClient().authCreate(usernameValue, passwordValue).enqueue(new Callback<Response>() {

            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {

                if (response.body().getStatus()) {

                    token = response.body().getAuth().getToken();

                    Intent i = new Intent().putExtra("token", token);

                    Fragment targetFragment = getTargetFragment();
                    if (targetFragment != null) {
                        targetFragment.onActivityResult(1001, 1, i);
                    }

                    FeedFragment feedFragment = FeedFragment.newInstance(token);

                    getFragmentManager().beginTransaction().replace(R.id.signin_root, feedFragment).commitNow();
                }

            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {

            }
        });
    }

    private void attemptLogin() {

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
        }

    }
    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
  //      return email.contains("@");
        return true;
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }
}