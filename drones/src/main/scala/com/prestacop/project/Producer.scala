package com.prestacop.project

import java.util.Properties
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerRecord
import com.prestacop.project.bean.Configuration

// --- envoie les alertes et heartbeat dans une stream kafka --- //

object Producer {

  var conf: Configuration = _

  def produceHeartBeat(message: String): Unit = {

    val topic = conf.kafkaRecordTopic
    val props = new Properties()
    props.put("bootstrap.servers", String.format("%s:%s", conf.kafkaHost, conf.kafkaPort))
    props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")

    val producer = new KafkaProducer[String, String](props)
    val record = new ProducerRecord[String, String](topic, message.hashCode.toString, message)

    producer.send(record)
    producer.close()

  }

  def produceAlert(alert: String): Unit = {

    val topic = conf.kafkaAlertTopic
    val props = new Properties()
    props.put("bootstrap.servers", String.format("%s:%s", conf.kafkaHost, conf.kafkaPort))
    props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")

    val producer = new KafkaProducer[String, String](props)
    val record = new ProducerRecord[String, String](topic, alert.hashCode.toString, alert)

    producer.send(record)
    producer.close()

  }

}
