package ru.com.cardiomagnyl.ui.slidingmenu.content.risk_analysis;

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
import android.widget.Toast;

import java.util.List;

import ru.com.cardiomagnyl.api.Url;
import ru.com.cardiomagnyl.app.R;
import ru.com.cardiomagnyl.application.AppState;
import ru.com.cardiomagnyl.model.common.Dummy;
import ru.com.cardiomagnyl.model.common.Email;
import ru.com.cardiomagnyl.model.common.Response;
import ru.com.cardiomagnyl.model.test.TestBanner;
import ru.com.cardiomagnyl.model.test.TestBanners;
import ru.com.cardiomagnyl.model.test.TestNote;
import ru.com.cardiomagnyl.model.test.TestResult;
import ru.com.cardiomagnyl.model.test.TestResult.STATES;
import ru.com.cardiomagnyl.model.test.TestResultDao;
import ru.com.cardiomagnyl.model.token.Token;
import ru.com.cardiomagnyl.model.user.User;
import ru.com.cardiomagnyl.ui.base.BaseItemFragment;
import ru.com.cardiomagnyl.ui.slidingmenu.content.personal_cabinet.CabinetTestFragment;
import ru.com.cardiomagnyl.ui.slidingmenu.menu.SlidingMenuActivity;
import ru.com.cardiomagnyl.util.CallbackOne;
import ru.com.cardiomagnyl.util.Tools;

