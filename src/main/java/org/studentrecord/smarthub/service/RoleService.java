package org.studentrecord.smarthub.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.studentrecord.smarthub.model.Role;
import org.studentrecord.smarthub.repository.RoleRepository;


@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

}
