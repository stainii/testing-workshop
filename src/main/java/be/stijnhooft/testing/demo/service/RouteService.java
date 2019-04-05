package be.stijnhooft.testing.demo.service;

import be.stijnhooft.testing.demo.model.Route;
import be.stijnhooft.testing.demo.repository.RouteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class RouteService {

    @Autowired
    private RouteRepository routeRepository;

    public List<Route> findAll() {
        return routeRepository.findAll();
    }

    public void create(Route idea) {
        routeRepository.save(idea);
    }

    public void delete(Long id) {
        routeRepository.deleteById(id);
    }

}
