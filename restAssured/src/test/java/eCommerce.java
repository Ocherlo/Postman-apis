import io.qameta.allure.Allure;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.jupiter.api.*;
import java.util.Base64;
import java.util.concurrent.TimeUnit;
import java.sql.Time;
import java.util.concurrent.TimeUnit;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;



@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class eCommerce {

    static private String base_url = "https://webapi.segundamano.mx";
    static private String token_basic1 = "d2F0c29uMTZjX3g3NDByQHllZnguaW5mbzpwYXNzd29yZDEyMw==";
    static private String email = "watson16c_x740r@yefx.info";
    static private String pass = "password123";
    static private String access_token;
    static private String account_id;
    static private String uuid;
    static private String username;
    static private int phoneNumber;
    static private String token2;
    static private String addressID;
    static private String ad_id;

    //@BeforeEach
    private String obtenerToken() {
        RestAssured.baseURI = String.format("%s/nga/api/v1.1/private/accounts", base_url);
        Response response = given().log().all()
                .queryParam("lang", "es")
                .auth().preemptive().basic(email, pass)
                .post();
        String body_response = response.getBody().asString();
        System.out.println("Body response :" + body_response);
        JsonPath jsonResponse = response.jsonPath();
        access_token = jsonResponse.get("access_token");
        account_id = jsonResponse.get("account.account_id");
        uuid = jsonResponse.get("account.uuid");
        String datos = uuid + ":" + access_token;
        String encodedToken2UP = Base64.getEncoder().encodeToString(datos.getBytes());

        return encodedToken2UP;
    }

    @Test
    @Order(1)
    @DisplayName("Test Case: Validar que se muestren todas las categorias")
    @Severity(SeverityLevel.CRITICAL)
    public void TC01_Obtener_Categorias() {
        RestAssured.baseURI = String.format("%s/nga/api/v1.1/public/categories/filter", base_url);

        Response response = given()
                .log().all()
                .queryParam("lang", "es")
                .get();
        String body_response = response.getBody().asString();
        String headers_response = response.getHeaders().toString();
        System.out.println("Body Response:" + body_response);
        System.out.println("Headers :" + headers_response);
        assertEquals(200, response.getStatusCode());
        assertNotNull(body_response);
        assertTrue(body_response.contains("id"));
    }

    @Test
    @Order(2)
    public void TC02_Obtener_Token_usando_header_authorization() {
        RestAssured.baseURI = String.format("%s/nga/api/v1.1/private/accounts", base_url);

        Response response = given()
                .log().all()
                .queryParam("lang", "es")
                .header("Authorization", "Basic " + token_basic1)
                .post();

        String body_response = response.getBody().asString();
        System.out.println("Body response :" + body_response);
        assertEquals(200, response.getStatusCode());
        assertTrue(response.getTimeIn(TimeUnit.MILLISECONDS) < 2000);
        assertNotNull(body_response);

    }

    @Test
    @Order(3)
    public void TC03_Obtener_toke_usando_authorization_email_pass() {
        RestAssured.baseURI = String.format("%s/nga/api/v1.1/private/accounts", base_url);

        Response response = given()
                .log().all()
                .queryParam("lang", "es")
                .auth().preemptive().basic(email, pass)
                .post();
        String body_response = response.getBody().asString();
        String body_pretty = response.getBody().prettyPrint();
        String headers_response = response.getHeaders().toString();
        System.out.println("Body Response:" + body_pretty);
        System.out.println("Headers :" + headers_response);
        System.out.println("Status Code :" + response.getStatusCode());
        System.out.println("Time response :" + response.getTime());

        assertEquals(200, response.getStatusCode());
        assertTrue(response.getTimeIn(TimeUnit.MILLISECONDS) < 2000);
        assertNotNull(body_response);
        Allure.addAttachment("Body pretty: ", body_pretty);
        JsonPath jsonResponse = response.jsonPath();
        System.out.println("AccessToken " + jsonResponse.get("access_token"));
        access_token = jsonResponse.get("access_token");
        System.out.println("Access Token " + access_token);
        String time2 = String.format(String.valueOf(response.time()));
        Allure.addAttachment("Time", time2);
        System.out.println("Account Id: " + jsonResponse.get("account.account_id"));
        System.out.println("uuid: " + jsonResponse.get("account.uuid"));

        account_id = jsonResponse.get("account.account_id");
        uuid = jsonResponse.get("account.uuid");
    }


    @Test
    @Order(4)
    public void TC04_Editar_datos_usuario() {
        String body_request = "{\n" +
                "    \"account\":\n" +
                "    {\n" +
                "        \"name\":\"testsegundamanos\",\n" +
                "        \"phone\":\"3221827122\",\n" +
                "        \"locations\":[\n" +
                "            {\n" +
                "            \"code\":\"16\",\n" +
                "            \"key\":\"region\",\n" +
                "            \"label\":\"Jalisco\",\n" +
                "            \"locations\":[\n" +
                "                {\n" +
                "                    \"code\":\"633\",\n" +
                "                    \"key\":\"municipality\",\n" +
                "                    \"label\":\"Tala\"\n" +
                "                }]\n" +
                "            }],\n" +
                "        \"professional\":false,\n" +
                "        \"phone_hidden\":false\n" +
                "    }\n" +
                "}";
        RestAssured.baseURI = String.format("%s/nga/api/v1%s", base_url, account_id);

        Response response = given()
                .log().all()
                .queryParam("lang", "es")
                .header("Content-type", "application/json")
                .header("Authorization", "tag:scmcoord.com,2013:api " + access_token)
                .body(body_request)
                .patch();

        String body_response = response.getBody().asString();
        System.out.println("Body response :" + body_response);
        assertEquals(200, response.getStatusCode());
        assertTrue(response.getTimeIn(TimeUnit.MILLISECONDS) < 2000);
        assertNotNull(body_response);
    }


    @Test
    @Order(5)
    public void TC05_CREAR_USUARIOS() {
        String username = "agente_ventas" + (Math.floor(Math.random() * 1534)) + "@mailinator.com";
        double password = (Math.floor(Math.random() * 153123) + 643421);

        String datos = username + ":" + password;
        String encode = Base64.getEncoder().encodeToString(datos.getBytes());
        String body_request = "{\n" +
                "    \"account\":{\n" +
                "        \"email\": \"" + username + "\"\n" +
                "    }\n" +
                "}";

        RestAssured.baseURI = String.format("%s/nga/api/v1.1/private/accounts", base_url);
        Response response = given()
                .log().all()
                .header("Authorization", "Basic " + encode)
                .queryParam("lang", "es")
                .contentType("application/json")
                .body(body_request)
                .post();
        String body_response = response.getBody().asString();
        System.out.println("Body response :" + body_response);
        assertTrue(response.getTimeIn(TimeUnit.MILLISECONDS) < 2000);
        assertEquals(401, response.getStatusCode());
        assertNotNull(body_response);
    }

    @Test
    @Order(6)
    public void TC06_ACTUALIZAR_TELEFONO() {
        phoneNumber = (int) (Math.random() * 999999999 + 321465465);
        String body_request = "{\n" +
                "    \"account\":\n" +
                "    {\n" +
                "        \"name\":\"" + username + "\",\n" +
                "        \"phone\":\"" + phoneNumber + "\",\n" +
                "        \"professional\":false\n" +
                "    }\n" +
                "}";
        RestAssured.baseURI = String.format("%s/nga/api/v1%s", base_url, account_id);
        Response response = given()
                .log().all()
                .header("Authorization", "tag:scmcoord.com,2013:api " + access_token)
                .queryParam("lang", "es")
                .contentType("application/json")
                .accept("application/json, text/plain, */*")
                .body(body_request)
                .patch();
        String body_response = response.getBody().asString();
        System.out.println("Body response :" + body_response);
        assertEquals(200, response.getStatusCode());
        assertNotNull(body_response);
        assertTrue(body_response.contains("phone"));
        JsonPath jsonResponse = response.jsonPath();
        String phoneResposne = jsonResponse.getString("account.phone");
        assertEquals(phoneResposne, "" + phoneNumber);
        assertTrue(response.getTimeIn(TimeUnit.MILLISECONDS) < 2000);
    }

    @Test
    @Order(7)
    public void TC07_CREAR_DIRECCION() {
        String token2UP = uuid + ":" + access_token;
        token2 = Base64.getEncoder().encodeToString(token2UP.getBytes());

        RestAssured.baseURI = String.format("%s/addresses/v1/create", base_url);
        Response response = given().log().all()
                .header("Authorization", "Basic " + token2)
                .header("contect-type", "application/x-www-form-urlencoded")
                .formParam("contact", "Juan Paco")
                .formParam("phone", "3333333333")
                .formParam("rfc", "XAXX010101000")
                .formParam("zipCode", "44000")
                .formParam("exteriorInfo", "calle")
                .formParam("interiorIngo", "12")
                .formParam("region", "16")
                .formParam("municipality", "589")
                .formParam("area", "128361")
                .formParam("alias", "Casa")
                .post();

        String body_response = response.getBody().asString();
        System.out.println("Body response :" + body_response);
        assertEquals(201, response.getStatusCode());
        assertNotNull(body_response);
        assertTrue(response.getTimeIn(TimeUnit.MILLISECONDS) < 2000);
        assertTrue(body_response.contains("addressID"));
        JsonPath jsonResponse = response.jsonPath();
        addressID = jsonResponse.getString("addressID");
    }
    @Test
    @Order(8)
    public void TC08_EDITAR_DIRECCION() {
        String token2UP = uuid + ":" + access_token;
        token2 = Base64.getEncoder().encodeToString(token2UP.getBytes());

        RestAssured.baseURI = String.format("%s/addresses/v1/modify/%s", base_url,addressID);
        Response response = given().log().all()
                .header("Authorization", "Basic " + token2)
                .header("contect-type", "application/x-www-form-urlencoded")
                .formParam("contact", "Juan Paco Pedro")
                .formParam("phone", "3323333332")
                .formParam("rfc", "XEXX010101000")
                .formParam("zipCode", "44000")
                .formParam("exteriorInfo", "calle")
                .formParam("interiorIngo", "12")
                .formParam("region", "16")
                .formParam("municipality", "589")
                .formParam("area", "128361")
                .formParam("alias", "Casa y Oficina")
                .put();

        String body_response = response.getBody().asString();
        System.out.println("Body response :" + body_response);
        assertEquals(200, response.getStatusCode());
        assertNotNull(body_response);
        assertTrue(response.getTimeIn(TimeUnit.MILLISECONDS) < 2000);
        assertTrue(body_response.contains("addressID"));
    }

    @Test
    @Order(9)
    public void TC09_OBTENER_DIRECCIONES() {
        String token2UP = uuid + ":" + access_token;
        token2 = Base64.getEncoder().encodeToString(token2UP.getBytes());
        RestAssured.baseURI = String.format("%s/addresses/v1/get", base_url);
        Response response = given().log().all()
                .header("Authorization", "Basic ZTgzMjc2OTktZTY5Yy00YmM4LThkMDUtYjA5NjNkNjRkNzdkOm1jMXg0YzgzNmFmOTExNWU0OWU4NjdhYzgxYTQ4M2UxOGJhZDA0ZWI5Y2I5X3Yy")
                .get();

        String body_response = response.getBody().asString();
        System.out.println("Body response :" + body_response);
        assertEquals(200, response.getStatusCode());
        assertNotNull(body_response);
        assertTrue(response.getTimeIn(TimeUnit.MILLISECONDS) < 2000);
    }

    @Test
    @Order(10)
    public void TC10_BORRAR_DIRECCION() {
        RestAssured.baseURI = String.format("%s/addresses/v1/delete/%s", base_url, addressID);
        Response response = given().log().all()
                .auth().preemptive().basic(uuid, access_token)
                .delete();
        String body_response = response.getBody().asString();
        System.out.println("Body response :" + body_response);
        assertEquals(200, response.getStatusCode());
        assertNotNull(body_response);
        assertTrue(response.getTimeIn(TimeUnit.MILLISECONDS) < 1000);
        assertTrue(body_response.contains("{\"message\":\"" + addressID + " deleted correctly\"}"));
    }

    @Test
    @Order(11)
    public void TC11_CREAR_ANUNCION() {
        String tokenUp2 = obtenerToken();

        RestAssured.baseURI = String.format("%s/v2/accounts/%s/up", base_url, uuid);
        String body_request = "{\n" +
                "    \"category\":\"8121\",\n" +
                "    \"subject\":\"Mudanzas y fletes baratos a todo mexico\",\n" +
                "    \"body\":\"Si estas buscando una mudanza barata, esta es tu opción. Tenemos cobertura en todo el país\",\n" +
                "    \"region\":\"5\",\"municipality\":\"51\",\"area\":\"140000\",\"price\":\"1\",\"phone_hidden\":\"true\",\"show_phone\":\"false\",\"contact_phone\":\"3333333333\"\n" +
                "}";
        Response response = given().log().all()
                .auth().preemptive().basic(uuid,access_token)
                .header("x-source", "PHOENIX_DESKTOP")
                .header("origin", "https://www.segundamano.mx")
                .contentType("application/json")
                .accept("application/json, text/plain, */*")
                .body(body_request)
                .post();
        String body_response = response.getBody().asString();
        System.out.println("Body response :" + body_response);
        assertTrue(response.getTimeIn(TimeUnit.MILLISECONDS) < 10000);
        assertEquals(200, response.getStatusCode());
        assertNotNull(body_response);
        JsonPath jsonResponse = response.jsonPath();
        ad_id = jsonResponse.getString("data.ad.ad_id");
    }


    @Test
    @Order(12)
    public void TC12_CREAR_ANUNCION_SIN_IMAGEN_COMO_OBLIGATORIO() {
        RestAssured.baseURI = String.format("%s/v2/accounts/%s/up", base_url, uuid);
        String body_request = "{\n" +
                "    \"category\":\"2020\",\n" +
                "    \"subject\":\"Mudanzas y fletes baratos a todo mexico\",\n" +
                "    \"body\":\"Si estas buscando una mudanza barata, esta es tu opción. Tenemos cobertura en todo el país\",\n" +
                "    \"region\":\"5\",\"municipality\":\"51\",\"area\":\"140000\",\"price\":\"1\",\"phone_hidden\":\"true\",\"show_phone\":\"false\",\"contact_phone\":\"3333333333\"\n" +
                "}";
        Response response = given().log().all()
                .auth().preemptive().basic(uuid,access_token)
                .header("x-source", "PHOENIX_DESKTOP")
                .header("origin", "https://www.segundamano.mx")
                .contentType("application/json")
                .accept("application/json, text/plain, */*")
                .body(body_request)
                .post();
        String body_response = response.getBody().asString();
        System.out.println("Body response :" + body_response);
        assertTrue(response.getTimeIn(TimeUnit.MILLISECONDS) < 10000);
        assertEquals(400, response.getStatusCode());
        assertNotNull(body_response);
    }


    @Test
    @Order(13)
    public void TC13_EDITAR_ANUNCION() {
        RestAssured.baseURI = String.format("%s/v2/accounts/%s/up/%s", base_url, uuid,ad_id);
        String body_request = "{\n" +
                "    \"category\":\"8121\",\n" +
                "    \"subject\":\"Mudanzas fletes y más baratos a todo mexico\",\n" +
                "    \"body\":\"Si estas buscando una mudanza baratas, tenemos todo tipo de transporte\",\n" +
                "    \"region\":\"5\",\"municipality\":\"51\",\"area\":\"140000\",\"price\":\"2500\",\"phone_hidden\":\"true\",\"show_phone\":\"false\",\"contact_phone\":\"3333333333\"\n" +
                "}";
        Response response = given().log().all()
                .auth().preemptive().basic(uuid,access_token)
                .header("x-source", "PHOENIX_DESKTOP")
                .header("origin", "https://www.segundamano.mx")
                .contentType("application/json")
                .accept("application/json, text/plain, */*")
                .body(body_request)
                .put();
        String body_response = response.getBody().asString();
        System.out.println("Body response :" + body_response);
        assertTrue(response.getTimeIn(TimeUnit.MILLISECONDS) < 10000);
        assertEquals(200, response.getStatusCode());
        assertNotNull(body_response);
    }
    @Test
    @Order(14)
    public void TC14_OBTENER_REGIONES(){
        RestAssured.baseURI = String.format("%s/nga/api/v1.1/public/regions", base_url);
        Response response = given().log().all()
                .queryParam("depth", "1")
                .queryParam("lang", "es")
                .get();
        String body_response = response.getBody().asString();
        System.out.println("Body response :" + body_response);
        assertTrue(response.getTimeIn(TimeUnit.MILLISECONDS) < 10000);
        assertEquals(200, response.getStatusCode());
        assertNotNull(body_response);
    }

    @Test
    @Order(15)
    public void TC15_GUARDAR_ANUNCIO_COMO_FAVORITO() {
        RestAssured.baseURI = String.format("%s/favorites/v1/private/accounts/938316105",base_url);
        String body_request = "{\"list_ids\":[938316105]}\"";
        Response response = given().log().all()
                .auth().preemptive().basic(uuid,access_token)
                .body(body_request)
                .post();
        String body_response = response.getBody().asString();
        System.out.println("Body response :" + body_response);
        assertTrue(response.getTimeIn(TimeUnit.MILLISECONDS) < 10000);
        assertEquals(200, response.getStatusCode());
        assertNotNull(body_response);
    }

    @Test
    @Order(16)
    public void TC16_VER_ANUNCIONS_PENDIENTES() {
        RestAssured.baseURI = String.format("%s/nga/api/v1%s/klfst?status=pending&lim=20&o=0&query=&lang=es", base_url, account_id);
        Response response = given().log().all()
                .queryParam("status", "pending")
                .queryParam("lim", "10")
                .queryParam("o", "0")
                .queryParam("query", "")
                .queryParam("lang", "es")
                .header("Authorization", "tag:scmcoord.com,2013:api " + access_token)
                .get();
        String body_response = response.getBody().asString();
        System.out.println("Body response :" + body_response);
        assertEquals(200, response.getStatusCode());
        assertTrue(response.getTimeIn(TimeUnit.MILLISECONDS) < 1000);
        assertNotNull(body_response);
    }

    @Test
    @Order(17)
    public void TC17_OBTENER_INFORMACION_DEL_USUARIO() {
        RestAssured.baseURI = String.format("%s/nga/api/v1%s", base_url, account_id);
        Response response = given().log().all()
                .header("Authorization", "tag:scmcoord.com,2013:api " + access_token)
                .queryParam("lang", "es")
                .get();
        String body_response = response.getBody().asString();
        System.out.println("Body response :" + body_response);
        assertEquals(200, response.getStatusCode());
        assertNotNull(body_response);
        assertTrue(response.getTimeIn(TimeUnit.MILLISECONDS) < 1000);
    }

    @Test
    @Order(18)
    public void TC18_OBTENER_ANUNCIOS() {
        RestAssured.baseURI = String.format("%s/listing", base_url);
        Response response = given().log().all()
                .header("Authorization", "tag:scmcoord.com,2013:api " + access_token)
                .queryParam("lang", "es")
                .get();
        String body_response = response.getBody().asString();
        System.out.println("Body response :" + body_response);
        assertEquals(200, response.getStatusCode());
        assertNotNull(body_response);
        assertTrue(response.getTimeIn(TimeUnit.MILLISECONDS) < 1000);
    }

    @Test
    @Order(19)
    public void TC19_OBTENER_BALANCE_DE_USUARIO() {
        RestAssured.baseURI = String.format("%s/credits/v1%s", base_url, account_id);
        Response response = given().log().all()
                .get();
        String body_response = response.getBody().asString();
        System.out.println("Body response :" + body_response);
        assertEquals(200, response.getStatusCode());
        assertNotNull(body_response);
        assertTrue(response.getTimeIn(TimeUnit.MILLISECONDS) < 1000);
    }

    @Test
    @Order(20)
    public void TC20_CAMBIAR_CONTRASENA() {
        String body_request = "{\"account\":{\"password\":\"password123\"}}";
        RestAssured.baseURI = String.format("%s/nga/api/v1/private/accounts/11708897", base_url);
        Response response = given().log().all()
                .queryParam("lang", "es")
                .header("Authorization", "tag:scmcoord.com,2013:api " + access_token)
                .body(body_request)
                .patch();
        String body_response = response.getBody().asString();
        System.out.println("Body response :" + body_response);
        assertEquals(200, response.getStatusCode());
        assertNotNull(body_response);
        assertTrue(response.getTimeIn(TimeUnit.MILLISECONDS) < 2000);
    }

}