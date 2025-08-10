package com.event.service;

import com.event.DTO.EventDTO;
import com.event.entity.Event;
import com.event.entity.Role;
import com.event.entity.User;
import com.event.exception.ResourceNotFoundException;
import com.event.repository.EventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EventServiceTest {

    @InjectMocks
    private EventService eventService;

    @Mock
    private EventRepository eventRepository;

    private User user;
    private Event event;

    @BeforeEach
    void setUp() {
        // Common setup for user and event objects to reduce code duplication
        user = new User(1L, "Test User", "password123", "Male", "123 Main St",
                "test.user@example.com", "1234567890", LocalDate.now(),
                LocalDate.now(), "Sports, Music", Role.USER);

        event = new Event(1L, "Sample Event", "Conference", "This is a sample event.",
                "123 Event St, Event City", LocalDate.now(), LocalDate.now(),
                LocalDateTime.now(), LocalDateTime.now(), user);
    }

    @Test
    public void createEvent_ShouldReturnSavedEvent() {
        // Arrange
        EventDTO eventDTO = EventDTO.mapToEventDTO(event);
        when(eventRepository.save(any(Event.class))).thenReturn(event);

        // Act
        Event createdEvent = eventService.createEvent(eventDTO, user);

        // Assert
        assertNotNull(createdEvent);
        assertEquals(event.getName(), createdEvent.getName());
        verify(eventRepository, times(1)).save(any(Event.class));
    }

    @Test
    public void getEventById_ShouldReturnEvent_WhenFound() {
        // Arrange
        given(eventRepository.findById(event.getId())).willReturn(Optional.of(event));

        // Act
        Optional<Event> foundEventOptional = eventService.getEventById(event.getId());

        // Assert
        assertTrue(foundEventOptional.isPresent());
        assertEquals(event.getId(), foundEventOptional.get().getId());
        verify(eventRepository).findById(event.getId());
    }

    @Test
    public void getEventById_ShouldThrowException_WhenNotFound() {
        // Arrange
        long nonExistentId = 99L;
        when(eventRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> eventService.getEventById(nonExistentId));
        verify(eventRepository).findById(nonExistentId);
    }

    @Test
    public void updateEvent_ShouldUpdateAndReturnEvent() {
        // Arrange
        EventDTO eventDTO = EventDTO.mapToEventDTO(event);
        eventDTO.setName("Updated Event Name");
        eventDTO.setDescription("This is an updated description.");

        when(eventRepository.findByIdAndUser(event.getId(), user)).thenReturn(Optional.of(event));
        when(eventRepository.save(any(Event.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Event updatedEvent = eventService.updateEvent(event.getId(), eventDTO, user);

        // Assert
        assertNotNull(updatedEvent);
        assertEquals("Updated Event Name", updatedEvent.getName());
        assertEquals("This is an updated description.", updatedEvent.getDescription());
        verify(eventRepository, times(1)).findByIdAndUser(event.getId(), user);
        verify(eventRepository, times(1)).save(any(Event.class));
    }

    @Test
    public void deleteEvent_ShouldCallRepositoryDelete() {
        // Arrange
        when(eventRepository.findByIdAndUser(event.getId(), user)).thenReturn(Optional.of(event));

        // Act
        eventService.deleteEvent(event.getId(), user);

        // Assert
        verify(eventRepository, times(1)).delete(event);
    }

    @Test
    public void deleteEvent_ShouldThrowException_WhenNotFound() {
        // Arrange
        long nonExistentId = 99L;
        when(eventRepository.findByIdAndUser(nonExistentId, user)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> eventService.deleteEvent(nonExistentId, user));

        // Verify that the delete method was never called
        verify(eventRepository, never()).delete(any(Event.class));
    }
}