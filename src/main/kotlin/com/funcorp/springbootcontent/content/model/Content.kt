package com.funcorp.springbootcontent.content.model

import java.sql.Timestamp
import java.time.Instant
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table


@Entity
@Table(name = "customer")
data class Content(
    @Id
    val id: String = "",

    @Column(name = "creation_time", nullable = false)
    val creationTime: Timestamp = Timestamp(0)
) {
    constructor(id: String, creationTime: String) : this(
        id,
        Timestamp.from(Instant.ofEpochSecond(creationTime.toLong()))
    )
}