package repository.interfaces;

import com.github.scribejava.core.model.OAuth2AccessToken;

import java.io.IOException;
import java.time.LocalDate;

public interface ICalendarManager extends IManager {
    String getEventIdOnDate(OAuth2AccessToken accessToken, LocalDate date) throws IOException;
}
