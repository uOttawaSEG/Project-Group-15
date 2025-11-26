package com.example.math_tutor_application;

import static org.junit.Assert.*;

import com.example.math_tutor_application.uml_classes.RegisteredSessions;
import com.google.firebase.Timestamp;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

// This test verifies filtering logic for RegisteredSessions.
// It checks that past sessions and upcoming sessions are correctly separated.
public class RegisteredSessionsFilterTest {

    private List<RegisteredSessions> createSampleSessions() {
        List<RegisteredSessions> sessions = new ArrayList<>();

        // Past session (ended 1 hour ago)
        RegisteredSessions past = new RegisteredSessions();
        past.setCourse("Math Past");
        past.setEndDate(new Timestamp(new Date(System.currentTimeMillis() - 3600000)));
        sessions.add(past);

        // Upcoming session (starts in 1 hour)
        RegisteredSessions upcoming = new RegisteredSessions();
        upcoming.setCourse("Math Upcoming");
        upcoming.setStartDate(new Timestamp(new Date(System.currentTimeMillis() + 3600000)));
        sessions.add(upcoming);

        // Current session (started 10 min ago, ends in 50 min)
        RegisteredSessions current = new RegisteredSessions();
        current.setCourse("Math Current");
        current.setStartDate(new Timestamp(new Date(System.currentTimeMillis() - 600000)));
        current.setEndDate(new Timestamp(new Date(System.currentTimeMillis() + 3000000)));
        sessions.add(current);

        return sessions;
    }

    @Test
    public void testFilterPastSessions() {
        List<RegisteredSessions> sessions = createSampleSessions();

        // Filter using isPastSession()
        List<RegisteredSessions> pastSessions = sessions.stream()
                .filter(RegisteredSessions::isPastSession)
                .collect(Collectors.toList());

        assertEquals(1, pastSessions.size());
        assertEquals("Math Past", pastSessions.get(0).getCourse());
    }

    @Test
    public void testFilterUpcomingSessions() {
        List<RegisteredSessions> sessions = createSampleSessions();

        // Filter using isUpcomingSession()
        List<RegisteredSessions> upcomingSessions = sessions.stream()
                .filter(RegisteredSessions::isUpcomingSession)
                .collect(Collectors.toList());

        assertEquals(1, upcomingSessions.size());
        assertEquals("Math Upcoming", upcomingSessions.get(0).getCourse());
    }

    @Test
    public void testFilterCurrentSessions() {
        List<RegisteredSessions> sessions = createSampleSessions();

        // Current sessions are neither past nor upcoming
        List<RegisteredSessions> currentSessions = sessions.stream()
                .filter(s -> !s.isPastSession() && !s.isUpcomingSession())
                .collect(Collectors.toList());

        assertEquals(1, currentSessions.size());
        assertEquals("Math Current", currentSessions.get(0).getCourse());
    }
}
