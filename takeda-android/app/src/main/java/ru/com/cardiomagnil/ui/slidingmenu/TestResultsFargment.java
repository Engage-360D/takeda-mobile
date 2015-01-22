package ru.com.cardiomagnil.ui.slidingmenu;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.Map;

import ru.com.cardiomagnil.app.R;
import ru.com.cardiomagnil.application.AppState;
import ru.com.cardiomagnil.model.TestResult;
import ru.com.cardiomagnil.model.TestResult.BANNERS;
import ru.com.cardiomagnil.model.TestResult.PAGES;
import ru.com.cardiomagnil.model.TestResult.PIECES;
import ru.com.cardiomagnil.model.TestResult.STATES;
import ru.com.cardiomagnil.model.TestResultBanner;
import ru.com.cardiomagnil.model.TestResultPage;
import ru.com.cardiomagnil.model.TestResultPiece;

public class TestResultsFargment extends Fragment {
    private View parentView;
    private SlidingMenuActivity mSlidingMenuActivity;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.fragment_slidingmenu_analysis_results, null);
        mSlidingMenuActivity = (SlidingMenuActivity) getActivity();

        initTestResultsFargment(parentView);

        return parentView;
    }

    private void initTestResultsFargment(View view) {
        TestResult testResult = AppState.getInstatce().getTestResult();

        ScrollView scrollViewResults = (ScrollView) view.findViewById(R.id.scrollViewResults);
        TextView textViewError = (TextView) view.findViewById(R.id.textViewError);

        if (testResult.isInitialized()) {
            scrollViewResults.setVisibility(View.VISIBLE);
            textViewError.setVisibility(View.INVISIBLE);
        } else {
            scrollViewResults.setVisibility(View.INVISIBLE);
            textViewError.setVisibility(View.VISIBLE);
            return;
        }

        TextView textViewResult = (TextView) view.findViewById(R.id.textViewResult);

        View linearLayoutScoreDescription = view.findViewById(R.id.linearLayoutScoreDescription);
        View linearLayoutDangerAlert = view.findViewById(R.id.linearLayoutDangerAlert);
        View linearLayoutMainRecommendation = view.findViewById(R.id.linearLayoutMainRecommendation);

        View linearLayoutBannerSmoking = view.findViewById(R.id.linearLayoutBannerSmoking);
        View linearLayoutBannerPhysicalActivity = view.findViewById(R.id.linearLayoutBannerPhysicalActivity);
        View linearLayoutBannerBMI = view.findViewById(R.id.linearLayoutBannerBMI);
        View linearLayoutBannerCholesterolLevel = view.findViewById(R.id.linearLayoutBannerCholesterolLevel);
        View linearLayoutBanneArterialPressure = view.findViewById(R.id.linearLayoutBanneArterialPressure);
        View linearLayoutBanneSugarProblems = view.findViewById(R.id.linearLayoutBanneSugarProblems);
        View linearLayoutBannePressureDrugs = view.findViewById(R.id.linearLayoutBannePressureDrugs);
        View linearLayoutBanneholesterolDrugs = view.findViewById(R.id.linearLayoutBanneholesterolDrugs);
        View linearLayoutBanneExtraSalt = view.findViewById(R.id.linearLayoutBanneExtraSalt);

        textViewResult.setText(String.valueOf(testResult.getScoreValue()) + "%");

        initPeace(linearLayoutScoreDescription, testResult.getPieces().get(PIECES.scoreDescription.name()));
        initPeace(linearLayoutDangerAlert, testResult.getPieces().get(PIECES.dangerAlert.name()));
        if (linearLayoutDangerAlert.getVisibility() != View.GONE) {
            // View linearLayoutBigWrapper = view.findViewById(R.id.linearLayoutBigWrapper);
            // linearLayoutBigWrapper.setVisibility(View.GONE);
            // return;
        }
        initPeace(linearLayoutMainRecommendation, testResult.getPieces().get(PIECES.mainRecommendation.name()));

        Map<String, TestResultBanner> banners = testResult.getBanners();
        Map<String, TestResultPage> pages = testResult.getPages();

        initBanner(linearLayoutBannerSmoking, banners.get(BANNERS.smoking.name()), pages.get(PAGES.smoking.name()));
        initBanner(linearLayoutBannerPhysicalActivity, banners.get(BANNERS.physicalActivity.name()), pages.get(PAGES.physicalActivity.name()));
        initBanner(linearLayoutBannerBMI, banners.get(BANNERS.bmi.name()), pages.get(PAGES.bmi.name()));
        initBanner(linearLayoutBannerCholesterolLevel, banners.get(BANNERS.cholesterolLevel.name()), pages.get(PAGES.cholesterolLevel.name()));
        initBanner(linearLayoutBanneArterialPressure, banners.get(BANNERS.arterialPressure.name()), pages.get(PAGES.arterialPressure.name()));
        initBanner(linearLayoutBanneSugarProblems, banners.get(BANNERS.sugarProblems.name()), pages.get(PAGES.sugarProblems.name()));
        initBanner(linearLayoutBannePressureDrugs, banners.get(BANNERS.arterialPressureDrugs.name()), pages.get(PAGES.arterialPressureDrugs.name()));
        initBanner(linearLayoutBanneholesterolDrugs, banners.get(BANNERS.cholesterolDrugs.name()), pages.get(PAGES.cholesterolDrugs.name()));
        initBanner(linearLayoutBanneExtraSalt, banners.get(BANNERS.extraSalt.name()), pages.get(PAGES.extraSalt.name()));
    }

    private void initPeace(View pieceView, TestResultPiece peaceData) {
        ImageView imageViewState = (ImageView) pieceView.findViewById(R.id.imageViewState);
        TextView textViewText = (TextView) pieceView.findViewById(R.id.textViewText);

        if (peaceData == null || (peaceData.getState() == STATES.empty && peaceData.getText().isEmpty())) {
            pieceView.setVisibility(View.GONE);
            return;
        }

        setImageView(imageViewState, peaceData.getState());
        setTextView(textViewText, peaceData.getText());
    }

    private void initBanner(View bannerView, TestResultBanner bannerData, final TestResultPage pageData) {
        ImageView imageViewState = (ImageView) bannerView.findViewById(R.id.imageViewState);
        TextView textViewTitle = (TextView) bannerView.findViewById(R.id.textViewTitle);
        TextView textViewAberration = (TextView) bannerView.findViewById(R.id.textViewAberration);
        TextView textViewNote = (TextView) bannerView.findViewById(R.id.textViewNote);
        ImageView imageViewRight = (ImageView) bannerView.findViewById(R.id.imageViewRight);

        if (bannerData == null || (bannerData.getState() == STATES.empty &&
        /**/bannerData.getTitle().isEmpty() &&
        /**/bannerData.getAberration().isEmpty() &&
        /**/bannerData.getNote().isEmpty())) {
            bannerView.setVisibility(View.GONE);
            return;
        }

        setImageView(imageViewState, bannerData.getState());
        setTextView(textViewTitle, bannerData.getTitle());
        setTextView(textViewAberration, bannerData.getAberration());
        setTextView(textViewNote, bannerData.getNote());

        if (pageData == null) {
            imageViewRight.setVisibility(View.INVISIBLE);
            imageViewRight.setOnClickListener(null);
        } else {
            imageViewRight.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    AppState.getInstatce().setTestResultPage(pageData);
                    // FIXME!!! switchContent
//                    mSlidingMenuActivity.switchContent(new TestResultsRecomendationFargment(), TestResultsFargment.this);
                }
            });
        }
    }

    private void setImageView(ImageView imageView, STATES state) {
        int imageResource;

        switch (state) {
        case ask:
            imageResource = R.drawable.ic_undefined;
            break;
        case attention:
            imageResource = R.drawable.ic_danger;
            break;
        case bell:
            imageResource = R.drawable.ic_bell;
            break;
        case doctor:
            imageResource = R.drawable.ic_doctor;
            break;
        case ok:
            imageResource = R.drawable.ic_good;
            break;
        case empty:
            imageResource = -1;
            break;
        default:
            imageResource = -1;
            break;

        }
        if (imageResource > 0) {
            imageView.setImageResource(imageResource);
        } else {
            imageView.setVisibility(View.INVISIBLE);
        }
    }

    private void setTextView(TextView textView, String text) {
        if (text.isEmpty()) {
            // textView.setVisibility(View.INVISIBLE);
            textView.setHeight(0);
        } else {
            textView.setText(text);
        }
    }
}
