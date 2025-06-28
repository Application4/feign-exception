package com.javatechie.controller;

import com.javatechie.dto.Course;
import com.javatechie.dto.Rating;
import com.javatechie.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @GetMapping
    public ResponseEntity<List<Course>> getAllCourses(@RequestHeader(name = "X-Request-Source", required = false) String requestSource) {
        // Optionally log or use the requestSource header
        return ResponseEntity.ok(courseService.getAllCourses());
    }

    /**
     * Get a course by its ID
     *
     * @param id            the course ID
     * @param requestSource optional header to indicate request origin
     * @return the course if found, otherwise 404
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getCourse(@PathVariable int id,
                                       @RequestHeader(name = "X-Request-Source") String requestSource) {

        // Custom logic for specific sources
        return switch (requestSource.toLowerCase()) {

            case "udemy" -> (id == 2)
                    ? fetchCourse(id)
                    : ResponseEntity.badRequest().body("Invalid course ID for Udemy " +
                    "source only" + " 'Microservices with Spring Cloud' course available in udemy ");

            case "coursera" -> (id == 1)
                    ? fetchCourse(id)
                    : ResponseEntity.badRequest()
                    .body("Invalid course ID for Coursera. Only 'Spring Boot course' (ID 1) is available in Coursera.");

            case "unacademy" -> fetchCourse(id);

            default -> ResponseEntity.internalServerError().body("Unknown request source: " + requestSource);
        };
    }

    private ResponseEntity<Course> fetchCourse(int id) {
        return ResponseEntity.ok(courseService.getCourseById(id));
    }

    /**
     * Add a rating to a course
     *
     * @param id     the course ID
     * @param rating the rating to add
     * @return success or error message
     */
    @PostMapping("/{id}/ratings")
    public ResponseEntity<String> addRating(@PathVariable int id,
                                            @RequestBody Rating rating) {
        boolean added = courseService.addRating(id, rating);
        if (added) {
            return ResponseEntity.ok("Rating submitted successfully!");
        } else {
            return ResponseEntity.badRequest().body("Course not found.");
        }
    }

}
