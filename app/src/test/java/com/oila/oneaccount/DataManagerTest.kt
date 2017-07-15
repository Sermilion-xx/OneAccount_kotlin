package com.oila.oneaccount

import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import com.oila.oneaccount.commons.TestDataFactory
import com.oila.oneaccount.data.DataManager
import com.oila.oneaccount.data.local.DatabaseHelper
import com.oila.oneaccount.data.model.profile.ProfileItem
import com.oila.oneaccount.data.remote.OneAccountService
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import rx.Observable
import rx.observers.TestSubscriber


/**
 * This test class performs local unit tests without dependencies on the Android framework
 * For testing methods in the DataManager follow this approach:
 * 1. Stub mock helper classes that your method relies on. e.g. RetrofitServices or DatabaseHelper
 * 2. Test the Observable using TestSubscriber
 * 3. Optionally write a SEPARATE test that verifies that your method is calling the right helper
 * using Mockito.verify()
 */
@RunWith(MockitoJUnitRunner::class)
class DataManagerTest {

    @Mock
    lateinit var mockDatabaseHelper: DatabaseHelper

    lateinit var dataManager: DataManager

    @Before
    fun setUp() {
        dataManager = DataManager(object :OneAccountService {
            override fun getProfile(): Observable<List<ProfileItem>> {
                TODO("not implemented")
            }
        }, mockDatabaseHelper)
    }

    @Test
    fun setProfileItemsEmitsValues() {
        val items = mutableListOf(TestDataFactory.makeProfileItem("r1"), TestDataFactory.makeProfileItem("r2"))
        stubSetProfileItemsHelperCalls(items)

        val result = TestSubscriber<ProfileItem>()
        dataManager.setProfileItems(items).subscribe(result)
        verify(mockDatabaseHelper).setProfileItems(items)
        result.assertNoErrors()
        result.assertReceivedOnNext(items)
    }

    @Test
    fun setProfileItemsCallsDatabase() {
        val items = mutableListOf(TestDataFactory.makeProfileItem("r3"), TestDataFactory.makeProfileItem("r4"))
        stubSetProfileItemsHelperCalls(items)
        val result = TestSubscriber<ProfileItem>()
        dataManager.setProfileItems(items).subscribe(result)
        result.assertNoErrors()
        result.assertReceivedOnNext(items)
        verify(mockDatabaseHelper).setProfileItems(items)
    }


    private fun stubSetProfileItemsHelperCalls(items: MutableList<ProfileItem>) {
        whenever(mockDatabaseHelper.setProfileItems(items)).thenReturn(Observable.from(items))
    }
}
