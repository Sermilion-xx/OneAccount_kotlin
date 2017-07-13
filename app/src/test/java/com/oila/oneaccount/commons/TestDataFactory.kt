package com.oila.oneaccount.commons

import com.oila.oneaccount.data.model.profile.FieldType
import com.oila.oneaccount.data.model.profile.ProfileItem
import java.util.*

object TestDataFactory {

    @JvmStatic fun randomUuid(): String {
        return UUID.randomUUID().toString()
    }

    @JvmStatic fun makeProfileItem(uniqueSuffix: String): ProfileItem {
        return makeProfile(uniqueSuffix)
    }

    @JvmStatic fun makeListProfile(number: Int): MutableList<ProfileItem> {
        val ribots = ArrayList<ProfileItem>()
        for (i in 0..number.dec()) {
            ribots.add(makeProfileItem(i.toString()))
        }
        return ribots
    }

    @JvmStatic fun makeProfile(uniqueSuffix: String): ProfileItem {
        return ProfileItem(key = "Key: $uniqueSuffix",
                value = "Value: $uniqueSuffix", type = FieldType.PRIMARY_NAME)
    }
}
