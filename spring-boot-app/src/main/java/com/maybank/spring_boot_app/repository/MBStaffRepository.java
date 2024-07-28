package com.maybank.spring_boot_app.repository;

import com.maybank.spring_boot_app.entity.MaybankStaff;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MBStaffRepository extends JpaRepository<MaybankStaff, Long> {
}