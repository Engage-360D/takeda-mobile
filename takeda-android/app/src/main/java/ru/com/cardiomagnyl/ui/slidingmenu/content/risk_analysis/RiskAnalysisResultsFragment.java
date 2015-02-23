package ru.com.cardiomagnyl.ui.slidingmenu.content.risk_analysis;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import ru.com.cardiomagnyl.api.Url;
import ru.com.cardiomagnyl.app.R;
import ru.com.cardiomagnyl.application.AppState;
import ru.com.cardiomagnyl.application.Constants;
import ru.com.cardiomagnyl.model.common.Dummy;
import ru.com.cardiomagnyl.model.common.Email;
import ru.com.cardiomagnyl.model.common.Response;
import ru.com.cardiomagnyl.model.test.PageDao;
import ru.com.cardiomagnyl.model.test.TestBanner;
import ru.com.cardiomagnyl.model.test.TestBanners;
import ru.com.cardiomagnyl.model.test.TestNote;
import ru.com.cardiomagnyl.model.test.TestPage;
import ru.com.cardiomagnyl.model.test.TestResult;
import ru.com.cardiomagnyl.model.test.TestResult.STATES;
import ru.com.cardiomagnyl.model.test.TestResultDao;
import ru.com.cardiomagnyl.model.token.Token;
import ru.com.cardiomagnyl.model.user.User;
import ru.com.cardiomagnyl.ui.base.BaseItemFragment;
import ru.com.cardiomagnyl.ui.slidingmenu.content.InformationFragment;
import ru.com.cardiomagnyl.ui.slidingmenu.content.SearchInstitutionsFragment;
import ru.com.cardiomagnyl.ui.slidingmenu.content.personal_cabinet.CabinetTestFragment;
import ru.com.cardiomagnyl.ui.slidingmenu.menu.MenuItem;
import ru.com.cardiomagnyl.ui.slidingmenu.menu.SlidingMenuActivity;
import ru.com.cardiomagnyl.util.CallbackOne;
import ru.com.cardiomagnyl.util.Tools;
import ru.com.cardiomagnyl.widget.CustomDialogs;

