package com.dashboard.app.repo;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dashboard.app.entity.Student;
@Repository
public interface StudentRepository extends JpaRepository<Student, Integer> {
 
}


