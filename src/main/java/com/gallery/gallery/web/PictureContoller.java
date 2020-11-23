package com.gallery.gallery.web;

import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.Principal;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.gallery.gallery.domain.Picture;
import com.gallery.gallery.domain.PictureRepository;
import com.gallery.gallery.domain.User;
import com.gallery.gallery.domain.UserRepository;

@Controller
public class PictureContoller {
	
	@Autowired
	PictureRepository repository;
	@Autowired
	UserRepository urepository;
	@Autowired
	private Path rootLocation;
	
	@RequestMapping(value="/login")
    public String login() {	
        return "login";
    }	

	@RequestMapping(value="/index")
	public String userPics(Model model) throws Exception {
		
		
		return "index";
}	
	
	@GetMapping("/images")
	public String listUploadedFiles(Model model, Principal principal) throws Exception {

		if (principal == null) {
			return "redirect:/find";
		}

		User user = urepository.findByUsername(principal.getName());

		List<String> stringss = user.getPictureList().stream()
				.map(picture -> this.rootLocation.resolve(picture.getName()))
				.map(path -> MvcUriComponentsBuilder
						.fromMethodName(PictureContoller.class, "serveFile", path.getFileName().toString()).build()
						.toString())				
				.collect(Collectors.toList());
		System.out.println("Kuvien tulostus");
		model.addAttribute("files", stringss);
		model.addAttribute("picture", repository.findAll());
		return "images";
	}
	
	
	
	
	@RequestMapping(value="/addpic")
	public String addPicture(Model model, Picture picture){
		model.addAttribute("picture", picture);
		return "addpic";
	}
	
	@PostMapping("/add")
	public String handleFileUpload(@RequestParam("file")MultipartFile file,  @RequestParam("picture")String type, RedirectAttributes redirectAttributes,
			Principal principal, Model model) throws Exception {

		if (file.getSize() == 0) {
			return "redirect:/addpic";
		}

		String imagePath = this.rootLocation.resolve(file.getOriginalFilename()).toString();

		User user = urepository.findByUsername(principal.getName());

		Set<Picture> stringList = user.getPictureList();
		
		stringList.add(new Picture(imagePath, type));
		Files.copy(file.getInputStream(), this.rootLocation.resolve(imagePath));
		
		
		urepository.save(user);
		System.out.println(urepository.count());
		System.out.print(stringList);

		return "redirect:images";
	}
	
	@GetMapping("/files/{filename:.+}")
	@ResponseBody
	public ResponseEntity<Resource> serveFile(@PathVariable String filename) throws MalformedURLException {

		Path file = this.rootLocation.resolve(filename);
		Resource resource = new UrlResource(file.toUri());

		return ResponseEntity
				.ok()
				.body(resource);	
	}
			

	@RequestMapping(value="/users")
	public String users(Model model) throws Exception {
		model.addAttribute("users", urepository.findAll());
		
		return "users";
}	
	
	
	@GetMapping("/search")
	public String findPhotos(@RequestParam("name") String name, Model model)  {

		User user;
		try {
			user = urepository.findByUsername(name);
		} catch (Exception e) {
			return "redirect:/users";
		}
		
		List<String> userImages = user.getPictureList().stream()
				.map(image -> this.rootLocation.resolve(image.getName()))
				.map(path -> MvcUriComponentsBuilder
						.fromMethodName(PictureContoller.class, "serveFile", path.getFileName().toString()).build()
						.toString())				
				.collect(Collectors.toList());
		
		model.addAttribute("files", userImages);
		model.addAttribute("user", user.getUsername());

		return "userimages";

	}
	
	@RequestMapping("/delete")
	public String deletePic(Principal principal, @RequestParam("name") String name, String text, String yolo) throws Exception {

		User user = urepository.findByUsername(principal.getName());
		
		yolo = name.substring(name.lastIndexOf("/"));
		text = this.rootLocation + yolo;
		text = text.replaceAll("/", "\\\\");

		Picture pic = repository.findByName(text);

		user.getPictureList().remove(pic);

		urepository.save(user);
		repository.delete(pic);
		
		System.out.println("you pushed: " + text + " And the current user is: " + user.getUsername() + " the image is: " + pic);
		

		return "redirect:/images";

	}
	
//	@RequestMapping("/delete/{pictureId}")
//	public String deletePic(@PathVariable("pictureId") Long pictureId, Model model) throws Exception {
//
//		urepository.delete(pictureId);
//		System.out.println("you pushed delete, pictures id: " + pictureId);
//		
//		file.delete()
//	try{
//
//		File file = new File("c:\\logfile20100131.log");
//
//		if(file.delete()){
//			System.out.println(file.getName() + " is deleted!");
//		}else{
//			System.out.println("Delete operation is failed.");
//		
//		return "redirect:../images";
//		
//	}
	
}


