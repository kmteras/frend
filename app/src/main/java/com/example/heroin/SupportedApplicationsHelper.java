package com.example.heroin;

import java.util.Arrays;
import java.util.List;

public class SupportedApplicationsHelper {

    public static final List<String> SUPPORTED_APPS = Arrays.asList(
//            "com.google.android.apps.photos",
            "com.hmdglobal.camera2",
//            "com.google.android.apps.books",
//            "com.google.android.calculator",
            "com.google.android.music",
//            "com.google.android.videos",
            "com.google.android.play.games",
            "com.ng_labs.kidspaint"
    );

    public static List<String> getListOfSupportedApplications() {
        return SUPPORTED_APPS;
    }
}
