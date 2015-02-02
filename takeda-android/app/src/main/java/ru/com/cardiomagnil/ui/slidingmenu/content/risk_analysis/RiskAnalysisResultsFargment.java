package ru.com.cardiomagnil.ui.slidingmenu.content.risk_analysis;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import ru.com.cardiomagnil.app.R;
import ru.com.cardiomagnil.application.AppState;
import ru.com.cardiomagnil.ca_api.Url;
import ru.com.cardiomagnil.model.test.Banner;
import ru.com.cardiomagnil.model.test.Banners;
import ru.com.cardiomagnil.model.test.Note;
import ru.com.cardiomagnil.model.test.TestResult;
import ru.com.cardiomagnil.model.test.TestResult.STATES;
import ru.com.cardiomagnil.ui.base.BaseItemFragment;

public class RiskAnalysisResultsFargment extends BaseItemFragment {
    private View parentView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.fragment_slidingmenu_analysis_results, null);
        initTestResultsFargment(parentView);
        return parentView;
    }

    @Override
    public void initTopBar(ViewGroup viewGroupTopBar) {
    }

    private void initTestResultsFargment(View view) {
        TestResult testResult = AppState.getInstatce().getTestResult();

        ScrollView scrollViewResults = (ScrollView) view.findViewById(R.id.scrollViewResults);
        TextView textViewError = (TextView) view.findViewById(R.id.textViewError);

        if (testResult != null && testResult.getId() != null) {
            scrollViewResults.setVisibility(View.VISIBLE);
            textViewError.setVisibility(View.INVISIBLE);
        } else {
            scrollViewResults.setVisibility(View.INVISIBLE);
            textViewError.setVisibility(View.VISIBLE);
            return;
        }
        // copy state from scoreNote into mainRecommendation because state in mainRecommendation is empty
        String state = testResult.getRecommendations().getScoreNote().getState();
        if (!STATES.empty.name().equals(state)) {
            testResult.getRecommendations().getMainRecommendation().setState(state);
            testResult.getRecommendations().getScoreNote().setState(STATES.empty.name());
        }

        Banners banners = testResult.getRecommendations().getBanners();

        Banner bannerAdjustmentOfDiet = new Banner();
        bannerAdjustmentOfDiet.setState(STATES.ask.name());
        bannerAdjustmentOfDiet.setTitle(getString(R.string.adjustment_of_diet));
        bannerAdjustmentOfDiet.setSubtitle(getString(R.string.pass_poll));
        bannerAdjustmentOfDiet.setPageUrl(Url.BANNER_CHOOSE_MEDICAL_INSTITUTION);

        Banner bannerChooseMedicalInstitution = new Banner();
        if (testResult.getRecommendations().getPlacesLinkShouldBeVisible()) {
            bannerChooseMedicalInstitution.setState(STATES.undefined.name());
            bannerChooseMedicalInstitution.setTitle("");
            bannerChooseMedicalInstitution.setSubtitle(getString(R.string.choose_medical_institution));
            bannerChooseMedicalInstitution.setPageUrl(Url.BANNER_PASS_POLL);
        }

        initScore(view, testResult);

        // FIXME
        // View linearLayoutDangerAlert = view.findViewById(R.id.linearLayoutDangerAlert);

        View linearLayoutScoreNote = view.findViewById(R.id.linearLayoutScoreNote);
        View linearLayoutMainRecommendation = view.findViewById(R.id.linearLayoutMainRecommendation);

        View linearLayoutBannerChooseMedicalInstitution = view.findViewById(R.id.linearLayoutBannerChooseMedicalInstitution);
        View linearLayoutBannerSmoking = view.findViewById(R.id.linearLayoutBannerSmoking);
        View linearLayoutBannerPhysicalActivity = view.findViewById(R.id.linearLayoutBannerPhysicalActivity);
        View linearLayoutBannerBMI = view.findViewById(R.id.linearLayoutBannerBMI);
        View linearLayoutBannerCholesterolLevel = view.findViewById(R.id.linearLayoutBannerCholesterolLevel);
        View linearLayoutBanneArterialPressure = view.findViewById(R.id.linearLayoutBanneArterialPressure);
        View linearLayoutBanneSugarProblems = view.findViewById(R.id.linearLayoutBanneSugarProblems);
        View linearLayoutBannePressureDrugs = view.findViewById(R.id.linearLayoutBannePressureDrugs);
        View linearLayoutBanneholesterolDrugs = view.findViewById(R.id.linearLayoutBanneholesterolDrugs);
        View linearLayoutBanneExtraSalt = view.findViewById(R.id.linearLayoutBannerExtraSalt);
        View linearLayoutBannerAdjustmentOfDiet = view.findViewById(R.id.linearLayoutBannerAdjustmentOfDiet);

        // FIXME
        // initPeace(linearLayoutDangerAlert, testResult.getRecommendations().getFullscreenAlert());
        // if (linearLayoutDangerAlert.getVisibility() != View.GONE) {
        //     View linearLayoutBigWrapper = view.findViewById(R.id.linearLayoutBigWrapper);
        //     linearLayoutBigWrapper.setVisibility(View.GONE);
        //     return;
        // }

        initPeace(linearLayoutScoreNote, testResult.getRecommendations().getScoreNote());
        initPeace(linearLayoutMainRecommendation, testResult.getRecommendations().getMainRecommendation());

        initBanner(linearLayoutBannerChooseMedicalInstitution, bannerChooseMedicalInstitution);
        initBanner(linearLayoutBannerSmoking, banners.getIsSmoker());
        initBanner(linearLayoutBannerPhysicalActivity, banners.getPhysicalActivityMinutes());
        initBanner(linearLayoutBannerBMI, banners.getBmi());
        initBanner(linearLayoutBannerCholesterolLevel, banners.getCholesterolLevel());
        initBanner(linearLayoutBanneArterialPressure, banners.getArterialPressure());
        initBanner(linearLayoutBanneSugarProblems, banners.getHadSugarProblems());
        initBanner(linearLayoutBannePressureDrugs, banners.getIsArterialPressureDrugsConsumer());
        initBanner(linearLayoutBanneholesterolDrugs, banners.getIsCholesterolDrugsConsumer());
        initBanner(linearLayoutBanneExtraSalt, banners.getIsAddingExtraSalt());
        initBanner(linearLayoutBannerAdjustmentOfDiet, bannerAdjustmentOfDiet);
    }

    private void initScore(final View view, final TestResult testResult) {
        view.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        initScoreHelper(view, testResult);
                        // unregister listener (this is important)
                        view.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    }
                });
    }

    private void initScoreHelper(View view, TestResult testResult) {
        View relativeLayoutScore = view.findViewById(R.id.relativeLayoutScore);
        View imageViewResultCircleHolder = view.findViewById(R.id.imageViewResultCircleHolder);
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) imageViewResultCircleHolder.getLayoutParams();
        TextView textViewResult = (TextView) view.findViewById(R.id.textViewResult);

        int maxX = relativeLayoutScore.getWidth() - (imageViewResultCircleHolder.getWidth() + layoutParams.leftMargin + layoutParams.rightMargin);
        int x = (maxX / 100) * testResult.getScore() + layoutParams.leftMargin;

        layoutParams.setMargins(x,  layoutParams.topMargin, layoutParams.rightMargin, layoutParams.bottomMargin);
        textViewResult.setText(String.valueOf(testResult.getScore()) + "%");
    }

    private void initPeace(View pieceView, Note note) {
        ImageView imageViewState = (ImageView) pieceView.findViewById(R.id.imageViewState);
        TextView textViewText = (TextView) pieceView.findViewById(R.id.textViewText);

        if (note == null || (TextUtils.isEmpty(note.getState()) && TextUtils.isEmpty(note.getText()))) {
            pieceView.setVisibility(View.GONE);
            return;
        }

        setImageView(imageViewState, note.getState());
        setTextView(textViewText, note.getText());
    }

    private void initBanner(View bannerView, Banner bannerData) {
        ImageView imageViewState = (ImageView) bannerView.findViewById(R.id.imageViewState);
        TextView textViewTitle = (TextView) bannerView.findViewById(R.id.textViewTitle);
        TextView textViewSubtitle = (TextView) bannerView.findViewById(R.id.textViewSubtitle);
        TextView textViewNote = (TextView) bannerView.findViewById(R.id.textViewNote);


        if (bannerData == null
                || ((TextUtils.isEmpty(bannerData.getState()) &&
                TextUtils.isEmpty(bannerData.getTitle()) &&
                TextUtils.isEmpty(bannerData.getSubtitle()) &&
                TextUtils.isEmpty(bannerData.getNote())))) {
            bannerView.setVisibility(View.GONE);
            return;
        }

        setClicable(bannerView, bannerData.getPageUrl());
        setImageView(imageViewState, bannerData.getState());
        setTextView(textViewTitle, bannerData.getTitle());
        setTextView(textViewSubtitle, bannerData.getSubtitle());
        setTextView(textViewNote, bannerData.getNote());
    }

    private void setClicable(final View bannerView, final String pageUrl) {
        ImageView imageViewRight = (ImageView) bannerView.findViewById(R.id.imageViewRight);

        if (TextUtils.isEmpty(pageUrl)) {
            imageViewRight.setVisibility(View.GONE);
            bannerView.setClickable(false);
        } else {
            imageViewRight.setVisibility(View.VISIBLE);
            bannerView.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (pageUrl.startsWith(Url.LOCAL)) {
                        showLocalPage(pageUrl);
                    } else {
                        showRemotePage(pageUrl);
                    }
                }
            });
        }
    }

    private void showLocalPage(String pageUrl) {
        if (Url.BANNER_CHOOSE_MEDICAL_INSTITUTION.equals(pageUrl)) {

        } else if (Url.BANNER_PASS_POLL.equals(pageUrl)) {

        }
    }

    private void showRemotePage(String pageUrl) {
        // do_in_background
    }

    private void setImageView(ImageView imageView, String stateString) {
        int imageResource;
        STATES state = STATES.undefined;

        try {
            state = STATES.valueOf(stateString.toLowerCase());
        } catch (Exception ex) {
            // do nothing
        }

        switch (state) {
            case ask:
                imageResource = R.drawable.ca_selector_undefined;
                break;
            case attention:
                imageResource = R.drawable.ca_selector_danger;
                break;
            case bell:
                imageResource = R.drawable.ca_selector_bell;
                break;
            case doctor:
                imageResource = R.drawable.ca_selector_doctor;
                break;
            case ok:
                imageResource = R.drawable.ca_selector_good;
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

    private void setTextView(TextView textView, String text) {
        if (TextUtils.isEmpty(text)) {
            // textView.setVisibility(View.INVISIBLE);
            textView.setHeight(0);
        } else {
            textView.setText(text);
        }
    }
}
