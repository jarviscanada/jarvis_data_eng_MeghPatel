from pyspark import pipelines as dp
from pyspark.sql import functions as F


@dp.table(
    name="jrvs_databricks.silver.aapl_silver",
    comment="Cleaned and formatted daily stock data for AAPL"
)
@dp.expect_or_drop("valid_close_price", "close_price > 0")
@dp.expect_or_drop("valid_volume", "volume > 0")
def aapl_silver():
    return (
        spark.readStream.table("jrvs_databricks.bronze.aapl_bronze")
        .select(
            F.col("symbol"),
            F.to_date(F.col("date")).alias("trade_date"),
            F.col("open").cast("double").alias("open_price"),
            F.col("high").cast("double").alias("high_price"),
            F.col("low").cast("double").alias("low_price"),
            F.col("close").cast("double").alias("close_price"),
            F.col("volume").cast("long").alias("volume"),
        )
    )
