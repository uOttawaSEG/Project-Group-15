package com.example.math_tutor_application;

import static org.junit.Assert.*;

import com.example.math_tutor_application.div4.StudentUpcomingSessionsAdapter;
import com.example.math_tutor_application.uml_classes.RegisteredSessions;
import com.google.firebase.Timestamp;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

// This test verifies StudentUpcomingSessionsAdapter without Android resources or Mockito.
// It checks that the adapter holds the correct data and that the cancel listener is triggered.
public class StudentUpcomingSessionsAdapterTest {

    private List<RegisteredSessions> sessions;
    private boolean cancelClicked;
    private StudentUpcomingSessionsAdapter adapter;

    @Before
    public void setUp() {
        // Create a fake session
        RegisteredSessions s = new RegisteredSessions();
        s.setCourse("Math 101");
        s.setStatus("approved");
        s.setStartDate(new Timestamp(new Date(System.currentTimeMillis() + 600000))); // 10 min later
        s.setEndDate(new Timestamp(new Date(System.currentTimeMillis() + 3600000))); // 1 hour later

        sessions = new ArrayList<>();
        sessions.add(s);

        // Real listener implementation that flips a flag
        cancelClicked = false;
        StudentUpcomingSessionsAdapter.OnCancelClickListener listener =
                session -> cancelClicked = true;

        // Create adapter with one session
        adapter = new StudentUpcomingSessionsAdapter(sessions, listener);
    }

    @Test
    public void testAdapterHoldsCorrectData() {
        // Verify adapter reports correct item count
        assertEquals(1, adapter.getItemCount());

        // Verify the session data is correct
        RegisteredSessions s = sessions.get(0);
        assertEquals("Math 101", s.getCourse());
        assertEquals("approved", s.getStatus());
    }

    @Test
    public void testCancelListenerTriggered() {
        // Simulate clicking cancel by directly invoking the listener
        RegisteredSessions s = sessions.get(0);
        adapter.getCancelClickListener().onCancelClick(s);

        // Verify listener was triggered
        assertTrue(cancelClicked);
    }
}
