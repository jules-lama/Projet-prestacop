
# lancement alertManager
# à dockeriser

echo "Lancement ALERTMANAGER"
/opt/spark/spark-2.4.4-bin-hadoop2.7/bin/spark-submit --class com.prestacop.project.Main alertManager/target/scala-2.11/alertManager-assembly-0.1.jar alertManager/conf/configuration.json &


docker-compose up
