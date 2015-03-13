package ru.com.cardiomagnyl.ui.slidingmenu.content.risk_analysis;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import ru.com.cardiomagnyl.app.R;
import ru.com.cardiomagnyl.application.Constants;
import ru.com.cardiomagnyl.model.test.TestPage;
import ru.com.cardiomagnyl.model.test.TestResult;

public class RiskAnalysisRecommendationFragment extends Fragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View parentView = inflater.inflate(R.layout.fragment_analysis_recomendation, null);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            String pageTitle = bundle.getString(Constants.PAGE_TITLE);
            TestPage testPage = bundle.getParcelable(Constants.TEST_PAGE);
            initTestResultsRecomendationFragment(parentView, testPage, pageTitle);
        }

        return parentView;
    }

    private void initTestResultsRecomendationFragment(final View view, final TestPage testPage, String pageTitle) {
        TextView textViewRecommendationAbout = (TextView) view.findViewById(R.id.textViewRecommendationAbout);
        TextView textViewRecommendationMain = (TextView) view.findViewById(R.id.textViewRecommendationMain);
        ImageView imageViewState = (ImageView) view.findViewById(R.id.imageViewState);
        TextView textViewRecommendationInfo = (TextView) view.findViewById(R.id.textViewRecommendationInfo);
        TextView textViewGeneralContent = (TextView) view.findViewById(R.id.textViewGeneralContent);

        textViewRecommendationAbout.setText(pageTitle);
        setImageView(imageViewState, testPage.getState());
        textViewRecommendationMain.setText(testPage.getTitle() + "\n" + testPage.getSubtitle());
        textViewRecommendationInfo.setText(view.getContext().getString(R.string.information));
        textViewGeneralContent.setText(testPage.getText());
    }

    private void setImageView(ImageView imageView, String stateString) {
        int imageResource;
        TestResult.STATES state = TestResult.STATES.undefined;

        try {
            state = TestResult.STATES.valueOf(stateString.toLowerCase());
        } catch (Exception ex) { /*does nothing*/ }

        switch (state) {
            case ask:
                imageResource = R.drawable.ic_undefined_pressed;
                break;
            case attention:
                imageResource = R.drawable.ic_attention_big_red;
                break;
            case bell:
                imageResource = R.drawable.ic_bell_pressed;
                break;
            case doctor:
                imageResource = R.drawable.ic_doctor_pressed;
                break;
            case ok:
                imageResource = R.drawable.ic_good_pressed;
                break;
            case empty:
                imageResource = 0;
                break;
            default:
                imageResource = -1;
                break;

        }
        if (imageResource > 0) {
            imageView.setImageResource(imageResource);
        } else if (imageResource == 0) {
            imageView.setVisibility(View.GONE);
        } else {
            imageView.setVisibility(View.INVISIBLE);
        }
    }

}
