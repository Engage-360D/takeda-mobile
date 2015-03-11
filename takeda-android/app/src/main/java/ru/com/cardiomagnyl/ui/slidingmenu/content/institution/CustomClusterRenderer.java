package ru.com.cardiomagnyl.ui.slidingmenu.content.institution;

import android.content.Context;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

import ru.com.cardiomagnyl.app.R;
import ru.com.cardiomagnyl.model.institution.Institution;

public class CustomClusterRenderer extends DefaultClusterRenderer<Institution> {
    public CustomClusterRenderer(Context context, GoogleMap googleMap, ClusterManager clusterManager) {
        super(context, googleMap, clusterManager);
    }

    @Override
    protected void onBeforeClusterItemRendered(Institution institution, MarkerOptions markerOptions) {
        markerOptions
                .title(institution.getName())
                .snippet(institution.getId()) /*hack*/
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_ic));

        super.onBeforeClusterItemRendered(institution, markerOptions);
    }

    @Override
    protected void onBeforeClusterRendered(Cluster<Institution> cluster, MarkerOptions markerOptions) {
        super.onBeforeClusterRendered(cluster, markerOptions);
    }

    @Override
    protected boolean shouldRenderAsCluster(Cluster cluster) {
        return cluster.getSize() > 3;
    }

}

