package demo;

import files.ReUsableMethods;
import files.payload;
import io.restassured.path.json.JsonPath;
import org.testng.Assert;
import org.testng.annotations.Test;

public class SumValidation {

    @Test
    public void sumofCourses(){

        int sum = 0;
        JsonPath js = ReUsableMethods.rawToJson(payload.CoursePrice());
        int count = js.getInt("courses.size()");
        for(int i=0; i<count; i++){
            int price = js.getInt("courses["+i+"].price");
            int copies = js.getInt("courses["+i+"].copies");
            int amount = price * copies;
            System.out.println(amount);
            sum = sum + amount;
        }

        System.out.println(sum);
        int purchaseAmount = js.getInt("dashboard.purchaseAmount");
        Assert.assertEquals(sum, purchaseAmount);
    }
}
