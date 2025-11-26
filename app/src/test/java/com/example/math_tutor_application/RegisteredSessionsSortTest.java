package com.example.math_tutor_application;

import static org.junit.Assert.*;

import com.example.math_tutor_application.uml_classes.RegisteredSessions;

import org.junit.Test;
import java.util.*;

// This test verifies that a list of RegisteredSessions objects
// can be sorted correctly by their startDate field.
public class RegisteredSessionsSortTest {

    @Test
    public void testSessionsAreSortedByStartDate() {
        // Create three random sessions with different start dates.
        // We're using hardcoded timestamps to simulate sessions on different days.
        RegisteredSessions s1 = new RegisteredSessions();
        s1.setStartDate(new com.google.firebase.Timestamp(new Date(1732400000000L)));
        // November 23, 2024

        RegisteredSessions s2 = new RegisteredSessions();
        s2.setStartDate(new com.google.firebase.Timestamp(new Date(1732500000000L)));
        // November 24, 2024

        RegisteredSessions s3 = new RegisteredSessions();
        s3.setStartDate(new com.google.firebase.Timestamp(new Date(1732300000000L)));
        // November 22, 2024

        // Put them in a list in random order
        List<RegisteredSessions> sessions = new ArrayList<>();
        sessions.add(s1);
        sessions.add(s2);
        sessions.add(s3);

        // Sort the list by startDate using a comparator
        sessions.sort((a, b) -> a.getStartDate().compareTo(b.getStartDate()));

        // Verify that the sessions are now in chronological order:
        // earliest first, latest last.
        assertEquals(s3, sessions.get(0)); // Nov 22 should be first
        assertEquals(s1, sessions.get(1)); // Nov 23 should be second
        assertEquals(s2, sessions.get(2)); // Nov 24 should be last
    }
}

