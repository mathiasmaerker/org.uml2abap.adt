package org.uml2abap.adt.data;

public class ClassMetaData {
	private ClassSource classSource;
	private String masterLanguage;
	private String masterSystem;
	private String className;
	private String packageAssignment;
	private String language;
	private String author;
	private String type;
	
	
	
	public ClassMetaData() {
		super();
	}
	
	
	public ClassMetaData(ClassSource classSource, String masterLanguage,
			String masterSystem, String className, String packageAssignment,
			String language, String author) {
		super();
		this.classSource = classSource;
		this.masterLanguage = masterLanguage;
		this.masterSystem = masterSystem;
		this.className = className;
		this.packageAssignment = packageAssignment;
		this.language = language;
		this.author = author;
	}


	public ClassSource getClassSource() {
		return classSource;
	}
	public void setClassSource(ClassSource classSource) {
		this.classSource = classSource;
	}
	public String getMasterLanguage() {
		return masterLanguage;
	}
	public void setMasterLanguage(String masterLanguage) {
		this.masterLanguage = masterLanguage;
	}
	public String getMasterSystem() {
		return masterSystem;
	}
	public void setMasterSystem(String masterSystem) {
		this.masterSystem = masterSystem;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getPackageAssignment() {
		return packageAssignment;
	}
	public void setPackageAssignment(String packageAssignment) {
		this.packageAssignment = packageAssignment;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}


	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}
	
	
	

}
