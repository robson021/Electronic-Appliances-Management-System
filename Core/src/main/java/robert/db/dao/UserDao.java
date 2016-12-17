package robert.db.dao;

import javassist.NotFoundException;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import robert.db.entity.*;
import robert.db.repository.*;
import robert.enums.Validation;
import robert.exceptions.ApplianceException;
import robert.exceptions.AuthException;
import robert.svc.appliance.ReservationInfo;
import robert.utils.api.AppLogger;

import java.util.*;

@Component
@Transactional
public class UserDao {

    private final AppLogger log;

    private final UserRepository userRepository;

    private final ApplianceRepository applianceRepository;

    private final BuildingRepository buildingRepository;

    private final RoomRepository roomRepository;

    private final ReservationRepository reservationRepository;

    @Autowired
    public UserDao(AppLogger log, UserRepository userRepository, ApplianceRepository applianceRepository, BuildingRepository buildingRepository,
            RoomRepository roomRepository, ReservationRepository reservationRepository) {
        this.log = log;
        this.userRepository = userRepository;
        this.applianceRepository = applianceRepository;
        this.buildingRepository = buildingRepository;
        this.roomRepository = roomRepository;
        this.reservationRepository = reservationRepository;
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public void deleteUser(User user) {
        userRepository.delete(user);
    }

    public void deleteUser(Long id) {
        userRepository.delete(id);
    }

    public User findUserByEmail(String email) {
        return userRepository.findOneByEmail(email.trim());
    }

    public User findUserById(long id) {
        return userRepository.findOneById(id);
    }

    public Iterable<User> findAllUsers() {
        return userRepository.findAll();
    }

    public void makeReservationForAppliance(String email, long applianceId, Date from, int minutes) throws ApplianceException {

        if ( minutes <= 0 || minutes > Validation.MAX_RESERVATION_TIME_IN_MINUTES ) {
            throw new ApplianceException("Invalid reservation time: " + minutes);
        }

        User user = userRepository.findOneByEmail(email);
        Appliance appliance = applianceRepository.findOne(applianceId);

        Reservation reservation = new Reservation();
        reservation.setAppliance(appliance);
        reservation.setUser(user);
        Date currentDate = new Date();
        if ( from.before(currentDate) ) {
            from = new DateTime(currentDate).plusMinutes(15)
                    .toDate();
        }
        reservation.setValidFrom(from.getTime());
        reservation.setValidTill(new DateTime(from).plusMinutes(minutes)
                .toDate()
                .getTime());

        validReservationTime(appliance, reservation);

        //appliance.addReservation(reservation); // fixme: duplicates reservation in Set
        user.addReservation(reservation);
        reservation.setAppliance(appliance);
        reservation.setUser(user);
        reservation.setAccessToken(UUID.randomUUID()
                .toString());
        userRepository.save(user);
        log.info("New reservation made for:", user.getEmail());
    }

    public Set<Reservation> getUsersReservations(String email) {
        User user = userRepository.findOneByEmail(email);
        if ( user == null || user.getReservations()
                .isEmpty() ) {
            return Collections.emptySet();
        }
        return user.getReservations();
    }

    public String getTokenForMyReservation(long reservationId, String email) throws NotFoundException, AuthException {
        Reservation reservation = reservationRepository.findOne(reservationId);
        validateUsersReservation(reservation, email);
        return reservation.getAccessToken();
    }

    public Set<Reservation> getAllReservationsForAppliance(long applianceId) {
        Appliance appliance = applianceRepository.findOne(applianceId);
        if ( appliance == null || CollectionUtils.isEmpty(appliance.getReservations()) ) {
            return Collections.emptySet();
        }
        return appliance.getReservations();
    }

    public void cancelReservation(String email, Long reservationId) throws Exception {
        Reservation reservation = reservationRepository.findOne(reservationId);
        if ( reservation == null ) {
            throw new NotFoundException("Could not found reservation.");
        }
        User user = reservation.getUser();
        if ( !user.getEmail()
                .equals(email) ) {
            throw new AuthException("Cannot delete reservation '" + reservationId + "' by " + email);
        }
        user.getReservations()
                .remove(reservation);
        userRepository.save(user);
        reservationRepository.delete(reservationId);

    }

    public void deleteBuilding(String buildingNumber) {
        this.buildingRepository.deleteOneByName(buildingNumber.trim()
                .toLowerCase());
    }

    public void renameBuilding(String buildingNumber, String newValue) {
        Building building = this.buildingRepository.findOneByName(buildingNumber.trim()
                .toLowerCase());
        building.setName(newValue);
        buildingRepository.save(building);
    }

    public void deleteRoom(long roomId) {
        Room room = roomRepository.findOne(roomId);
        try {
            Building building = room.getBuilding();
            building.getRooms()
                    .remove(room);
            buildingRepository.save(building);
        } catch (Throwable ignored) {
        } finally {
            roomRepository.delete(room);
        }
    }

    public void renameRoom(long roomId, String newNumber) {
        Room room = roomRepository.findOne(roomId);
        room.setNumber(newNumber);
        roomRepository.save(room);
    }

    public void deleteAppliance(long applianceId) {
        Appliance appliance = applianceRepository.findOne(applianceId);
        try {
            Room room = appliance.getRoom();
            room.getAppliances()
                    .remove(appliance);
            appliance.setRoom(null);
            roomRepository.save(room);
        } catch (Throwable ignored) {
        } finally {
            applianceRepository.delete(applianceId);
        }
    }

    public void renameAppliance(long applianceId, String newName) {
        Appliance appliance = applianceRepository.findOne(applianceId);
        appliance.setName(newName);
        applianceRepository.save(appliance);
    }

    public void setNewAddressForTheAppliance(long applianeId, String newAddress) {
        Appliance appliance = applianceRepository.findOne(applianeId);
        if ( appliance == null || "".equals(newAddress) ) {
            throw new NullPointerException("Cannot find appliance with id '" + applianeId + "'");
        }
        appliance.setAddress(newAddress);
        applianceRepository.save(appliance);
    }

    public ReservationInfo getReservationInfo(long reservationId, String email) throws Exception {
        Reservation reservation = reservationRepository.findOne(reservationId);
        validateUsersReservation(reservation, email);

        ReservationInfo ri = new ReservationInfo();
        ri.setAccessCode(reservation.getAccessToken());
        ri.setApplianceAddress(reservation.getAppliance()
                .getAddress());
        ri.setReservationId(reservationId);
        ri.setTime(reservation.getDurationOfAccess());
        return ri;
    }

    private void validReservationTime(Appliance appliance, Reservation reservation) throws ApplianceException {
        Optional<Reservation> any = appliance.getReservations()
                .stream()
                .filter(r -> ( //
                        (reservation.getValidFrom() < r.getValidTill() && reservation.getValidTill() > r.getValidTill()) ||  //
                                (reservation.getValidFrom() < r.getValidFrom() && reservation.getValidTill() > r.getValidFrom())) //
                )
                .findFirst();
        if ( any.isPresent() ) {
            throw new ApplianceException("Cannot make reservation between these hours.");
        }
    }

    private void validateUsersReservation(Reservation reservation, String email) throws AuthException, NotFoundException {
        if ( reservation == null ) {
            throw new NotFoundException("Reservation with id not found.");
        }
        if ( !reservation.getUser()
                .getEmail()
                .equals(email) ) {
            throw new AuthException(email + " does not have permission to the reservation.");
        }
    }
}
