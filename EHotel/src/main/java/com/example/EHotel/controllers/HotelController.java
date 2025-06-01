package com.example.EHotel.controllers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.EHotel.dtos.CreateHotelDTO;
import com.example.EHotel.model.employee.Employee;
import com.example.EHotel.model.hotel.Hotel;
import com.example.EHotel.model.hotelchain.HotelChain;
import com.example.EHotel.services.EmployeeService;
import com.example.EHotel.services.HotelChainService;
import com.example.EHotel.services.HotelService;

import org.springframework.ui.Model;

@Controller
@RequestMapping("/hotel")
public class HotelController {
    
    @Autowired
    private HotelService hotelService;
    @Autowired
    private HotelChainService hotelChainService;
    @Autowired
    private EmployeeService employeeService;

    @GetMapping("/hotels")
    public String showsHotels(Model model) {
        List<Hotel> hotels = hotelService.getHotels();
        model.addAttribute("hotels", hotels);
        return "hotels";
    }

    @GetMapping("/hotel/add")
    public String showAddHotelForm(Model model) {
        List<HotelChain> hotelChains = hotelChainService.getHotelChains();
        List<Employee> employees = employeeService.getEmployees();
        model.addAttribute("hotel", new CreateHotelDTO());
        model.addAttribute("hotelChains", hotelChains);
        model.addAttribute("employees", employees);

        return "add-hotel-form";
    }

    @PostMapping("/hotel/add")
    public String addHotel(@ModelAttribute("hotel") CreateHotelDTO hotel) {
        Hotel newHotel = new Hotel();
        HotelChain hotelChain = hotelChainService.getHotelChain(hotel.getIdHotelChain());
        Employee manager = employeeService.getEmployee(hotel.getSinManager());

        newHotel.setName(hotel.getName());
        newHotel.setRoomsNumber(hotel.getRoomsNumber());
        newHotel.setStartNumber(hotel.getStartNumber());
        newHotel.setEmail(hotel.getEmail());
        newHotel.setStreetName(hotel.getStreetName());
        newHotel.setStreetNumber(hotel.getStreetNumber());
        newHotel.setCity(hotel.getCity());
        newHotel.setPostalCode(hotel.getPostalCode());
        newHotel.setCountry(hotel.getCountry());
        newHotel.setHotelChain(hotelChain);
        newHotel.setManager(manager);

        System.out.println(newHotel.getIdHotel());

        hotelService.addHotel(newHotel);

        
        return "redirect:/hotel/hotels";
    }

    @GetMapping("/hotel/delete/{id}")
    public String deleteHotel(@ModelAttribute("id") int id) {
        hotelService.deleteHotel(id);
        return "redirect:/hotel/hotels";
    }

    @GetMapping("/hotel/update/{id}")
    public String showUpdateHotelForm(@ModelAttribute("id") int id, Model model) {
        Hotel existingHotel = hotelService.getHotel(id);
        CreateHotelDTO hotelDTO = new CreateHotelDTO();

        hotelDTO.setIdHotel(existingHotel.getIdHotel());
        hotelDTO.setName(existingHotel.getName());
        hotelDTO.setRoomsNumber(existingHotel.getRoomsNumber());
        hotelDTO.setStartNumber(existingHotel.getStartNumber());
        hotelDTO.setEmail(existingHotel.getEmail());
        hotelDTO.setStreetName(existingHotel.getStreetName());
        hotelDTO.setCity(existingHotel.getCity());
        hotelDTO.setPostalCode(existingHotel.getPostalCode());
        hotelDTO.setCountry(existingHotel.getCountry());
        hotelDTO.setIdHotelChain(existingHotel.getHotelChain().getId());
        hotelDTO.setSinManager(existingHotel.getManager().getSinEmployee());

        List<HotelChain> hotelChains = hotelChainService.getHotelChains();
        List<Employee> employees = employeeService.getEmployees();
        model.addAttribute("hotel", hotelDTO);
        model.addAttribute("hotelChains", hotelChains);
        model.addAttribute("employees", employees);

        return "update-hotel-form";
    }

    @PostMapping("/hotel/update/{id}")
    public String updateHotel(@ModelAttribute("id") int id, @ModelAttribute("hotel") CreateHotelDTO hotel) {
        Hotel updatedHotel = hotelService.getHotel(id);
        HotelChain hotelChain = hotelChainService.getHotelChain(hotel.getIdHotelChain());
        Employee manager = employeeService.getEmployee(hotel.getSinManager());

        updatedHotel.setName(hotel.getName());
        updatedHotel.setRoomsNumber(hotel.getRoomsNumber());
        updatedHotel.setStartNumber(hotel.getStartNumber());
        updatedHotel.setEmail(hotel.getEmail());
        updatedHotel.setStreetName(hotel.getStreetName());
        updatedHotel.setCity(hotel.getCity());
        updatedHotel.setPostalCode(hotel.getPostalCode());
        updatedHotel.setCountry(hotel.getCountry());
        updatedHotel.setHotelChain(hotelChain);
        updatedHotel.setManager(manager);

        hotelService.updateHotel(updatedHotel);
        return "redirect:/hotel/hotels";
    }
    

}
