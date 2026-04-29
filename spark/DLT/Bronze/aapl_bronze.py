from pyspark import pipelines as dp


@dp.table(
    name="jrvs_databricks.bronze.aapl_bronze",
    comment="Raw daily stock data for AAPL ingested from Alpha Vantage API"
)
@dp.expect("valid_date", "date IS NOT NULL")
@dp.expect("valid_close", "close IS NOT NULL")
def aapl_bronze():
    return (
        spark.readStream.format("cloudFiles")
        .option("cloudFiles.format", "json")
        .option("cloudFiles.inferColumnTypes", "true")
        .load("/Volumes/jrvs_databricks/bronze/stock_data_staging/aapl_daily/")
    )
