package ru.com.cardiomagnyl.ui.start;

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

import com.github.gorbin.asne.core.AccessToken;
import com.github.gorbin.asne.core.persons.SocialPerson;

import java.util.List;

import ru.com.cardiomagnyl.app.BuildConfig;
import ru.com.cardiomagnyl.app.R;
import ru.com.cardiomagnyl.application.SocialManager;
import ru.com.cardiomagnyl.model.common.Dummy;
import ru.com.cardiomagnyl.model.common.Response;
import ru.com.cardiomagnyl.model.social.Social;
import ru.com.cardiomagnyl.model.user.Email;
import ru.com.cardiomagnyl.model.user.LgnPwd;
import ru.com.cardiomagnyl.model.user.UserDao;
import ru.com.cardiomagnyl.ui.base.BaseStartFragment;
import ru.com.cardiomagnyl.util.CallbackOne;
import ru.com.cardiomagnyl.util.Tools;

public class LoginOrRestoreFragment extends BaseStartFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_start_login_or_restore, container, false);
        initFragment(view);
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
    public void initSocials(StartActivity startActivity) {
        SocialManager socialManager = new SocialManager(startActivity);

        socialManager.initSocials(this, getView(), new SocialManager.OnTokenReceived() {
            @Override
            public void execute(int networkId, SocialPerson socialPerson, AccessToken accessToken) {
                Social social = new Social();
                social.setUserId(socialPerson.id);
                social.setAccessToken(accessToken.token);

                startAuthorization(networkId, social);
            }
        });
    }

    @Override
    public void initFieldsFromSocial(int networkId, SocialPerson socialPerson) { /*does nothing*/ }

    private void initFragment(final View view) {
        initLoginFields(view);
        initRestoreFields(view);
        initLoginRestoreSwitcher(view);
        // TODO: remove after tests
        customizeIfDebug(view);
    }

    private void initLoginFields(final View view) {
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
                startAuthorization(view);
            }
        });

        buttonRegister.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Tools.hideKeyboard(getActivity());
                StartActivity startActivity = (StartActivity) getActivity();
                startActivity.slideViewPager(true);
            }
        });
    }

    private void initRestoreFields(final View view) {
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
                Email email = new Email();
                email.setEmail(pickRestoreFields(view));
                startRestoring(email);
            }
        });
    }

    private void initLoginRestoreSwitcher(final View view) {
        View textViewPerformLogin = view.findViewById(R.id.textViewPerformLogin);
        View textViewPerformRestore = view.findViewById(R.id.textViewPerformRestore);

        final LinearLayout linearLayoutLogin = (LinearLayout) view.findViewById(R.id.linearLayoutLogin);
        final LinearLayout linearLayoutRestore = (LinearLayout) view.findViewById(R.id.linearLayoutRestore);

        textViewPerformLogin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Tools.hideKeyboard(getActivity());
                linearLayoutLogin.setVisibility(View.VISIBLE);
                linearLayoutRestore.setVisibility(View.GONE);
            }
        });

        textViewPerformRestore.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Tools.hideKeyboard(getActivity());
                linearLayoutRestore.setVisibility(View.VISIBLE);
                linearLayoutLogin.setVisibility(View.GONE);
            }
        });
    }

    // TODO: remove after tests
    private void customizeIfDebug(final View view) {
        if (BuildConfig.DEBUG) {
            EditText editTextEmailLogin = (EditText) view.findViewById(R.id.editTextEmailLogin);
            EditText editTextPassword = (EditText) view.findViewById(R.id.editTextPassword);

            editTextEmailLogin.setText("y.andreyko+3@gmail.com");
            editTextPassword.setText("aaa");
        }
    }

    private void startAuthorization(final View view) {
        LgnPwd lgnPwd = pickAuthorizationFields(view);
        // response handled in handleRegAuth
        startAuthorization(lgnPwd);
    }

    private void startRestoring(Email email) {
        final StartActivity startActivity = (StartActivity) getActivity();
        startActivity.showProgressDialog();

        UserDao.resetPassword(
                email,
                new CallbackOne<List<Dummy>>() {
                    @Override
                    public void execute(List<Dummy> dummy) {
                        startActivity.hideProgressDialog();
                        Tools.showToast(getActivity(), R.string.restored_successfully, Toast.LENGTH_LONG);
                    }
                },
                new CallbackOne<Response>() {
                    @Override
                    public void execute(Response responseError) {
                        startActivity.hideProgressDialog();
                        Tools.showToast(getActivity(), R.string.error_occurred, Toast.LENGTH_LONG);
                    }
                }
        );
    }

    private LgnPwd pickAuthorizationFields(final View view) {
        LgnPwd lgnPwd = new LgnPwd();

        EditText editTextEmailLogin = (EditText) view.findViewById(R.id.editTextEmailLogin);
        EditText editTextPassword = (EditText) view.findViewById(R.id.editTextPassword);

        lgnPwd.setEmail(editTextEmailLogin.getText().toString());
        lgnPwd.setPlainPassword(editTextPassword.getText().toString());

        return lgnPwd;
    }

    private String pickRestoreFields(final View view) {
        EditText editTextEmailRestore = (EditText) view.findViewById(R.id.editTextEmailRestore);
        return editTextEmailRestore.getText().toString();
    }

}