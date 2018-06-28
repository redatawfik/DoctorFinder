package com.doctor.finder.ui;


import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.widget.RemoteViews;

import com.doctor.finder.R;
import com.doctor.finder.database.AppDatabase;
import com.doctor.finder.database.AppExecutors;
import com.doctor.finder.database.DoctorEntry;
import com.squareup.picasso.Picasso;

import java.util.List;


/**
 * Implementation of App Widget functionality.
 */
public class DoctorInfoWidget extends AppWidgetProvider {

    DoctorEntry doctorEntry;

    Context context;
    AppWidgetManager appWidgetManager;
    int appWidgetId;

    void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                         int appWidgetId) {

        this.context = context;
        this.appWidgetManager = appWidgetManager;
        this.appWidgetId = appWidgetId;

        getDoctor(context);

    }

    void initViews() {

        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", doctorEntry.getLandLineNumber(), null));
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        // Construct the RemoteViews object
        final RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.doctor_info_widget);
        views.setTextViewText(R.id.widget_name, doctorEntry.getFirstName() + " " + doctorEntry.getLastName());
        views.setTextViewText(R.id.widget_specialty, doctorEntry.getTitle() + ", " + doctorEntry.getSpecialtyName());
        views.setTextViewText(R.id.widget_location, doctorEntry.getState() + ", " + doctorEntry.getCity() + ", " + doctorEntry.getStreet());
        views.setOnClickPendingIntent(R.id.widget_call_button, pendingIntent);


        Handler uiHandler = new Handler(Looper.getMainLooper());
        uiHandler.post(() -> {
            Picasso.with(context)
                    .load(doctorEntry.getProfileImage())
                    .into(views, R.id.widget_profile_image, new int[]{appWidgetId});

        });


        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);

    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    private void getDoctor(Context context) {

        final AppDatabase appDatabase = AppDatabase.getInstance(context.getApplicationContext());
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                List<DoctorEntry> list = appDatabase.doctorDao().loadDoctorsFoeWidget();
                doctorEntry = list.get(list.size() - 1);
                initViews();
            }
        });
    }


}

