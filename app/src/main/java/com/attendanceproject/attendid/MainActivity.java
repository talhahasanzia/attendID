package com.attendanceproject.attendid;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
{

    ArrayList<User> users;
    ListView listView;

    @Override
    protected void onCreate( Bundle savedInstanceState )
    {


        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );
        listView = (ListView) findViewById( R.id.list_view );


    }


    @Override protected void onResume()
    {
        super.onResume();


        startService( new Intent( this, AttendanceNotificationService.class ) );

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference( "Attendance of SSUET" ).child( "attendance" );




        myRef.addValueEventListener( new ValueEventListener()
        {
            @Override
            public void onDataChange( DataSnapshot dataSnapshot )
            {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                users = new ArrayList<>();


                String value = dataSnapshot.getValue().toString();

                Log.d( "Data", "Count: " + dataSnapshot.getChildrenCount() );

                if ( value != null )
                {
                    Log.d( "Data", "Value is: " + value );
                    for ( DataSnapshot dataSnapshotChild : dataSnapshot.getChildren() )
                    {


                        User user = dataSnapshotChild.getValue( User.class );

                        // logic to eliminate repetitions

                        ArrayList<User> tusers = new ArrayList<User>( users );
                        int i = 0;
                        for ( User tuser : tusers )
                        {

                            if ( tuser.value.equals( user.value ) )
                            {

                                users.remove( i );

                            }
                            i++;
                        }

                        users.add( user );

                        Log.d( "Data", user.value );


                    }

                    CustomListAdapter customListAdapter = new CustomListAdapter( MainActivity.this, R.layout.list_view_item,
                            R.id.value, users );

                    listView.setAdapter( customListAdapter );

                    customListAdapter.notifyDataSetChanged();

                }
                else
                {

                    Toast.makeText( MainActivity.this, "Data fetching error.", Toast.LENGTH_SHORT ).show();

                }


            }

            @Override
            public void onCancelled( DatabaseError error )
            {

            }
        } );
    }

    public class CustomListAdapter extends ArrayAdapter<User>
    {
        ArrayList<User> users;

        public CustomListAdapter( Context context, int resource, int textViewResourceId, ArrayList<User> objects )
        {
            super( context, resource, textViewResourceId, objects );

            this.users=objects;
        }

        @NonNull @Override public View getView( int position, View convertView, ViewGroup parent )
        {


            User user = this.users.get( position );

            // Check if an existing view is being reused, otherwise inflate the view

            if ( convertView == null )
            {

                convertView = LayoutInflater.from( getContext() ).inflate( R.layout.list_view_item, parent, false );

            }

            // Lookup view for data population

            TextView tvName = (TextView) convertView.findViewById( R.id.value );

            TextView tvDate = (TextView) convertView.findViewById( R.id.time_date );

            // Populate the data into the template view using the data object

            tvName.setText( user.value );

            tvDate.setText( user.time+"  - "+user .date);

            // Return the completed view to render on screen

            return convertView;
        }


    }
}
