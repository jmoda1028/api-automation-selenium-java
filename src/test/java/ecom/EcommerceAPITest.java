package ecom;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.path.json.JsonPath;

import org.testng.Assert;
import pojo.LoginRequest;
import pojo.LoginResponse;
import pojo.OrderDetail;
import pojo.Orders;

import javax.swing.text.AbstractDocument;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;

public class EcommerceAPITest {

    public static void main(String[] args) {

         // Login to get token and userId
        //SSL
        RequestSpecification req=	new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com")
                .setContentType(ContentType.JSON).build();

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUserEmail("junaidpapandayan23@gmail.com");
        loginRequest.setUserPassword("Haffsah06!");


        RequestSpecification reqLogin =given().relaxedHTTPSValidation().log().all().spec(req).body(loginRequest);
        LoginResponse loginResponse = reqLogin.when().post("/api/ecom/auth/login").then().log().all().extract().response()
                .as(LoginResponse.class);
        System.out.println(loginResponse.getToken());
        String token = loginResponse.getToken();
        System.out.println(loginResponse.getUserId());
        String userId =loginResponse.getUserId();

        //add product
        RequestSpecification addProductBaseReq = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com")
                .addHeader("authorization", token)
                .build();
        RequestSpecification reqAddProduct = given().log().all().spec(addProductBaseReq).param("productName", "Malong")
                .param("productAddedBy", userId).param("productCategory", "traditional-attire")
                .param("productSubCategory", "best-attire").param("productPrice", "1500")
                .param("productDescription", "Batik").param("productFor", "Unisex")
                .multiPart("productImage", new File(System.getProperty("user.dir") + "\\src\\main\\resources\\malong.png"));

        String addProductResponse = reqAddProduct.when().post("/api/ecom/product/add-product")
                .then().log().all().extract().response().asString();
        JsonPath js = new JsonPath(addProductResponse);
        String productId = js.get("productId");

        //create order
        RequestSpecification createOrderBaseReq=	new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com")
                .addHeader("authorization", token).setContentType(ContentType.JSON)
                .build();
        OrderDetail orderDetail = new OrderDetail();
        // can make either 1 or more orders
        orderDetail.setCountry("India");
        orderDetail.setProductOrderedId(productId);
        orderDetail.setCountry("Philippines");
        orderDetail.setProductOrderedId("6701364cae2afd4c0b90113c");

        List<OrderDetail> orderDetailList = new ArrayList<OrderDetail> ();
        orderDetailList.add(orderDetail);
        Orders orders = new Orders();
        orders.setOrders(orderDetailList);

        RequestSpecification createOrderReq=given().log().all().spec(createOrderBaseReq).body(orders);

        String responseAddOrder = createOrderReq.when().post("/api/ecom/order/create-order").then().log().all().extract().response().asString();
        System.out.println(responseAddOrder);

        //delete product

        RequestSpecification deleteProdBaseReq=	new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com")
                .addHeader("authorization", token).setContentType(ContentType.JSON)
                .build();

        RequestSpecification deleteProdReq =given().log().all().spec(deleteProdBaseReq).pathParam("productId",productId);

        String deleteProductResponse = deleteProdReq.when().delete("/api/ecom/product/delete-product/{productId}").then().log().all().
                extract().response().asString();

        JsonPath js1 = new JsonPath(deleteProductResponse);

        Assert.assertEquals("Product Deleted Successfully",js1.get("message"));
    }


}
