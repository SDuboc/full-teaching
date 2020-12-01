package com.fullteaching.backend.comment;

import com.fullteaching.backend.course.Course;
import com.fullteaching.backend.coursedetails.CourseDetails;
import com.fullteaching.backend.coursedetails.CourseDetailsRepository;
import com.fullteaching.backend.entry.Entry;
import com.fullteaching.backend.entry.EntryRepository;
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
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(MockitoExtension.class)
public class CommentControllerTest {

    @InjectMocks
    public CommentController controller;
    @Mock
    private AuthorizationService authorizationService;
    @Mock
    private CourseDetailsRepository courseDetailsRepository;
    @Mock
    private UserComponent user;
    @Mock
    private EntryRepository entryRepository;
    @Mock
    private CommentRepository commentRepository;


    @Test
    public void testSemAutorizacao() {
        Mockito.when(authorizationService.checkBackendLogged()).thenReturn(new ResponseEntity<>(HttpStatus.UNAUTHORIZED));
        ResponseEntity<Object> responseEntity = controller.newComment(new Comment(), "", "");
        Assertions.assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
    }

    @Test
    public void testAutorizado() {
        Mockito.when(authorizationService.checkBackendLogged()).thenReturn(null);
        ResponseEntity<Object> responseEntity = controller.newComment(new Comment(), "abc", "def");
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    public void testUsuarioNaoAutorizado() {
        Mockito.when(authorizationService.checkBackendLogged()).thenReturn(null);
        CourseDetails cd = createCourseDetails();
        Mockito.when(courseDetailsRepository.findById(123L)).thenReturn(Optional.of(cd));
        Mockito.when(authorizationService.checkAuthorizationUsers(eq(cd), eq(cd.getCourse().getAttenders()))).thenReturn(new ResponseEntity<Object>(HttpStatus.UNAUTHORIZED));
        ResponseEntity<Object> responseEntity = controller.newComment(new Comment(), "123", "123");
        Assertions.assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
    }

    @Test
    public void testUsuarioNaoEncontrado() {
        Mockito.when(authorizationService.checkBackendLogged()).thenReturn(null);
        CourseDetails cd = createCourseDetails();
        Mockito.when(courseDetailsRepository.findById(123L)).thenReturn(Optional.of(cd));
        Mockito.when(authorizationService.checkAuthorizationUsers(eq(cd), eq(cd.getCourse().getAttenders()))).thenReturn(new ResponseEntity<Object>(HttpStatus.NOT_FOUND));
        ResponseEntity<Object> responseEntity = controller.newComment(new Comment(), "123", "123");
        Assertions.assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    public void testCreated() {
        CourseDetails cd = createCourseDetails();
        Entry entry = createEntry();

        Mockito.when(authorizationService.checkBackendLogged()).thenReturn(null);
        Mockito.when(courseDetailsRepository.findById(123L)).thenReturn(Optional.of(cd));
        Mockito.when(authorizationService.checkAuthorizationUsers(eq(cd), eq(cd.getCourse().getAttenders()))).thenReturn(null);
        Mockito.when(user.getLoggedUser()).thenReturn(new User());
        Mockito.when(entryRepository.findById(123L)).thenReturn(Optional.of(entry));

        ResponseEntity<Object> responseEntity = controller.newComment(new Comment(), "123", "123");
        Assertions.assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
    }

    @Test
    public void testBadRequest() {
        CourseDetails cd = createCourseDetails();
        Mockito.when(authorizationService.checkBackendLogged()).thenReturn(null);
        Mockito.when(courseDetailsRepository.findById(123L)).thenReturn(Optional.of(cd));
        Mockito.when(authorizationService.checkAuthorizationUsers(eq(cd), eq(cd.getCourse().getAttenders()))).thenReturn(null);
        Mockito.when(user.getLoggedUser()).thenReturn(new User());
        Mockito.when(entryRepository.findById(123L)).thenReturn(Optional.ofNullable(null));

        ResponseEntity<Object> responseEntity = controller.newComment(new Comment(), "123", "123");
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    public void testCommentParentNotNullCreated() {
        CourseDetails cd = createCourseDetails();
        Comment comment = new Comment("", 12L, new User(), new Comment());

        Mockito.when(authorizationService.checkBackendLogged()).thenReturn(null);
        Mockito.when(courseDetailsRepository.findById(123L)).thenReturn(Optional.of(cd));
        Mockito.when(authorizationService.checkAuthorizationUsers(eq(cd), eq(cd.getCourse().getAttenders()))).thenReturn(null);
        Mockito.when(user.getLoggedUser()).thenReturn(new User());
        Mockito.when(commentRepository.findById(any())).thenReturn(Optional.of(comment));
        Mockito.when(entryRepository.findById(any())).thenReturn(Optional.of(createEntry()));
        ResponseEntity<Object> responseEntity = controller.newComment(comment, "123", "123");
        Assertions.assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
    }

    @Test
    public void testCParentNullBadRequest() {
        CourseDetails cd = createCourseDetails();
        Comment comment = new Comment("", 12L, new User(), new Comment());

        Mockito.when(authorizationService.checkBackendLogged()).thenReturn(null);
        Mockito.when(courseDetailsRepository.findById(123L)).thenReturn(Optional.of(cd));
        Mockito.when(authorizationService.checkAuthorizationUsers(eq(cd), eq(cd.getCourse().getAttenders()))).thenReturn(null);
        Mockito.when(user.getLoggedUser()).thenReturn(new User());
        Mockito.when(commentRepository.findById(any())).thenReturn(Optional.ofNullable(null));
        ResponseEntity<Object> responseEntity = controller.newComment(comment, "123", "123");
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    private Entry createEntry() {
        Entry entry = new Entry();
        entry.setComments(new ArrayList<Comment>());
        return entry;
    }

    private CourseDetails createCourseDetails() {
        CourseDetails cd = new CourseDetails();
        cd.setCourse(new Course());
        cd.setForum(new Forum());
        return cd;
    }

}