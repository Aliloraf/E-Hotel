package com.example.EHotel.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.EHotel.dtos.BookingDTO;
import com.example.EHotel.exceptions.BusinessRuleException;
import com.example.EHotel.exceptions.ResourceNotFoundException;
import com.example.EHotel.model.customer.Customer;
import com.example.EHotel.model.hotel.Booking;
import com.example.EHotel.model.hotel.Rental;
import com.example.EHotel.model.hotel.Room;
import com.example.EHotel.repositories.customer.CustomerRepository;
import com.example.EHotel.repositories.hotel.BookingRepository;
import com.example.EHotel.repositories.hotel.RentalRepository;
import com.example.EHotel.repositories.hotel.RoomRepository;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

    private static final LocalDate START = LocalDate.now().plusDays(5);
    private static final LocalDate END = START.plusDays(2);
    private static final String SIN = "123-456-789";

    @Mock private BookingRepository bookingRepository;
    @Mock private RentalRepository rentalRepository;
    @Mock private RoomRepository roomRepository;
    @Mock private CustomerRepository customerRepository;
    @InjectMocks private BookingService bookingService;

    private Room room;

    @BeforeEach
    void setUp() {
        room = new Room(7, 101, true, new BigDecimal("70.00"), "City", false, "Double", null);
    }

    @Test
    void bookingForAReturningCustomerReusesTheExistingCustomer() {
        Customer existing = customer();
        when(roomRepository.findByIdForUpdate(7)).thenReturn(Optional.of(room));
        when(customerRepository.findById(SIN)).thenReturn(Optional.of(existing));
        when(bookingRepository.save(any(Booking.class))).thenAnswer(inv -> inv.getArgument(0));

        Booking booking = bookingService.createBooking(dto(null));

        assertThat(booking.getCustomer()).isSameAs(existing);
        assertThat(booking.getRoom()).isSameAs(room);
        assertThat(booking.getStartDate()).isEqualTo(START);
        assertThat(booking.getEndDate()).isEqualTo(END);
        verify(customerRepository, never()).save(any());
    }

    @Test
    void bookingForAnUnknownSinCreatesTheCustomer() {
        when(roomRepository.findByIdForUpdate(7)).thenReturn(Optional.of(room));
        when(customerRepository.findById(SIN)).thenReturn(Optional.empty());
        when(customerRepository.save(any(Customer.class))).thenAnswer(inv -> inv.getArgument(0));
        when(bookingRepository.save(any(Booking.class))).thenAnswer(inv -> inv.getArgument(0));

        bookingService.createBooking(dto("Ada"));

        ArgumentCaptor<Customer> saved = ArgumentCaptor.forClass(Customer.class);
        verify(customerRepository).save(saved.capture());
        assertThat(saved.getValue().getSinCustomer()).isEqualTo(SIN);
        assertThat(saved.getValue().getFirstname()).isEqualTo("Ada");
        assertThat(saved.getValue().getCheckInDate()).isEqualTo(LocalDate.now());
    }

    @Test
    void bookingForAnUnknownSinWithoutDetailsIsRejected() {
        when(roomRepository.findByIdForUpdate(7)).thenReturn(Optional.of(room));
        when(customerRepository.findById(SIN)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookingService.createBooking(dto(null)))
                .isInstanceOf(BusinessRuleException.class);
        verify(bookingRepository, never()).save(any());
    }

    @Test
    void overlappingBookingIsRejected() {
        when(roomRepository.findByIdForUpdate(7)).thenReturn(Optional.of(room));
        when(bookingRepository.existsOverlapping(7, START, END)).thenReturn(true);

        assertThatThrownBy(() -> bookingService.createBooking(dto("Ada")))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessageContaining("déjà réservée");
        verify(bookingRepository, never()).save(any());
    }

    @Test
    void overlappingRentalIsRejected() {
        when(roomRepository.findByIdForUpdate(7)).thenReturn(Optional.of(room));
        when(rentalRepository.existsOverlapping(7, START, END)).thenReturn(true);

        assertThatThrownBy(() -> bookingService.createBooking(dto("Ada")))
                .isInstanceOf(BusinessRuleException.class);
        verify(bookingRepository, never()).save(any());
    }

    @Test
    void unavailableRoomIsRejected() {
        room.setAvailability(false);
        when(roomRepository.findByIdForUpdate(7)).thenReturn(Optional.of(room));

        assertThatThrownBy(() -> bookingService.createBooking(dto("Ada")))
                .isInstanceOf(BusinessRuleException.class);
        verify(bookingRepository, never()).save(any());
    }

    @Test
    void unknownRoomIsReportedAsNotFound() {
        when(roomRepository.findByIdForUpdate(7)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookingService.createBooking(dto("Ada")))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void convertingABookingCopiesItIntoARentalAndRemovesTheBooking() {
        Booking booking = new Booking(customer(), room, START, END);
        booking.setIdBooking(3);
        when(bookingRepository.findById(3)).thenReturn(Optional.of(booking));
        when(rentalRepository.save(any(Rental.class))).thenAnswer(inv -> inv.getArgument(0));

        Rental rental = bookingService.convertToRental(3);

        assertThat(rental.getCustomer()).isSameAs(booking.getCustomer());
        assertThat(rental.getRoom()).isSameAs(room);
        assertThat(rental.getStartDate()).isEqualTo(START);
        assertThat(rental.getEndDate()).isEqualTo(END);
        verify(bookingRepository).delete(booking);
    }

    private static BookingDTO dto(String firstname) {
        if (firstname == null) {
            return new BookingDTO(SIN, null, null, null, null, null, null, null, 7, START, END);
        }
        return new BookingDTO(SIN, firstname, "Lovelace", 75, "Laurier", "Ottawa", "K1N 6N5", "CA", 7, START, END);
    }

    private static Customer customer() {
        return new Customer(SIN, "Ada", "Lovelace", LocalDate.now(), 75, "Laurier", "Ottawa", "K1N 6N5", "CA");
    }
}
