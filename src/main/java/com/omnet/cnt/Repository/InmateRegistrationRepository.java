package com.omnet.cnt.Repository;

import com.omnet.cnt.Model.InmateRegistration;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InmateRegistrationRepository extends JpaRepository<InmateRegistration, String> {
	
}
