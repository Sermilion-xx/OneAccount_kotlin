package com.oila.oneaccount.ui.profile

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import butterknife.BindView
import butterknife.ButterKnife
import com.oila.oneaccount.R
import com.oila.oneaccount.data.model.profile.ProfileItem
import com.oila.oneaccount.ui.base.BaseActivity
import com.oila.oneaccount.ui.main.*
import org.jetbrains.anko.toast
import javax.inject.Inject

class ProfileActivity : BaseActivity(), ProfileContract.View {

    @Inject lateinit var presenter: ProfilePresenter
    @Inject lateinit var profileAdapter: ProfileAdapter

    @BindView(R.id.toolbar) lateinit var toolbar: Toolbar
    @BindView(R.id.recycler_view) lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityComponent.inject(this)
        setContentView(R.layout.activity_profile)
        ButterKnife.bind(this)
        setSupportActionBar(toolbar)

        recyclerView.adapter = profileAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        presenter.attachView(this)
        presenter.loadProfile()

    }

    override fun showProfile(profileItems : MutableList<ProfileItem>) {
        profileAdapter.mItems = profileItems
        profileAdapter.notifyDataSetChanged()
    }

    override fun showError() {
        toast(R.string.error_loading_profile)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }


}
