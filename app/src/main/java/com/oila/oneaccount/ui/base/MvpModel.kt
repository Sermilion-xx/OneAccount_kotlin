package com.oila.oneaccount.ui.base

import io.reactivex.Observable

/**
 * ---------------------------------------------------
 * Created by Sermilion on 08/07/2017.
 * Project: OneAccountKotlin
 * ---------------------------------------------------
 * <a href="http://www.ucomplex.org">www.ucomplex.org</a>
 * <a href="http://www.github.com/sermilion>github</a>
 * ---------------------------------------------------
 */
interface MvpModel<L, out P, in E> {
    fun loadData(params: E): Observable<L>
    fun clear()
    fun getData(): P
    fun processData(data: L): P
}