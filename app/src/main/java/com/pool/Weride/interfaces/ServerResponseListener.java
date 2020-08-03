package com.pool.Weride.interfaces;

import com.android.volley.VolleyError;


public interface ServerResponseListener {
    void onResponse(boolean isTimeOutError, boolean isError, String msg, String thisResponse, VolleyError volleyError);
}

