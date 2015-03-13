package ru.com.cardiomagnyl.ui.slidingmenu.content;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.com.cardiomagnyl.api.Url;
import ru.com.cardiomagnyl.app.R;
import ru.com.cardiomagnyl.ui.base.BaseItemFragment;
import ru.com.cardiomagnyl.ui.base.ExecutableFragment;

public class ReportsFragment extends BaseItemFragment implements ExecutableFragment {

    @Override
    public void initTopBar(ViewGroup viewGroupTopBar) { /*does nothing*/ }

    @Override
    public void execute(Context context) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Url.ACCOUNT_REPORTS));
        context.startActivity(browserIntent);
    }

    @Override
    public boolean isShowable() {
        return false;
    }

}
