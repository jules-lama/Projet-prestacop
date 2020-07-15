FROM bde2020/spark-submit:2.4.5-hadoop2.7

ADD conf /conf
ADD target/scala-2.11/recordManager-assembly-0.1.jar /
ADD submit.sh /

ENV SPARK_MASTER_NAME debian
ENV SPARK_MASTER_PORT 7077
ENV SPARK_APPLICATION_JAR_LOCATION recordManager-assembly-0.1.jar
ENV SPARK_APPLICATION_MAIN_CLASS com.prestacop.project.Main
ENV SPARK_APPLICATION_ARGS "conf/configuration.json"