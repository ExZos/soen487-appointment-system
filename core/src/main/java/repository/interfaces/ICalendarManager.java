package repository.interfaces;

import com.github.scribejava.core.model.OAuth2AccessToken;
import com.google.api.services.calendar.model.Event;

import java.io.IOException;
import java.time.LocalDate;

public interface ICalendarManager extends IManager {
    Event getEventOnDate(OAuth2AccessToken accessToken, String resourceName, LocalDate date) throws IOException;
    String getEventIdOnDate(OAuth2AccessToken accessToken, String resourceName, LocalDate date) throws IOException;
    boolean createEventOnDate(OAuth2AccessToken accessToken, String resourceName, LocalDate date) throws IOException;
    boolean deleteEventOnDate(OAuth2AccessToken accessToken, String resourceName, LocalDate date) throws IOException;
    boolean updateEventOnDate(OAuth2AccessToken accessToken, String resourceName, LocalDate fromDate, LocalDate toDate) throws IOException;
}
