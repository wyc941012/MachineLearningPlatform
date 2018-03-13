package com.hhu.machinelearningplatformserver.algorithm;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hhu.machinelearningplatformserver.common.HBaseUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

/**
 * 从HBase中读取算法描述
 *
 * @author hayes, @create 2017-12-12 14:43
 **/
public class MLAlgorithmLoader {

    private static Map<Integer, MLAlgorithmDesc> mlAlgos = new HashMap<>();

    private static IOException err;

    @SuppressWarnings("unchecked")
	public static void init() {
    	HBaseUtil hBaseUtil=HBaseUtil.getInstance();
    	try {
			hBaseUtil.connection();
			List<Result> results=hBaseUtil.getAllData("algorithm");
			for(Result result : results) {
				int id=Bytes.toInt(result.getRow());
				String name=Bytes.toString(result.getValue(Bytes.toBytes("info"), Bytes.toBytes("name")));
				String showName=Bytes.toString(result.getValue(Bytes.toBytes("info"), Bytes.toBytes("showName")));
				String className=Bytes.toString(result.getValue(Bytes.toBytes("info"), Bytes.toBytes("className")));
				String componentsType=Bytes.toString(result.getValue(Bytes.toBytes("info"), Bytes.toBytes("componentsType")));
				String usageType=Bytes.toString(result.getValue(Bytes.toBytes("info"), Bytes.toBytes("usageType")));
				String parameterDescsMap=Bytes.toString(result.getValue(Bytes.toBytes("info"), Bytes.toBytes("parameterDescs")));
				MLAlgorithmDesc mlAlgorithmDesc=new MLAlgorithmDesc();
				mlAlgorithmDesc.setId(id);
				mlAlgorithmDesc.setName(name);
				mlAlgorithmDesc.setShowName(showName);
				mlAlgorithmDesc.setClassName(className);
				mlAlgorithmDesc.setComponentsType(ComponentType.getComponentTypeByValue(componentsType));
				mlAlgorithmDesc.setUsageType(UsageType.getUsageTypeByValue(usageType));
				ObjectMapper objectMapper=new ObjectMapper();
				Map<String, ParameterDesc> parameterDescs=objectMapper.readValue(parameterDescsMap, Map.class);
				mlAlgorithmDesc.setParameterDescs(parameterDescs);
				mlAlgos.put(id, mlAlgorithmDesc);
			}
			hBaseUtil.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    }

    public static Map<Integer, MLAlgorithmDesc> getAll() throws IOException {
        if (err == null) {
            return mlAlgos;
        }

        throw err;
    }

    public static MLAlgorithmDesc getMLAlgorithmDesc(int id) throws IOException {
        if (err == null) {
            return mlAlgos.get(id);
        }

        throw err;
    }


}
