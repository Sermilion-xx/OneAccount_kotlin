package com.oila.oneaccount


import com.nhaarman.mockito_kotlin.doThrow
import com.nhaarman.mockito_kotlin.whenever
import com.oila.oneaccount.data.DataManager
import com.oila.oneaccount.data.model.profile.ProfileItem
import com.oila.oneaccount.ui.main.ProfileContract
import com.oila.oneaccount.ui.main.ProfilePresenter
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyList
import org.mockito.Mock
import org.mockito.Mockito.never
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner
import rx.Observable
import com.oila.oneaccount.commons.TestDataFactory
import com.oila.oneaccount.data.local.DatabaseHelper
import okhttp3.internal.Util
import uk.co.ribot.androidboilerplate.util.RxSchedulersOverrideRule


@RunWith(MockitoJUnitRunner::class)
class ProfilePresenterTest {

    @Rule @JvmField
    val overrideSchedulersRule = RxSchedulersOverrideRule()

    @Mock
    lateinit var mockMainMvpView: ProfileContract.View

    @Mock
    lateinit var mockDataManager: DataManager

    @Mock
    lateinit var mockDatabaseHelper: DatabaseHelper

    lateinit var mainPresenter: ProfilePresenter

    @Before
    fun setUp() {
        mainPresenter = ProfilePresenter(mockDataManager)
        mainPresenter.attachView(mockMainMvpView)
    }

    @After
    fun tearDown() {
        mainPresenter.detachView()
    }

//    @Test
//    fun loadProfileReturnsProfileItems() {
//        val items = TestDataFactory.makeListProfile(10)
//        whenever(mockDataManager.getProfileItems()).thenReturn(Observable.just(items))
//
//        mainPresenter.loadProfile()
//        verify(mockMainMvpView).showProfile(items)
//        verify(mockMainMvpView, never()).showError()
//    }

    @Test
    fun loadProfileReturningEmptyItemsCallsSetProfileItems() {
        val items = mutableListOf<ProfileItem>()
        val itemsToSave = TestDataFactory.makeListProfile(10)
        whenever(mockDataManager.getProfileItems()).thenReturn(Observable.just(items))
        whenever(mockDataManager.setProfileItems(itemsToSave)).thenReturn(Observable.from(itemsToSave))
        whenever(mockDatabaseHelper.setProfileItems(itemsToSave)).thenReturn(Observable.from(itemsToSave))
//        whenever(mainPresenter.saveProfile(itemsToSave)).then {  }

        mainPresenter.loadProfile()
//        verify(mainPresenter).saveProfile(itemsToSave)
//        verify(mockMainMvpView).showProfile(itemsToSave)
        verify(mockMainMvpView, never()).showError()
    }

//    @Test
//    fun loadRibotsReturnsEmptyList() {
//        whenever(mockDataManager.getProfileItems()).thenReturn(Observable.just(mutableListOf()))
//
//        mainPresenter.loadProfile()
//        verify(mockMainMvpView).showError()
//        verify(mockMainMvpView, never()).showProfile(anyList<ProfileItem>())
//        verify(mockMainMvpView, never()).showError()
//    }
//
//    @Test
//    fun loadRibotsFails() {
//        whenever(mockDataManager.getProfileItems()).
//                thenReturn(Observable.error<MutableList<ProfileItem>>(RuntimeException()))
//
//        mainPresenter.loadProfile()
//        verify(mockMainMvpView).showError()
//        verify(mockMainMvpView, never()).showError()
//        verify(mockMainMvpView, never()).showProfile(anyList<ProfileItem>())
//    }
}
