package ru.com.cardiomagnyl.ui.start;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import ru.com.cardiomagnyl.app.R;
import ru.com.cardiomagnyl.ui.base.BaseStartFragment;
import ru.com.cardiomagnyl.util.ProfileHelper;
import ru.com.cardiomagnyl.util.Tools;

public class RegistrationFragment extends BaseStartFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ca_fragment_start_registration, container, false);
        initRegistrationFragment(view);
        return view;
    }

    @Override
    public void initParent(Activity activity) {
        TextView textViewHeader = (TextView) activity.findViewById(R.id.textViewHeader);

        ProgressBar progressBarBottomOutsideStartWork = (ProgressBar) activity.findViewById(R.id.progressBarBottomOutsideStartWork);
        TextView textViewBottomOutsideAction = (TextView) activity.findViewById(R.id.textViewBottomOutsideAction);

        textViewHeader.setText(activity.getString(R.string.header_registration));

        progressBarBottomOutsideStartWork.setMax(3);
        progressBarBottomOutsideStartWork.setProgress(2);
        textViewBottomOutsideAction.setText(activity.getString(R.string.two_minutes));
    }

    @Override
    public void initFieldsFromSocial(ru.com.cardiomagnyl.social.User user) {
        EditText editTextName = (EditText) getActivity().findViewById(R.id.editTextFirstName);
        EditText editTextRegEmail = (EditText) getActivity().findViewById(R.id.editTextRegEmail);
        TextView textViewBirthDate = (TextView) getActivity().findViewById(R.id.textViewBirthDateValue);

        if (!user.getFirstName().isEmpty()) {
            editTextName.setText(user.getFirstName());
        }

        if (!user.getEmail().isEmpty()) {
            editTextRegEmail.setText(user.getEmail());
        }

        if (!user.getBirthday().isEmpty()) {
            textViewBirthDate.setText(user.getBirthday());
            textViewBirthDate.setTag(Tools.calendarFromShort(user.getBirthday()));
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
                    startRegistration(ProfileHelper.pickRegistrationFields(parentView));
                } else {
                    Tools.showToast(getActivity(), R.string.complete_required_fields, Toast.LENGTH_SHORT);
                }
            }
        });
    }

}
