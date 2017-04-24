package com.attendanceproject.attendid;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AttendanceNotificationService extends Service
{
    static boolean firstStart = true;

    public AttendanceNotificationService()
    {
    }

    @Nullable @Override public IBinder onBind( Intent intent )
    {
        return null;
    }

    @Override public void onCreate()
    {
        super.onCreate();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference( "Attendance of SSUET" ).child( "attendance" );

        myRef.addValueEventListener( new ValueEventListener()
        {
            @Override
            public void onDataChange( DataSnapshot dataSnapshot )
            {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

                NotificationCompat.Builder mBuilder =
                        new NotificationCompat.Builder( AttendanceNotificationService.this )
                                .setSmallIcon( R.mipmap.ic_launcher )
                                .setContentTitle( "New Entry" )
                                .setContentText( "Someone logged their attendance click to see details." );


                Intent resultIntent = new Intent( AttendanceNotificationService.this, MainActivity.class );
// Because clicking the notification opens a new ("special") activity, there's
// no need to create an artificial back stack.
                PendingIntent resultPendingIntent =
                        PendingIntent.getActivity(
                                AttendanceNotificationService.this,
                                0,
                                resultIntent,
                                PendingIntent.FLAG_UPDATE_CURRENT
                        );


                mBuilder.setContentIntent( resultPendingIntent );

                int mNotificationId = 001;
// Gets an instance of the NotificationManager service
                NotificationManager mNotifyMgr =
                        (NotificationManager) getSystemService( NOTIFICATION_SERVICE );
// Builds the notification and issues it.

                if ( !firstStart )
                {
                    mNotifyMgr.notify( mNotificationId, mBuilder.build() );
                }
                else
                {
                    firstStart = false;
                }
                /*String value = dataSnapshot.getValue().toString();
                Log.d("Data", "Value is: " + value);*/
            }

            @Override
            public void onCancelled( DatabaseError error )
            {

            }
        } );
    }
}
