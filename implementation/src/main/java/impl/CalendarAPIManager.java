package impl;

import com.github.scribejava.core.model.OAuth2AccessToken;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import repository.interfaces.ICalendarManager;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

public class CalendarAPIManager implements ICalendarManager {
    public String getEventIdOnDate(OAuth2AccessToken accessToken, LocalDate date) throws IOException {
        // Prepare DateTime values
        LocalDateTime localDateTime = date.atStartOfDay();

        // In a rush so I'll use this even if it's deprecated
        Credential credential = new GoogleCredential().setAccessToken(accessToken.getAccessToken());

        Calendar service = new Calendar.Builder(new NetHttpTransport(), new JacksonFactory(), credential)
                .setApplicationName("Appointment System")
                .build();

        List<Event> events = service.events()
                .list("primary")
                .setMaxResults(10)
                .setOrderBy("startTime")
                .setSingleEvents(true)
                .setFields("items")
                .setTimeMin(toGoogleDateTime(localDateTime, 1))
                .setTimeMax(toGoogleDateTime(localDateTime.plusDays(1), -1))
                .setTimeZone("UTC")
                .execute()
                .getItems();

        if(events.size() == 0)
            return null;

        return events.get(0).getId();
    }

    private DateTime toGoogleDateTime(LocalDateTime localDateTime, int modifier) {
        return new DateTime(localDateTime.toInstant(ZoneOffset.UTC).toEpochMilli() + modifier);
    }
}
