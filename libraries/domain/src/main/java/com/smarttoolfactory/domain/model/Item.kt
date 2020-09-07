package com.smarttoolfactory.domain.model

import android.os.Parcelable
import com.smarttoolfactory.data.model.Mappable

interface Item : Parcelable, Mappable {

    /**
     * Data classes have same hash code if their constructor has the same objects
     *
     * * Data classes have 3 common properties if properties of  constructor is the same
     * * Hash code
     * * Equals
     * * toString
     */
    fun dataEquals(other: Item?): Boolean {
        return this.hashCode() == other.hashCode()
    }
}
