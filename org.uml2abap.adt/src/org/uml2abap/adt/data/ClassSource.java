package org.uml2abap.adt.data;

public class ClassSource {
	private  String sourceDefinition;
	private  String sourceImplementation;

	public ClassSource() {
		super();
	}

	public String getSourceImplementation() {
		return sourceImplementation;
	}

	public String getSourceDefinition() {
		return sourceDefinition;
	}

	public void setSourceDefinition(String sourceDefinition) {
		this.sourceDefinition = sourceDefinition;
	}

	public void setSourceImplementation(String sourceImplementation) {
		this.sourceImplementation = sourceImplementation;
	}
}
