package com.fullteaching.backend.entry;

import com.fullteaching.backend.comment.Comment;
import com.fullteaching.backend.course.Course;
import com.fullteaching.backend.coursedetails.CourseDetails;
import com.fullteaching.backend.coursedetails.CourseDetailsRepository;
import com.fullteaching.backend.forum.Forum;
import com.fullteaching.backend.security.AuthorizationService;
import com.fullteaching.backend.user.User;
import com.fullteaching.backend.user.UserComponent;
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
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(MockitoExtension.class)
class EntryControllerTest {

    @InjectMocks
    public EntryController controller;

    @Mock
    private CourseDetailsRepository courseDetailsRepository;

    @Mock
    private UserComponent user;

    @Mock
    private AuthorizationService authorizationService;

    @Test
    public void testAuthorizedNotNull() {
        Mockito.when(authorizationService.checkBackendLogged()).thenReturn(new ResponseEntity<>(HttpStatus.UNAUTHORIZED));
        ResponseEntity<Object> responseEntity = controller.newEntry(new Entry(), "1");
        Assertions.assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
    }

    @Test
    public void testParseLongCatch() {
        Mockito.when(authorizationService.checkBackendLogged()).thenReturn(null);
        ResponseEntity<Object> responseEntity = controller.newEntry(new Entry(), "abc");
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    public void testAuthorizationServiceNotNull() {
        Mockito.when(authorizationService.checkBackendLogged()).thenReturn(null);
        CourseDetails cd = createCourseDetails(new Forum());
        Mockito.when(courseDetailsRepository.findById(1L)).thenReturn(Optional.of(cd));
        Mockito.when(authorizationService.checkAuthorizationUsers(eq(cd), eq(cd.getCourse().getAttenders()))).thenReturn(new ResponseEntity<Object>(HttpStatus.UNAUTHORIZED));
        ResponseEntity<Object> responseEntity = controller.newEntry(new Entry(), "1");
        Assertions.assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
    }


    @Test
    public void testAuthorizationServiceNull() {
        Forum f = createForum();
        CourseDetails cd = createCourseDetails(f);
        List<Comment> commentList = new ArrayList<Comment>();
        commentList.add(createComment());
        Entry entry = createEntry(commentList);

        Mockito.when(authorizationService.checkBackendLogged()).thenReturn(null);
        Mockito.when(courseDetailsRepository.findById(1L)).thenReturn(Optional.of(cd));
        Mockito.when(authorizationService.checkAuthorizationUsers(eq(cd), eq(cd.getCourse().getAttenders()))).thenReturn(null);
        Mockito.when(user.getLoggedUser()).thenReturn(new User());

        ResponseEntity<Object> responseEntity = controller.newEntry(entry, "1");
        Assertions.assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
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

    private Entry createEntry(List<Comment> commentList) {
        Entry entry = new Entry();
        entry.setComments(commentList);
        return entry;
    }

    private Comment createComment() {
        Comment c = new Comment();
        c.setId(1);
        c.setMessage("msg");
        return c;
    }
}






































