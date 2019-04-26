package com.jpaProject.spring.app.controllers;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.jpaProject.spring.app.models.entity.Cliente;
import com.jpaProject.spring.app.models.service.IClienteService;
import com.jpaProject.spring.app.models.service.IUploadFileService;
import com.jpaProject.spring.app.util.paginator.PageRender;

@Controller
@SessionAttributes("cliente") // usar esto como buena practica para mantener vivo el objeto y asi poderlo
								// modificar
public class ClienteController {

	

	@Autowired
	private IClienteService clienteService;
	@Autowired
	private IUploadFileService uploadFileService;
	

	@GetMapping(value="/uploads/{filename:.+}")// El .+ es una expresion regular que hace que no se incluya la extension es decir el .jpg etc
	public ResponseEntity<Resource> verFoto(@PathVariable String filename){
		Resource recurso = null;
		try {
			recurso = uploadFileService.load(filename);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachement: filename=\""+recurso.getFilename() +"\"").body(recurso);
	}
 	@GetMapping(value="/ver/{id}")
	public String ver(@PathVariable(value= "id")Long id, Map<String, Object> model, RedirectAttributes flash) {
		// Con este metodo redirecciono a otra vista para ver los detalles de la persona mas la imagen
		Cliente cliente= clienteService.findById(id);
		if(cliente == null) {
			flash.addFlashAttribute("error","El cliente no existe en la BD");
			return "redirect:/listarClientes";
		}
		model.put("cliente", cliente);
		model.put("titulo", "Detalle Cliente "+cliente.getNombre()+" "+cliente.getApellido());
		return "ver";
	}
	
	
	
	@GetMapping("/listarClientes")
	public String Listar(@RequestParam(name="page",defaultValue="0") int page ,Model model) {
		// Pageable--> se implementa esta clase para hacer la consulta paginada, siguen siendo los mismos metodos de busqueda y demas porque extiende de serializable
		Pageable pageRequest = PageRequest.of(page,5);
		Page<Cliente> clientes = clienteService.findAll(pageRequest);
		PageRender<Cliente> pageRender = new PageRender<>("/listarClientes",clientes);
		model.addAttribute("titulo", "Listado de clientes");
		model.addAttribute("page",pageRender);
		model.addAttribute("clientes", clientes);
		return "listar";
	}

	@RequestMapping("/form")
	public String crear(Map<String, Object> model) {
		Cliente cliente = new Cliente();
		model.put("cliente", cliente);
		model.put("titulo", "Formulario de cliente");
		return "form";
	}

	@RequestMapping(value = "/form/{id}")
	public String editar(@PathVariable(value = "id") Long id, Map<String, Object> model,RedirectAttributes flash) {
		Cliente cliente = null;
		if (id > 0) {
			cliente = clienteService.findById(id);
		} else {
			flash.addFlashAttribute("error","Id del cliente no puede ser 0");
			return "redirect:/listarClientes";
		}
		model.put("cliente", cliente);
		model.put("titulo", "Editar Cliente");
		return "form";

	}
	
	//el flash lo que hace es mandar un mensaje a una variable llamada success o lo que sea. 
	// que esta definida en una etiqueta en el layout
	
	@PostMapping("/form")
	public String guardar(@Valid Cliente cliente, BindingResult result, Model model,@RequestParam("file") MultipartFile foto,RedirectAttributes flash ,SessionStatus status)  {
		if (result.hasErrors()) {
			model.addAttribute("Titulo", "Formulario de Cliente");
			return "form";
		}
		
		if(!foto.isEmpty()) {
			//Con este codigo puedo subir una imagen en mi pagina web
			// Metodo 1->String rootPath = "C://Temp//uploads";
			// Se pondra codigo para eliminar la imagen cuando sea reemplazada o algo asi por el estilo.
			
			if(cliente.getId()!= null && cliente.getId()>0 && cliente.getFoto()!= null && cliente.getFoto().length()>0) {
				uploadFileService.delete(cliente.getFoto());
			}
			String uniqueFileName = null;
			try {
				uniqueFileName = uploadFileService.copy(foto);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			flash.addFlashAttribute("info", "Ha subido correctamente la imagen:" + uniqueFileName + ".");
			cliente.setFoto(uniqueFileName);

		}
		
		String mensaje= (cliente.getId() != null)? "Cliente editado correctamente" : "Cliente creado correctamente";
		clienteService.save(cliente);
		status.setComplete();
		flash.addFlashAttribute("success",mensaje);
		return "redirect:/listarClientes";

	}

	@RequestMapping(value = "/eliminar/{id}")
	public String eliminar(@PathVariable(value = "id") Long id, RedirectAttributes flash) {
		if (id > 0) {
			// Se obtiene el cliente para eliminar la imagen, hay que buscar la ruta
			// absoluta y demas.
			Cliente cliente = clienteService.findById(id);
			clienteService.delete(id);
			flash.addFlashAttribute("success", "Cliente eliminado correctamente");

			if (uploadFileService.delete(cliente.getFoto())) {
				flash.addFlashAttribute("info", "Foto: " + cliente.getFoto() + " Eliminada con exito");
			}
		}
		return "redirect:/listarClientes";
	}

}
