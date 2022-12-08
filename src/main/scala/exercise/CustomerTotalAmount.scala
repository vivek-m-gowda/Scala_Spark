package exercise

import org.apache.spark._
import org.apache.log4j._

object CustomerTotalAmount extends App{

  def extractCustomerPricePairs(line: String): (Int, Float) = {
    val fields = line.split(",")
    (fields(0).toInt, fields(2).toFloat)
  }

  // Set the log level to only print errors
  Logger.getLogger("org").setLevel(Level.ERROR)

  val sc = new SparkContext("local[*]", "Total Amount Spent by Customer")

  val lines = sc.textFile("data/customer-orders.csv")

  val mappedInput = lines.map(extractCustomerPricePairs)

  val totalByCustomer = mappedInput.reduceByKey((x, y) => x + y)

  val flipped = totalByCustomer.map(x => (x._2, x._1))

  val totalByCustomerSorted = flipped.sortByKey()

  val results = totalByCustomerSorted.collect()



  // Print the results.
  results.foreach(println)

}
