package demo;

import files.ReUsableMethods;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

import java.io.File;

import static io.restassured.RestAssured.*;

public class CrateJiraBug {

    public static void main(String[] args){

         RestAssured.baseURI = "https://junaidmoda-team.atlassian.net/";

         String createIssueResponse = given()
                .header("Content-Type","application/json")
                .header("Authorization","Basic anVuYWlkcGFwYW5kYXlhbjIzQGdtYWlsLmNvbTpBVEFUVDN4RmZHRjBMODVHZldvQS1qcDlBdkR2cXNaV1VqcHRydENjRnNia2VpdlRmeFdCSUtUYm4wN0ZybGlfaXJfR205aEJnR2twcC1PREtJd3BaM1FhQVVKa2JNSjcyUWx2Zk5ocDd4NmtRazVqTExjeTNjQnNwa2dXWUVlN08xdU84WDd0S3YyeXpRTVZNTUJnUVFzd3F3ZGR6eFBKVExCZnZEVnRDU2hORV83U2E3ejNKLVE9ODhGRDMwMUI=")
                .body("{\n" +
                        "    \"fields\": {\n" +
                        "       \"project\":\n" +
                        "       {\n" +
                        "          \"key\": \"SCRUM\"\n" +
                        "       },\n" +
                        "       \"summary\": \"Website items are not working-automation Rest Assured\",\n" +
                        "       \"issuetype\": {\n" +
                        "          \"name\": \"Bug\"\n" +
                        "       }\n" +
                        "   }\n" +
                        "}")
                .log().all().post("rest/api/3/issue").then().log().all().assertThat().statusCode(201)
                .extract().response().asString();

        JsonPath js = ReUsableMethods.rawToJson(createIssueResponse);
        String issueId = js.getString("id");
        System.out.println(issueId);

        String filePath = System.getProperty("user.dir") + "/src/main/resources/ss-jira.PNG";

        given()
                .pathParam("key", issueId)
                .header("X-Atlassian-Token","no-check")
                .header("Authorization", "Basic anVuYWlkcGFwYW5kYXlhbjIzQGdtYWlsLmNvbTpBVEFUVDN4RmZHRjBMODVHZldvQS1qcDlBdkR2cXNaV1VqcHRydENjRnNia2VpdlRmeFdCSUtUYm4wN0ZybGlfaXJfR205aEJnR2twcC1PREtJd3BaM1FhQVVKa2JNSjcyUWx2Zk5ocDd4NmtRazVqTExjeTNjQnNwa2dXWUVlN08xdU84WDd0S3YyeXpRTVZNTUJnUVFzd3F3ZGR6eFBKVExCZnZEVnRDU2hORV83U2E3ejNKLVE9ODhGRDMwMUI=")
                .multiPart("file",new File(filePath)).log().all()
                .post("rest/api/3/issue/{key}/attachments").then().log().all().assertThat().statusCode(200);
    }
}
