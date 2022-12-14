package io.codelex.flightplanner.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

@Entity
@Table(name = "airports")
public class Airport {

    @NotBlank
    private String country;

    @NotBlank
    private String city;

    @NotBlank
    @Id
    private String airport;

    public Airport(String country, String city, String airport) {
        this.country = formatInput(country);
        this.city = formatInput(city);
        this.airport = airport.toUpperCase().trim();
    }

    public Airport() {

    }

    private String formatInput(String input) {
        return Arrays
                .stream(input.split(" "))
                .map(originalInput -> originalInput.substring(0, 1).toUpperCase() + originalInput.substring(1).toLowerCase())
                .collect(Collectors.joining(" ")).trim();
    }

    public String getCountry() {
        return country;
    }

    public String getCity() {
        return city;
    }

    public String getAirport() {
        return airport;
    }

    public void setAirport(String airport) {
        this.airport = airport;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Airport airport1 = (Airport) o;
        return country.equals(airport1.country) && city.equals(airport1.city) && airport.equals(airport1.airport);
    }

    @Override
    public int hashCode() {
        return Objects.hash(country, city, airport);
    }
}
