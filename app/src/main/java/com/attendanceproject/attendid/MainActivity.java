package com.attendanceproject.attendid;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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


        getSupportActionBar().setDisplayUseLogoEnabled(true);

        getSupportActionBar().setDisplayUseLogoEnabled( true );

        getSupportActionBar().setHomeButtonEnabled( true );


        getSupportActionBar().setLogo( R.mipmap.ic_launcher );


    }


    @Override protected void onResume()
    {
        super.onResume();


        startService( new Intent( this, AttendanceNotificationService.class ) );

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference( "Attendance of SSUET" ).child( "attendance" );

        final ProgressDialog progressDialog=ProgressDialog.show( this,"Please wait","Getting data ready." );
        progressDialog.show();




        if ( !isOnline() )
        {

            progressDialog.cancel();
            Toast.makeText( this, "No network connection, turn on network and restart the app.", Toast.LENGTH_LONG ).show();
        }


        myRef.addValueEventListener( new ValueEventListener()
        {
            @Override
            public void onDataChange( DataSnapshot dataSnapshot )
            {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

               progressDialog.cancel();

                users = new ArrayList<>();


                ArrayList<ListUser> userList=new ArrayList<ListUser>(  );

                String value = dataSnapshot.getValue().toString();


                if ( value != null )
                {

                    for ( DataSnapshot dataSnapshotChild : dataSnapshot.getChildren() )
                    {


                        User user = dataSnapshotChild.getValue( User.class );

                        // logic to eliminate repetitions

                        ArrayList<User> tusers = new ArrayList<User>( users );
                        int i = 0;
                        for ( User tuser : tusers )
                        {

                            if ( tuser.value.equals( user.value ) && tuser.status.equals(  user.status) )
                            {

                                users.remove( i );

                            }
                            i++;
                        }

                        users.add( user );






                    }


                    User[] usersArray=users.toArray(new User[users.size()]);




                    for(int count=0; count<usersArray.length; count++)
                    {


                        User tempUser1=usersArray[count];
                        User tempUser2;


                        try
                        {
                            if ( tempUser1.value.equals(usersArray[count+1].value) );
                            {

                                tempUser2=usersArray[count+1];


                            }


                            if(tempUser1!=null && tempUser2!=null)
                            {

                                count++;

                                ListUser listUser=new ListUser( tempUser1, tempUser2 );


                                listUser.timeDifference=listUser.getTimeDifference();

                                listUser.value=tempUser1.value;


                                listUser.date=tempUser1.date;


                                listUser.status=tempUser2.status;

                                userList.add( listUser );

                            }

                            else
                            {

                                ListUser listUser=new ListUser(  );


                                listUser.timeDifference="";

                                listUser.value=tempUser1.value;


                                listUser.date=tempUser1.date;


                                listUser.status=tempUser1.status;

                                userList.add( listUser );

                            }

                        }
                        catch ( IndexOutOfBoundsException iob )
                        {
                            ListUser listUser=new ListUser(  );


                            listUser.timeDifference="";

                            listUser.value=tempUser1.value;


                            listUser.date=tempUser1.date;


                            listUser.status=tempUser1.status;

                            userList.add( listUser );


                        }



                    }


                    if(userList.size()>0)
                    {
                        for ( ListUser tempUserFromList : userList )
                        {
                           Log.d( "USER_LIST", "TimeDiff: "+ tempUserFromList.timeDifference+" - Status:"+tempUserFromList.status+
                           " - Value: "+tempUserFromList.value);
                        }

                    }
                    CustomListAdapter customListAdapter = new CustomListAdapter( MainActivity.this, R.layout.list_view_item,
                            R.id.value, userList );

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

                progressDialog.cancel();
            }
        } );
    }

    public class CustomListAdapter extends ArrayAdapter<ListUser>
    {
        ArrayList<ListUser> users;


        public CustomListAdapter( Context context, int resource, int textViewResourceId, ArrayList<ListUser> objects )
        {
            super( context, resource, textViewResourceId, objects );

            this.users=objects;
        }

        @NonNull @Override public View getView( int position, View convertView, ViewGroup parent )
        {


            ListUser user = this.users.get( position );

            // Check if an existing view is being reused, otherwise inflate the view

            if ( convertView == null )
            {

                convertView = LayoutInflater.from( getContext() ).inflate( R.layout.list_view_item, parent, false );

            }

            // Lookup view for data population

            TextView tvDate = (TextView) convertView.findViewById( R.id.date );

            TextView tvName = (TextView) convertView.findViewById( R.id.value );

            TextView tvTimeIn = (TextView) convertView.findViewById( R.id.time_in );

            TextView tvTimeOut = (TextView) convertView.findViewById( R.id.time_out );


            TextView total=(TextView) convertView.findViewById( R.id.total );

            // Populate the data into the template view using the data object

            tvName.setText( user.value );

            tvDate.setText( user.date);


            tvTimeIn.setText( user.entryObject.time );


            tvTimeOut.setText( user.exitObject.time );


            total.setText( user.getTimeDifference() );

            // Return the completed view to render on screen

            return convertView;
        }


    }


    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
