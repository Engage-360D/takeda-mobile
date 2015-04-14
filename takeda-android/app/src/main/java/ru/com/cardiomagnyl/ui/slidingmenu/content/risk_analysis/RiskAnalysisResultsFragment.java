package ru.com.cardiomagnyl.ui.slidingmenu.content.risk_analysis;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
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
import ru.com.cardiomagnyl.model.user.Email;
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
import ru.com.cardiomagnyl.ui.slidingmenu.content.institution.InstitutionsSearchFragment;
import ru.com.cardiomagnyl.ui.slidingmenu.content.recommendations.RecommendationsTestResultsFragment;
import ru.com.cardiomagnyl.ui.slidingmenu.menu.SlidingMenuActivity;
import ru.com.cardiomagnyl.util.CallbackOne;
import ru.com.cardiomagnyl.util.Tools;
import ru.com.cardiomagnyl.widget.CustomDialogLayout;

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
        TestResult testResult = AppState.getInsnatce().getTestResult();
        if (testResult != null) {
            initTopBarMenuBellCabinet(viewGroupTopBar, true, true, true);
        } else {
            initTopBarMenuBellCabinet(viewGroupTopBar, false, false, false);
        }
    }

    private void initTestResultsFragment(View view, boolean isNotPassed) {
        TestResult testResult = AppState.getInsnatce().getTestResult();
        User user = AppState.getInsnatce().getUser();
        Token token = AppState.getInsnatce().getToken();

        ScrollView scrollViewResults = (ScrollView) view.findViewById(R.id.scrollViewResults);
        TextView textViewError = (TextView) view.findViewById(R.id.textViewError);

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

        initBanner(linearLayoutBannerSmoking, banners.getIsSmoker(), testResult.getId());
        initBanner(linearLayoutBannerPhysicalActivity, banners.getPhysicalActivityMinutes(), testResult.getId());
        initBanner(linearLayoutBannerBMI, banners.getBmi(), testResult.getId());
        initBanner(linearLayoutBannerCholesterolLevel, banners.getCholesterolLevel(), testResult.getId());
        initBanner(linearLayoutBannerArterialPressure, banners.getArterialPressure(), testResult.getId());
        initBanner(linearLayoutBannerSugarProblems, banners.getHadSugarProblems(), testResult.getId());
        initBanner(linearLayoutBannerPressureDrugs, banners.getIsArterialPressureDrugsConsumer(), testResult.getId());
        initBanner(linearLayoutBannerCholesterolDrugs, banners.getIsCholesterolDrugsConsumer(), testResult.getId());
        initBanner(linearLayoutBannerExtraSalt, banners.getIsAddingExtraSalt(), testResult.getId());
        initBanner(linearLayoutBannerChooseMedicalInstitution, bannerChooseMedicalInstitution, testResult.getId());
        initBanner(linearLayoutBannerAdjustmentOfDiet, bannerAdjustmentOfDiet, testResult.getId());
    }

    private TestBanner createBannerChooseMedicalInstitution(TestResult testResult) {
        TestBanner bannerChooseMedicalInstitution = new TestBanner();
        if (testResult.getRecommendations().getPlacesLinkShouldBeVisible()) {
            bannerChooseMedicalInstitution.setState(STATES.undefined.name());
            bannerChooseMedicalInstitution.setTitle("");
            bannerChooseMedicalInstitution.setSubtitle(getString(R.string.choose_nearest_medical_institution));
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
        initBanner(linearLayoutBannerChooseMedicalInstitution, bannerChooseMedicalInstitution, testResult.getId());
        linearLayoutBigWrapper.setVisibility(View.GONE);
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
        final TextView textViewResult = (TextView) view.findViewById(R.id.textViewResult);
        final View relativeLayoutScore = view.findViewById(R.id.relativeLayoutScore);
        final RelativeLayout imageViewResultCircleHolder = (RelativeLayout) view.findViewById(R.id.relativeLayoutResultCircleHolder);
        final RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) imageViewResultCircleHolder.getLayoutParams();

        textViewResult.setText(String.valueOf(testResult.getScorePercents()) + "%");

        int maxX = relativeLayoutScore.getWidth() - (imageViewResultCircleHolder.getWidth() + layoutParams.leftMargin + layoutParams.rightMargin);
        int x = (maxX * testResult.getScorePercents() / 100) + layoutParams.leftMargin;

        RelativeLayout.LayoutParams newLayoutParams = new RelativeLayout.LayoutParams(imageViewResultCircleHolder.getMeasuredWidth(), imageViewResultCircleHolder.getMeasuredWidth());
        newLayoutParams.setMargins(x, layoutParams.topMargin, layoutParams.rightMargin, layoutParams.bottomMargin);
        imageViewResultCircleHolder.setLayoutParams(newLayoutParams);

        // necessary for eliminate visual resizing
        imageViewResultCircleHolder.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        // unregister listener (this is important)
                        imageViewResultCircleHolder.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                        relativeLayoutScore.setVisibility(View.VISIBLE);
                    }
                });
    }

    private void tryToSendResult(final TestResult testResult, final User user, final Token token) {
        View body = getActivity().getLayoutInflater().inflate(R.layout.layout_dialog_send_by_email, null);

        final Button buttonYes = (Button) View.inflate(body.getContext(), R.layout.layout_button_main, null);
        buttonYes.setText(R.string.yes);
        buttonYes.setEnabled(!user.isDoctor());

        OnClickListener onClickListenerYes = new OnClickListener() {
            @Override
            public void onClick(View v) {
                sendByEmail(testResult, user, token);
            }
        };

        if (!user.isDoctor()) {
            EditText editTextEmail = (EditText) body.findViewById(R.id.editTextEmail);
            editTextEmail.setText(user.getEmail());

            editTextEmail.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    buttonYes.setEnabled(s.length() > 0 && Tools.isValidEmail(s.toString()));
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
        }

        CustomDialogLayout customDialogLayout = new CustomDialogLayout
                .Builder(getActivity())
                .setBody(body)
                .addButton(R.string.no, CustomDialogLayout.DialogStandardAction.dismiss)
                .addButton(buttonYes, onClickListenerYes)
                .create();

        AlertDialog alertDialog = new AlertDialog
                .Builder(getActivity())
                .setView(customDialogLayout)
                .create();
        customDialogLayout.setParentDialog(alertDialog);

        alertDialog.show();
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

    private void initBanner(View bannerView, TestBanner bannerData, String testResultId) {
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

        setClickable(bannerView, bannerData, testResultId);
        setImageView(imageViewState, bannerData.getState());
        setTextView(textViewTitle, bannerData.getTitle());
        setTextView(textViewSubtitle, bannerData.getSubtitle());
        setTextView(textViewNote, bannerData.getNote());
    }

    private void setClickable(final View bannerView, final TestBanner bannerData, final String testResultId) {
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
                        showLocalPage(bannerData, testResultId);
                    } else {
                        showRemotePage(bannerData, testResultId);
                    }
                }
            });
        }
    }

    private void showLocalPage(final TestBanner bannerData, String testResultId) {
        SlidingMenuActivity slidingMenuActivity = (SlidingMenuActivity) getActivity();
        if (Url.BANNER_CHOOSE_MEDICAL_INSTITUTION.equals(bannerData.getPageUrl())) {
            // attempt to fix slow initialization of SupportMapFragment ("dirty hack")
            ((ViewGroup) getView()).removeAllViews();
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View layout_loading_map = layoutInflater.inflate(R.layout.layout_loading_map, null);
            ((ViewGroup) getView()).addView(layout_loading_map);

            BaseItemFragment searchInstitutionsFragment = new InstitutionsSearchFragment();
            slidingMenuActivity.replaceAllContent(searchInstitutionsFragment, true);
            slidingMenuActivity.selectCurrentItem(searchInstitutionsFragment);
        } else if (Url.BANNER_PASS_POLL.equals(bannerData.getPageUrl())) {
            BaseItemFragment fragment = new RecommendationsTestResultsFragment();
            slidingMenuActivity.replaceAllContent(fragment, true);
            slidingMenuActivity.selectCurrentItem(fragment);
        }
    }

    private void showRemotePage(final TestBanner bannerData, String testResultId) {
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
        } catch (Exception ex) { /*does nothing*/ }

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