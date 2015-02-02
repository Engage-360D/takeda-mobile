package ru.com.cardiomagnil.ui.base;

import android.view.View;
import android.widget.TextView;

import ru.com.cardiomagnil.app.R;
import ru.com.cardiomagnil.ui.base.BaseItemFragment;

public abstract class BaseRiskAnalysis extends BaseItemFragment {
    protected void initTabs(View view, int tab) {
        TextView textViewStepOne = (TextView) view.findViewById(R.id.textViewStepOne);
        TextView textViewStepTwo = (TextView) view.findViewById(R.id.textViewStepTwo);
        TextView textViewStepThree = (TextView) view.findViewById(R.id.textViewStepThree);

        switch (tab) {
            case 0:
                initTab(textViewStepOne, false);
                initTab(textViewStepTwo, false);
                initTab(textViewStepThree, false);
                break;
            case 1:
                initTab(textViewStepOne, true);
                initTab(textViewStepTwo, false);
                initTab(textViewStepThree, false);
                break;
            case 2:
                initTab(textViewStepOne, true);
                initTab(textViewStepTwo, true);
                initTab(textViewStepThree, false);
                break;
        }
    }

    private void initTab(TextView textView, boolean isPassed) {
        if (isPassed) {
            textView.setTextColor(getResources().getColor(R.color.text_test_passed));
            textView.setBackgroundResource(R.color.bg_test_tab_passed);
        } else {
            textView.setTextColor(getResources().getColor(R.color.white));
            textView.setBackgroundResource(R.color.bg_test_tab);
        }
    }
}
