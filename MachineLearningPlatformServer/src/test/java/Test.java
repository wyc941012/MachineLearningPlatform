import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.ml.Estimator;
import org.apache.spark.ml.Model;
import org.apache.spark.ml.classification.LogisticRegression;
import org.apache.spark.ml.linalg.VectorUDT;
import org.apache.spark.ml.linalg.Vectors;
import org.apache.spark.ml.param.DoubleParam;
import org.apache.spark.ml.param.IntParam;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.Metadata;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;

public class Test {

	public static void main(String[] args) throws InstantiationException, IllegalAccessException, ClassNotFoundException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
		System.setProperty("hadoop.home.dir", "D:/Hadoop/");
		SparkSession sparkSession=SparkSession.builder().appName("sparktest").master("local").getOrCreate();
		List<Row> dataTraining = Arrays.asList(
			    RowFactory.create(1.0, Vectors.dense(0.0, 1.1, 0.1)),
			    RowFactory.create(0.0, Vectors.dense(2.0, 1.0, -1.0)),
			    RowFactory.create(0.0, Vectors.dense(2.0, 1.3, 1.0)),
			    RowFactory.create(1.0, Vectors.dense(0.0, 1.2, -0.5))
			);
		StructType schema = new StructType(new StructField[]{
		    new StructField("label", DataTypes.DoubleType, false, Metadata.empty()),
		    new StructField("features", new VectorUDT(), false, Metadata.empty())
		});
		Dataset<Row> training = sparkSession.createDataFrame(dataTraining, schema);
		int num=100;
		Class clazz=Class.forName("org.apache.spark.ml.classification.LogisticRegression");
		Method[] methods=clazz.getMethods();
		for(Method method : methods) {
			//System.out.println(method.getName());
		}
		Method method=clazz.getMethod("fit", Dataset.class);
		Method setMethod1=clazz.getMethod("setMaxIter", int.class);
		Method setMethod3=clazz.getMethod("setRegParam", double.class);
		Object suanfa= clazz.newInstance();
		setMethod1.invoke(suanfa, 100);
		setMethod3.invoke(suanfa, 0.01);
		Model model=(Model) method.invoke(suanfa, training);
		List<Row> dataTest = Arrays.asList(
			    RowFactory.create(1.0, Vectors.dense(-1.0, 1.5, 1.3)),
			    RowFactory.create(0.0, Vectors.dense(3.0, 2.0, -0.1)),
			    RowFactory.create(1.0, Vectors.dense(0.0, 2.2, -1.5))
			);
		Dataset<Row> test = sparkSession.createDataFrame(dataTest, schema);
		Dataset<Row> results = model.transform(test);
		for (Row r: results.collectAsList()) {
			  System.out.println("(" + r.get(0) + ", " + r.get(1) + ") -> prob=" + r.get(2)
			    + ", prediction=" + r.get(3));
		}
	}
}
