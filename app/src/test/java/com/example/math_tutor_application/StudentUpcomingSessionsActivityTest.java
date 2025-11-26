package com.example.math_tutor_application;

import static org.junit.Assert.*;
import org.junit.Test;
import com.example.math_tutor_application.uml_classes.Sessions;
import com.google.firebase.Timestamp;
import java.util.Date;

// This test class verifies the helper methods in Sessions:
// 1. isPastSession() correctly detects sessions that have already ended.
// 2. isUpcomingSession() correctly detects sessions that are scheduled in the future.
public class StudentUpcomingSessionsActivityTest {

    @Test
    public void testIsPastSessionTrue() {
        // Create a session that ended 10 minutes ago
        Sessions s = new Sessions();
        s.setEndDate(new Timestamp(new Date(System.currentTimeMillis() - (10 * 60 * 1000))));

        // Verify that isPastSession() returns true
        assertTrue(s.isPastSession());
    }

    @Test
    public void testIsPastSessionFalse() {
        // Create a session that ends 10 minutes in the future
        Sessions s = new Sessions();
        s.setEndDate(new Timestamp(new Date(System.currentTimeMillis() + (10 * 60 * 1000))));

        // Verify that isPastSession() returns false
        assertFalse(s.isPastSession());
    }

    @Test
    public void testIsUpcomingSessionTrue() {
        // Create a session that starts 30 minutes in the future
        Sessions s = new Sessions();
        s.setStartDate(new Timestamp(new Date(System.currentTimeMillis() + (30 * 60 * 1000))));

        // Verify that isUpcomingSession() returns true
        assertTrue(s.isUpcomingSession());
    }

    @Test
    public void testIsUpcomingSessionFalse() {
        // Create a session that started 30 minutes ago
        Sessions s = new Sessions();
        s.setStartDate(new Timestamp(new Date(System.currentTimeMillis() - (30 * 60 * 1000))));

        // Verify that isUpcomingSession() returns false
        assertFalse(s.isUpcomingSession());
    }
}
