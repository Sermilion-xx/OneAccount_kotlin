package com.oila.oneaccount.ui.profile

import android.content.Context
import android.net.Uri
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.*
import butterknife.ButterKnife
import com.oila.oneaccount.OneApplication
import com.oila.oneaccount.R
import com.oila.oneaccount.data.model.SharedData
import com.oila.oneaccount.data.model.profile.FieldType
import com.oila.oneaccount.data.model.profile.ProfileItem
import com.oila.oneaccount.ui.callbacks.IntentCallback
import com.oila.oneaccount.ui.callbacks.OnListItemClicked
import com.oila.oneaccount.util.hideKeyboard
import com.oila.oneaccount.util.showKeyboard
import com.tubb.smrv.SwipeHorizontalMenuLayout
import com.tubb.smrv.SwipeMenuLayout
import com.tubb.smrv.listener.SwipeSwitchListener
import org.jetbrains.anko.find
import java.util.*
import javax.inject.Inject


class ProfileAdapter @Inject constructor(): RecyclerView.Adapter<ProfileAdapter.ProfileHolder>() {

    var isMyProfile: Boolean = false
    var mIntentCallback: IntentCallback<Void>? = null
    var mProfileCallback: ProfileCallback? = null
    var mSaveCallback: OnListItemClicked<ProfileItem>? = null
    var mItems = mutableListOf<ProfileItem>()
    val selectedPositions: MutableSet<ProfileItem>

    init {
        this.selectedPositions = HashSet<ProfileItem>()
    }

    fun getSelectedNum() = selectedPositions.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileHolder {
        val inflater = LayoutInflater.from(parent.context)
        var layout = R.layout.item_profile_header
        if (viewType == TYPE_PRIMARY_FIELD || viewType == TYPE_CUSTOM_FIELDS) {
            layout = R.layout.item_profile_swipe
        } else if (viewType == TYPE_PHOTO_FIELD) {
            layout = R.layout.item_profile_photo_swipe
        } else if (viewType == TYPE_CONTACTS) {
            layout = R.layout.item_profile_contacts
        } else if (viewType == TYPE_CUSTOM_FIELDS_SEPARATOR) {
            layout = R.layout.item_profile_custom_fields_separator
        }
        val viewRow = inflater.inflate(layout, parent, false)
        return ProfileHolder(viewRow, viewType, isMyProfile)
    }

    override fun onBindViewHolder(holder: ProfileHolder, position: Int) {
        val viewType = getItemViewType(holder.adapterPosition)
        val item = mItems[position]

        if (!isMyProfile) {
            if (item.isShared) {
                holder.mShared?.setImageResource(R.drawable.ic_tick_green)
            } else {
                holder.mShared?.setImageResource(R.drawable.ic_clear_orange)
            }
        }

        if (isMyProfile) {
            if (item.isSelected) {
                holder.mTick?.setImageResource(R.drawable.ic_tick_green)
            } else {
                holder.mTick?.visibility = View.GONE
            }
        }

        val isShareableItem = viewType != TYPE_CUSTOM_FIELDS_SEPARATOR && viewType != TYPE_CONTACTS && viewType != TYPE_HEADER
        if (isShareableItem) {
            if (item.isShared) {
                if (holder.mTick?.visibility == View.VISIBLE) {
                    item.isShared = false
                }
                holder.mTick?.visibility = View.GONE
            }

            holder.mClickArea.setOnClickListener({
                if (selectedPositions.size > 0) {
                    markItemAsSelected(holder.mTick!!, item)
                }
            })
            onSelectPressed(holder, item)
        }

        if (viewType == TYPE_PRIMARY_FIELD || viewType == TYPE_CUSTOM_FIELDS) {
            initFieldsViewHolder(holder, item)
        } else if (viewType == TYPE_PHOTO_FIELD) {
            setupPhotoItem(holder, item)
        } else if (viewType == TYPE_CONTACTS) {
            holder.mContactsButton.setOnClickListener({ mIntentCallback?.startIntent(null) })
        }
    }

    private fun onSelectPressed(viewHolder: ProfileHolder, item: ProfileItem) {
        viewHolder.mSelect.setOnClickListener({
            markItemAsSelected(viewHolder.mTick!!, item)
            viewHolder.swipeHorizontalMenuLayout.smoothCloseMenu()
        })
    }

    private fun markItemAsSelected(view: View, item: ProfileItem) {
        if (view.visibility == View.GONE) {
            view.visibility = View.VISIBLE
            selectedPositions.add(item)
            item.isSelected = true
        } else if (view.visibility == View.VISIBLE) {
            view.visibility = View.GONE
            selectedPositions.remove(item)
            item.isSelected = false
        }
        mProfileCallback?.showSelectToolbar(selectedPositions.size)
//        mSaveCallback?.onClick(item)
    }

