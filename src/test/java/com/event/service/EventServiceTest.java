package com.event.service;

import com.event.DTO.EventDTO;
import com.event.entity.Event;
import com.event.entity.Role;
import com.event.entity.User;
import com.event.exception.ResourceNotFoundException;
import com.event.repository.EventRepository;
import com.event.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EventServiceTest {

    @InjectMocks
    private EventService eventService;

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private EventRepository eventRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private MessageSource messageSource;

    @Test
    public void createEvent() {

        User user = new User();
        user.setId(1L);
        user.setPassword("password123");
        user.setUpdatedDate(LocalDate.now());
        user.setCreationDate(LocalDate.now());
        user.setEventsOfInterest("Sports, Music, Technology");
        user.setRole(Role.USER);
        user.setMobile("1234567890");
        user.setGender("Male");
        user.setEmail("crtharun1@gmail.com");
        user.setName("Test event");
        user.setAddress("Coimbatore");

        Event event = new Event();
        event.setId(1L);
        event.setName("News Events");
        event.setCategory("News, advertisement");
        event.setDescription("This is new event.");
        event.setLocation("RaceCourse, Coimbatore");
        event.setCreationDate(LocalDate.now());
        event.setUpdatedDate(LocalDate.now());
        event.setEventStartDateAndTime(LocalDateTime.now());
        event.setEventEndDateAndTime(LocalDateTime.now());
        event.setUser(user);

        EventDTO eventDTO = EventDTO.mapToEventDTO(event);
        when(eventRepository.save(any(Event.class))).thenReturn(event);
        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));

        // Act
        Event createdEvent = eventService.createEvent(eventDTO, user, user.getId());
        Optional<Event> optionalEvent = eventService.getEventById(createdEvent.getId());
        Event savedEvent = optionalEvent.orElse(new Event());

        // Assert
        assertEquals(createdEvent.getId(), savedEvent.getId());
    }

    @Test
    public void invalidUser() {
        EventDTO eventDTO = new EventDTO();
        eventDTO.setId(1L);
        eventDTO.setName("Event- s");
        eventDTO.setCategory("News, advertisement");
        eventDTO.setDescription("This is new event.");
        eventDTO.setLocation("RaceCourse, Coimbatore");
        eventDTO.setCreationDate(LocalDate.now());
        eventDTO.setUpdatedDate(LocalDate.now());
        eventDTO.setEventStartDateAndTime(LocalDateTime.now());
        eventDTO.setEventEndDateAndTime(LocalDateTime.now());

        assertThrows(UsernameNotFoundException.class, () -> userService.findById(132L));
    }

    @Test
    public void getEventById() {
        User user = new User(1L, "Ramesh C S", "password123", "Male", "20 Gotham city",
                "csramesh@gmail.com", "1234567890", LocalDate.now(),
                LocalDate.now(), "Sports, Music", Role.USER);

        Event event = new Event(63L, "Sample Event1", "Conference", "This is a sample event.", "123 Event St, Event City",
                LocalDate.now(), LocalDate.now(), LocalDateTime.now(), LocalDateTime.now(), user);

        Optional<Event> optionalEvent = Optional.of(event);
        given(eventRepository.findById(event.getId())).willReturn(optionalEvent);
        Optional<Event> expectedEventOptional = eventService.getEventById(event.getId());
        Event expectedEvent = expectedEventOptional.orElse(new Event());

        assertEquals(expectedEvent.getId(), event.getId());
        verify(eventRepository).findById(expectedEvent.getId());
    }

    @Test
    public void eventNotFound() {
        assertThrows(ResourceNotFoundException.class, () -> eventService.getEventById(1565L));
    }

    @Test
    public void updateEvent() {
        User user = new User(1L, "John Doe", "password123", "Male", "123 Main St",
                "john.doe@example.com", "1234567890", LocalDate.now(),
                LocalDate.now(), "Sports, Music", Role.USER);

        Event existingEvent = new Event(1L, "News Events", "News, advertisement", "This is new event.", "123 Event St, Event City",
                LocalDate.now(), LocalDate.now(), LocalDateTime.now(), LocalDateTime.now(), user);

        EventDTO eventDTO = EventDTO.mapToEventDTO(existingEvent);
        eventDTO.setName("Updated- Event");
        eventDTO.setDescription("This is description for testing event");

        // Mocking the repository and message source
        when(eventRepository.findByIdAndUser(existingEvent.getId(), user)).thenReturn(Optional.of(existingEvent));
        when(eventRepository.save(any(Event.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Call the method under test
        Event updatedEvent = eventService.updateEvent(existingEvent.getId(), eventDTO, user, user.getId());

        // Verify interactions
        verify(eventRepository, times(1)).findByIdAndUser(eventDTO.getId(), user);
        verify(eventRepository, times(1)).save(any(Event.class));
        verify(messageSource, times(0)).getMessage(eq("event.not.found.msg"), any(), isNull());

        assertEquals(updatedEvent.getId(), existingEvent.getId());
    }

    @Test
    public void deleteEvent() {
        User user = new User(1L, "Tharun C R", "password123", "Male", "20 - Racecourse",
                "crtharun1@gmail.com", "1234567890", LocalDate.of(2024, 5, 3),
                LocalDate.of(2024, 5, 3), "Music, Sports", Role.USER);

        Event event = new Event(14343L, "Sample Event1", "Conference", "This is a sample event.", "123 Event St, Event City",
                LocalDate.now(), LocalDate.now(), LocalDateTime.now(), LocalDateTime.now(), user);

        // Mock the behavior of eventRepository.findByIdAndUser
        when(eventRepository.findByIdAndUser(event.getId(), user)).thenReturn(Optional.of(event));

        // Call the service method
        eventService.deleteEvent(event.getId(), user, user.getId());

        // Verify that the delete method was called on the repository
        verify(eventRepository).delete(event);
    }

    @Test
    public void deleteEvent_EventNotFound() {
        User user = new User(1L, "Tharun C R", "password123", "Male", "20 - Racecourse ",
                "crtharun1@gmail.com", "1234567890", LocalDate.of(2024, 5, 3),
                LocalDate.of(2024, 5, 3), "Music, Sports", Role.USER);

        // Mock the behavior of eventRepository.findByIdAndUser to return empty Optional
        when(eventRepository.findByIdAndUser(anyLong(), any(User.class))).thenReturn(Optional.empty());

        // Mock the behavior of messageSource
        when(messageSource.getMessage(eq("event.not.found.msg"), any(), isNull()))
                .thenReturn("Event not found with ID ");

        // Call the service method and assert that a ResourceNotFoundException is thrown
        assertThrows(ResourceNotFoundException.class, () ->
                eventService.deleteEvent(1L, user, user.getId()));

        // Verify that the delete method was not called on the repository
        verify(eventRepository, never()).delete(any(Event.class));
    }
}
