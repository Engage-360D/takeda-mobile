package ru.com.cardiomagnyl.application;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Toast;

import com.github.gorbin.asne.core.AccessToken;
import com.github.gorbin.asne.core.SocialNetwork;
import com.github.gorbin.asne.core.SocialNetworkManager;
import com.github.gorbin.asne.core.listener.OnLoginCompleteListener;
import com.github.gorbin.asne.core.listener.OnPostingCompleteListener;
import com.github.gorbin.asne.core.listener.OnRequestAccessTokenCompleteListener;
import com.github.gorbin.asne.core.listener.OnRequestDetailedSocialPersonCompleteListener;
import com.github.gorbin.asne.core.persons.SocialPerson;
import com.github.gorbin.asne.facebook.FacebookSocialNetwork;
import com.github.gorbin.asne.googleplus.GooglePlusSocialNetwork;
import com.github.gorbin.asne.odnoklassniki.OkSocialNetwork;
import com.github.gorbin.asne.vk.VkSocialNetwork;
import com.vk.sdk.VKScope;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ru.com.cardiomagnyl.app.R;
import ru.com.cardiomagnyl.model.social.SocialNetworks;
import ru.com.cardiomagnyl.ui.base.BaseFragmentActivityWrapper;
import ru.com.cardiomagnyl.widget.CustomDialogs;

