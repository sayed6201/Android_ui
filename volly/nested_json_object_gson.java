====================================================================================
QUESTION: 
How to use gson and volley parse nested json?
Ask Question

====================================================================================


{
     "name" : "Ravi Tamada", 
     "email" : "ravi8x@gmail.com",
     "phone" : 
      {
         "home" : "08947 000000",
         "mobile" : "9999999999"
      }

}



====================================================================================
Answer 1
====================================================================================
--------------------------------------------------------------------------------------
1- You have to update your bean class as follows :-
--------------------------------------------------------------------------------------
public class People implements Serializable {
private String name ;
private String email;
private Phone phone;

 public Phone getPhone () {
    return phone;
}

public void setPhone (Phone phone) {
    this.phone = phone;
}
public String getName () {
    return name;
}

public void setName (String name) {
    this.name = name;
}

public String getEmail () {
    return email;
}

public void setEmail (String email) {
    this.email = email;
}}

--------------------------------------------------------------------------------------
2- Create a new bean class Phone.java :-
--------------------------------------------------------------------------------------
public class Phone implements Serializable{
private String home;
private String mobile;

public String getMobile () {
    return mobile;
}

public void setMobile (String mobile) {
    this.mobile = mobile;
}

public String getHome () {
    return home;
}

public void setHome (String home) {
    this.home = home;
}

}

--------------------------------------------------------------------------------------
3- Now update your code as follows:-
--------------------------------------------------------------------------------------
JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, APITEST,null, new Response.Listener<JSONObject>() {

    @Override
    public void onResponse(JSONObject jsonObject) {

        Gson gson = new Gson();
        People people;
        people = gson.fromJson(jsonObject.toString(),People.class);
        tv_city.setText(""+people.email);
        //for getting Home & Mobile number
          String home=people.getPhone.getHome();
          String mobile=people.getPhone.getMobile();

    }
}, new Response.ErrorListener() {
    @Override
    public void onErrorResponse(VolleyError volleyError) {



    }
});

//Note:- My above bean is as per expected api response in your question. But if you have nested objects then you have to choose either List<Phone> or ArrayList<Phone> inside in your People bean and then create its getters and setters.

//Hope this will help you !!!


//Replace your JavaBean class with

public class People {

@SerializedName("name")
@Expose
private String name;
@SerializedName("email")
@Expose
private String email;
@SerializedName("phone")
@Expose

private Phone phone;

/**
* 
* @return
* The name
*/
public String getName() {
return name;
}

/**
* 
* @param name
* The name
*/
public void setName(String name) {
this.name = name;
}

/**
* 
* @return
* The email
*/
public String getEmail() {
return email;
}

/**
* 
* @param email
* The email
*/
public void setEmail(String email) {
this.email = email;
}

/**
* 
* @return
* The phone
*/
public Phone getPhone() {
return phone;
}

/**
* 
* @param phone
* The phone
*/
public void setPhone(Phone phone) {
this.phone = phone;
}

}
And

public class Phone {

@SerializedName("home")
@Expose
private String home;
@SerializedName("mobile")
@Expose
private String mobile;

/**
* 
* @return
* The home
*/
public String getHome() {
return home;
}

/**
* 
* @param home
* The home
*/
public void setHome(String home) {
this.home = home;
}

/**
* 
* @return
* The mobile
*/
public String getMobile() {
return mobile;
}

/**
* 
* @param mobile
* The mobile
*/
public void setMobile(String mobile) {
this.mobile = mobile;
}

}

//and then you can make call in your JsonResponse like

JSONObject phone=jsonObject.getJSONObject("phone");

String home=phone.getHome();
// will return you the home Number.



you can also get the data directly from the json object like this

if(JsonObject!=null){
String email=JsonObject.getString("email");
}  
OR
To make it work write getters() and setters() in your model object(person object) you can auto generate it too . once you do that get the data like this

JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, APITEST,null, new Response.Listener<JSONObject>() {

        @Override
        public void onResponse(JSONObject jsonObject) {

            Gson gson = new Gson();
            People people;
            people = gson.fromJson(jsonObject.toString(),People.class);
            tv_city.setText(""+people.getEmail());

        }
    }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError volleyError) {



        }
    });