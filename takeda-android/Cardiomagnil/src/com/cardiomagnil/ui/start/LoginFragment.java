package com.cardiomagnil.ui.start;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cardiomagnil.R;
import com.cardiomagnil.application.AppConfig;
import com.cardiomagnil.application.AppState;
import com.cardiomagnil.model.Authorization;

public class LoginFragment extends CustomFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        initLoginFragment(view);

        return view;
    }

    @Override
    public void initParent() {
        ImageView imageViewBottomInsideLeft = (ImageView) getActivity().findViewById(R.id.imageViewBottomInsideLeft);
        TextView textViewBottomInsideAction = (TextView) getActivity().findViewById(R.id.textViewBottomInsideAction);
        ImageView imageViewBottomInsideRight = (ImageView) getActivity().findViewById(R.id.imageViewBottomInsideRight);

        ProgressBar progressBarBottomOutsideStartWork = (ProgressBar) getActivity().findViewById(R.id.progressBarBottomOutsideStartWork);
        TextView textViewBottomOutsideAction = (TextView) getActivity().findViewById(R.id.textViewBottomOutsideAction);

        imageViewBottomInsideLeft.setVisibility(View.INVISIBLE);
        textViewBottomInsideAction.setText(getActivity().getString(R.string.bottom_registration));
        imageViewBottomInsideRight.setVisibility(View.VISIBLE);

        progressBarBottomOutsideStartWork.setMax(5);
        progressBarBottomOutsideStartWork.setProgress(2);
        textViewBottomOutsideAction.setText(getActivity().getString(R.string.two_minutes));
    }

    private void initLoginFragment(View view) {
        final EditText editTextEmail = (EditText) view.findViewById(R.id.editTextEmail);
        final EditText editTextPassword = (EditText) view.findViewById(R.id.editTextPassword);
        final Button buttonEnter = (Button) view.findViewById(R.id.buttonEnter);

        editTextEmail.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable arg0) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                buttonEnter.setEnabled(s.length() != 0 && editTextPassword.getText().length() != 0);
            }

        });

        editTextPassword.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable arg0) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                buttonEnter.setEnabled(s.length() != 0 && editTextEmail.getText().length() != 0);
            }

        });

        buttonEnter.setEnabled(false);
        buttonEnter.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                tryAuthorization(editTextEmail.getText().toString(), editTextPassword.getText().toString());
            }
        });
    }

    private void tryAuthorization(String username, String password) {
        Authorization currentAuthorization = new Authorization();
        currentAuthorization.setClientId(AppConfig.CLIENT_ID);
        currentAuthorization.setClientSecret(AppConfig.CLIENT_SECRET);
        currentAuthorization.setGrantType(AppConfig.GRANT_TYPE);
        currentAuthorization.setUsername(username);
        currentAuthorization.setPassword(password);

        AppState.getInstatce().setAuthorization(currentAuthorization);

        StartActivity startActivity = (StartActivity)getActivity();
        startActivity.userAuthorization();
    }
}