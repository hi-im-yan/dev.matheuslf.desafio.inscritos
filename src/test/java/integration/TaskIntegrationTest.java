package integration;

import dev.matheuslf.desafio.inscritos.InscritosApplication;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
public class TaskIntegrationTest {

    @LocalServerPort
    private int port;

    private Integer testProjectId;

    @BeforeEach
    void setup() {
        RestAssured.port = port;
        RestAssured.baseURI = "http://localhost";
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();

        // Create a test project to associate with the task
        testProjectId = createTestProject();
    }

    @Test
    void whenCreateTaskWithValidData_thenStatus201AndTaskReturned() {
        given()
                .contentType(ContentType.JSON)
                .body(String.format("""
                        {
                            "title": "Implement User Authentication",
                            "description": "Add JWT authentication to the application",
                            "status": "TODO",
                            "priority": "HIGH",
                            "dueDate": "15-11-2025",
                            "projectId": %d
                        }
                        """, testProjectId))
                .when()
                .post("/api/tasks")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .body("id", notNullValue())
                .body("title", equalTo("Implement User Authentication"))
                .body("description", equalTo("Add JWT authentication to the application"))
                .body("status", equalTo("TODO"))
                .body("priority", equalTo("HIGH"))
                .body("dueDate", equalTo("15-11-2025"))
                .body("projectId", equalTo(testProjectId));
    }

    @Test
    void whenCreateTaskWithMissingRequiredFields_thenStatus400() {
        given()
                .contentType(ContentType.JSON)
                .body(String.format("""
                        {
                            "description": "Task with missing title",
                            "projectId": %d
                        }
                        """, testProjectId))
                .when()
                .post("/api/tasks")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("status", equalTo(400))
                .body("error", equalTo("Bad Request"))
                .body("message", equalTo("Validation failed"))
                .body("errors", hasItem(containsString("title: Title is required")))
                .body("errors", hasItem(containsString("status: Status is required")))
                .body("errors", hasItem(containsString("priority: Priority is required")));
    }

    @Test
    void whenCreateTaskWithInvalidDate_thenStatus400() {
        given()
                .contentType(ContentType.JSON)
                .body(String.format("""
                        {
                            "title": "Task with invalid date",
                            "status": "TODO",
                            "priority": "MEDIUM",
                            "dueDate": "2025/11/15",
                            "projectId": %d
                        }
                        """, testProjectId))
                .when()
                .post("/api/tasks")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("status", equalTo(400))
                .body("error", equalTo("Bad Request"))
                .body("message", containsString("Invalid date format. Please use dd-MM-yyyy format"));
    }

    @Test
    void whenCreateTaskWithNonExistentProject_thenStatus404() {
        long nonExistentProjectId = 9999L;

        given()
                .contentType(ContentType.JSON)
                .body(String.format("""
                        {
                            "title": "Task for non-existent project",
                            "status": "TODO",
                            "priority": "LOW",
                            "projectId": %d
                        }
                        """, nonExistentProjectId))
                .when()
                .post("/api/tasks")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void whenUpdateTaskWithInvalidStatus_thenStatus400() {
        Integer taskId = createTestTask();
        given()
                .contentType(ContentType.JSON)
                .when()
                .put("/api/tasks/" + taskId + "/status/INVALID_STATUS")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("status", equalTo(400))
                .body("error", equalTo("Bad Request"))
                .body("message", equalTo("The parameter 'status' of value 'INVALID_STATUS' could not be converted to type 'TaskStatusEnum'. Available values:  TODO, DOING, DONE"));
    }

    @Test
    void whenUpdateTaskStatus_thenStatus200AndTaskUpdated() {
        Integer taskId = createTestTask();

        given()
                .contentType(ContentType.JSON)
                .body("""
                        {
                            "status": "DOING"
                        }
                        """)
                .when()
                .patch("/api/tasks/{id}/status", taskId)
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("id", equalTo(taskId))
                .body("status", equalTo("DOING"))
                .body("title", equalTo("Test Task")); // Verify other fields remain unchanged

    }

    @Test
    void whenUpdateStatusOfNonExistentTask_thenStatus404() {
        given()
                .contentType(ContentType.JSON)
                .when()
                .patch("/api/tasks/999999/status/DONE")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void whenDeleteExistingTask_thenStatus204AndTaskIsRemoved() {
        // First create a task to delete
        Integer taskId = createTestTask();

        // Delete the task
        given()
                .contentType(ContentType.JSON)
                .when()
                .delete("/api/tasks/{taskId}", taskId)
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());

    }

    @Test
    void whenDeleteNonExistentTask_thenStatus404() {
        given()
                .contentType(ContentType.JSON)
                .when()
                .delete("/api/tasks/999999")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body("status", equalTo(404))
                .body("error", equalTo("Not Found"))
                .body("message", containsString("Task not found with id: 999999"));
    }

    // Helper method to create a test task and return its ID
    private Integer createTestTask() {
        return given()
                .contentType(ContentType.JSON)
                .body(String.format("""
                        {
                            "title": "Test Task",
                            "description": "Test Description",
                            "status": "TODO",
                            "priority": "MEDIUM",
                            "dueDate": "31-12-2025",
                            "projectId": %d
                        }
                        """, testProjectId))
                .when()
                .post("/api/tasks")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .extract()
                .path("id");
    }

    // Helper method to create a test project
    private Integer createTestProject() {
        return given()
                .contentType(ContentType.JSON)
                .body("""
                        {
                            "name": "Test Project for Tasks",
                            "startDate": "01-11-2025"
                        }
                        """)
                .when()
                .post("/api/projects")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .extract()
                .path("id");
    }
}
