package com.oila.oneaccount


import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.whenever
import com.oila.oneaccount.commons.TestDataFactory
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
import uk.co.ribot.androidboilerplate.util.RxSchedulersOverrideRule


@RunWith(MockitoJUnitRunner::class)
class ProfilePresenterTest {

    @Rule @JvmField
    val overrideSchedulersRule = RxSchedulersOverrideRule()

    @Mock
    lateinit var mockMainMvpView: ProfileContract.View

    @Mock
    lateinit var mockDataManager: DataManager

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

    @Test
    fun loadProfileReturnsProfileItems() {
        val items = TestDataFactory.makeListProfile(10)
        whenever(mockDataManager.getProfileItems()).thenReturn(Observable.just(items))

        mainPresenter.loadProfile()
        verify(mockMainMvpView).showProfile(items)
        verify(mockMainMvpView, never()).showError(ProfileContract.View.profileLoadError)
    }


    @Test
    fun loadProfileItemsShowsDefaultItemsIfDbReturnsEmptyList() {
        whenever(mockDataManager.getProfileItems()).thenReturn(Observable.just(mutableListOf()))

        mainPresenter.loadProfile()
        verify(mockMainMvpView).onEmptyItems(anyList())
    }

    @Test
    fun loadProfileItemsFails() {
        whenever(mockDataManager.getProfileItems()).
                thenReturn(Observable.error<MutableList<ProfileItem>>(RuntimeException()))

        mainPresenter.loadProfile()
        verify(mockMainMvpView).showError(ProfileContract.View.profileLoadError)
        verify(mockMainMvpView, never()).showProfile(anyList<ProfileItem>())
    }


    @Test
    fun saveProfileSuccessCallsOnProfileSaved() {
        val items = TestDataFactory.makeListProfile(10)
        whenever(mockDataManager.setProfileItems(items)).thenReturn(Observable.from(items))

        mainPresenter.saveProfile(items)
        verify(mockMainMvpView).onProfileSaved()
        verify(mockMainMvpView, never()).showError(ProfileContract.View.profileSaveError)
    }

    @Test
    fun saveProfileFailShowsError() {
        val items = TestDataFactory.makeListProfile(10)
        whenever(mockDataManager.setProfileItems(items)).
        thenReturn(Observable.error<ProfileItem>(RuntimeException()))

        mainPresenter.saveProfile(items)
        verify(mockMainMvpView, never()).onProfileSaved()
        verify(mockMainMvpView).showError(ProfileContract.View.profileSaveError)
    }
}
