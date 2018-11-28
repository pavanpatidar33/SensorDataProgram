package com.sampleProject.sampleProjectProgram

import org.json4s.Implicits
import java.io.File
import scala.io.Source

import org.json4s.Implicits
import java.io.File
import scala.io.Source
import org.apache.spark.sql.SQLContext
import org.apache.spark.{ SparkConf, SparkContext }
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.types.IntegerType
import org.apache.spark.sql.types.StructType
import org.apache.spark.sql.types.StructField
import org.apache.spark.sql.types.StringType
import org.apache.spark.sql.functions._
import org.apache.spark.sql.expressions._
object Sample {

    def main(args: Array[String]) {
  
    var conf = new SparkConf().setAppName("Read CSV File").setMaster("local[*]")
    val sc = new SparkContext(conf)
    val sqlContext = new SQLContext(sc)
    val spark = SparkSession.builder.config(conf).getOrCreate()
    import sqlContext.implicits._
    val d = new File("/home/xterra/2018102601200001")
    if (d.exists && d.isDirectory) {
      println("Num of processed files: = " + d.listFiles.filter(_.getName.endsWith(".csv")).length)
    }

    val dataframe1 = spark.read.format("CSV").option("header", "true").load("/home/xterra/2018102601200001")
    println("Num of failed measurements: = " + dataframe1.filter($"humidity" === "NaN").count())
    dataframe1.show()
println("Num of processed measurements: = " + dataframe1.count())
   
println("===========================================================================")
    val dataframe2 = dataframe1.withColumn("humidity", 'humidity.cast("Int"))

    val finalDf = dataframe2.groupBy("sensorid").agg(
      min($"humidity").as("min"),
      avg($"humidity").as("avg"),
      max($"humidity").as("max"))
     .withColumn("min", when($"min"isNull, "NaN").otherwise($"min"))
     .withColumn("avg", when($"avg"isNull, "NaN").otherwise($"avg"))
     .withColumn("max", when($"max"isNull, "NaN").otherwise($"max"))
      
      
    finalDf.show()
    
  
  }

}


  