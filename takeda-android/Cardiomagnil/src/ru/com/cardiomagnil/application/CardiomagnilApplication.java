package ru.com.cardiomagnil.application;

import ru.com.cardiomagnil.model.Authorization;
import ru.com.cardiomagnil.model.TestResult;
import ru.com.cardiomagnil.model.Token;
import ru.com.cardiomagnil.model.User;
import ru.evilduck.framework.SFApplicationState;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.util.Log;

public class CardiomagnilApplication extends Application {
    private static Context mAppContext = null;
    private Activity mCurrentActivity = null;

    @Override
    public void onCreate() {
        super.onCreate();

        mAppContext = this;
        SFApplicationState.getInstatce().initialize(this);
        initAppState();

        // Log.d("Hash", Tools.getAppKeyHashB64());
        // testTest();
    }

    public static Context getAppContext() {
        return (Context) mAppContext;
    }

    public Activity getCurrentActivity() {
        return mCurrentActivity;
    }

    public void setCurrentActivity(Activity currentActivity) {
        mCurrentActivity = currentActivity;
    }

    private void initAppState() {
        AppSharedPreferences appSharedPreferences = AppSharedPreferences.getInstatce();
        AppState appState = AppState.getInstatce();

        appSharedPreferences.load();

        Token token = new Token(appSharedPreferences.getPreference(AppSharedPreferences.PREFERENCES.token));
        appState.setToken(token);

        // TODO: init user from json
        User user = new User();
        user.setEmail(appSharedPreferences.getPreference(AppSharedPreferences.PREFERENCES.email));
        user.setPlainPasswordFirst(appSharedPreferences.getPreference(AppSharedPreferences.PREFERENCES.plain_password_first));
        user.setPlainPasswordFirst(appSharedPreferences.getPreference(AppSharedPreferences.PREFERENCES.plain_password_second));
        appState.setUser(user);

        Authorization authorization = new Authorization();
        authorization.setClientId(AppConfig.CLIENT_ID);
        authorization.setClientSecret(AppConfig.CLIENT_SECRET);
        authorization.setGrantType(AppConfig.GRANT_TYPE);
        authorization.setUsername(user.getEmail());
        authorization.setPassword(user.getPlainPasswordFirst());
        // TODO: add id field into user
        // authorization.setClientId(user.getId);
        appState.setAuthorization(authorization);
    }

