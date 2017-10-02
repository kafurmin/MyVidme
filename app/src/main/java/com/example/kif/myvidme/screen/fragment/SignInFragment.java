package com.example.kif.myvidme.screen.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.kif.myvidme.BuildConfig;
import com.example.kif.myvidme.R;
import com.example.kif.myvidme.model.Response;
import com.example.kif.myvidme.api.VidmeApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;



public class SignInFragment extends Fragment {
    public EditText username;
    public EditText password;
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

        username =  rootView.findViewById(R.id.user_name_field);
        password =  rootView.findViewById(R.id.password_field);
        LinearLayout linearLayout = (LinearLayout)rootView.findViewById(R.id.my_button);
        SharedPreferences auth = getActivity().getSharedPreferences("Pref",Context.MODE_PRIVATE);
        inSharedPreferenceUsername = auth.getString("username",null);
        inSharedPreferencePassword = auth.getString("password",null);

        if(inSharedPreferenceUsername!=null & inSharedPreferencePassword!=null){
            SignIn();
        }
        else {
            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SignIn();
                }
            });
        }
        return rootView;
    }

    public void SignIn() {
        Retrofit retrofitAdapter = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BuildConfig.API_ENDPOINT)
                .build();

        final VidmeApi vidmeApi = retrofitAdapter.create(VidmeApi.class);

         usernameValue = username.getText().toString();
         passwordValue = password.getText().toString();

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
}