package io.codelex.flightplanner.dto;

import javax.validation.constraints.NotNull;
import java.util.Objects;

public class SearchFlightsRequest {
    @NotNull
    private final String from;
    @NotNull
    private final String to;
    @NotNull
    private final String departureDate;

    public SearchFlightsRequest(String from, String to, String departureDate) {
        this.from = from;
        this.to = to;
        this.departureDate = departureDate;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public String getDepartureDate() {
        return departureDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SearchFlightsRequest that = (SearchFlightsRequest) o;
        return from.equals(that.from) && to.equals(that.to) && departureDate.equals(that.departureDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(from, to, departureDate);
    }
}
