package com.oila.oneaccount.ui.callbacks;

/**
 * ---------------------------------------------------
 * Created by Sermilion on 11/04/2017.
 * Project: UComplex
 * ---------------------------------------------------
 * <a href="http://www.ucomplex.org">www.ucomplex.org</a>
 * <a href="http://www.github.com/sermilion>github</a>
 * ---------------------------------------------------
 */

public interface DownloadCallback<T> {
    void onResponse(T response);
    void onError(Throwable t);
}
