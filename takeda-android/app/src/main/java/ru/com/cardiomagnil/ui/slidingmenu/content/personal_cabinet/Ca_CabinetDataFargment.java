package ru.com.cardiomagnil.ui.slidingmenu.content.personal_cabinet;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.Toast;

import java.util.List;

import ru.com.cardiomagnil.app.R;
import ru.com.cardiomagnil.application.AppState;
import ru.com.cardiomagnil.model.common.Dummy;
import ru.com.cardiomagnil.model.common.Email;
import ru.com.cardiomagnil.model.common.Response;
import ru.com.cardiomagnil.model.token.Token;
import ru.com.cardiomagnil.model.user.User;
import ru.com.cardiomagnil.model.user.UserDao;
import ru.com.cardiomagnil.ui.base.BaseItemFragment;
import ru.com.cardiomagnil.ui.slidingmenu.menu.SlidingMenuActivity;
import ru.com.cardiomagnil.util.CallbackOne;
import ru.com.cardiomagnil.util.ProfileHelper;
import ru.com.cardiomagnil.util.Tools;

public class Ca_CabinetDataFargment extends BaseItemFragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ca_fragment_slidingmenu_cabinet_data, null);
        initFargment(view);
        return view;
    }

    @Override
    public void initTopBar(ViewGroup viewGroupTopBar) {
        initTopBarBellCabinet(viewGroupTopBar, true, false);
    }

    private void initFargment(View view) {
        unselectCurrentItem(view);
        initTabs(view);
        initButtons(view);
        ProfileHelper.initCabinetDataFargment(view, this);
    }

    private void unselectCurrentItem(final View view) {
        if (getActivity() != null && getActivity() instanceof SlidingMenuActivity) {
            SlidingMenuActivity slidingMenuActivity = (SlidingMenuActivity) getActivity();
            slidingMenuActivity.unselectCurrentItem();
        }
    }

    private void initTabs(final View view) {
        RadioButton radioButtonRecomendations = (RadioButton) view.findViewById(R.id.radioButtonRecomendations);
        RadioButton radioButtonSetting = (RadioButton) view.findViewById(R.id.radioButtonSetting);

        radioButtonRecomendations.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (getActivity() != null && getActivity() instanceof SlidingMenuActivity) {
                        Fragment fragment = new Ca_CabinetTestFargment();
                        SlidingMenuActivity slidingMenuActivity = (SlidingMenuActivity) getActivity();
                        slidingMenuActivity.replaceAllContent(fragment, false);
                    }
                }
            }
        });

        radioButtonSetting.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (getActivity() != null && getActivity() instanceof SlidingMenuActivity) {
                        Fragment fragment = new Ca_CabinetSettingsFargment();
                        SlidingMenuActivity slidingMenuActivity = (SlidingMenuActivity) getActivity();
                        slidingMenuActivity.replaceAllContent(fragment, false);
                    }
                }
            }
        });
    }

    private void initButtons(final View parentView) {
        View buttonGenerate = parentView.findViewById(R.id.buttonGenerate);
        View buttonSave = parentView.findViewById(R.id.buttonSave);

        buttonGenerate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Email email = new Email();
                email.setEmail(AppState.getInstatce().getUser().getEmail());
                startGeneration(email);
            }
        });


        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View paramView) {
                if (ProfileHelper.validateRegistrationFields(parentView)) {
                    User user = ProfileHelper.pickRegistrationFields(parentView);
                    Token token = AppState.getInstatce().getToken();
                    startUpdating(user, token);
                } else {
                    Tools.showToast(getActivity(), R.string.complete_required_fields, Toast.LENGTH_SHORT);
                }
            }
        });
    }

    private void startGeneration(Email email) {
        final SlidingMenuActivity slidingMenuActivity = (SlidingMenuActivity) getActivity();
        slidingMenuActivity.showProgressDialog();

        UserDao.resetPassword(
                email,
                new CallbackOne<List<Dummy>>() {
                    @Override
                    public void execute(List<Dummy> dummy) {
                        slidingMenuActivity.hideProgressDialog();
                        Tools.showToast(getActivity(), R.string.restored_successfully, Toast.LENGTH_LONG);
                    }
                },
                new CallbackOne<Response>() {
                    @Override
                    public void execute(Response responseError) {
                        slidingMenuActivity.hideProgressDialog();
                        Tools.showToast(getActivity(), R.string.error_occurred, Toast.LENGTH_LONG);
                    }
                }
        );
    }

    protected void startUpdating(final User user, Token token) {
        final SlidingMenuActivity slidingMenuActivity = (SlidingMenuActivity) getActivity();
        slidingMenuActivity.showProgressDialog();

        UserDao.update(
                user,
                token,
                new CallbackOne<User>() {
                    @Override
                    public void execute(User newUser) {
                        slidingMenuActivity.hideProgressDialog();
                        AppState.getInstatce().setUser(user);
                        Tools.showToast(getActivity(), R.string.saved_successfully, Toast.LENGTH_LONG);
                    }
                },
                new CallbackOne<Response>() {
                    @Override
                    public void execute(Response responseError) {
                        slidingMenuActivity.hideProgressDialog();
                        Tools.showToast(getActivity(), R.string.error_occurred, Toast.LENGTH_LONG);
                    }
                }
        );
    }
}
