package com.omnet.cnt.Repository;

import com.omnet.cnt.Model.InmateGrievance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OffenderGrievanceRepository extends JpaRepository<InmateGrievance, String> {

}