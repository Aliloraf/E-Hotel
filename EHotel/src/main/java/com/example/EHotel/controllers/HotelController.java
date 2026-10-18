package com.example.EHotel.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.EHotel.dtos.CreateHotelDTO;
import com.example.EHotel.services.EmployeeService;
import com.example.EHotel.services.HotelChainService;
import com.example.EHotel.services.HotelService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/hotel")
@RequiredArgsConstructor
public class HotelController {

    private final HotelService hotelService;
    private final HotelChainService hotelChainService;
    private final EmployeeService employeeService;

    @GetMapping("/hotels")
    public String showHotels(Model model) {
        model.addAttribute("hotels", hotelService.getHotels());
        return "hotels";
    }

    @GetMapping("/add")
    public String showAddHotelForm(Model model) {
        model.addAttribute("hotel", new CreateHotelDTO());
        return hotelForm("add-hotel-form", model);
    }

    @PostMapping("/add")
    public String addHotel(@Valid @ModelAttribute("hotel") CreateHotelDTO hotelDTO, BindingResult result,
            Model model, RedirectAttributes redirect) {
        if (result.hasErrors()) {
            return hotelForm("add-hotel-form", model);
        }
        hotelService.addHotel(hotelDTO);
        redirect.addFlashAttribute("success", "Hôtel ajouté.");
        return "redirect:/hotel/hotels";
    }

    @PostMapping("/delete/{id}")
    public String deleteHotel(@PathVariable("id") int id, RedirectAttributes redirect) {
        hotelService.deleteHotel(id);
        redirect.addFlashAttribute("success", "Hôtel supprimé.");
        return "redirect:/hotel/hotels";
    }

    @GetMapping("/update/{id}")
    public String showUpdateHotelForm(@PathVariable("id") int id, Model model) {
        model.addAttribute("hotel", hotelService.toDto(hotelService.getHotel(id)));
        return hotelForm("update-hotel-form", model);
    }

    @PostMapping("/update/{id}")
    public String updateHotel(@PathVariable("id") int id, @Valid @ModelAttribute("hotel") CreateHotelDTO hotelDTO,
            BindingResult result, Model model, RedirectAttributes redirect) {
        hotelDTO.setIdHotel(id);
        if (result.hasErrors()) {
            return hotelForm("update-hotel-form", model);
        }
        hotelService.updateHotel(id, hotelDTO);
        redirect.addFlashAttribute("success", "Hôtel modifié.");
        return "redirect:/hotel/hotels";
    }

    private String hotelForm(String view, Model model) {
        model.addAttribute("hotelChains", hotelChainService.getHotelChains());
        model.addAttribute("managers", employeeService.getManagers());
        return view;
    }
}
