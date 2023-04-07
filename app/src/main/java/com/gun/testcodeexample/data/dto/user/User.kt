package com.gun.testcodeexample.data.dto.user

import com.google.gson.annotations.SerializedName


/**
 * .../users API Response
 * @link "file:///android_asset/user.json"
 *
 * .../users/nickname API Response
 * @link "file:///android_asset/user_detail.json"
 **/
data class User(
    // .../users API Response
    @SerializedName("login") val login: String,
    @SerializedName("id") val id: Int,
    @SerializedName("node_id") val nodeId: String,
    @SerializedName("avatar_url") val avatarUrl: String,
    @SerializedName("gravatar_id") val gravatarId: String,
    @SerializedName("url") val url: String,
    @SerializedName("html_url") val htmlUrl: String,
    @SerializedName("followers_url") val followersUrl: String,
    @SerializedName("following_url") val followingUrl: String,
    @SerializedName("gists_url") val gistsUrl: String,
    @SerializedName("starred_url") val starredUrl: String,
    @SerializedName("subscriptions_url") val subscriptionsUrl: String,
    @SerializedName("organizations_url") val organizationsUrl: String,
    @SerializedName("repos_url") val reposUrl: String,
    @SerializedName("events_url") val eventsUrl: String,
    @SerializedName("received_events_url") val receivedEventsUrl: String,
    @SerializedName("type") val type: String,
    @SerializedName("site_admin") val siteAdmin: String,
    // .../users/nickname API Response
    @SerializedName("name") val name: String,
    @SerializedName("blog") val blog: String,
    @SerializedName("location") val location: String,
    @SerializedName("public_repos") val public_repos: Int,
    @SerializedName("public_gists") val public_gists: Int,
    @SerializedName("followers") val followers: Int,
    @SerializedName("following") val following: Int,
    @SerializedName("created_at") val created_at: String,
    @SerializedName("updated_at") val updated_at: String
) : java.io.Serializable {
    fun existUserDetail(): Boolean {
        return name != null
    }
}
