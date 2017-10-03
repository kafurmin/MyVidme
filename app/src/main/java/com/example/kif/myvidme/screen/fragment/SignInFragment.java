package com.example.kif.myvidme.screen.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Loader;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.kif.myvidme.BuildConfig;
import com.example.kif.myvidme.R;
import com.example.kif.myvidme.model.Response;
import com.example.kif.myvidme.api.VidmeApi;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.Manifest.permission.READ_CONTACTS;


public class SignInFragment extends Fragment {
    private static final int REQUEST_READ_CONTACTS = 0;

    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;

    public String sharedPreferencesUsername;
    public String sharedPreferencesPassword;
    public String usernameValue;
    public String passwordValue;
    public String inSharedPreferenceUsername;
    public String inSharedPreferencePassword;
    public Call<Response> call;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_signin, container, false);


        SharedPreferences auth = getActivity().getSharedPreferences("Pref",Context.MODE_PRIVATE);
        inSharedPreferenceUsername = auth.getString("username",null);
        inSharedPreferencePassword = auth.getString("password",null);

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


        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Pref",Context.MODE_PRIVATE);
        sharedPreferencesUsername = sharedPreferences.getString("username",null);
        sharedPreferencesPassword = sharedPreferences.getString("password",null);
        Log.d("Shared preference", sharedPreferencesUsername + sharedPreferencesPassword);

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
                if(response.body()==null){
                    buildDialog(getActivity()).show();
                    SharedPreferences failConnectionValues = getActivity().getSharedPreferences("Pref",Context.MODE_PRIVATE);
                    SharedPreferences.Editor edit = failConnectionValues.edit();
                    edit.putString("username",null);
                    edit.putString("password",null);
                    edit.commit();
                }
                else {
                    if (response.body().getStatus()) {
                        SharedPreferences sharedPreferences1 = getActivity().getSharedPreferences("Pref",Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences1.edit();
                        editor.putString("token", response.body().getAuth().getToken());
                        editor.putString("username", usernameValue);
                        editor.putString("password", passwordValue);
                        editor.commit();

                        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                        FeedFragment feedFragment = new FeedFragment();
                        fragmentTransaction.replace(R.id.signin_root,feedFragment);
                        fragmentTransaction.commit();
/*
                        Intent feed_intent = new Intent(getActivity(), Feed.class);
                        startActivity(feed_intent);*/
                    }
                }
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {

                call.cancel();
                buildDialog(getActivity());
            }
        });
    }

    public AlertDialog.Builder buildDialog(Context c) {

        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setTitle("Invalid password ");
        builder.setMessage("The password you entered was not valid");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });

        return builder;
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
/*

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }


private interface ProfileQuery {
    String[] PROJECTION = {
            ContactsContract.CommonDataKinds.Email.ADDRESS,
            ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
    };

    int ADDRESS = 0;
    int IS_PRIMARY = 1;
}

*/
/**
 * Represents an asynchronous login/registration task used to authenticate
 * the user.
 *//*

public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

    private final String mEmail;
    private final String mPassword;

    UserLoginTask(String email, String password) {
        mEmail = email;
        mPassword = password;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        // TODO: attempt authentication against a network service.

        try {
            // Simulate network access.
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            return false;
        }

        for (String credential : DUMMY_CREDENTIALS) {
            String[] pieces = credential.split(":");
            if (pieces[0].equals(mEmail)) {
                // Account exists, return true if the password matches.
                return pieces[1].equals(mPassword);
            }
        }

        // TODO: register the new account here.
        return true;
    }

    @Override
    protected void onPostExecute(final Boolean success) {
        mAuthTask = null;
        showProgress(false);

        if (success) {
            finish();
        } else {
            mPasswordView.setError(getString(R.string.error_incorrect_password));
            mPasswordView.requestFocus();
        }
    }

    @Override
    protected void onCancelled() {
        mAuthTask = null;
        showProgress(false);
    }
}
}



*/
