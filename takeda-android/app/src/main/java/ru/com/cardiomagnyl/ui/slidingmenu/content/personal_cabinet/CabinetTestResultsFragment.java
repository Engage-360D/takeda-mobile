package ru.com.cardiomagnyl.ui.slidingmenu.content.personal_cabinet;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import ru.com.cardiomagnyl.app.R;
import ru.com.cardiomagnyl.application.AppState;
import ru.com.cardiomagnyl.model.common.Response;
import ru.com.cardiomagnyl.model.test.TestBanner;
import ru.com.cardiomagnyl.model.test.TestNote;
import ru.com.cardiomagnyl.model.test_diet.TestDietRecomendation;
import ru.com.cardiomagnyl.model.test_diet.TestDietResult;
import ru.com.cardiomagnyl.model.test_diet.TestDietResultDao;
import ru.com.cardiomagnyl.ui.base.BaseItemFragment;
import ru.com.cardiomagnyl.ui.slidingmenu.menu.SlidingMenuActivity;
import ru.com.cardiomagnyl.util.CallbackOne;

public class CabinetTestResultsFragment extends BaseItemFragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cabinet_test_results, null);
        initFragmentStart(view);
        return view;
    }

    @Override
    public void initTopBar(ViewGroup viewGroupTopBar) {
        initTopBarBellCabinet(viewGroupTopBar, true, true);
    }

    protected void initFragmentStart(final View fragmentView) {
        fragmentView.findViewById(R.id.fragmentContent).setVisibility(View.INVISIBLE);
        fragmentView.findViewById(R.id.textViewMessage).setVisibility(View.INVISIBLE);

        SlidingMenuActivity slidingMenuActivity = (SlidingMenuActivity) getActivity();
        slidingMenuActivity.showProgressDialog();

        getDietTestResult(fragmentView);
    }

    public void getDietTestResult(final View fragmentView) {
        String testResultId = AppState.getInsnatce().getTestResult().getId();
        TestDietResultDao.getByTestId(
                testResultId,
                new CallbackOne<TestDietResult>() {
                    @Override
                    public void execute(TestDietResult testDietResult) {
                        initFragmentFinish(fragmentView, testDietResult);
                    }
                },
                new CallbackOne<Response>() {
                    @Override
                    public void execute(Response responseError) {
                        initFragmentFinish(fragmentView, null);
                    }
                }
        );
    }

    private void initFragmentFinish(final View fragmentView, final TestDietResult testDietResult) {
        final View fragmentContent = fragmentView.findViewById(R.id.fragmentContent);
        final View textViewMessage = fragmentView.findViewById(R.id.textViewMessage);

        SlidingMenuActivity slidingMenuActivity = (SlidingMenuActivity) getActivity();
        slidingMenuActivity.hideProgressDialog();

        if (testDietResult == null) {
            fragmentContent.setVisibility(View.GONE);
            textViewMessage.setVisibility(View.VISIBLE);

            slidingMenuActivity.replaceContentOnTop(new CabinetTestFragment(), false);
        } else {
            textViewMessage.setVisibility(View.GONE);
            fragmentContent.setVisibility(View.VISIBLE);
            initFragmentFinishHelper(fragmentView, testDietResult);
        }
    }

    private void initFragmentFinishHelper(final View fragmentView, final TestDietResult testDietResult) {
        initPiecesHolder(fragmentView, testDietResult);
        initRedHolder(fragmentView, testDietResult);
        initBlueHolder(fragmentView, testDietResult);
    }

    private void initPiecesHolder(final View fragmentView, final TestDietResult testDietResult) {
        LinearLayout linearLayoutPiecesHolder = (LinearLayout) fragmentView.findViewById(R.id.linearLayoutPiecesHolder);

        if (testDietResult.getMessages() == null || testDietResult.getMessages().isEmpty()) {
            linearLayoutPiecesHolder.setVisibility(View.GONE);
            return;
        }

        for (String message : testDietResult.getMessages()) {
            LinearLayout layoutPiece = (LinearLayout) fragmentView.inflate(fragmentView.getContext(), R.layout.layout_piece, null);

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            int spaceMedium = (int) fragmentView.getResources().getDimension(R.dimen.space_medium);
            layoutParams.setMargins(0, 0, 0, spaceMedium);
            layoutPiece.setLayoutParams(layoutParams);

            TestNote testNote = new TestNote();
            testNote.setText(message);
            initPeace(layoutPiece, testNote);

            linearLayoutPiecesHolder.addView(layoutPiece);
        }
    }

    private void initRedHolder(final View fragmentView, final TestDietResult testDietResult) {
        LinearLayout linearLayoutRedHolder = (LinearLayout) fragmentView.findViewById(R.id.linearLayoutRedHolder);

        if (testDietResult.getRed() == null || testDietResult.getRed().isEmpty()) {
            linearLayoutRedHolder.setVisibility(View.GONE);
            return;
        }

        for (TestDietRecomendation testDietRecomendation : testDietResult.getRed()) {
            LinearLayout layoutBanner = (LinearLayout) fragmentView.inflate(fragmentView.getContext(), R.layout.layout_banner, null);

            TestBanner testBanner = new TestBanner();
            testBanner.setTitle(testDietRecomendation.getTitle());
            testBanner.setSubtitle(testDietRecomendation.getNote());
            testBanner.setPageUrl(null);
            initBanner(layoutBanner, testBanner);

            linearLayoutRedHolder.addView(layoutBanner);
        }
    }

    private void initBlueHolder(final View fragmentView, final TestDietResult testDietResult) {
        LinearLayout linearLayoutBlueHolder = (LinearLayout) fragmentView.findViewById(R.id.linearLayoutBlueHolder);

        if (testDietResult.getBlue() == null || testDietResult.getBlue().isEmpty()) {
            linearLayoutBlueHolder.setVisibility(View.GONE);
            return;
        }

        for (TestDietRecomendation testDietRecomendation : testDietResult.getBlue()) {
            LinearLayout layoutBanner = (LinearLayout) fragmentView.inflate(fragmentView.getContext(), R.layout.layout_banner, null);

            TestBanner testBanner = new TestBanner();
            testBanner.setTitle(testDietRecomendation.getText());
            testBanner.setPageUrl(null);
            initBanner(layoutBanner, testBanner);

            linearLayoutBlueHolder.addView(layoutBanner);
        }
    }

    // similar method is in RiskAnalysisResultsFragment
    private void initPeace(View pieceView, TestNote testNote) {
        ImageView imageViewState = (ImageView) pieceView.findViewById(R.id.imageViewState);
        TextView textViewText = (TextView) pieceView.findViewById(R.id.textViewText);

        if (TestNote.isEmpty(testNote)) {
            pieceView.setVisibility(View.GONE);
            return;
        }

        pieceView.setVisibility(View.VISIBLE);

        imageViewState.setVisibility(View.GONE);
        setTextView(textViewText, testNote.getText(), getResources().getColor(R.color.text_main_37));
    }

    // similar method is in RiskAnalysisResultsFragment
    private void setTextView(TextView textView, String text, int color) {
        if (TextUtils.isEmpty(text)) {
            // textView.setVisibility(View.INVISIBLE);
            textView.setHeight(0);
        } else {
            textView.setText(text);
            textView.setTextColor(color);
        }
    }

    // similar method is in RiskAnalysisResultsFragment
    private void initBanner(View bannerView, TestBanner bannerData) {
        ImageView imageViewState = (ImageView) bannerView.findViewById(R.id.imageViewState);
        TextView textViewTitle = (TextView) bannerView.findViewById(R.id.textViewTitle);
        TextView textViewSubtitle = (TextView) bannerView.findViewById(R.id.textViewSubtitle);
        TextView textViewNote = (TextView) bannerView.findViewById(R.id.textViewNote);
        ImageView imageViewRight = (ImageView) bannerView.findViewById(R.id.imageViewRight);

        if (bannerData == null
                || ((TextUtils.isEmpty(bannerData.getState()) &&
                TextUtils.isEmpty(bannerData.getTitle()) &&
                TextUtils.isEmpty(bannerData.getSubtitle()) &&
                TextUtils.isEmpty(bannerData.getNote())))) {
            bannerView.setVisibility(View.GONE);
            return;
        }

        bannerView.setClickable(false);
        imageViewState.setVisibility(View.GONE);
        setTextView(textViewTitle, bannerData.getTitle(), getResources().getColor(R.color.white));
        setTextView(textViewSubtitle, bannerData.getSubtitle(), getResources().getColor(R.color.white));
        setTextView(textViewNote, bannerData.getNote(), getResources().getColor(R.color.white));
        imageViewRight.setVisibility(View.GONE);
    }

}