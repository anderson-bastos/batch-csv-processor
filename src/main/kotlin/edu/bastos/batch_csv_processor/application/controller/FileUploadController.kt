package edu.bastos.batch_csv_processor.application.controller

import edu.bastos.batch_csv_processor.domain.entity.DocumentNumber
import org.springframework.batch.core.Job
import org.springframework.batch.core.JobParametersBuilder
import org.springframework.batch.core.launch.JobLauncher
import org.springframework.batch.item.file.FlatFileItemReader
import org.springframework.core.io.ByteArrayResource
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
class FileUploadController(
    private val jobLauncher: JobLauncher,
    private val job: Job,
    private val reader: FlatFileItemReader<DocumentNumber>
) {

    @PostMapping("/upload")
    fun handleFileUpload(@RequestParam("file") file: MultipartFile): ResponseEntity<String> {
        reader.setResource(ByteArrayResource(file.bytes))

        val jobParameters = JobParametersBuilder()
            .addLong("time", System.currentTimeMillis())
            .toJobParameters()

        jobLauncher.run(job, jobParameters)

        return ResponseEntity.ok("File uploaded and processed successfully")
    }
}