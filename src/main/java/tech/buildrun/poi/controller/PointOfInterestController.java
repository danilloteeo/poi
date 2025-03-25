package tech.buildrun.poi.controller;

import org.springframework.web.bind.annotation.RestController;
import tech.buildrun.poi.controller.dto.CreatePointOfInterest;
import tech.buildrun.poi.entity.PointOfInterest;
import tech.buildrun.poi.repository.PointOfInterestRepository;
import org.springframework.data.domain.Page;  // Import correto para Page
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
public class PointOfInterestController {

    private final PointOfInterestRepository repository;

    public PointOfInterestController(PointOfInterestRepository repository) {
        this.repository = repository;
    }

    @PostMapping("/points-of-interests")
    public ResponseEntity<Void> createPoi(@RequestBody CreatePointOfInterest body) {
        repository.save(new PointOfInterest(body.name(), body.x(), body.y()));
        return ResponseEntity.ok().build();
    }

    @GetMapping("/points-of-interests")
    public ResponseEntity<Page<PointOfInterest>> listPoi(
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {

        var body = repository.findAll(PageRequest.of(page, pageSize));
        
        return ResponseEntity.ok(body);
    }

    @GetMapping("/near-me")
    public ResponseEntity<Page<PointOfInterest>> nearMe(
            @RequestParam("x") Long x,
            @RequestParam("y") Long y,
            @RequestParam("dmax") Long dmax) {

        var xMin = x - dmax;
        var xMax = x + dmax;
        var yMin = y - dmax;
        var yMax = y + dmax;
        

        var body = repository.findNearMe(xMin, xMax, yMin, yMax);
        
        return ResponseEntity.ok(body);
    }
}