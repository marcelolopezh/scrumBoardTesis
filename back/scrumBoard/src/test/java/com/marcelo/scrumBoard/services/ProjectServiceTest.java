package com.marcelo.scrumBoard.services;

import com.marcelo.scrumBoard.models.Project;
import com.marcelo.scrumBoard.models.ResponsePokeApi;
import com.marcelo.scrumBoard.repositories.ProjectRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@RunWith(MockitoJUnitRunner.class)
public class ProjectServiceTest {

    @Mock
    ProjectRepository projectRepository;

    @Mock
    RestTemplate restTemplate;

    @InjectMocks
    ProjectService projectService;

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void findAll() {
        when(projectService.findAll()).thenReturn(anyList());
    }

    @Test
    public void createProject() {
        Project project = new Project();
        when(projectService.createProject(project)).thenReturn(project);
    }

    @Test
    public void findById_Null() {
        assertNull(projectService.findById(1L));
    }

    @Test
    public void findById_NotNull() {
        Project project = new Project();

    }

    @Test
    public void getInfoFromApiOK() throws Exception {
        HttpMethod method = HttpMethod.GET;
        ResponsePokeApi responseExpected = new ResponsePokeApi();
        responseExpected.setId((long) 1);
        responseExpected.setName("pikachu");
        responseExpected.setBase_experience("13");

        ResponseEntity<ResponsePokeApi> expected = new ResponseEntity<>(responseExpected,HttpStatus.OK);
        when(restTemplate.exchange(
                    anyString(),
                    any(HttpMethod.class),
                    any(HttpEntity.class),
                    any(Class.class),
                    anyString()))
                .thenReturn(expected);

        ResponseEntity resp = projectService.getInfoFromApi("Pikachu");
        Assert.assertTrue(resp.getStatusCode().equals(HttpStatus.OK));
    }

    @Test(expected = Exception.class)
    public void getInfoFromApiCatch() throws Exception {
        HttpMethod method = HttpMethod.GET;
        ResponsePokeApi responseExpected = new ResponsePokeApi();
        responseExpected.setId((long) 1);
        responseExpected.setName("pikachu");
        responseExpected.setBase_experience("13");

        when(restTemplate.exchange(
                anyString(),
                any(HttpMethod.class),
                any(HttpEntity.class),
                any(Class.class),
                anyString()))
                .thenThrow(new MockitoException("Err"));

        ResponseEntity resp = projectService.getInfoFromApi("Pikachu");
    }
}