public class RiskAnalysisResultsFragment extends BaseItemFragment {
    private View parentView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.fragment_analysis_results, null);
        initTestResultsFragment(parentView);
        return parentView;
    }

    @Override
    public void initTopBar(ViewGroup viewGroupTopBar) {
        initTopBarBellCabinet(viewGroupTopBar, true, true);
    }

    private void initTestResultsFragment(View view) {
        TestResult testResult = AppState.getInsnatce().getTestResult();
        User user = AppState.getInsnatce().getUser();
        Token token = AppState.getInsnatce().getToken();

        initButtons(view, testResult, user, token);
        initScore(view, testResult);

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

        TestBanners banners = testResult.getRecommendations().getBanners();

        TestBanner bannerAdjustmentOfDiet = new TestBanner();
        bannerAdjustmentOfDiet.setState(STATES.ask.name());
        bannerAdjustmentOfDiet.setTitle(getString(R.string.adjustment_of_diet));
        bannerAdjustmentOfDiet.setSubtitle(getString(R.string.pass_poll));
        bannerAdjustmentOfDiet.setPageUrl(Url.BANNER_PASS_POLL);

        TestBanner bannerChooseMedicalInstitution = new TestBanner();
        if (testResult.getRecommendations().getPlacesLinkShouldBeVisible()) {
            bannerChooseMedicalInstitution.setState(STATES.undefined.name());
            bannerChooseMedicalInstitution.setTitle("");
            bannerChooseMedicalInstitution.setSubtitle(getString(R.string.choose_medical_institution));
            bannerChooseMedicalInstitution.setPageUrl(Url.BANNER_CHOOSE_MEDICAL_INSTITUTION);
        }

        // FIXME
        // View linearLayoutDangerAlert = view.findViewById(R.id.linearLayoutDangerAlert);

        View linearLayoutScoreNote = view.findViewById(R.id.linearLayoutScoreNote);
        View linearLayoutMainRecommendation = view.findViewById(R.id.linearLayoutMainRecommendation);

        View linearLayoutBannerChooseMedicalInstitution = view.findViewById(R.id.linearLayoutBannerChooseMedicalInstitution);
        View linearLayoutBannerSmoking = view.findViewById(R.id.linearLayoutBannerSmoking);
        View linearLayoutBannerPhysicalActivity = view.findViewById(R.id.linearLayoutBannerPhysicalActivity);
        View linearLayoutBannerBMI = view.findViewById(R.id.linearLayoutBannerBMI);
        View linearLayoutBannerCholesterolLevel = view.findViewById(R.id.linearLayoutBannerCholesterolLevel);
        View linearLayoutBannerArterialPressure = view.findViewById(R.id.linearLayoutBannerArterialPressure);
        View linearLayoutBannerSugarProblems = view.findViewById(R.id.linearLayoutBannerSugarProblems);
        View linearLayoutBannerPressureDrugs = view.findViewById(R.id.linearLayoutBannerPressureDrugs);
        View linearLayoutBannerCholesterolDrugs = view.findViewById(R.id.linearLayoutBannerCholesterolDrugs);
        View linearLayoutBannerExtraSalt = view.findViewById(R.id.linearLayoutBannerExtraSalt);
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
        initBanner(linearLayoutBannerArterialPressure, banners.getArterialPressure());
        initBanner(linearLayoutBannerSugarProblems, banners.getHadSugarProblems());
        initBanner(linearLayoutBannerPressureDrugs, banners.getIsArterialPressureDrugsConsumer());
        initBanner(linearLayoutBannerCholesterolDrugs, banners.getIsCholesterolDrugsConsumer());
        initBanner(linearLayoutBannerExtraSalt, banners.getIsAddingExtraSalt());
        initBanner(linearLayoutBannerAdjustmentOfDiet, bannerAdjustmentOfDiet);
    }

    private void initButtons(final View view, final TestResult testResult, final User user, final Token token) {
        View imageViewSendEmail = view.findViewById(R.id.imageViewSendEmail);
        View imageViewInfo = view.findViewById(R.id.imageViewInfo);

        imageViewSendEmail.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                sendByEmail(testResult, user, token);
            }
        });

        imageViewInfo.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
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

    private void sendByEmail(final TestResult testResult, final User user, Token token) {
        final SlidingMenuActivity slidingMenuActivity = (SlidingMenuActivity) getActivity();
        slidingMenuActivity.showProgressDialog();

        Email email = new Email();
        email.setEmail(user.getEmail());

        TestResultDao.sendByEmail(
                testResult,
                email,
                token,
                new CallbackOne<List<Dummy>>() {
                    @Override
                    public void execute(List<Dummy> dummy) {
                        slidingMenuActivity.hideProgressDialog();
                        Tools.showToast(getActivity(), R.string.results_sent_successfully, Toast.LENGTH_LONG);
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

    private void initScoreHelper(View view, TestResult testResult) {
        View relativeLayoutScore = view.findViewById(R.id.relativeLayoutScore);
        View imageViewResultCircleHolder = view.findViewById(R.id.imageViewResultCircleHolder);
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) imageViewResultCircleHolder.getLayoutParams();
        TextView textViewResult = (TextView) view.findViewById(R.id.textViewResult);

        int maxX = relativeLayoutScore.getWidth() - (imageViewResultCircleHolder.getWidth() + layoutParams.leftMargin + layoutParams.rightMargin);
        int x = (maxX / 100) * testResult.getScore() + layoutParams.leftMargin;

        layoutParams.setMargins(x, layoutParams.topMargin, layoutParams.rightMargin, layoutParams.bottomMargin);
        textViewResult.setText(String.valueOf(testResult.getScore()) + "%");
    }

    private void initPeace(View pieceView, TestNote testNote) {
        ImageView imageViewState = (ImageView) pieceView.findViewById(R.id.imageViewState);
        TextView textViewText = (TextView) pieceView.findViewById(R.id.textViewText);

        if (testNote == null || (TextUtils.isEmpty(testNote.getState()) && TextUtils.isEmpty(testNote.getText()))) {
            pieceView.setVisibility(View.GONE);
            return;
        }

        setImageView(imageViewState, testNote.getState());
        setTextView(textViewText, testNote.getText());
    }

    private void initBanner(View bannerView, TestBanner bannerData) {
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

        setClickable(bannerView, bannerData.getPageUrl());
        setImageView(imageViewState, bannerData.getState());
        setTextView(textViewTitle, bannerData.getTitle());
        setTextView(textViewSubtitle, bannerData.getSubtitle());
        setTextView(textViewNote, bannerData.getNote());
    }

    private void setClickable(final View bannerView, final String pageUrl) {
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
        SlidingMenuActivity slidingMenuActivity = (SlidingMenuActivity) getActivity();
        if (Url.BANNER_CHOOSE_MEDICAL_INSTITUTION.equals(pageUrl)) {

        } else if (Url.BANNER_PASS_POLL.equals(pageUrl)) {
            BaseItemFragment cabinetTestFragment = new CabinetTestFragment();
            slidingMenuActivity.replaceAllContent(cabinetTestFragment, false);
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
                imageResource = R.drawable.selector_undefined;
                break;
            case attention:
                imageResource = R.drawable.selector_danger;
                break;
            case bell:
                imageResource = R.drawable.selector_bell;
                break;
            case doctor:
                imageResource = R.drawable.selector_doctor;
                break;
            case ok:
                imageResource = R.drawable.selector_good;
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
