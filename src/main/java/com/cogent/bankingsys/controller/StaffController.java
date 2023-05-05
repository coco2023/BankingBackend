package com.cogent.bankingsys.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.cogent.bankingsys.errhandle.AlreadyExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import com.cogent.bankingsys.entity.Staff;
import com.cogent.bankingsys.repo.StaffRepository;
import com.cogent.bankingsys.errhandle.ResourceNotFoundException;

@RestController
@RequestMapping("/api/admin/staff")
public class StaffController {

//	HTTP Methods
//	   GET
//	   POST
//	   DELETE
//	   PUT


	@Autowired
	StaffRepository staffRepository;
	

	@PostMapping()          //So @Valid - need to Activate Validation
	public Staff addStaff(@Valid @RequestBody Staff newStaff) {
		System.out.println("Adding record to database");
		Optional<Staff> existingStaff = staffRepository.findBystaffUserName(newStaff.getStaffUserName());
		if (existingStaff.isPresent()) {
			throw new AlreadyExistException("Staff user name already exist.");
		}else{
			LocalDate currentDate = LocalDate.now();
			newStaff.setStuffAddedDate(currentDate);
			return staffRepository.save(newStaff);   //insert SQL - jdbc- MySQL
		}


	}
	




	@GetMapping()
	public List<StaffRepository.staffIdAndstaffUserNameAndStatusProjection> getStaff() {

		return staffRepository.findAllBy();
		
	}




	
	@PutMapping()
	  public ResponseEntity<Staff> updateStaff(@RequestBody Staff newStaffupdate) {
	    Long id = newStaffupdate.getStaffId();
		Optional<Staff> existingStaff = staffRepository.findById(id); //existind old data

		if (existingStaff.isPresent()) {
	    	
	      Staff Staff = existingStaff.get();
	      
	      Staff.setStatus(newStaffupdate.getStatus());
	      return new ResponseEntity<>(staffRepository.save(Staff), HttpStatus.OK);
	      
	    } else {                                //No data with id is Matched
	    	throw new ResourceNotFoundException("Staff status not changed");
	      //return new ResponseEntity<>( HttpStatus.NOT_FOUND);
	    }
	  }

}

