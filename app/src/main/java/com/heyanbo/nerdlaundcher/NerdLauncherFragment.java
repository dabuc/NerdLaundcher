package com.heyanbo.nerdlaundcher;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


import com.heyanbo.nerdlaundcher.dummy.DummyContent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnFragmentInteractionListener}
 * interface.
 */
public class NerdLauncherFragment extends ListFragment {

    private static final String TAG = "NerdLauncherFragment";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent startupIntent = new Intent(Intent.ACTION_MAIN);
        startupIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        final PackageManager pm = getActivity().getPackageManager();
        List<ResolveInfo> activities = pm.queryIntentActivities(startupIntent, 0);
        Log.i(TAG, "I've found " + activities.size() + " activities.");

        Collections.sort(activities, new Comparator<ResolveInfo>() {
            @Override
            public int compare(ResolveInfo lhs, ResolveInfo rhs) {
                PackageManager pm = getActivity().getPackageManager();
                return String.CASE_INSENSITIVE_ORDER.compare(
                        lhs.loadLabel(pm).toString(),
                        rhs.loadLabel(pm).toString());
            }
        });

        ArrayAdapter<ResolveInfo> adapter = new ArrayAdapter<ResolveInfo>(getActivity(), android.R.layout.simple_list_item_1, activities) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
//                View v= super.getView(position, convertView, parent);
//                TextView tv= (TextView) v;
//                ResolveInfo ri=getItem(position);
//                tv.setText(ri.loadLabel(pm));

                if (convertView == null) {
                    convertView = getActivity().getLayoutInflater().inflate(R.layout.list_item_activity, null);
                }
                TextView tv = (TextView) convertView.findViewById(R.id.activity_list_item_text);
                ResolveInfo ri = getItem(position);
                tv.setText(ri.loadLabel(pm));
                ImageView imageView = (ImageView) convertView.findViewById(R.id.activity_list_item_Icon);
                imageView.setImageDrawable(ri.loadIcon(pm));
                return convertView;
            }
        };

        setListAdapter(adapter);

    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        ResolveInfo resolveInfo = (ResolveInfo) l.getAdapter().getItem(position);
        ActivityInfo activityInfo = resolveInfo.activityInfo;
        if (activityInfo == null) return;
        Intent i = new Intent(Intent.ACTION_MAIN);
        i.setClassName(activityInfo.applicationInfo.packageName, activityInfo.name);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }
}
