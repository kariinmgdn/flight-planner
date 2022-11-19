package io.codelex.flightplanner.repository;

import io.codelex.flightplanner.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Repository
public class InMemoryRepository implements FlightRepository {

    private final List<Flight> flightList = new ArrayList<>();

    @Override
    public synchronized Flight addFlight(AddFlightRequest flightRequest) {

        Flight newFlight = new Flight(flightList.size(), flightRequest.getFrom(), flightRequest.getTo(),
                flightRequest.getCarrier(), flightRequest.getDepartureTime(),
                flightRequest.getArrivalTime());

        if (flightList.contains(newFlight)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }

        flightList.add(newFlight);
        return newFlight;
    }

    @Override
    public Flight fetchFlight(int id) {

        Flight flight = flightList.stream().filter(flight1 -> flight1.getId() == id).findAny().orElse(null);
        if (flight == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return flight;
    }

    @Override
    public Flight findFlightById(int id) {

        Flight flight = flightList.stream().filter(flight1 -> flight1.getId() == id).findAny().orElse(null);

        if (flight == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        if (flight.getTo().equals(flight.getFrom()) || flight.getDepartureTime().isAfter(flight.getArrivalTime())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return flight;
    }

    @Override
    public PageResult<Flight> searchFlights(SearchFlightsRequest searchFlightsRequest) {

        List<Flight> flights = new ArrayList<>();

        for (Flight flight : flightList) {

            if (flight.getFrom().getAirport().equals(searchFlightsRequest.getFrom())
                    && flight.getTo().getAirport().equals(searchFlightsRequest.getTo())
                    && flight.getDepartureTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                    .equals(searchFlightsRequest.getDepartureDate())) {
                flights.add(flight);
            }
        }

        return new PageResult<>(0, flights.size(), flights);
    }


    @Override
    public synchronized void deleteFlight(int id) {
        flightList.remove(flightList.stream().filter(flight1 -> flight1.getId() == id).findAny().orElse(null));
    }

    @Override
    public HashSet<Airport> searchAirports(String input) {

        HashSet<Airport> airports = new HashSet<>();
        input = input.toLowerCase().trim();

        for (Flight flight : flightList) {

            if (flight.getFrom().getAirport().toLowerCase().contains(input)
                    || flight.getFrom().getCity().toLowerCase().contains(input)
                    || flight.getFrom().getCountry().toLowerCase().contains(input)) {
                airports.add(flight.getFrom());
            } else if (flight.getTo().getAirport().toLowerCase().contains(input)
                    || flight.getTo().getCity().toLowerCase().contains(input)
                    || flight.getTo().getCountry().toLowerCase().contains(input)) {
                airports.add(flight.getTo());
            }
        }
        return airports;
    }


    @Override
    public void clear() {
        flightList.clear();
    }
}
