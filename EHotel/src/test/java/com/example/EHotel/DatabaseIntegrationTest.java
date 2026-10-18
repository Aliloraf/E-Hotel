package com.example.EHotel;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import com.example.EHotel.dtos.BookingDTO;
import com.example.EHotel.dtos.CreateRoomDTO;
import com.example.EHotel.dtos.RoomSearchCriteriaDTO;
import com.example.EHotel.exceptions.BusinessRuleException;
import com.example.EHotel.model.hotel.Booking;
import com.example.EHotel.model.hotel.Rental;
import com.example.EHotel.model.hotel.Room;
import com.example.EHotel.services.BookingService;
import com.example.EHotel.services.EmployeeService;
import com.example.EHotel.services.HotelService;
import com.example.EHotel.services.RoomService;

import jakarta.persistence.EntityManager;

/** Exercises the services against schema.sql, including its triggers and cascades. Each test is rolled back. */
@Transactional
class DatabaseIntegrationTest extends PostgresIntegrationTest {

    private static final LocalDate START = LocalDate.now().plusDays(10);
    private static final LocalDate END = START.plusDays(3);

    @Autowired private JdbcTemplate jdbc;
    @Autowired private EntityManager entityManager;
    @Autowired private BookingService bookingService;
    @Autowired private RoomService roomService;
    @Autowired private HotelService hotelService;
    @Autowired private EmployeeService employeeService;

    @Test
    void seedDataIsLoaded() {
        assertThat(count("hotel_chain")).isEqualTo(5);
        assertThat(count("hotel")).isEqualTo(40);
        assertThat(count("room")).isEqualTo(200);
    }

    @Test
    void viewsCanBeQueried() {
        assertThat(jdbc.queryForObject("SELECT total_capacity FROM ViewTotalCapacityByHotel WHERE id_hotel = 1", Long.class))
                .isPositive();
        assertThat(jdbc.queryForList("SELECT * FROM ViewAvailableRoomsByArea")).isNotEmpty();
    }

    @Test
    void addingARoomGeneratesAnIdAfterTheSeedDataAndUpdatesTheHotelRoomCount() {
        int roomsBefore = roomsNumberOfHotel(1);

        Room room = roomService.addRoom(new CreateRoomDTO(null, 101, true, new BigDecimal("89.99"), "City", false, "Double", 1));
        entityManager.flush();

        assertThat(room.getIdRoom()).isGreaterThan(200);
        assertThat(roomsNumberOfHotel(1)).isEqualTo(roomsBefore + 1);
    }

    @Test
    void bookingIsArchivedAndBlocksOverlappingBookings() {
        Booking booking = bookingService.createBooking(newCustomerBooking("123-456-789", 1, START, END));
        entityManager.flush();

        assertThat(jdbc.queryForObject("SELECT COUNT(*) FROM booking_archieve WHERE id_booking = ?", Integer.class,
                booking.getIdBooking())).isEqualTo(1);
        assertThatThrownBy(() -> bookingService.createBooking(returningCustomerBooking("123-456-789", 1, START.plusDays(1), END.plusDays(1))))
                .isInstanceOf(BusinessRuleException.class);
        // A stay starting on the previous stay's departure day does not overlap
        assertThat(bookingService.createBooking(returningCustomerBooking("123-456-789", 1, END, END.plusDays(2)))).isNotNull();
    }

    @Test
    void searchExcludesRoomsBookedForOverlappingDates() {
        bookingService.createBooking(newCustomerBooking("123-456-789", 1, START, END));
        entityManager.flush();

        assertThat(searchRoomIds(START.plusDays(1), END.plusDays(1))).doesNotContain(1).contains(2);
        assertThat(searchRoomIds(END, END.plusDays(2))).contains(1);
    }

