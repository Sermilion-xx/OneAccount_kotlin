package com.oila.oneaccount.data.model.profile

import java.util.Comparator

/**
 * ---------------------------------------------------
 * Created by Sermilion on 08/07/2017.
 * Project: OneAccountKotlin
 * ---------------------------------------------------
 * <a href="http://www.ucomplex.org">www.ucomplex.org</a>
 * <a href="http://www.github.com/sermilion>github</a>
 * ---------------------------------------------------
 */
class ProfileItem(val key: String, var value: String, val type: FieldType) : Comparable<ProfileItem> {

    override fun compareTo(other: ProfileItem): Int {
        if (this.order > other.order) {
            return 1
        } else if (this.order < other.order) {
            return -1
        }
        return 0
    }

    class ProfileItemComparator : Comparator<ProfileItem> {
        override fun compare(o1: ProfileItem, o2: ProfileItem): Int {
            return o1.compareTo(o2)
        }
    }

    val id: Long = -1
    var isShared: Boolean = false
    var isSelected: Boolean = false
    val order: Int

    init {
        this.isShared = false
        this.isSelected = false
        this.order = defineOrder()
    }

    private fun defineOrder(): Int {
        if (type === FieldType.MARKER && key == HEADER) {
            return 0
        } else if (type === FieldType.PRIMARY_NAME && key == FIRSTNAME) {
            return 1
        } else if (type === FieldType.PRIMARY_NAME && key == MIDDLENAME) {
            return 2
        } else if (type === FieldType.PRIMARY_NAME && key == LASTNAME) {
            return 3
        } else if (type === FieldType.PRIMARY_PHOTO && key == PROFILE_IMAGE) {
            return 4
        } else if (type === FieldType.MARKER && key == CONTACTS_SEPARATOR) {
            return 5
        } else {
            return 6
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other?.javaClass != javaClass) return false

        other as ProfileItem

        if (key != other.key) return false
        if (value != other.value) return false
        if (type != other.type) return false
        if (order != other.order) return false

        return true
    }

    override fun hashCode(): Int {
        var result = key.hashCode()
        result = 31 * result + value.hashCode()
        result = 31 * result + type.hashCode()
        result = 31 * result + order
        return result
    }

    companion object {
        val FIRSTNAME = "First name"
        val MIDDLENAME = "Middle name"
        val LASTNAME = "Last name"
        val PROFILE_IMAGE = "Profile image"
        val CONTACTS_SEPARATOR = "Contacts separator"
        val HEADER = "Field"
    }



}
