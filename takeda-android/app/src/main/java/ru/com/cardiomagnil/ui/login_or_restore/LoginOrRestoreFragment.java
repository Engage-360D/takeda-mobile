package ru.com.cardiomagnil.ui.login_or_restore;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import ru.com.cardiomagnil.app.R;
import ru.com.cardiomagnil.ca_model.user.Ca_User;
import ru.com.cardiomagnil.social.FbApi;
import ru.com.cardiomagnil.social.FbUser;
import ru.com.cardiomagnil.social.OkApi;
import ru.com.cardiomagnil.social.OkUser;
import ru.com.cardiomagnil.social.VkApi;
import ru.com.cardiomagnil.social.VkUser;
import ru.com.cardiomagnil.ui.base.BaseStartFragment;
import ru.com.cardiomagnil.ui.start.SignInWithSocialNetwork;
import ru.com.cardiomagnil.ui.start.StartActivity;
import ru.com.cardiomagnil.util.Tools;

public class LoginOrRestoreFragment extends BaseStartFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ca_fragment_start_login_or_restore, container, false);
        initLoginOrRestoreFragment(view);
        return view;
    }

    @Override
    public void initParent(Activity activity) {
        ProgressBar progressBarBottomOutsideStartWork = (ProgressBar) activity.findViewById(R.id.progressBarBottomOutsideStartWork);
        TextView textViewBottomOutsideAction = (TextView) activity.findViewById(R.id.textViewBottomOutsideAction);

        progressBarBottomOutsideStartWork.setMax(5);
        progressBarBottomOutsideStartWork.setProgress(2);
        textViewBottomOutsideAction.setText(activity.getString(R.string.two_minutes));
    }

    private void initLoginOrRestoreFragment(final View view) {
        initLogin(view);
        initRestore(view);
        initLoginRestoreSwitcher(view);
        initSocials(view);
    }

    private void initLogin(final View view) {
        final EditText editTextEmailLogin = (EditText) view.findViewById(R.id.editTextEmailLogin);
        final EditText editTextPassword = (EditText) view.findViewById(R.id.editTextPassword);
        final Button buttonEnter = (Button) view.findViewById(R.id.buttonEnter);
        final Button buttonRegister = (Button) view.findViewById(R.id.buttonRegister);

        editTextEmailLogin.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable arg0) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                buttonEnter.setEnabled(Tools.isValidEmail(s) && editTextPassword.getText().length() != 0);
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
                buttonEnter.setEnabled(s.length() != 0 && Tools.isValidEmail(editTextEmailLogin.getText()));
            }
        });

        buttonEnter.setEnabled(false);
        buttonEnter.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                tryAuthorization();
            }
        });

        buttonRegister.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                StartActivity startActivity = (StartActivity) v.getContext();
                startActivity.slideViewPager(true);
            }
        });
    }

    private void initRestore(final View view) {
        final EditText editTextEmailRestore = (EditText) view.findViewById(R.id.editTextEmailRestore);
        final Button buttonRestore = (Button) view.findViewById(R.id.buttonRestore);


        editTextEmailRestore.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable arg0) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                buttonRestore.setEnabled(Tools.isValidEmail(s));
            }
        });

        buttonRestore.setEnabled(false);
        buttonRestore.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                tryRestore();
            }
        });
    }

    private void initLoginRestoreSwitcher(final View view) {
        LinearLayout linearLayoutPerformLogin = (LinearLayout) view.findViewById(R.id.linearLayoutPerformLogin);
        LinearLayout linearLayoutPerformRestore = (LinearLayout) view.findViewById(R.id.linearLayoutPerformRestore);

        final LinearLayout linearLayoutLogin = (LinearLayout) view.findViewById(R.id.linearLayoutLogin);
        final LinearLayout linearLayoutRestore = (LinearLayout) view.findViewById(R.id.linearLayoutRestore);

        linearLayoutPerformLogin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayoutLogin.setVisibility(View.VISIBLE);
                linearLayoutRestore.setVisibility(View.GONE);
            }
        });

        linearLayoutPerformRestore.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayoutRestore.setVisibility(View.VISIBLE);
                linearLayoutLogin.setVisibility(View.GONE);
            }
        });
    }

    private void initSocials(final View view) {
        view.findViewById(R.id.imageViewFB).setOnClickListener(new SignInWithSocialNetwork(this, new FbApi(), new RegisterUserOnFinish(this, FbUser.class)));
        view.findViewById(R.id.imageViewVK).setOnClickListener(new SignInWithSocialNetwork(this, new VkApi(), new RegisterUserOnFinish(this, VkUser.class)));
        view.findViewById(R.id.imageViewOK).setOnClickListener(new SignInWithSocialNetwork(this, new OkApi(), new RegisterUserOnFinish(this, OkUser.class)));
    }

    private void tryAuthorization() {
        Ca_User user = pickAuthorizationFields();

        // FIXME: implement authorization

//        if (user.validate(true)) {
//            AppState.getInstatce().set User(user);
//
//            StartActivity startActivity = (StartActivity) getActivity();
//            startActivity.userAuthorization();
//        } else {
//            Toast.makeText(parentView.getContext(), parentView.getContext().getString(R.string.complete_all_fields), Toast.LENGTH_LONG).show();
//        }
    }

    // FIXME: remove getActivity
    private void tryRestore() {
        String email = pickRestoreFields();
        if (Tools.isValidEmail(email)) {
            StartActivity startActivity = (StartActivity) getActivity();
            startActivity.restorePassword(email);
        } else {
            Toast.makeText(this.getActivity(), this.getActivity().getString(R.string.complete_all_fields), Toast.LENGTH_LONG).show();
        }
    }

    // FIXME: remove getActivity
    private Ca_User pickAuthorizationFields() {
        Ca_User user = new Ca_User();

        try {
            EditText editTextEmailLogin = (EditText) getActivity().findViewById(R.id.editTextEmailLogin);
            EditText editTextPassword = (EditText) getActivity().findViewById(R.id.editTextPassword);

            user.setEmail(editTextEmailLogin.getText().toString());
            user.setPlainPassword(editTextPassword.getText().toString());
        } catch (Exception e) {
            // do nothing
        }

        return user;
    }

    // FIXME: remove getActivity
    private String pickRestoreFields() {
        EditText editTextEmailRestore = (EditText) getActivity().findViewById(R.id.editTextEmailRestore);
        return editTextEmailRestore.getText().toString();
    }

    // FIXME: remove getActivity
    private void initFields(ru.com.cardiomagnil.social.User user) {
        final EditText editTextEmail = (EditText) this.getActivity().findViewById(R.id.editTextEmailLogin);

        if (!user.getEmail().isEmpty()) {
            editTextEmail.setText(user.getEmail());
        }
    }

}