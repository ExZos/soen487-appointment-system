package impl;

import com.github.scribejava.core.model.OAuth2AccessToken;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import org.json.simple.parser.ParseException;
import repository.interfaces.ICalendarManager;
import utilities.ConfigReader;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

public class CalendarAPIManager implements ICalendarManager {
    private String appName;

    private String calendarId;
    private String zoneOffset;

    public CalendarAPIManager() throws IOException, ParseException {
        appName = ConfigReader.GOOGLE_CREDS_FILE.getConfigFileKey("app_name");
        calendarId = "primary";
        zoneOffset = "-04:00";
    }

    public Event getEventOnDate(OAuth2AccessToken accessToken, LocalDate date) throws IOException {
        Calendar service = buildCalendarService(accessToken);

        // Prepare DateTime values
        LocalDateTime localDateTime = date.atStartOfDay();

        List<Event> events = service.events()
                .list(calendarId)
                .setMaxResults(10)
                .setOrderBy("startTime")
                .setSingleEvents(true)
                .setFields("items")
                .setTimeMin(toGoogleDateTime(localDateTime, 1))
                .setTimeMax(toGoogleDateTime(localDateTime.plusDays(1), -1))
                .execute()
                .getItems();

        if(events.size() == 0)
            return null;

        return events.get(0);
    }

    public String getEventIdOnDate(OAuth2AccessToken accessToken, LocalDate date) throws IOException {
        Event event = getEventOnDate(accessToken, date);
        if(event == null)
            return null;

        return event.getId();
    }

    public boolean createEventOnDate(OAuth2AccessToken accessToken, String resourceName, LocalDate date) throws IOException {
        Calendar service = buildCalendarService(accessToken);

        // Prepare DateTime values
        LocalDateTime localDateTime = date.atStartOfDay();

        Event event = new Event();
        event.setSummary(resourceName);
        event.setStart(toGoogleEventDateTime(localDateTime));
        event.setEnd(toGoogleEventDateTime(localDateTime.plusDays(1)));

        event = service.events()
                .insert(calendarId, event)
                .execute();

        if(event == null)
            return false;

        return true;
    }

    public boolean deleteEventOnDate(OAuth2AccessToken accessToken, LocalDate date) throws IOException {
        String eventId = getEventIdOnDate(accessToken, date);
        if(eventId == null)
            return false;

        Calendar service = buildCalendarService(accessToken);
        service.events()
                .delete(calendarId, eventId)
                .execute();

        return true;
    }

    public boolean updateEventOnDate(OAuth2AccessToken accessToken, LocalDate fromDate, LocalDate toDate) throws IOException {
        Event event = getEventOnDate(accessToken, fromDate);
        if(event == null)
            return false;

        Calendar service = buildCalendarService(accessToken);

        // Prepare DateTime values
        LocalDateTime localDateTime = toDate.atStartOfDay();
        event.setStart(toGoogleEventDateTime(localDateTime));
        event.setEnd(toGoogleEventDateTime(localDateTime.plusDays(1)));

        event = service.events()
                .update(calendarId, event.getId(), event)
                .execute();

        if(event == null)
            return false;

        return true;
    }

    private DateTime toGoogleDateTime(LocalDateTime localDateTime, int modifier) {
        return new DateTime(localDateTime.toInstant(ZoneOffset.of(zoneOffset)).toEpochMilli() + modifier);
    }

    private EventDateTime toGoogleEventDateTime(LocalDateTime localDateTime) {
        DateTime dateTime = toGoogleDateTime(localDateTime, 0);

        return new EventDateTime()
                .setDateTime(dateTime);
    }

    private Calendar buildCalendarService(OAuth2AccessToken accessToken) {
        // In a rush so I'll use this even if it's deprecated
        Credential credential = new GoogleCredential().setAccessToken(accessToken.getAccessToken());

        return new Calendar.Builder(new NetHttpTransport(), new JacksonFactory(), credential)
                .setApplicationName(appName)
                .build();
    }
}
