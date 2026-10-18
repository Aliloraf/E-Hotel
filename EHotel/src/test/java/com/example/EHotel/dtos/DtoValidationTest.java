package com.example.EHotel.dtos;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

class DtoValidationTest {

    private static ValidatorFactory factory;
    private static Validator validator;

    @BeforeAll
    static void setUp() {
        factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @AfterAll
    static void tearDown() {
        factory.close();
    }

    @Test
    void validBookingHasNoViolations() {
        assertThat(validator.validate(booking(LocalDate.now(), LocalDate.now().plusDays(2)))).isEmpty();
    }

    @Test
    void bookingMustEndAfterItStarts() {
        LocalDate start = LocalDate.now().plusDays(3);
        assertThat(invalidFields(booking(start, start))).containsExactly("endDateAfterStartDate");
    }

    @Test
    void bookingCannotStartInThePast() {
        assertThat(invalidFields(booking(LocalDate.now().minusDays(1), LocalDate.now().plusDays(1))))
                .containsExactly("startDate");
    }

    @ParameterizedTest
    @ValueSource(strings = {"123456789", "12-345-6789", "abc-def-ghi", ""})
    void sinMustUseTheDatabaseFormat(String sin) {
        BookingDTO dto = booking(LocalDate.now(), LocalDate.now().plusDays(1));
        dto.setSinCustomer(sin);
        assertThat(invalidFields(dto)).contains("sinCustomer");
    }

    @ParameterizedTest
    @ValueSource(strings = {"10036", "10036-1234", "K1N 6N5", "k1n6n5", "06010"})
    void acceptsUsCanadianAndMexicanPostalCodes(String postalCode) {
        AddCustomerDTO dto = customer();
        dto.setPostalCode(postalCode);
        assertThat(validator.validate(dto)).isEmpty();
    }

    @ParameterizedTest
    @ValueSource(strings = {"1234", "K1N-6N5", "D1N 6N5", "ABCDE"})
    void rejectsInvalidPostalCodes(String postalCode) {
        AddCustomerDTO dto = customer();
        dto.setPostalCode(postalCode);
        assertThat(invalidFields(dto)).containsExactly("postalCode");
    }

    @Test
    void countryMustBeSupported() {
        AddCustomerDTO dto = customer();
        dto.setCountry("FR");
        assertThat(invalidFields(dto)).containsExactly("country");
    }

    @Test
    void hotelStarsMustBeBetweenOneAndFive() {
        CreateHotelDTO hotel = new CreateHotelDTO(null, "Test", 6, "info@test.com", 1, "Main", "Ottawa", "K1N 6N5", "CA", 1, null);
        assertThat(invalidFields(hotel)).containsExactly("startNumber");
    }

    @Test
    void roomPriceMustBePositive() {
        CreateRoomDTO room = new CreateRoomDTO(null, 1, true, BigDecimal.ZERO, "City", false, "Double", 1);
        assertThat(invalidFields(room)).containsExactly("price");
    }

    @Test
    void searchOnlyRequiresDates() {
        RoomSearchCriteriaDTO criteria = new RoomSearchCriteriaDTO();
        criteria.setStartDate(LocalDate.now());
        criteria.setEndDate(LocalDate.now().plusDays(1));
        assertThat(validator.validate(criteria)).isEmpty();

        assertThat(invalidFields(new RoomSearchCriteriaDTO())).containsExactlyInAnyOrder("startDate", "endDate");
    }

    private Set<String> invalidFields(Object dto) {
        return validator.validate(dto).stream()
                .map(ConstraintViolation::getPropertyPath)
                .map(Object::toString)
                .collect(Collectors.toSet());
    }

    private static BookingDTO booking(LocalDate start, LocalDate end) {
        return new BookingDTO("123-456-789", null, null, null, null, null, null, null, 1, start, end);
    }

    private static AddCustomerDTO customer() {
        return new AddCustomerDTO("123-456-789", "Ada", "Lovelace", 75, "Laurier", "Ottawa", "K1N 6N5", "CA");
    }
}
