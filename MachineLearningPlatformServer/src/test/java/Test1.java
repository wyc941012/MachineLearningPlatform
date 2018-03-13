import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.parquet.column.ValuesType;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.ml.Model;
import org.apache.spark.ml.linalg.VectorUDT;
import org.apache.spark.ml.linalg.Vectors;
import org.apache.spark.rdd.RDD;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.Metadata;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.hhu.machinelearningplatformserver.algorithm.MLAlgorithmDesc;
import com.hhu.machinelearningplatformserver.algorithm.ParameterDesc;
import com.hhu.machinelearningplatformserver.algorithm.ParameterValueType;
import com.hhu.machinelearningplatformserver.data.DataFile;
import com.hhu.machinelearningplatformserver.data.FieldInfo;
import com.hhu.machinelearningplatformserver.data.SparkDataFileConverter;
import com.hhu.machinelearningplatformserver.exception.CantConverException;

public class Test1 {

	public static void main(String[] args) throws JsonParseException, JsonMappingException, IOException, ClassNotFoundException, NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, CantConverException {
		BufferedReader bufferedReader=new BufferedReader(new FileReader("src/test/resources/test.json"));
		String json="";
		String str;
		while((str=bufferedReader.readLine())!=null) {
			json+=str;
		}
		ObjectMapper objectMapper=new ObjectMapper();
		MLAlgorithmDesc algorithmDesc=objectMapper.readValue(json, MLAlgorithmDesc.class);
		System.setProperty("hadoop.home.dir", "D:/Hadoop/");
		SparkSession sparkSession=SparkSession.builder().appName("sparktest").master("local").getOrCreate();
		/*List<Row> dataTraining = Arrays.asList(
			    RowFactory.create(1.0, Vectors.dense(0.0, 1.1, 0.1)),
			    RowFactory.create(0.0, Vectors.dense(2.0, 1.0, -1.0)),
			    RowFactory.create(0.0, Vectors.dense(2.0, 1.3, 1.0)),
			    RowFactory.create(1.0, Vectors.dense(0.0, 1.2, -0.5))
			);*/
		/*List<Row> dataTraining= JavaSparkContext.fromSparkContext(sparkSession.sparkContext()).textFile("src/test/resources/datafile.csv")
				.map(x->{
					String[] lineArr=x.split(" ");
					double[] list=new double[lineArr.length-1];
					for(int i=1;i<lineArr.length-1;i++) {
						list[i]=Double.valueOf(lineArr[i+1]);
					}
					return RowFactory.create(Double.valueOf(lineArr[0]), Vectors.dense(list));
				}).collect();
		StructType schema = new StructType(new StructField[]{
		    new StructField("label", DataTypes.DoubleType, false, Metadata.empty()),
		    new StructField("features", new VectorUDT(), false, Metadata.empty())
		});
		Dataset<Row> training = sparkSession.createDataFrame(dataTraining, schema);*/
		StructType schema = new StructType(new StructField[]{
			    new StructField("label", DataTypes.DoubleType, false, Metadata.empty()),
			    new StructField("features", new VectorUDT(), false, Metadata.empty())
			});
		DataFile dataFile=new DataFile();
		dataFile.setName("data");
		dataFile.setPath("src/test/resources/datafile.csv");
		dataFile.setDelim(" ");
		List<FieldInfo> fieldInfos=new ArrayList<FieldInfo>();
		FieldInfo label=new FieldInfo();
		label.setName("label");
		label.setDataType("double");
		label.setIndex(0);
		fieldInfos.add(label);
		FieldInfo col1=new FieldInfo();
		col1.setName("features");
		col1.setDataType("double");
		col1.setIndex(-1);
		col1.setStartIndex(1);
		col1.setEndIndex(3);
		fieldInfos.add(col1);
		dataFile.setFieldInfos(fieldInfos);
		Dataset<Row> training=SparkDataFileConverter.convertToDataFrame(dataFile, JavaSparkContext.fromSparkContext(sparkSession.sparkContext()));
		int num=100;
		Class clazz=Class.forName(algorithmDesc.getClassName());
		Method method=clazz.getMethod("fit", Dataset.class);
		Map<String, ParameterDesc> parameterDescs=algorithmDesc.getParameterDescs();
		Object object=clazz.newInstance();
		for(Map.Entry<String, ParameterDesc> entry : parameterDescs.entrySet()) {
			Method method1=null;
			Object param=null;
			if(entry.getValue().getValueType()==ParameterValueType.INT) {
				method1=clazz.getMethod("set"+entry.getValue().getName().substring(0, 1).toUpperCase()+entry.getValue().getName().substring(1), int.class);
				param=Integer.valueOf(entry.getValue().getValue());
			}
			if(entry.getValue().getValueType()==ParameterValueType.DOUBLE) {
				method1=clazz.getMethod("set"+entry.getValue().getName().substring(0, 1).toUpperCase()+entry.getValue().getName().substring(1), double.class);
				param=Double.valueOf(entry.getValue().getValue());
			}
			method1.invoke(clazz.newInstance(), param);
			
		}
		Object model= method.invoke(object, training);
		List<Row> dataTest = Arrays.asList(
			    RowFactory.create(1.0, Vectors.dense(-1.0, 1.5, 1.3)),
			    RowFactory.create(0.0, Vectors.dense(3.0, 2.0, -0.1)),
			    RowFactory.create(1.0, Vectors.dense(0.0, 2.2, -1.5))
			);
		Dataset<Row> test = sparkSession.createDataFrame(dataTest, schema);
		Method t=model.getClass().getMethod("transform", Dataset.class);
		Dataset<Row> results = (Dataset<Row>) t.invoke(model, test);
		for (Row r: results.collectAsList()) {
			  System.out.println("(" + r.get(0) + ", " + r.get(1) + ") -> prob=" + r.get(2)
			   + ", prediction=" + r.get(3));
		}
	}
}
