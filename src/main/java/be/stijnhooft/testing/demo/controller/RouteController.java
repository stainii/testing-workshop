package be.stijnhooft.testing.demo.controller;

import be.stijnhooft.testing.demo.dto.RouteStatus;
import be.stijnhooft.testing.demo.facade.RouteFacade;
import be.stijnhooft.testing.demo.model.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/route/")
public class RouteController {

    @Autowired
    private RouteFacade statusFacade;

    @RequestMapping("/")
    public List<RouteStatus> getAllRoutesAndTheirDelayStatus() {
        return statusFacade.getCurrentStatusForAllRoutes();
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public void createRoute(@RequestBody Route route) {
        this.statusFacade.createRoute(route);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void deleteRoute(@PathVariable("id") long id) {
        this.statusFacade.deleteRoute(id);
    }
}
