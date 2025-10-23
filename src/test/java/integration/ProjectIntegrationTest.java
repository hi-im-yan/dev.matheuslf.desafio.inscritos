package integration;

import dev.matheuslf.desafio.inscritos.InscritosApplication;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@SpringBootTest(
        classes = InscritosApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ProjectIntegrationTest {

    @LocalServerPort
    private int port;

    private static Long createdProjectId;

    @BeforeEach
    void setup() {
        RestAssured.port = port;
        RestAssured.baseURI = "http://localhost";
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    @Test
    @Order(1)
    void whenCreateProject_thenStatus201AndProjectIsReturned() {
        given()
                .contentType(ContentType.JSON)
                .body("""
                {
                    "name": "Test Project",
                    "description": "Integration Test Project",
                    "startDate": "01-01-2025",
                    "endDate": "31-12-2025"
                }
                """)
                .when()
                .post("/api/projects")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .body("id", notNullValue())
                .body("name", equalTo("Test Project"))
                .body("description", equalTo("Integration Test Project"))
                .body("startDate", equalTo("01-01-2025"))
                .body("endDate", equalTo("31-12-2025"))
                .extract()
                .path("id");
    }

    @Test
    @Order(2)
    void whenGetAllProjects_thenStatus200AndNonEmptyList() {
        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/projects")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("$", hasSize(greaterThan(0)))
                .body("[0].id", notNullValue())
                .body("[0].name", not(emptyString()));
    }

    @Test
    @Order(3)
    void whenCreateProjectWithInvalidStartDate_thenStatus400() {
        given()
                .contentType(ContentType.JSON)
                .body("""
                {
                    "name": "123",
                    "startDate": "aaaa"
                }
                """)
                .when()
                .post("/api/projects")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("status", equalTo(400))
                .body("error", not(emptyString()))
                .body("message", containsString("Please use dd-MM-yyyy"));
    }


    @Test
    @Order(3)
    void whenCreateProjectWithInvalidFieldFormats_thenStatus400() {
        given()
                .contentType(ContentType.JSON)
                .body("""
            {
                "name": "",
                "startDate": ""
            }
            """)
                .when()
                .post("/api/projects")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("status", equalTo(400))
                .body("error", equalTo("Bad Request"))
                .body("message", equalTo("Validation failed"))
                .body("path", equalTo("/api/projects"))
                .body("errors", hasSize(3))
                .body("errors", hasItems(
                        "name: Name is required",
                        "startDate: Start date is required",
                        "name: Name must be between 3 and 100 characters"
                ));
    }

}