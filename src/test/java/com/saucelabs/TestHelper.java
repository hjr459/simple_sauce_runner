package com.saucelabs;

import java.util.Map;

/**
 * Created by baba on 4/20/15.
 */
public class TestHelper {
    //private Map<String, String> env = System.getenv();
    //private String username = env.get("SAUCE_USERNAME");
    //private String accessKey = env.get("SAUCE_ACCESS_KEY");

    public static String username = "idontknow";
    public static String accessKey = "1234567890";

    //public static String onDemandUrl = "http://" + username + ":" + accessKey + "@ondemand.saucelabs.com:80/wd/hub";
    //public static String onDemandUrl = "http://" + username + ":" + accessKey + "@gotitwrong.com:80/wd/hub";
    public static String onDemandUrl = "https://localhost:4444/wd/hub";
}
