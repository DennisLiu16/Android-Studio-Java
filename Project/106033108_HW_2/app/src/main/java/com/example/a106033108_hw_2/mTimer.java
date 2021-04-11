package com.example.a106033108_hw_2;

import android.widget.Button;

import java.util.concurrent.TimeUnit;
/*if there is underline(_) before var , means it's a internal var*/
/**
 * This class is myTimer
 * @constructor
 *  input : long _now
 *  rtnVal : none
 *  des : log start time , create vars
 *
 * @toString
 *  input : none
 *  rtnval : (String)getElapsedString()
 *  des : Get time info to update on textview
 *
 * @get_isrunning
 *  input : none
 *  rtnval : (Boolean) _isrunning
 *  des : return private long _isrunning , the state of timer
 *
 * @getNanoElapsed
 *  input : none
 *  rtnval : (long) _have_pass
 *  des : return passed time in nano second
 *
 * @getMiliElapsed
 *  input : none
 *  rtnval : (long) _elapsed
 *  des : change nano second to milisecond , call @getNanoElapsed
 *
 * @getElapsedString
 *  input : none
 *  rtnval : (String) time format , e.g. 00:01:235
 *  des : transform mili second to time format , call @getMiliElapsed
 *
 * @reset
 *  input : none
 *  rtnval : none
 *  des : clean _start , _end , _have_pass , _isrunning
 *
 * @start
 *  input : none
 *  rtnval : none
 *  des : log _start from System.nanotime , flag _isrunning become true
 *
 * @pause
 *  input : none
 *  rtnval : none
 *  des : update _end, _have_pass ,then clean _start, _end, _isrunning
 *
 * @static init
 *  input : none
 *  rtnval : (mTimer)timer-object
 *  des : return new timer object , init _start with System.nanotime() , call @constructor
 *
* */
public class mTimer {
    /*private vars*/
    private long _start = 0;
    private long _end = 0;
    private long _have_pass = 0;
    private Boolean _isrunning = false;

    /*private constructor*/
    private mTimer (long _now)
    {
        _start = _now;
    }

    /*public class internal func*/
    @Override
    public String toString()
    {
        return getElapsedString();
    }

    public Boolean get_isrunning()
    {
        return _isrunning;
    }

    public long getNanoElapsed()
    {
        if(_isrunning)
        {
            return _have_pass + System.nanoTime() - _start;
        }
        return  _have_pass;
    }

    public long getMiliElapsed()
    {
        /*return milisecond*/
        long _elapsed = -1;
        _elapsed = getNanoElapsed()/1000000;
        return _elapsed;
    }

    public String getElapsedString() {
        long nano = getMiliElapsed();
        long milisec = nano % 1000;
        long tmp = nano / 1000;
        long sec = tmp%60;
        long min = tmp/60;

        return String.format("%02d:%02d.%03d",min,sec,milisec);
    }

    public void reset()
    {
        _start = 0;
        _end = 0;
        _have_pass = 0;
        _isrunning = false;
    }

    /*public outer class func*/
    public mTimer start(){
        if(!_isrunning)
        {
            _start = System.nanoTime();
            _isrunning = true;
        }
        return this;
    }

    /*class stop*/
    public mTimer pause(){
        if(_isrunning)
        {
            _end = System.nanoTime();
            _have_pass = _have_pass + _end - _start;
            _start = _end = 0;
            _isrunning = false;
        }
        return this;
    }

    /*class start , can assign*/
    public static mTimer init(){
        return new mTimer(System.nanoTime());
    }
}