    fun deselectItems() {
        for (selectedItem in selectedPositions) {
            selectedItem.isSelected = false
        }
        selectedPositions.clear()
        notifyDataSetChanged()
    }

    fun deleteAction() {
        for (selectedItem in selectedPositions) {
            if (selectedItem.type === FieldType.PRIMARY_NAME) {
                selectedItem.value = ""
            } else {
                mItems.remove(selectedItem)
            }
            mSaveCallback?.onClick(selectedItem)
        }
        notifyDataSetChanged()
    }

    private fun setupPhotoItem(viewHolder: ProfileHolder, item: ProfileItem) {
        setSwipeListener(viewHolder)
        viewHolder.mFieldTitle.setOnClickListener(
                {
                    val photoUri = Uri.parse(SharedData.getInstance().photoItem.value)
                    mProfileCallback?.getProfileBitmap(photoUri)
                }
        )

        viewHolder.mDelete.setOnClickListener({
            item.value = ""
            mProfileCallback?.showToast(R.string.photo_deleted)
            notifyItemChanged(viewHolder.adapterPosition)
            mSaveCallback?.onClick(item)
            viewHolder.mTick?.visibility = View.GONE
            viewHolder.swipeHorizontalMenuLayout.smoothCloseMenu()
        })
        viewHolder.mDelete.setText(R.string.clear)
        viewHolder.mDelete.setBackgroundColor(ContextCompat.getColor(OneApplication.getInstance(), R.color.colorOrange))
    }

    private fun initFieldsViewHolder(viewHolder: ProfileHolder, item: ProfileItem) {
        val context: Context = viewHolder.mField.context
        val position = viewHolder.adapterPosition
        val itemView = viewHolder.swipeHorizontalMenuLayout
        val key = item.key
        val value = item.value
        if (value.replace(" ", "").isEmpty()) {
            viewHolder.mField.hint = key
            viewHolder.mField.setText("")
            viewHolder.mFieldTitleStatic.visibility = View.GONE
            viewHolder.mFieldTitle.visibility = View.GONE
        } else {
            viewHolder.mField.hint = value
            viewHolder.mFieldTitleStatic.text = key
            viewHolder.mFieldTitleStatic.visibility = View.VISIBLE
        }
        viewHolder.mFieldTitle.text = key
        viewHolder.mField.setOnEditorActionListener({ v, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                viewHolder.mField.clearFocus()
                hideKeyboard(v, context)
            }
            false
        })
        setSwipeListener(viewHolder)
        viewHolder.mDelete.setOnClickListener({
            if (getItemViewType(position) == TYPE_PRIMARY_FIELD) {
                item.value = ""
                viewHolder.mField.clearFocus()
                viewHolder.mField.hint = ""
                viewHolder.mField.setText("")
                notifyItemChanged(position)
                viewHolder.mFieldTitleStatic.visibility = View.GONE
            } else if (getItemViewType(position) == TYPE_CUSTOM_FIELDS) {
                mItems.removeAt(position)
                notifyItemRemoved(position)
                notifyItemRangeChanged(position, itemCount)
            }
            mSaveCallback?.onClick(item)
            itemView.smoothCloseMenu()
        })

