package com.javatechie.service;

import com.javatechie.client.CourseClient;
import com.javatechie.dto.Course;
import com.javatechie.dto.Rating;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {

//    @Autowired
//    private CourseRestClientService courseClient;

    @Autowired
    private CourseClient courseClient;


    public Course searchCourse(int courseId,String courseProvider) {
        return courseClient.course(courseId,courseProvider);
    }

    public List<Course> getAllCourses() {
        return courseClient.courses();
    }

    public String submitRating(int courseId, Rating rating) {
        return courseClient.submitRating(courseId, rating);
    }
}
