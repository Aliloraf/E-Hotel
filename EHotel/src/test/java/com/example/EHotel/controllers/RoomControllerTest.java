package com.example.EHotel.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.hamcrest.Matchers.containsString;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.example.EHotel.dtos.BookingDTO;
import com.example.EHotel.exceptions.BusinessRuleException;
import com.example.EHotel.exceptions.ResourceNotFoundException;
import com.example.EHotel.model.hotel.Booking;
import com.example.EHotel.model.hotel.Hotel;
import com.example.EHotel.model.hotel.Room;
import com.example.EHotel.model.hotelchain.HotelChain;
import com.example.EHotel.services.BookingService;
import com.example.EHotel.services.HotelChainService;
import com.example.EHotel.services.HotelService;
import com.example.EHotel.services.RoomService;

@WebMvcTest(RoomController.class)
class RoomControllerTest {

    private static final String START = LocalDate.now().plusDays(5).toString();
    private static final String END = LocalDate.now().plusDays(7).toString();

    @Autowired private MockMvc mvc;
    @MockBean private RoomService roomService;
    @MockBean private BookingService bookingService;
    @MockBean private HotelService hotelService;
    @MockBean private HotelChainService hotelChainService;

    @BeforeEach
    void setUp() {
        Hotel hotel = new Hotel(1, "Hotel Test", 5, 4, "info@test.com", 1, "Main", "Ottawa", "K1N 6N5", "CA",
                new HotelChain(1, "Chain", 1), null);
        when(roomService.getRoom(7)).thenReturn(new Room(7, 101, true, new BigDecimal("70.00"), "City", false, "Double", hotel));
        when(roomService.getRoom(999)).thenThrow(new ResourceNotFoundException("Chambre introuvable : 999"));
        when(hotelChainService.getHotelChains()).thenReturn(List.of());
        when(hotelService.getCities()).thenReturn(List.of("Ottawa"));
    }

    @Test
    void searchPageRenders() throws Exception {
        mvc.perform(get("/room/search"))
                .andExpect(status().isOk())
                .andExpect(view().name("search"));
    }

    @Test
    void invalidBookingRedisplaysTheFormWithErrors() throws Exception {
        mvc.perform(post("/room/booking")
                        .param("idRoom", "7")
                        .param("sinCustomer", "not-a-sin")
                        .param("startDate", END)
                        .param("endDate", START))
                .andExpect(status().isOk())
                .andExpect(view().name("booking-form"))
                .andExpect(model().attributeHasFieldErrors("booking", "sinCustomer", "endDateAfterStartDate"))
                .andExpect(content().string(containsString("Le NAS doit avoir le format 123-456-789")));
        verify(bookingService, never()).createBooking(any());
    }

    @Test
    void businessRuleViolationIsShownOnTheForm() throws Exception {
        when(bookingService.createBooking(any(BookingDTO.class)))
                .thenThrow(new BusinessRuleException("Cette chambre est déjà réservée ou louée pour ces dates."));

        mvc.perform(post("/room/booking")
                        .param("idRoom", "7")
                        .param("sinCustomer", "123-456-789")
                        .param("startDate", START)
                        .param("endDate", END))
                .andExpect(status().isOk())
                .andExpect(view().name("booking-form"))
                .andExpect(content().string(containsString("déjà réservée ou louée")));
    }

    @Test
    void successfulBookingRedirectsWithConfirmation() throws Exception {
        Booking booking = new Booking();
        booking.setIdBooking(42);
        when(bookingService.createBooking(any(BookingDTO.class))).thenReturn(booking);

        mvc.perform(post("/room/booking")
                        .param("idRoom", "7")
                        .param("sinCustomer", "123-456-789")
                        .param("startDate", START)
                        .param("endDate", END))
                .andExpect(redirectedUrl("/room/search"))
                .andExpect(flash().attribute("success", containsString("42")));
    }

    @Test
    void unknownRoomReturns404Page() throws Exception {
        mvc.perform(get("/room/update/999"))
                .andExpect(status().isNotFound())
                .andExpect(view().name("error"))
                .andExpect(content().string(containsString("Chambre introuvable : 999")));
    }

    @Test
    void deleteRequiresPost() throws Exception {
        mvc.perform(get("/room/delete/7")).andExpect(status().isMethodNotAllowed());
        verify(roomService, never()).deleteRoom(7);
    }
}
