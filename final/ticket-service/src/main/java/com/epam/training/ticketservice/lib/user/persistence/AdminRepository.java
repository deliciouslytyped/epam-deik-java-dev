package com.epam.training.ticketservice.lib.user.persistence;

import com.epam.training.ticketservice.lib.user.persistence.base.UserBaseFragment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<ApplicationAdmin, Long>, UserBaseFragment<ApplicationAdmin> {
}