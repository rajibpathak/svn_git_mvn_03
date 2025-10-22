package com.omnet.cnt.Repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.omnet.cnt.Model.InmateAddress;

@Repository
public interface InmateAddressRepository extends JpaRepository<InmateAddress, String> {
	
}
