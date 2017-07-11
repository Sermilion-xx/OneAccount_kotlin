package com.oila.oneaccount.data.model

import com.oila.oneaccount.data.model.profile.FieldType
import com.oila.oneaccount.data.model.profile.ProfileItem

class SharedData private constructor() {

    var items: MutableList<ProfileItem>

    init {
        items = defaultData
    }

    private val defaultData: MutableList<ProfileItem>
        get() {
            items = mutableListOf()
            if (items.isEmpty()) {
                items.add(ProfileItem(HEADER, "", FieldType.MARKER))
                items.add(ProfileItem(FIRSTNAME, "", FieldType.PRIMARY_NAME))
                items.add(ProfileItem(MIDDLENAME, "", FieldType.PRIMARY_NAME))
                items.add(ProfileItem(LASTNAME, "", FieldType.PRIMARY_NAME))
                items.add(ProfileItem(PROFILE_IMAGE, "", FieldType.PRIMARY_PHOTO))
                items.add(ProfileItem(CONTACTS_SEPARATOR, "", FieldType.MARKER))
            }
            return items
        }

    val photoItem: ProfileItem
        get() = items[POSITION_PHOTO]

    val firstNameItem: ProfileItem
        get() = items[POSITION_FIRSTNAME]

    companion object {

        val FIRSTNAME = "First name"
        val MIDDLENAME = "Middle name"
        val LASTNAME = "Last name"
        val PROFILE_IMAGE = "Profile image"
        val CONTACTS_SEPARATOR = "Contacts separator"
        val HEADER = "Field"

        private val POSITION_FIRSTNAME = 1
        private val POSITION_PHOTO = 4

        fun getInstance(): SharedData {
            return SharedDataSingletonHelper.INSTANCE
        }
        object SharedDataSingletonHelper {
            internal val INSTANCE = SharedData()
        }
    }

}