package com.example.a106033108_hw_2;

import java.util.concurrent.TimeUnit;
/*
* This class is for myTimer
* @constructor
*   @input : long _now
*   @rtnVal : none
*   @des : log start time
*
* @static method
*
*
*
*
* */
public class mTimer {
    /*private vars*/
    private long _start = 0;
    private long _end = 0;
    private long _have_pass = 0;

    private Boolean _isrunning = true;

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

        return String.format("%s:%s.%s",min,sec,milisec);
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

