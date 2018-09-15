package com.jaagro.tms.jpush;

import cn.jpush.api.JPushClient;
import org.junit.Before;

public abstract class BaseTest {
//    protected static final String APP_KEY = "d4ee2375846bc30fa51334f5";
//    protected static final String MASTER_SECRET = "3f045fd404d09a8a1f38d791";
	protected static final String APP_KEY = "97e71dd3af7b165822a52658";
    protected static final String MASTER_SECRET = "4e6cc81b13f8f12754282c7b";
    protected static final String GROUP_MASTER_SECRET = "b11314807507e2bcfdeebe2e";
    protected static final String GROUP_PUSH_KEY = "2c88a01e073a0fe4fc7b167c";

    public static final String ALERT = "JPush Test - alertqqqq";
    public static final String MSG_CONTENT = "JPush Test - msgContent";
    
    public static final String REGISTRATION_ID1 = "0900e8d85ef";
    public static final String REGISTRATION_ID2 = "0a04ad7d8b4";
   // public static final String REGISTRATION_ID3 = "18071adc030dcba91c0";
//    public static final String REGISTRATION_ID3 = "864728033568701";
//    public static final String REGISTRATION_ID3 = "864728038568706";
    public static final String REGISTRATION_ID3 = "A00000688FA790";
    
    protected JPushClient jpushClient = null;
    
    @Before
    public void before() {
        jpushClient = new JPushClient(MASTER_SECRET, APP_KEY);
        
    }

}
