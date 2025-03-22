package edu.bastos.batch_csv_processor.infrastructure.persistence.repository

import edu.bastos.batch_csv_processor.infrastructure.persistence.Person
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PersonRepository : JpaRepository<Person, Long>