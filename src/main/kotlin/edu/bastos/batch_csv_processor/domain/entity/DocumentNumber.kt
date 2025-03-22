package edu.bastos.batch_csv_processor.domain.entity

import com.fasterxml.jackson.annotation.JsonProperty

data class DocumentNumber(val value: String) {
    constructor() : this("")
}