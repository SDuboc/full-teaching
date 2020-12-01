package com.fullteaching.backend.forum;

import com.fullteaching.backend.course.Course;
import com.fullteaching.backend.coursedetails.CourseDetails;
import com.fullteaching.backend.coursedetails.CourseDetailsRepository;
import com.fullteaching.backend.entry.Entry;
import com.fullteaching.backend.security.AuthorizationService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class ForumControllerTest {

    @InjectMocks
    public ForumController controller;

    @Mock
    private AuthorizationService authorizationService;

    @Mock
    private CourseDetailsRepository courseDetailsRepository;

    @Test
    public void testAuthorizedNotNull() {
        Mockito.when(authorizationService.checkBackendLogged()).thenReturn(new ResponseEntity<>(HttpStatus.UNAUTHORIZED));
        ResponseEntity<Object> responseEntity = controller.modifyForum(true, "1");
        Assertions.assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
    }

    @Test
    public void testParseLongCatch() {
        Mockito.when(authorizationService.checkBackendLogged()).thenReturn(null);
        Mockito.when(authorizationService.checkBackendLogged()).thenReturn(null);
        ResponseEntity<Object> responseEntity = controller.modifyForum(true, "abc");
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    public void testAuthorizationServiceNotNull() {
        CourseDetails cd = createCourseDetails(new Forum());

        Mockito.when(authorizationService.checkBackendLogged()).thenReturn(null);
        Mockito.when(courseDetailsRepository.findById(1L)).thenReturn(Optional.of(cd));
        Mockito.when(authorizationService.checkAuthorization(cd, cd.getCourse().getTeacher())).thenReturn(new ResponseEntity<Object>(HttpStatus.UNAUTHORIZED));

        ResponseEntity<Object> responseEntity = controller.modifyForum(true, "1");
        Assertions.assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
    }

    @Test
    public void testAuthorizationServiceNull() {
        Forum f = createForum();
        CourseDetails cd = createCourseDetails(f);

        Mockito.when(authorizationService.checkBackendLogged()).thenReturn(null);
        Mockito.when(courseDetailsRepository.findById(1L)).thenReturn(Optional.of(cd));
        Mockito.when(authorizationService.checkAuthorization(cd, cd.getCourse().getTeacher())).thenReturn(null);

        ResponseEntity<Object> responseEntity = controller.modifyForum(true, "1");
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    private CourseDetails createCourseDetails(Forum f) {
        CourseDetails cd = new CourseDetails();
        cd.setCourse(new Course());
        cd.setForum(f);
        return cd;
    }

    private Forum createForum() {
        Forum f = new Forum();
        f.setEntries(new ArrayList<Entry>());
        return f;
    }
}