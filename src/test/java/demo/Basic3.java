package demo;

import files.ReUsableMethods;
import files.payload;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.testng.Assert;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class Basic3 {

    public static void main(String[] args) {

        //given - all input details
        //when - submit the API, resource, http method
        //Then - validate the response
        RestAssured.baseURI = "https://rahulshettyacademy.com";
        String response = given().log().all()
                .queryParam("key", "qaclick123")
                .header("Content-Type", "application/json")
                .body(payload.AddPlace())
                .when().post("maps/api/place/add/json")
                .then().assertThat().statusCode(200).body("scope", equalTo(("APP")))
                .header("server", "Apache/2.4.52 (Ubuntu)").extract().response().asString();

        System.out.println(response);
//        JsonPath js = new JsonPath(response); //parsing json
        String placeId = ReUsableMethods.rawToJson(response).getString("place_id");

        System.out.println(placeId);

        //update place
        String newAddress = "Summer Walk, Africa";

        given().log().all().queryParam("key", "qaclick123").header("Content-Type","application/json")
                .body("{\n" +
                        "    \"place_id\":\""+placeId+"\",\n" +
                        "    \"address\":\""+newAddress+"\",\n" +
                        "    \"key\":\"qaclick123\"\n" +
                        "}")
                .when().put("maps/api/place/update/json")
                .then().assertThat().log().all().statusCode(200).body("msg", equalTo("Address successfully updated"));

        //get place

        String getPlaceResponse = given().log().all()
                .queryParam("key", "qaclick123")
                .queryParam("place_id", placeId)
                .when().get("maps/api/place/get/json")
                .then().assertThat().log().all().statusCode(200).extract().response().asString();

        String actualAddress = ReUsableMethods.rawToJson(getPlaceResponse).getString("address");

        System.out.println(actualAddress);
        Assert.assertEquals(actualAddress, newAddress);
    }
}
