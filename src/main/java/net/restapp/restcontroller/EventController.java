package net.restapp.restcontroller;

import io.swagger.annotations.*;
import net.restapp.exception.EntityNullException;
import net.restapp.exception.PathVariableNullException;
import net.restapp.model.ArchiveSalary;
import net.restapp.model.Event;
import net.restapp.servise.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/event")
@Secured({"ROLE_ADMIN", "ROLE_MODERATOR"})
@Api(value="event", description="Operations pertaining to event in HRManagement")
public class EventController {

    @Autowired
    EventService eventService;

    @ApiOperation(value = "View event by id", response = ArchiveSalary.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Event successfully retrieved"),
            @ApiResponse(code = 401, message = "You are not authorized to view event"),
            @ApiResponse(code = 403, message = "Accessing retrieving the event you were trying to reach is forbidden"),
            @ApiResponse(code = 400, message = "request is not correct")
    })
    @RequestMapping(value = "/{eventId}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Object> getDepartment(@ApiParam(value = "id of the Event", required = true) @PathVariable("eventId") Long eventId,
                                                HttpServletRequest request) {
        if (eventId == null) {
            String msg = "PathVariable can't be null ";
            throw new PathVariableNullException(msg);
        }
        Event event = eventService.getById(eventId);

        if (event == null) {
            String msg = String.format("There is no event with id: %d", eventId);
            throw new EntityNotFoundException(msg);
        }
        return new ResponseEntity<>(event, HttpStatus.OK);
    }

    @ApiOperation(value = "Delete event by id", response = ArchiveSalary.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Event successfully deleted"),
            @ApiResponse(code = 401, message = "You are not authorized to delete event"),
            @ApiResponse(code = 403, message = "Accessing deleting the employee you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The event you were trying to reach is not found"),
            @ApiResponse(code = 400, message = "request is not correct")
    })
    @RequestMapping(value = "/{eventId}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Object> deleteDepartment(@ApiParam(value = "id of the Event", required = true) @PathVariable("eventId") Long eventId,
                                                   HttpServletRequest request) {

        if (eventId == null) {
            String msg = "PathVariable can't be null ";
            throw new PathVariableNullException(msg);
        }
        Event event = eventService.getById(eventId);

        if (event == null) {
            String msg = String.format("There is no departments with id: %d", eventId);
            throw new EntityNotFoundException(msg);
        }
        eventService.delete(eventId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @ApiOperation(value = "Update event by id", response = ArchiveSalary.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Event successfully updated"),
            @ApiResponse(code = 401, message = "You are not authorized to update event"),
            @ApiResponse(code = 403, message = "Accessing updating the employee you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The event you were trying to reach is not found"),
            @ApiResponse(code = 400, message = "request is not correct")
    })
    @RequestMapping(value = "/{eventId}",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Object> editDepartment(@ApiParam(value = "id of the Event", required = true)  @PathVariable("eventId") Long eventId,
                                                 @ApiParam(value = "json body of the Event", required = true) @RequestBody @Valid Event event,
                                                 HttpServletRequest request) {

        if (eventId == null) {
            String msg = "PathVariable can't be null ";
            throw new PathVariableNullException(msg);
        }
        Event event1 = eventService.getById(eventId);

        if (event == null) {
            String msg = String.format("There is no departments with id: %d", eventId);
            throw new EntityNotFoundException(msg);
        }
        event.setId(eventId);
        eventService.save(event);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @ApiOperation(value = "Save event to database", response = ArchiveSalary.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Event successfully created"),
            @ApiResponse(code = 401, message = "You are not authorized to create event"),
            @ApiResponse(code = 403, message = "Accessing creating the employee you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The event you were trying to reach is not found"),
            @ApiResponse(code = 400, message = "request is not correct")
    })
    @RequestMapping(value = "/add",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Object> saveDepartment(@ApiParam(value = "json body of the Event", required = true) @RequestBody @Valid Event event,
                                                 UriComponentsBuilder builder,
                                                 HttpServletRequest request) {
        HttpHeaders httpHeaders = new HttpHeaders();

        if (event == null) {
            throw new EntityNullException("event can't be null");
        }
        eventService.save(event);

        httpHeaders.setLocation(builder.path("/event/getAll").buildAndExpand().toUri());
        return new ResponseEntity<>(event, httpHeaders, HttpStatus.CREATED);
    }
    @ApiOperation(value = "Retrieve all events", response = ArchiveSalary.class, responseContainer="List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Events successfully retrieved"),
            @ApiResponse(code = 401, message = "You are not authorized to retrieve events"),
            @ApiResponse(code = 403, message = "Accessing retrieving events you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The is no events to retrieve")
    })
    @RequestMapping(value = "/getAll",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Object> getAllDepartment(HttpServletRequest request) {
        List<Event> listEvent = eventService.getAll();
        if (listEvent.isEmpty()) {
            String msg = "There is no events ";
            throw new EntityNotFoundException(msg);
        }
        return new ResponseEntity<>(listEvent, HttpStatus.OK);
    }


}
