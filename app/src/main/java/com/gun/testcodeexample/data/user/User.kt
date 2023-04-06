package com.gun.testcodeexample.data.user

import com.google.gson.annotations.SerializedName

/**
 * Response Example
 *
https://api.github.com/users :
{
"login": "bmizerany",
"id": 46,
"node_id": "MDQ6VXNlcjQ2",
"avatar_url": "https://avatars.githubusercontent.com/u/46?v\u003d4",
"gravatar_id": "",
"url": "https://api.github.com/users/bmizerany",
"html_url": "https://github.com/bmizerany",
"followers_url": "https://api.github.com/users/bmizerany/followers",
"following_url": "https://api.github.com/users/bmizerany/following{/other_user}",
"gists_url": "https://api.github.com/users/bmizerany/gists{/gist_id}",
"starred_url": "https://api.github.com/users/bmizerany/starred{/owner}{/repo}",
"subscriptions_url": "https://api.github.com/users/bmizerany/subscriptions",
"organizations_url": "https://api.github.com/users/bmizerany/orgs",
"repos_url": "https://api.github.com/users/bmizerany/repos",
"events_url": "https://api.github.com/users/bmizerany/events{/privacy}",
"received_events_url": "https://api.github.com/users/bmizerany/received_events",
"type": "User",
"site_admin": false
}

https://api.github.com/users/a
{
"login": "A",
"id": 1410106,
"node_id": "MDQ6VXNlcjE0MTAxMDY\u003d",
"avatar_url": "https://avatars.githubusercontent.com/u/1410106?v\u003d4",
"gravatar_id": "",
"url": "https://api.github.com/users/A",
"html_url": "https://github.com/A",
"followers_url": "https://api.github.com/users/A/followers",
"following_url": "https://api.github.com/users/A/following{/other_user}",
"gists_url": "https://api.github.com/users/A/gists{/gist_id}",
"starred_url": "https://api.github.com/users/A/starred{/owner}{/repo}",
"subscriptions_url": "https://api.github.com/users/A/subscriptions",
"organizations_url": "https://api.github.com/users/A/orgs",
"repos_url": "https://api.github.com/users/A/repos",
"events_url": "https://api.github.com/users/A/events{/privacy}",
"received_events_url": "https://api.github.com/users/A/received_events",
"type": "User",
"site_admin": false,
"name": "Shuvalov Anton",
"blog": "http://anto.sh",
"location": "Ho Chi Minh, Vietnam",
"public_repos": 27,
"public_gists": 128,
"followers": 537,
"following": 149,
"created_at": "2012-02-05T14:53:26Z",
"updated_at": "2023-03-26T07:04:29Z"
}
 *
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
