package com.gallery.gallery.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.gallery.gallery.domain.Picture;
import com.gallery.gallery.domain.PictureRepository;

@Controller
public class PictureController {
	@Autowired
	PictureRepository repository;

	@RequestMapping(value="/index")
	public String picture(Model model) {
		model.addAttribute("pics", repository.findAll());
		return "index";
}	
	
	@RequestMapping(value="addpic")
	public String addPicture(Model model){
		model.addAttribute("pic", new Picture());
		return "addpic";
	}
	
	@RequestMapping(value="add", method=RequestMethod.POST)
	public String save(Picture picture){
		repository.save(picture);
		return "redirect:index";
		
	}
	
	@RequestMapping(value="/delete/{id}", method=RequestMethod.GET)
	public String delete(@PathVariable("id") Long id, Model model){
		repository.delete(id);
		return "redirect:../index";
		
	}
}