        viewHolder.mField.setOnFocusChangeListener({ v, hasFocus ->
            if (hasFocus) {
                showKeyboard(v, context)
                viewHolder.mField.setText(item.value)
                viewHolder.mField.setSelection(item.value.length)
                viewHolder.mField.hint = ""
                if (viewHolder.mField.text.isEmpty()) {
                    showTitleText(viewHolder)
                }
            } else {
                mItems[position].value = viewHolder.mField.text.toString()
                viewHolder.mField.hint = item.key
                viewHolder.mFieldTitleStatic.visibility = View.GONE
                if (viewHolder.mField.length() == 0) {
                    hideTitleText(viewHolder)
                    viewHolder.mFieldTitle.visibility = View.GONE
                }
                mSaveCallback?.onClick(item)
                hideKeyboard(v, context)
            }
        })
    }


    private fun setSwipeListener(viewHolder: ProfileHolder) {
        val itemView = viewHolder.swipeHorizontalMenuLayout
        itemView.setSwipeListener(object : SwipeSwitchListener {
            override fun beginMenuClosed(swipeMenuLayout: SwipeMenuLayout) {
                setBordersVisibility(viewHolder, View.GONE)
            }

            override fun beginMenuOpened(swipeMenuLayout: SwipeMenuLayout) {
                setBordersVisibility(viewHolder, View.VISIBLE)
            }

            override fun endMenuClosed(swipeMenuLayout: SwipeMenuLayout) {
                setBordersVisibility(viewHolder, View.GONE)
            }

            override fun endMenuOpened(swipeMenuLayout: SwipeMenuLayout) {
                setBordersVisibility(viewHolder, View.VISIBLE)
            }
        })
    }

    private fun setBordersVisibility(viewHolder: ProfileHolder, visibility: Int) {
        viewHolder.mBorderBottom.visibility = visibility
        viewHolder.mBorderTop.visibility = visibility
    }

    private fun hideTitleText(viewHolder: ProfileHolder) {
        viewHolder.mFieldTitle.animate().translationY(0f)
        viewHolder.mFieldTitle.visibility = View.GONE
    }

    private fun showTitleText(viewHolder: ProfileHolder) {
        viewHolder.mFieldTitle.visibility = View.VISIBLE
        viewHolder.mFieldTitle.animate().translationY((-viewHolder.mField.height).toFloat())
    }

    override fun getItemCount(): Int = mItems.size

    inner class ProfileHolder(itemView: View, viewType: Int, isMine: Boolean) : RecyclerView.ViewHolder(itemView) {

        lateinit var mField: EditText
        lateinit var mFieldTitle: TextView
        lateinit var mFieldTitleStatic: TextView
        lateinit var mContactsButton: ImageButton
        lateinit var mDelete: Button
        lateinit var mSelect: Button
        var mTick: ImageView? = null
        lateinit var mBorderTop: View
        lateinit var mBorderBottom: View
        var mShared: ImageView? = null
        lateinit var swipeHorizontalMenuLayout: SwipeHorizontalMenuLayout
        lateinit var mClickArea: ViewGroup

        init {
            ButterKnife.bind(this, itemView)
            if (viewType == TYPE_PRIMARY_FIELD || viewType == TYPE_CUSTOM_FIELDS) {
                mField = itemView.find<EditText>(R.id.field)
                mFieldTitle = itemView.find(R.id.field_title)
                mFieldTitleStatic = itemView.find<TextView>(R.id.field_title_static)
                swipeHorizontalMenuLayout = itemView.find<SwipeHorizontalMenuLayout>(R.id.swipe_layout)
            } else if (viewType == TYPE_CONTACTS) {
                mContactsButton = itemView.find<ImageButton>(R.id.image)
            } else if (viewType == TYPE_PHOTO_FIELD) {
                mFieldTitle = itemView.find<TextView>(R.id.field)
                swipeHorizontalMenuLayout = itemView.find<SwipeHorizontalMenuLayout>(R.id.swipe_layout)
            }
            if (viewType == TYPE_PHOTO_FIELD || viewType == TYPE_PRIMARY_FIELD || viewType == TYPE_CUSTOM_FIELDS) {
                mDelete = itemView.find<Button>(R.id.menu_right)
                mBorderTop = itemView.find(R.id.border_top)
                mBorderBottom = itemView.find(R.id.border_bottom)
            }
            if (viewType != TYPE_CUSTOM_FIELDS_SEPARATOR
                    && viewType != TYPE_CONTACTS
                    && viewType != TYPE_HEADER) {
                mSelect = itemView.find<Button>(R.id.menu_left)
                mTick = itemView.find<ImageView>(R.id.tick)
                mClickArea = itemView.find<ViewGroup>(R.id.smContentView)
            }
            if (!isMine) {
                mShared = itemView.find<ImageView>(R.id.shared)
                mShared?.visibility = View.VISIBLE
            }
        }

    }

    override fun getItemViewType(position: Int): Int {
        val key = mItems[position].key
        val type = mItems[position].type

        if (type == FieldType.MARKER && key == ProfileItem.HEADER) {
            return TYPE_HEADER
        } else if (type == FieldType.PRIMARY_PHOTO) {
            return TYPE_PHOTO_FIELD
        } else if (type == FieldType.PRIMARY_CONTACTS) {
            return TYPE_CONTACTS
        } else if (type == FieldType.MARKER && key == ProfileItem.CONTACTS_SEPARATOR) {
            return TYPE_CUSTOM_FIELDS_SEPARATOR
        } else if (type == FieldType.PRIMARY_NAME) {
            return TYPE_PRIMARY_FIELD
        } else {
            return TYPE_CUSTOM_FIELDS
        }
    }

    companion object {
        private val TYPE_HEADER = 0
        private val TYPE_PRIMARY_FIELD = 1
        private val TYPE_PHOTO_FIELD = 2
        private val TYPE_CUSTOM_FIELDS = 3
        private val TYPE_CUSTOM_FIELDS_SEPARATOR = 4
        private val TYPE_CONTACTS = 5
    }
}
