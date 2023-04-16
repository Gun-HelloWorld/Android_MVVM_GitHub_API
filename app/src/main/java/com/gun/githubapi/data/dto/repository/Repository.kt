package com.gun.githubapi.data.dto.repository

import com.google.gson.annotations.SerializedName
import com.gun.githubapi.data.dto.user.User

/**
 * .../users/name/repos API Response
 * @link "file:///android_asset/user_detail.json"
 **/
data class Repository(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("private") val private: Boolean,
    @SerializedName("owner") val user: User,
    @SerializedName("html_url") val htmlUrl: String,
    @SerializedName("description") val description: String,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("updated_at") val updatedAt: String,
    @SerializedName("pushed_at") val pushedAt: String,
    @SerializedName("stargazers_count") val stargazersCount: Int,
    @SerializedName("watchers_count") val watchersCount: Int,
    @SerializedName("forks_count") val forksCount: Int,
    @SerializedName("language") val language: String,
    @SerializedName("license") val license: License?,
    @SerializedName("topics") val topics: List<Object>,
    @SerializedName("visibility") val visibility: String
)