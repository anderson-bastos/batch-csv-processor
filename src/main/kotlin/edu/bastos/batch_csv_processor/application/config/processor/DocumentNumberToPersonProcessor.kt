package edu.bastos.batch_csv_processor.application.config.processor

import edu.bastos.batch_csv_processor.domain.entity.DocumentNumber
import edu.bastos.batch_csv_processor.infrastructure.persistence.Person
import org.springframework.batch.item.ItemProcessor
import org.springframework.stereotype.Component

@Component
class DocumentNumberToPersonProcessor : ItemProcessor<DocumentNumber, Person> {
    override fun process(documentNumber: DocumentNumber) =
        Person(
            documentNumber = documentNumber.value,
            status = "ACTIVE"
        )
}