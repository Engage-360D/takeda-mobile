package ru.com.cardiomagnyl.ui.slidingmenu.content.recommendations;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import ru.com.cardiomagnyl.api.Url;
import ru.com.cardiomagnyl.app.R;
import ru.com.cardiomagnyl.application.AppState;
import ru.com.cardiomagnyl.model.common.Response;
import ru.com.cardiomagnyl.model.test_diet.TestDiet;
import ru.com.cardiomagnyl.model.test_diet.TestDietDao;
import ru.com.cardiomagnyl.model.test_diet.TestDietResult;
import ru.com.cardiomagnyl.model.test_diet.TestDietResultDao;
import ru.com.cardiomagnyl.model.test_diet_answer.TestDietAnswer;
import ru.com.cardiomagnyl.model.token.Token;
import ru.com.cardiomagnyl.ui.base.BaseItemFragment;
import ru.com.cardiomagnyl.ui.slidingmenu.menu.SlidingMenuActivity;
import ru.com.cardiomagnyl.util.CallbackOne;
import ru.com.cardiomagnyl.util.Tools;
import ru.com.cardiomagnyl.widget.CustomDialogs;

public class RecommendationsTestFragment extends BaseItemFragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recommendations_test, null);
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

        getDietTest(fragmentView);
    }

    public void getDietTest(final View fragmentView) {
        String testResultId = AppState.getInsnatce().getTestResult().getId();
        Token token = AppState.getInsnatce().getToken();
        TestDietDao.getTestDietByTestId(
                testResultId,
                token,
                new CallbackOne<List<TestDiet>>() {
                    @Override
                    public void execute(List<TestDiet> testDietList) {
                        initFragmentFinish(fragmentView, testDietList);
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

    private void initFragmentFinish(final View fragmentView, final List<TestDiet> testDietList) {
        final View fragmentContent = fragmentView.findViewById(R.id.fragmentContent);
        final View textViewMessage = fragmentView.findViewById(R.id.textViewMessage);

        SlidingMenuActivity slidingMenuActivity = (SlidingMenuActivity) getActivity();
        slidingMenuActivity.hideProgressDialog();

        if (testDietList == null || testDietList.isEmpty()) {
            fragmentContent.setVisibility(View.GONE);
            textViewMessage.setVisibility(View.VISIBLE);
        } else {
            textViewMessage.setVisibility(View.GONE);
            fragmentContent.setVisibility(View.VISIBLE);
            initFragmentFinishHelper(fragmentView, testDietList);
        }
    }

    private void initFragmentFinishHelper(final View fragmentView, final List<TestDiet> testDietList) {
        initRadioButtons(fragmentView, testDietList);
        initButtonGetRecommendations(fragmentView);
    }

    private void initRadioButtons(final View fragmentView, final List<TestDiet> testDietList) {
        LinearLayout linearLayoutContentHolder = (LinearLayout) fragmentView.findViewById(R.id.linearLayoutContentHolder);

        for (TestDiet testDiet : testDietList) {
            final LinearLayout layoutRadiogroupTest = (LinearLayout) fragmentView.inflate(fragmentView.getContext(), R.layout.layout_radiogroup_test, null);
            final TextView textViewHeader = (TextView) layoutRadiogroupTest.findViewById(R.id.textViewHeader);
            final RadioGroup radioGroupCondition = (RadioGroup) layoutRadiogroupTest.findViewById(R.id.radioGroupCondition);

            textViewHeader.setText(testDiet.getQuestion());

            for (TestDietAnswer testDietAnswer : testDiet.getAnswers()) {
                RadioButton radioButton = (RadioButton) fragmentView.inflate(fragmentView.getContext(), R.layout.layout_radiobutton_test, null);
                radioButton.setText(testDietAnswer.getAnswer());
                radioButton.setLayoutParams(new RadioGroup.LayoutParams(0, RadioGroup.LayoutParams.MATCH_PARENT, 1f));
                radioButton.setTag(new Pair<>(testDiet.getId(), testDietAnswer.getId()));
                radioGroupCondition.addView(radioButton);
            }
            radioGroupCondition.setWeightSum(testDiet.getAnswers().size());

            radioGroupCondition.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    RadioButton radioButton = (RadioButton) fragmentView.findViewById(checkedId);
                    if (radioButton.isChecked()) layoutRadiogroupTest.setTag(radioButton.getTag());
                }
            });

            linearLayoutContentHolder.addView(layoutRadiogroupTest);
        }
    }

    private void initButtonGetRecommendations(final View fragmentView) {
        Button buttonGetRecommendations = (Button) fragmentView.findViewById(R.id.buttonGetRecommendations);
        buttonGetRecommendations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String answers = getAnswers(fragmentView);
                if (TextUtils.isEmpty(answers))
                    CustomDialogs.showAlertDialog(getActivity(), R.string.complete_all_fields);
                else tryToSendTest(answers);
            }
        });
    }

    private void tryToSendTest(final String testDietSource) {
        final SlidingMenuActivity slidingMenuActivity = (SlidingMenuActivity) getActivity();
        slidingMenuActivity.showProgressDialog();

        String testResultId = AppState.getInsnatce().getTestResult().getId();
        Token token = AppState.getInsnatce().getToken();
        TestDietResultDao.sendTestDietSource(
                testDietSource,
                testResultId,
                token,
                new CallbackOne<TestDietResult>() {
                    @Override
                    public void execute(TestDietResult testDietResult) {
                        handleTestResult(testDietResult, null);
                    }
                },
                new CallbackOne<Response>() {
                    @Override
                    public void execute(Response responseError) {
                        handleTestResult(null, responseError);
                    }
                }
        );
    }

    private void handleTestResult(TestDietResult testDietResult, Response responseError) {
        SlidingMenuActivity slidingMenuActivity = (SlidingMenuActivity) getActivity();
        slidingMenuActivity.hideProgressDialog();

        if (testDietResult == null) {
            responseError = (responseError == null || responseError.getError() == null) ?
                    new Response.Builder(new ru.com.cardiomagnyl.model.common.Error()).create() :
                    responseError;

            switch (responseError.getError().getCode()) {
                default:
                    Tools.showToast(getActivity(), R.string.error_occurred, Toast.LENGTH_LONG);
            }
        } else {
            AppState.getInsnatce().setTestDietResult(testDietResult);
            slidingMenuActivity.replaceContentOnTop(new RecommendationsTestResultsFragment(), false);
        }

        // set up menu items according to testResult
        slidingMenuActivity.refreshMenuItems();
    }

    private String getAnswers(final View fragmentView) {
        String answers = "";

        LinearLayout linearLayoutContentHolder = (LinearLayout) fragmentView.findViewById(R.id.linearLayoutContentHolder);
        for (int counter = 0; counter < linearLayoutContentHolder.getChildCount(); ++counter) {
            View child = linearLayoutContentHolder.getChildAt(counter);
            if (child.getTag() != null) {
                Pair<String, String> answer = (Pair<String, String>) child.getTag();
                answers = answers.concat(String.format(Url.ACCOUNT_TEST_RESULTS_DIET_ANSWERS, answer.first, answer.second));
            } else return "";
        }

        return answers;
    }

}

