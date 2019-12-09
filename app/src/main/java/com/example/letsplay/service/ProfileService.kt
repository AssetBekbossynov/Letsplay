package com.example.letsplay.service

import com.example.letsplay.entity.auth.PhotoDto
import com.example.letsplay.entity.auth.UserDto
import com.example.letsplay.entity.profile.FriendsInfo
import com.example.letsplay.entity.profile.Player
import com.example.letsplay.entity.profile.UserUpdateRequest
import okhttp3.MultipartBody
import retrofit2.http.*

interface ProfileService {
    @PUT("api/user/complete")
    suspend fun completeUser(@Header("Content-Type") contentType: String,
                             @Header("Authorization") token: String,
                             @Body userUpdateRequest: UserUpdateRequest): UserDto

    @POST("api/user")
    suspend fun updateUser(@Header("Content-Type") contentType: String,
                           @Header("Authorization") token: String,
                           @Body userUpdateRequest: UserUpdateRequest): UserDto

    @GET("api/user")
    suspend fun getUser(@Header("Content-Type") contentType: String,
                        @Header("Authorization") token: String): UserDto

    @GET("api/user/{name}")
    suspend fun getUser(@Header("Content-Type") contentType: String,
                        @Header("Authorization") token: String,
                        @Path("name") name: String): UserDto

    @Multipart
    @POST("api/user/image")
    suspend fun uploadPhoto(@Header("Authorization") token: String,
                            @Part file: MultipartBody.Part): PhotoDto

    @POST("api/friend/{nickname}")
    suspend fun addFriend(@Header("Authorization") token: String,
                          @Header("Content-Type") contentType: String,
                          @Path("nickname") nickname: String): FriendsInfo

    @GET("api/user/search/{text}")
    suspend fun getSearchResult(@Header("Authorization") token: String,
                                @Path("text") text: String,
                                @Query("1") from: Int,
                                @Query("10") to: Int): List<Player>

    @PUT("/api/friend/approve/{nickname}")
    suspend fun approveFriend(@Header("Authorization") token: String,
                              @Header("Content-Type") contentType: String,
                              @Path("nickname") nickname: String): FriendsInfo

    @PUT("/api/friend/cancel/{nickname}")
    suspend fun cancelFriend(@Header("Authorization") token: String,
                              @Header("Content-Type") contentType: String,
                              @Path("nickname") nickname: String): FriendsInfo

    @PUT("/api/friend/reject/{nickname}")
    suspend fun rejectFriend(@Header("Authorization") token: String,
                              @Header("Content-Type") contentType: String,
                              @Path("nickname") nickname: String): FriendsInfo

    @PUT("/api/friend/unfriend/{nickname}")
    suspend fun unfriendFriend(@Header("Authorization") token: String,
                              @Header("Content-Type") contentType: String,
                              @Path("nickname") nickname: String): FriendsInfo
}