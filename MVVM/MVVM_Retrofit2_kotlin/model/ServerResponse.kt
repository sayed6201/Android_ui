package com.sayed.ahmed.vu_kotlin.model

import com.google.gson.annotations.SerializedName

class UserData{
    @SerializedName("page")
    public val page: Int ?= null;

    @SerializedName("per_page")
    public val perPage : Int ?= 0;

    @SerializedName("total")
    public val total : Int ?= 0;

    @SerializedName("total_pages")
    public val totalPages: Int ?= 0;

    @SerializedName("data")
    public val data : ArrayList<User> ?= null;

    @SerializedName("ad")
    public val ad : Ad ?= null;
}


public class User {

    @SerializedName("id")
    public val id: Int ? =null;

    @SerializedName("email")
    public val email: String ? =null;

    @SerializedName("first_name")
    public val firstName: String ? = null;

    @SerializedName("last_name")
    public val lastName : String ? = null;

    @SerializedName("avatar")
    public val avatar: String ? =null;

}

public class Ad {

    @SerializedName("company")
    public val company : String ?= null;

    @SerializedName("url")
    public val url : String ? = null;

    @SerializedName("text")
    public val text : String ? = null;
}