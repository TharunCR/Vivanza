package com.event.service;

import com.event.DTO.EventDTO;
import com.event.entity.Event;
import com.event.entity.User;
import com.event.exception.ResourceNotFoundException;
import com.event.repository.EventRepository;
import com.event.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@CacheConfig(cacheNames = "eventCache")
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private UserRepository userRepository;

    @Cacheable("events")
    public List<Event> getAllEvents() {
        doLongRunningTask();
        return eventRepository.findAll();
    }

    /* For pagination
    @Cacheable("events")
    public Page<Event> getAllEvents(Pageable pageable) {
        return eventRepository.findAll(pageable);
    }*/

    @Caching(evict = {
            @CacheEvict(cacheNames = "events", allEntries = true),
            @CacheEvict(cacheNames = "eventCache", key = "'eventsByUser:' + #user.id")
    })
    public Event createEvent(EventDTO eventDTO, User user) {
        Event event = EventDTO.mapToEvent(eventDTO);
        event.setUser(user);
        doLongRunningTask();
        return eventRepository.save(event);
    }

    @Cacheable(cacheNames = "event", key = "#id", unless = "#result == null")
    public Optional<Event> getEventById(Long id) {
        doLongRunningTask();
        return Optional.ofNullable(eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found with ID: " + id)));
    }

    @Cacheable(key = "'eventsByUser:' + #userId")
    public List<Event> getEventsByUser(Long userId) {
        doLongRunningTask();
        // Fetch the user from the repository to ensure the ID is valid
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));
        return eventRepository.findByUser(user);
    }

    @Caching(evict = {
            @CacheEvict(cacheNames = "events", allEntries = true),
            @CacheEvict(cacheNames = "eventCache", key = "'eventsByUser:' + #user.id")
    })
    @CachePut(value = "event", key = "#eventId")
    public Event updateEvent(Long eventId, EventDTO eventDetails, User user) {
        doLongRunningTask();
        Event event = eventRepository.findByIdAndUser(eventId, user)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found with ID: " + eventId));

        event.setName(eventDetails.getName());
        event.setCategory(eventDetails.getCategory());
        event.setDescription(eventDetails.getDescription());
        event.setLocation(eventDetails.getLocation());
        event.setEventEndDateAndTime(eventDetails.getEventEndDateAndTime());
        event.setEventStartDateAndTime(eventDetails.getEventStartDateAndTime());
        event.setUser(user);
        return eventRepository.save(event);
    }

    @Caching(evict = {
            @CacheEvict(cacheNames = "event", key = "#eventId"),
            @CacheEvict(cacheNames = "eventCache", key = "'eventsByUser:' + #user.id")
    })
    @CacheEvict(cacheNames = "events", allEntries = true)
    public void deleteEvent(Long eventId, User user) {
        Event event = eventRepository.findByIdAndUser(eventId, user)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found or you do not have permission to delete it. ID: " + eventId));

        eventRepository.delete(event);
        log.info("Deleted the event with id {}", eventId);
    }

    private void doLongRunningTask() {
        try {
            Thread.sleep(3000); // Simulating a slow database call
        } catch (InterruptedException e) {
            log.error("Long running task was interrupted", e);
            Thread.currentThread().interrupt();
        }
    }
}