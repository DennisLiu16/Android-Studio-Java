package com.example.finaltest_106033108;
import android.app.Application;
public class Global extends Application{
    private boolean active = true;
    public boolean get()
    {
        return active;
    }
    public void set(boolean b)
    {
        this.active = b;
    }
}
