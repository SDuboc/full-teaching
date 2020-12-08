package com.fullteaching.backend.filereader;

import com.fullteaching.backend.course.Course;
import com.fullteaching.backend.course.CourseRepository;
import com.fullteaching.backend.file.FileController;
import com.fullteaching.backend.file.FileOperationsService;
import com.fullteaching.backend.security.AuthorizationService;
import com.fullteaching.backend.user.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class FileReaderControllerTest {

    @InjectMocks
    public FileReaderController controller;

    @Mock
    private MultipartHttpServletRequest request;

    @Mock
    private AuthorizationService authorizationService;

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private FileOperationsService fileOperationsService;

    @Test
    public void testAuthorizedNotNull() throws IOException {
        Mockito.when(authorizationService.checkBackendLogged()).thenReturn(new ResponseEntity<>(HttpStatus.UNAUTHORIZED));
        ResponseEntity<Object> responseEntity = controller.handleFileReaderUpload(request, "1");
        Assertions.assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
    }

    @Test
    public void testParseLongCatch() throws IOException {
        Mockito.when(authorizationService.checkBackendLogged()).thenReturn(null);
        ResponseEntity<Object> responseEntity = controller.handleFileReaderUpload(request, "abc");
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    public void testTeacherAuthorizedNotNull() throws IOException {
        Course c = creatCourse();
        Mockito.when(courseRepository.findById(1L)).thenReturn(Optional.of(c));
        Mockito.when(authorizationService.checkAuthorization(c, c.getTeacher())).thenReturn(new ResponseEntity<Object>(HttpStatus.UNAUTHORIZED));
        ResponseEntity<Object> responseEntity = controller.handleFileReaderUpload(request, "1");
        Assertions.assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
    }

    @Test
    public void testRuntimeException() throws IOException {
        Course c = creatCourse();
        List<String> listIterator = creatListIterator();

        Mockito.when(courseRepository.findById(1L)).thenReturn(Optional.of(c));
        Mockito.when(authorizationService.checkAuthorization(c, c.getTeacher())).thenReturn(null);
        Mockito.when(request.getFileNames()).thenReturn(listIterator.iterator());
        Mockito.when(request.getFile("a")).thenReturn(creatMultipartFileEmpty());
        Assertions.assertThrows(RuntimeException.class, () -> {
            controller.handleFileReaderUpload(request, "1");
        });
    }

    @Test
    public void testOk() throws Exception {
        Course c = creatCourse();
        List<String> listIterator = creatListIterator();

        Mockito.when(courseRepository.findById(1L)).thenReturn(Optional.of(c));
        Mockito.when(authorizationService.checkAuthorization(c, c.getTeacher())).thenReturn(null);
        Mockito.when(request.getFileNames()).thenReturn(listIterator.iterator());
        Mockito.when(request.getFile("a")).thenReturn(creatMultipartFileNotEmpty());

        ResponseEntity<Object> responseEntity = controller.handleFileReaderUpload(request, "1");
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void test() throws Exception {
        Course c = creatCourse();
        List<String> listIterator = new ArrayList<>();

        Mockito.when(courseRepository.findById(1L)).thenReturn(Optional.of(c));
        Mockito.when(authorizationService.checkAuthorization(c, c.getTeacher())).thenReturn(null);
        Mockito.when(request.getFileNames()).thenReturn(listIterator.iterator());

        ResponseEntity<Object> responseEntity = controller.handleFileReaderUpload(request, "1");
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }


    private List<String> creatListIterator() {
        List<String> list = new ArrayList<>();
        list.add("a");
        return list;
    }


    private Course creatCourse() {
        Course c = new Course();
        c.setTeacher(new User());
        return c;
    }

    private MultipartFile creatMultipartFileEmpty() {
        MultipartFile multipartFile = new MultipartFile() {

            @Override
            public String getName() {
                return null;
            }

            @Override
            public String getOriginalFilename() {
                return null;
            }

            @Override
            public String getContentType() {
                return null;
            }

            @Override
            public boolean isEmpty() {
                return true;
            }

            @Override
            public long getSize() {
                return 0;
            }

            @Override
            public byte[] getBytes() throws IOException {
                return new byte[0];
            }

            @Override
            public InputStream getInputStream() throws IOException {
                return null;
            }

            @Override
            public void transferTo(File dest) throws IOException, IllegalStateException {

            }


        };
        return multipartFile;
    }

    private MultipartFile creatMultipartFileNotEmpty() {
        MultipartFile multipartFile = new MultipartFile() {

            @Override
            public String getName() {
                return null;
            }

            @Override
            public String getOriginalFilename() {
                return "name";
            }

            @Override
            public String getContentType() {
                return null;
            }

            @Override
            public boolean isEmpty() {
                return false;
            }

            @Override
            public long getSize() {
                return 0;
            }

            @Override
            public byte[] getBytes() throws IOException {
                return new byte[0];
            }

            @Override
            public InputStream getInputStream() throws IOException {
                return null;
            }

            @Override
            public void transferTo(File dest) throws IOException, IllegalStateException {

            }


        };
        return multipartFile;
    }

}