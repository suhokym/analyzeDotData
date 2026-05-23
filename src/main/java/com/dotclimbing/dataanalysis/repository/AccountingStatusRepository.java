package com.dotclimbing.dataanalysis.repository;

import com.dotclimbing.dataanalysis.entity.AccountingStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountingStatusRepository extends JpaRepository<AccountingStatus, Long> {
}
