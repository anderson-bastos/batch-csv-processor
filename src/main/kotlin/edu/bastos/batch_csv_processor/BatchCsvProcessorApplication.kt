package edu.bastos.batch_csv_processor

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class BatchCsvProcessorApplication

fun main(args: Array<String>) {
	runApplication<BatchCsvProcessorApplication>(*args)
}
