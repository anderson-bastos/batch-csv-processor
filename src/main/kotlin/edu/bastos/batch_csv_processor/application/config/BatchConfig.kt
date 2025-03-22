package edu.bastos.batch_csv_processor.application.config

import edu.bastos.batch_csv_processor.domain.entity.DocumentNumber
import edu.bastos.batch_csv_processor.infrastructure.persistence.Person
import edu.bastos.batch_csv_processor.infrastructure.persistence.repository.PersonRepository
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
import org.springframework.batch.core.job.builder.JobBuilder
import org.springframework.batch.core.partition.support.Partitioner
import org.springframework.batch.core.partition.support.SimplePartitioner
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.step.builder.StepBuilder
import org.springframework.batch.item.ItemProcessor
import org.springframework.batch.item.ItemWriter
import org.springframework.batch.item.data.RepositoryItemWriter
import org.springframework.batch.item.database.JdbcBatchItemWriter
import org.springframework.batch.item.file.FlatFileItemReader
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ByteArrayResource
import org.springframework.data.repository.CrudRepository
import org.springframework.retry.backoff.ExponentialBackOffPolicy
import org.springframework.retry.policy.SimpleRetryPolicy
import org.springframework.retry.support.RetryTemplate
import org.springframework.transaction.PlatformTransactionManager

@Configuration
@EnableBatchProcessing
class BatchConfig {

    @Bean
    fun job(jobRepository: JobRepository, step: Step): Job {
        return JobBuilder("personJob", jobRepository)
            .start(step)
            .build()
    }

    @Bean
    fun step(
        jobRepository: JobRepository,
        transactionManager: PlatformTransactionManager,
        reader: FlatFileItemReader<DocumentNumber>,
        processor: ItemProcessor<DocumentNumber, Person>,
        writer: ItemWriter<Person>
    ): Step {
        return StepBuilder("personStep", jobRepository)
            .chunk<DocumentNumber, Person>(5000, transactionManager)
            .reader(reader)
            .processor(processor)
            .writer(writer)
            .build()
    }

    @Bean
    fun reader(): FlatFileItemReader<DocumentNumber> {
        return FlatFileItemReaderBuilder<DocumentNumber>()
            .name("csvReader")
            .resource(ByteArrayResource(byteArrayOf())) // Recurso vazio inicialmente
            .linesToSkip(1)                             // Pula a primeira linha (cabeçalho)
            .delimited()                                // Define que o CSV é delimitado (por vírgula, por exemplo)
            .names("pep_document_number")               // Nome da coluna no CSV
            .fieldSetMapper { fieldSet ->
                DocumentNumber(fieldSet.readString("pep_document_number"))
            }
            .build()
    }

    @Bean
    fun writer(repository: PersonRepository): ItemWriter<Person> {
        return ItemWriter { items ->
            repository.saveAll(items)
        }
    }

    @Bean
    fun retryTemplate(): RetryTemplate =
        RetryTemplate().apply {
            setRetryPolicy(SimpleRetryPolicy(3))
            setBackOffPolicy(
                ExponentialBackOffPolicy().apply {
                    initialInterval = 1000L // Intervalo inicial de 1 segundo
                    multiplier = 2.0 // Multiplicador para o intervalo (dobrando a cada falha)
                }
            )
        }
}