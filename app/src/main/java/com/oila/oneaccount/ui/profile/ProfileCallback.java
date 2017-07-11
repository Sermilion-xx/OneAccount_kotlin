package com.oila.oneaccount.ui.profile;

import android.net.Uri;

/**
 * ---------------------------------------------------
 * Created by Sermilion on 06/03/2017.
 * Project: OneAccount
 * ---------------------------------------------------
 * <a href="http://www.ucomplex.org">www.ucomplex.org</a>
 * <a href="http://www.github.com/sermilion>github</a>
 * ---------------------------------------------------
 */

public interface ProfileCallback {
    void showToast(int message);
    void showSelectToolbar(int selectedNum);
    void getProfileBitmap(Uri uri);
}
