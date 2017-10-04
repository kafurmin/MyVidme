package com.example.kif.myvidme.screen.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
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
import android.widget.Toast;

import com.example.kif.myvidme.BuildConfig;
import com.example.kif.myvidme.R;
import com.example.kif.myvidme.model.Response;
import com.example.kif.myvidme.api.VidmeApi;
import com.example.kif.myvidme.screen.activity.MainActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;



public class SignInFragment extends Fragment {

    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;

    public String sharedPreferencesUsername;
    public String sharedPreferencesPassword;
    public String usernameValue;
    public String passwordValue;
    public String SharedUsername;
    public String SharedPassword;
    public Call<Response> call;
    public String token;
    public static ImageButton imageButton;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_signin, container, false);

        setHasOptionsMenu(false);
     /*   SharedPreferences auth = getActivity().getSharedPreferences("Pref",Context.MODE_PRIVATE);
        SharedUsername = auth.getString("username",null);
        SharedPassword = auth.getString("password",null);
*/
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
        mProgressView = rootView.findViewById(R.id.login_progress);


        return rootView;
    }

    public void SignIn() {

        attemptLogin();

        Retrofit retrofitAdapter = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BuildConfig.API_ENDPOINT)
                .build();

        final VidmeApi vidmeApi = retrofitAdapter.create(VidmeApi.class);


         usernameValue = mEmailView.getText().toString();
         passwordValue = mPasswordView.getText().toString();


   /*     SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Pref",Context.MODE_PRIVATE);
        sharedPreferencesUsername = sharedPreferences.getString("username",null);
        sharedPreferencesPassword = sharedPreferences.getString("password",null);
        Log.d("Shared preference", sharedPreferencesUsername + sharedPreferencesPassword);
*/
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
    }
    /*@Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
       Fragment parent = getParentFragment();
        if (parent != null) {
            parent.onActivityResult(1001, 1, new Intent().putExtra("token",token));
        }
    }
    */


/*
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Fragment target = getTargetFragment();
        if(target!=null){
            target.onActivityResult(getTargetRequestCode(),1, new Intent("token"));
        }
    }*/


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
            showProgress(true);
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

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
}