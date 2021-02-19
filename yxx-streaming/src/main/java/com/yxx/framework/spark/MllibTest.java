package com.yxx.framework.spark;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaDoubleRDD;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.mllib.linalg.Matrices;
import org.apache.spark.mllib.linalg.Matrix;
import org.apache.spark.mllib.linalg.Vector;
import org.apache.spark.mllib.linalg.Vectors;
import org.apache.spark.mllib.regression.LabeledPoint;
import org.apache.spark.mllib.stat.MultivariateStatisticalSummary;
import org.apache.spark.mllib.stat.Statistics;
import org.apache.spark.mllib.stat.test.*;
import org.apache.spark.mllib.util.MLUtils;
import org.apache.spark.rdd.RDD;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.streaming.Duration;
import org.apache.spark.streaming.StreamingContext;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spark_project.guava.collect.ImmutableMap;
import org.springframework.util.StringUtils;
import scala.Tuple2;

import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * desc
 * </p>
 *
 * @author wangpan
 * @date 2021/1/14
 */
public class MllibTest {

    private static final Logger logger = LoggerFactory.getLogger(MllibTest.class);

    private SparkSession sparkSession;

    MllibTest () {
        this.sparkSession = SparkSession.builder()
                .config(new SparkConf().setMaster("local[1]").setAppName(MllibTest.class.getName())).getOrCreate();
    }

    private void mlStatistic() {
        JavaSparkContext jsc = JavaSparkContext.fromSparkContext(sparkSession.sparkContext());
        JavaRDD<Vector> mat = jsc.parallelize(Arrays.asList(
                Vectors.dense(1.0, 10.0, 100.0),
                Vectors.dense(2.0, 20.0, 200.0),
                Vectors.dense(3.0, 30.0, 300.0)
        ));
        MultivariateStatisticalSummary summary = Statistics.colStats(mat.rdd());
        // 均差
        logger.warn("均差:{}", summary.mean());
        // 方差
        logger.warn("方差:{}", summary.variance());
        // 每列中的非零数量
        logger.warn("每列中的非零数量:{}", summary.numNonzeros());
        // 相关性计算
        Matrix corr = Statistics.corr(mat.rdd(), "pearson");
        logger.warn("相关性计算:{}", corr.toString());
        // 采样分析
        List<Tuple2<Integer, Character>> list = Arrays.asList(
                new Tuple2<>(1, 'a'),
                new Tuple2<>(1, 'b'),
                new Tuple2<>(2, 'c'),
                new Tuple2<>(2, 'd'),
                new Tuple2<>(2, 'e'),
                new Tuple2<>(3, 'f')
        );
        JavaPairRDD<Integer, Character> pairData = jsc.parallelizePairs(list);
        // 每个样品所需的确切分数
        ImmutableMap<Integer, Double> fractions = ImmutableMap.of(1, 0.1, 2, 0.6, 3, 0.3);
        JavaPairRDD<Integer, Character> approxSample = pairData.sampleByKey(false, fractions);
        JavaPairRDD<Integer, Character> exactSample = pairData.sampleByKeyExact(false, fractions);

        // 假设检验
        /**
         *  method: pearson 使用方法
         *  degrees of freedom = 33 检验统计量（决定是否可以拒绝原假设的证据，检验统计量的绝对值越大，拒绝原假设的理由越充分）
         *  statistic = 36.0 自由度。表示可自由变动的样本观测值的数目
         *  pValue = 0.32988920994279636 统计学根据显著性检验方法所得到的P 值。一般以P < 0.05 为显著， P<0.01 为非常显著
         *  No presumption against null hypothesis: the occurrence of the outcomes is statistically independent..
         */

        Vector dense = Vectors.dense(0.1, 0.15, 0.2, 0.3, 0.25);
        ChiSqTestResult chiSqTestResult = Statistics.chiSqTest(dense);
        logger.warn("拟合度检验Vector--------{}", chiSqTestResult.toString());

        Matrix densem = Matrices.dense(3, 2, new double[]{1.0, 3.0, 5.0, 2.0, 4.0, 6.0});
        ChiSqTestResult chiSqTestResultm = Statistics.chiSqTest(densem);
        logger.warn("独立性检验Matrix--------{}", chiSqTestResultm.toString());

        // 概率分布
        JavaDoubleRDD javaDoubleRDD = jsc.parallelizeDoubles(Arrays.asList(0.1, 0.15, 0.2, 0.3, 0.25));
        /**
         * norm 分布类型：正态分布 不填默认标准的正态分布
         */
        KolmogorovSmirnovTestResult norm = Statistics.kolmogorovSmirnovTest(javaDoubleRDD, "norm", 0.0, 1.0);
        logger.warn("正态分布---{}", norm);

    }

    public void svmFileProcess(String path) {
        JavaRDD<LabeledPoint> labeledPointRDD = MLUtils.loadLibSVMFile(sparkSession.sparkContext(), path).toJavaRDD();
        ChiSqTestResult[] chiSqTestResults = Statistics.chiSqTest(labeledPointRDD);
        Arrays.stream(chiSqTestResults).forEach(item -> logger.warn("Column-----------{}", item));

        // 分布测试
        StreamingContext sc = new StreamingContext(sparkSession.sparkContext(), new Duration(5000));
        JavaStreamingContext jsc = new JavaStreamingContext(sc);
        JavaDStream<BinarySample> data = jsc.textFileStream(path).map(item -> {
            String[] ts = item.split(",");
            boolean label = Boolean.parseBoolean(ts[0]);
            double value = Double.parseDouble(ts[1]);
            return new BinarySample(label, value);
        });
        StreamingTest streamingTest = new StreamingTest()
                .setPeacePeriod(0)
                .setWindowSize(0)
                .setTestMethod("welch");
        logger.warn("streamingTest-----{}", streamingTest);

        JavaDStream<StreamingTestResult> testResult = streamingTest.registerStream(data);

        testResult.print();
        sc.start();
        sc.awaitTermination();
    }

    public static void main(String[] args) {
//        new MllibTest().mlStatistic();
        new MllibTest().svmFileProcess("F:/softPackage/spark-2.4.4-bin-hadoop2.6/data/mllib/sample_lda_libsvm_data.txt");
    }

}
