package com.prestacop.project.bean

class Configuration(var kafkaHost: String,
                    var kafkaPort: String,
                    var kafkaRecordTopic: String,
                    var kafkaAlertTopic: String,
                    var nbDrones: Int,
                    var alertFrequency: Int,
                    var sgPath: String,
                    var lePath: String,
                    var sfPath: String){


}
