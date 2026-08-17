package com.microservices.firstjobapp.job;

import com.microservices.firstjobapp.company.Company;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/jobs")
public class JobController {

    private final JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @GetMapping
    public ResponseEntity<List<Job>> findAll(){
        return ResponseEntity.ok(jobService.findAll());
    }

    @PostMapping
    public ResponseEntity<String> addJob(@RequestBody Job job) {
        jobService.createJob(job);
        return new ResponseEntity<>("Job added successfully", HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Job> getJobById(@PathVariable Long id){
        Job job = jobService.getJobById(id);
        if(job != null){
            return new ResponseEntity<>(job, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteJobById(@PathVariable Long id){
       boolean result = jobService.deleteJobById(id);
       if(result){
           return new ResponseEntity<>("Job deleted successfully", HttpStatus.OK);
       }else{
           return new ResponseEntity<>("Job not found and unable to delete", HttpStatus.NOT_FOUND);
       }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateJob(@PathVariable Long id, @RequestBody Job updatedJob){
        boolean updated = jobService.updateJobById(id,updatedJob);
        if(updated) return  new ResponseEntity<>("Job updated successfully", HttpStatus.OK);
        else return new ResponseEntity<>("Job not found", HttpStatus.NOT_FOUND);
    }
}
