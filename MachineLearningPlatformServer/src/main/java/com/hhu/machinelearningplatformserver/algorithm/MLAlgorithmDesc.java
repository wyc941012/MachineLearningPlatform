package com.hhu.machinelearningplatformserver.algorithm;

import java.util.Map;

/**
 * spark mllib 算法
 *
 * @author hayes, @create 2017-12-11 16:57
 **/
public class MLAlgorithmDesc {

	private int id;
    /**
     * 算法名字
     */
    private String name;

    /**
     * 对外展示的名字
     */
    private String showName;

    /**
     * 算法完整类名
     */
    private String className;

    /**
     * 算法类型
     */
    private ComponentType componentsType;

    /**
     * 用途分类
     */
    private UsageType usageType;

    /**
     * 算法参数
     */
    private Map<String, ParameterDesc> parameterDescs;
    
    public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShowName() {
        return showName;
    }

    public void setShowName(String showName) {
        this.showName = showName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public ComponentType getComponentsType() {
        return componentsType;
    }

    public void setComponentsType(ComponentType componentsType) {
        this.componentsType = componentsType;
    }

    public UsageType getUsageType() {
        return usageType;
    }

    public void setUsageType(UsageType usageType) {
        this.usageType = usageType;
    }

    public Map<String, ParameterDesc> getParameterDescs() {
        return parameterDescs;
    }

    public void setParameterDescs(Map<String, ParameterDesc> parameterDescs) {
        this.parameterDescs = parameterDescs;
    }
}