public class SocialManager implements
        SocialNetworkManager.OnInitializationCompleteListener,
        OnLoginCompleteListener,
        OnRequestDetailedSocialPersonCompleteListener,
        OnRequestAccessTokenCompleteListener,
        OnPostingCompleteListener {
    private BaseFragmentActivityWrapper mFragmentActivityWrapper;

    private Object mSocialSynchroObject = new Object();
    private OnTokenReceived mOnTokenReceived;
    private SocialNetworkManager mSocialNetworkManager;

    private int mSelectedSocialNetwork;
    private SocialPerson mSocialPerson;
    private AccessToken mAccessToken;
    private Pair<String, String> mHeadAndLink = null;

    private List<View> mSocialsHolders;

    public static final String SOCIAL_NETWORK_TAG = "SocialIntegrationMain.SOCIAL_NETWORK_TAG";

    public SocialManager(BaseFragmentActivityWrapper fragmentActivityWrapper) {
        mFragmentActivityWrapper = fragmentActivityWrapper;
    }

    public interface OnTokenReceived {
        public void execute(int networkId, SocialPerson socialPerson, AccessToken accessToken);
    }

    public void initSocials(final Fragment fragment,
                            final View socialsHolder,
                            final OnTokenReceived onTokenReceived) {
        initSocials(fragment, Arrays.asList(socialsHolder), onTokenReceived);
    }

    public void initSocials(final Fragment fragment,
                            final List<View> socialsHolders,
                            final OnTokenReceived onTokenReceived) {
        synchronized (mSocialSynchroObject) {
            if (socialsHolders == null || socialsHolders.isEmpty()) return;

            mSocialsHolders = socialsHolders;
            mOnTokenReceived = onTokenReceived;
            mSocialPerson = null;

            for (View socialsHolder : socialsHolders) {
                View imageViewGP = socialsHolder.findViewById(R.id.imageViewGP);
                View imageViewVK = socialsHolder.findViewById(R.id.imageViewVK);
                View imageViewFB = socialsHolder.findViewById(R.id.imageViewFB);
                View imageViewOK = socialsHolder.findViewById(R.id.imageViewOK);

                imageViewGP.setOnClickListener(socialClick);
                imageViewVK.setOnClickListener(socialClick);
                imageViewFB.setOnClickListener(socialClick);
                imageViewOK.setOnClickListener(socialClick);
            }

            //Get Keys for initiate SocialNetworks
            String OK_APP_ID = mFragmentActivityWrapper.getFragmentActivity().getString(R.string.ok_app_id);
            String OK_PUBLIC_KEY = mFragmentActivityWrapper.getFragmentActivity().getString(R.string.ok_public_key);
            String OK_SECRET_KEY = mFragmentActivityWrapper.getFragmentActivity().getString(R.string.ok_secret_key);
            String VK_KEY = mFragmentActivityWrapper.getFragmentActivity().getString(R.string.vk_app_id);

            //Chose permissions
            ArrayList<String> fbScope = new ArrayList<String>(Arrays.asList("public_profile, basic_info, email, user_birthday"));
            String[] okScope = new String[]{"VALUABLE_ACCESS"};
            String[] vkScope = new String[]{VKScope.WALL};

            //Use manager to manage SocialNetworks
            mSocialNetworkManager = (SocialNetworkManager) fragment.getFragmentManager().findFragmentByTag(SOCIAL_NETWORK_TAG);

            //Check if manager exist
            if (mSocialNetworkManager == null) {
                mSocialNetworkManager = new SocialNetworkManager();

                //Init and add to manager GooglePlusSocialNetwork
                GooglePlusSocialNetwork gpNetwork = new GooglePlusSocialNetwork(fragment);
                mSocialNetworkManager.addSocialNetwork(gpNetwork);

                //Init and add to manager FacebookSocialNetwork
                FacebookSocialNetwork fbNetwork = new FacebookSocialNetwork(fragment, fbScope);
                mSocialNetworkManager.addSocialNetwork(fbNetwork);

                //Init and add to manager VkSocialNetwork
                VkSocialNetwork vkNetwork = new VkSocialNetwork(fragment, VK_KEY, vkScope);
                mSocialNetworkManager.addSocialNetwork(vkNetwork);

                //Init and add to manager OkSocialNetwork
                OkSocialNetwork okNetwork = new OkSocialNetwork(fragment, OK_APP_ID, OK_PUBLIC_KEY, OK_SECRET_KEY, okScope);
                mSocialNetworkManager.addSocialNetwork(okNetwork);

                //Initiate every network from mSocialNetworkManager
                mSocialNetworkManager.setOnInitializationCompleteListener(this);
                mFragmentActivityWrapper.getFragmentActivity().getSupportFragmentManager().beginTransaction().add(mSocialNetworkManager, SOCIAL_NETWORK_TAG).commit();

                mFragmentActivityWrapper.showProgressDialog();
            } else {
                //if manager exist - get and setup login only for initialized SocialNetworks
                if (!mSocialNetworkManager.getInitializedSocialNetworks().isEmpty()) {
                    initSocialButtons(socialsHolders, mSocialNetworkManager.getInitializedSocialNetworks());
                }
            }
        }
    }

    private void initSocialButtons(final List<View> socialsHolders, final List<SocialNetwork> socialNetworks) {
        if (socialsHolders == null || socialsHolders.isEmpty()) return;

        for (View socialsHolder : socialsHolders) {
            View imageViewGP = socialsHolder.findViewById(R.id.imageViewGP);
            View imageViewVK = socialsHolder.findViewById(R.id.imageViewVK);
            View imageViewFB = socialsHolder.findViewById(R.id.imageViewFB);
            View imageViewOK = socialsHolder.findViewById(R.id.imageViewOK);

            imageViewGP.setEnabled(false);
            imageViewVK.setEnabled(false);
            imageViewFB.setEnabled(false);
            imageViewOK.setEnabled(false);

            if (socialNetworks != null && !socialNetworks.isEmpty())
                for (SocialNetwork socialNetwork : socialNetworks) {
                    socialNetwork.setOnLoginCompleteListener(this);
                    switch (socialNetwork.getID()) {
                        case SocialNetworks.Facebook:
                            imageViewFB.setEnabled(true);
                            break;
                        case SocialNetworks.GooglePlus:
                            imageViewGP.setEnabled(true);
                            break;
                        case SocialNetworks.Vkontakte:
                            imageViewVK.setEnabled(true);
                            break;
                        case SocialNetworks.Odnoklassniki:
                            imageViewOK.setEnabled(true);
                            break;
                    }

                }
        }
    }

    private View.OnClickListener socialClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (view.getTag() != null && view.getTag() instanceof Pair) {
                mHeadAndLink = (Pair<String, String>) view.getTag();
            } else {
                mHeadAndLink = null;
//                // FIXME: remove after sests
//                mHeadAndLink = new Pair<>("Движение – это жизнь!", String.format(Url.SHARE_LINK, 1, 1));
            }

            switch (view.getId()) {
                case R.id.imageViewGP:
                    mSelectedSocialNetwork = GooglePlusSocialNetwork.ID;
                    break;
                case R.id.imageViewVK:
                    mSelectedSocialNetwork = VkSocialNetwork.ID;
                    break;
                case R.id.imageViewFB:
                    mSelectedSocialNetwork = FacebookSocialNetwork.ID;
                    break;
                case R.id.imageViewOK:
                    mSelectedSocialNetwork = OkSocialNetwork.ID;
                    break;
            }

            SocialNetwork socialNetwork = mSocialNetworkManager.getSocialNetwork(mSelectedSocialNetwork);
            if (!socialNetwork.isConnected()) {
                if (mSelectedSocialNetwork != 0) {
                    mFragmentActivityWrapper.showProgressDialog();
                    socialNetwork.requestLogin(SocialManager.this);
                } else {
                    Toast.makeText(mFragmentActivityWrapper.getFragmentActivity(), "Wrong networkId", Toast.LENGTH_LONG).show();
                }
            } else {
                mFragmentActivityWrapper.showProgressDialog();
                socialNetwork.requestDetailedCurrentPerson(SocialManager.this);
            }
        }
    };

    private void tryToShareLink(final int networkId) {
        Context context = mFragmentActivityWrapper.getFragmentActivity();
        String question = String.format(context.getString(R.string.share_link), SocialNetworks.getnameById(networkId));
        CustomDialogs.showConfirmationDialog(
                context,
                question,
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        shareMessage(networkId);
                    }
                }
        );
    }

    private void shareMessage(final int networkId) {
        mFragmentActivityWrapper.showProgressDialog();

        Bundle postParams = new Bundle();
        postParams.putString(SocialNetwork.BUNDLE_NAME, mHeadAndLink.first);
        postParams.putString(SocialNetwork.BUNDLE_LINK, mHeadAndLink.second);
        if (networkId == GooglePlusSocialNetwork.ID) {
            mSocialNetworkManager.getSocialNetwork(networkId).requestPostDialog(postParams, SocialManager.this);
        } else {
            mSocialNetworkManager.getSocialNetwork(networkId).requestPostLink(postParams, "", SocialManager.this);
        }
    }

    @Override
    public void onSocialNetworkManagerInitialized() {
        initSocialButtons(mSocialsHolders, mSocialNetworkManager.getInitializedSocialNetworks());
        mFragmentActivityWrapper.hideProgressDialog();
    }

    @Override
    public void onLoginSuccess(int networkId) {
        SocialNetwork socialNetwork = mSocialNetworkManager.getSocialNetwork(networkId);
        socialNetwork.requestDetailedCurrentPerson(SocialManager.this);
    }

    @Override
    public void onError(int networkId, String requestID, String errorMessage, Object data) {
        mFragmentActivityWrapper.hideProgressDialog();

        if (networkId != mSelectedSocialNetwork) return;

        mSocialNetworkManager.getSocialNetwork(networkId).logout();

        if (/*FB*/    TextUtils.isEmpty(errorMessage)
            /*FB*/ || errorMessage.equals("Operation canceled")
            /*OK*/ || errorMessage.equals("ОK Login cancaled!")
            /*GP*/ || errorMessage.equals("canceled")
            /*VK*/ || errorMessage.equals("VKError (Canceled)")) return;

        if (/**/      requestID.equals(SocialNetwork.REQUEST_LOGIN)
            /*GP*/ || errorMessage.equals("error: 4")) {
            CustomDialogs.showAlertDialog(mFragmentActivityWrapper.getFragmentActivity(), R.string.error_authorization);
        } else if (requestID.equals(SocialNetwork.REQUEST_GET_PERSON)
                || requestID.equals(SocialNetwork.REQUEST_GET_CURRENT_PERSON)
                || requestID.equals(SocialNetwork.REQUEST_GET_DETAIL_PERSON)) {
            CustomDialogs.showAlertDialog(mFragmentActivityWrapper.getFragmentActivity(), R.string.error_getting_user_data);
        } else {
            CustomDialogs.showAlertDialog(mFragmentActivityWrapper.getFragmentActivity(), R.string.error_occurred);
            if (!TextUtils.isEmpty(errorMessage)) {
                Log.d(CardiomagnylApplication.getInstance().getTag(), errorMessage);
            }
        }
    }

    @Override
    public void onRequestDetailedSocialPersonSuccess(int networkId, SocialPerson socialPerson) {
        if (networkId != mSelectedSocialNetwork) {
            mFragmentActivityWrapper.hideProgressDialog();
            return;
        }

        mSocialPerson = socialPerson;
        SocialNetwork socialNetwork = mSocialNetworkManager.getSocialNetwork(networkId);
        socialNetwork.requestAccessToken(this);
    }

    @Override
    public void onRequestAccessTokenComplete(int networkId, AccessToken accessToken) {
        mFragmentActivityWrapper.hideProgressDialog();
        synchronized (mSocialSynchroObject) {
            if (networkId == mSelectedSocialNetwork && mSocialPerson != null) {
                mAccessToken = accessToken;
                if (mHeadAndLink != null) {
                    tryToShareLink(networkId);
                } else {
                    if (mOnTokenReceived != null) {
                        mOnTokenReceived.execute(networkId, mSocialPerson, mAccessToken);
                    }
                }
            }
        }
    }

    @Override
    public void onPostSuccessfully(int networkId) {
        String message = String.format(mFragmentActivityWrapper.getFragmentActivity().getString(R.string.shared_successfully), SocialNetworks.getnameById(networkId));
        mFragmentActivityWrapper.hideProgressDialog();
        CustomDialogs.showAlertDialog(mFragmentActivityWrapper.getFragmentActivity(), message);
    }

}
