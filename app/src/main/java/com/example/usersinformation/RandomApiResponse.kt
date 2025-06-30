package com.example.usersinformation

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


data class RandomApiResponse(
    val results: List<ApiUser>
)
@Parcelize
data class ApiUser(
    val gender: String,
    val name: Name,
    val location: Location,
    val email: String,
    val login: Login,
    val dob: Dob,
    val registered: Registered,
    val phone: String,
    val cell: String,
    val id: Id,
    val picture: Picture,
    val nat: String
): Parcelable {
    @Parcelize
    data class Name(
        val title: String,
        val first: String,
        val last: String
    ):Parcelable {
        fun getFullName() = "$title $first $last"
    }

    @Parcelize
    data class Location(
        val street: Street,
        val city: String,
        val state: String,
        val country: String,
        val postcode: String,
        val coordinates: Coordinates,
        val timezone: Timezone
    ):Parcelable{
        fun getLocation() = "$state $city ${street.getStreet()} $postcode ${timezone.getTimeZone()} ${coordinates.getCoordinates()}"
    }

    @Parcelize
    data class Street(
        val number: Int,
        val name: String
    ):Parcelable{
        fun getStreet()= "$name $number"
    }

    @Parcelize
    data class Coordinates(
        val latitude: String,
        val longitude: String
    ):Parcelable{
        fun getCoordinates() = "$latitude $longitude"
    }

    @Parcelize
    data class Timezone(
        val offset: String,
        val description: String
    ):Parcelable{
        fun getTimeZone()="$offset $description"
    }

    @Parcelize
    data class Login(
        val uuid: String,
        val username: String,
        val password: String,
        val salt: String,
        val md5: String,
        val sha1: String,
        val sha256: String
    ):Parcelable{
        fun getLogin() = "$uuid $username $password"
    }

    @Parcelize
    data class Dob(
        val date: String,
        val age: Int
    ):Parcelable

    @Parcelize
    data class Registered(
        val date: String,
        val age: Int
    ):Parcelable{
        fun getRegistred() = "$date $age"
    }

    @Parcelize
    data class Id(
        val name: String,
        val value: String
    ):Parcelable{
        fun getId() = "$name $value"
    }

    @Parcelize
    data class Picture(
        val large: String,
        val medium: String,
        val thumbnail: String
    ):Parcelable
}