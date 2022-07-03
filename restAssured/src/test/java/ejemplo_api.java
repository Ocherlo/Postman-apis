import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

public class ejemplo_api {
    @Test
    public void api_covid_test403(){
        RestAssured.baseURI = String.format("https://regres.in/api/users?page=1");
        Response response=given()
                .log().all()
                .headers("Accept","application/json")
                .get();
        String body_response = response.getBody().prettyPrint();
        String status = String.format(String.valueOf(response.getStatusCode()));

        System.out.println("Status code" + status);
        //Probar el codigo de respuesta
        assertEquals(403,response.getStatusCode());
        //Revisaar que el body response no esté vacío
        assertNotNull(body_response);
        //validar que el body contenga id
        assertTrue(body_response.contains("id"));
    }
}
