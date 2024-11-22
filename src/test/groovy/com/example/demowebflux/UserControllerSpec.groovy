package com.example.demowebflux

import com.example.demowebflux.entity.User
import com.example.demowebflux.service.impl.UserServiceImpl
import org.junit.jupiter.api.Disabled
import org.openapitools.model.UserResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.core.publisher.Mono
import spock.lang.Specification
import spock.lang.Unroll

import java.time.OffsetDateTime

@Disabled
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerSpec extends Specification {

    @Autowired
    WebTestClient webTestClient

    @Autowired
    UserServiceImpl userService

    private static final String AUTH_TOKEN = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJ5b3VyLWFwcCIsInN1YiI6InVzZXIiLCJwYXNzd29yZCI6InBhc3N3b3JkIiwiZXhwIjoxNzMyMzk0OTY2fQ.38QQMxzC82qz5fbDBNdw0mQz-WHtGUdTHhZC9xyoC8E"

    @Unroll
    def "return http status created when use request is valid"() {
        given:
        User user = new User()
        user.setName(request.name)
        user.setEmail(request.email)
        user.setPassword(request.password)
        user.setCreatedDate(OffsetDateTime.now())
        user.setModifiedDate(OffsetDateTime.now())
        user.setLastLogin(OffsetDateTime.now())
        user.setPhones(request.phones)

        when:
        def response = webTestClient.post()
                .uri("/users")
                .header(HttpHeaders.AUTHORIZATION, AUTH_TOKEN)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(user), User.class)
                .exchange()

        then:
        response.expectStatus().isCreated()
                .expectBody(UserResponse.class)
                .value { userResponse ->
                        assert userResponse.getId() != null
                }

        where:
        request << [
                [name: "John Doe", email: "john.doe@example.cl", password: "password1234", phones: [
                        [number: "122112", citycode: "01", contrycode: "51"]
                ]],
                [name: "Jane Doe", email: "jane.doe@example.cl", password: "password1234", phones: [
                        [number: "999999", citycode: "02", contrycode: "56"]
                ]],
        ]
    }

    @Unroll
    def "return bad request when user request is invalid"() {
        given:
        User user = new User()
        user.setName(request.name)
        user.setEmail(request.email)
        user.setPassword(request.password)
        user.setCreatedDate(OffsetDateTime.now())
        user.setModifiedDate(OffsetDateTime.now())
        user.setLastLogin(OffsetDateTime.now())
        user.setPhones(request.phones)

        when:
        def response = webTestClient.post()
                .uri("/users")
                .header(HttpHeaders.AUTHORIZATION, AUTH_TOKEN)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(user), User.class)
                .exchange()

        then:
        response.expectStatus().isBadRequest()

        where:
        request << [
                [name: "John Doe", email: "john.doe@example.com", password: "password", phones: [
                        [number: "122112", citycode: "01", countrycode: "51"]
                ]],
                [name: "Jane Doe", email: "jane.doeexample.cl", password: "password"],
                [name: "Jane Doe", email: "jane.doe@examplecl", password: "password"],
        ]
    }

    @Unroll
    def "return unauthorized status when request sent not has token jwt"() {
        given:
        User user = new User()
        user.setName(request.name)
        user.setEmail(request.email)
        user.setPassword(request.password)
        user.setCreatedDate(OffsetDateTime.now())
        user.setModifiedDate(OffsetDateTime.now())
        user.setLastLogin(OffsetDateTime.now())

        when:
        def response = webTestClient.post()
                .uri("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(user), User.class)
                .exchange()

        then:
        response.expectStatus().isUnauthorized()

        where:
        request << [
                [name: "John Doe", email: "john.doe@example.com", password: "password"],
        ]
    }
}
