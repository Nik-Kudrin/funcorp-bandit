package com.funcorp.springbootcontent.content

import java.sql.Timestamp
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id


@Entity
//@Table(name = "customer")
class Content {
    @Id
    @GeneratedValue
    private val id: String = ""

    @Column(name = "creation_time", nullable = false)
    private val creationTime: Timestamp = Timestamp(0)
}