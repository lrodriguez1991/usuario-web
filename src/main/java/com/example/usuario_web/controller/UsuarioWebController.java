package com.example.usuario_web.controller;

import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.usuario_web.model.UsuarioWeb;
import com.example.usuario_web.service.UsuarioWebService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/usuarios")
public class UsuarioWebController {	
	
	private final UsuarioWebService service;

	public UsuarioWebController(UsuarioWebService service) {		
		this.service = service;
	}
	
	@GetMapping("/")
    public String home() {
        return "redirect:/usuarios";
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("usuarios", service.listar());
        return "usuarios/lista";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("usuario", new UsuarioWeb());
        return "usuarios/formulario";
    }

    @PostMapping
    public String save(@Valid @ModelAttribute UsuarioWeb usuario, BindingResult br) {
        if (br.hasErrors()) return "usuarios/formulario";
        service.registrar(usuario);
        return "redirect:/usuarios";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        Optional<UsuarioWeb> p = service.obtener(id);
        if (p.isEmpty()) return "redirect:/usuarios";
        model.addAttribute("usuario", p.get());
        return "usuarios/formulario";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        service.eliminar(id);
        return "redirect:/usuarios";
    }
	
	
	

}
