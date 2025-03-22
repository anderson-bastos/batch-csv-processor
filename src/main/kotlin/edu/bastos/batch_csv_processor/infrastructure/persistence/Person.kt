package edu.bastos.batch_csv_processor.infrastructure.persistence

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "person")
data class Person(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(name = "document_number", nullable = false)
    val documentNumber: String,

    @Column(name = "status", nullable = false)
    val status: String = "ACTIVE", // Valor padrão para o status

    @Column(name = "created_at", nullable = false)
    val createdAt: LocalDateTime = LocalDateTime.now() // Valor padrão para a data de criação
)