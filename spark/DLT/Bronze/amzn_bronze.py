from pyspark import pipelines as dp


@dp.table(
    name="jrvs_databricks.bronze.amzn_bronze",
    comment="Raw daily stock data for AMZN ingested from Alpha Vantage API"
)
@dp.expect("valid_date", "date IS NOT NULL")
@dp.expect("valid_close", "close IS NOT NULL")
def amzn_bronze():
    return (
        spark.readStream.format("cloudFiles")
        .option("cloudFiles.format", "json")
        .option("cloudFiles.inferColumnTypes", "true")
        .load("/Volumes/jrvs_databricks/bronze/stock_data_staging/amzn_daily/")
    )
