package ru.com.cardiomagnyl.ui.start;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.gorbin.asne.core.AccessToken;
import com.github.gorbin.asne.core.persons.SocialPerson;
import com.github.gorbin.asne.facebook.FacebookPerson;
import com.github.gorbin.asne.googleplus.GooglePlusPerson;
import com.github.gorbin.asne.odnoklassniki.OkPerson;
import com.github.gorbin.asne.vk.VKPerson;

import ru.com.cardiomagnyl.app.R;
import ru.com.cardiomagnyl.application.SocialManager;
import ru.com.cardiomagnyl.model.social.Social;
import ru.com.cardiomagnyl.model.social.SocialNetworks;
import ru.com.cardiomagnyl.ui.base.BaseStartFragment;
import ru.com.cardiomagnyl.util.ProfileHelper;
import ru.com.cardiomagnyl.util.Tools;

public class RegistrationFragment extends BaseStartFragment {
    private int mNetworkId;
    private Social mSocial;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_start_registration, container, false);
        initRegistrationFragment(view);
        return view;
    }

    @Override
    public void initParent(Activity activity) {
        ProgressBar progressBarBottomOutsideStartWork = (ProgressBar) activity.findViewById(R.id.progressBarBottomOutsideStartWork);
        TextView textViewBottomOutsideAction = (TextView) activity.findViewById(R.id.textViewBottomOutsideAction);

        progressBarBottomOutsideStartWork.setMax(3);
        progressBarBottomOutsideStartWork.setProgress(2);
        textViewBottomOutsideAction.setText(activity.getString(R.string.two_minutes));
    }

    @Override
    public void initSocials(StartActivity startActivity) {
        final View imageViewGP = getView().findViewById(R.id.imageViewGP);
        final View imageViewFB = getView().findViewById(R.id.imageViewFB);
        final View imageViewVK = getView().findViewById(R.id.imageViewVK);
        final View imageViewOK = getView().findViewById(R.id.imageViewOK);

        SocialManager socialManager = new SocialManager(startActivity);

        socialManager.initSocials(this, getView(), new SocialManager.OnTokenReceived() {
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

                initFieldsFromSocial(networkId, socialPerson);
            }
        }, null);
    }

    @Override
    public void initFieldsFromSocial(int networkId, SocialPerson socialPerson) {
        EditText editTextFirstName = (EditText) getActivity().findViewById(R.id.editTextFirstName);
        EditText editTextLastName = (EditText) getActivity().findViewById(R.id.editTextLastName);
        TextView textViewBirthDate = (TextView) getActivity().findViewById(R.id.textViewBirthDateValue);
        EditText editTextRegEmail = (EditText) getActivity().findViewById(R.id.editTextRegEmail);

        String firstName = "";
        String lastName = "";
        String birthDate = "";
        String email = socialPerson.email;

        if (socialPerson instanceof FacebookPerson) {
            FacebookPerson facebookPerson = (FacebookPerson) socialPerson;
            firstName = facebookPerson.firstName;
            lastName = facebookPerson.lastName;
            birthDate = Tools.fbDateToShortDate(facebookPerson.birthday); /*MM/DD/YYYY -> yyyy-MM-dd*/
        } else if (socialPerson instanceof GooglePlusPerson) {
            GooglePlusPerson googlePlusPerson = (GooglePlusPerson) socialPerson;
            firstName = googlePlusPerson.name;
            lastName = "";
            birthDate = googlePlusPerson.birthday; /*YYYY-MM-DD*/
        } else if (socialPerson instanceof VKPerson) {
            VKPerson vkPerson = (VKPerson) socialPerson;
            firstName = vkPerson.name;
            lastName = "";
            birthDate = Tools.vkDateToShortDate(vkPerson.birthday); /*dd.MM.yyyy -> yyyy-MM-dd*/
        } else if (socialPerson instanceof OkPerson) {
            OkPerson okPerson = (OkPerson) socialPerson;
            firstName = okPerson.firstName;
            lastName = okPerson.lastName;
            birthDate = okPerson.birthday; /*YYYY-MM-DD*/
        }

        if (!TextUtils.isEmpty(firstName)) {
            editTextFirstName.setText(firstName);
        }

        if (!TextUtils.isEmpty(lastName)) {
            editTextLastName.setText(lastName);
        }

        if (!TextUtils.isEmpty(birthDate)) {
            textViewBirthDate.setText(birthDate);
            textViewBirthDate.setTag(Tools.calendarFromShortDate(birthDate));
        }

        if (!TextUtils.isEmpty(email)) {
            editTextRegEmail.setText(email);
        }

    }

    private void initRegistrationFragment(final View view) {
        ProfileHelper.initRegistrationFragment(view, this);
        initSignUpButton(view);
    }

    private void initSignUpButton(final View parentView) {
        Button buttonSignUp = (Button) parentView.findViewById(R.id.buttonSignUp);
        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View paramView) {
                if (ProfileHelper.validateRegistrationFields(parentView)) {
                    // response handled in handleRegAuth
                    startRegistration(ProfileHelper.pickRegistrationFields(parentView, mNetworkId, mSocial));
                } else {
                    Tools.showToast(getActivity(), R.string.complete_required_fields, Toast.LENGTH_SHORT);
                }
            }
        });
    }

}
