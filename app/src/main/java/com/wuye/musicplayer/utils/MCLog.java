package com.wuye.musicplayer.utils;

import android.util.Log;

public class MCLog {

	public static final boolean isDebug = false;
	 
	public static void i(String tag, String msg)  
    {  
        if (isDebug)  {
            Log.i(tag, msg);  
        }
    }  
  
    public static void d(String tag, String msg)  
    {  
        if (isDebug)  {
            Log.d(tag, msg);
        }
    }  
  
    public static void w(String tag, String msg)  
    {  
        if (isDebug)  {
            Log.w(tag, msg);
        }
    }  

    
    public static void e(String tag, String msg)  
    {  
        if (isDebug)  {
            Log.w(tag, msg);  
        }
    }  
  
    public static void v(String tag, String msg)  
    {  
        if (isDebug)  {
            Log.v(tag, msg);  
        }
    }  
}