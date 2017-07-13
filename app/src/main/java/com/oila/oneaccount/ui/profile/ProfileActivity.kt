package com.oila.oneaccount.ui.profile

import android.app.Dialog
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.widget.ImageView
import butterknife.BindView
import butterknife.ButterKnife
import com.oila.oneaccount.R
import com.oila.oneaccount.data.model.profile.ProfileItem
import com.oila.oneaccount.ui.base.BaseActivity
import com.oila.oneaccount.ui.callbacks.IntentCallback
import com.oila.oneaccount.ui.callbacks.OnListItemClicked
import com.oila.oneaccount.ui.main.ProfileContract
import com.oila.oneaccount.ui.main.ProfilePresenter
import com.oila.oneaccount.util.FileUtils
import org.jetbrains.anko.toast
import javax.inject.Inject

class ProfileActivity : BaseActivity(), ProfileContract.View {

    @Inject lateinit var presenter: ProfilePresenter
    @Inject lateinit var profileAdapter: ProfileAdapter

    @BindView(R.id.toolbar) lateinit var toolbar: Toolbar
    @BindView(R.id.recyclerView) lateinit var recyclerView: RecyclerView

    private var mMenuVisible: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityComponent.inject(this)
        setContentView(R.layout.activity_profile)
        ButterKnife.bind(this)
        setSupportActionBar(toolbar)

        setupAdapter()
        recyclerView.adapter = profileAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        presenter.attachView(this)
        presenter.loadProfile()
    }

    private fun setupAdapter() {
        profileAdapter.isMyProfile = true
        profileAdapter.mIntentCallback = IntentCallback { }
        profileAdapter.mProfileCallback = profileCallback
        profileAdapter.mSaveCallback = OnListItemClicked {
            presenter.saveProfile(mutableListOf(it))
        }
    }

    override fun onBackPressed() {
        if (profileAdapter.getSelectedNum() > 0) {
            profileAdapter.deselectItems()
            showSelectedToolbar(0)
        } else {
            super.onBackPressed()
        }
    }

    val profileCallback = object : ProfileCallback {
        override fun showToast(message: Int) {
            toast(message)
        }

        override fun showSelectToolbar(selectedNum: Int) {
            showSelectedToolbar(selectedNum)
        }

        override fun getProfileBitmap(uri: Uri) {
            val path = FileUtils.getPath(this@ProfileActivity, uri)
            val bitmap = BitmapFactory.decodeFile(path)
            showProfileImageDialog(bitmap)
        }

    }

    fun showSelectedToolbar(selectedNum: Int) {
        if (supportActionBar != null) {
            var icon = R.drawable.ic_clear
            var title = selectedNum.toString()
            var visible = true

            if (selectedNum < 1) {
                icon = R.drawable.ic_menu
                title = getString(R.string.app_name)
                visible = false
                profileAdapter.deselectItems()
            }
            supportActionBar?.setHomeAsUpIndicator(icon)
            mToolbar.title = title
            showMenu(visible)
        }
    }

    fun showMenu(visible: Boolean) {
        mMenuVisible = visible
        invalidateOptionsMenu()
    }

    private fun showProfileImageDialog(bitmap: Bitmap?) {
        val dialogText = Dialog(this)
        dialogText.setContentView(R.layout.dialog_photo)
        val image = dialogText.findViewById(R.id.image) as ImageView
        if (bitmap != null) {
            image.setImageBitmap(bitmap)
        }
        dialogText.show()
    }

    override fun showProfile(profileItems: MutableList<ProfileItem>) {
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
