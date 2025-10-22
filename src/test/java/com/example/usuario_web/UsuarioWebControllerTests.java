package com.example.usuario_web;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import com.example.usuario_web.controller.UsuarioWebController;
import com.example.usuario_web.model.UsuarioWeb;
import com.example.usuario_web.service.UsuarioWebService;

@WebMvcTest(UsuarioWebController.class)
@Import(UsuarioWebControllerTests.MockConfig.class)
class UsuarioWebControllerTests {

	@Autowired
    private MockMvc mockMvc;

    @Autowired
    private UsuarioWebService service;

    @Test
    void listarTest() throws Exception {
        when(service.listar()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/usuarios"))
                .andExpect(status().isOk())
                .andExpect(view().name("usuarios/lista"))
                .andExpect(model().attributeExists("usuarios"));
    }

    @Test
    void createFormReturnsFormView() throws Exception {
        mockMvc.perform(get("/usuarios/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("usuarios/formulario"))
                .andExpect(model().attributeExists("usuario"));
    }

    @Test
    void saveValidPersonRedirects() throws Exception {
        mockMvc.perform(post("/usuarios")
                        .param("name", "Luis Arturo")                        
                        .param("email", "lrodriguez@gmail.com"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/usuarios"));

        verify(service, times(1)).registrar(any(UsuarioWeb.class));
    } 

    @Test
    void editExistingPersonReturnsForm() throws Exception {
        var person = new UsuarioWeb();
        person.setId(1L);
        person.setName("Luis");
        when(service.obtener(1L)).thenReturn(Optional.of(person));

        mockMvc.perform(get("/usuarios/edit/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("usuarios/formulario"))
                .andExpect(model().attributeExists("usuario"));
    }
    
    @Test
    void deletePersonRedirects() throws Exception {
        mockMvc.perform(post("/usuarios/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/usuarios"));

        verify(service).eliminar(1L);
    }

    @TestConfiguration
    static class MockConfig {
        @Bean
        public UsuarioWebService usuarioWebService() {
            return Mockito.mock(UsuarioWebService.class);
        }
    }

}
