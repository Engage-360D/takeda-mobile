package ru.com.cardiomagnyl.ui.slidingmenu.content.personal_cabinet;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.Toast;

import java.util.List;

import ru.com.cardiomagnyl.app.R;
import ru.com.cardiomagnyl.application.AppState;
import ru.com.cardiomagnyl.model.common.Dummy;
import ru.com.cardiomagnyl.model.common.Email;
import ru.com.cardiomagnyl.model.common.Response;
import ru.com.cardiomagnyl.model.token.Token;
import ru.com.cardiomagnyl.model.user.User;
import ru.com.cardiomagnyl.model.user.UserDao;
import ru.com.cardiomagnyl.ui.base.BaseItemFragment;
import ru.com.cardiomagnyl.ui.slidingmenu.menu.SlidingMenuActivity;
import ru.com.cardiomagnyl.util.CallbackOne;
import ru.com.cardiomagnyl.util.ProfileHelper;
import ru.com.cardiomagnyl.util.Tools;

public class CabinetDataFragment extends BaseItemFragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_slidingmenu_cabinet_data, null);
        initFragment(view);
        return view;
    }

    @Override
    public void initTopBar(ViewGroup viewGroupTopBar) {
        initTopBarBellCabinet(viewGroupTopBar, true, false);
    }

    private void initFragment(View view) {
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
        RadioButton radioButtonRecommendations = (RadioButton) view.findViewById(R.id.radioButtonRecomendations);
        RadioButton radioButtonSetting = (RadioButton) view.findViewById(R.id.radioButtonSetting);

        radioButtonRecommendations.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (getActivity() != null && getActivity() instanceof SlidingMenuActivity) {
                        Fragment fragment = new CabinetTestFragment();
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
                        Fragment fragment = new CabinetSettingsFragment();
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
                email.setEmail(AppState.getInsnatce().getUser().getEmail());
                startGeneration(email);
            }
        });


        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View paramView) {
                if (ProfileHelper.validateRegistrationFields(parentView)) {
                    User user = ProfileHelper.pickRegistrationFields(parentView);
                    Token token = AppState.getInsnatce().getToken();
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
                        AppState.getInsnatce().setUser(user);
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
