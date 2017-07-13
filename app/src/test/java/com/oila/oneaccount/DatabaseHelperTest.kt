package com.oila.oneaccount

import com.oila.oneaccount.data.local.DatabaseHelper
import com.oila.oneaccount.data.local.Db
import com.oila.oneaccount.data.local.OneDBOpenHelper
import com.oila.oneaccount.data.model.profile.ProfileItem
import com.squareup.sqlbrite.SqlBrite
import junit.framework.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config
import rx.android.schedulers.AndroidSchedulers
import rx.observers.TestSubscriber
import com.oila.oneaccount.commons.TestDataFactory
import uk.co.ribot.androidboilerplate.util.DefaultConfig
import uk.co.ribot.androidboilerplate.util.RxSchedulersOverrideRule

/**
 * Unit tests integration with a SQLite Database using Robolectric
 */
@RunWith(RobolectricTestRunner::class)
@Config(constants = BuildConfig::class, sdk = intArrayOf(DefaultConfig.EMULATE_SDK))
class DatabaseHelperTest {

    @Rule @JvmField
    val overrideSchedulersRule = RxSchedulersOverrideRule()

    val databaseHelper: DatabaseHelper by lazy {
        val dbHelper = OneDBOpenHelper(RuntimeEnvironment.application, OneDBOpenHelper.DATABASE_NAME, null)
        val sqlBrite = SqlBrite.Builder()
                .build()
                .wrapDatabaseHelper(dbHelper, AndroidSchedulers.mainThread())
        DatabaseHelper(sqlBrite)
    }

    @Test
    fun setRibots() {
        val ribots = listOf(TestDataFactory.makeProfileItem("r1"), TestDataFactory.makeProfileItem("r2"))

        val result = TestSubscriber<ProfileItem>()
        databaseHelper.setProfileItems(ribots).subscribe(result)
        result.assertNoErrors()
        result.assertReceivedOnNext(ribots)

        val cursor = databaseHelper.db.query("SELECT * FROM ${Db.ProfileTable.TABLE_PROFILE}")
        assertEquals(2, cursor.count)

        ribots.forEach {
            cursor.moveToNext()
            assertEquals(it.key, Db.ProfileTable.parseCursor(cursor))
        }

        cursor.close()
    }

    @Test
    fun getRibots() {
        val ribots = listOf(TestDataFactory.makeProfileItem("r3"), TestDataFactory.makeProfileItem("r4"))

        databaseHelper.setProfileItems(ribots).subscribe()

        val result = TestSubscriber<List<ProfileItem>>()
        databaseHelper.getProfile().subscribe(result)
        result.assertNoErrors()
        result.assertValue(ribots)
    }
}
