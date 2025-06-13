package api.rewards.TestCases.IntegrationTestCases;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration test class for RewardController.
 * Tests the REST endpoints of the rewards API using a running Spring Boot context.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RewardControllerIntegrationTest {

    @LocalServerPort
    private int port;

    private final RestTemplate restTemplate = new RestTemplate();

    /**
     * Builds the full URL for the API endpoint using the random port.
     * @param path the endpoint path
     * @return the full URL
     */
    private String buildUrl(String path) {
        return "http://localhost:" + port + "/api/rewards" + path;
    }

    /**
     * Tests the /api/rewards/{customerId} endpoint with a valid customer ID.
     * Verifies that the response is OK and contains expected fields.
     */
    @Test
    void testGetRewards_validCustomer() {
        ResponseEntity<Map> response = restTemplate.getForEntity(buildUrl("/C001"), Map.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("C001", response.getBody().get("customerId"));
        assertTrue((Integer) response.getBody().get("totalPoints") >= 0);
    }

    /**
     * Tests the /api/rewards/{customerId} endpoint with an invalid customer ID.
     * Expects a 404 Not Found error and verifies the error message structure.
     */
    @Test
    void testGetRewards_invalidCustomer() {
        try {
            restTemplate.getForEntity(buildUrl("/INVALID"), Map.class);
            fail("Expected 404 exception was not thrown");
        } catch (HttpClientErrorException.NotFound ex) {
            assertEquals(404, ex.getRawStatusCode());

            String responseBody = ex.getResponseBodyAsString();
            System.out.println("Actual response: " + responseBody);

            try {
                ObjectMapper mapper = new ObjectMapper();
                Map<String, Object> errorMap = mapper.readValue(responseBody, Map.class);

                assertEquals("Customer Not Found", errorMap.get("error"));
                assertTrue(((String) errorMap.get("message")).contains("Customer with ID INVALID not found."));
            } catch (Exception parseEx) {
                fail("Failed to parse error response: " + parseEx.getMessage());
            }
        }
    }

    /**
     * Tests the /api/rewards/all endpoint to retrieve rewards for all customers.
     * Verifies that the response is OK and contains a non-null array.
     */
    @Test
    void testGetAllCustomerRewards() {
        ResponseEntity<Object[]> response = restTemplate.getForEntity(buildUrl("/all"), Object[].class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().length >= 0);
    }
}
