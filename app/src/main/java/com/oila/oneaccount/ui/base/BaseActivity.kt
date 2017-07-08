package com.oila.oneaccount.ui.base

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.FrameLayout
import android.widget.ProgressBar
import com.oila.oneaccount.OneApplication
import com.oila.oneaccount.R
import com.oila.oneaccount.injection.component.ActivityComponent
import com.oila.oneaccount.injection.component.ConfigPersistentComponent
import com.oila.oneaccount.injection.component.DaggerConfigPersistentComponent
import com.oila.oneaccount.injection.module.ActivityModule
import com.oila.oneaccount.ui.navdrawer.DrawerAdapter
import com.oila.oneaccount.ui.navdrawer.DrawerItem
import com.oila.oneaccount.ui.navdrawer.getDrawerItems
import org.jetbrains.anko.find
import java.util.*
import java.util.concurrent.atomic.AtomicLong


open class BaseActivity : AppCompatActivity() {

    companion object {
        @JvmStatic private val KEY_ACTIVITY_ID = "KEY_ACTIVITY_ID"
        @JvmStatic private val NEXT_ID = AtomicLong(0)
        @JvmStatic private val componentsMap = HashMap<Long, ConfigPersistentComponent>()
    }

    protected var mProgress: ProgressBar? = null
    protected var mToolbar: Toolbar? = null
    protected var mDrawer: DrawerLayout? = null
    protected var navigationView: NavigationView? = null
    protected var mDrawerAdapter: DrawerAdapter? = null
    protected var mActionBarDrawerToggle: ActionBarDrawerToggle? = null

    private var activityId: Long = 0
    lateinit var activityComponent: ActivityComponent
        get


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initDI(savedInstanceState)
    }

    private fun initDI(savedInstanceState: Bundle?) {
        activityId = savedInstanceState?.getLong(KEY_ACTIVITY_ID) ?: NEXT_ID.getAndIncrement()
        val configPersistentComponent = componentsMap.getOrPut(activityId, {
            val component = (applicationContext as OneApplication).applicationComponent
            DaggerConfigPersistentComponent.builder()
                    .applicationComponent(component)
                    .build()
        })
        activityComponent = configPersistentComponent.activityComponent(ActivityModule(this))

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putLong(KEY_ACTIVITY_ID, activityId)
    }

    override fun onDestroy() {
        if (!isChangingConfigurations) {
            componentsMap.remove(activityId)
        }
        super.onDestroy()
    }

    protected fun setupToolbar(title: String, vararg homeAsUpIndicator: Int): Toolbar? {
        mToolbar = find(R.id.toolbar)
        if (mToolbar != null) {
            mToolbar!!.title = title
            setSupportActionBar(mToolbar)
            if (homeAsUpIndicator.isNotEmpty()) {
                if (supportActionBar != null) {
                    supportActionBar!!.setHomeAsUpIndicator(homeAsUpIndicator[0])
                    supportActionBar!!.setDisplayHomeAsUpEnabled(true)
                    supportActionBar!!.setDisplayShowHomeEnabled(true)
                }
            }
        }
        return mToolbar
    }

    protected fun setupToolbar(title: String, drawable: Drawable): Toolbar? {
        mToolbar = findViewById(R.id.toolbar) as Toolbar
        if (mToolbar != null) {
            mToolbar!!.title = title
            setSupportActionBar(mToolbar)
            if (supportActionBar != null) {
                supportActionBar!!.setHomeAsUpIndicator(drawable)
                supportActionBar!!.setDisplayHomeAsUpEnabled(true)
                supportActionBar!!.setDisplayShowHomeEnabled(true)
            }
        }
        return mToolbar
    }

    //=================Setup methods================//
    fun setupDrawer(layout: Int) {
        val contentFrameLayout = find<FrameLayout>(R.id.content_frame)
        layoutInflater.inflate(layout, contentFrameLayout)
        val items = getDrawerItems(this, OneApplication.drawerProfileUrl, OneApplication.drawerUserName)
        setupDrawerView(items)
    }

    private fun setupDrawerView(drawerListItems: List<DrawerItem>) {
        mDrawer = find(R.id.drawer_layout)
        mDrawerAdapter = DrawerAdapter(drawerListItems)
        val mRecyclerView = find<RecyclerView>(R.id.left_drawer)
        val mLinearLayoutManager = LinearLayoutManager(this)
        mRecyclerView.layoutManager = mLinearLayoutManager
        mRecyclerView.adapter = mDrawerAdapter

        mActionBarDrawerToggle = ActionBarDrawerToggle(this, mDrawer, mToolbar, R.string.drawer_open, R.string.drawer_closed)
        if (mActionBarDrawerToggle != null) {
            mDrawer?.addDrawerListener(mActionBarDrawerToggle!!)
            mDrawer?.addDrawerListener(mActionBarDrawerToggle!!)
        }
        navigationView = find(R.id.navigation_view)
        navigationView?.setNavigationItemSelectedListener({ item ->
            when (item.itemId) {
                R.id.menu_home -> mDrawer?.closeDrawers()
            }
            false
        })
        mDrawer?.post({ mActionBarDrawerToggle?.syncState() })
    }
    //===========================================================================================//

    fun showProgress() {
        mProgress?.visibility = View.VISIBLE
    }

    fun hideProgress() {
        mProgress?.visibility = View.GONE
    }
}
