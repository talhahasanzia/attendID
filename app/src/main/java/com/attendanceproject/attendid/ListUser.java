package com.attendanceproject.attendid;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created by tzia on 03-May-17.
 */

public class ListUser
{

    public User entryObject;
    public User exitObject;
    public String date;
    public String time;
    public String value;
    public String status;
    String timeDifference;


    public ListUser()
    {


    }

    public ListUser( User entryObject, User exitObject )
    {

        this.entryObject = entryObject;
        this.exitObject = exitObject;


    }


    public String getTimeDifference()
    {
        String timeDifference = "";


        String etime = entryObject.time.replace( "\"", "" );
        String edate = entryObject.date.replace( "\"", "" );
        String xtime = exitObject.time.replace( "\"", "" );
        String xdate = exitObject.date.replace( "\"", "" );

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat( "dd/MM/yyyy HH:mm:ss" );

        try
        {
            long entryTime = simpleDateFormat.parse( edate + " " + etime ).getTime();

            long exitTime = simpleDateFormat.parse( xdate + " " + xtime ).getTime();


            long totalTime = exitTime - entryTime;


            int SECOND = 1000;
            final int MINUTE = 60 * SECOND;
            int HOUR = 60 * MINUTE;
            int DAY = 24 * HOUR;


            long ms = totalTime;
            StringBuffer text = new StringBuffer( "" );



            if ( ms > HOUR )
            {

                if(ms/HOUR<10)
                    text.append( "0"+ms / HOUR ).append( ":" );
                else
                text.append( ms / HOUR ).append( ":" );
                ms %= HOUR;

            }
            else
            {

               text.append( "00:" );
            }
            if ( ms > MINUTE )
            {
                if(ms/MINUTE<10)
                    text.append("0"+ ms / MINUTE ).append( ":" );
                else
                text.append( ms / MINUTE ).append( ":" );


                ms %= MINUTE;


            }
            else
            {

                text.append("00:");
            }
            if (ms > SECOND) {
                if(ms/SECOND<10)
                text.append("0"+ms / SECOND).append("");
                else
                    text.append(ms / SECOND).append("");

                ms %= SECOND;


            }



        timeDifference=text.toString();


        }
        catch ( ParseException e )
        {
            e.printStackTrace();
        }


        return timeDifference;
    }
}
