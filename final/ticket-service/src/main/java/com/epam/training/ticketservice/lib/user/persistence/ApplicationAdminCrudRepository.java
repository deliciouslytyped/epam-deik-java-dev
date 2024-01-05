package com.epam.training.ticketservice.lib.user.persistence;

import com.epam.training.ticketservice.lib.user.persistence.base.UserBaseFragment;
import com.epam.training.ticketservice.support.jparepo.UpdateByEntityFragment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicationAdminCrudRepository extends JpaRepository<ApplicationAdmin, Long>, UserBaseFragment<ApplicationAdmin>, UpdateByEntityFragment<ApplicationAdmin> {
}