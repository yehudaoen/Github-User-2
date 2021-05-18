package com.example.githubuser2

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DataUserGithub(
        var name: String? = null,
        var avatar: String? = null,
        var username: String? = null,
        var repository: String? = null,
        var location: String? = null,
        var company: String? = null,
        var followers: String? = null,
        var following: String? = null,
) : Parcelable
/*{
    constructor(parcel: Parcel) : this(
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    companion object : Parceler<DataUserGithub> {

        override fun DataUserGithub.write(parcel: Parcel, flags: Int) {
            parcel.writeValue(avatar)
            parcel.writeString(name)
            parcel.writeString(username)
            parcel.writeString(following)
            parcel.writeString(followers)
            parcel.writeString(repository)
            parcel.writeString(company)
            parcel.writeString(location)
        }

        override fun create(parcel: Parcel): DataUserGithub {
            return DataUserGithub(parcel)
        }
    }
}*/