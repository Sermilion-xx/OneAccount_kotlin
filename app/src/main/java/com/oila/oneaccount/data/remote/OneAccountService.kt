package com.oila.oneaccount.data.remote

import com.oila.oneaccount.data.model.profile.ProfileItem
import retrofit2.http.GET
import rx.Observable

interface OneAccountService {
    @GET("profile")
    fun getProfile(): Observable<List<ProfileItem>>
}
