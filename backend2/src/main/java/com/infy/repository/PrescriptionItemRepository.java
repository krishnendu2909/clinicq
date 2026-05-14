package com.infy.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.infy.entity.PrescriptionItem;

public interface PrescriptionItemRepository extends JpaRepository<PrescriptionItem,Long>{

    List<PrescriptionItem> findByPrescriptionId(Long id);

}
 