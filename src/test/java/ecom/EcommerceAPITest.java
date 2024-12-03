package ecom;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import pojo.LoginRequest;
import pojo.LoginResponse;

import javax.swing.text.AbstractDocument;

import static io.restassured.RestAssured.given;

public class EcommerceAPITest {

    public static void main(String[] args) {

         // Login to get token and userId
        RequestSpecification req = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com")
                .setContentType(ContentType.JSON).build();

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUserEmail("junaidpapandayan23@gmail.com");
        loginRequest.setUserPassword("Haffsah06!");

        RequestSpecification reqLogin = given().log().all().spec(req).body(loginRequest);
        LoginResponse loginResponse = reqLogin.when().post("/api/ecom/auth/login").then().log().all().extract().response()
                .as(LoginResponse.class);
        System.out.println(loginResponse.getToken());
        System.out.println(loginResponse.getUserId());

        String token = loginResponse.getToken();
        String userId = loginResponse.getUserId();

        //add product
        RequestSpecification addProductBaseReq = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com")
                .addHeader("authorization", token)
                .build();
        given().log().all().spec(addProductBaseReq).param("productName", "Laptop")
                .param("productAddedBy", userId)
    }
}
