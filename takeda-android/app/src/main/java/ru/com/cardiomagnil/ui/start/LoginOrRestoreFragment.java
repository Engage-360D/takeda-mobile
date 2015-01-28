package ru.com.cardiomagnil.ui.start;

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

import ru.com.cardiomagnil.app.BuildConfig;
import ru.com.cardiomagnil.app.R;
import ru.com.cardiomagnil.ca_api.Status;
import ru.com.cardiomagnil.ca_model.common.Ca_Error;
import ru.com.cardiomagnil.ca_model.common.Ca_Response;
import ru.com.cardiomagnil.ca_model.user.Ca_User;
import ru.com.cardiomagnil.ca_model.user.Ca_UserDao;
import ru.com.cardiomagnil.ca_model.user.Ca_UserLgnPwd;
import ru.com.cardiomagnil.social.FbApi;
import ru.com.cardiomagnil.social.FbUser;
import ru.com.cardiomagnil.social.OkApi;
import ru.com.cardiomagnil.social.OkUser;
import ru.com.cardiomagnil.social.VkApi;
import ru.com.cardiomagnil.social.VkUser;
import ru.com.cardiomagnil.ui.base.BaseStartFragment;
import ru.com.cardiomagnil.util.CallbackOne;
import ru.com.cardiomagnil.util.Tools;
import ru.com.cardiomagnil.util.Utils;

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

    @Override
    public void initFieldsFromSocial(ru.com.cardiomagnil.social.User user) {
        final EditText editTextEmail = (EditText) getActivity().findViewById(R.id.editTextEmailLogin);

        if (!user.getEmail().isEmpty()) {
            editTextEmail.setText(user.getEmail());
        }
    }

    public void handleResetPassword(Ca_User user, Ca_Response responseError) {
        StartActivity startActivity = (StartActivity) getActivity();
        startActivity.hideProgressDialog();

        responseError = (responseError == null || responseError.getError() == null) ?
                new Ca_Response.Builder(new Ca_Error()).create() :
                responseError;

        if (user == null) {
            switch (responseError.getError().getCode()){
                case Status.NO_DATA_ERROR:
                    Toast.makeText(getActivity(), getActivity().getString(R.string.error_user_not_found), Toast.LENGTH_LONG).show();
                    break;
                default:
                    Toast.makeText(getActivity(), getActivity().getString(R.string.error_occurred), Toast.LENGTH_LONG).show();
            }
        }
    }

    private void initLoginOrRestoreFragment(final View view) {
        initLogin(view);
        initRestore(view);
        initLoginRestoreSwitcher(view);
        initSocials(view);
        // TODO: remove after tests
        customizeIfDebug(view);
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
                startAuthorization();
            }
        });

        buttonRegister.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.hideKeyboard(getActivity());
                StartActivity startActivity = (StartActivity) getActivity();
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
                startRestore();
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
                Utils.hideKeyboard(getActivity());
                linearLayoutLogin.setVisibility(View.VISIBLE);
                linearLayoutRestore.setVisibility(View.GONE);
            }
        });

        linearLayoutPerformRestore.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.hideKeyboard(getActivity());
                linearLayoutRestore.setVisibility(View.VISIBLE);
                linearLayoutLogin.setVisibility(View.GONE);
            }
        });
    }

    private void initSocials(final View view) {
        view.findViewById(R.id.imageViewFB).setOnClickListener(new SignInWithSocialNetwork(this, new FbApi(), new CustomAuthorizationListener(this, FbUser.class)));
        view.findViewById(R.id.imageViewVK).setOnClickListener(new SignInWithSocialNetwork(this, new VkApi(), new CustomAuthorizationListener(this, VkUser.class)));
        view.findViewById(R.id.imageViewOK).setOnClickListener(new SignInWithSocialNetwork(this, new OkApi(), new CustomAuthorizationListener(this, OkUser.class)));
    }

    // TODO: remove after tests
    private void customizeIfDebug(final View view) {
        if (BuildConfig.DEBUG) {
            EditText editTextEmailLogin = (EditText) view.findViewById(R.id.editTextEmailLogin);
            EditText editTextPassword = (EditText) view.findViewById(R.id.editTextPassword);

            editTextEmailLogin.setText("y.andreyko@gmail.com");
            editTextPassword.setText("aaa");
        }
    }

    private void startAuthorization() {
        Ca_UserLgnPwd userLgnPwd = pickAuthorizationFields();
        // response handled in handleRegAuth
        startAuthorization(userLgnPwd);
    }

    private void startRestore() {
        StartActivity startActivity = (StartActivity) getActivity();
        startActivity.showProgressDialog();

        String email = pickRestoreFields();
        restorePassword(email);
    }

    // FIXME
    private void restorePassword(String email) {
        Ca_UserDao.resetPassword(
                email,
                new CallbackOne<Ca_User>() {
                    @Override
                    public void execute(Ca_User newUser) {
                        handleResetPassword(newUser, null);
                    }
                },
                new CallbackOne<Ca_Response>() {
                    @Override
                    public void execute(Ca_Response responseError) {
                        handleResetPassword(null, responseError);
                    }
                }
        );
    }

    private Ca_UserLgnPwd pickAuthorizationFields() {
        Ca_UserLgnPwd userLgnPwd = new Ca_UserLgnPwd();

        try {
            EditText editTextEmailLogin = (EditText) getActivity().findViewById(R.id.editTextEmailLogin);
            EditText editTextPassword = (EditText) getActivity().findViewById(R.id.editTextPassword);

            userLgnPwd.setEmail(editTextEmailLogin.getText().toString());
            userLgnPwd.setPlainPassword(editTextPassword.getText().toString());
        } catch (Exception e) {
            // do nothing
        }

        return userLgnPwd;
    }

    private String pickRestoreFields() {
        EditText editTextEmailRestore = (EditText) getActivity().findViewById(R.id.editTextEmailRestore);
        return editTextEmailRestore.getText().toString();
    }

}