public class RiskAnalysisResultsFragment extends BaseItemFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View parentView = inflater.inflate(R.layout.fragment_analysis_results, null);

        boolean isNotPassed = false;
        Bundle bundle = this.getArguments();
        if (bundle != null) isNotPassed = bundle.getBoolean(Constants.IS_NOT_PASSED);

        initTestResultsFragment(parentView, isNotPassed);
        return parentView;
    }

    @Override
    public void initTopBar(ViewGroup viewGroupTopBar) {
        initTopBarMenuBellCabinet(viewGroupTopBar, true, true, true);
    }

    private void initTestResultsFragment(View view, boolean isNotPassed) {
        TestResult testResult = AppState.getInsnatce().getTestResult();
        User user = AppState.getInsnatce().getUser();
        Token token = AppState.getInsnatce().getToken();

        ScrollView scrollViewResults = (ScrollView) view.findViewById(R.id.scrollViewResults);
        TextView textViewError = (TextView) view.findViewById(R.id.textViewError);

        SlidingMenuActivity slidingMenuActivity = (SlidingMenuActivity) getActivity();
        if (testResult != null) {
            slidingMenuActivity.unLockMenu();
            updateMenu();

            initTopBarBellCabinet(slidingMenuActivity.getLayoutHeader(), true, true);
        } else {
            initTopBarBellCabinet(slidingMenuActivity.getLayoutHeader(), false, false);
        }

        if (isNotPassed) {
            scrollViewResults.setVisibility(View.INVISIBLE);
            textViewError.setVisibility(View.VISIBLE);

            initButtons(view, null, user, token);
            initScore(view, null);
            return;
        } else {
            scrollViewResults.setVisibility(View.VISIBLE);
            textViewError.setVisibility(View.INVISIBLE);

            initButtons(view, testResult, user, token);
            initScore(view, testResult);
        }

        View linearLayoutScoreNote = view.findViewById(R.id.linearLayoutScoreNote);
        View linearLayoutFullScreenAlert = view.findViewById(R.id.linearLayoutFullScreenAlert);
        View linearLayoutMainRecommendation = view.findViewById(R.id.linearLayoutMainRecommendation);

        TestNote scoreNote = testResult.getRecommendations().getScoreNote();
        TestNote fullScreenAlert = testResult.getRecommendations().getFullScreenAlert();
        TestNote mainRecommendation = testResult.getRecommendations().getMainRecommendation();

        if (TestNote.isEmpty(fullScreenAlert)) {
            initPeace(linearLayoutScoreNote, scoreNote);
            initPeace(linearLayoutFullScreenAlert, null);
            initPeace(linearLayoutMainRecommendation, mainRecommendation);
        } else {
            initPeace(linearLayoutScoreNote, null);
            initPeace(linearLayoutFullScreenAlert, fullScreenAlert);
            initPeace(linearLayoutMainRecommendation, null);

            customizeOnFullScreenAlert(view, linearLayoutFullScreenAlert, testResult);
        }

        TestBanners banners = testResult.getRecommendations().getBanners();
        TestBanner bannerChooseMedicalInstitution = createBannerChooseMedicalInstitution(testResult);
        TestBanner bannerAdjustmentOfDiet = createBannerAdjustmentOfDiet();

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

        initBanner(linearLayoutBannerSmoking, banners.getIsSmoker());
        initBanner(linearLayoutBannerPhysicalActivity, banners.getPhysicalActivityMinutes());
        initBanner(linearLayoutBannerBMI, banners.getBmi());
        initBanner(linearLayoutBannerCholesterolLevel, banners.getCholesterolLevel());
        initBanner(linearLayoutBannerArterialPressure, banners.getArterialPressure());
        initBanner(linearLayoutBannerSugarProblems, banners.getHadSugarProblems());
        initBanner(linearLayoutBannerPressureDrugs, banners.getIsArterialPressureDrugsConsumer());
        initBanner(linearLayoutBannerCholesterolDrugs, banners.getIsCholesterolDrugsConsumer());
        initBanner(linearLayoutBannerExtraSalt, banners.getIsAddingExtraSalt());
        initBanner(linearLayoutBannerChooseMedicalInstitution, bannerChooseMedicalInstitution);
        initBanner(linearLayoutBannerAdjustmentOfDiet, bannerAdjustmentOfDiet);
    }

    private TestBanner createBannerChooseMedicalInstitution(TestResult testResult) {
        TestBanner bannerChooseMedicalInstitution = new TestBanner();
        if (testResult.getRecommendations().getPlacesLinkShouldBeVisible()) {
            bannerChooseMedicalInstitution.setState(STATES.undefined.name());
            bannerChooseMedicalInstitution.setTitle("");
            bannerChooseMedicalInstitution.setSubtitle(getString(R.string.choose_medical_institution));
            bannerChooseMedicalInstitution.setPageUrl(Url.BANNER_CHOOSE_MEDICAL_INSTITUTION);
        }
        return bannerChooseMedicalInstitution;
    }

    private TestBanner createBannerAdjustmentOfDiet() {
        TestBanner bannerAdjustmentOfDiet = new TestBanner();
        bannerAdjustmentOfDiet.setState(STATES.ask.name());
        bannerAdjustmentOfDiet.setTitle(getString(R.string.adjustment_of_diet));
        bannerAdjustmentOfDiet.setSubtitle(getString(R.string.pass_poll));
        bannerAdjustmentOfDiet.setPageUrl(Url.BANNER_PASS_POLL);
        return bannerAdjustmentOfDiet;
    }

    private void customizeOnFullScreenAlert(View parentView, View linearLayoutFullScreenAlert, TestResult testResult) {
        TestBanner bannerChooseMedicalInstitution = createBannerChooseMedicalInstitution(testResult);

        View layoutTopMenu = parentView.findViewById(R.id.layoutTopMenu);
        TextView textViewResult = (TextView) parentView.findViewById(R.id.textViewResult);
        ImageView imageViewState = (ImageView) linearLayoutFullScreenAlert.findViewById(R.id.imageViewState);
        View linearLayoutBannerChooseMedicalInstitution = parentView.findViewById(R.id.linearLayoutBannerChooseMedicalInstitution);
        View linearLayoutBigWrapper = parentView.findViewById(R.id.linearLayoutBigWrapper);

        parentView.setBackgroundColor(getResources().getColor(R.color.bg_test_result_bad));
        layoutTopMenu.setBackgroundColor(getResources().getColor(R.color.bg_header_result_bad));
        textViewResult.setTextColor(getResources().getColor(R.color.bg_test_result_bad));
        imageViewState.setImageResource(R.drawable.ic_attention_big_white);
        initBanner(linearLayoutBannerChooseMedicalInstitution, bannerChooseMedicalInstitution);
        linearLayoutBigWrapper.setVisibility(View.GONE);
    }

    private void updateMenu() {
        MenuItem.item_analysis_results.setItemIsEnabled(true);
        MenuItem.item_analysis_results.setItemIsVisible(true);
        SlidingMenuActivity slidingMenuActivity = (SlidingMenuActivity) getActivity();
        slidingMenuActivity.refreshMenuItems();
        slidingMenuActivity.selectCurrentItem(this);
    }

    private void initButtons(final View view, final TestResult testResult, final User user, final Token token) {
        View imageViewSendEmail = view.findViewById(R.id.imageViewSendEmail);
        View imageViewInfo = view.findViewById(R.id.imageViewInfo);

        int visibility = testResult == null ? View.INVISIBLE : View.VISIBLE;
        imageViewSendEmail.setVisibility(visibility);
        imageViewInfo.setVisibility(visibility);

        if (testResult == null) return;

        imageViewSendEmail.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                tryToSendResult(testResult, user, token);
            }
        });

        imageViewInfo.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                SlidingMenuActivity slidingMenuActivity = (SlidingMenuActivity) getActivity();
                BaseItemFragment informationFragment = new InformationFragment();
                slidingMenuActivity.putContentOnTop(informationFragment, false);
            }
        });
    }

    private void initScore(final View view, final TestResult testResult) {
        if (testResult == null) return;

        view.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        // unregister listener (this is important)
                        view.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                        initScoreHelper(view, testResult);
                    }
                });
    }

    private void initScoreHelper(View view, TestResult testResult) {
        TextView textViewResult = (TextView) view.findViewById(R.id.textViewResult);
        View relativeLayoutScore = view.findViewById(R.id.relativeLayoutScore);
        RelativeLayout imageViewResultCircleHolder = (RelativeLayout) view.findViewById(R.id.relativeLayoutResultCircleHolder);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) imageViewResultCircleHolder.getLayoutParams();

        textViewResult.setText(String.valueOf(testResult.getScorePercents()) + "%");

        int maxX = relativeLayoutScore.getWidth() - (imageViewResultCircleHolder.getWidth() + layoutParams.leftMargin + layoutParams.rightMargin);
        int x = (maxX * testResult.getScorePercents() / 100) + layoutParams.leftMargin;

        RelativeLayout.LayoutParams newLayoutParams = new RelativeLayout.LayoutParams(imageViewResultCircleHolder.getMeasuredWidth(), imageViewResultCircleHolder.getMeasuredWidth());
        newLayoutParams.setMargins(x, layoutParams.topMargin, layoutParams.rightMargin, layoutParams.bottomMargin);
        imageViewResultCircleHolder.setLayoutParams(newLayoutParams);
    }

    private void tryToSendResult(final TestResult testResult, final User user, final Token token) {
        CustomDialogs.showConfirmationDialog(
                getActivity(),
                String.format(getString(R.string.send_results), user.getEmail()),
                new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sendByEmail(testResult, user, token);
                    }
                }
        );
    }

    private void sendByEmail(final TestResult testResult, final User user, final Token token) {
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

    private void initPeace(View pieceView, TestNote testNote) {
        ImageView imageViewState = (ImageView) pieceView.findViewById(R.id.imageViewState);
        TextView textViewText = (TextView) pieceView.findViewById(R.id.textViewText);

        if (TestNote.isEmpty(testNote)) {
            pieceView.setVisibility(View.GONE);
            return;
        }

        pieceView.setVisibility(View.VISIBLE);

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

        setClickable(bannerView, bannerData);
        setImageView(imageViewState, bannerData.getState());
        setTextView(textViewTitle, bannerData.getTitle());
        setTextView(textViewSubtitle, bannerData.getSubtitle());
        setTextView(textViewNote, bannerData.getNote());
    }

    private void setClickable(final View bannerView, final TestBanner bannerData) {
        ImageView imageViewRight = (ImageView) bannerView.findViewById(R.id.imageViewRight);

        if (TextUtils.isEmpty(bannerData.getPageUrl())) {
            imageViewRight.setVisibility(View.GONE);
            bannerView.setClickable(false);
        } else {
            imageViewRight.setVisibility(View.VISIBLE);
            bannerView.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (bannerData.getPageUrl().startsWith(Url.LOCAL)) {
                        showLocalPage(bannerData);
                    } else {
                        showRemotePage(bannerData);
                    }
                }
            });
        }
    }

    private void showLocalPage(final TestBanner bannerData) {
        SlidingMenuActivity slidingMenuActivity = (SlidingMenuActivity) getActivity();
        if (Url.BANNER_CHOOSE_MEDICAL_INSTITUTION.equals(bannerData.getPageUrl())) {
            // attempt to fix slow initialization of SupportMapFragment ("dirty hack")
            ((ViewGroup) getView()).removeAllViews();
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View layout_loading_map = layoutInflater.inflate(R.layout.layout_loading_map, null);
            ((ViewGroup) getView()).addView(layout_loading_map);

            BaseItemFragment searchInstitutionsFragment = new SearchInstitutionsFragment();
            slidingMenuActivity.replaceAllContent(searchInstitutionsFragment, false);
            slidingMenuActivity.selectCurrentItem(searchInstitutionsFragment);
        } else if (Url.BANNER_PASS_POLL.equals(bannerData.getPageUrl())) {
            BaseItemFragment cabinetTestFragment = new CabinetTestFragment();
            slidingMenuActivity.replaceAllContent(cabinetTestFragment, false);
            //FIXME: change if need
            // slidingMenuActivity.selectCurrentItem(cabinetTestFragment);
            slidingMenuActivity.unselectCurrentItem();
        }
    }

    private void showRemotePage(final TestBanner bannerData) {
        final SlidingMenuActivity slidingMenuActivity = (SlidingMenuActivity) getActivity();
        slidingMenuActivity.showProgressDialog();

        Token token = AppState.getInsnatce().getToken();

        PageDao.getByLink(
                bannerData.getPageUrl(),
                token,
                new CallbackOne<TestPage>() {
                    @Override
                    public void execute(TestPage testPage) {
                        slidingMenuActivity.hideProgressDialog();

                        Bundle bundle = new Bundle();
                        bundle.putString(Constants.PAGE_TITLE, bannerData.getTitle());
                        bundle.putParcelable(Constants.TEST_PAGE, testPage);

                        RiskAnalysisRecommendationFragment riskAnalysisRecommendationFragment = new RiskAnalysisRecommendationFragment();
                        riskAnalysisRecommendationFragment.setArguments(bundle);

                        slidingMenuActivity.putContentOnTop(riskAnalysisRecommendationFragment, true);
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