    @SuppressWarnings("unused")
    private String json_1 = "{\"bmi\" : 388.9,\"createdAt\" : \"2014-04-08T23:41:29+0400\",\"id\" : 281,\"scoreValue\" : 20,\"institutionsUrl\" : true,\"scoreDescription\" : {\"state\" : \"attention\",\"text\" : \"Категория высокого риска развития смертельных сердечно-сосудистых заболеваний в ближайшие 10 лет\"},\"dangerAlert\" : {\"state\" : \"attention\",\"text\" : \"Наличие перенесенного инфаркта (инсульта/стентирования коронарных артерий/операции аортокоронарного шунтирования) автоматически приводит к очень высокому риску смерти от сердечно-сосудистых причин.\nРекомендовано наблюдение у кардиолога, соблюдение диеты, регулярный контроль уровня холестерина (общий ХС<4,0, ЛПНП<1,8  ммоль/л), артериального давления (менее 140/90 мм рт. ст.).\n\nДля снижения риска смерти  необходимо постоянно принимать препараты ацетилсалициловой кислоты, препараты снижающие артериальное давление и  статины при отсутствии противопоказаний,\nпроконсультируйтесь у врача о назначении вам этих препаратов.\"},\"mainRecommendation\" : {\"text\" : \"Прием препаратов ацетилсалициловой кислоты является фактором риска развития язвенной болезни желудка.\nОбратите внимание на специализированные препараты, снижающие этот риск, например, кардиомагнил.\"},\"recommendations\" : {\"banners\" : {\"arterialPressure\" : {\"aberration\" : \"большое отклонение\",\"note\" : \"Фактор риска\",\"pageUrl\" : \"/test-results/281/arterialPressure\",\"state\" : \"attention\",\"title\" : \"Систолическое давление\"},\"arterialPressureDrugs\" : {\"aberration\" : null,\"note\" : \"У вас повышено артериальное давление – это опасный фактор риска. Вам необходимо обратиться к врачу.\",\"pageUrl\" : null,\"state\" : \"doctor\",\"title\" : null},\"bmi\" : {\"aberration\" : \"большое отклонение\",\"note\" : \"Необходимо улучшение\",\"pageUrl\" : \"/test-results/281/bmi\",\"state\" : \"attention\",\"title\" : \"Вес\"},\"cholesterolDrugs\" : {\"aberration\" : null,\"note\" : \"У вас отмечается повышение уровня холестерина. Вам необходимо обратиться к врачу.\",\"pageUrl\" : null,\"state\" : \"doctor\",\"title\" : null},\"cholesterolLevel\" : {\"aberration\" : \"фактор риска\",\"note\" : \"Группа риска\",\"pageUrl\" : \"/test-results/281/cholesterolLevel\",\"state\" : \"attention\",\"title\" : \"Уровень холестирина\"},\"extraSalt\" : {\"aberration\" : null,\"note\" : \"Необходимо улучшение\",\"pageUrl\" : \"/test-results/281/extraSalt\",\"state\" : \"attention\",\"title\" : \"Потребление соли\"},\"physicalActivity\" : {\"aberration\" : \"большое отклонение\",\"note\" : \"Необходимо улучшение\",\"pageUrl\" : \"/test-results/281/physicalActivity\",\"state\" : \"attention\",\"title\" : \"Физическая активность\"},\"smoking\" : {\"aberration\" : null,\"note\" : \"Фактор риска\",\"pageUrl\" : \"/test-results/281/smoking\",\"state\" : \"attention\",\"title\" : \"Курение\"},\"sugarProblems\" : null},\"pages\" : {\"arterialPressure\" : {\"institutionsUrl\" : true,\"subtitle\" : \"Нормой является значение до 139\",\"text\" : \"У вас повышено артериальное давление – это опасный фактор риска развития сердечно-сосудистых осложнений, таких как инфаркт миокарда и инсульт.\n\nОбратитесь к врачу для решения вопроса о назначении вам лекарственных препаратов, снижающих артериальное давление.\n\",\"title\" : \"У вас высокое давление - ?\",\"urlText\" : \"Давление\"},\"bmi\" : {\"institutionsUrl\" : false,\"subtitle\" : \"Нормой является значение менее 25 кг/м2\",\"text\" : \"Ваш индекс массы тела превышает 30 кг/м2 – это ожирение. Избыточный вес тела и, особенно, его крайняя степень - ожирение, являются фактором повышенного риска болезней сердца и сосудов.\n\nРекомендовано снижение индекса массы тела менее 25 кг/м2, или по крайней мере, на 10% от исходного  веса.\n\nСнижение веса возможно за счет уменьшения калорийности питания и увеличения физической активности. Существуют медикаментозные препараты разного механизма действия, но они имеют нежелательные побочные явления и должны применяться только под строгим контролем врача.\n\",\"title\" : \"У вас высокий индекс массы тела - ? кг/м2\",\"urlText\" : \"Вес\"},\"cholesterolLevel\" : {\"institutionsUrl\" : true,\"subtitle\" : \"Нормой является значение до 4,9 ммоль/л\",\"text\" : \"У вас повышен уровень холестерина (более 4,9 ммоль/л) - это фактор риска сердечно-сосудистых заболеваний. Соблюдайте диету с ограничением животных жиров (кроме рыбы) и трансжиров.\n\nЗамените большую часть животных жиров растительными маслами, увеличьте количество овощей и фруктов.\n\nЖивотные жиры способствуют атеросклерозу сосудов, приводящему к инфаркту и инсульту, а растительные жиры (кроме кокосового и пальмового масла) и рыбий жир противодействуют атеросклерозу.\n\",\"title\" : \"У вас высокий уровень холестерина - ? ммоль/л\",\"urlText\" : \"Холестерин\"},\"extraSalt\" : {\"institutionsUrl\" : false,\"subtitle\" : null,\"text\" : \"Потребление соли может приводить к повышению артериального давления. Ограничьте потребление соли и продуктов, богатых натрием.\n\nСтарайтесь не досаливать пищу.\nДля улучшения вкусовых качеств пищи используйте различные травы, специи, лимонный сок, чеснок.\n\",\"title\" : \"Потребление соли может приводить к повышению артериального давления.\",\"urlText\" : \"Соль\"},\"physicalActivity\" : {\"institutionsUrl\" : false,\"subtitle\" : \"Нормой является значение от 150 минут в неделю\",\"text\" : \"Физическая активность является средством сохранения здоровья сердца и сосудов.\n\nЖелательно предпочесть аэробную динамическую нагрузку 5 раз в неделю от 30 мин. до 2 ч. в день.\nНагрузка  рекомендуется такая, чтобы частота пульса достигла 65-70% от максимальной частоты пульса для данного возраста.\n\nМаксимальная частота пульса рассчитывается по формуле: 220 минус возраст в годах.\n\nБольным с заболеваниями сердца и сосудов режим нагрузок подбирается врачом индивидуально в соответствии с результатами электрокардиографического теста с физической нагрузкой.\n\",\"title\" : \"У вас недостаточно физической активности ? минут в неделю\",\"urlText\" : \"Физическая активность\"},\"smoking\" : {\"institutionsUrl\" : false,\"subtitle\" : \"Вам необходимо бросить курить.\",\"text\" : \"Курение повышает риск смерти в 3-6 раз от онкологических, сердечно-сосудистых заболеваний, дыхательной системы, системы пищеварения.\n\nЧерез 5 лет после отказа от курения риск инсульта и инфаркта становится таким же, как у никогда не куривших, повышается потенция у мужчин, репродуктивная функция у женщин, на половину снижается риск рака.\n\nМножество способов отказа от курения самостоятельно, с помощью врача, гипноз, лекарства.\nОбратитесь к врачу за помощью для назначения лекарственной поддержки и снижения симптомов отмены, следуйте его советам.\n\",\"title\" : \"Курение повышает риск смерти в 3-6 раз\",\"urlText\" : \"Курение\"}}}}";
    private String json_2 = "{\"bmi\":27.8,\"scoreValue\":0,\"recommendations\":{\"scoreDescription\":{\"state\":\"ok\",\"text\":\"Категория низкого риска развития смертельных сердечно-сосудистых заболеваний в ближайшие 10 лет\"},\"dangerAlert\":null,\"mainRecommendation\":{\"text\":\"Обратитесь к врачу для консультации о необходимости приема препаратов ацетилсалициловой кислоты для снижения риска тромбозов.\n\"},\"institutionsUrl\":false,\"banners\":{\"smoking\":{\"pageUrl\":null,\"state\":\"ok\",\"title\":\"Курение\",\"aberration\":null,\"note\":\"Все хорошо\"},\"arterialPressure\":{\"pageUrl\":null,\"state\":\"ok\",\"title\":\"Систолическое давление\",\"aberration\":\"близко к норме\",\"note\":\"Все хорошо\"},\"extraSalt\":{\"pageUrl\":null,\"state\":\"ok\",\"title\":\"Потребление соли\",\"aberration\":null,\"note\":\"Все хорошо\"},\"cholesterolLevel\":{\"pageUrl\":null,\"state\":\"ok\",\"title\":\"Уровень холестирина\",\"aberration\":\"близко к норме\",\"note\":\"Все хорошо\"},\"physicalActivity\":{\"pageUrl\":\"/test-results/168/physicalActivity\",\"state\":\"attention\",\"title\":\"Физическая активность\",\"aberration\":\"большое отклонение\",\"note\":\"Необходимо улучшение\"},\"bmi\":{\"pageUrl\":\"/test-results/168/bmi\",\"state\":\"bell\",\"title\":\"Вес\",\"aberration\":\"среднее отклонение\",\"note\":\"Необходимо улучшение\"},\"sugarProblems\":null,\"arterialPressureDrugs\":null,\"cholesterolDrugs\":null},\"pages\":{\"smoking\":null,\"arterialPressure\":null,\"extraSalt\":null,\"cholesterolLevel\":null,\"physicalActivity\":{\"urlText\":\"Физическая активность\",\"title\":\"У вас недостаточно физической активности 80 минут в неделю\",\"subtitle\":\"Нормой является значение от 150 минут в неделю\",\"institutionsUrl\":false,\"text\":\"Физическая активность является средством сохранения здоровья сердца и сосудов.\n\nЖелательно предпочесть аэробную динамическую нагрузку 5 раз в неделю от 30 мин. до 2 ч. в день.\nНагрузка  рекомендуется такая, чтобы частота пульса достигла 65-70% от максимальной частоты пульса для данного возраста.\n\nМаксимальная частота пульса рассчитывается по формуле: 220 минус возраст в годах.\n\nБольным с заболеваниями сердца и сосудов режим нагрузок подбирается врачом индивидуально в соответствии с результатами электрокардиографического теста с физической нагрузкой.\n\"},\"bmi\":{\"urlText\":\"Вес\",\"title\":\"У вас высокий индекс массы тела - 27.8 кг/м2\",\"subtitle\":\"Нормой является значение менее 25 кг/м2\",\"institutionsUrl\":false,\"text\":\"Ваш индекс массы тела превышает 25 кг/м2 – это избыточный вес. Избыточный вес тела является фактором повышенного риска болезней сердца и сосудов.\n\nСнижение веса возможно за счет уменьшения калорийности питания и увеличения физической активности. Существуют медикаментозные препараты разного механизма действия, но они имеют нежелательные побочные явления и должны применяться только под строгим контролем врача.\"}}},\"id\":168,\"sex\":\"male\",\"birthday\":\"2003-04-10T00:00:00+0400\",\"growth\":180,\"weight\":90,\"smoking\":false,\"cholesterolLevel\":3,\"cholesterolDrugs\":false,\"diabetes\":false,\"sugarProblems\":false,\"sugarDrugs\":false,\"arterialPressure\":120,\"arterialPressureDrugs\":false,\"physicalActivity\":80,\"heartAttackOrStroke\":false,\"extraSalt\":false,\"acetylsalicylicDrugs\":false,\"createdAt\":\"2014-04-10T02:00:48+0400\"}";

    @SuppressWarnings("unused")
    private void testTest() {
        TestResult testResult = new TestResult(json_2);
        AppState.getInstatce().setTestResult(testResult);
    }
}
