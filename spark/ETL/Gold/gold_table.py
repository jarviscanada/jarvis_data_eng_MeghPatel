from pyspark import pipelines as dp
from pyspark.sql import functions as F
from pyspark.sql.window import Window


@dp.materialized_view(
    name="jrvs_databricks.gold.stock_trends",
    comment="Price and volume trend analysis for AAPL, GOOGL, MSFT, AMZN over 7, 30, and 90 day windows"
)
def stock_trends():
    # Batch read from all silver streaming tables
    aapl = spark.read.table("jrvs_databricks.silver.aapl_silver")
    googl = spark.read.table("jrvs_databricks.silver.googl_silver")
    msft = spark.read.table("jrvs_databricks.silver.msft_silver")
    amzn = spark.read.table("jrvs_databricks.silver.amzn_silver")

    combined = aapl.unionByName(googl).unionByName(msft).unionByName(amzn)

    w = Window.partitionBy("symbol").orderBy("trade_date")
    w7 = Window.partitionBy("symbol").orderBy("trade_date").rowsBetween(-6, 0)
    w30 = Window.partitionBy("symbol").orderBy("trade_date").rowsBetween(-29, 0)
    w90 = Window.partitionBy("symbol").orderBy("trade_date").rowsBetween(-89, 0)

    return (
        combined
        .withColumn("close_7d_ago", F.lag("close_price", 7).over(w))
        .withColumn("close_30d_ago", F.lag("close_price", 30).over(w))
        .withColumn("close_90d_ago", F.lag("close_price", 90).over(w))
        .withColumn("price_change_7d", F.round(F.col("close_price") - F.col("close_7d_ago"), 2))
        .withColumn("price_change_30d", F.round(F.col("close_price") - F.col("close_30d_ago"), 2))
        .withColumn("price_change_90d", F.round(F.col("close_price") - F.col("close_90d_ago"), 2))
        .withColumn(
            "price_pct_change_7d",
            F.round((F.col("close_price") - F.col("close_7d_ago")) / F.col("close_7d_ago") * 100, 2),
        )
        .withColumn(
            "price_pct_change_30d",
            F.round((F.col("close_price") - F.col("close_30d_ago")) / F.col("close_30d_ago") * 100, 2),
        )
        .withColumn(
            "price_pct_change_90d",
            F.round((F.col("close_price") - F.col("close_90d_ago")) / F.col("close_90d_ago") * 100, 2),
        )
        .withColumn("avg_volume_7d", F.round(F.avg("volume").over(w7)))
        .withColumn("avg_volume_30d", F.round(F.avg("volume").over(w30)))
        .withColumn("avg_volume_90d", F.round(F.avg("volume").over(w90)))
        .select(
            "symbol", "trade_date", "open_price", "high_price", "low_price",
            "close_price", "volume",
            "price_change_7d", "price_change_30d", "price_change_90d",
            "price_pct_change_7d", "price_pct_change_30d", "price_pct_change_90d",
            "avg_volume_7d", "avg_volume_30d", "avg_volume_90d",
        )
    )
