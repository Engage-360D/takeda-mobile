package ru.com.cardiomagnyl.ui.slidingmenu.content.personal_cabinet;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioGroup;

import ru.com.cardiomagnyl.app.R;
import ru.com.cardiomagnyl.ui.base.BaseItemFragment;

public class CabinetIncidentFragment extends BaseItemFragment {
    private final View.OnClickListener mOnDoneClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            onIncidentDone(view);
        }
    };

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cabinet_incident, null);
        return view;
    }

    @Override
    public void initTopBar(ViewGroup viewGroupTopBar) {
        initTopBarDone(viewGroupTopBar, null);
    }

    private void initFragment(final View view) {
        initIncidentFields(view);
    }


    private void initIncidentFields(final View view) {
        final EditText editTextComment = (EditText) view.findViewById(R.id.editTextComment);
        final RadioGroup radioGroupIncident = (RadioGroup) view.findViewById(R.id.radioGroupIncident);

        editTextComment.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable arg0) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (checkIncidentFields(editTextComment, radioGroupIncident)) {

                } else {

                }
            }
        });
    }

    private boolean checkIncidentFields(EditText editTextComment, RadioGroup radioGroupIncident) {
        return editTextComment.getText().length() != 0 && radioGroupIncident.isSelected();
    }

    private void onIncidentDone(View view) {

    }

}