    @Test
    void searchAppliesOptionalCriteria() {
        RoomSearchCriteriaDTO criteria = new RoomSearchCriteriaDTO();
        criteria.setStartDate(START);
        criteria.setEndDate(END);
        criteria.setRoomCapacity("Suite");
        criteria.setMaxPrice(new BigDecimal("150"));
        criteria.setCity("Toronto");

        List<Room> rooms = roomService.findAvailableRooms(criteria);

        assertThat(rooms).isNotEmpty().allSatisfy(room -> {
            assertThat(room.getCapacity()).isEqualTo("Suite");
            assertThat(room.getPrice()).isLessThanOrEqualTo(new BigDecimal("150"));
            assertThat(room.getHotel().getCity()).isEqualTo("Toronto");
        });
    }

    @Test
    void convertingABookingCreatesARentalAndMakesTheRoomUnavailable() {
        Booking booking = bookingService.createBooking(newCustomerBooking("123-456-789", 1, START, END));
        entityManager.flush();

        Rental rental = bookingService.convertToRental(booking.getIdBooking());
        entityManager.flush();

        assertThat(count("booking")).isZero();
        assertThat(jdbc.queryForObject("SELECT COUNT(*) FROM rental_archieve WHERE id_rental = ?", Integer.class,
                rental.getIdRental())).isEqualTo(1);
        assertThat(jdbc.queryForObject("SELECT availability FROM room WHERE id_room = 1", Boolean.class)).isFalse();
    }

    @Test
    void deletingAHotelCascadesToItsRoomsAndKeepsTheArchive() {
        Booking booking = bookingService.createBooking(newCustomerBooking("123-456-789", 1, START, END));
        entityManager.flush();
        int chainHotelsBefore = jdbc.queryForObject("SELECT hotels_number FROM hotel_chain WHERE id_hotel_chain = 1", Integer.class);

        hotelService.deleteHotel(1);

        assertThat(jdbc.queryForObject("SELECT COUNT(*) FROM room WHERE id_hotel = 1", Integer.class)).isZero();
        assertThat(count("booking")).isZero();
        assertThat(jdbc.queryForObject("SELECT COUNT(*) FROM booking_archieve WHERE id_booking = ?", Integer.class,
                booking.getIdBooking())).isEqualTo(1);
        assertThat(jdbc.queryForObject("SELECT hotels_number FROM hotel_chain WHERE id_hotel_chain = 1", Integer.class))
                .isEqualTo(chainHotelsBefore - 1);
    }

    @Test
    void deletingAManagerLeavesTheHotelWithoutManager() {
        String manager = jdbc.queryForObject("SELECT sin_manager FROM hotel WHERE id_hotel = 2", String.class);

        employeeService.deleteEmployee(manager);

        assertThat(jdbc.queryForObject("SELECT sin_manager FROM hotel WHERE id_hotel = 2", String.class)).isNull();
    }

    private List<Integer> searchRoomIds(LocalDate start, LocalDate end) {
        RoomSearchCriteriaDTO criteria = new RoomSearchCriteriaDTO();
        criteria.setStartDate(start);
        criteria.setEndDate(end);
        return roomService.findAvailableRooms(criteria).stream().map(Room::getIdRoom).toList();
    }

    private static BookingDTO newCustomerBooking(String sin, int roomId, LocalDate start, LocalDate end) {
        return new BookingDTO(sin, "Ada", "Lovelace", 75, "Laurier Ave E", "Ottawa", "K1N 6N5", "CA", roomId, start, end);
    }

    private static BookingDTO returningCustomerBooking(String sin, int roomId, LocalDate start, LocalDate end) {
        return new BookingDTO(sin, null, null, null, null, null, null, null, roomId, start, end);
    }

    private int count(String table) {
        return jdbc.queryForObject("SELECT COUNT(*) FROM " + table, Integer.class);
    }

    private int roomsNumberOfHotel(int hotelId) {
        return jdbc.queryForObject("SELECT rooms_number FROM hotel WHERE id_hotel = ?", Integer.class, hotelId);
    }
}
