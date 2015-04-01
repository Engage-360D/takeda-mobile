package ru.com.cardiomagnyl.ui.slidingmenu.content.personal_cabinet;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.Toast;

import com.github.gorbin.asne.core.AccessToken;
import com.github.gorbin.asne.core.persons.SocialPerson;

import java.util.List;

import ru.com.cardiomagnyl.app.R;
import ru.com.cardiomagnyl.application.AppState;
import ru.com.cardiomagnyl.application.SocialManager;
import ru.com.cardiomagnyl.model.common.Dummy;
import ru.com.cardiomagnyl.model.common.Response;
import ru.com.cardiomagnyl.model.social.Social;
import ru.com.cardiomagnyl.model.social.SocialNetworks;
import ru.com.cardiomagnyl.model.token.Token;
import ru.com.cardiomagnyl.model.user.Email;
import ru.com.cardiomagnyl.model.user.User;
import ru.com.cardiomagnyl.model.user.UserDao;
import ru.com.cardiomagnyl.ui.base.BaseItemFragment;
import ru.com.cardiomagnyl.ui.slidingmenu.menu.SlidingMenuActivity;
import ru.com.cardiomagnyl.util.CallbackOne;
import ru.com.cardiomagnyl.util.ProfileHelper;
import ru.com.cardiomagnyl.util.Tools;

public class CabinetDataFragment extends BaseItemFragment {
    private int mNetworkId;
    private Social mSocial;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cabinet_data, null);
        initFragment(view);
        return view;
    }

    @Override
    public void initTopBar(ViewGroup viewGroupTopBar) {
        initTopBarBellCabinet(viewGroupTopBar, true, false);
    }

    private void initFragment(View fragmentView) {
        unselectCurrentItem(fragmentView);
        initTabs(fragmentView);
        initButtons(fragmentView);
        initSocials(fragmentView);
        ProfileHelper.initCabinetDataFargment(fragmentView, this);
    }

    private void initSocials(final View fragmentView) {
        initSocialsHelper(fragmentView);

        final View imageViewGP = fragmentView.findViewById(R.id.imageViewGP);
        final View imageViewVK = fragmentView.findViewById(R.id.imageViewVK);
        final View imageViewFB = fragmentView.findViewById(R.id.imageViewFB);
        final View imageViewOK = fragmentView.findViewById(R.id.imageViewOK);

        SocialManager socialManager = new SocialManager((SlidingMenuActivity) getActivity());

        socialManager.initSocials(this, fragmentView, new SocialManager.OnTokenReceived() {
            @Override
            public void execute(int networkId, SocialPerson socialPerson, AccessToken accessToken) {
                mNetworkId = networkId;
                mSocial = new Social();
                mSocial.setUserId(socialPerson.id);
                mSocial.setAccessToken(accessToken.token);

                imageViewGP.setSelected(false);
                imageViewFB.setSelected(false);
                imageViewVK.setSelected(false);
                imageViewOK.setSelected(false);

                switch (networkId) {
                    case SocialNetworks.GooglePlus:
                        imageViewGP.setSelected(true);
                        break;
                    case SocialNetworks.Facebook:
                        imageViewFB.setSelected(true);
                        break;
                    case SocialNetworks.Vkontakte:
                        imageViewVK.setSelected(true);
                        break;
                    case SocialNetworks.Odnoklassniki:
                        imageViewOK.setSelected(true);
                        break;
                }
            }
        }, null);
    }

    private void initSocialsHelper(final View fragmentView) {
        final View imageViewGP = fragmentView.findViewById(R.id.imageViewGP);
        final View imageViewVK = fragmentView.findViewById(R.id.imageViewVK);
        final View imageViewFB = fragmentView.findViewById(R.id.imageViewFB);
        final View imageViewOK = fragmentView.findViewById(R.id.imageViewOK);

        final View layoutUsedSocial = fragmentView.findViewById(R.id.layoutUsedSocial);

        final View imageViewUsedGP = fragmentView.findViewById(R.id.imageViewUsedGP);
        final View imageViewUsedVK = fragmentView.findViewById(R.id.imageViewUsedVK);
        final View imageViewUsedFB = fragmentView.findViewById(R.id.imageViewUsedFB);
        final View imageViewUsedOK = fragmentView.findViewById(R.id.imageViewUsedOK);

        User currentUser = AppState.getInsnatce().getUser();

        boolean gp = !TextUtils.isEmpty(currentUser.getGoogleId()) || !TextUtils.isEmpty(currentUser.getGoogleToken());
        boolean fb = !TextUtils.isEmpty(currentUser.getFacebookId()) || !TextUtils.isEmpty(currentUser.getFacebookToken());
        boolean vk = !TextUtils.isEmpty(currentUser.getVkontakteId()) || !TextUtils.isEmpty(currentUser.getVkontakteToken());
        boolean ok = !TextUtils.isEmpty(currentUser.getOdnoklassnikiId()) || !TextUtils.isEmpty(currentUser.getOdnoklassnikiToken());

        imageViewGP.setEnabled(!gp);
        imageViewFB.setEnabled(!fb);
        imageViewVK.setEnabled(!vk);
        imageViewOK.setEnabled(!ok);

        layoutUsedSocial.setVisibility((gp || fb || vk || ok) ? View.VISIBLE : View.GONE);

        imageViewUsedGP.setVisibility(gp ? View.VISIBLE : View.GONE);
        imageViewUsedFB.setVisibility(fb ? View.VISIBLE : View.GONE);
        imageViewUsedVK.setVisibility(vk ? View.VISIBLE : View.GONE);
        imageViewUsedOK.setVisibility(ok ? View.VISIBLE : View.GONE);
    }

    private void unselectCurrentItem(final View view) {
        if (getActivity() != null && getActivity() instanceof SlidingMenuActivity) {
            SlidingMenuActivity slidingMenuActivity = (SlidingMenuActivity) getActivity();
            slidingMenuActivity.unselectCurrentItem();
        }
    }

    private void initTabs(final View view) {
        RadioButton radioButtonSetting = (RadioButton) view.findViewById(R.id.radioButtonSetting);

        final SlidingMenuActivity slidingMenuActivity = (SlidingMenuActivity) getActivity();

        radioButtonSetting.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (getActivity() != null && getActivity() instanceof SlidingMenuActivity) {
                        CabinetSettingsFragment cabinetSettingsFragment = new CabinetSettingsFragment();
                        slidingMenuActivity.replaceAllContent(cabinetSettingsFragment, false);
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
                generatePassword(email);
            }
        });

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View paramView) {
                if (ProfileHelper.validateRegistrationFields(parentView)) {
                    User user = ProfileHelper.pickRegistrationFields(parentView, mNetworkId, mSocial);
                    Token token = AppState.getInsnatce().getToken();
                    updateUser(parentView, user, token);
                } else {
                    Tools.showToast(getActivity(), R.string.complete_required_fields, Toast.LENGTH_SHORT);
                }
            }
        });
    }

    private void generatePassword(Email email) {
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

    protected void updateUser(final View parentView, final User user, Token token) {
        final SlidingMenuActivity slidingMenuActivity = (SlidingMenuActivity) getActivity();
        slidingMenuActivity.showProgressDialog();

        UserDao.update(
                user,
                token,
                new CallbackOne<User>() {
                    @Override
                    public void execute(User newUser) {
                        slidingMenuActivity.hideProgressDialog();
                        AppState.getInsnatce().setUser(newUser);
                        initSocialsHelper(parentView);
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
