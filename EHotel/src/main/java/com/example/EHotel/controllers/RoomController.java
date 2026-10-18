package com.example.EHotel.controllers;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.EHotel.dtos.BookingDTO;
import com.example.EHotel.dtos.CreateRoomDTO;
import com.example.EHotel.dtos.RoomSearchCriteriaDTO;
import com.example.EHotel.exceptions.BusinessRuleException;
import com.example.EHotel.model.hotel.Booking;
import com.example.EHotel.model.hotel.Hotel;
import com.example.EHotel.model.hotel.Room;
import com.example.EHotel.services.BookingService;
import com.example.EHotel.services.HotelChainService;
import com.example.EHotel.services.HotelService;
import com.example.EHotel.services.RoomService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/room")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;
    private final BookingService bookingService;
    private final HotelService hotelService;
    private final HotelChainService hotelChainService;

    // --- Customer side: search and booking ---

    @GetMapping("/search")
    public String showSearchForm(Model model) {
        RoomSearchCriteriaDTO criteria = new RoomSearchCriteriaDTO();
        criteria.setStartDate(LocalDate.now());
        criteria.setEndDate(LocalDate.now().plusDays(1));
        model.addAttribute("criteria", criteria);
        return searchForm(model);
    }

    @PostMapping("/search")
    public String searchRooms(@Valid @ModelAttribute("criteria") RoomSearchCriteriaDTO criteria,
            BindingResult result, Model model) {
        if (!result.hasErrors()) {
            model.addAttribute("rooms", roomService.findAvailableRooms(criteria));
        }
        return searchForm(model);
    }

    @GetMapping("/book/{idRoom}")
    public String showBookingForm(@PathVariable("idRoom") int idRoom,
            @RequestParam(name = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(name = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            Model model) {
        BookingDTO booking = new BookingDTO();
        booking.setIdRoom(idRoom);
        booking.setStartDate(startDate);
        booking.setEndDate(endDate);
        model.addAttribute("booking", booking);
        model.addAttribute("room", roomService.getRoom(idRoom));
        return "booking-form";
    }

    @PostMapping("/booking")
    public String bookRoom(@Valid @ModelAttribute("booking") BookingDTO bookingDTO, BindingResult result,
            Model model, RedirectAttributes redirect) {
        if (!result.hasErrors()) {
            try {
                Booking booking = bookingService.createBooking(bookingDTO);
                redirect.addFlashAttribute("success", "Réservation n° " + booking.getIdBooking() + " confirmée.");
                return "redirect:/room/search";
            } catch (BusinessRuleException e) {
                result.reject("booking", e.getMessage());
            }
        }
        model.addAttribute("room", roomService.getRoom(bookingDTO.getIdRoom()));
        return "booking-form";
    }

    // --- Manager side: room management ---

    @GetMapping("/list")
    public String listRooms(@RequestParam(name = "hotelId", required = false) Integer hotelId, Model model) {
        List<Hotel> hotels = hotelService.getHotels();
        if (hotelId == null && !hotels.isEmpty()) {
            hotelId = hotels.get(0).getIdHotel();
        }
        model.addAttribute("hotels", hotels);
        model.addAttribute("selectedHotelId", hotelId);
        model.addAttribute("rooms", hotelId == null ? List.of() : roomService.findRoomsByHotelId(hotelId));
        return "room-list";
    }

    @PostMapping("/delete/{id}")
    public String deleteRoom(@PathVariable("id") int id, RedirectAttributes redirect) {
        int hotelId = roomService.deleteRoom(id);
        redirect.addFlashAttribute("success", "Chambre supprimée.");
        return "redirect:/room/list?hotelId=" + hotelId;
    }

    @GetMapping("/update/{id}")
    public String showUpdateRoomForm(@PathVariable("id") int id, Model model) {
        Room room = roomService.getRoom(id);
        model.addAttribute("room", roomService.toDto(room));
        model.addAttribute("hotelName", room.getHotel().getName());
        return "update-room-form";
    }

    @PostMapping("/update/{id}")
    public String updateRoom(@PathVariable("id") int id, @Valid @ModelAttribute("room") CreateRoomDTO roomDTO,
            BindingResult result, Model model, RedirectAttributes redirect) {
        Room existing = roomService.getRoom(id);
        roomDTO.setIdRoom(id);
        roomDTO.setIdHotel(existing.getHotel().getIdHotel());
        if (result.hasErrors()) {
            model.addAttribute("hotelName", existing.getHotel().getName());
            return "update-room-form";
        }
        roomService.updateRoom(id, roomDTO);
        redirect.addFlashAttribute("success", "Chambre modifiée.");
        return "redirect:/room/list?hotelId=" + roomDTO.getIdHotel();
    }

    @GetMapping("/add")
    public String showAddRoomForm(@RequestParam(name = "hotelId", required = false) Integer hotelId, Model model) {
        CreateRoomDTO room = new CreateRoomDTO();
        room.setIdHotel(hotelId);
        model.addAttribute("room", room);
        model.addAttribute("hotels", hotelService.getHotels());
        return "add-room-form";
    }

    @PostMapping("/add")
    public String addRoom(@Valid @ModelAttribute("room") CreateRoomDTO roomDTO, BindingResult result,
            Model model, RedirectAttributes redirect) {
        if (result.hasErrors()) {
            model.addAttribute("hotels", hotelService.getHotels());
            return "add-room-form";
        }
        roomService.addRoom(roomDTO);
        redirect.addFlashAttribute("success", "Chambre ajoutée.");
        return "redirect:/room/list?hotelId=" + roomDTO.getIdHotel();
    }

    private String searchForm(Model model) {
        model.addAttribute("hotelChains", hotelChainService.getHotelChains());
        model.addAttribute("cities", hotelService.getCities());
        return "search";
    }
}
