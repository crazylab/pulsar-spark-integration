package com.pulsarspark

import org.apache.spark.sql.{DataFrame, SparkSession}

import scala.util.{Failure, Success, Try}

object PulsarSparkApp {
  private val checkpointPath = "./pulsar-spark-integration/checkpointsB"

  val STARTING_OFFSETS_OPTION_KEY = "startingoffsets"

  def main(args: Array[String]): Unit = {
    println("Starting Job..")

    val spark = SparkSession.builder()
      .appName("Pulsar Spark")
      .master("local[*]")
      .getOrCreate()
    println(Console.GREEN, Thread.activeCount())
    //      .withWatermark("__eventTime", "3 minutes")

    val inputDF: DataFrame = spark
      .readStream
      .format("pulsar")
      .option("service.url", "pulsar://localhost:6650")
      .option("admin.url", "http://localhost:8080")
      .option(STARTING_OFFSETS_OPTION_KEY, "earliest")
      //      .option("authpluginclassname", "org.apache.pulsar.broker.authentication.AuthenticationProviderToken")
      //      .option("authparams", "")
      .option("topic", "topicA")
      .load()
    val output = inputDF
      .writeStream
      .format("pulsar")
      //      .foreachBatch((data, batchID) => println(s"The current batch ID: $batchID"))
      .option("service.url", "pulsar://localhost:6650")
      .option("admin.url", "http://localhost:8080")
      .option("topic", "topicB")
      .option("checkpointLocation", checkpointPath).start()


    //    sys.addShutdownHook {
    //      output.stop()
    //    }

    Try(output.awaitTermination()) match {
      case Success(v) => println("Successful Termination!")
      case Failure(exception: InterruptedException) => println("Application have been interrupted!")
      case _ => println("Streaming query failed!")
    }

    println("This is the last line of code")
  }
}
