package com.example.shareit.booking;

import com.example.shareit.booking.DTO.BookingDto;
import com.example.shareit.booking.DTO.BookingInputDto;
import com.example.shareit.booking.DTO.BookingShortDto;
import com.example.shareit.exception.BookingNotFoundException;
import com.example.shareit.exception.UserNotFoundException;
import com.example.shareit.exception.ValidationException;
import com.example.shareit.service.CheckConsistencyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;
    private final CheckConsistencyService checker;
    @Autowired
    public BookingServiceImpl(BookingRepository bookingRepository, BookingMapper bookingMapper,
                              CheckConsistencyService checker){
        this.bookingRepository = bookingRepository;
        this.bookingMapper = bookingMapper;
        this.checker = checker;
    }
    @Override
    public BookingDto createQuery(BookingInputDto bookingInputDto, Long bookerId) {
        checker.isExistUser(bookerId);
        if (!checker.isAvailableItem(bookingInputDto.getItemId())) {
            throw new ValidationException("Вещь с ID = " + bookingInputDto.getItemId() +
                    "недоступна для бронирования");
        }
        Booking booking = bookingMapper.toBooking(bookingInputDto, bookerId);
        if (bookerId.equals(booking.getItem().getOwner().getId())) {
            throw new ValidationException("Вещь с ID=" + bookingInputDto.getItemId() +
                    " недоступна для бронирования самим владельцем!");
        }
        return bookingMapper.toBookingDto(bookingRepository.save(booking));

    }

    @Override
    public BookingDto update(Long bookingId, Long userId, Boolean approved) {
        checker.isExistUser(userId);
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(
                () -> new BookingNotFoundException("Бронирование с ID = " +
                        bookingId + "не найдено")
        );
        if (booking.getEnd().isBefore(LocalDateTime.now())) {
            throw new ValidationException("Время бронирования уже истекло ");
        }

        if (booking.getBooker().getId().equals(userId)) {
            if (!approved) {
                booking.setStatus(Status.CANCELED);
                log.info("Пользователь с ID={} отменил бронирование с ID={}", userId, bookingId);
            } else {
                throw new UserNotFoundException("Подтвердить бронирование может только владелец вещи");
            }
        } else if (checker.isItemOwner(booking.getItem().getId(), userId) &&
                (!booking.getStatus().equals(Status.CANCELED))) {
            if (!booking.getStatus().equals(Status.WAITING)) {
                throw new ValidationException("Решение по бронированию уже принято");
            }
            if (approved) {
                booking.setStatus(Status.APPROVED);
                log.info("Пользователь с ID={} подтвердил бронирование с ID={}",userId,bookingId);
            } else {
                booking.setStatus(Status.REJECTED);
                log.info("Пользователь с ID={} отклонил бронирование с ID={}",userId,bookingId);
            }
        } else {
            if (booking.getStatus().equals(Status.CANCELED)) {
                throw new ValidationException("Бронирование было отменено");
            } else {
                throw new ValidationException("Подтвердить бронирование может только владелец вещи ");
            }
        }
        return bookingMapper.toBookingDto(bookingRepository.save(booking));
    }

    @Override
    public BookingDto getBookingById(Long bookingId, Long userId) {
        checker.isExistUser(userId);
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(
                () -> new BookingNotFoundException("Бронирование с ID = " +
                        bookingId + "не найдено")
        );
        if (booking.getBooker().getId().equals(userId) ||
                checker.isItemOwner(booking.getItem().getId(), userId)) {
            return bookingMapper.toBookingDto(booking);
        } else {
            throw new UserNotFoundException("Посмотреть данные бронирования может только владелец" +
                    "или бронирующий ее");
        }
    }

    @Override
    public List<BookingDto> getBookingsByBooker(String state, Long userId) {
        checker.isExistUser(userId);
        List<Booking> bookings;
        Sort sort = Sort.by(Sort.Direction.DESC, "start");
        switch (state){
            case "ALL":
                bookings = bookingRepository.findByBookerId(userId, sort);
                break;
            case "CURRENT":
                bookings = bookingRepository.findByBookerIdAndStartIsBeforeAndEndIsAfter(userId,LocalDateTime.now(),
                        LocalDateTime.now(), sort);
                break;
            case "PAST":
                bookings = bookingRepository.findByBookerIdAndEndIsBefore(userId, LocalDateTime.now(), sort);
                break;
            case "FUTURE":
                bookings = bookingRepository.findByBookerIdAndStartIsAfter(userId, LocalDateTime.now(), sort);
                break;
            case "WAITING":
                bookings = bookingRepository.findByBookerIdAndStatus(userId, Status.WAITING, sort);
                break;
            case "REJECTED":
                bookings = bookingRepository.findByBookerIdAndStatus(userId, Status.REJECTED, sort);
                break;
            default:
                throw new ValidationException("Неизвестный state " + state);
        }
        return bookings.stream()
                .map(bookingMapper::toBookingDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookingDto> getBookingsByOwner(String state, Long userId) {
        checker.isExistUser(userId);
        List<Booking> bookings;
        Sort sort = Sort.by(Sort.Direction.DESC, "start");
        switch (state){
            case "ALL":
                bookings = bookingRepository.findByItem_Owner_Id(userId, sort);
                break;
            case "CURRENT":
                bookings = bookingRepository.findByItem_Owner_IdAndStartIsBeforeAndEndIsAfter(userId,LocalDateTime.now(),
                        LocalDateTime.now(), sort);
                break;
            case "PAST":
                bookings = bookingRepository.findByItem_Owner_IdAndEndIsBefore(userId, LocalDateTime.now(), sort);
                break;
            case "FUTURE":
                bookings = bookingRepository.findByItem_Owner_IdAndStartIsAfter(userId, LocalDateTime.now(), sort);
                break;
            case "WAITING":
                bookings = bookingRepository.findByItem_Owner_IdAndStatus(userId, Status.WAITING, sort);
                break;
            case "REJECTED":
                bookings = bookingRepository.findByItem_Owner_IdAndStatus(userId, Status.REJECTED, sort);
                break;
            default:
                throw new ValidationException("Неизвестный state " + state);
        }
        return bookings.stream()
                .map(bookingMapper::toBookingDto)
                .collect(Collectors.toList());
    }

    @Override
    public BookingShortDto getLastBooking(Long itemId) {
        return bookingMapper.toBookingShortDto(
                bookingRepository.findFirstByItem_IdAndEndBeforeOrderByEndDesc(itemId, LocalDateTime.now())
        );
    }

    @Override
    public BookingShortDto getNextBooking(Long itemId) {
        return bookingMapper.toBookingShortDto(
                bookingRepository.findFirstByItem_IdAndStartAfterOrderByStartAsc(itemId, LocalDateTime.now())
        );
    }

    @Override
    public Booking getBookingWithUserBookedItem(Long itemId, Long userId) {
        checker.isExistUser(userId);
        return bookingRepository.findFirstByItem_IdAndBooker_IdAndEndIsBeforeAndStatus(
                itemId,
                userId,
                LocalDateTime.now(),
                Status.APPROVED
        );
    }
}
