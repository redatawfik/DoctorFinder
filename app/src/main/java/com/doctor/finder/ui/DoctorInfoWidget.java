package com.doctor.finder.ui;


import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.widget.RemoteViews;

import com.doctor.finder.R;
import com.doctor.finder.database.AppDatabase;
import com.doctor.finder.database.DoctorEntry;
import com.squareup.picasso.Picasso;

import java.util.List;


public class DoctorInfoWidget extends AppWidgetProvider {

    private static DoctorEntry mDoctorEntry;

    private static Context context;
    private static AppWidgetManager appWidgetManager;
    private static int appWidgetId;

    private void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                 int appWidgetId) {

        this.context = context;
        this.appWidgetManager = appWidgetManager;
        this.appWidgetId = appWidgetId;

        SavedDoctor savedDoctor = new SavedDoctor();
        savedDoctor.execute();

    }

    private static void initViews() {

        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", mDoctorEntry.getLandLineNumber(), null));
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        // Construct the RemoteViews object
        final RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.doctor_info_widget);
        views.setTextViewText(R.id.widget_name, mDoctorEntry.getFirstName() + " " + mDoctorEntry.getLastName());
        views.setTextViewText(R.id.widget_specialty, mDoctorEntry.getTitle() + ", " + mDoctorEntry.getSpecialtyName());
        views.setTextViewText(R.id.widget_location, mDoctorEntry.getState() + ", " + mDoctorEntry.getCity() + ", " + mDoctorEntry.getStreet());
        views.setOnClickPendingIntent(R.id.widget_call_button, pendingIntent);


        Handler uiHandler = new Handler(Looper.getMainLooper());
        uiHandler.post(() -> Picasso.with(context)
                .load(mDoctorEntry.getProfileImage())
                .into(views, R.id.widget_profile_image, new int[]{appWidgetId}));


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

    public static class SavedDoctor
            extends AsyncTask<Void, Void, DoctorEntry> {

        final AppDatabase appDatabase = AppDatabase.getInstance(context.getApplicationContext());

        @Override
        protected DoctorEntry doInBackground(Void... voids) {

            List<DoctorEntry> list = appDatabase.doctorDao().loadDoctorsFoeWidget();
            return list.get(list.size() - 1);
        }

        @Override
        protected void onPostExecute(DoctorEntry doctorEntry) {
            super.onPostExecute(doctorEntry);
            mDoctorEntry = doctorEntry;
            initViews();
        }
    }
}

