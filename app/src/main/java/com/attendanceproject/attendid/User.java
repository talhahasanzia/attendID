package com.attendanceproject.attendid;

/**
 * Created by Me on 23-Apr-17.
 */

public class User
{
    public String date;
    public String time;
    public String value;
    public String status;

    public User( )
    {

    }

    public User( String date, String time, String value, String status )
    {
        this.date = date;
        this.time = time;
        this.value = value;
        this.status=status;
    }
}
