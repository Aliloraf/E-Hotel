package com.example.EHotel.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.EHotel.dtos.BookingDTO;
import com.example.EHotel.dtos.CreateRoomDTO;
import com.example.EHotel.dtos.RoomSearchByIdHotel;
import com.example.EHotel.dtos.RoomSearchCriteriaDTO;
import com.example.EHotel.model.customer.Customer;
import com.example.EHotel.model.hotel.Booking;
import com.example.EHotel.model.hotel.Hotel;
import com.example.EHotel.model.hotel.Room;
import com.example.EHotel.services.BookingService;
import com.example.EHotel.services.CustomerService;
import com.example.EHotel.services.HotelService;
import com.example.EHotel.services.RoomService;

@Controller
@RequestMapping("/room")
public class RoomController {

    @Autowired
    private RoomService roomService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private BookingService bookingService;
    @Autowired
    private HotelService hotelService;


    @GetMapping("/search")
    public String showSearchForm(Model model) {
        model.addAttribute("criteria", new RoomSearchCriteriaDTO());
        return "search";
    }

    @PostMapping("/search")
    public String searchRooms(@ModelAttribute("criteria") RoomSearchCriteriaDTO criteria, Model model) {
        List<Room> rooms = roomService.findAvailableRooms(criteria);
        model.addAttribute("rooms", rooms);
        return "search";
    }

    @GetMapping("/select/{id}")
    public String selectRoom(@PathVariable("id") int id, Model model) {
        return "redirect:/room/book/" + id;
    }

    @GetMapping("/book/{idRoom}")
    public String showBookingForm(@PathVariable("idRoom") int idRoom, Model model) {
        BookingDTO booking = new BookingDTO();
        booking.setIdRoom(idRoom);
        model.addAttribute("booking", booking);
        return "booking-form";
    }
    @PostMapping("/booking")
    public String bookRoom(@ModelAttribute("booking") BookingDTO bookingDTO) {

        Customer existingCustomer = customerService.getCustomer(bookingDTO.getSinCustomer());

        Customer customer = new Customer();

        if (existingCustomer != null) {
            customer = existingCustomer;
        }else {
            customer = new Customer(
                bookingDTO.getSinCustomer(),
                bookingDTO.getFirstname(),
                bookingDTO.getLastname(),
                bookingDTO.getCheckInDate(),
                bookingDTO.getStreetNumber(),
                bookingDTO.getStreetName(),
                bookingDTO.getCity(),
                bookingDTO.getPostalCode(),
                bookingDTO.getCountry()
            );
            customerService.addCustomer(customer);
        }

        Room room = roomService.findRoomById(bookingDTO.getIdRoom());

        Booking booking = new Booking(
            customer,
            room,
            bookingDTO.getStartDate(),
            bookingDTO.getEndDate()
        );


        bookingService.addBooking(booking);


        return "redirect:/room/search";
    }

    @GetMapping("/list/{idHotel}")
    public String listRooms(@PathVariable("idHotel")  int idHotel ,Model model) {
        List<Room> rooms = roomService.findRoomsByHotelId(idHotel);
        List<Hotel> hotels = hotelService.getHotels();
        RoomSearchByIdHotel roomSearch = new RoomSearchByIdHotel(); 
        model.addAttribute("rooms", rooms);
        model.addAttribute("hotels", hotels);
        model.addAttribute("roomSearch", roomSearch); 
        return "room-list";
    }


    @GetMapping("/delete/{id}")
    public String deleteRoom(@PathVariable("id") int id) {
        roomService.deleteRoom(id);
        return "redirect:/room/list/1";
    }

    @GetMapping("/update/{id}")
    public String showUpdateRoomForm(@PathVariable("id") int id, Model model) {
        Room existingRoom = roomService.findRoomById(id);
        model.addAttribute("room", existingRoom);
        return "update-room-form";
    }

    @PostMapping("/update/{id}")
    public String updateRoom(@PathVariable("id") int id, @ModelAttribute("room") Room room) {
        Room updatedRoom = roomService.findRoomById(id);
        updatedRoom.setRoomNumber(room.getRoomNumber());
        updatedRoom.setAvailability(room.getAvailability());
        updatedRoom.setPrice(room.getPrice());
        updatedRoom.setView(room.getView());
        updatedRoom.setExtensible(room.getExtensible());
        updatedRoom.setCapacity(room.getCapacity());
        roomService.updateRoom(updatedRoom);
        return "redirect:/room/list/1";
    }

    @GetMapping("/add")
    public String showAddRoomForm(Model model) {
    
        List<Hotel> hotels = hotelService.getHotels();
        model.addAttribute("hotels", hotels);
        model.addAttribute("room", new CreateRoomDTO());
     
        return "add-room-form";
    }

    @PostMapping("/add")
    public String addRoom(@ModelAttribute("room") CreateRoomDTO roomInfo) {
        Room room = new Room();
        Hotel hotel = hotelService.getHotel(roomInfo.getIdHotel());

        room.setRoomNumber(roomInfo.getRoomNumber());
        room.setAvailability(roomInfo.getAvailability());
        room.setPrice(roomInfo.getPrice());
        room.setView(roomInfo.getView());
        room.setExtensible(roomInfo.getExtensible());
        room.setCapacity(roomInfo.getCapacity());
        room.setHotel(hotel);


        roomService.saveRoom(room);


        return "redirect:/room/list/1";
    }

}
