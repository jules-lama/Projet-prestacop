package com.prestacop.project.bean

class Configuration(var kafkaHost: String,
                    var kafkaPort: String,
                    var kafkaTopic: String,
                    var kafkaGroup: String,
                    var mongoUrl: String,
                    var mongoHBCol: String,
                    var mongoAlertCol: String,
                    var imgPath: String) {

}
